package ru.kata.spring.boot_security.demo.controllers;

import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import ru.kata.spring.boot_security.demo.models.Role;
import ru.kata.spring.boot_security.demo.models.User;
import ru.kata.spring.boot_security.demo.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import java.sql.Array;
import java.util.HashSet;
import java.util.Set;

@Controller
@RequestMapping("/admin")
public class AdminsController {
    private UserService userService;

    @Autowired
    public AdminsController(UserService userService) {
        this.userService = userService;
    }

    @GetMapping
    public String adminPage(Model model) {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        User admin = (User) authentication.getPrincipal();
        model.addAttribute("user", new User());
        model.addAttribute("existingRoles", userService.getRolesList());
        model.addAttribute("admin", admin);
        model.addAttribute("usersList", userService.findAllUsers());
        model.addAttribute("role_user", userService.findRoleByRoleName("ROLE_USER"));
        model.addAttribute("role_admin", userService.findRoleByRoleName("ROLE_ADMIN"));
        return "/BSDEMO/admin_panel";
    }


    @GetMapping(value = "/get/{id}")
    @ResponseBody
    public String getUser(@PathVariable("id") Integer id, Model model) {
        model.addAttribute("user", userService.findUserById(id));
        return "redirect:/admin";
    }
//
//    @GetMapping("/get/{id}")
//    @ResponseBody
//    public User getUser(@PathVariable("id") Integer id) {
//        System.out.println(userService.findUserById(id).toString());
//        return userService.findUserById(id);
//    }

    @GetMapping("/new")
    public String newUser(@ModelAttribute("user") User user) {
        return "admin/new";
    }

    @PostMapping()
    public String addUser(User user) {
        userService.addUser(user);
        return "redirect:/admin";
    }

    @GetMapping(value = "/{id}/edit")
    public String editUser(Model model, @PathVariable("id") Integer id) {
        model.addAttribute("existingRoles", userService.getRolesList());
        model.addAttribute("user", userService.findUserById(id));
        return "/admin/edit";
    }


//    @PatchMapping("/{id}")
//    public String update(@ModelAttribute("user") User updatedUser, @RequestParam("roles") String[] roles, @PathVariable("id") Integer id) {
//        Set<Role> updatedRoles = new HashSet<>();
//        for (String role : roles) {
//            updatedRoles.add(userService.findRoleByRoleName(role));
//        }
//        updatedUser.setRoles(updatedRoles);
//        userService.updateUser(updatedUser);
//        return "redirect:/admin";
//    }

    @PatchMapping("/edit")
    public String update(@ModelAttribute("user") User updatedUser, @RequestParam(value = "roles", required = false) String[] roles) {
        Set<Role> updatedRoles = new HashSet<>();
        for (String role : roles) {
            updatedRoles.add(userService.findRoleByRoleName(role));
        }
        updatedUser.setRoles(updatedRoles);
        userService.updateUser(updatedUser);
        return "redirect:/admin";
    }

    @PatchMapping("/{id}/admin")
    public String giveAdminRights(@ModelAttribute("user") User user, @PathVariable("id") Integer id) {
        userService.giveAdminRights(userService.findUserById(id));
        return "redirect:/admin";
    }

    @GetMapping(value = "/delete/{id}")
    public String deleteUser(@PathVariable Integer id, Model model) {
        userService.deleteUser(id);
        return "redirect:/admin";
    }
}

