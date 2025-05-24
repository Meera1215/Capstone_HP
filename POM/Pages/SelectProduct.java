package Pages;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;

public class SelectProduct {

    private static final Logger log = LogManager.getLogger(SelectProduct.class);
    WebDriver driver;

    public SelectProduct(WebDriver driver) {
        this.driver = driver;
    }

    By productLink = By.xpath("//a[text()='Samsung galaxy s6']");

    public void selectProduct() throws InterruptedException {
        log.info("Selecting product: Samsung galaxy s6");
        driver.findElement(productLink).click();
        Thread.sleep(3000);
    }
}
