import { Observable } from "rxjs";
import * as shapefile from "shpjs";

export function readFileContent(file: File): Observable<any> {
    if (file) {
        let fileReader: FileReader = new FileReader();
        fileReader.readAsArrayBuffer(file);
        return new Observable(observer => {
            fileReader.onloadend = () => {
                observer.next(fileReader.result);
                observer.complete();
            };
        });
    }
}

export function readFileContentAsText(file: File): Observable<any> {
    if (file) {
        let fileReader: FileReader = new FileReader();
        return new Observable(observer => {
            fileReader.onload = () => {
                observer.next(fileReader.result)
            }
            fileReader.onerror = () => {
                observer.error(fileReader.error)
            }
            fileReader.onloadend = () => {
                observer.complete()
            }
            fileReader.readAsText(file);
        });
    }
}

//Tillfällig lösning att konvertera till geojson
//När det finns en annan lösning kom ihåg att ta bort biblioteket
export function convertShapeFileToGeoJSON(shapeFile: ArrayBuffer) {
    if (shapeFile) {
        return shapefile(shapeFile).then(geojson => {
            return geojson;
        })
    }
}