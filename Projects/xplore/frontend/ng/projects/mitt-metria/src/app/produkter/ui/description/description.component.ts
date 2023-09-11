import { Component, Input, OnInit } from '@angular/core';

@Component({
  selector: 'mm-description',
  templateUrl: './description.component.html',
  styleUrls: ['./description.component.scss']
})
export class DescriptionComponent implements OnInit {
  @Input() content: string;
  @Input() limit: number;
  @Input() innerHtml: boolean = false;

  // Om innerHtml används går det att skicka in ett id för styling som sätts i styles.scss
  @Input() styling: string;

  // Om limit överskrids kan man lägga till en knapp för att läsa mer/mindre
  @Input() showReadMoreAndLessButton: boolean = false;
  @Input() readMoreText: string = "Läs mer";
  @Input() readLessText: string = "Läs mindre";

  toggleReadMore: boolean;
  nonEditedContent: string;

  constructor() { }

  ngOnInit() {
    this.nonEditedContent = this.content;

    if (this.content.length > this.limit) {
      this.content = this.formatContent(this.content);
    }
  }

  toggleContent() {
    this.toggleReadMore = !this.toggleReadMore;
    this.content = this.toggleReadMore ? this.nonEditedContent : this.formatContent(this.content);
  }

  formatContent(content: string) {
    return `${content.substring(0, this.limit)}...`;
  }
}