package uiTest.utils;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.openqa.selenium.*;
import org.openqa.selenium.support.PageFactory;
import org.openqa.selenium.support.ui.ExpectedCondition;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.WebDriverWait;

import java.util.ArrayList;
import java.util.List;

import static org.apache.commons.lang3.StringUtils.substringAfter;

public class PageObject {
    protected WebDriver driver;
    private static final String ERROR_START = "\n\nElement not found in: ";
    private static final String ERROR_MIDDLE = " within: ";
    private static final String ERROR_END = " seconds!\n\n";
    private final Logger logger = LogManager.getLogger(getClass());

    public PageObject(WebDriver driver) {
        this.driver = driver;
        PageFactory.initElements(driver, this);
    }

    public static int WAIT_TIMEOUT() {
        return 5;
    }

    public void sendTextToWebElement(WebElement element, String text) {
        try {
            element.clear();
            element.sendKeys(text);

            logger.info("LOGGING: Send the text: *** " + text + " *** to element: " + cutXpathString(element));
        } catch (Exception e) {
            logger.info("LOGGING: Waiting for " + cutXpathString(element) + " to be visible!");
            waitForElementToBeVisible(driver, WAIT_TIMEOUT(), element);
            element.clear();
            element.sendKeys(text);
        }
    }

    public void selectElement(WebElement element) {
        try {
            element.isSelected();
            element.click();
            logger.info("LOGGING: Selected element: " + cutXpathString(element));
        } catch (Exception e) {
            logger.info("LOGGING: Waiting for " + cutXpathString(element) + " to be visible!");
            waitForElementToBeVisible(driver, WAIT_TIMEOUT(), element);
            element.isSelected();
            element.click();
        }
    }

    public void clickOnWebElement(WebElement element) {
        try {
            element.click();
            logger.info("LOGGING: clicked on element: " + cutXpathString(element));
        } catch (Exception e) {
            logger.info("LOGGING: Waiting for " + cutXpathString(element) + " to be visible!");
            waitForElementToBeVisible(driver, WAIT_TIMEOUT(), element);
            element.click();
        }
    }

    public void submitForm(WebElement element) {
        try {
            element.submit();
            logger.info("LOGGING: submit form: " + cutXpathString(element));
        } catch (Exception e) {
            logger.info("LOGGING: Waiting for " + cutXpathString(element) + " to be visible!");
            waitForElementToBeVisible(driver, WAIT_TIMEOUT(), element);
            element.submit();
        }
    }

    public void clickBack() {
        driver.navigate().back();
        logger.info("LOGGING: clicked the browser back button");
    }

    /**
     * cuts the WebElement designation into only the xpath
     *
     * @param element gets the xpath of the used WebElement
     * @return only the xpath, not the whole WebElement designation
     */
    private static String cutXpathString(WebElement element) {
        return substringAfter(element.toString(), "->");
    }

    protected void scrollViewToElement(WebElement element) {
        try {
            ((JavascriptExecutor) driver).executeScript("window.scrollTo(0," + (element.getLocation().y - 100) + ")");
        } catch (Exception e) {
            waitForElementToBeVisible(driver, WAIT_TIMEOUT(), element);
            logger.info("LOGGING: Waiting for " + cutXpathString(element) + " to be visible!");
            ((JavascriptExecutor) driver).executeScript("window.scrollTo(0," + (element.getLocation().y - 100) + ")");
        }
    }

    /**
     * sometimes the normal clickOnElement does not work, but with clicking via the javascript click it does
     * this is just a backup for this case
     */
    public void clickElementJS(WebElement element) {
        ((JavascriptExecutor) driver).executeScript("arguments[0].click();", element);
    }

    public boolean isElementDisplayed(WebElement element) {
        boolean isDisplayed;
        try {
            logger.info("LOGGING: checking if element is displayed: " + cutXpathString(element));
            waitForElementToBeVisible(driver, WAIT_TIMEOUT(), element);
            isDisplayed = element.isDisplayed();
        } catch (Exception e) {
            logger.info("LOGGING: Caught exception, checking if element is displayed: " + cutXpathString(element));
            waitForElementToBeVisible(driver, WAIT_TIMEOUT(), element);
            isDisplayed = element.isDisplayed();
        }
        return isDisplayed;
    }

    public boolean isLabelNotEmpty(WebElement element) {
        return !element.getText().isEmpty();
    }

    /**
     * conditional explicit wait for Element to be displayed
     *
     * @param seconds MAXIMUM amount of time to wait for condition to be true
     * @param element the element to wait for, it needs to be .isDisplayed() == true
     */
    public static void waitForElementToBeVisible(WebDriver driver, int seconds, WebElement element) {
        WebDriverWait wait = (WebDriverWait) new WebDriverWait(driver, seconds)
                .ignoring(StaleElementReferenceException.class);
        wait.withMessage(ERROR_START + element + ERROR_MIDDLE + seconds + ERROR_END);
        wait.until((ExpectedCondition<Boolean>) webDriver -> element != null && element.isDisplayed());
    }

    /**
     * conditional explicit wait for Element not to be displayed
     * this one is also bound in a try / catch as it can cause a NoSuchElementException
     *
     * @param seconds MAXIMUM amount of time to wait for condition to be true
     * @param element the element to wait for, it needs to be .isDisplayed() == false
     */
    public static void waitForElementToBeinvisible(WebDriver driver, int seconds, WebElement element) {
        try {
            WebDriverWait wait = (WebDriverWait) new WebDriverWait(driver, seconds / 2)
                    .ignoring(StaleElementReferenceException.class);
            wait.withMessage(ERROR_START + element + ERROR_MIDDLE + seconds + ERROR_END);
            wait.until((ExpectedCondition<Boolean>) webDriver -> element != null && !element.isDisplayed());
        } catch (Exception e) {
            //do nothing
        }
    }

    /**
     * conditional explicit wait for a List of Elements to have size 0
     *
     * @param seconds  MAXIMUM amount of time to wait for condition to be true
     * @param elements the list of elements to wait for, it needs to have .size() == 0
     */
    public static void waitForElementListToBeEmtpy(WebDriver driver, int seconds, List<WebElement> elements) {
        WebDriverWait wait = (WebDriverWait) new WebDriverWait(driver, seconds)
                .ignoring(StaleElementReferenceException.class);
        wait.withMessage("\nError: List of elements was not empty in " + seconds + ERROR_END);
        wait.until((ExpectedCondition<Boolean>) webDriver -> elements != null && elements.size() == 0);
    }

    /**
     * conditional explicit wait for a List of Elements to have expected size
     *
     * @param seconds  MAXIMUM amount of time to wait for condition to be true
     * @param elements the list of elements to wait for, it needs to have .size() == size
     * @param size     the expected size to compare against
     */
    public static void waitForElementListHasSize(WebDriver driver, int seconds, List<WebElement> elements, int size) {
        WebDriverWait wait = (WebDriverWait) new WebDriverWait(driver, seconds)
                .ignoring(StaleElementReferenceException.class);
        wait.withMessage("\nError: List of elements did not have size of " + size + " in " + seconds + ERROR_END);
        wait.until((ExpectedCondition<Boolean>) webDriver -> elements != null && elements.size() == size);
    }

    /**
     * conditional explicit wait for a List of Elements to have expected size
     *
     * @param seconds  MAXIMUM amount of time to wait for condition to be true
     * @param elements the list of elements to wait for, it needs to have .size() > size
     * @param size     the expected size to compare against
     */
    public static void waitForElementListToBeHigherThanValue(WebDriver driver, int seconds, List<WebElement> elements, int size) {
        WebDriverWait wait = (WebDriverWait) new WebDriverWait(driver, seconds)
                .ignoring(StaleElementReferenceException.class);
        wait.withMessage("\nError: List of elements did not have list size higher than " + size + " in " + seconds + ERROR_END);
        wait.until((ExpectedCondition<Boolean>) webDriver -> elements != null && elements.size() > size);
    }

    /**
     * conditional explicit wait to check if an Elements text contains String
     *
     * @param seconds MAXIMUM amount of time to wait for condition to be true
     * @param element the WebElement that has a label to check
     * @param text    the String that needs to be contained in the WebElement
     */
    public static void waitForElementToHaveText(WebDriver driver, int seconds, WebElement element, String text) {
        WebDriverWait wait = (WebDriverWait) new WebDriverWait(driver, seconds)
                .ignoring(StaleElementReferenceException.class);
        wait.withMessage("\nError: Webelement " + element + "\ndid not have text of " + text + " in " + seconds + ERROR_END);
        wait.until((ExpectedCondition<Boolean>) webDriver -> element != null && element.getText().contains(text));
    }

    public static String waitForElementToHaveTextAndReturnText(WebDriver driver, int seconds, WebElement element) {
        WebDriverWait wait = (WebDriverWait) new WebDriverWait(driver, seconds)
                .ignoring(StaleElementReferenceException.class);
        wait.withMessage("\nError: Webelement " + element + "\n" + seconds + ERROR_END);
        wait.until((ExpectedCondition<Boolean>) webDriver -> element != null);
        return element.getText();
    }

    /**
     * conditional explicit wait that gets the "value" attribute from a WebElement and compares it to expected text
     *
     * @param seconds MAXIMUM amount of time to wait for condition to be true
     * @param element the WebElement to get the the attribute "value" from and compare it
     * @param text    the text to compare the WebElement value against
     */
    public static void waitForInputFieldToHaveText(WebDriver driver, int seconds, WebElement element, String text) {
        WebDriverWait wait = (WebDriverWait) new WebDriverWait(driver, seconds)
                .ignoring(StaleElementReferenceException.class);
        wait.withMessage("\nError: Inputfield " + element + "\ndid not have text of " + text + " in " + seconds + ERROR_END);
        wait.until((ExpectedCondition<Boolean>) webDriver -> element.getAttribute("value").contains(text));

    }

    /**
     * conditional explicit wait to check the current URL to contain String
     *
     * @param seconds MAXIMUM amount of time to wait for condition to be true
     * @param title   the String that the URL should contain
     */
    public static void waitForUrlToContainText(WebDriver driver, int seconds, String title) {
        WebDriverWait wait = new WebDriverWait(driver, seconds);
        wait.withMessage("\nError: Page title does not contain: " + title + " after: " + seconds + "\n" +
                "on page: " + driver.getCurrentUrl() + " \nactual page title is: " + driver.getCurrentUrl() + "\n--------");
        wait.until((ExpectedCondition<Boolean>) webDriver -> driver.getCurrentUrl().contains(title));
    }

    /**
     * method that switches focus to either another tab or another window, both are possible and handled
     *
     * @param tabNumber given the number of the new tab, e.g. if more than one new tab is opened,
     *                  also there is always 1 tab open already
     *                  The sleep is necessary, as it seems that Browsers have weird issues with opening new tabs
     *                  and directly switching to them, the new tab usually gets stuck in somekind of infinite loading loop
     *                  which causes Selenium to ignore all timeouts etc and just keeps on going,
     *                  causing the tests to take forever
     */
    public static void switchToTab(WebDriver driver, int tabNumber, int sleepTimer) {
        ArrayList<String> tabs2 = new ArrayList<>(driver.getWindowHandles());
        sleep(sleepTimer);
        driver.switchTo().window(tabs2.get(tabNumber));
    }

    public boolean currentUrlContains(String text) {
        return driver.getCurrentUrl().contains(text);
    }

    public boolean currentUrlEquals(String text) {
        return driver.getCurrentUrl().equals(text);
    }

    public static void waitForPageToLoad(WebDriver driver) {
        String pageLoadStatus;
        JavascriptExecutor js;
        do {
            js = (JavascriptExecutor) driver;
            pageLoadStatus = (String) js.executeScript("return document.readyState");
        } while (!pageLoadStatus.equals("complete"));
    }

    /**
     * constant Thread.sleep method, that will ALWAYS wait for millis TIME
     *
     * @param millis the amount of milliseconds it will wait
     */
    public static void sleep(int millis) {
        try {
            Thread.sleep(millis);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
    }

    /**
     * method that waits for any Alert from the browser and accepts it
     * CAUTION: this does NOT work for SafariDriver - tests with Alerts CANNOT be executed on Safari
     */
    protected static void acceptAlertIfPresent(WebDriver driver) {
        try {
            WebDriverWait wait = new WebDriverWait(driver, WAIT_TIMEOUT());
            wait.until(ExpectedConditions.alertIsPresent());
            Alert alert = driver.switchTo().alert();
            alert.accept();
        } catch (Exception e) {
            //Do nothing if alert is not displayed within 10 sec
        }
    }

}
