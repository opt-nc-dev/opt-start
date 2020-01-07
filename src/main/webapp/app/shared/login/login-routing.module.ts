import { NgModule } from '@angular/core';
import { RouterModule } from '@angular/router';

import { LOGIN_ROUTE } from './login.route';

const LOGIN_ROUTES = [
    LOGIN_ROUTE
];

@NgModule({
  imports: [
    RouterModule.forRoot(LOGIN_ROUTES, { useHash: true })
  ],
  exports: [
    RouterModule
  ]
})
export class LoginRoutingModule {}
