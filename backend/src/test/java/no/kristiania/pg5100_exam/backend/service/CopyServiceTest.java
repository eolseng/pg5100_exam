package no.kristiania.pg5100_exam.backend.service;

import no.kristiania.pg5100_exam.backend.StubApplication;
import no.kristiania.pg5100_exam.backend.entity.Item;
import no.kristiania.pg5100_exam.backend.entity.Copy;
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
class CopyServiceTest extends ServiceTestBase {

    private int userIdCounter = 0;
    private int itemIdCounter = 0;

    @Autowired
    UserService userService;

    @Autowired
    ItemService itemService;

    @Autowired
    CopyService copyService;

    private User createUser(String username) {
        return userService.createUser(username, "bar");
    }


    @Test
    public void testRegisterTransaction() {

        String username = "Test_" + userIdCounter++;
        createUser(username);

        String itemName = "TestItem_" + itemIdCounter++;
        Long itemId = itemService.createCard(itemName, itemName, 1, "Very painful", 100);

        Copy copy = copyService.registerBooking(username, itemId);

        User userWithTransaction = userService.getUser(username, true);
        Item itemWithTransaction = itemService.getCard(itemId, true);

        assertEquals(1, userWithTransaction.getCopies().size());
        assertEquals(1, itemWithTransaction.getCopies().size());

    }

    @Test
    public void testInvalidUser() {
        String username = "Test_" + userIdCounter++;
//        createUser(username);
        String itemName = "TestItem_" + itemIdCounter++;
        Long itemId = itemService.createCard(itemName, itemName, 1, "Very painful", 100);
        assertThrows(IllegalArgumentException.class, () -> copyService.registerBooking(username, itemId));
    }

    @Test
    public void testInvalidItem() {
        String username = "Test_" + userIdCounter++;
        createUser(username);
        assertThrows(IllegalArgumentException.class, () -> copyService.registerBooking(username, -1L));
    }

    @Test
    public void testGetTransactionsByUsername() {

        String username = "Test_" + userIdCounter++;
        createUser(username);
        String itemName = "TestItem_" + itemIdCounter++;
        Long itemId = itemService.createCard(itemName, itemName, 1, "Very painful", 100);

        Copy copy = copyService.registerBooking(username, itemId);

        User userWithTransaction = userService.getUser(username, true);
        Item itemWithTransaction = itemService.getCard(itemId, true);
        assertEquals(1, userWithTransaction.getCopies().size());
        assertEquals(1, itemWithTransaction.getCopies().size());

        List<Copy> copies = copyService.getTransactionsByUsername(username);
        assertEquals(1, copies.size());
    }

    @Test
    public void testGetTransactionsByItemId() {

        String username = "Test_" + userIdCounter++;
        createUser(username);
        String itemName = "TestItem_" + itemIdCounter++;
        Long itemId = itemService.createCard(itemName, itemName, 1, "Very painful", 100);

        Copy copy = copyService.registerBooking(username, itemId);

        User userWithTransaction = userService.getUser(username, true);
        Item itemWithTransaction = itemService.getCard(itemId, true);
        assertEquals(1, userWithTransaction.getCopies().size());
        assertEquals(1, itemWithTransaction.getCopies().size());

        List<Copy> copies = copyService.getTransactionsByItemId(itemId);
        assertEquals(1, copies.size());

    }

}