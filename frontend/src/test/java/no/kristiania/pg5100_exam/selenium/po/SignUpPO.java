package no.kristiania.pg5100_exam.selenium.po;

import no.kristiania.pg5100_exam.selenium.PageObject;

public class SignUpPO extends LayoutPO {

    public SignUpPO(PageObject otherPO) {
        super(otherPO);
    }

    @Override
    public boolean isOnPage() {
        return getDriver().getTitle().contains("Sign Up");
    }

    public IndexPO createUser(String username, String password) {

        setText("username", username);
        setText("password", password);
        clickAndWait("submit-btn");

        IndexPO po = new IndexPO(this);
        if(po.isOnPage()) {
            return po;
        } else {
            return null;
        }
    }
}
