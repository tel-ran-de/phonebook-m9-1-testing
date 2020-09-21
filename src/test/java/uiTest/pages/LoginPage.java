package uiTest.pages;

import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.FindBy;
import uiTest.utils.PageObject;

public class LoginPage extends PageObject {

    @FindBy(id = "")
    private WebElement login;

    @FindBy(id = "")
    private WebElement password;

    @FindBy(xpath = "/html/body/app-root/app-registration/form/a")
    private WebElement loginButton;

    public LoginPage(WebDriver driver) {
        super(driver);
    }

    //TODO
    public MainPage login(String user, String password) {
        return null;
    }
}
