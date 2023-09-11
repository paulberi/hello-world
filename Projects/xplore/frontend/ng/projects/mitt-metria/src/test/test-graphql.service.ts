import { Injectable } from "@angular/core";
import { NetworkStatus, WatchQueryFetchPolicy, InternalRefetchQueriesInclude } from "@apollo/client/core";
import { Apollo } from "apollo-angular";
import { DocumentNode } from "graphql";
import { Observable } from "rxjs";
import { filter, map } from "rxjs/operators";

@Injectable({
  providedIn: "root"
})
export class TestGraphqlService {

  private readonly context = {
    headers: {}
  };

  constructor(private apollo: Apollo) { }

  query<T = any, V = any>(query: DocumentNode, variables?: V, fetchPolicy?: WatchQueryFetchPolicy): Observable<T> {
    return this.apollo.watchQuery<T, V>({
      query,
      variables,
      context: this.context,
      fetchPolicy: fetchPolicy || "cache-first"
    }).valueChanges.pipe(
      filter(result => result.networkStatus === NetworkStatus.ready),
      map(response => response.data));
  }

  mutate<T = any, V = any>(mutation: DocumentNode, variables?: V, refetchQueries?: InternalRefetchQueriesInclude): Observable<T> {
    return this.apollo.mutate<T, V>({
      mutation,
      variables,
      refetchQueries,
      context: this.context
    }).pipe(map(response => response.data as T));
  }
}
