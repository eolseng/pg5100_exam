package no.kristiania.pg5100_exam.backend.service;

import no.kristiania.pg5100_exam.backend.entity.User;
import no.kristiania.pg5100_exam.backend.repository.UserRepository;
import org.hibernate.Hibernate;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Collections;
import java.util.List;
import java.util.Optional;

@Service
@Transactional
public class UserService {

    @Autowired
    private PasswordEncoder passwordEncoder;

    @Autowired
    private UserRepository repo;

    public User createUser(String username, String password) {

        String hashedPassword = passwordEncoder.encode(password);

        // Username must be unique
        if (repo.existsById(username)){
            throw new IllegalArgumentException("A user with that username already exists: " + username);
        }

        User user = new User();
        user.setUsername(username);
        user.setPassword(hashedPassword);
        user.setRoles(Collections.singleton("USER"));
        user.setEnabled(true);
        user.setMoney(3000L);
        repo.save(user);

        return user;
    }

    public User getUser(String username, boolean withBookings) {

        Optional<User> user = repo.findById(username);
        if(user.isEmpty()) {
            throw new IllegalArgumentException("Username does not exists: " + username);
        }
        if (withBookings) {
            Hibernate.initialize(user.get().getBookings());
        }

        return user.get();
    }

    public List<User> getAllUsers(boolean withBookings) {

        List<User> users = repo.findAll();
        if (withBookings) {
            users.forEach(user -> Hibernate.initialize(user.getBookings()));
        }
        return users;
    }

    public Boolean existsById(String username) {
        return repo.existsById(username);
    }
}
