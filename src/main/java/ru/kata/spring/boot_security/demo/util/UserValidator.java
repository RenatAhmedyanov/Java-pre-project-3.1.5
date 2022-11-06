package ru.kata.spring.boot_security.demo.util;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.validation.Errors;
import org.springframework.validation.Validator;
import ru.kata.spring.boot_security.demo.dao.UserDAO;
import ru.kata.spring.boot_security.demo.models.User;

@Component
public class UserValidator implements Validator {
    private final UserDAO userDAO;

    @Autowired
    public UserValidator(UserDAO userDAO) {
        this.userDAO = userDAO;
    }

    @Override
    public boolean supports(Class<?> clazz) {
        return User.class.equals(clazz);
    }

    @Override
    public void validate(Object target, Errors errors) {
         User user = (User) target;
         if (userDAO.findUserByEmail(user.getEmail()).isPresent()) {
             errors.rejectValue("email", "", "this email is already taken");
         }
         if (userDAO.findUserByUsername(user.getUsername()).isPresent()) {
             errors.rejectValue("username", "", "this username is already taken");
         }
    }
}
