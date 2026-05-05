package pages;

import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.FindBy;
import org.openqa.selenium.support.PageFactory;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.WebDriverWait;

import java.time.Duration;
import java.util.List;

public class UsersPage {
    private WebDriverWait wait;

    // Xác định ô nhập liệu tìm kiếm
    @FindBy(css = "input[placeholder='Search employees...']")
    private WebElement searchInput;

    // Xác định danh sách các dòng trong bảng kết quả
    @FindBy(css = ".p-datatable-tbody tr")
    private List<WebElement> tableRows;

    public UsersPage(WebDriver driver) {
        this.wait = new WebDriverWait(driver, Duration.ofSeconds(10));
        PageFactory.initElements(driver, this);
    }

    // Hàm tìm kiếm nhân viên
    public void searchEmployee(String keyword) {
        // Chờ dữ liệu ban đầu load xong để tránh race condition (ghi đè kết quả)
        try { Thread.sleep(2000); } catch (Exception e) {}

        WebElement searchInput = wait.until(ExpectedConditions.visibilityOfElementLocated(org.openqa.selenium.By.cssSelector("input[placeholder='Search employees...']")));
        
        // Xóa nội dung bằng Javascript để tránh lỗi với chuỗi dài
        org.openqa.selenium.WebDriver driver = ((org.openqa.selenium.WrapsDriver) searchInput).getWrappedDriver();
        org.openqa.selenium.JavascriptExecutor js = (org.openqa.selenium.JavascriptExecutor) driver;
        
        // Gán giá trị và trigger event cho Vue 3
        js.executeScript(
            "arguments[0].value = arguments[1];" +
            "arguments[0].dispatchEvent(new Event('input', { bubbles: true }));" +
            "arguments[0].dispatchEvent(new Event('change', { bubbles: true }));", 
            searchInput, keyword
        );
        
        // Chờ kết quả tìm kiếm load xong
        try { Thread.sleep(3000); } catch (Exception e) {}
    }

    // Hàm đếm số lượng kết quả hiển thị
    public int getNumberOfResults() {
        try {
            wait.until(ExpectedConditions.visibilityOfAllElements(tableRows));
            
            // Nếu dòng duy nhất hiển thị là dòng thông báo trống (No results found)
            if (tableRows.size() == 1) {
                org.openqa.selenium.WebElement row = tableRows.get(0);
                String rowClass = row.getAttribute("class");
                String rowText = row.getText().toLowerCase();
                

                if ((rowClass != null && (rowClass.contains("empty") || rowClass.contains("no-data"))) 
                    || rowText.contains("no result") 
                    || rowText.contains("not found")
                    || rowText.contains("no records")
                    || rowText.contains("no employee yet")
                    || rowText.contains("không tìm thấy")
                    || rowText.trim().isEmpty()) {
                    return 0;
                }
            }
            
            return tableRows.size();
        } catch (Exception e) {
            return 0; // Trả về 0 nếu không có dòng nào
        }
    }
}
