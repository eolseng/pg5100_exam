package no.kristiania.pg5100_exam.backend.service;

import no.kristiania.pg5100_exam.backend.entity.User;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import javax.annotation.PostConstruct;
import java.util.function.Supplier;

@Service
public class DefaultDataInitializerService {

    @Autowired
    private UserService userService;

    @Autowired
    private TripService itemService;

    @Autowired
    private BookingService bookingService;

    @PostConstruct
    public void initialize() {
        User user1 = attempt(() -> userService.createUser("Foo", "Bar"));
        assert user1 != null;
        User user2 = attempt(() -> userService.createUser("James", "Bond"));
        assert user2 != null;
        User user3 = attempt(() -> userService.createUser("Fus", "Ro'Dah"));
        assert user3 != null;

        Long item1Id = attempt(() -> itemService.createTrip("Pistol", 300L));
        Long item2Id = attempt(() -> itemService.createTrip("Dagger", 200L));
        Long item3Id = attempt(() -> itemService.createTrip("Whiskey", 500L));

        attempt(() -> bookingService.registerBooking(user1.getUsername(), item1Id));
        attempt(() -> bookingService.registerBooking(user1.getUsername(), item2Id));
        attempt(() -> bookingService.registerBooking(user1.getUsername(), item3Id));
        attempt(() -> bookingService.registerBooking(user2.getUsername(), item2Id));
        attempt(() -> bookingService.registerBooking(user2.getUsername(), item3Id));
        attempt(() -> bookingService.registerBooking(user3.getUsername(), item3Id));
    }

    private  <T> T attempt(Supplier<T> lambda){
        try{
            return lambda.get();
        }catch (Exception e){
            return null;
        }
    }

}
