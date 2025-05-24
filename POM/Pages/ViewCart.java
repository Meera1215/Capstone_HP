package Pages;

import java.time.Duration;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.WebDriverWait;
import org.testng.Assert;

public class ViewCart {
    private static final Logger log = LogManager.getLogger(ViewCart.class);
    WebDriver driver;
    WebDriverWait wait;

    public ViewCart(WebDriver driver) {
        this.driver = driver;
        wait = new WebDriverWait(driver, Duration.ofSeconds(10));
    }

    public void addToCart() {
        log.info("Clicking 'Add to cart' button");
        WebElement addToCartBtn = wait.until(ExpectedConditions.elementToBeClickable(By.xpath("//*[@id='tbodyid']/div[2]/div/a")));
        addToCartBtn.click();

        log.info("Waiting for alert");
        wait.until(ExpectedConditions.alertIsPresent());

        String alertMessage = driver.switchTo().alert().getText();
        log.info("Alert message received: " + alertMessage);

        Assert.assertEquals(alertMessage, "Product added.", "Alert message mismatch!");
        log.info("Alert message verified successfully");

        driver.switchTo().alert().accept();
    }

    public void openCart() {
        log.info("Opening cart");
        WebElement cartLink = wait.until(ExpectedConditions.elementToBeClickable(By.id("cartur")));
        cartLink.click();
    }

    public void placeOrder() {
        log.info("Clicking 'Place Order' button");
        WebElement placeOrderBtn = wait.until(ExpectedConditions.elementToBeClickable(By.xpath("//button[text()='Place Order']")));
        placeOrderBtn.click();
    }

    public void fillOrderDetails() {
        log.info("Filling order details");
        wait.until(ExpectedConditions.visibilityOfElementLocated(By.id("orderModal")));
        driver.findElement(By.id("name")).sendKeys("John Doe");
        driver.findElement(By.id("country")).sendKeys("USA");
        driver.findElement(By.id("city")).sendKeys("New York");
        driver.findElement(By.id("card")).sendKeys("1234567812345678");
        driver.findElement(By.id("month")).sendKeys("12");
        driver.findElement(By.id("year")).sendKeys("2025");
    }

    public void confirmPurchase() {
        log.info("Confirming purchase");
        WebElement purchaseBtn = driver.findElement(By.xpath("//button[contains(text(), 'Purchase')]"));
        purchaseBtn.click();
    }

    public boolean isConfirmationDisplayed() {
        log.info("Checking confirmation message");
        WebElement confirmationMsg = wait.until(ExpectedConditions.visibilityOfElementLocated(By.xpath("//h2[contains(text(), 'Thank you for your purchase!')]")));
        boolean displayed = confirmationMsg.isDisplayed();
        log.info("Confirmation displayed: " + displayed);
        return displayed;
    }
}
