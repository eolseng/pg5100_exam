package no.kristiania.pg5100_exam.backend.service;

import no.kristiania.pg5100_exam.backend.entity.Booking;
import no.kristiania.pg5100_exam.backend.entity.Trip;
import no.kristiania.pg5100_exam.backend.entity.User;
import no.kristiania.pg5100_exam.backend.repository.BookingRepository;
import org.hibernate.Hibernate;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import javax.persistence.EntityManager;
import java.util.Date;
import java.util.List;
import java.util.Optional;

@Service
@Transactional
public class BookingService {

    @Autowired
    private EntityManager em;

    @Autowired
    private BookingRepository repo;

    @Autowired
    private UserService userService;

    @Autowired
    private TripService tripService;

    public Booking registerBooking(String username, Long tripId) {

        User user = userService.getUser(username, false);
        Trip trip = tripService.getTrip(tripId, false);

        if (trip.getCost() > user.getMoney()) {
            throw new IllegalArgumentException("User does not have enough money.");
        }

        // Set new balance
        Long newBalance = user.getMoney() - trip.getCost();
        user.setMoney(newBalance);

        // Persist transaction
        Booking booking = new Booking();
        booking.setCreatedAt(new Date());
        booking.setUser(user);
        booking.setTrip(trip);
        repo.save(booking);

        return booking;
    }

    public Booking getBooking(Long bookingId) {

        Optional<Booking> booking = repo.findById(bookingId);
        if(booking.isEmpty()) {
            throw new IllegalArgumentException("Booking does not exists: " + booking);
        }

        return booking.get();
    }

    public void cancelBooking(Long bookingId) {

        Booking booking = getBooking(bookingId);
        User user = booking.getUser();
        Trip trip = booking.getTrip();

        Long newBalance = user.getMoney() + trip.getCost();
        user.setMoney(newBalance);

        repo.deleteById(bookingId);
    }

    public List<Booking> getTransactionsByUsername(String username) {
        return repo.findAllByUsername(username);
    }

    public List<Booking> getTransactionsByItemId(Long itemId) {
        return repo.findAllByItemId(itemId);
    }
}
