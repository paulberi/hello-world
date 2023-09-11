import { APP_INITIALIZER, NgModule } from '@angular/core';
import { APOLLO_NAMED_OPTIONS, APOLLO_OPTIONS, ApolloModule } from 'apollo-angular';
import { ApolloClientOptions, InMemoryCache, ApolloLink, from } from '@apollo/client/core';
import { HttpLink } from 'apollo-angular/http';
import { environment } from '../environments/environment';
import { HttpClient } from '@angular/common/http';
import { onError } from '@apollo/client/link/error';
import { GraphQlModuleService, MMDefaultErrorMessage } from './shared/data-access/graphql-module.service';
import { timer } from 'rxjs';


let adminUrl: string;
let shopUrl: string;

export function initGraphQLModule(httpClient: HttpClient) {
  return () => {
    return httpClient
      .get(environment.ehandelUrls, { responseType: "json" })
      .toPromise()
      .then((response: any) => {
        adminUrl = response.admin + "?languageCode=sv";
        shopUrl = response.shop + "?languageCode=sv";
      });
  };
};

export function createShopApollo(httpLink: HttpLink, graphQlModuleService: GraphQlModuleService): ApolloClientOptions<any> {

  const httpShopLink = httpLink.create({
    uri: shopUrl,
    withCredentials: true,
  });

  const activityMiddleware = new ApolloLink((operation, forward) => {
    const vendureChannel = graphQlModuleService.getUser()?.vendureChannels[0];

    if (vendureChannel) {
      operation.setContext(({ headers = {} }) => ({
        headers: {
          ...headers,
          "vendure-token": vendureChannel
        }
      }));
    }

    return forward(operation);
  })

  const errorLink = onErrorResp('shop', graphQlModuleService)

  return {
    link: from([errorLink, activityMiddleware, httpShopLink]),
    cache: new InMemoryCache(),
  };
}

export function createAdminApollo(httpLink: HttpLink, graphQlModuleService: GraphQlModuleService): Record<string, ApolloClientOptions<any>> {

  const httpAdminLink = httpLink.create({
    uri: adminUrl,
    withCredentials: true
  });

  const errorLink = onErrorResp('admin', graphQlModuleService)

  return {
    adminApi: {
      name: 'admin',
      link: from([errorLink, httpAdminLink]),
      cache: new InMemoryCache(),
    }
  };
}

function onErrorResp(typeOfUser: string, graphQlModuleService: GraphQlModuleService) {
  const errLink = onError(({ graphQLErrors, networkError }) => {
    if (graphQLErrors) {
      graphQLErrors.map(async ({ message, path, extensions }) => {
        console.error(`Vendure: Message: ${message}, Path: ${path}`)
        if (extensions?.code === 'FORBIDDEN') {
          graphQlModuleService.notifyUserOnError("Din session har gått ut, vänligen ladda om sidan");
        };
      });
    };
    if (networkError) {
      console.log(`[Network error]: ${networkError?.message}`)
      const errorStatus = networkError["status"];
      switch (errorStatus) {
        case 400:
          graphQlModuleService.notifyUserOnError(MMDefaultErrorMessage.BadRequest)
          break;
        case 401:
          graphQlModuleService.notifyUserOnError(MMDefaultErrorMessage.Unauthorized)
          timer(5000).subscribe(() => {
            graphQlModuleService.logout();
            if (typeOfUser === 'admin') {
              graphQlModuleService.logoutAdminVendure();
            } else if (typeOfUser === 'shop') {
              graphQlModuleService.logoutShopVendure();
            }
          });
          break;
        case 403:
          graphQlModuleService.notifyUserOnError(MMDefaultErrorMessage.Forbidden)
          break;
        case 404:
          graphQlModuleService.notifyUserOnError(MMDefaultErrorMessage.NotFound)
          break;
        case 500:
          graphQlModuleService.notifyUserOnError(MMDefaultErrorMessage.InternalServerError)
          break;
        case 502:
          graphQlModuleService.notifyUserOnError(MMDefaultErrorMessage.BadGateway)
          break;
        case 503:
          graphQlModuleService.notifyUserOnError(MMDefaultErrorMessage.ServiceUnavailable)
          break;
        case 504:
          graphQlModuleService.notifyUserOnError(MMDefaultErrorMessage.GatewayTimeout)
          break;
        default:
          graphQlModuleService.notifyUserOnError(MMDefaultErrorMessage.Default)
      }
    };
  });
  return errLink;
};

@NgModule({
  imports: [
    ApolloModule
  ],
  providers: [
    {
      provide: APP_INITIALIZER,
      deps: [HttpClient],
      useFactory: initGraphQLModule,
      multi: true
    },
    {
      provide: APOLLO_OPTIONS,
      deps: [HttpLink, GraphQlModuleService],
      useFactory: createShopApollo
    },
    {
      provide: APOLLO_NAMED_OPTIONS,
      deps: [HttpLink, GraphQlModuleService],
      useFactory: createAdminApollo
    }
  ]
})
export class GraphQLModule { }
