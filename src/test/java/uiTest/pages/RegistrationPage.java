package uiTest.pages;

import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.FindBy;
import uiTest.utils.PageObject;

public class RegistrationPage extends PageObject {

    @FindBy(id = "defaultRegisterFormEmail")
    private WebElement userEmail;

    @FindBy(xpath = "/html/body/app-root/app-registration/form/div[2]/input")
    private WebElement password;

    @FindBy(xpath = "/html/body/app-root/app-registration/form/div[3]/input")
    private WebElement passwordConfirm;

    @FindBy(xpath = "/html/body/app-root/app-registration/form/button")
    private WebElement submitBtn;

    public RegistrationPage(WebDriver driver) {
        super(driver);
    }

    public void fillRegisterForm(String userEmail, String password, String passwordConfirm) {
        sendTextToWebElement(this.userEmail, userEmail);
        sendTextToWebElement(this.password, password);
        sendTextToWebElement(this.passwordConfirm, passwordConfirm);
    }


    public RegistrationConfirmationPage clickSubmit() {
        clickOnWebElement(submitBtn);
        return new RegistrationConfirmationPage(driver);
    }

    public String passwordGetErrorText() {
        WebElement passwordErrorMessage = driver.findElement(By.xpath("/html/body/app-root/app-registration/form/div[3]/div"));
        return waitForElementToHaveTextAndReturnText(driver, 2, passwordErrorMessage);
    }

    public String passwordConfirmErrorGetText() {
        WebElement passwordConfirmErrorMessage = driver.findElement(By.xpath("/html/body/app-root/app-registration/form/div[5]/div"));
        return waitForElementToHaveTextAndReturnText(driver, 2, passwordConfirmErrorMessage);
    }

    public String emailConfirmErrorGetText() {
        WebElement emailErrorMessage = driver.findElement(By.xpath("/html/body/app-root/app-registration/form/div[2]/div"));
        return waitForElementToHaveTextAndReturnText(driver, 2, emailErrorMessage);
    }

    public boolean isBtnSubmitDisabled() {
        return submitBtn.getAttribute("disabled") != null;
    }

    public void fillPasswordField(String password) {
        sendTextToWebElement(this.password, password);
    }

    public void fillConfirmPasswordField(String confirmPassword) {
        sendTextToWebElement(this.passwordConfirm, confirmPassword);
    }

    public void fillEmailField(String userEmail) {
        sendTextToWebElement(this.userEmail, userEmail);
    }
}
