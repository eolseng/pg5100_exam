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
import java.util.Set;
import java.util.stream.Collectors;

import static org.junit.jupiter.api.Assertions.*;

@ExtendWith(SpringExtension.class)
@SpringBootTest(
        classes = StubApplication.class,
        webEnvironment = SpringBootTest.WebEnvironment.NONE
)
class CardPackServiceTest extends ServiceTestBase {

    @Autowired
    private CardPackService cardPackService;

    @Autowired
    private UserService userService;

    @Test
    void testOpenCardPack() {

        String username = "Test_" + userIdCounter++;
        User user = createUser(username, "bar");
        int originalCardPackAmount = user.getCardPacks();
        assertTrue(originalCardPackAmount > 0);

        for (int i = 0; i < 15; i++) {
            // Create 15 random items to choose from
            createRandomItem();
        }

        // Open a card pack
        List<Copy> randomCards = cardPackService.openCardPack(username);
        assertEquals(CardPackService.CARD_PACK_SIZE, randomCards.size());

        // Refresh user and check that a card pack has been opened
        User refreshedUser = userService.getUser(username, true);
        assertEquals(originalCardPackAmount - 1, refreshedUser.getCardPacks());

        // Check that the user has the new cards in his collection
        Set<Long> userCopies = refreshedUser.getCopies().stream()
                .map(Copy::getId)
                .collect(Collectors.toSet());
        randomCards.forEach(copy -> {
            assertTrue(userCopies.contains(copy.getId()));
        });
    }

    @Test
    public void testOpenTooManyCardPacks() {

        String username = "Test_" + userIdCounter++;
        User user = createUser(username, "bar");
        int cardPacks = user.getCardPacks();
        assertTrue(cardPacks > 0);

        for (int i = 0; i < 15; i++) {
            // Create 15 random items to choose from
            createRandomItem();
        }

        while (cardPacks > 0) {
            cardPackService.openCardPack(username);
            cardPacks--;
        }

        User refreshedUser = userService.getUser(username, false);
        assertEquals(0, refreshedUser.getCardPacks());
        assertThrows(IllegalArgumentException.class, () ->
                cardPackService.openCardPack(username)
        );
    }

    @Test
    public void testPurchaseCardPack() {

        String username = "Test_" + userIdCounter++;
        User user = createUser(username, "bar");
        int cardPacks = user.getCardPacks();
        Long balance = user.getBalance();

        cardPackService.purchaseCardPack(username);
        User refreshedUser = userService.getUser(username, false);
        assertTrue(refreshedUser.getCardPacks() > cardPacks);
        assertTrue(refreshedUser.getBalance() < balance);

        while (userService.getUser(username, false).getBalance() >= CardPackService.CARD_PACK_PRICE) {
            cardPackService.purchaseCardPack(username);
        }
        assertThrows(IllegalArgumentException.class, () -> cardPackService.purchaseCardPack(username));
    }
}