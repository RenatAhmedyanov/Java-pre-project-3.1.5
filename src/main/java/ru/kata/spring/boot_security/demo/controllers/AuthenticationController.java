package ru.kata.spring.boot_security.demo.controllers;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import ru.kata.spring.boot_security.demo.models.User;
import ru.kata.spring.boot_security.demo.service.UserService;
import ru.kata.spring.boot_security.demo.util.UserValidator;

import javax.annotation.PostConstruct;
import javax.validation.Valid;

@Controller
@RequestMapping("/authentication")
public class AuthenticationController {
    private final UserValidator userValidator;
    private final UserService userService;

    @Autowired
    public AuthenticationController(UserValidator userValidator, UserService userService) {
        this.userValidator = userValidator;
        this.userService = userService;
    }

    @GetMapping("/login")
    public String loginPage() {
        return "authentication/login";
    }

    @GetMapping("/registration")
    public String registrationPage(@ModelAttribute("user") User user) {
        return "authentication/registration";
    }

    @PostMapping("/registration")
    public String performRegistration(@ModelAttribute("user") @Valid User user, BindingResult bindingResult) {
        userValidator.validate(user, bindingResult);
        if (bindingResult.hasErrors()) {
            return "authentication/registration";
        }
        userService.addUser(user);
        return "redirect:/authentication/login";
    }

    @PostConstruct
    public void loadUserTableTest() {
        userService.addUser(new User("admin", "admin@mail.ru", "admin"));
        userService.addUser(new User("Homer", "homer@mail.ru", "homer"));
        userService.addUser(new User("Marge", "marge@mail.ru", "marge"));

        userService.addUser(new User("user", "user@mail.ru", "user"));
        userService.addUser(new User("Bart", "bart@mail.ru", "bart"));
        userService.addUser(new User("Lisa", "lisa@mail.ru", "lisa"));

        userService.giveAdminRights((User)userService.loadUserByUsername("admin"));
        userService.giveAdminRights((User)userService.loadUserByUsername("Homer"));
        userService.giveAdminRights((User)userService.loadUserByUsername("Marge"));
    }

}
