package ru.kata.spring.boot_security.demo.service;

import ru.kata.spring.boot_security.demo.models.User;
import org.springframework.security.core.userdetails.UserDetailsService;
import java.util.List;
import java.util.Optional;

public interface UserService extends UserDetailsService {
    void addUser(User user);
    void giveAdminRights(User user);
    User findUserById(Integer id);
    Optional<User> findUserByEmail(String email);
    List<User> findAllUsers();
    void updateUser(User updatedUser);
    void deleteUser(int id);

}
