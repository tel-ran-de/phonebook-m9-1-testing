package uiTest;

import org.junit.Before;
import org.junit.Test;
import org.openqa.selenium.NoSuchElementException;
import uiTest.pages.LoginPage;
import uiTest.pages.MainPage;
import uiTest.pages.RegistrationConfirmationPage;
import uiTest.pages.RegistrationPage;
import uiTest.utils.FunctionalTest;



import static org.junit.Assert.assertEquals;

public class BasicGUITest extends FunctionalTest {

    private final LoginPage loginPage = new LoginPage(driver);
    private MainPage mainPage;

    private final String userName = "qatest.taran02@gmail.com";
    private final String userRandomName = System.currentTimeMillis() + "@mail.com";
    private final String password = "01234567890";
    private final String baseURL = "http://localhost:4200/";
    private final String signUpUrl = "http://localhost:4200/user/registration";

    @Before
    public void init() {
        driver.get(baseURL);
        mainPage = loginPage.login(userName, password);
    }

    @Test
    public void testCreateUser() {
        driver.get(signUpUrl);

        RegistrationPage registrationPage = new RegistrationPage(driver);
        registrationPage.fillRegisterForm(userRandomName, password, password);

        assertEquals(false, registrationPage.isBtnSubmitDisabled());

        RegistrationConfirmationPage confirmationPage = registrationPage.clickSubmit();

        assertEquals("Please, check your email and activate your account.", confirmationPage.getConfirmationPageText());
        assertEquals("http://localhost:4200/user/activate-email", driver.getCurrentUrl());
    }

    @Test
    public void testPasswordTooShort() {
        driver.get(signUpUrl);

        RegistrationPage registrationPage = new RegistrationPage(driver);

        registrationPage.fillPasswordField("01234");

        assertEquals(true, registrationPage.isBtnSubmitDisabled());

        assertEquals("Password must be at least 8 characters.", registrationPage.passwordGetErrorText());
    }

    @Test
    public void testPasswordTooLong() {
        driver.get(signUpUrl);

        RegistrationPage registrationPage = new RegistrationPage(driver);

        registrationPage.fillPasswordField("0123456789_0123456789");

        assertEquals(true, registrationPage.isBtnSubmitDisabled());

        assertEquals("Password must be at least 8 characters.", registrationPage.passwordGetErrorText());
    }

    @Test
    public void testPasswordAndConfirmPasswortNotEquals() {
        driver.get(signUpUrl);

        RegistrationPage registrationPage = new RegistrationPage(driver);

        registrationPage.fillPasswordField("0123456789");
        registrationPage.fillConfirmPasswordField("01234567890");

        assertEquals(true, registrationPage.isBtnSubmitDisabled());
        assertEquals("Password do not match.", registrationPage.passwordConfirmErrorGetText());
    }

    @Test(expected = NoSuchElementException.class)
    public void testEmailToUpperCase() {
        driver.get(signUpUrl);

        RegistrationPage registrationPage = new RegistrationPage(driver);

        registrationPage.fillRegisterForm("email.TO_UPPER_CASE@gamil.com", password, password);

        assertEquals(false, registrationPage.isBtnSubmitDisabled());
        registrationPage.emailConfirmErrorGetText();
    }

    @Test
    public void testEmailNoDomain1Lvl() {
        driver.get(signUpUrl);

        RegistrationPage registrationPage = new RegistrationPage(driver);

        registrationPage.fillRegisterForm("email.TO_UPPER_CASE@gamil.", password, password);

        assertEquals(false, registrationPage.isBtnSubmitDisabled());
        registrationPage.emailConfirmErrorGetText();
    }
}
