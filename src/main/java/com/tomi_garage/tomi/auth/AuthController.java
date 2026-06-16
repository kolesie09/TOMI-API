package com.tomi_garage.tomi.auth;

import java.util.Optional;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.bind.annotation.RestController;

import com.tomi_garage.tomi.security.JwtService;
import com.tomi_garage.tomi.user.User;
import com.tomi_garage.tomi.user.UserService;

@RestController
public class AuthController {

    private final UserService userService;
    private final JwtService jwtService;

    public AuthController(UserService userService, JwtService jwtService) {
        this.userService = userService;
        this.jwtService = jwtService;
    }

    @GetMapping("/api/auth/me")
    public String getEmailFromToken(@RequestHeader("Authorization") String authHeader) {
        String token = authHeader.substring(7);
        return jwtService.extractEmail(token);
    }

    @PostMapping("/api/auth/login")
    public AuthResponse login(@RequestBody LoginRequest loginRequest) {
        Optional<User> userOptional = userService.getUserByEmail(loginRequest.getEmail());

        if (userOptional.isEmpty()) {
            return new AuthResponse("Użytkownik nie istnieje", null);
        }

        User user = userOptional.get();

        boolean correctPassword = userService.checkPassword(
                loginRequest.getPassword(),
                user.getPassword()
        );

        if (!correctPassword) {
            return new AuthResponse("Nieprawidłowe hasło", null);
        }

        String token = jwtService.generateToken(user.getEmail());

        return new AuthResponse("Logowanie poprawne", token);
    }
}
