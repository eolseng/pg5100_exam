package no.kristiania.pg5100_exam.selenium.po;

import no.kristiania.pg5100_exam.selenium.PageObject;
import no.kristiania.pg5100_exam.selenium.po.ui.CollectionPO;
import no.kristiania.pg5100_exam.selenium.po.ui.ProfilePO;
import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;

import static org.junit.Assert.assertTrue;

public abstract class LayoutPO extends PageObject {

    public LayoutPO(WebDriver driver, String host, int port) {
        super(driver, host, port);
    }

    public LayoutPO(PageObject otherPO) {
        super(otherPO);
    }

    public SignUpPO toSignUp() {

        clickAndWait("toSignUp-btn");

        SignUpPO po = new SignUpPO(this);
        assertTrue(po.isOnPage());

        return po;
    }

    public LogInPO toLogIn() {

        clickAndWait("toLogIn-btn");

        LogInPO po = new LogInPO(this);
        assertTrue(po.isOnPage());

        return po;
    }

    public CollectionPO toCollection() {

        clickAndWait("toCollection-btn");

        CollectionPO po = new CollectionPO(this);
        assertTrue(po.isOnPage());

        return po;

    }

    public ProfilePO toProfile() {

        clickAndWait("toProfile-btn");

        ProfilePO po = new ProfilePO(this);
        assertTrue(po.isOnPage());

        return po;
    }

    public IndexPO doLogout() {
        clickAndWait("logout-btn");

        IndexPO po = new IndexPO(this);
        assertTrue(po.isOnPage());

        return po;
    }

    public boolean isLoggedIn() {
        return getDriver().findElements(By.id("logout-btn")).size() > 0 &&
                getDriver().findElements(By.id("toSignUp-btn")).isEmpty();
    }

    public int getCardPackCount() {
        return Integer.parseInt(getText("cardpacks-text").split(" ")[2]);
    }

    public int getTotalCardCount() {
        return Integer.parseInt(getText("total-cards-text").split(" ")[2]);
    }

    public int getBalance() {
        return Integer.parseInt(getText("balance-text").split("\\$")[1]);
    }

}
