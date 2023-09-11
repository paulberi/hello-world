import winston from "winston";

const ecsFormat = require("@elastic/ecs-winston-format");

let format;
export let apm;

if ("ELASTIC_APM_SERVER_URL" in process.env) {
    format = ecsFormat({ convertReqRes: true });

    // eslint-disable-next-line @typescript-eslint/no-var-requires
    apm = require("elastic-apm-node").start({
        serviceName: "pdfGenerator",
    });
} else {
    format = winston.format.simple();
}


const Logger = winston.createLogger({
    level: "debug",
    format: format,
    transports: [
        new winston.transports.Console(),
    ],
});

export default Logger;
