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

import javax.annotation.PostConstruct;
import java.util.*;

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
    public void populateDatabase(){
        userDAO.createAdminRole();
        userDAO.createUserRole();
        List<User> users = new ArrayList<>();
        users.add(new User("admin", "admin@mail.ru", passwordEncoder.encode("admin")));
        users.add(new User("user", "user@mail.ru", passwordEncoder.encode("user")));
        users.add(new User("Homer", "homer@mail.ru", passwordEncoder.encode("homer")));
        users.add(new User("Marge", "marge@mail.ru", passwordEncoder.encode("marge")));
        users.add(new User("Bart", "bart@mail.ru", passwordEncoder.encode("bart")));
        users.add(new User("Lisa", "lisa@mail.ru", passwordEncoder.encode("lisa")));
        users.forEach(x -> x.addRoles(userDAO.findRoleByRoleName("ROLE_USER")));
        users.get(0).addRoles(userDAO.findRoleByRoleName("ROLE_ADMIN"));
        users.get(2).addRoles(userDAO.findRoleByRoleName("ROLE_ADMIN"));
        users.get(3).addRoles(userDAO.findRoleByRoleName("ROLE_USER"));
        users.forEach(userDAO::addUser);
    }

    @Transactional
    public void giveAdminRights(User user) {
        user.addRoles(userDAO.findRoleByRoleName("ROLE_ADMIN"));
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

    @Transactional
    public void deleteTables() {
        userDAO.deleteTables();
    }
}
