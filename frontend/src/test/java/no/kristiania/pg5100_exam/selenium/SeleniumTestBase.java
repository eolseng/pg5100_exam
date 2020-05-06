package no.kristiania.pg5100_exam.selenium;

import no.kristiania.pg5100_exam.backend.entity.Item;
import no.kristiania.pg5100_exam.backend.service.CardPackService;
import no.kristiania.pg5100_exam.backend.service.ItemService;
import no.kristiania.pg5100_exam.backend.service.UserService;
import no.kristiania.pg5100_exam.selenium.po.IndexPO;
import no.kristiania.pg5100_exam.selenium.po.LogInPO;
import no.kristiania.pg5100_exam.selenium.po.SignUpPO;
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

    protected abstract WebDriver getDriver();
    protected abstract String getServerHost();
    protected abstract int getServerPort();

    private static final AtomicInteger counter = new AtomicInteger(0);
    private String getUniqueId() {
        return "test_unique_id_selenium_" + counter.getAndIncrement();
    }

    @Autowired
    UserService userService;

    @Autowired
    ItemService itemService;

    private IndexPO home;

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

        LogInPO logInPO =  home.toLogIn();
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
        items.forEach(item -> {
            assertTrue(pageSource.contains(item.getName()));
        });

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
        assertEquals(CardPackService.CARD_PACK_SIZE , cardPackPO.getNewCardCount());
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
                assertEquals(amount -1, newAmount);
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
}
