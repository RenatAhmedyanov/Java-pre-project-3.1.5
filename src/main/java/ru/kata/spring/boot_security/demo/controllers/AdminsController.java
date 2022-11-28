package ru.kata.spring.boot_security.demo.controllers;

import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.transaction.annotation.Transactional;
import ru.kata.spring.boot_security.demo.models.Role;
import ru.kata.spring.boot_security.demo.models.User;
import ru.kata.spring.boot_security.demo.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import javax.annotation.PostConstruct;
import javax.annotation.PreDestroy;
import java.util.HashSet;
import java.util.Set;

@Controller
public class AdminsController {
    private final UserService userService;

    @Autowired
    public AdminsController(UserService userService) {
        this.userService = userService;
    }

    @GetMapping("/login")
    public String loginPage() {
        return "views/login";
    }

    @GetMapping("/admin")
    public String adminPage(Model model) {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        User signedUser = (User) authentication.getPrincipal();
        model.addAttribute("user", new User());
        model.addAttribute("existingRoles", userService.getRolesList());
        model.addAttribute("signedUser", signedUser);
        model.addAttribute("usersList", userService.findAllUsers());
        model.addAttribute("role_user", userService.findRoleByRoleName("USER"));
        model.addAttribute("role_admin", userService.findRoleByRoleName("ADMIN"));
        return "/views/admin_panel";
    }

    @GetMapping(value = "/get/{id}")
    @ResponseBody
    public String getUser(@PathVariable("id") Integer id, Model model) {
        model.addAttribute("user", userService.findUserById(id));
        return "redirect:/admin";
    }

    @PostMapping(value = "/new")
    public String addUser(@ModelAttribute("user") User newUser, @RequestParam(value = "new-roles", required = false) String[] roles) {
        Set<Role> updatedRoles = new HashSet<>();
        for (String role : roles) {
            updatedRoles.add(userService.findRoleByRoleName(role));
        }
        newUser.setRoles(updatedRoles);
        userService.addUser(newUser);
        return "redirect:/admin";
    }

    @PatchMapping("/edit")
    public String update(@ModelAttribute("user") User updatedUser, @RequestParam(value = "updated-roles", required = false) String[] roles) {
        Set<Role> updatedRoles = new HashSet<>();
        for (String role : roles) {
            updatedRoles.add(userService.findRoleByRoleName(role));
        }
        updatedUser.setRoles(updatedRoles);
        userService.updateUser(updatedUser);
        return "redirect:/admin";
    }

    @GetMapping(value = "/delete/{id}")
    public String deleteUser(@PathVariable Integer id) {
        userService.deleteUser(id);
        return "redirect:/admin";
    }

    @Transactional
    @PostConstruct
    public void populateDatabase() {
        userService.populateDatabase();
    }

    @Transactional
    @PreDestroy
    public void deleteTables() {
        userService.deleteTables();
    }
}

