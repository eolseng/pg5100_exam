package no.kristiania.pg5100_exam.backend.service;

import no.kristiania.pg5100_exam.backend.StubApplication;
import no.kristiania.pg5100_exam.backend.entity.Trip;
import no.kristiania.pg5100_exam.backend.entity.Booking;
import no.kristiania.pg5100_exam.backend.entity.User;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit.jupiter.SpringExtension;

import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

@ExtendWith(SpringExtension.class)
@SpringBootTest(
        classes = StubApplication.class,
        webEnvironment = SpringBootTest.WebEnvironment.NONE
)
class BookingServiceTest extends ServiceTestBase {

    private int userIdCounter = 0;
    private int itemIdCounter = 0;

    @Autowired
    UserService userService;

    @Autowired
    TripService itemService;

    @Autowired
    BookingService bookingService;

    private User createUser(String username) {
        return userService.createUser(username, "bar");
    }


    @Test
    public void testRegisterTransaction() {

        String username = "Test_" + userIdCounter++;
        createUser(username);

        String itemName = "TestItem_" + itemIdCounter++;
        Long itemId = itemService.createTrip(itemName, 0L);

        Booking booking = bookingService.registerBooking(username, itemId);

        User userWithTransaction = userService.getUser(username, true);
        Trip itemWithTransaction = itemService.getTrip(itemId, true);

        assertEquals(1, userWithTransaction.getBookings().size());
        assertEquals(1, itemWithTransaction.getBookings().size());

    }

    @Test
    public void testInvalidUser() {
        String username = "Test_" + userIdCounter++;
//        createUser(username);
        String itemName = "TestItem_" + itemIdCounter++;
        Long itemId = itemService.createTrip(itemName, 0L);
        assertThrows(IllegalArgumentException.class, () -> bookingService.registerBooking(username, itemId));
    }

    @Test
    public void testInvalidItem() {
        String username = "Test_" + userIdCounter++;
        createUser(username);
        assertThrows(IllegalArgumentException.class, () -> bookingService.registerBooking(username, -1L));
    }

    @Test
    public void testGetTransactionsByUsername() {

        String username = "Test_" + userIdCounter++;
        createUser(username);
        String itemName = "TestItem_" + itemIdCounter++;
        Long itemId = itemService.createTrip(itemName, 0L);

        Booking booking = bookingService.registerBooking(username, itemId);

        User userWithTransaction = userService.getUser(username, true);
        Trip itemWithTransaction = itemService.getTrip(itemId, true);
        assertEquals(1, userWithTransaction.getBookings().size());
        assertEquals(1, itemWithTransaction.getBookings().size());

        List<Booking> bookings = bookingService.getTransactionsByUsername(username);
        assertEquals(1, bookings.size());
    }

    @Test
    public void testGetTransactionsByItemId() {

        String username = "Test_" + userIdCounter++;
        createUser(username);
        String itemName = "TestItem_" + itemIdCounter++;
        Long itemId = itemService.createTrip(itemName, 0L);

        Booking booking = bookingService.registerBooking(username, itemId);

        User userWithTransaction = userService.getUser(username, true);
        Trip itemWithTransaction = itemService.getTrip(itemId, true);
        assertEquals(1, userWithTransaction.getBookings().size());
        assertEquals(1, itemWithTransaction.getBookings().size());

        List<Booking> bookings = bookingService.getTransactionsByItemId(itemId);
        assertEquals(1, bookings.size());

    }

}