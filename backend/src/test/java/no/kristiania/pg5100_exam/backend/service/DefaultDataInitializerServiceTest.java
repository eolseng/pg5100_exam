package no.kristiania.pg5100_exam.backend.service;

import no.kristiania.pg5100_exam.backend.StubApplication;
import no.kristiania.pg5100_exam.backend.entity.Item;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.annotation.DirtiesContext;
import org.springframework.test.context.junit.jupiter.SpringExtension;

import java.util.List;

import static org.junit.jupiter.api.Assertions.*;
import static org.springframework.test.annotation.DirtiesContext.ClassMode.BEFORE_CLASS;

@ExtendWith(SpringExtension.class)
@SpringBootTest(
        classes = StubApplication.class,
        webEnvironment = SpringBootTest.WebEnvironment.NONE
)
@DirtiesContext(classMode = BEFORE_CLASS)
class DefaultDataInitializerServiceTest extends ServiceTestBase {

    @Autowired
    private DefaultDataInitializerService dataInitializerService;

    @Autowired
    private ItemService itemService;

    @Test
    public void testInit() {

        // Need to have a clean database (from the ServiceTestBase) before initializing production data.
        dataInitializerService.initialize();

        List<Item> items = itemService.getAllItems(true);
        assertEquals(30, items.size(), "There are less than 30 cards in the database.");

    }
}