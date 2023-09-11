import { Component, EventEmitter, Input, Output } from '@angular/core';
import { BehaviorSubject, Observable } from 'rxjs';
import { IntrangVerktygEnum } from "../verktyg/intrang-verktyg-enum";

export interface MkIntrangsVerktygslada {
  get toolChange$(): Observable<IntrangVerktygEnum>;
  get isOpen$(): Observable<boolean>;

  setOpen(open: boolean);
  selectTool(tool: IntrangVerktygEnum);
}

export class VerktygsladaButton {
  id: IntrangVerktygEnum;
  icon: string;
  iconText: string;
}

@Component({
  selector: 'mk-intrang-verktygslada',
  templateUrl: './verktygslada.component.html',
  styleUrls: ['./verktygslada.component.scss']
})
export class MkVerktygsladaComponent implements MkIntrangsVerktygslada {
  @Input() isOpen = false;
  @Input() buttons: VerktygsladaButton[] = []

  @Output() toolChange = new EventEmitter<IntrangVerktygEnum>();
  @Output() openChange = new EventEmitter<boolean>();

  private selectedToolSubject: BehaviorSubject<IntrangVerktygEnum> = new BehaviorSubject(IntrangVerktygEnum.SELECT);
  private isOpenSubject: BehaviorSubject<boolean> = new BehaviorSubject(false);

  selectedTool: IntrangVerktygEnum = IntrangVerktygEnum.SELECT;

  readonly ToolboxTool = IntrangVerktygEnum;
  constructor() {
    this.selectedToolSubject.subscribe(val => this.toolChange.emit(val));
    this.isOpenSubject.subscribe(val => this.isOpen = val);
  }

  get toolChange$(): Observable<IntrangVerktygEnum> {
    return this.toolChange.asObservable();
  }

  get isOpen$(): Observable<boolean> {
    return this.isOpenSubject.asObservable();
  }

  selectTool(tool: IntrangVerktygEnum) {
    this.selectedTool = tool;
    this.selectedToolSubject.next(tool);
  }

  setOpen(isOpen: boolean) {
    this.isOpenSubject.next(isOpen);
  }

  closeToolbox() {
    this.isOpenSubject.next(false);
  }
}
