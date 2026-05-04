package pages;

import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.FindBy;
import org.openqa.selenium.support.PageFactory;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.WebDriverWait;

import java.time.Duration;
import java.util.List;

public class StudentsPage {
    private WebDriverWait wait;

    // Xác định ô nhập liệu tìm kiếm
    @FindBy(css = "input[placeholder='Search students...']")
    private WebElement searchInput;

    // Xác định danh sách các dòng trong bảng kết quả
    @FindBy(css = ".p-datatable-tbody tr")
    private List<WebElement> tableRows;

    public StudentsPage(WebDriver driver) {
        this.wait = new WebDriverWait(driver, Duration.ofSeconds(10));
        PageFactory.initElements(driver, this);
    }

    // Hàm tìm kiếm sinh viên
    public void searchStudent(String keyword) {
        WebElement searchInput = wait.until(ExpectedConditions.visibilityOfElementLocated(org.openqa.selenium.By.cssSelector("input[placeholder='Search students...']")));
        
        // Xóa nội dung bằng cách nhấn BACK_SPACE nhiều lần để không làm hỏng v-model của Vue 3
        for (int i = 0; i < 30; i++) {
            searchInput.sendKeys(org.openqa.selenium.Keys.BACK_SPACE);
        }
        
        // Gõ từ khóa tìm kiếm
        searchInput.sendKeys(keyword);
        
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
                    || rowText.contains("no students yet")
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
