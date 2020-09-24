package uiTest.pages;

import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.FindBy;
import uiTest.utils.PageObject;

public class LoginPage extends PageObject {

    @FindBy(id = "defaultRegisterFormEmail")
    private WebElement login;

    @FindBy(xpath = "/html/body/app-root/app-login/div/div[2]/div/form/div[2]/div[1]/div/input")
    private WebElement password;

    @FindBy(xpath = "/html/body/app-root/app-login/div/div[2]/div/form/div[3]/div[1]/button")
    private WebElement loginButton;

    public LoginPage(WebDriver driver) {
        super(driver);
    }

    public void fillLoginForm(String userEmail, String password) {
        sendTextToWebElement(this.login, userEmail);
        sendTextToWebElement(this.password, password);
        }

    public HomePage clickLoginButton() {
        clickOnWebElement(loginButton);
        return new HomePage(driver);
    }

    public boolean isLoginBtnDisabled() {
        return loginButton.getAttribute("disabled") != null;
    }

    public String emailGetErrorText() {
        WebElement emailErrorMessage = driver.findElement(By.xpath("/html/body/app-root/app-login/div/div[2]/div/form/div[3]/div[2]/div"));
        return waitForElementToHaveTextAndReturnText(driver, 2, emailErrorMessage);
    }


    //TODO
    public MainPage login(String user, String password) {
        return null;
    }
}
