package no.kristiania.pg5100_exam.backend.service;

import no.kristiania.pg5100_exam.backend.entity.PlaceholderItem;
import no.kristiania.pg5100_exam.backend.entity.Transaction;
import no.kristiania.pg5100_exam.backend.entity.User;
import no.kristiania.pg5100_exam.backend.repository.TransactionRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import javax.persistence.EntityManager;
import java.util.Date;
import java.util.List;

@Service
@Transactional
public class TransactionService {

    @Autowired
    private EntityManager em;

    @Autowired
    private TransactionRepository repository;

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

    public List<Transaction> getTransactionsByUsername(String username) {
        return repository.findAllByUsername(username);
    }

    public List<Transaction> getTransactionsByItemId(Long itemId) {
        return repository.findAllByItemId(itemId);
    }
}
