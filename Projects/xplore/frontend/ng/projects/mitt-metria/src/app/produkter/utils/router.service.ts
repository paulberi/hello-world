import { Injectable } from "@angular/core";
import { ActivatedRoute, ParamMap, Router } from "@angular/router";
import { Observable } from "rxjs";
import { distinctUntilChanged, map, shareReplay, tap } from "rxjs/operators";

@Injectable({
    providedIn: "root"
})
export class RouterService {

    constructor(private router: Router) { }

    getQueryParam(param: string, route: ActivatedRoute): Observable<string> {
        return route.queryParamMap.pipe(
            map(pm => pm.get(param) || ''),
            distinctUntilChanged(),
            shareReplay(1),
        );
    }

    updateParam(key: string, param: string, route: ActivatedRoute) {
        this.router.navigate(['./'], {
            queryParams: { [key]: param },
            relativeTo: route,
            queryParamsHandling: 'merge',
        });
    }

    getArrayParam(param: string, route: ActivatedRoute): Observable<string[]> {
        return route.paramMap.pipe(
            map(pm => this.getRouteArrayParam(pm, param)),
            distinctUntilChanged((x, y) => x.toString() === y.toString()),
            tap(() => {
              //this.currentPage = 0;
            }),
            shareReplay(1),
          ); 
    }

    updateArrayParam(key: string, params: string[], route: ActivatedRoute) {
        this.router.navigate(['./', {
            [key]: params,
        }], {
            queryParamsHandling: 'merge',
            relativeTo: route,
            state: {
                noScroll: true,
            },
        });
    }

    private getRouteArrayParam(paramMap: ParamMap, paramName: string): string[] {
        const existing = paramMap.getAll(paramName);
        if (!existing) {
            return [];
        }
        let result = existing;
        if (existing.length === 1) {
            const value = existing[0];
            if (value.indexOf(',') > -1) {
                result = value.split(',');
            } else {
                result = [value];
            }
        }
        return result.filter(x => !!x);
    }
}