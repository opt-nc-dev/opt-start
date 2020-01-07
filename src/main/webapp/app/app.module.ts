import './vendor.ts';

import { APP_INITIALIZER, NgModule } from '@angular/core';
import { BrowserModule } from '@angular/platform-browser';
import { Ng2Webstorage } from 'ng2-webstorage';

import { PocSharedModule, UserRouteAccessService, ConfigurationService } from './shared';
import { PocHomeModule } from './home/home.module';
import { PocAdminModule } from './admin/admin.module';
import { PocEntityModule } from './entities/entity.module';

import { LayoutRoutingModule } from './layouts';
import { LoginRoutingModule } from './shared';
import { customHttpProvider } from './blocks/interceptor/http.provider';
import { PaginationConfig } from './blocks/config/uib-pagination.config';

import {
    JhiMainComponent,
    NavbarComponent,
    FooterComponent,
    ProfileService,
    PageRibbonComponent,
    ActiveMenuDirective,
    ErrorComponent
} from './layouts';

export function configServiceFactory(config: ConfigurationService) {
    return () => config.load();
}

@NgModule({
    imports: [
        BrowserModule,
        LayoutRoutingModule,
        LoginRoutingModule,
        Ng2Webstorage.forRoot({ prefix: 'jhi', separator: '-'}),
        PocSharedModule,
        PocHomeModule,
        PocAdminModule,
        PocEntityModule,
        // jhipster-needle-angular-add-module JHipster will add new module here
    ],
    declarations: [
        JhiMainComponent,
        NavbarComponent,
        ErrorComponent,
        PageRibbonComponent,
        ActiveMenuDirective,
        FooterComponent
    ],
    providers: [
        ProfileService,
        customHttpProvider(),
        PaginationConfig,
        UserRouteAccessService,
        ConfigurationService,
        { provide: APP_INITIALIZER,
            useFactory: configServiceFactory,
            deps: [ConfigurationService],
            multi: true }
    ],
    bootstrap: [ JhiMainComponent ]
})
export class PocAppModule {}
