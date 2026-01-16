package org.unibl.etf.pisio.user_service.exception;

public class UserNotFoundException extends RuntimeException {

    public UserNotFoundException() {
        super("Wrong username or password!");
    }

    public UserNotFoundException(String message) {
        super(message);
    }
}
