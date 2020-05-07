package no.kristiania.pg5100_exam.selenium.po;

import no.kristiania.pg5100_exam.selenium.PageObject;

public class LogInPO extends LayoutPO {

    public LogInPO(PageObject otherPO) {
        super(otherPO);
    }

    @Override
    public boolean isOnPage() {
        return getDriver().getTitle().contains("Log In");
    }

    public IndexPO doLogIn(String username, String password) {

        setText("username", username);
        setText("password", password);
        clickAndWait("submit-btn");

        IndexPO po = new IndexPO(this);
        if (po.isOnPage()) {
            return po;
        } else {
            return null;
        }
    }
}
