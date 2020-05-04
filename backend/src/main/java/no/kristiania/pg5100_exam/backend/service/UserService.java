package no.kristiania.pg5100_exam.backend.service;

import no.kristiania.pg5100_exam.backend.entity.PlaceholderItem;
import no.kristiania.pg5100_exam.backend.entity.User;
import org.hibernate.Hibernate;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import javax.persistence.EntityManager;
import javax.persistence.TypedQuery;
import java.util.Collections;
import java.util.List;

@Service
@Transactional
public class UserService {

    @Autowired
    private EntityManager em;

    @Autowired
    private PasswordEncoder passwordEncoder;

    public User createUser(String username, String password) {

        String hashedPassword = passwordEncoder.encode(password);

        // Check if user already exists
        if (em.find(User.class, username) != null) {
            return null;
        }

        User user = new User();
        user.setUsername(username);
        user.setPassword(hashedPassword);
        user.setRoles(Collections.singleton("USER"));
        user.setEnabled(true);

        em.persist(user);

        return user;
    }

    public User getUser(String username, boolean withTransactions) {

        User user = em.find(User.class, username);

        if (withTransactions && user != null) {
            Hibernate.initialize(user.getTransactions());
        }

        return user;
    }

    public List<User> getAllUsers(boolean withItems) {

        TypedQuery<User> query = em.createQuery("SELECT user FROM User user", User.class);
        List<User> users = query.getResultList();

        if (withItems && users != null) {
            users.forEach(user -> Hibernate.initialize(user.getTransactions()));
        }

        return users;

    }
}
