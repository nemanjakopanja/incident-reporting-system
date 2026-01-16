import { User } from "./user";

export class RegisterResponse {
    message!: string;
    user!: User;

    constructor( message: string, user: User) {
        this.message = message;
        this.user = user;
    }
}