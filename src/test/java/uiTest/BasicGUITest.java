package uiTest;

import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;
import org.openqa.selenium.NoSuchElementException;
import uiTest.pages.*;
import uiTest.utils.FunctionalTest;



import static org.junit.Assert.assertEquals;

public class BasicGUITest extends FunctionalTest {

    private final LoginPage loginPage = new LoginPage(driver);
    private MainPage mainPage;

    private final String userName = "paxpistor@gmail.com";
    private final String userRandomName = System.currentTimeMillis() + "@mail.com";
    private final String password = "12345678";
    private final String baseURL = "http://localhost:4200/";
    private final String signUpUrl = "http://localhost:4200/user/registration";
    private final String loginURL = "http://localhost:4200/user/login";

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

    @Test
    public void testLogIn() throws InterruptedException {
        driver.get(loginURL);

        LoginPage loginPage = new LoginPage(driver);
        loginPage.fillLoginForm(userName, password);

        assertEquals(false, loginPage.isLoginBtnDisabled());

        HomePage homePage = loginPage.clickLoginButton();
        Thread.sleep(3000);

        assertEquals("http://localhost:4200/contacts", driver.getCurrentUrl());
    }

    @Test
    public void testLogInEmailNotEquals(){
        driver.get(loginURL);

        LoginPage loginPage = new LoginPage(driver);
        loginPage.fillLoginForm(userRandomName, password);

        assertEquals(false, loginPage.isLoginBtnDisabled());
        HomePage homePage = loginPage.clickLoginButton();
        assertEquals("Please check your activation or Login + Password combination",loginPage.emailGetErrorText());
       }

    @Test
    public void testLogInPasswordNotEquals(){
        driver.get(loginURL);

        LoginPage loginPage = new LoginPage(driver);
        loginPage.fillLoginForm(userName, "asdfghjk");

        assertEquals(false, loginPage.isLoginBtnDisabled());
        HomePage homePage = loginPage.clickLoginButton();
        assertEquals("Please check your activation or Login + Password combination",loginPage.emailGetErrorText());
    }

    @Test
    public void testChangePassword() {
        driver.get(loginURL);

        LoginPage loginPage = new LoginPage(driver);
        loginPage.fillLoginForm(userName, password);

        assertEquals(false, loginPage.isLoginBtnDisabled());
        HomePage homePage = loginPage.clickLoginButton();

        ChangePassword changePassword = new ChangePassword(driver);

        changePassword.fillPasswordField("qwertyui");
        changePassword.fillConfirmPasswordField("asdfghjkl");

        assertEquals("Passwords do not match.",changePassword.passwordConfirmErrorMessageText());
    }
}
