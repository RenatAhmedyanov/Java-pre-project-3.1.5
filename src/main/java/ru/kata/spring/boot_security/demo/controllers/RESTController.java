package ru.kata.spring.boot_security.demo.controllers;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.bind.annotation.*;
import ru.kata.spring.boot_security.demo.models.Role;
import ru.kata.spring.boot_security.demo.models.User;
import ru.kata.spring.boot_security.demo.service.UserService;

import javax.annotation.PostConstruct;
import javax.annotation.PreDestroy;
import java.security.Principal;
import java.util.List;

@CrossOrigin(origins = "http://localhost:8080")
@RestController
@RequestMapping("/api")
public class RESTController {
    private final UserService userService;

    @Autowired
    public RESTController(UserService userService) {
        this.userService = userService;
    }

    @GetMapping("/index")
    public List<User> getAllUsers() {
        return userService.findAllUsers();
    }

    @GetMapping("/index/{id}")
    public User getUser(@PathVariable("id") Integer id) {
        return userService.findUserById(id).orElseThrow();
    }

    @GetMapping("/index/roles")
    public List<Role> getAllRoles() {
        return userService.getRolesList();
    }

    @PutMapping("/index")
    public ResponseEntity<?> updateUser(@RequestBody User user) {
        userService.updateUser(user);
        return new ResponseEntity<>(HttpStatus.ACCEPTED);
    }

    @DeleteMapping("/index/{id}")
    public ResponseEntity<?> deleteUser(@PathVariable("id") Integer id) {
        userService.deleteUser(id);
        return new ResponseEntity<>(HttpStatus.OK);
    }

    @PostMapping("/index")
    public ResponseEntity<?> addNewUser(@RequestBody User user) {
        userService.addUser(user);
        return new ResponseEntity<>(HttpStatus.CREATED);
    }

    @GetMapping("/user")
    public ResponseEntity<User> getCurrentUser(Principal principal){
        System.out.println(principal);
        User user = (User)userService.loadUserByUsername(principal.getName());
        return new ResponseEntity<>(user, HttpStatus.OK);
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
