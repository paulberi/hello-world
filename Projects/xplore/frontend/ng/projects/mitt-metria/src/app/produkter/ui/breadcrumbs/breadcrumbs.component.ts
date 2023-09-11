import { Component, Input } from "@angular/core";

export interface Breadcrumb {
  name: string;
  path: string;
}

@Component({
  selector: "mm-breadcrumbs",
  templateUrl: "./breadcrumbs.component.html",
  styleUrls: ["./breadcrumbs.component.scss"],
})
export class BreadcrumbsComponent {
  @Input() items: Breadcrumb[];
  @Input() strictRouterLink: boolean = true;
}