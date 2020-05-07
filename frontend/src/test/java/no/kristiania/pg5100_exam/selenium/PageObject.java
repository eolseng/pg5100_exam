package no.kristiania.pg5100_exam.selenium;

import org.openqa.selenium.*;
import org.openqa.selenium.support.ui.ExpectedCondition;
import org.openqa.selenium.support.ui.WebDriverWait;

import java.util.concurrent.atomic.AtomicLong;

import static org.junit.Assert.assertEquals;

public abstract class PageObject {

    protected final WebDriver driver;
    protected final String host;
    protected final int port;

    private static final AtomicLong counter = new AtomicLong(System.currentTimeMillis());

    public static String getUniqueId() {
        return "unique_id_" + counter.incrementAndGet();
    }

    public PageObject(WebDriver driver, String host, int port) {
        this.driver = driver;
        this.host = host;
        this.port = port;
    }

    public PageObject(PageObject otherPO) {
        this(otherPO.getDriver(), otherPO.getHost(), otherPO.getPort());
    }

    public abstract boolean isOnPage();

    public void refresh() {
        getDriver().navigate().refresh();
    }

    public void clickAndWait(String id) {
        WebElement element = getDriver().findElement(By.id(id));
        element.click();
        try {Thread.sleep(200);} catch (Exception ex){/**/}
        waitForPageToLoad();
        try {Thread.sleep(300);} catch (Exception ex){/**/}
    }

    private Boolean waitForPageToLoad() {
        JavascriptExecutor jsExecutor = (JavascriptExecutor) getDriver();
        WebDriverWait wait = new WebDriverWait(getDriver(), 10);

        return wait.until((ExpectedCondition<Boolean>) input -> {
            String result = jsExecutor.executeScript("return /loaded|complete/.test(document.readyState);").toString();
            return Boolean.parseBoolean(result);
        });
    }

    public String getText(String id) {
        return getDriver().findElement(By.id(id)).getText();
    }

    public int getInteger(String id) {
        String text = getText(id);
        return Integer.parseInt(text);
    }

    public void setText(String id, String text) {
        WebElement element = getDriver().findElement(By.id(id));

        element.clear();
        element.click();
        element.sendKeys(text);

        assertEquals(text, element.getAttribute("value"));
    }

    public WebDriver getDriver() {
        return driver;
    }

    public String getHost() {
        return host;
    }

    public int getPort() {
        return port;
    }
}
