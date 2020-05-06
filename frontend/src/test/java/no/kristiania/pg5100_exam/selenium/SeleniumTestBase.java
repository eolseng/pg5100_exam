package no.kristiania.pg5100_exam.selenium;

import no.kristiania.pg5100_exam.selenium.po.IndexPO;
import no.kristiania.pg5100_exam.selenium.po.LogInPO;
import no.kristiania.pg5100_exam.selenium.po.SignUpPO;
import no.kristiania.pg5100_exam.selenium.po.ui.ChangePasswordPO;
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
//        assertFalse(home.isLoggedIn(), "Failed to start not logged in");
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
}
