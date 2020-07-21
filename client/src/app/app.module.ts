import { BrowserModule } from '@angular/platform-browser';
import { NgModule } from '@angular/core';

import { AppComponent } from './app.component';
import {HttpClientModule, HttpParams} from '@angular/common/http';
import { LoginComponent } from './login/login.component';
import { AppRoutingModule } from './app-routing.module';
import { GoogleAuthenticatorComponent } from './component/google-authenticator/google-authenticator.component';
import {AuthenticatorService} from './service/authenticator.service';

@NgModule({
  declarations: [
    AppComponent,
    LoginComponent,
    GoogleAuthenticatorComponent,
  ],
  imports: [
    BrowserModule,
    HttpClientModule,
    AppRoutingModule,
  ],
  providers: [
    AuthenticatorService
  ],
  bootstrap: [AppComponent]
})
export class AppModule { }
