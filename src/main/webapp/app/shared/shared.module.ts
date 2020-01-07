import { NgModule, CUSTOM_ELEMENTS_SCHEMA } from '@angular/core';
import { DatePipe } from '@angular/common';

import {
    PocSharedLibsModule,
    PocSharedCommonModule,
    CSRFService,
    AuthServerProvider,
    AccountService,
    StateStorageService,
    LoginService,
    Principal,
    HasAnyAuthorityDirective,
    JhiLoginComponent
} from './';

@NgModule({
    imports: [
        PocSharedLibsModule,
        PocSharedCommonModule
    ],
    declarations: [
        JhiLoginComponent,
        HasAnyAuthorityDirective
    ],
    providers: [
        LoginService,
        AccountService,
        StateStorageService,
        Principal,
        CSRFService,
        AuthServerProvider,
        DatePipe
    ],
    entryComponents: [JhiLoginComponent],
    exports: [
        PocSharedCommonModule,
        JhiLoginComponent,
        HasAnyAuthorityDirective,
        DatePipe
    ],
    schemas: [CUSTOM_ELEMENTS_SCHEMA]

})
export class PocSharedModule {}
