package no.kristiania.pg5100_exam.backend.service;

import no.kristiania.pg5100_exam.backend.entity.Item;
import no.kristiania.pg5100_exam.backend.entity.Copy;
import no.kristiania.pg5100_exam.backend.entity.User;
import no.kristiania.pg5100_exam.backend.repository.CopyRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import javax.persistence.EntityManager;
import java.util.List;
import java.util.Optional;

@Service
@Transactional
public class CopyService {

    @Autowired
    private CopyRepository repo;

    @Autowired
    private UserService userService;

    @Autowired
    private ItemService itemService;

    public Copy registerCopy(String username, Long itemId) {

        User user = userService.getUser(username, false);
        Item item = itemService.getItem(itemId, false);

        Copy copy;
        if(repo.existsByUserAndItem(user, item)) {
            copy = repo.findFirstByUserAndItem(user, item);
            copy.setAmount(copy.getAmount() + 1);
        } else {
            copy = new Copy();
            copy.setUser(user);
            copy.setItem(item);
            copy.setAmount(1);
            repo.save(copy);
        }

        return copy;
    }

    public Copy getCopy(Long copyId) {

        Optional<Copy> copy = repo.findById(copyId);
        if(copy.isEmpty()) {
            throw new IllegalArgumentException("Copy does not exists: " + copy);
        }

        return copy.get();
    }

    public void millCopy(Long copyId) {

        Copy copy = getCopy(copyId);
        User user = copy.getUser();
        Item item = copy.getItem();

        if (copy.getAmount() <= 1) {
            repo.deleteById(copyId);
        } else {
            copy.setAmount(copy.getAmount() - 1);
        }

        Long newBalance = user.getBalance() + item.getValue();
        user.setBalance(newBalance);
    }

    public List<Copy> getCopiesByUsername(String username) {
        return repo.findAllByUsername(username);
    }

    public List<Copy> getCopiesByItemId(Long itemId) {
        return repo.findAllByItemId(itemId);
    }
}
