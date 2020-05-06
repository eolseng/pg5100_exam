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

        if (password.length() < User.MIN_PASSWORD_LENGTH) {
            throw new IllegalArgumentException("Password is too short. Must be at least " + User.MIN_PASSWORD_LENGTH + " long.");
        }

        if (repo.existsById(username)) {
            // Username must be unique
            throw new IllegalArgumentException("A user with that username already exists: " + username);
        }

        User user = new User();
        user.setUsername(username);
        user.setPasswordHash(hashedPassword);
        user.setRoles(Collections.singleton("USER"));
        user.setEnabled(true);
        user.setMoney(User.STARTING_MONEY);
        repo.save(user);

        return user;
    }

    public User getUser(String username, boolean withBookings) {

        Optional<User> user = repo.findById(username);
        if (user.isEmpty()) {
            throw new IllegalArgumentException("Username does not exists: " + username);
        }
        if (withBookings) {
            Hibernate.initialize(user.get().getCopies());
        }

        return user.get();
    }

    public List<User> getAllUsers(boolean withBookings) {

        List<User> users = repo.findAll();
        if (withBookings) {
            users.forEach(user -> Hibernate.initialize(user.getCopies()));
        }
        return users;
    }

    public Boolean existsByUsername(String username) {
        return repo.existsByUsername(username);
    }

    public void updatePassword(String username, String oldPassword, String newPassword) {

        String hashedPassword = passwordEncoder.encode(newPassword);

        if (newPassword.length() < User.MIN_PASSWORD_LENGTH) {
            throw new IllegalArgumentException("Password is too short. Must be at least " + User.MIN_PASSWORD_LENGTH + " long.");
        }

        User user = getUser(username, false);
        if (!passwordEncoder.matches(oldPassword, user.getPasswordHash())) {
            throw new IllegalArgumentException("Old password is wrong.");
        }
        user.setPasswordHash(hashedPassword);

    }
}
