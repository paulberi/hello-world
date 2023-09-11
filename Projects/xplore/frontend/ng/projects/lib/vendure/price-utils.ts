export function calculatePrice(price: number): number | string {
    if (price !== 0) {
        return (price / 100).toFixed(2);
    } else {
        return price;
    }
}

export function translateCurrencyCode(currency: string): string {
    switch (currency) {
        case "SEK":
            return "kr";
        default:
            return currency;
    }
}