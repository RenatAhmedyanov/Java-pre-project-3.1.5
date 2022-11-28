package ru.kata.spring.boot_security.demo.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import ru.kata.spring.boot_security.demo.models.Role;
import ru.kata.spring.boot_security.demo.models.User;
import ru.kata.spring.boot_security.demo.repositories.RoleRepository;
import ru.kata.spring.boot_security.demo.repositories.UserRepository;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

@Service
@Transactional(readOnly = true)
public class UserServiceImplementation implements UserService {
    private final RoleRepository roleRepository;
    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;

    @Autowired
    public UserServiceImplementation(UserRepository userRepository, RoleRepository roleRepository, PasswordEncoder passwordEncoder) {
        this.userRepository = userRepository;
        this.roleRepository = roleRepository;
        this.passwordEncoder = passwordEncoder;
    }

    @Override
    public Role findRoleByRoleName(String roleName) {
        return roleRepository.findRoleByRoleName(roleName);
    }

    @Override
    public List<Role> getRolesList() {
        return roleRepository.findAll();
    }

    @Transactional
    public void addUser(User user) {
        user.setPassword(passwordEncoder.encode(user.getPassword()));
        userRepository.save(user);
    }

    @Transactional
    public void populateDatabase(){
        roleRepository.save(new Role("ADMIN"));
        roleRepository.save(new Role("USER"));
        List<User> users = new ArrayList<>();
        users.add(new User("admin", "admin@mail.ru", passwordEncoder.encode("admin")));
        users.add(new User("user", "user@mail.ru", passwordEncoder.encode("user")));
        users.add(new User("Homer", "homer@mail.ru", passwordEncoder.encode("homer")));
        users.add(new User("Marge", "marge@mail.ru", passwordEncoder.encode("marge")));
        users.add(new User("Bart", "bart@mail.ru", passwordEncoder.encode("bart")));
        users.add(new User("Lisa", "lisa@mail.ru", passwordEncoder.encode("lisa")));
        users.forEach(x -> x.addRoles(roleRepository.findRoleByRoleName("USER")));
        users.get(0).addRoles(roleRepository.findRoleByRoleName("ADMIN"));
        users.get(2).addRoles(roleRepository.findRoleByRoleName("ADMIN"));
        users.get(3).addRoles(roleRepository.findRoleByRoleName("ADMIN"));
        userRepository.saveAll(users);
    }

    public Optional<User> findUserById(Integer id) {
        return userRepository.findById(id);
    }

    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        return userRepository.findUserByUsername(username).orElseThrow(() -> new UsernameNotFoundException("user's not found"));
    }

    public List<User> findAllUsers() {
        return userRepository.findAll();
    }

    @Transactional
    public void updateUser(User updatedUser) {
        if (updatedUser.getPassword().equals(userRepository.findById(updatedUser.getId()).orElseThrow().getPassword()) || updatedUser.getPassword().equals("")) {
            updatedUser.setPassword(userRepository.findById(updatedUser.getId()).orElseThrow().getPassword());
        } else {
            updatedUser.setPassword(passwordEncoder.encode(updatedUser.getPassword()));
        }
        userRepository.save(updatedUser);
    }

    @Transactional
    public void deleteUser(int id) {
        userRepository.deleteById(id);
    }

    @Transactional
    public void deleteTables() {
        userRepository.deleteAll(userRepository.findAll());
        roleRepository.deleteAll(roleRepository.findAll());
    }
}
