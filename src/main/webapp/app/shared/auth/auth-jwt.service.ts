import { Injectable } from '@angular/core';
import { Http, Response, ResponseOptions, RequestOptions, Headers, URLSearchParams } from '@angular/http';
import { Observable } from 'rxjs/Rx';
import { LocalStorageService, SessionStorageService } from 'ng2-webstorage';

import { ConfigurationService } from '../configuration/configuration.service';

@Injectable()
export class AuthServerProvider {
    constructor(
        private http: Http,
        private $localStorage: LocalStorageService,
        private $sessionStorage: SessionStorageService,
        private configurationService: ConfigurationService
    ) {}

    getToken() {
        return this.$localStorage.retrieve('authenticationToken') || this.$sessionStorage.retrieve('authenticationToken');
    }

    login(credentials): Observable<any> {

        const data = {
            username: credentials.username,
            password: credentials.password,
            rememberMe: credentials.rememberMe
        };

        if (this.configurationService.get('security-type') === 'local') {
            this.storeAuthenticationToken(credentials.username, credentials.rememberMe);
            return Observable.of(new Response(new ResponseOptions( { body: credentials.username })));
        } else {
            const url = this.configurationService.get('auth-url');
            const options: RequestOptions = new RequestOptions();
            options.withCredentials = true;
            return this.http.post(url, data, options).map(authenticateSuccess.bind(this));
            // return this.http.get(url, options).map(authenticateSuccess.bind(this));
        }

        function authenticateSuccess(resp) {
            const bearerToken = resp.headers.get('Authorization');
            if (bearerToken && bearerToken.slice(0, 7) === 'Bearer ') {
                const jwt = bearerToken.slice(7, bearerToken.length);
                this.storeAuthenticationToken(jwt, credentials.rememberMe);
                return jwt;
            }
        }
    }

    loginWithToken(jwt, rememberMe) {
        if (jwt) {
            this.storeAuthenticationToken(jwt, rememberMe);
            return Promise.resolve(jwt);
        } else {
            return Promise.reject('auth-jwt-service Promise reject'); // Put appropriate error message here
        }
    }

    storeAuthenticationToken(jwt, rememberMe) {
        if (rememberMe) {
            this.$localStorage.store('authenticationToken', jwt);
        } else {
            this.$sessionStorage.store('authenticationToken', jwt);
        }
    }

    logout(): Observable<any> {
        return new Observable((observer) => {
            this.$localStorage.clear('authenticationToken');
            this.$sessionStorage.clear('authenticationToken');
            observer.complete();
        });
    }
}
