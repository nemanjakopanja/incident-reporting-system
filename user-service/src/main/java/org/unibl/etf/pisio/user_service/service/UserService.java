package org.unibl.etf.pisio.user_service.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.unibl.etf.pisio.user_service.enums.Role;
import org.unibl.etf.pisio.user_service.model.LoginRequest;
import org.unibl.etf.pisio.user_service.model.User;
import org.unibl.etf.pisio.user_service.model.RegisterRequest;
import org.unibl.etf.pisio.user_service.repository.UserRepository;

import java.util.Optional;

@Service
public class UserService {

    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;

    @Autowired
    public UserService(UserRepository userRepository, PasswordEncoder passwordEncoder) {
        this.userRepository = userRepository;
        this.passwordEncoder = passwordEncoder;
    }

    public boolean registerUser(RegisterRequest registerRequest) {
        Optional<User> user = userRepository.findByUsername(registerRequest.getUsername());
        if(user.isPresent()) {
            //throw new UsernameAlreadyTakenException();
            return false;
        }

        User newUser = new User(registerRequest.getFirstName(), registerRequest.getLastName(), registerRequest.getUsername(),
                hashedPassword(registerRequest.getPassword()), Role.CLIENT);

        //User registeredUser = userRepository.save(newUser);
        //return registeredUser;

        userRepository.save(newUser);
        return true;
    }

    public User login(LoginRequest loginRequest) {
        System.out.println("UserService - login");
        System.out.println("username: " + loginRequest.getUsername() + ", password: " + loginRequest.getPassword());
        Optional<User> userOptional = userRepository.findByUsername(loginRequest.getUsername());
        User user;
        if(userOptional.isPresent())
            user = userOptional.get();
        else
            return null;

        if(!passwordEncoder.matches(loginRequest.getPassword(), user.getPassword())) {
            return null;
        }

        System.out.println("UserService return user: " + user);
        return user;

        /*User user = userRepository.findByUsername(loginRequest.getUsername()).orElseThrow(() -> new UserNotFoundException("Wrong username!"));
        if(!passwordEncoder.matches(loginRequest.getPassword(), user.getPassword())) {
            throw new UserNotFoundException("Wrong password!");
        }
        return user;*/
    }

    private String hashedPassword(String password) {
        return passwordEncoder.encode(password);
    }
}
