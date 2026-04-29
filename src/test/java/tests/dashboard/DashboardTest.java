package tests.dashboard;

import base.BaseTest;
import data.TestData;
import org.testng.Assert;
import org.testng.annotations.Test;
import pages.DashboardPage;
import pages.LoginPage;

public class DashboardTest extends BaseTest {

    @Test
    public void testDashboardAccess() {
        LoginPage loginPage = new LoginPage(driver);
        DashboardPage dashboardPage = loginPage.login(TestData.VALID_ADMIN_EMAIL, TestData.VALID_ADMIN_PASSWORD);
        
        Assert.assertTrue(dashboardPage.isDashboardLoaded(), "Dashboard should be accessible after login");
    }
}
