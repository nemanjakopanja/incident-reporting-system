package org.unibl.etf.pisio.auth_service.dto;

public class AuthResponse {

    private String token;
    private String message;
    private UserDto user;

    public AuthResponse() {
    }

    public AuthResponse(String token, String message, UserDto user) {
        this.token = token;
        this.message = message;
        this.user = user;
    }

    public String getToken() {
        return token;
    }

    public void setToken(String token) {
        this.token = token;
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }

    public UserDto getUser() {
        return user;
    }

    public void setUser(UserDto user) {
        this.user = user;
    }

    @Override
    public String toString() {
        return "AuthResponse{" +
                "token='" + token + '\'' +
                ", user=" + user +
                '}';
    }
}
