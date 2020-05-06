package no.kristiania.pg5100_exam.selenium.po.ui;

import no.kristiania.pg5100_exam.selenium.PageObject;
import no.kristiania.pg5100_exam.selenium.po.LayoutPO;

public class ChangePasswordPO extends LayoutPO {

    public ChangePasswordPO(PageObject otherPO) {
        super(otherPO);
    }

    @Override
    public boolean isOnPage() {
        return getDriver().getTitle().contains("Change password");
    }

    public ProfilePO doChangePassword(String oldPassword, String newPassword) {
        setText("old-password", oldPassword);
        setText("new-password", newPassword);
        setText("confirm-password", newPassword);
        clickAndWait("submit-btn");

        ProfilePO po = new ProfilePO(this);
        if (po.isOnPage()) {
            return po;
        } else {
            return null;
        }
    }
}
