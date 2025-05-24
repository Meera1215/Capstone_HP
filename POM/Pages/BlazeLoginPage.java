package Pages;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;

public class BlazeLoginPage {

    private static final Logger log = LogManager.getLogger(BlazeLoginPage.class);
    WebDriver driver;

    public BlazeLoginPage(WebDriver driver) {
        this.driver = driver;
    }

    By loginLink = By.id("login2");
    By usernameField = By.id("loginusername");
    By passwordField = By.id("loginpassword");
    By loginButton = By.xpath("//button[text()='Log in']");
    By loggedInUser = By.id("nameofuser");

    public void login(String username, String password) throws InterruptedException {
        log.info("Clicking login link");
        driver.findElement(loginLink).click();
        Thread.sleep(2000);

        log.info("Entering username: " + username);
        driver.findElement(usernameField).sendKeys(username);

        log.info("Entering password");
        driver.findElement(passwordField).sendKeys(password);

        log.info("Clicking login button");
        driver.findElement(loginButton).click();
        Thread.sleep(3000);
    }

    public String getLoggedInUsername() {
        String welcomeText = driver.findElement(loggedInUser).getText();
        log.info("Logged in username text retrieved: " + welcomeText);
        return welcomeText;
    }

    public boolean isLoginSuccessful() {
        boolean displayed = driver.findElement(loggedInUser).isDisplayed();
        log.info("Is login successful? " + displayed);
        return displayed;
    }
}
