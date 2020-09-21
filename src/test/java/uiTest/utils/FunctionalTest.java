package uiTest.utils;

import org.apache.commons.io.FileUtils;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.junit.AfterClass;
import org.junit.BeforeClass;
import org.junit.Rule;
import org.junit.rules.TestRule;
import org.junit.rules.TestWatcher;
import org.junit.runner.Description;
import org.openqa.selenium.OutputType;
import org.openqa.selenium.TakesScreenshot;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.chrome.ChromeDriver;

import java.io.File;
import java.io.IOException;
import java.time.LocalDateTime;
import java.util.concurrent.TimeUnit;


public class FunctionalTest {
    private static final String PATH_TO_DRIVER = "C:\\opt\\chromedriver.exe";

    protected static WebDriver driver;
    protected final Logger logger = LogManager.getLogger(getClass());

    @Rule
    public final TestRule watchman = new TestWatcher() {
        // This method gets invoked if the test fails for any reason:
        @Override
        protected void failed(Throwable e, Description description) {
            // Print out the error message:
            logger.info(description.getDisplayName() + " " + e.getClass().getSimpleName() + "\n");
            // Now you can do whatever you need to do with it, for example take a screenshot
            takeScreenShot(description.getDisplayName());
        }
    };

    @BeforeClass
    public static void setUp() {
        System.setProperty("webdriver.chrome.driver", PATH_TO_DRIVER);
        driver = new ChromeDriver();
        driver.manage().window().maximize();
        driver.manage().timeouts().implicitlyWait(5, TimeUnit.SECONDS);
        driver.manage().deleteAllCookies();
    }

    @AfterClass
    public static void tearDown() {
        driver.quit();
    }

    private void takeScreenShot(String methodName) {
        File scrFile = ((TakesScreenshot) driver).getScreenshotAs(OutputType.FILE);
        try {
            FileUtils.copyFile(scrFile, new File("src/screenshots/" + LocalDateTime.now().toString().substring(0, 19).replace(":", "-") + "_" + methodName + ".png"));
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
