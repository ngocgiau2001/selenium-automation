package tests.login;

import base.BaseTest;
import data.TestData;
import org.testng.Assert;
import org.testng.annotations.DataProvider;
import org.testng.annotations.Test;
import pages.DashboardPage;
import pages.LoginPage;

public class LoginTest extends BaseTest {

    @Test(priority = 1, description = "TC01: Login thành công với tài khoản admin hợp lệ")
    public void testValidAdminLogin() {
        LoginPage loginPage = new LoginPage(driver);
        DashboardPage dashboardPage = loginPage.login(TestData.VALID_ADMIN_EMAIL, TestData.VALID_ADMIN_PASSWORD);

        Assert.assertTrue(dashboardPage.isDashboardLoaded(),
                "Phải chuyển hướng đến Dashboard sau khi login thành công");
    }

    @Test(priority = 2, dataProvider = "invalidLoginData", description = "TC02-07: Login thất bại với các dữ liệu không hợp lệ")
    public void testInvalidLogin(String email, String password, String scenario) {
        LoginPage loginPage = new LoginPage(driver);

        if (email != null && !email.isEmpty()) {
            loginPage.enterEmail(email);
        }
        if (password != null && !password.isEmpty()) {
            loginPage.enterPassword(password);
        }
        loginPage.clickLogin();

        // Kiểm tra xem message lỗi có hiển thị hoặc vẫn đang ở lại trang login không
        boolean isErrorShown = loginPage.isErrorMessageDisplayed();
        boolean isStillOnLoginPage = driver.getCurrentUrl().contains("login");

        Assert.assertTrue(isErrorShown || isStillOnLoginPage, "Test failed cho kịch bản: " + scenario);
    }

    @DataProvider(name = "invalidLoginData")
    public Object[][] getInvalidLoginData() {
        return new Object[][] {
                { "", TestData.VALID_ADMIN_PASSWORD, "Email rỗng" },
                { TestData.VALID_ADMIN_EMAIL, "", "Password rỗng" },
                { "", "", "Email và Password rỗng" },
                { TestData.INVALID_EMAIL, TestData.VALID_ADMIN_PASSWORD, "Email không tồn tại" },
                { TestData.VALID_ADMIN_EMAIL, TestData.INVALID_PASSWORD, "Password sai" },
                { "admin_invalid_format", TestData.VALID_ADMIN_PASSWORD, "Email sai định dạng (không có @)" },
                { " admin@gmail.com", TestData.VALID_ADMIN_PASSWORD, "Email có chứa khoảng trắng ở đầu" }
        };
    }
}
