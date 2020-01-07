import { Injectable } from '@angular/core';
import { Http } from '@angular/http';

@Injectable()
export class ConfigurationService {

    config: any;

    constructor(
        private http: Http,
    ) {}

    load() {
        this.http.get('/management/configuration').subscribe((res) =>
            this.config = res.json()
        );
    }

    get(key: string) {
        return this.config[key];
    }
}
