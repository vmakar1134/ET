export class UserAuth {
  login: string;
  password: string;

  constructor(username: string, password: string) {
    this.login = username;
    this.password = password;
  }
}

