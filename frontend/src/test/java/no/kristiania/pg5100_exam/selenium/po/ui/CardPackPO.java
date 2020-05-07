package no.kristiania.pg5100_exam.selenium.po.ui;

import no.kristiania.pg5100_exam.selenium.PageObject;
import no.kristiania.pg5100_exam.selenium.po.LayoutPO;
import org.openqa.selenium.By;

import static org.junit.jupiter.api.Assertions.assertTrue;

public class CardPackPO extends LayoutPO {

    public CardPackPO(PageObject otherPO) {
        super(otherPO);
    }

    @Override
    public boolean isOnPage() {
        return getDriver().getTitle().contains("Open Card Pack");
    }

    public int getNewCardCount() {
        return getDriver().findElements(By.className("card-container")).size();
    }

    public CardPackPO openAnotherCardPack() {

        clickAndWait("toOpenCardPack-btn");

        CardPackPO po = new CardPackPO(this);
        assertTrue(po.isOnPage());

        return po;
    }

}
