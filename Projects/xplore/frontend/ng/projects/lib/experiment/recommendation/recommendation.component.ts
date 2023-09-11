import { Component, EventEmitter, Input, Output } from "@angular/core";
import { TranslocoService } from "@ngneat/transloco";


export interface RecommendationItem {
  title: string;
  description: string;
  image: string;
}

/**
 * Experimentiell komponent för att visa olika typer av rekommendationer,
 * ex. ur Metrias produktkatalog. Innehållet för rekommendationen är mockad i den här
 * komponenten, men ska framöver hämtas från en backend-tjänst. 
 * Förbättringsförlag: Hantering av bilder ska inte vara hårdkodad med css.
 */

@Component({
  selector: "xp-recommendation-ui",
  templateUrl: "./recommendation.component.html",
  styleUrls: ["./recommendation.component.scss"]
})
export class XpRecommendationComponent {

  @Input() items: Array<RecommendationItem>;

  @Output() showMoreClick = new EventEmitter();

  @Output() showCatalogueClick = new EventEmitter();
 
  constructor(private translation: TranslocoService) {}
}
