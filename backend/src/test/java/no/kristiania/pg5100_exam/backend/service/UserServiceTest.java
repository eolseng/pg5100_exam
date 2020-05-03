package no.kristiania.pg5100_exam.backend.service;

import no.kristiania.pg5100_exam.backend.StubApplication;
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
class UserServiceTest extends ServiceTestBase {

    private static int counter = 0;

    @Autowired
    private UserService userService;

    @Autowired
    private PlaceholderItemService itemService;

    @Test
    public void testGetUser() {

        String username = "Test_" + counter++;
        String password = "bar";

        userService.createUser(username, password);
        List<User> users = userService.getAllUsers(false);

        assertTrue(users.size() > 0);
    }

    @Test
    public void testHashedPassword() {

        String username = "Test_" + counter++;
        String password = "bar";

        userService.createUser(username, password);
        User user = userService.getUser(username, false);

        assertNotEquals(password, user.getPassword());
    }

}