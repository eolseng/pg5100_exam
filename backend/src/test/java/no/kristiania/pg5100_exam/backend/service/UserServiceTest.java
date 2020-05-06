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

    @Test
    public void testCreateUser() {

        String username = "Test_" + counter++;
        String password = "bar";

        User user = userService.createUser(username, password);

        assertEquals(username, user.getUsername());
        assertThrows(IllegalArgumentException.class, () ->
                // Already exists
                userService.createUser(username, password)
        );
        assertThrows(IllegalArgumentException.class, () ->
                // Too short password
                userService.updatePassword(username, password, "12")
        );
        assertThrows(IllegalArgumentException.class, () ->
                // Wrong old password
                userService.updatePassword(username, password + 1, "12345")
        );
    }

    @Test
    public void testGetUser() {

        String username = "Test_" + counter++;
        String password = "bar";

        assertThrows(IllegalArgumentException.class, () ->
                userService.getUser(username, false)
        );

        userService.createUser(username, password);

        User user = userService.getUser(username, false);
        assertEquals(username, user.getUsername());
    }

    @Test
    public void testGetAllUsers() {

        String username = "Test_" + counter++;
        String password = "bar";

        List<User> users = userService.getAllUsers(false);
        assertEquals(0, users.size());

        userService.createUser(username, password);
        users = userService.getAllUsers(false);
        assertTrue(users.size() > 0);

    }

    @Test
    public void testExistsByUsername() {

        String username = "Test_" + counter++;
        String password = "bar";

        Boolean exists = userService.existsByUsername(username);
        assertFalse(exists);

        userService.createUser(username, password);
        exists = userService.existsByUsername(username);
        assertTrue(exists);

    }

    @Test
    public void testUpdatePassword() {

        String username = "Test_" + counter++;
        String password = "bar";

        User user = userService.createUser(username, password);
        String oldHash = user.getPasswordHash();

        userService.updatePassword(username, password, "123");
        user = userService.getUser(username, false);
        String newHash = user.getPasswordHash();

        assertNotEquals(oldHash, newHash);
        assertThrows(IllegalArgumentException.class, () ->
                // No existing user
                userService.updatePassword(username + 1, password, "12345")
        );
        assertThrows(IllegalArgumentException.class, () ->
                // Too short password
                userService.updatePassword(username, password, "12")
        );
        assertThrows(IllegalArgumentException.class, () ->
                // Wrong old password
                userService.updatePassword(username, password +1, "123")
        );

    }

    @Test
    public void testHashedPassword() {

        String username = "Test_" + counter++;
        String password = "bar";

        userService.createUser(username, password);
        User user = userService.getUser(username, false);

        assertNotEquals(password, user.getPasswordHash());
    }
}