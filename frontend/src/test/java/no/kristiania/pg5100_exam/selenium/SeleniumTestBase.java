package no.kristiania.pg5100_exam.selenium;

import no.kristiania.pg5100_exam.backend.entity.Item;
import no.kristiania.pg5100_exam.backend.service.CardPackService;
import no.kristiania.pg5100_exam.backend.service.ItemService;
import no.kristiania.pg5100_exam.backend.service.UserService;
import no.kristiania.pg5100_exam.selenium.po.IndexPO;
import no.kristiania.pg5100_exam.selenium.po.LogInPO;
import no.kristiania.pg5100_exam.selenium.po.SignUpPO;
import no.kristiania.pg5100_exam.selenium.po.admin.AdminPO;
import no.kristiania.pg5100_exam.selenium.po.ui.CardPackPO;
import no.kristiania.pg5100_exam.selenium.po.ui.ChangePasswordPO;
import no.kristiania.pg5100_exam.selenium.po.ui.CollectionPO;
import no.kristiania.pg5100_exam.selenium.po.ui.ProfilePO;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.openqa.selenium.By;
import org.openqa.selenium.NoSuchElementException;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.springframework.beans.factory.annotation.Autowired;

import java.util.List;
import java.util.concurrent.atomic.AtomicInteger;

import static org.junit.jupiter.api.Assertions.*;

public abstract class SeleniumTestBase {

    private static final AtomicInteger counter = new AtomicInteger(0);
    @Autowired
    UserService userService;
    @Autowired
    ItemService itemService;
    private IndexPO home;

    protected abstract WebDriver getDriver();

    protected abstract String getServerHost();

    protected abstract int getServerPort();

    private String getUniqueId() {
        return "test_unique_id_selenium_" + counter.getAndIncrement();
    }

    private IndexPO createNewUser(String username, String password) {

        home.toStartingPage();

        SignUpPO signUpPO = home.toSignUp();
        IndexPO indexPO = signUpPO.createUser(username, password);
        assertNotNull(indexPO);

        return indexPO;
    }

    @BeforeEach
    public void initTest() {

        // Clear all cookies before each new test for a fresh session.
        getDriver().manage().deleteAllCookies();

        home = new IndexPO(getDriver(), getServerHost(), getServerPort());
        home.toStartingPage();

        assertTrue(home.isOnPage(), "Failed to start from Home Page");
        assertFalse(home.isLoggedIn());
    }

    @Test
    public void testCreateUser() {

        String username = getUniqueId();
        String password = "12345";

        home = createNewUser(username, password);
        assertTrue(home.isLoggedIn());
        assertTrue(home.getDriver().getPageSource().contains(username));

    }

    @Test
    public void testFailCreateUser() {

        String username = getUniqueId();
        String password = "12345";

        SignUpPO po = home.toSignUp();
        assertTrue(po.isOnPage());

        po.setText("username", username);
        po.setText("password", password);
        po.setText("confirm-password", password + 1);
        po.clickAndWait("submit-btn");

        assertTrue(po.getDriver().getPageSource().contains("Passwords do not match"));
    }

    @Test
    public void testDoLogoutAndLogIn() {

        String username = getUniqueId();
        String password = "12345";

        home = createNewUser(username, password);
        assertTrue(home.isLoggedIn());
        assertTrue(home.getDriver().getPageSource().contains(username));

        home.doLogout();
        assertFalse(home.isLoggedIn());
        assertFalse(home.getDriver().getPageSource().contains(username));

        LogInPO logInPO = home.toLogIn();
        home = logInPO.doLogIn(username, password);
        assertTrue(home.isLoggedIn());
        assertTrue(home.getDriver().getPageSource().contains(username));

    }

    @Test
    public void testUserProfile() {

        String username = getUniqueId();
        String password = "12345";

        home = createNewUser(username, password);
        assertTrue(home.isLoggedIn());
        assertTrue(home.getDriver().getPageSource().contains(username));

        ProfilePO po = home.toProfile();
        assertTrue(po.isOnPage());
        assertFalse(po.hasTransactions());

    }

    @Test
    public void testChangePassword() {

        String username = getUniqueId();
        String password = "12345";

        home = createNewUser(username, password);
        assertTrue(home.isLoggedIn());

        // Navigate to profile page
        ProfilePO profilePO = home.toProfile();
        assertTrue(profilePO.isOnPage());

        // Navigate to change password page
        ChangePasswordPO changePasswordPO = profilePO.toChangePassword();
        assertTrue(changePasswordPO.isOnPage());

        // Input wrong old password
        changePasswordPO.setText("old-password", password + 1);
        changePasswordPO.setText("new-password", password);
        changePasswordPO.setText("confirm-password", password);
        changePasswordPO.clickAndWait("submit-btn");
        assertTrue(changePasswordPO.getDriver().getPageSource().contains("ERROR: Wrong old password"));

        // Input mismatching passwords
        changePasswordPO.setText("old-password", password);
        changePasswordPO.setText("new-password", password);
        changePasswordPO.setText("confirm-password", password + 1);
        changePasswordPO.clickAndWait("submit-btn");
        assertTrue(changePasswordPO.getDriver().getPageSource().contains("ERROR: Passwords do not match"));

        // Successful change password
        String newPassword = "54321";
        profilePO = changePasswordPO.doChangePassword(password, newPassword);
        assertTrue(profilePO.isOnPage());
        assertTrue(profilePO.getDriver().getPageSource().contains("Password updated successfully!"));

        // Logout
        home.doLogout();
        assertFalse(home.isLoggedIn());

        // Confirm new password is set
        LogInPO logInPO = home.toLogIn();
        home = logInPO.doLogIn(username, newPassword);
        assertTrue(home.isLoggedIn());
    }

    @Test
    public void testDisplayHomePage() {

        String pageSource = home.getDriver().getPageSource();

        List<Item> items = itemService.getAllItems(false);
        items.forEach(item -> assertTrue(pageSource.contains(item.getName())));

    }

    @Test
    public void testEmptyCollection() {

        String username = getUniqueId();
        String password = "123";

        home = createNewUser(username, password);

        CollectionPO po = home.toCollection();
        assertTrue(po.isOnPage());

        int cardPacks = userService.getUser(username, false).getCardPacks();
        assertFalse(po.hasCards());
        assertEquals(cardPacks, po.getCardPackCount());

    }

    @Test
    public void testRedeemLootBox() {

        String username = getUniqueId();
        String password = "123";
        home = createNewUser(username, password);

        int originalCardPacks = home.getCardPackCount();
        int originalCollectionSize = home.getTotalCardCount();

        CollectionPO collectionPO = home.toCollection();

        CardPackPO cardPackPO = collectionPO.toOpenCardPack();
        assertEquals(CardPackService.CARD_PACK_SIZE, cardPackPO.getNewCardCount());
        assertTrue(cardPackPO.getCardPackCount() < originalCardPacks);
        assertTrue(cardPackPO.getTotalCardCount() > originalCollectionSize);

    }

    @Test
    public void testFailedRedeemLootBox() {

        String username = getUniqueId();
        String password = "123";
        home = createNewUser(username, password);

        CollectionPO collectionPO = home.toCollection();

        CardPackPO cardPackPO = collectionPO.toOpenCardPack();
        while (cardPackPO.getCardPackCount() > 0) {
            cardPackPO = cardPackPO.openAnotherCardPack();
        }
        assertEquals(0, cardPackPO.getCardPackCount());

        assertFalse(cardPackPO.getDriver().getPageSource().contains("OPEN ANOTHER CARD PACK"));
    }

    @Test
    public void testMillItem() {

        String username = getUniqueId();
        String password = "123";
        home = createNewUser(username, password);

        CollectionPO collectionPO = home.toCollection();

        CardPackPO cardPackPO = collectionPO.toOpenCardPack();
        while (cardPackPO.getCardPackCount() > 0) {
            cardPackPO = cardPackPO.openAnotherCardPack();
        }
        assertEquals(0, cardPackPO.getCardPackCount());
        collectionPO = cardPackPO.toCollection();

        while (collectionPO.hasCards()) {

            WebElement card = getDriver().findElements(By.className("copy-container")).get(0);
            int copyId = Integer.parseInt(card.getAttribute("id").split("_")[2]);
            int amount = Integer.parseInt(card.findElement(By.id("copy_amount_" + copyId)).getText().split(" ")[1]);
            int balance = collectionPO.getBalance();

            collectionPO = collectionPO.millCopy(copyId);
            assertTrue(balance < collectionPO.getBalance());

            if (amount > 1) {
                int newAmount = Integer.parseInt(getDriver().findElement(By.id("copy_amount_" + copyId)).getText().split(" ")[1]);
                assertEquals(amount - 1, newAmount);
            } else {
                assertThrows(NoSuchElementException.class, () -> getDriver().findElement(By.id("copy_id_" + copyId)));
            }
        }
    }

    @Test
    public void testBuyLootbox() {

        String username = getUniqueId();
        String password = "123";
        home = createNewUser(username, password);

        CollectionPO collectionPO = home.toCollection();

        int balance = collectionPO.getBalance();
        int cardPacks = collectionPO.getCardPackCount();

        collectionPO = collectionPO.purchaseCardPack();

        assertEquals(cardPacks + 1, collectionPO.getCardPackCount());
        assertEquals(balance - CardPackService.CARD_PACK_PRICE, collectionPO.getBalance());
        assertTrue(getDriver().getPageSource().contains("Congratulations! You just bought a new Card Pack!"));

    }

    @Test
    public void testSortCollection() {

        String username = getUniqueId();
        String password = "123";
        home = createNewUser(username, password);

        CollectionPO collectionPO = home.toCollection();

        CardPackPO cardPackPO = collectionPO.toOpenCardPack();
        while (cardPackPO.getCardPackCount() > 0) {
            cardPackPO = cardPackPO.openAnotherCardPack();
        }
        assertEquals(0, cardPackPO.getCardPackCount());
        collectionPO = cardPackPO.toCollection();

        // Sort alphabetically
        collectionPO.sortBy("alphabetical");
        List<WebElement> cards = collectionPO.getDriver().findElements(By.className("copy-container"));
        for (int i = 0; i < cards.size() - 1; i++) {
            String cardName = cards.get(i).findElements(By.className("card-name")).get(0).getText();
            String nextCardName = cards.get(i + 1).findElements(By.className("card-name")).get(0).getText();
            assertTrue(cardName.compareToIgnoreCase(nextCardName) <= 0);
        }

        // Sort by copies
        collectionPO.sortBy("copies");
        cards = collectionPO.getDriver().findElements(By.className("copy-container"));
        for (int i = 0; i < cards.size() - 1; i++) {
            int copyAmount = Integer.parseInt(cards.get(i).findElements(By.className("copy-amount")).get(0).getText().split(" ")[1]);
            int nextCopyAmount = Integer.parseInt(cards.get(i + 1).findElements(By.className("copy-amount")).get(0).getText().split(" ")[1]);

            System.out.println(copyAmount + " : " + nextCopyAmount);
            assertTrue(copyAmount <= nextCopyAmount);
        }

        // Sort by painLevel
        collectionPO.sortBy("painLevel");
        cards = collectionPO.getDriver().findElements(By.className("copy-container"));
        for (int i = 0; i < cards.size() - 1; i++) {
            int cardPainLevel = Integer.parseInt(cards.get(i).findElements(By.className("card-pain-level")).get(0).getText().split(" ")[2]);
            int nextCardPainLevel = Integer.parseInt(cards.get(i + 1).findElements(By.className("card-pain-level")).get(0).getText().split(" ")[2]);
            assertTrue(cardPainLevel <= nextCardPainLevel);
        }

        // Sort by value
        collectionPO.sortBy("value");
        cards = collectionPO.getDriver().findElements(By.className("copy-container"));
        for (int i = 0; i < cards.size() - 1; i++) {
            int cardValue = Integer.parseInt(cards.get(i).findElements(By.className("card-value")).get(0).getText().split("\\$")[1]);
            int nextCardValue = Integer.parseInt(cards.get(i + 1).findElements(By.className("card-value")).get(0).getText().split("\\$")[1]);
            assertTrue(cardValue <= nextCardValue);
        }
    }

    @Test
    public void testCreateNewCard() {

        LogInPO loginPO = home.toLogIn();
        assertFalse(loginPO.isAdmin());

        loginPO.doLogIn("admin", "admin");
        loginPO.isLoggedIn();
        assertTrue(loginPO.isAdmin());

        AdminPO adminPO = loginPO.toAdminPage();

        int uniqueCards = Integer.parseInt(adminPO.getDriver().findElement(By.id("unique-cards-text")).getText().split(" ")[4]);

        adminPO.createRandomCard();
        int newUniqueCards = Integer.parseInt(adminPO.getDriver().findElement(By.id("unique-cards-text")).getText().split(" ")[4]);
        assertTrue(adminPO.getDriver().getPageSource().contains("Successfully created the card."));
        assertEquals(uniqueCards + 1, newUniqueCards);

    }
}
