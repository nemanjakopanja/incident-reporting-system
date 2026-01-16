package org.unibl.etf.pisio.user_service.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.unibl.etf.pisio.user_service.model.LoginRequest;
import org.unibl.etf.pisio.user_service.model.RegisterRequest;
import org.unibl.etf.pisio.user_service.model.User;
import org.unibl.etf.pisio.user_service.service.UserService;

@RestController
@RequestMapping("/api/users")
public class UserController {

    private final UserService userService;

    @Autowired
    public UserController(UserService userService) {
        this.userService = userService;
    }

    @GetMapping
    public ResponseEntity<String> getAllUsers() {
        return new ResponseEntity<>("Marko, Nemanja, Dragan", HttpStatus.OK);
    }

    @PostMapping("/register")
    public ResponseEntity<Boolean> registerUser(@RequestBody RegisterRequest registerRequest) {
        System.out.println("UserController - register");

        boolean isUserRegistered = userService.registerUser(registerRequest);
        if (isUserRegistered)
            return ResponseEntity.status(HttpStatus.CREATED).body(true);
        else
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(false);
    }

    @PostMapping("/login")
    public ResponseEntity<?> login(@RequestBody LoginRequest loginRequest) {
        System.out.println("UserController - login");
        System.out.println("username: " + loginRequest.getUsername() + ", password: " + loginRequest.getPassword());

        User user = userService.login(loginRequest);
        System.out.println("UserController return user: " + user);
        HttpStatus httpStatus;
        if (user != null)
            httpStatus = HttpStatus.OK;
        else
            httpStatus = HttpStatus.BAD_REQUEST;

        return ResponseEntity.status(httpStatus).body(user);

        /*try {
            User user = userService.login(loginRequest);
            return ResponseEntity.ok(user);
        } catch (RuntimeException e) {
            Map<String, String> errorResponse = new HashMap<>();    //vracam mapu jer angular ocekuje JSON, a ne String
            errorResponse.put("error", e.getMessage());
            return ResponseEntity.badRequest().body(errorResponse);
        }*/
    }
}
