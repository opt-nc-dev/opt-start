import {Route} from '@angular/router';

import {JhiLoginComponent} from './login.component';

export const LOGIN_ROUTE: Route = {
    path: 'login',
    component: JhiLoginComponent,
    data: {
        authorities: [],
        pageTitle: 'home.title'
    }
};
