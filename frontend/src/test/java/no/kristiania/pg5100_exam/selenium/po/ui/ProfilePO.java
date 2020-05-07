package no.kristiania.pg5100_exam.selenium.po.ui;

import no.kristiania.pg5100_exam.selenium.PageObject;
import no.kristiania.pg5100_exam.selenium.po.LayoutPO;
import org.openqa.selenium.By;

import static org.junit.jupiter.api.Assertions.assertTrue;

public class ProfilePO extends LayoutPO {


    public ProfilePO(PageObject otherPO) {
        super(otherPO);
    }

    @Override
    public boolean isOnPage() {
        return getDriver().getPageSource().contains("Profile information:");
    }

    public boolean hasTransactions() {
        return getDriver().findElements(By.className("transaction-container")).size() > 0;
    }

    public ChangePasswordPO toChangePassword() {
        clickAndWait("toChangePassword-btn");

        ChangePasswordPO po = new ChangePasswordPO(this);
        assertTrue(po.isOnPage());

        return po;
    }

}
