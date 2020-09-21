package uiTest.pages;

import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.FindBy;
import uiTest.utils.PageObject;

public class RegistrationConfirmationPage extends PageObject {
    public RegistrationConfirmationPage(WebDriver driver) {
        super(driver);
    }

    @FindBy(xpath = "/html/body/app-root/app-activate-email/main/section/div/h4")
    private WebElement confirmationText;

    public String getConfirmationPageText() {
        return confirmationText.getText();
    }
}
