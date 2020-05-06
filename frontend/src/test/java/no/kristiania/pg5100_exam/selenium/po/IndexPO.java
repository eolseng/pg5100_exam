package no.kristiania.pg5100_exam.selenium.po;

import no.kristiania.pg5100_exam.selenium.PageObject;
import org.openqa.selenium.WebDriver;

public class IndexPO extends LayoutPO {

    public IndexPO(PageObject otherPO) {
        super(otherPO);
    }

    public IndexPO(WebDriver driver, String host, int port) {
        super(driver, host, port);
    }

    @Override
    public boolean isOnPage() {
        return getDriver().getPageSource().contains("Welcome to Schmidts Stinging Friends");
    }

    public void toStartingPage(){
        getDriver().get(host + ":" + port);
    }

}
