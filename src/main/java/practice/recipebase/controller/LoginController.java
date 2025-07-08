package practice.recipebase.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
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
    public String showLoginForm(Model model) {
        model.addAttribute("user", new AppUser());
        return "login";
    }

    @GetMapping("/register")
    public String showRegistrationForm(Model model) {
        model.addAttribute("user", new AppUser());
        return "register";
    }

    @PostMapping("/register")
    public String registerNewUser(@ModelAttribute("user") AppUser newUser) {
        userService.register(newUser);
        return "index";
    }
}
