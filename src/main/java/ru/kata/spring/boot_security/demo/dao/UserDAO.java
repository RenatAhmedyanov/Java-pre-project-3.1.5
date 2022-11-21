package ru.kata.spring.boot_security.demo.dao;

import ru.kata.spring.boot_security.demo.models.Role;
import ru.kata.spring.boot_security.demo.models.User;

import java.util.List;
import java.util.Optional;

public interface UserDAO {
    Role findRoleByRoleName(String roleName);
    List<Role> getRolesList();
    void addUser(User user);
    User findUserById(Integer id);
    Optional<User> findUserByUsername(String username);
    Optional<User> findUserByEmail(String email);
    List<User> findAllUsers();
    void createAdminRole();
    void createUserRole();
    void updateUser(User updatedUser);
    void deleteUser(int id);
    void deleteTables();
}
