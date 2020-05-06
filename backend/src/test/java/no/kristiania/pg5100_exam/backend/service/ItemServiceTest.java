package no.kristiania.pg5100_exam.backend.service;

import no.kristiania.pg5100_exam.backend.StubApplication;
import no.kristiania.pg5100_exam.backend.entity.Item;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit.jupiter.SpringExtension;

import java.util.HashSet;
import java.util.List;
import java.util.Set;

import static org.junit.jupiter.api.Assertions.*;

@ExtendWith(SpringExtension.class)
@SpringBootTest(
        classes = StubApplication.class,
        webEnvironment = SpringBootTest.WebEnvironment.NONE
)
class ItemServiceTest extends ServiceTestBase {

    @Autowired
    private ItemService itemService;

    @Test
    public void testGetItem() {

        String itemName = "Test_" + itemIdCounter++;
        Long itemId = itemService.createItem(itemName, itemName, 1, "Very painful", 100);
        assertNotNull(itemId);

        Item item = itemService.getItem(itemId, false);
        assertNotNull(item);

    }

    @Test
    public void testGetAllItems() {
        String itemName = "Test_" + itemIdCounter++;
        Long itemId = itemService.createItem(itemName, itemName, 1, "Very painful", 100);
        assertNotNull(itemId);

        List<Item> items = itemService.getAllItems(false);
        assertTrue(items.size() > 0);
    }

    @Test
    public void testGetRandomItems() {

        for (int i = 0; i < 15; i++) {
            createRandomItem();
        }

        int amount = 3;
        boolean foundAllUnique = false;
        boolean foundDuplicate = false;

        for (int i = 0; i < 100; i++) {
            // Limit to 100 iterations to avoid eternal looping in case of bugs.

            List<Item> randomItems = itemService.getRandomItems(amount, false);
            assertEquals(amount, randomItems.size());

            Set<Long> ids = new HashSet<>();

            for (Item item : randomItems) {
                if (ids.contains(item.getId())) {
                    // Check if ID is already in the set - if so duplicates has been found
                    foundDuplicate = true;
                }
                ids.add(item.getId());
            }

            if (ids.size() == amount) {
                // Check if 'amount' unique IDs have been added to the set
                foundAllUnique = true;
            }

            if (foundAllUnique && foundDuplicate) {
                // Break if both conditions are met
                break;
            }
        }
        assertTrue(foundAllUnique);
        assertTrue(foundDuplicate);
    }
}