package tests.courses;

import org.testng.Assert;
import org.testng.annotations.DataProvider;
import org.testng.annotations.Test;

import base.BaseTest;
import data.TestData;
import pages.DashboardPage;
import pages.LoginPage;
import pages.CoursesPage;

public class CoursesTest extends BaseTest {
    @Test(dataProvider = "searchData", description = "Kiểm tra tính năng tìm kiếm khóa học với nhiều kịch bản")
    public void testCoursesSearch(String keyword, String expectedResult, String scenario) {
        // 1. Đăng nhập và đợi Dashboard load xong
        LoginPage loginPage = new LoginPage(driver);
        DashboardPage dashboardPage = loginPage.login(TestData.VALID_ADMIN_EMAIL, TestData.VALID_ADMIN_PASSWORD);
        Assert.assertTrue(dashboardPage.isDashboardLoaded(), "Lỗi: Không thể đăng nhập vào Dashboard!");

        // 2. Chuyển sang trang Courses
        driver.get("https://edupathway.studyguide.dev/admin/courses");

        // Khởi tạo Kỹ sư quản lý trang Courses
        CoursesPage coursesPage = new CoursesPage(driver);

        // 3. Thực hiện tìm kiếm với từ khóa từ DataProvider
        coursesPage.searchCourse(keyword);

        // 4. Kiểm tra kết quả
        int resultsCount = coursesPage.getNumberOfResults();

        if (expectedResult.equals("FOUND")) {
            Assert.assertTrue(resultsCount > 0,
                    "Kịch bản [" + scenario + "] - Lỗi: Không tìm thấy khóa học nào với từ khóa: '" + keyword
                            + "'");
        } else if (expectedResult.equals("NOT_FOUND")) {
            Assert.assertEquals(resultsCount, 0, "Kịch bản [" + scenario
                    + "] - Lỗi: Đáng lẽ không được tìm thấy kết quả nào cho: '" + keyword + "'");
        } else if (expectedResult.equals("NO_CRASH")) {
            Assert.assertTrue(resultsCount >= 0, "Kịch bản [" + scenario + "] - Lỗi: Hệ thống bị crash!");
        } else if (expectedResult.equals("NOT_ALL_DATA")) {
            // Giả sử tổng số data trên page là 5, nếu trả về 5 tức là trả về toàn bộ
            Assert.assertTrue(resultsCount < 5 || resultsCount == 0, "Kịch bản [" + scenario
                    + "] - Lỗi: Hệ thống trả về toàn bộ data (Dính SQL Injection hoặc xử lý sai)!");
        } else if (expectedResult.equals("ALL_DATA")) {
            Assert.assertTrue(resultsCount > 0,
                    "Kịch bản [" + scenario + "] - Lỗi: Hệ thống không trả về danh sách đầy đủ ban đầu!");
        }
    }

    @DataProvider(name = "searchData")
    public Object[][] getSearchData() {
        return new Object[][] {
                // Nhóm Positive
                { "management", "FOUND", "TC01 - Tìm kiếm từ khóa chính xác (Exact match)" },
                { "MANAGEMENT", "FOUND", "TC02 - Tìm kiếm không phân biệt hoa thường (Case insensitive)" },
                { "manage", "FOUND", "TC03 - Tìm kiếm một phần của từ (Partial match)" },

                // Nhóm Negative / Trống
                { "xyz123abc", "NOT_FOUND", "TC04 - Tìm kiếm chuỗi không tồn tại" },
                { "", "ALL_DATA", "TC05 - Để trống ô tìm kiếm (Empty search)" },
                { "     ", "ALL_DATA", "TC06 - Tìm kiếm toàn khoảng trắng (Space only)" },

                // Nhóm Edge / Boundary
                { "   management   ", "FOUND", "TC07 - Xóa khoảng trắng thừa (Trim input)" },
                { "@#$%^&*()_+", "NO_CRASH", "TC08 - Tìm kiếm với ký tự đặc biệt" },
                { "management".repeat(100), "NO_CRASH", "TC09 - Tìm kiếm chuỗi cực kỳ dài (Max length)" },

                // Nhóm Security
                { "' OR 1=1 --", "NOT_ALL_DATA", "TC10 - SQL Injection cơ bản" },
                { "<script>alert('hack')</script>", "NO_CRASH", "TC11 - Cross-Site Scripting (XSS) cơ bản" },

                // Nhóm Performance
                { "management".repeat(1000), "NO_CRASH", "TC12 - Tìm kiếm với từ khóa cực kỳ dài" },
                { "management".repeat(5000), "NO_CRASH", "TC13 - Tìm kiếm với từ khóa siêu dài" }
        };
    }
}
