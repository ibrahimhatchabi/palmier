import { Component, OnInit, ElementRef, Renderer, AfterViewInit } from '@angular/core';
import { JhiEventManager } from 'ng-jhipster';

import { Account } from 'app/core/user/account.model';
import { LoginService } from 'app/core/login/login.service';
import { StateStorageService } from 'app/core/auth/state-storage.service';
import { Router } from '@angular/router';
import { FormBuilder } from '@angular/forms';
import { AccountService } from 'app/core/auth/account.service';

@Component({
  selector: 'jhi-home',
  templateUrl: './home.component.html',
  styleUrls: ['home.scss']
})
export class HomeComponent implements OnInit, AfterViewInit {
  account: Account;
  // authSubscription: Subscription;
  authenticationError: boolean;

  loginForm = this.fb.group({
    username: [''],
    password: [''],
    rememberMe: [false]
  });

  constructor(
    private eventManager: JhiEventManager,
    private loginService: LoginService,
    private stateStorageService: StateStorageService,
    private elementRef: ElementRef,
    private renderer: Renderer,
    private router: Router,
    private fb: FormBuilder,
    private accountService: AccountService
  ) {}

  ngAfterViewInit() {
    setTimeout(() => this.renderer.invokeElementMethod(this.elementRef.nativeElement.querySelector('#username'), 'focus', []), 0);
  }

  cancel() {
    /* this.authenticationError = false;
    this.loginForm.patchValue({
      username: '',
      password: ''
    });
    this.activeModal.dismiss('cancel');*/
  }

  login() {
    this.loginService
      .login({
        username: this.loginForm.get('username').value,
        password: this.loginForm.get('password').value,
        rememberMe: this.loginForm.get('rememberMe').value
      })
      .subscribe(
        () => {
          this.authenticationError = false;
          // this.activeModal.dismiss('login success');
          if (
            this.router.url === '/account/register' ||
            this.router.url.startsWith('/account/activate') ||
            this.router.url.startsWith('/account/reset/')
          ) {
            this.router.navigate(['']);
          }

          this.eventManager.broadcast({
            name: 'authenticationSuccess',
            content: 'Sending Authentication Success'
          });

          // previousState was set in the authExpiredInterceptor before being redirected to login modal.
          // since login is successful, go to stored previousState and clear previousState
          const redirect = this.stateStorageService.getUrl();
          if (redirect) {
            this.stateStorageService.storeUrl(null);
            this.router.navigateByUrl(redirect);
          }
        },
        () => (this.authenticationError = true)
      );
  }

  goToCommandes() {
    this.router.navigate(['/commande']);
  }

  register() {
    /* this.activeModal.dismiss('to state register');
    this.router.navigate(['/account/register']);*/
  }

  requestResetPassword() {
    /* this.activeModal.dismiss('to state requestReset');
    this.router.navigate(['/account/reset', 'request']);*/
  }

  ngOnInit() {
    /* this.accountService.identity().subscribe((account: Account) => {
      this.account = account;
    });*/
    // this.registerAuthenticationSuccess();
  }

  /* registerAuthenticationSuccess() {
    this.authSubscription = this.eventManager.subscribe('authenticationSuccess', message => {
      this.accountService.identity().subscribe(account => {
        this.account = account;
      });
    });
  }*/

  isAuthenticated() {
    return this.accountService.isAuthenticated();
  }

  /* login() {
    this.modalRef = this.loginModalService.open();
  }*/

  /* ngOnDestroy() {
    if (this.authSubscription) {
      this.eventManager.destroy(this.authSubscription);
    }
  }*/
}
