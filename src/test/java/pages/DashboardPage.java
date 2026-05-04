package pages;

import org.openqa.selenium.WebDriver;
import org.openqa.selenium.support.ui.WebDriverWait;

import java.time.Duration;

public class DashboardPage {
    private WebDriver driver;

    public DashboardPage(WebDriver driver) {
        this.driver = driver;
    }

    public boolean isDashboardLoaded() {
        try {
            WebDriverWait wait = new WebDriverWait(driver, Duration.ofSeconds(10));
            return wait.until(d -> d.getCurrentUrl().contains("/admin") && !d.getCurrentUrl().contains("login"));
        } catch (Exception e) {
            return false;
        }
    }
}
