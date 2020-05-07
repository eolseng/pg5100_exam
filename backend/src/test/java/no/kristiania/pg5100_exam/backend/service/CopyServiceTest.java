package no.kristiania.pg5100_exam.backend.service;

import no.kristiania.pg5100_exam.backend.StubApplication;
import no.kristiania.pg5100_exam.backend.entity.Copy;
import no.kristiania.pg5100_exam.backend.entity.Item;
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

    @Autowired
    UserService userService;

    @Autowired
    ItemService itemService;

    @Autowired
    CopyService copyService;

    @Test
    public void testRegisterCopy() {

        String username = "Test_" + userIdCounter++;
        createUser(username, "bar");

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
        createUser(username, "bar");
        assertThrows(IllegalArgumentException.class, () -> copyService.registerCopy(username, -1L));
    }

    @Test
    public void testGetCopiesByUsername() {

        String username = "Test_" + userIdCounter++;
        createUser(username, "bar");
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
        createUser(username, "bar");
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

    @Test
    public void testMillCopy() {

        String username = "Test_" + userIdCounter++;
        User user = createUser(username, "bar");
        Long itemId = createRandomItem();

        Copy copy = copyService.registerCopy(username, itemId);
        assertEquals(1, copy.getAmount());
        copy = copyService.registerCopy(username, itemId);
        assertEquals(2, copy.getAmount());

        Long copyId = copy.getId();
        Long userBalance = user.getBalance();

        copyService.millCopy(copyId);
        copy = copyService.getCopy(copyId);
        user = userService.getUser(username, false);
        assertEquals(1, copy.getAmount());
        assertTrue(user.getBalance() > userBalance);

        userBalance = user.getBalance();

        copyService.millCopy(copy.getId());
        user = userService.getUser(username, false);
        assertThrows(IllegalArgumentException.class, () ->
                copyService.getCopy(copyId)
        );
        assertTrue(user.getBalance() > userBalance);

        assertThrows(IllegalArgumentException.class, () ->
                copyService.millCopy(copyId)
        );

    }
}