import { Injectable, OnDestroy } from "@angular/core";
import { interval, Observable, Subject } from "rxjs";
import { debounce, distinctUntilChanged } from "rxjs/operators";
@Injectable()
export class XpUiSearchFieldPresenter implements OnDestroy {
  debounceTime = 500;

  private searchQuery = new Subject<string>();
  searchQuery$: Observable<string>;

  constructor() {
    this.searchQuery$ = this.searchQuery.pipe(
      debounce(() => interval(this.debounceTime)),
      distinctUntilChanged()
    );
  }

  setDebounceTime(time: number){
    this.debounceTime = time;
  }

  ngOnDestroy(): void {
    this.searchQuery.complete();
  }

  search(query: string) {
    this.searchQuery.next(query);
  }
}
