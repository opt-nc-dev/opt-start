import { Component, OnInit, AfterViewInit, Renderer, ElementRef } from '@angular/core';
import { NgbActiveModal } from '@ng-bootstrap/ng-bootstrap';
import { Router } from '@angular/router';
import { JhiLanguageService, JhiEventManager } from 'ng-jhipster';

import { LoginService } from './login.service';
import { StateStorageService } from '../auth/state-storage.service';
import { Principal } from '../auth/principal.service';

@Component({
    selector: 'jhi-login',
    templateUrl: './login.component.html'
})
export class JhiLoginComponent implements OnInit, AfterViewInit {
    authenticationError: boolean;
    password: string;
    rememberMe: boolean;
    username: string;
    credentials: any;

    constructor(
        private eventManager: JhiEventManager,
        private languageService: JhiLanguageService,
        private loginService: LoginService,
        private stateStorageService: StateStorageService,
        private elementRef: ElementRef,
        private renderer: Renderer,
        private router: Router,
        private principal: Principal
    ) {
        this.credentials = {};
    }

    ngOnInit() {
        if (this.principal.isAuthenticated()) {
            this.router.navigate(['']);
        }
    }

    ngAfterViewInit() {
        this.renderer.invokeElementMethod(this.elementRef.nativeElement.querySelector('#username'), 'focus', []);
    }

    login() {
        this.loginService.login({
            username: this.username,
            password: this.password,
            rememberMe: this.rememberMe
        }).then(() => {
            this.authenticationError = false;

            if (this.router.url === '/register' || (/activate/.test(this.router.url)) ||
                this.router.url === '/finishReset' || this.router.url === '/requestReset') {
                this.router.navigate(['']);
            }

            this.eventManager.broadcast({
                name: 'authenticationSuccess',
                content: 'Sending Authentication Success'
            });

            // // previousState was set in the authExpiredInterceptor before being redirected to login modal.
            // // since login is succesful, go to stored previousState and clear previousState
            let redirect = this.stateStorageService.getUrl();
            redirect = redirect && redirect !== 'login' ? redirect : '';
            this.router.navigate([redirect]);
        }).catch(() => {
            this.authenticationError = true;
        });
    }
}
