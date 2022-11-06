package ru.kata.spring.boot_security.demo.dao;

import ru.kata.spring.boot_security.demo.models.User;
import org.springframework.stereotype.Component;
import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.persistence.TypedQuery;
import java.util.List;
import java.util.Optional;

@Component
public class UserDAOImplementation implements UserDAO {
    @PersistenceContext
    private EntityManager entityManager;

    public void addUser(User user) {
        entityManager.persist(user);
    }

    public User findUserById(Integer id) {
        return entityManager.find(User.class, id);
    }

    public Optional<User> findUserByUsername(String username) {
        TypedQuery<User> findByUsernameQuery = entityManager.createQuery("SELECT user FROM User user JOIN FETCH user.roles WHERE user.username = :username", User.class).setParameter("username", username);
        return findByUsernameQuery.getResultStream().findFirst();
    }

    public Optional<User> findUserByEmail(String email) {
        TypedQuery<User> findByUsernameQuery = entityManager.createQuery("SELECT user FROM User user WHERE user.email = :email", User.class).setParameter("email", email);
        return findByUsernameQuery.getResultStream().findFirst();
    }

    public List<User> findAllUsers() {
        TypedQuery<User> findAllQuery = entityManager.createQuery("SELECT user FROM User user", User.class);
        return findAllQuery.getResultList();
    }

    public void updateUser(User updatedUser) {
        entityManager.merge(updatedUser);
    }

    public void deleteUser(int id) {
        entityManager.remove(entityManager.find(User.class, id));
    }
}
