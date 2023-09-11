# PDFGenerator
Tjänst för generering av PDF-dokument från en URL.

## Installation
Tjänsten är en Node.js-app, och körs sedvanligt genom att installera beroenden
```$ npm install```

och sedan starta upp servern.
```$ npm start```

## Användning
Tjänsten anropas med en POST till http://localhost:3000/pdf/.
Bifoga URL och eventuella inställningar, och få en PDF tillbaka i andra änden.

### JSON payload:
```
{
    // Efterfrågad URL.
    url : string

    // Autentiseringsinställningar för den URL du vill generera en PDF utav.
    // Optional
    authOptions : {
        authType : "basic" | "none",
        password : string,
        username : string,
        authUrl  : string
    }

    // Inställningar från Puppeteer API för pdf-generering (https://github.com/puppeteer/puppeteer).
    // Optional
    pdfOptions: {
        // https://github.com/puppeteer/puppeteer/blob/v3.3.0/docs/api.md#pagesetviewportviewport
        viewport: {
            width: number,
            height: number
        },

        // https://github.com/puppeteer/puppeteer/blob/v3.3.0/docs/api.md#pagepdfoptions
        printBackground: boolean,
        margin: {
            top?: string | number,
            right?: string | number,
            bottom?: string | number,
            left?: string | number,
        },
        format: PDFFormat,
        landscape: boolean,

        // Timeout in milliseconds. Set to 0 to remove timeout. Default 30000ms.
        timeout?: number
    }
}
```