import { Bild } from "./bild";
import { SamradStatus } from "./samradStatus";

export class Samrad {
    samradId?: string;
    namn?: string;
    ingress?: string;
    brodtext?: string;
    bildList?: Bild[];
    slug?: string;
    status?: SamradStatus;
}
