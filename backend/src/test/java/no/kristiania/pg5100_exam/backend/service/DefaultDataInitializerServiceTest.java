package no.kristiania.pg5100_exam.backend.service;

import no.kristiania.pg5100_exam.backend.StubApplication;
import no.kristiania.pg5100_exam.backend.entity.Item;
import no.kristiania.pg5100_exam.backend.entity.User;
import org.hibernate.LazyInitializationException;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.annotation.DirtiesContext;
import org.springframework.test.context.junit.jupiter.SpringExtension;

import java.util.List;

import static org.junit.jupiter.api.Assertions.*;
import static org.springframework.test.annotation.DirtiesContext.ClassMode.BEFORE_CLASS;

@ExtendWith(SpringExtension.class)
@SpringBootTest(
        classes = StubApplication.class,
        webEnvironment = SpringBootTest.WebEnvironment.NONE
)
@DirtiesContext(classMode = BEFORE_CLASS)
class DefaultDataInitializerServiceTest extends ServiceTestBase {

    @Autowired
    private DefaultDataInitializerService dataInitializerService;

    @Autowired
    private UserService userService;

    @Autowired
    private ItemService itemService;

    @Test
    public void testInit() {

        // Need to have a clean database (from the ServiceTestBase) before initializing production data.
        dataInitializerService.initialize();

        List<User> users = userService.getAllUsers(true);
        assertTrue(users.size() > 0);
        assertTrue(users.get(0).getCopies().size() > 0);

        String username = users.get(0).getUsername();

        assertThrows(LazyInitializationException.class, () ->
                userService.getUser(username, false).getCopies().size()
        );

        User userWithBookings = userService.getUser(username, true);
        assertTrue(userWithBookings.getCopies().size() > 0);


        List<Item> tripsWithBookings = itemService.getAllCards(true);
        assertTrue(tripsWithBookings.size() > 0);
        assertTrue(tripsWithBookings.get(0).getCopies().size() > 0);
    }
}