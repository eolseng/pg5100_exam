package no.kristiania.pg5100_exam.backend.service;

import no.kristiania.pg5100_exam.backend.StubApplication;
import no.kristiania.pg5100_exam.backend.entity.Item;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit.jupiter.SpringExtension;

import java.util.List;

import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertTrue;

@ExtendWith(SpringExtension.class)
@SpringBootTest(
        classes = StubApplication.class,
        webEnvironment = SpringBootTest.WebEnvironment.NONE
)
class ItemServiceTest extends ServiceTestBase {

    private int itemIdCounter = 0;

    @Autowired
    private ItemService itemService;

    @Test
    public void testGetItem() {

        String itemName = "Test_" + itemIdCounter++;
        Long itemId = itemService.createCard(itemName, itemName, 1, "Very painful", 100);
        assertNotNull(itemId);

        Item item = itemService.getCard(itemId, false);
        assertNotNull(item);

    }

    @Test
    public void testGetAllItems() {
        String itemName = "Test_" + itemIdCounter++;
        Long itemId = itemService.createCard(itemName, itemName, 1, "Very painful", 100);
        assertNotNull(itemId);

        List<Item> items = itemService.getAllCards(false);
        assertTrue(items.size() > 0);
    }
}