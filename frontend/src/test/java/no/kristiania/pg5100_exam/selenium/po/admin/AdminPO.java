package no.kristiania.pg5100_exam.selenium.po.admin;

import no.kristiania.pg5100_exam.selenium.PageObject;
import no.kristiania.pg5100_exam.selenium.po.LayoutPO;

public class AdminPO extends LayoutPO {

    public AdminPO(PageObject otherPO) {
        super(otherPO);
    }

    @Override
    public boolean isOnPage() {
        return getDriver().getTitle().contains("ADMIN");
    }

    public AdminPO createRandomCard() {

        setText("name", "Very Stingy");
        setText("latin-name", "Enrique Igle'sias");
        setText("description", "This stings a whole lot");
        clickAndWait("submit-btn");

        AdminPO po = new AdminPO(this);
        if (po.isOnPage()) {
            return po;
        } else {
            return null;
        }
    }
}
