package no.kristiania.pg5100_exam.backend.service;

import no.kristiania.pg5100_exam.backend.StubApplication;
import no.kristiania.pg5100_exam.backend.entity.PlaceholderItem;
import no.kristiania.pg5100_exam.backend.entity.Transaction;
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
class TransactionServiceTest extends ServiceTestBase {

    private int userIdCounter = 0;
    private int itemIdCounter = 0;

    @Autowired
    UserService userService;

    @Autowired
    PlaceholderItemService itemService;

    @Autowired
    TransactionService transactionService;

    private User createUser(String username) {
        return userService.createUser(username, "bar");
    }


    @Test
    public void testRegisterTransaction() {

        String username = "Test_" + userIdCounter++;
        createUser(username);

        String itemName = "TestItem_" + itemIdCounter++;
        Long itemId = itemService.createItem(itemName);

        Transaction transaction = transactionService.registerTransaction(username, itemId);

        User userWithTransaction = userService.getUser(username, true);
        PlaceholderItem itemWithTransaction = itemService.getItem(itemId, true);

        assertEquals(1, userWithTransaction.getTransactions().size());
        assertEquals(1, itemWithTransaction.getTransactions().size());

    }

    @Test
    public void testInvalidUser() {
        String username = "Test_" + userIdCounter++;
//        createUser(username);
        String itemName = "TestItem_" + itemIdCounter++;
        Long itemId = itemService.createItem(itemName);
        assertThrows(IllegalArgumentException.class, () -> transactionService.registerTransaction(username, itemId));
    }

    @Test
    public void testInvalidItem() {
        String username = "Test_" + userIdCounter++;
        createUser(username);
        assertThrows(IllegalArgumentException.class, () -> transactionService.registerTransaction(username, -1L));
    }

    @Test
    public void testGetTransactionsByUsername() {

        String username = "Test_" + userIdCounter++;
        createUser(username);
        String itemName = "TestItem_" + itemIdCounter++;
        Long itemId = itemService.createItem(itemName);

        Transaction transaction = transactionService.registerTransaction(username, itemId);

        User userWithTransaction = userService.getUser(username, true);
        PlaceholderItem itemWithTransaction = itemService.getItem(itemId, true);
        assertEquals(1, userWithTransaction.getTransactions().size());
        assertEquals(1, itemWithTransaction.getTransactions().size());

        List<Transaction> transactions = transactionService.getTransactionsByUsername(username);
        assertEquals(1, transactions.size());
    }

    @Test
    public void testGetTransactionsByItemId() {

        String username = "Test_" + userIdCounter++;
        createUser(username);
        String itemName = "TestItem_" + itemIdCounter++;
        Long itemId = itemService.createItem(itemName);

        Transaction transaction = transactionService.registerTransaction(username, itemId);

        User userWithTransaction = userService.getUser(username, true);
        PlaceholderItem itemWithTransaction = itemService.getItem(itemId, true);
        assertEquals(1, userWithTransaction.getTransactions().size());
        assertEquals(1, itemWithTransaction.getTransactions().size());

        List<Transaction> transactions = transactionService.getTransactionsByItemId(itemId);
        assertEquals(1, transactions.size());

    }

}