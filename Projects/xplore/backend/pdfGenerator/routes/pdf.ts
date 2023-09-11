import express from "express";
import fs from "fs";
import path from "path";
import Puppeteer, {Browser, ConsoleMessage, JSHandle, Page, PaperFormat} from "puppeteer";
import Logger from "../config/logging";
import {apm} from "../config/logging";

/* Customize the PDF output to your hearts delight. These options are passed on to Puppeteer,
so refer to the Puppeteer API documentaion for further details. */
interface PdfOptions {
    // https://github.com/puppeteer/puppeteer/blob/v3.3.0/docs/api.md#pagesetviewportviewport
    viewport: {
        width: number,
        height: number,
    };

    // https://github.com/puppeteer/puppeteer/blob/v3.3.0/docs/api.md#pagepdfoptions
    printBackground: boolean;
    margin: {
        top?: string | number,
        right?: string | number,
        bottom?: string | number,
        left?: string | number,
    };
    format: PaperFormat;
    landscape: boolean;

    // Timeout in milliseconds. Set to 0 to remove timeout. Default 30000ms.
    timeout?: number;
}

const pdfOptionsDefaults: PdfOptions = {
    viewport: {
        width: 1920,
        height: 1080,
    },
    printBackground: true,
    margin: {
    },
    format: "a4",
    landscape: false,
    timeout: 30000,
};

const AUTH_NONE = "none";
const AUTH_BASIC = "basic";
const AUTH_URL = "url";
type AuthType = typeof AUTH_NONE | typeof AUTH_BASIC | typeof AUTH_URL;

// Authentication might be required for the requested page. This is where you stuff the credentials.
interface AuthOptions {
    authType: AuthType;
    password?: string;
    username?: string;
    authUrl?: string;
}

let browser = null as Browser;
export let router = express.Router();

export async function initBrowser(): Promise<void> {
    let args = ["--no-sandbox"];
    if (process.env.PROXY) {
        args.push(` --proxy-server=${process.env.PROXY}`)
        Logger.debug("using proxy: " + process.env.PROXY);
    }

    Logger.debug("args: [" + args + "]");
    browser = await Puppeteer.launch({args: args});
}

router.post("/", async (req, res) => {
    Logger.debug("req.body: ", req.body);

    // Få med trace ids i loggarna hela vägen
    let currentTraceIds;
    if (apm != null) {
        currentTraceIds = {
            span: {id: apm.currentTraceIds["span.id"]},
            trace: {id: apm.currentTraceIds["trace.id"]},
            transaction: {id: apm.currentTraceIds["transaction.id"]},
        };
    } else {
        currentTraceIds = "";
    }

    try {
        const url = getUrl(req.body.url);
        const pdfOptions = req.body.pdfOptions as PdfOptions;
        const authOptions = req.body.authOptions as AuthOptions;

        const pdfName = new Date().getTime() + ".pdf";
        const pdfPath = path.join(__dirname, pdfName);

        const pdf = await pdfFromUrl(url, pdfPath, authOptions, pdfOptions, currentTraceIds);

        res.status(201);
        res.set({
            "Content-Type": "application/pdf",
            "Content-Length": pdf.length,
        });
        res.sendFile(pdfPath, function() {
            fs.unlinkSync(pdfPath);
        });
    } catch (error) {
        Logger.error(error, currentTraceIds);
        res.status(500).end(error.message);
    }
});

async function pdfFromUrl(
    url: string,
    pdfPath: string,
    authOptions?: AuthOptions,
    pdfOptions?: PdfOptions,
    currentTraceIds?: any,
): Promise<Buffer> {
    if (!browser) {
        throw new Error("Chrome browser not initialized.");
    }

    const page = await browser.newPage();

    // make args accessible
    const describe = (jsHandle: JSHandle) => {
        return jsHandle.executionContext().evaluate((obj) => {
            // serialize |obj| however you want
            return `OBJ: ${typeof obj}, ${obj}`;
        }, jsHandle as JSHandle);
    };

    // listen to browser console there
    page.on("console", async (msg: ConsoleMessage) => {
        const type = msg.type().substr(0, 3).toUpperCase();

        const args = await Promise.all(msg.args().map((arg) => describe(arg)));

        let text = "";

        for (let i = 0; i < args.length; ++i) {
            text += `[${i}] ${args[i]} `;
        }
        Logger.info(`CONSOLE.${type}: ${msg.text()}\n${text} `, currentTraceIds);
    });

    page.on("pageerror", (error) => {
        Logger.error("Console error: " + error.message, currentTraceIds);
    });

    if (authOptions) {
        await authenticate(page, authOptions, currentTraceIds);
    }

    const mergedOptions = Object.assign({}, pdfOptionsDefaults, pdfOptions);
    const pdf = await pdfWithOptions(page, url, pdfPath, mergedOptions, currentTraceIds);

    await page.close();

    return pdf;
}

async function pdfWithOptions(
    page: Page,
    url: string,
    pdfPath: string,
    pdfOptions: PdfOptions,
    currentTraceIds: any): Promise<Buffer> {
    Logger.debug("pdfOptions: ", pdfOptions, currentTraceIds);
    await page.setViewport({
        width: pdfOptions.viewport.width,
        height: pdfOptions.viewport.height,
    });

    await page.goto(url, {
        waitUntil: "networkidle0",
        timeout: pdfOptions.timeout,
    });

    return await page.pdf({
        path: pdfPath,
        printBackground: pdfOptions.printBackground,
        margin: pdfOptions.margin,
        format: pdfOptions.format,
        landscape: pdfOptions.landscape,
    });
}

async function authenticate(page: Page, authOptions: AuthOptions, currentTraceIds: any): Promise<void> {
    Logger.debug("authUrl", authOptions.authUrl, currentTraceIds);
    switch (authOptions.authType) {
        case AUTH_URL:
            await page.goto(authOptions.authUrl, {waitUntil: "networkidle0"});
            break;
        case AUTH_BASIC:
            await page.authenticate({
                username: authOptions.username,
                password: authOptions.password,
            });
            await page.goto(authOptions.authUrl);
            break;
        case AUTH_NONE:
            break;
        default:
            throw new Error("Invalid authentication type: " + authOptions.authType);
    }
}

function getUrl(url: string): string {
    if (!url) {
        throw new Error("Missing required field Url");
    }

    return containsProtocol(url) ? url : "http://" + url;
}

function containsProtocol(addr: string): boolean {
    return addr.match(/^\w*:\/\//) ? true : false;
}
