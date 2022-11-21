package ru.kata.spring.boot_security.demo.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import ru.kata.spring.boot_security.demo.dao.UserDAO;
import ru.kata.spring.boot_security.demo.models.Role;
import ru.kata.spring.boot_security.demo.models.User;

import java.util.HashSet;
import java.util.List;
import java.util.Optional;
import java.util.Set;

@Service
@Transactional(readOnly = true)
public class UserServiceImplementation implements UserService {
    private final UserDAO userDAO;
    private final PasswordEncoder passwordEncoder;

    @Autowired
    public UserServiceImplementation(UserDAO userDAO, PasswordEncoder passwordEncoder) {
        this.userDAO = userDAO;
        this.passwordEncoder = passwordEncoder;
    }

    @Override
    public Role findRoleByRoleName(String roleName) {
        return userDAO.findRoleByRoleName(roleName);
    }

    @Override
    public List<Role> getRolesList() {
        return userDAO.getRolesList();
    }

    @Transactional
    public void addUser(User user) {
        user.setPassword(passwordEncoder.encode(user.getPassword()));
        user.addRoles(userDAO.findRoleByRoleName("ROLE_USER"));
        userDAO.addUser(user);
    }

    @Transactional
    public void firstInit(){
        User admin = new User("admin", "admin@mail.ru", "admin");
        admin.addRoles(new Role("ROLE_ADMIN"));
        admin.addRoles(new Role("ROLE_USER"));
        userDAO.addUser(admin);
    }

    @Transactional
    public void giveAdminRights(User user) {
        user.addRoles(userDAO.findRoleByRoleName("ROLE_ADMIN"));
        userDAO.updateUser(user);
    }

    @Transactional
    public void revokeAdminRights(User user) {
        user.setRoles(new HashSet<>());
        user.addRoles(userDAO.findRoleByRoleName("ROLE_USER"));
        userDAO.updateUser(user);
    }

    public User findUserById(Integer id) {
        return userDAO.findUserById(id);
    }

    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        return userDAO.findUserByUsername(username).orElseThrow(() -> new UsernameNotFoundException("user's not found"));
    }

    public Optional<User> findUserByEmail(String email) {
        return userDAO.findUserByEmail(email);
    }

    public List<User> findAllUsers() {
        return userDAO.findAllUsers();
    }

    @Transactional
    public void updateUser(User updatedUser) {
        if (updatedUser.getPassword().equals(userDAO.findUserById(updatedUser.getId()).getPassword()) || updatedUser.getPassword().equals("")) {
            updatedUser.setPassword(userDAO.findUserById(updatedUser.getId()).getPassword());
        } else {
            updatedUser.setPassword(passwordEncoder.encode(updatedUser.getPassword()));
        }
        userDAO.updateUser(updatedUser);
    }

    @Transactional
    public void deleteUser(int id) {
        userDAO.deleteUser(id);
    }
}
