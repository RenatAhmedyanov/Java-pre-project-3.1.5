package ru.kata.spring.boot_security.demo.service;

import org.springframework.security.core.userdetails.UserDetailsService;
import ru.kata.spring.boot_security.demo.models.Role;
import ru.kata.spring.boot_security.demo.models.User;
import java.util.List;

public interface UserService extends UserDetailsService {
    void populateDatabase();
    void addUser(User user);
    User findUserById(Integer id);
    List<User> findAllUsers();
    void updateUser(User updatedUser);
    void deleteUser(int id);
    Role findRoleByRoleName(String roleName);
    List<Role> getRolesList();
    void deleteTables();
}
