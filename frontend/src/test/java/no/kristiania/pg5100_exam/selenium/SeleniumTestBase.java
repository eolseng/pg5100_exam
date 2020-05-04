package no.kristiania.pg5100_exam.selenium;

import no.kristiania.pg5100_exam.selenium.po.IndexPO;
import no.kristiania.pg5100_exam.selenium.po.LogInPO;
import no.kristiania.pg5100_exam.selenium.po.SignUpPO;
import no.kristiania.pg5100_exam.selenium.po.ui.ProfilePO;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.openqa.selenium.WebDriver;

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
    }

    @Test
    public void testCreateUser() {

        assertFalse(home.isLoggedIn());

        String username = getUniqueId();
        String password = "12345";

        home = createNewUser(username, password);
        assertTrue(home.isLoggedIn());
        assertTrue(home.getDriver().getPageSource().contains(username));

    }

    @Test
    public void testFailCreateUser() {
        assertFalse(home.isLoggedIn());

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

        assertFalse(home.isLoggedIn());

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

        assertFalse(home.isLoggedIn());

        String username = getUniqueId();
        String password = "12345";

        home = createNewUser(username, password);
        assertTrue(home.isLoggedIn());
        assertTrue(home.getDriver().getPageSource().contains(username));

        ProfilePO po = home.toProfile();
        assertTrue(po.isOnPage());
        assertFalse(po.hasTransactions());

    }
}
