import { Pipe, PipeTransform } from "@angular/core";
import {HttpClient} from "@angular/common/http";
import {map} from "rxjs/operators";
import {DomSanitizer, SafeUrl} from "@angular/platform-browser";
import {Observable} from "rxjs";

/*
 Hämta bilder via angulars http-bibliotek så att interceptorer får en chans att användas.
 Tex:
 <img class="img" [src]="lagesbildUrl() | secureLoadImage | async" alt="Lägesbild på objekt">

 Inbygd gif som visas innan riktiga bilden hämtats för att undvika felmeddelanden.
 */
@Pipe({name: "secureLoadImage"})
export class SecureLoadImagePipe implements PipeTransform {
  constructor(private http: HttpClient,
              private domSanitizer: DomSanitizer) {}

  transform(url: string) {
    return new Observable<SafeUrl>((observer) => {
      observer.next("data:image/gif;base64,R0lGODlhAQABAAAAACH5BAEKAAEALAAAAAABAAEAAAICTAEAOw==");
      const {next, error} = observer;

      this.http.get(url, {responseType: "blob"})
        .subscribe(response => {
          observer.next(this.domSanitizer.bypassSecurityTrustUrl(URL.createObjectURL(response)));
      });
        // .pipe(map(e => this.domSanitizer.bypassSecurityTrustUrl(URL.createObjectURL(e))));

      return {unsubscribe() {  }};
    });
  }
}