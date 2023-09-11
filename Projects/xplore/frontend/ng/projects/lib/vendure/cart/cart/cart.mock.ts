import { DisplayAttribute, OrderLine } from "../../../ui/order-line/order-line.component";

export const mockAttributes: DisplayAttribute[] = [
    { displayName: "Produkt", value: "Fastighetskartan vektor" },
    { displayName: "Kommun", value: "Umeå" },
    { displayName: "Referenssystem", value: "SWEREF 99 TM" },
    { displayName: "Leveransformat", value: "TIFF" }
];

export const cartItems: OrderLine[] = [
    { orderLineId: "1", attributes: mockAttributes, linePrice: 100, vat: 25, currency: "kr" },
    { orderLineId: "2", attributes: mockAttributes, linePrice: 100, vat: 25, currency: "kr" }
]