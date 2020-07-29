import {BrowserModule} from '@angular/platform-browser';
import {NgModule} from '@angular/core';

import {AppComponent} from './app.component';
import {HttpClientModule} from '@angular/common/http';
import {MDBBootstrapModulesPro} from 'ng-uikit-pro-standard';
import {LoginComponent} from './login/login.component';
import {RouterModule} from '@angular/router';
import {appRoutingModule} from './app.routing';
import {HomeComponent} from './home';
import {ReactiveFormsModule} from '@angular/forms';

@NgModule({
  declarations: [
    AppComponent,
    HomeComponent,
    LoginComponent
  ],
  imports: [
    BrowserModule,
    HttpClientModule,
    MDBBootstrapModulesPro.forRoot(),
    appRoutingModule,
    ReactiveFormsModule
  ],
  providers: [],
  bootstrap: [AppComponent]
})
export class AppModule {
}
