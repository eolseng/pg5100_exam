package no.kristiania.pg5100_exam.backend.service;

import no.kristiania.pg5100_exam.backend.entity.Copy;
import no.kristiania.pg5100_exam.backend.entity.Item;
import no.kristiania.pg5100_exam.backend.entity.User;
import org.junit.jupiter.api.BeforeEach;
import org.springframework.beans.factory.annotation.Autowired;

import static org.junit.jupiter.api.Assertions.assertNotNull;

public class ServiceTestBase {

    public int userIdCounter = 0;
    public int itemIdCounter = 0;


    @Autowired
    private ResetService resetService;

    @Autowired
    private UserService userService;

    @Autowired
    private ItemService itemService;

    @Autowired
    private CopyService copyService;

    @BeforeEach
    public void cleanDatabase() {
        resetService.resetDatabase();
    }

    public User createUser(String username, String password) {
        return userService.createUser(username, password);
    }

    public Long createRandomItem() {

        int counter = itemIdCounter++;
        String itemName = "Test_" + counter;
        Long itemId = itemService.createItem(itemName, "This is the latin name " + counter, 1, "Very painful", 100);
        assertNotNull(itemId);

        return itemId;
    }

    public Copy registerCopy(String username, Long itemId) {
        return copyService.registerCopy(username, itemId);
    }

}
