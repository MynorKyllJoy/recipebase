package practice.recipebase.web;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;
import practice.recipebase.model.AppUser;
import practice.recipebase.service.AppUserService;

@Controller
public class LoginController {
    @Autowired
    AppUserService userService;

    @GetMapping("/login")
    public String showLoginForm() {
        return "login";
    }

    @PostMapping("/login")
    public String loginUser() {
        return "index";
    }

    @GetMapping("/register")
    public String showRegistrationForm() {
        return "register";
    }

    @PostMapping("/register")
    public String registerNewUser(@ModelAttribute("user") AppUser newUser) {
        userService.register(newUser);
        return "index";
    }
}
