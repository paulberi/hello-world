import { Injectable } from '@angular/core';
import { BehaviorSubject, Observable, Subject } from 'rxjs';
import { tap } from 'rxjs/operators';
import { InstallningarApiService, NisKalla } from '../../../../../generated/markkoll-api';

@Injectable({
  providedIn: 'root'
})
export class MkInstallningarService {

  nisKalla$: Subject<NisKalla> = new Subject();

  constructor(private installningarApi: InstallningarApiService) {}

  getNisKalla(kundId: string): Observable<NisKalla> {
    return this.installningarApi.getNisKalla(kundId).pipe(tap(res => this.nisKalla$.next(res)));
  }

  updateNisKalla(kundId: string, nisKalla: NisKalla): Observable<NisKalla> {
    return this.installningarApi.updateNisKalla(kundId, nisKalla).pipe(tap(res => this.nisKalla$.next(res)));
  }
}
