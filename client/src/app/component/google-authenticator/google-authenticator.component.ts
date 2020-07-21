import { Component, OnInit } from '@angular/core';
import {User} from '../../model/user';
import {AuthenticatorService} from '../../service/authenticator.service';

@Component({
  selector: 'app-google-authenticator',
  templateUrl: './google-authenticator.component.html',
  styleUrls: ['./google-authenticator.component.css']
})
export class GoogleAuthenticatorComponent implements OnInit {

  public authIsLoaded = false;
  public isLoggedIn  = true;
  public user: User;

  constructor(private authenticatorService: AuthenticatorService) { }

  signIn(): void {
    this.authenticatorService.signIn();
    console.log(this.isLoggedIn);
  }

  signOut(): void {
    this.authenticatorService.signOut();
  }

  ngOnInit() {
    this.authenticatorService.isLoaded$.subscribe( value => {
      this.authIsLoaded = value;
    });

    this.authenticatorService.isLoggedIn$.subscribe( value => {
      this.isLoggedIn = value;
    });

    this.authenticatorService.user$.subscribe( value => {
      this.user = value;
    });

    this.authenticatorService.loadAuth2();
  }

}
