package no.kristiania.pg5100_exam.backend.service;

import no.kristiania.pg5100_exam.backend.entity.PlaceholderItem;
import no.kristiania.pg5100_exam.backend.entity.Transaction;
import no.kristiania.pg5100_exam.backend.entity.User;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import javax.persistence.EntityManager;
import java.util.Date;

@Service
@Transactional
public class TransactionService {

    @Autowired
    private EntityManager em;

    public Transaction registerTransaction(String username, Long itemId) {

        User user = em.find(User.class, username);
        if (user == null) {
            throw new IllegalArgumentException("No existing user: " + username);
        }

        PlaceholderItem item = em.find(PlaceholderItem.class, itemId);
        if (item == null) {
            throw new IllegalArgumentException("No existing item: " + itemId);
        }

        Transaction transaction = new Transaction();
        transaction.setCreatedAt(new Date());
        transaction.setUser(user);
        transaction.setItem(item);
        em.persist(transaction);

        return transaction;
    }
}
