package practice.recipebase.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import practice.recipebase.model.AppUser;
import practice.recipebase.service.AppUserService;

@RestController
@RequestMapping("/auth")
@CrossOrigin(origins="http://localhost:5173")
public class LoginController {
    @Autowired
    AppUserService userService;

    @PostMapping("/login")
    public String login(@RequestBody AppUser user) {
        return userService.verify(user);
    }

    @PostMapping("/register")
    public String register(@RequestBody AppUser newUser) {
        return userService.register(newUser);
    }
}
