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

    private Long createRandomItem() {

        int counter = itemIdCounter++;
        String itemName = "Test_" + counter;
        Long itemId = itemService.createItem(itemName, "This is the latin name " + counter, 1, "Very painful", 100);
        assertNotNull(itemId);

        return itemId;
    }


    @Test
    public void testRegisterCopy() {

        String username = "Test_" + userIdCounter++;
        createUser(username);

        Long itemId = createRandomItem();

        Copy copy = copyService.registerCopy(username, itemId);
        assertEquals(1, copy.getAmount());
        assertEquals(username, copy.getUser().getUsername());
        assertEquals(itemId, copy.getItem().getId());

        User userWithCopies = userService.getUser(username, true);
        assertEquals(1, userWithCopies.getCopies().size());
        assertEquals(1, userWithCopies.getCopies().get(0).getAmount());

        Item itemWithCopies = itemService.getItem(itemId, true);
        assertEquals(1, itemWithCopies.getCopies().size());
        assertEquals(1, itemWithCopies.getCopies().get(0).getAmount());

        Copy newCopy = copyService.registerCopy(username, itemId);
        assertEquals(2, newCopy.getAmount());

        userWithCopies = userService.getUser(username, true);
        assertEquals(1, userWithCopies.getCopies().size());
        assertEquals(2, userWithCopies.getCopies().get(0).getAmount());

        itemWithCopies = itemService.getItem(itemId, true);
        assertEquals(1, userWithCopies.getCopies().size());
        assertEquals(2, itemWithCopies.getCopies().get(0).getAmount());

    }

    @Test
    public void testInvalidUser() {
        String username = "Test_" + userIdCounter++;
//        createUser(username);
        String itemName = "TestItem_" + itemIdCounter++;
        Long itemId = itemService.createItem(itemName, itemName, 1, "Very painful", 100);
        assertThrows(IllegalArgumentException.class, () -> copyService.registerCopy(username, itemId));
    }

    @Test
    public void testInvalidItem() {
        String username = "Test_" + userIdCounter++;
        createUser(username);
        assertThrows(IllegalArgumentException.class, () -> copyService.registerCopy(username, -1L));
    }

    @Test
    public void testGetCopiesByUsername() {

        String username = "Test_" + userIdCounter++;
        createUser(username);
        String itemName = "TestItem_" + itemIdCounter++;
        Long itemId = itemService.createItem(itemName, itemName, 1, "Very painful", 100);

        Copy copy = copyService.registerCopy(username, itemId);

        User userWithTransaction = userService.getUser(username, true);
        Item itemWithTransaction = itemService.getItem(itemId, true);
        assertEquals(1, userWithTransaction.getCopies().size());
        assertEquals(1, itemWithTransaction.getCopies().size());

        List<Copy> copies = copyService.getCopiesByUsername(username);
        assertEquals(1, copies.size());
    }

    @Test
    public void testGetCopiesByItemId() {

        String username = "Test_" + userIdCounter++;
        createUser(username);
        String itemName = "TestItem_" + itemIdCounter++;
        Long itemId = itemService.createItem(itemName, itemName, 1, "Very painful", 100);

        Copy copy = copyService.registerCopy(username, itemId);

        User userWithCopies = userService.getUser(username, true);
        Item itemWithCopies = itemService.getItem(itemId, true);
        assertEquals(1, userWithCopies.getCopies().size());
        assertEquals(1, itemWithCopies.getCopies().size());

        List<Copy> copies = copyService.getCopiesByItemId(itemId);
        assertEquals(1, copies.size());

    }

}