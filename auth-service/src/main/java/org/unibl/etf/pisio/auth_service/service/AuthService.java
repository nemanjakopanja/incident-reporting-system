package org.unibl.etf.pisio.auth_service.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.web.reactive.function.client.WebClient;
import org.unibl.etf.pisio.auth_service.dto.LoginRequest;
import org.unibl.etf.pisio.auth_service.dto.RegisterRequest;
import org.unibl.etf.pisio.auth_service.dto.UserDto;
import reactor.core.publisher.Mono;

@Service
public class AuthService {

    private final WebClient.Builder webClientBuilder;

    @Autowired
    public AuthService(WebClient.Builder webClientBuilder) {
        this.webClientBuilder = webClientBuilder;
    }

    public Mono<UserDto> login(LoginRequest loginRequest) {
        System.out.println("AuthService - login");
        System.out.println("username: " + loginRequest.getUsername() + ", password: " + loginRequest.getPassword());
        return webClientBuilder.build().post()
                .uri("http://user-service/api/users/login")
                .bodyValue(loginRequest)
                .retrieve()
                .bodyToMono(UserDto.class);
    }

    public Mono<Boolean> register(RegisterRequest registerRequest) {
        System.out.println("AuthService - register");
        return webClientBuilder.build().post()
                .uri("http://user-service/api/users/register")
                .bodyValue(registerRequest)
                .retrieve()
                .bodyToMono(Boolean.class);
    }
}
