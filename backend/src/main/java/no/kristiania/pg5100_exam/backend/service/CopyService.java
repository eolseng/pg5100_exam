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
    private EntityManager em;

    @Autowired
    private CopyRepository repo;

    @Autowired
    private UserService userService;

    @Autowired
    private ItemService itemService;

    public Copy registerBooking(String username, Long tripId) {

        User user = userService.getUser(username, false);
        Item item = itemService.getCard(tripId, false);

        if (item.getValue() > user.getMoney()) {
            throw new IllegalArgumentException("User does not have enough money.");
        }

        // Set new balance
        Long newBalance = user.getMoney() - item.getValue();
        user.setMoney(newBalance);

        // Persist transaction
        Copy copy = new Copy();
        copy.setUser(user);
        copy.setItem(item);
        repo.save(copy);

        return copy;
    }

    public Copy getBooking(Long bookingId) {

        Optional<Copy> booking = repo.findById(bookingId);
        if(booking.isEmpty()) {
            throw new IllegalArgumentException("Booking does not exists: " + booking);
        }

        return booking.get();
    }

    public void cancelBooking(Long bookingId) {

        Copy copy = getBooking(bookingId);
        User user = copy.getUser();
        Item item = copy.getItem();

        Long newBalance = user.getMoney() + item.getValue();
        user.setMoney(newBalance);

        repo.deleteById(bookingId);
    }

    public List<Copy> getTransactionsByUsername(String username) {
        return repo.findAllByUsername(username);
    }

    public List<Copy> getTransactionsByItemId(Long itemId) {
        return repo.findAllByItemId(itemId);
    }
}
