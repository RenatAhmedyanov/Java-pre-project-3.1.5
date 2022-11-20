package ru.kata.spring.boot_security.demo.controllers;

import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import ru.kata.spring.boot_security.demo.models.User;
import ru.kata.spring.boot_security.demo.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import java.util.Optional;

@Controller
@RequestMapping("/admin")
public class AdminsController {
    private UserService userService;

    @Autowired
    public AdminsController(UserService userService) {
        this.userService = userService;
    }

    @GetMapping
    public String adminPage(@ModelAttribute("user") User user, Model model) {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        User admin = (User) authentication.getPrincipal();
        model.addAttribute("admin", admin);
        model.addAttribute("usersList", userService.findAllUsers());
        return "/BSDEMO/admin_panel";
    }

    @GetMapping(value = "/{id}")
    public String showUserById(@PathVariable("id") Integer id, Model model) {
        model.addAttribute("user", userService.findUserById(id));
        return "admin/show";
    }

    @GetMapping("/get/{id}")
    @ResponseBody
    public User getUser(@PathVariable("id") Integer id) {
        System.out.println(userService.findUserById(id).toString());
        return userService.findUserById(id);
    }

    @GetMapping("/new")
    public String newUser(@ModelAttribute("user") User user) {
        return "admin/new";
    }

    @PostMapping()
    public String addUser(User user) {
        userService.addUser(user);
        return "redirect:/admin";
    }

    @GetMapping("/{id}/edit")
    public String editUser(Model model, @PathVariable("id") Integer id) {
        model.addAttribute("user", userService.findUserById(id));
        return "/admin/edit";
    }

    @PatchMapping("/update")
    public String update(@ModelAttribute("user") User user) {
        userService.updateUser(user);
        return "redirect:/admin";
    }

    @PatchMapping("/{id}/admin")
    public String giveAdminRights(@ModelAttribute("user") User user, @PathVariable("id") Integer id) {
        userService.giveAdminRights(userService.findUserById(id));
        return "redirect:/admin";
    }

    @RequestMapping(value = "/delete", method = {RequestMethod.DELETE, RequestMethod.GET})
    public String deleteUser(Integer id) {
        userService.deleteUser(id);
        return "redirect:/admin";
    }
}
