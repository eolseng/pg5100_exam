package no.kristiania.pg5100_exam.backend.service;

import no.kristiania.pg5100_exam.backend.StubApplication;
import no.kristiania.pg5100_exam.backend.entity.PlaceholderItem;
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
class PlaceholderItemServiceTest extends ServiceTestBase {

    private int itemIdCounter = 0;

    @Autowired
    private PlaceholderItemService itemService;

    @Test
    public void testGetItem() {

        String itemName = "Test_" + itemIdCounter++;
        Long itemId = itemService.createItem(itemName, 0L);
        assertNotNull(itemId);

        PlaceholderItem item = itemService.getItem(itemId, false);
        assertNotNull(item);

    }

    @Test
    public void testGetAllItems() {
        String itemName = "Test_" + itemIdCounter++;
        Long itemId = itemService.createItem(itemName, 0L);
        assertNotNull(itemId);

        List<PlaceholderItem> items = itemService.getAllItems(false);
        assertTrue(items.size() > 0);
    }
}