import { createWriteStream, supported } from "streamsaver";
import {Injectable} from "@angular/core";
import {OAuthModuleConfig, OAuthService, OAuthStorage} from "angular-oauth2-oidc";


@Injectable({
  providedIn: "root"
})
export class HttpDownloadService {
  constructor(
    private oAuthStorage: OAuthStorage,
    private oAuthService: OAuthService,
    private oAuthModuleConfig: OAuthModuleConfig
  ) {
  }

  /**
   * Ladda ner en fil från en URL där vi explicit vill skicka med en en JWT och inte kan
   * förlita på t.ex. cookies, exempelvis om nginx behöver läsa av din JWT.
   *
   * Lösningar som FileSaver har problemet att vi först måste ladda ner den önskade filen till minnet,
   * innan browsern kan be oss att spara ner den till disk.
   *
   * @param url                 URL med filen man vill ladda ner
   */
  public async httpDownload(url: string) {
    const headers = new Headers();

    if (this.oAuthModuleConfig?.resourceServer?.allowedUrls) {
      const urlToCheck = url.toLowerCase();
      const found = this.oAuthModuleConfig.resourceServer.allowedUrls.find(u => urlToCheck.startsWith(u));

      if (!!found) {
        const token = sessionStorage.getItem("access_token");

        headers.set("Authorization", "Bearer " + token);
      }
    }

    const options = {headers};

    const response = await fetch(url, options);

    const contentDispo = response.headers.get("Content-Disposition");
    const fileName = contentDispo.match(/filename[^;=\n]*=((['"]).*?\2|[^;\n]*)/)[1];

    if (!supported) {
      window.WritableStream = WritableStream;
    }
    const filestream = createWriteStream(fileName);
    const writer = filestream.getWriter();

    if (response.body.pipeTo) {
      writer.releaseLock();
      return response.body.pipeTo(filestream);
    }

    const reader = response.body.getReader();

    const pump = () =>
      reader
        .read()
        .then(({value, done}) => (done ? writer.close() : writer.write(value).then(pump)));

    return pump();
  }
}
