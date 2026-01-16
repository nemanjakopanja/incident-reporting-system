package org.unibl.etf.pisio.user_service.exception;

public class UsernameAlreadyTakenException extends RuntimeException {

    public UsernameAlreadyTakenException() {
        super("Username already taken!");
    }

    public UsernameAlreadyTakenException(String message) {
        super(message);
    }
}
