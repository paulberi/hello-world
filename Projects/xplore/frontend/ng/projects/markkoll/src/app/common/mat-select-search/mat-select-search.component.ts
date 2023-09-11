import { AfterViewInit, Component, ContentChild, EventEmitter, Input, OnInit, Output, TemplateRef, ViewChild } from '@angular/core';
import { CommonModule } from '@angular/common';
import { NgxMatSelectSearchModule } from "ngx-mat-select-search";
import { FormControl, FormsModule, ReactiveFormsModule } from "@angular/forms";
import { MatSelect, MatSelectModule } from "@angular/material/select";
import { ReplaySubject } from "rxjs";
import { MatFormFieldModule } from "@angular/material/form-field";
import { take } from "rxjs/operators";

@Component({
  selector: 'mk-mat-select-search',
  standalone: true,
  imports: [
    CommonModule,
    FormsModule,
    ReactiveFormsModule,
    NgxMatSelectSearchModule,
    MatFormFieldModule,
    MatSelectModule
  ],
  templateUrl: './mat-select-search.component.html',
  styleUrls: ['./mat-select-search.component.scss']
})
export class MkMatSelectSearchComponent implements OnInit, AfterViewInit {

  @Input() options: any[] = [];
  @Input() value: any;
  @Input() compareWith: (o1: any, o2: any) => boolean = (o1, o2) => o1 === o2;

  @Input() label: string = "Sök";
  @Input() searchLabel: string = "Sök";
  @Input() noEntriesFoundLabel: string = "Inga träffar";

  @Output() selectionChange = new EventEmitter<any>();

  optionCtrl = new FormControl<any>(null);
  filterCtrl = new FormControl<String>('');
  filtered = new ReplaySubject<any[]>(1);

  @ViewChild('singleSelect', { static: true }) singleSelect: MatSelect;

  @ContentChild(TemplateRef) templateRef: TemplateRef<any>;
  @ContentChild('Trigger') triggerRef: TemplateRef<any>;

  constructor() { }

  ngOnInit(): void {
    this.optionCtrl.setValue(this.value);

    this.filtered.next(this.options.slice());
    this.filterCtrl.valueChanges.subscribe(() => {
      this.filterOptions();
    });
    this.optionCtrl.valueChanges.subscribe(() => {
      this.value = this.optionCtrl.value;
      this.selectionChange.emit(this.optionCtrl.value);
    });
  }

  ngAfterViewInit(): void {
    this.filtered
    .pipe(take(1))
    .subscribe(() => {
      this.singleSelect.compareWith = this.compareWith;
    });
  }

  private filterOptions() {
    if (!this.options) {
      return;
    }

    let search = this.filterCtrl.value;
    if (!search) {
      this.filtered.next(this.options.slice());
      return;
    }
    search = search.toLowerCase();

    this.filtered.next(
      this.options.filter(opt => opt.name.toLowerCase().indexOf(search) > -1)
    );
  }
}
