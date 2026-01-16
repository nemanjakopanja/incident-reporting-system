package org.unibl.etf.pisio.auth_service.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.unibl.etf.pisio.auth_service.dto.AuthResponse;
import org.unibl.etf.pisio.auth_service.dto.LoginRequest;
import org.unibl.etf.pisio.auth_service.dto.RegisterRequest;
import org.unibl.etf.pisio.auth_service.security.JwtUtil;
import org.unibl.etf.pisio.auth_service.service.AuthService;
import reactor.core.publisher.Mono;

@RestController
@RequestMapping("/api/auth")
public class AuthController {

    private final AuthService authService;
    private final JwtUtil jwtUtil;
    private static final String SUCCESSFUL_LOGIN_MESSAGE = "Uspješno prijavljivanje na sistem!";
    private static final String UNSUCCESSFUL_LOGIN_MESSAGE = "Pogrešno korisničko ime ili lozinka!";

    @Autowired
    public AuthController(AuthService authService, JwtUtil jwtUtil) {
        this.authService = authService;
        this.jwtUtil = jwtUtil;
    }

    @PostMapping("/login")
    public Mono<ResponseEntity<AuthResponse>> login(@RequestBody LoginRequest loginRequest) {
        System.out.println("AuthController - login");
        System.out.println("username: " + loginRequest.getUsername() + ", password: " + loginRequest.getPassword());
        if(loginRequest.getUsername() == null || loginRequest.getPassword() == null) {
            return Mono.just(ResponseEntity.badRequest().body(null));
        }
        /*Mono<UserDto> user = authService.login(loginRequest);
        return user.map(result -> ResponseEntity.ok(result))
                .onErrorReturn(ResponseEntity.status(HttpStatus.BAD_REQUEST).body(null));*/

        return authService.login(loginRequest)
                .map(user -> {
                    String token = jwtUtil.generateToken(user);
                    System.out.println("TOKEN - " + token);
                    AuthResponse response = new AuthResponse(token, SUCCESSFUL_LOGIN_MESSAGE, user);
                    System.out.println("RESPONSE - " + response);
                    return ResponseEntity.ok(response);
                })
                .onErrorReturn(ResponseEntity.status(HttpStatus.UNAUTHORIZED).body(new AuthResponse(null, UNSUCCESSFUL_LOGIN_MESSAGE, null)));
    }

    @PostMapping("/register")
    public Mono<ResponseEntity<Boolean>> register(@RequestBody RegisterRequest registerRequest) {
        System.out.println("AuthController - register");
        if(registerRequest.getFirstName() == null
                || registerRequest.getLastName() == null
                || registerRequest.getUsername() == null
                || registerRequest.getPassword() == null) {
            return Mono.just(ResponseEntity.badRequest().body(false));
        }
        Mono<Boolean> isUserRegistered = authService.register(registerRequest);
        return isUserRegistered.map(result -> ResponseEntity.ok(result))
                .onErrorReturn(ResponseEntity.status(HttpStatus.BAD_REQUEST).body(false));
    }

    @GetMapping("/logout")
    public ResponseEntity<String> logout() {
        return ResponseEntity.ok("LOGOUT");
    }

}
