package uiTest.utils;

import org.apache.commons.io.FileUtils;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.junit.AfterClass;
import org.junit.Before;
import org.junit.BeforeClass;
import org.junit.Rule;
import org.junit.rules.TestRule;
import org.junit.rules.TestWatcher;
import org.junit.runner.Description;
import org.openqa.selenium.OutputType;
import org.openqa.selenium.TakesScreenshot;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.chrome.ChromeDriver;
import uiTest.pages.HomePage;
import uiTest.pages.LoginPage;
import uiTest.pages.MainPage;

import java.io.File;
import java.io.IOException;
import java.time.LocalDateTime;
import java.util.concurrent.TimeUnit;

import static org.junit.Assert.assertEquals;
import static uiTest.BasicGUITest.loginPage;
import static uiTest.ConstansName.password;
import static uiTest.ConstansName.userName;
import static uiTest.ConstansURL.baseURL;


public class FunctionalTest {
    private static final String PATH_TO_DRIVER = "C:\\Users\\paxpi\\Downloads\\chromedriver.exe";

    protected static WebDriver driver;
    protected final Logger logger = LogManager.getLogger(getClass());

    private final LoginPage loginPage = new LoginPage(driver);
    public static MainPage mainPage;

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

    @Before
    public void init() {
        driver.get(baseURL);
        mainPage = loginPage.login(userName, password);
    }

//    @AfterClass
//    public static void tearDown() {
//        driver.quit();
//    }

    public void logIn(){
        LoginPage loginPage = new LoginPage(driver);
        loginPage.fillLoginForm(userName, password);

        assertEquals(false, loginPage.isLoginBtnDisabled());
        HomePage homePage = loginPage.clickLoginButton();
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
