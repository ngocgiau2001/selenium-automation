package pages;

import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.FindBy;
import org.openqa.selenium.support.PageFactory;
import utils.WaitUtils;

public class LoginPage {
    private WebDriver driver;

    @FindBy(id = "email")
    private WebElement emailInput;

    @FindBy(id = "password")
    private WebElement passwordInput;

    @FindBy(css = "button[type='submit']")
    private WebElement loginBtn;
    
    @FindBy(css = ".p-toast-message, .p-error, .text-red-500, [role='alert']")
    private WebElement errorMessage;

    public LoginPage(WebDriver driver) {
        this.driver = driver;
        PageFactory.initElements(driver, this);
        try { Thread.sleep(2000); } catch (Exception e) {}
    }

    public void enterEmail(String email) {
        WebElement el = new org.openqa.selenium.support.ui.WebDriverWait(driver, java.time.Duration.ofSeconds(10))
            .until(d -> {
                for (WebElement e : d.findElements(org.openqa.selenium.By.id("email"))) {
                    if (e.isDisplayed() && e.isEnabled()) return e;
                }
                return null;
            });
        String val = email == null ? "" : email;
        try {
            // Dùng Actions để click và gõ phím trực tiếp
            new org.openqa.selenium.interactions.Actions(driver).moveToElement(el).click().sendKeys(val).perform();
        } catch (Exception e) {
            // Backup
            org.openqa.selenium.JavascriptExecutor js = (org.openqa.selenium.JavascriptExecutor) driver;
            js.executeScript("arguments[0].value=arguments[1]; arguments[0].dispatchEvent(new Event('input', {bubbles: true}));", el, val);
        }
    }

    public void enterPassword(String password) {
        WebElement el = new org.openqa.selenium.support.ui.WebDriverWait(driver, java.time.Duration.ofSeconds(10))
            .until(d -> {
                for (WebElement e : d.findElements(org.openqa.selenium.By.id("password"))) {
                    if (e.isDisplayed() && e.isEnabled()) return e;
                }
                return null;
            });
        String val = password == null ? "" : password;
        try {
            new org.openqa.selenium.interactions.Actions(driver).moveToElement(el).click().sendKeys(val).perform();
        } catch (Exception e) {
            org.openqa.selenium.JavascriptExecutor js = (org.openqa.selenium.JavascriptExecutor) driver;
            js.executeScript("arguments[0].value=arguments[1]; arguments[0].dispatchEvent(new Event('input', {bubbles: true}));", el, val);
        }
    }

    public void clickLogin() {
        WebElement btn = new org.openqa.selenium.support.ui.WebDriverWait(driver, java.time.Duration.ofSeconds(10))
            .until(d -> {
                for (WebElement e : d.findElements(org.openqa.selenium.By.cssSelector("button[type='submit']"))) {
                    if (e.isDisplayed() && e.isEnabled()) return e;
                }
                return null;
            });
        btn.click();
    }

    public DashboardPage login(String email, String password) {
        enterEmail(email);
        enterPassword(password);
        clickLogin();
        return new DashboardPage(driver);
    }
    
    public boolean isErrorMessageDisplayed() {
        try {
            WaitUtils.waitForElementVisible(driver, errorMessage, 5);
            return errorMessage.isDisplayed();
        } catch (Exception e) {
            return false;
        }
    }
}
