package utility;

import org.apache.commons.io.FileUtils;
import org.openqa.selenium.*;

import java.io.File;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.Date;

public final class ScreenshotUtils {   // renamed class to ScreenshotUtil to match code 1

    private ScreenshotUtils() {}  // Utility class ⇒ no public constructor

    /** Captures a screenshot and returns the absolute path of the saved file. */
    public static String capture(WebDriver driver, String testName) {
        String timeStamp = new SimpleDateFormat("yyyyMMdd_HHmmss").format(new Date());
        String fileName  = testName + "_" + timeStamp + ".png";
        String dirPath   = "test-output" + File.separator + "screenshots";  // same folder structure as code 1
        File screenshot  = ((TakesScreenshot) driver).getScreenshotAs(OutputType.FILE);
        File dest        = new File(dirPath + File.separator + fileName);

        try {
            dest.getParentFile().mkdirs();  // Ensure directory exists
            FileUtils.copyFile(screenshot, dest);
            System.out.println("✅ Screenshot saved to: " + dest.getAbsolutePath());
        } catch (IOException e) {
            System.err.println("❌ Could not save screenshot: " + e.getMessage());
        }

        return dest.getAbsolutePath(); // Use this path for reporting tools
    }
}
