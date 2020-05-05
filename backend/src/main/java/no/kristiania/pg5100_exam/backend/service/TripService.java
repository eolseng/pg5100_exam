package no.kristiania.pg5100_exam.backend.service;

import no.kristiania.pg5100_exam.backend.entity.Trip;
import no.kristiania.pg5100_exam.backend.repository.TripRepository;
import org.hibernate.Hibernate;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import javax.persistence.EntityManager;
import javax.persistence.TypedQuery;
import javax.swing.text.html.Option;
import java.util.List;
import java.util.Optional;

@Service
@Transactional
public class TripService {

    @Autowired
    private EntityManager em;

    @Autowired
    private TripRepository repo;

    public Long createTrip(String name, Long cost) {

        // Trip names must be unique
        if (repo.existsByName(name)) {
            throw new IllegalArgumentException("A trip with that name already exists: " + name);
        }

        Trip trip = new Trip();
        trip.setName(name);
        trip.setCost(cost);
        repo.save(trip);

        return trip.getId();
    }

    public Trip getTrip(Long id, boolean withBookings) {

        Optional<Trip> trip = repo.findById(id);
        if (trip.isEmpty()) {
            throw new IllegalArgumentException("Trip with that ID does not exists: " + id);
        }
        if (withBookings) {
            Hibernate.initialize(trip.get().getBookings());
        }
        return trip.get();
    }

    public List<Trip> getAllTrips(boolean withBookings) {

        List<Trip> items = repo.findAll();
        if(withBookings) {
            items.forEach(item -> Hibernate.initialize(item.getBookings()));
        }
        return items;
    }
}
