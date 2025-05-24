package utility;

import java.io.FileInputStream;
import java.io.IOException;
import java.util.Properties;

public class configReader {
    static Properties prop;

    public static String getProperty(String key) {
        if (prop == null) {
            prop = new Properties();
            try {
                FileInputStream fis = new FileInputStream("testData/config.properties");
                prop.load(fis);
            } catch (IOException e) {
                e.printStackTrace();
            }
        }

        	return prop.getProperty(key);
        
        
    }
}
