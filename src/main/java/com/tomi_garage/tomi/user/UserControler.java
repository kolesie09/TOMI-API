package com.tomi_garage.tomi.user;

import java.util.List;
import java.util.Optional;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class UserControler {

    private final UserRepository userRepository;
    private final UserService userService;

    public UserControler(UserService userService, UserRepository userRepository) {
        this.userService = userService;
        this.userRepository = userRepository;
    }

    @GetMapping("/api/users")
    public List<User> getAllUsers() {
        return userService.getAllUsers();
    }

    @GetMapping("api/users/email/{email}")
    public Optional<User> getUserByEmail(@PathVariable String email) {
        return userService.getUserByEmail(email);
    }

    @GetMapping("/api/users/encode/{password}")
    public String encodePassword(@PathVariable String password) {
        return userService.encodePassword(password);
    }

    @GetMapping("/api/users/check")
    public String checkPassword() {
        Optional<User> userOptional = userService.getUserByEmail("admin@tomi.pl");

        if (userOptional.isEmpty()) {
            return "Użytkownik nie istnieje";
        }

        User user = userOptional.get();

        boolean correctPassword = userService.checkPassword("admin123", user.getPassword());

        if (correctPassword) {
            return "Hasło poprawne";
        }

        return "Hasło niepoprawne";
    }

}
