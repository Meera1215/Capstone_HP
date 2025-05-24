package Tests;

import java.io.File;
import java.lang.reflect.Method;
import java.time.Duration;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.chrome.ChromeDriver;
import org.openqa.selenium.chrome.ChromeOptions;
import org.testng.ITestResult;
import org.testng.annotations.AfterMethod;
import org.testng.annotations.AfterSuite;
import org.testng.annotations.BeforeMethod;
import org.testng.annotations.BeforeSuite;

import com.aventstack.extentreports.ExtentReports;
import com.aventstack.extentreports.ExtentTest;
import com.aventstack.extentreports.reporter.ExtentSparkReporter;

import Pages.BlazeLoginPage;
import Pages.SelectProduct;
import Pages.ViewCart;
import utility.Constant;
import utility.ScreenshotUtils;
import utility.configReader;

public class TestBase {

    public WebDriver driver;
    protected BlazeLoginPage loginPage;
    protected SelectProduct homePage;
    protected ViewCart cartPage;

    protected static final Logger logger = LogManager.getLogger(TestBase.class);

    protected static ExtentReports extent;      // 🔧 Now class-level static
    protected static ExtentTest test;           // 🔧 Available to child classes

    @BeforeSuite
    public void setUpReport() {
        String reportPath = "test-output" + File.separator + "ExtentReport.html";
        ExtentSparkReporter spark = new ExtentSparkReporter(reportPath);

        extent = new ExtentReports();           // 🔧 Assign to class-level variable
        extent.attachReporter(spark);
        extent.setSystemInfo("Project", "BlazeDemo Automation");
        extent.setSystemInfo("Tested By", "Meera Natarajan");
        extent.setSystemInfo("Date", "26/05/2025");

        logger.info("✅ ExtentReport initialized at: {}", reportPath);
    }

    @BeforeMethod
    public void launchApp(Method method) throws Exception {
        logger.info("======== Starting Test Setup: launchApp =========");

        ChromeOptions options = new ChromeOptions();
        String isHeadless = configReader.getProperty("headless");

        if ("true".equalsIgnoreCase(isHeadless)) {
            options.addArguments("--headless=new");
            logger.info("Running in headless mode.");
        } else {
            logger.info("Running in headed (visible) mode.");
        }

        driver = new ChromeDriver(options);
        driver.manage().window().maximize();
        driver.manage().timeouts().implicitlyWait(Duration.ofSeconds(10));
        driver.get(Constant.blaze_appURL);

        logger.info("Navigated to URL: {}", Constant.blaze_appURL);

        // Initialize Page Objects
        loginPage = new BlazeLoginPage(driver);
        homePage = new SelectProduct(driver);
        cartPage = new ViewCart(driver);

        // 📝 Create test node
        test = extent.createTest(method.getName());

        logger.info("======== Test Setup Complete: launchApp =========");
    }

    @AfterMethod
    public void tearDown(ITestResult result) {
        if (result.getStatus() == ITestResult.FAILURE) {
            String screenshotPath = ScreenshotUtils.capture(driver, result.getMethod().getMethodName());
            test.fail("❌ Test Failed: " + result.getThrowable());
            try {
                test.addScreenCaptureFromPath(screenshotPath);
            } catch (Exception e) {
                logger.error("⚠️ Failed to attach screenshot to report", e);
            }
            logger.error("Test failed: {}", result.getMethod().getMethodName(), result.getThrowable());
        } else if (result.getStatus() == ITestResult.SUCCESS) {
            test.pass("✅ Test Passed");
        } else if (result.getStatus() == ITestResult.SKIP) {
            test.skip("⚠️ Test Skipped");
        }

        if (driver != null) {
            driver.quit();
            logger.info("🔻 Browser closed");
        }
    }

    @AfterSuite
    public void tearDownReport() {
        if (extent != null) {
            extent.flush();  // ✅ Now it will not be null
            logger.info("📄 ExtentReport flushed and saved.");
        } else {
            logger.error("❌ ExtentReport was not initialized properly.");
        }
    }
}
