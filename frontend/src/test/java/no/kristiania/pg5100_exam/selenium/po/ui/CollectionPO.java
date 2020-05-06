package no.kristiania.pg5100_exam.selenium.po.ui;

import no.kristiania.pg5100_exam.selenium.PageObject;
import no.kristiania.pg5100_exam.selenium.po.LayoutPO;
import no.kristiania.pg5100_exam.selenium.po.LogInPO;
import org.openqa.selenium.By;

import static org.junit.Assert.assertTrue;

public class CollectionPO extends LayoutPO {

    public CollectionPO(PageObject otherPO) {
        super(otherPO);
    }

    @Override
    public boolean isOnPage() {
        return getDriver().getTitle().contains("My collection");
    }

    public CardPackPO toOpenCardPack() {

        clickAndWait("toOpenCardPack-btn");

        CardPackPO po = new CardPackPO(this);
        assertTrue(po.isOnPage());

        return po;

    }

    public boolean hasCards() {
        return driver.findElements(By.className("card-container")).size() > 0;
    }

    public CollectionPO millCopy(int copyId) {

        clickAndWait("mill-btn-" + copyId);

        CollectionPO po = new CollectionPO(this);
        assertTrue(po.isOnPage());

        return po;
    }

    public CollectionPO purchaseCardPack() {

        clickAndWait("purchaseCardPack-btn");

        CollectionPO po = new CollectionPO(this);
        assertTrue(po.isOnPage());

        return po;
    }

}
