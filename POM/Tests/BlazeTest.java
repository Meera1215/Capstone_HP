package Tests;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.openqa.selenium.Alert;
import org.openqa.selenium.By;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.WebDriverWait;
import org.testng.Assert;
import org.testng.annotations.BeforeMethod;
import org.testng.annotations.Test;

import Pages.BlazeLoginPage;
import Pages.SelectProduct;
import Pages.ViewCart;
import utility.Constant;

import java.time.Duration;

public class BlazeTest extends TestBase {

    private static final Logger log = LogManager.getLogger(BlazeTest.class);

    BlazeLoginPage lp;
    SelectProduct hp;
    ViewCart cp;
    WebDriverWait wait;

    @BeforeMethod
    public void setup() {
        log.info("Initializing page objects");
        lp = new BlazeLoginPage(driver);
        hp = new SelectProduct(driver);
        cp = new ViewCart(driver);
        wait = new WebDriverWait(driver, Duration.ofSeconds(10));
    }

    @Test
    public void invalidThenValidLogin() throws Exception {
        log.info("Starting test: Invalid login, then valid login");

        // Step 1: Open login modal
        driver.findElement(By.id("login2")).click();
        wait.until(ExpectedConditions.visibilityOfElementLocated(By.id("loginusername")));

        // Step 2: Enter invalid credentials and submit
        WebElement username = driver.findElement(By.id("loginusername"));
        username.sendKeys(Constant.blazeUser);
        log.info("Entered username: " + Constant.blazeUser);

        WebElement password = driver.findElement(By.id("loginpassword"));
        password.sendKeys(Constant.blazePass1); // invalid password
        log.info("Entered password.");

        driver.findElement(By.xpath("//button[text()='Log in']")).click();
        log.info("Clicked login button.");

        // Step 3: Handle alert popup after invalid login
        try {
            wait.until(ExpectedConditions.alertIsPresent());
            Alert alert = driver.switchTo().alert();
            log.info("Alert text after invalid login: " + alert.getText());
            alert.accept();
        } catch (Exception e) {
            log.warn("No alert displayed after invalid login");
        }

        // Step 4: Clear username and password fields
        username.clear();
        password.clear();
        log.info("Cleared username and password fields.");

        // Step 5: Close the login modal
        driver.findElement(By.xpath("//div[@id='logInModal']//button[@class='btn btn-secondary']")).click();
        wait.until(ExpectedConditions.invisibilityOfElementLocated(By.id("loginusername")));

        // Step 6: Reopen the login modal
        driver.findElement(By.id("login2")).click();
        wait.until(ExpectedConditions.visibilityOfElementLocated(By.id("loginusername")));
        wait.until(ExpectedConditions.visibilityOfElementLocated(By.id("loginpassword")));

        // Step 7: Enter valid credentials and submit
        WebElement validUsername = driver.findElement(By.id("loginusername"));
        validUsername.sendKeys(Constant.blazeUser);
        log.info("Entered username: " + Constant.blazeUser);

        WebElement validPassword = driver.findElement(By.id("loginpassword"));
        validPassword.sendKeys(Constant.blazePass);
        log.info("Entered password.");

        driver.findElement(By.xpath("//button[text()='Log in']")).click();
        log.info("Clicked login button.");

        // Step 8: Validate successful login
        wait.until(ExpectedConditions.visibilityOfElementLocated(By.id("nameofuser")));
        Assert.assertTrue(lp.isLoginSuccessful(), "Login failed after valid credentials.");
        log.info("Valid login successful.");

        // Step 9: Select product and add to cart
        hp.selectProduct();
        cp.addToCart();
        cp.openCart();

        // Step 10: Place order and confirm purchase
        cp.placeOrder();
        cp.fillOrderDetails();
        cp.confirmPurchase();

        // Step 11: Verify confirmation message
        Assert.assertTrue(cp.isConfirmationDisplayed(), "Order confirmation message not displayed.");

        log.info("Test completed successfully: invalidThenValidLogin");
    }
}
