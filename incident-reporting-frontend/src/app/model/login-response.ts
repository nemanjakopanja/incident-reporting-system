import { User } from "./user";

export class LoginResponse {
    token!: string;
    message!: string;
    user!: User;

    constructor(token: string, message: string, user: User) {
        this.token = token;
        this.message = message;
        this.user = user;
    }
}