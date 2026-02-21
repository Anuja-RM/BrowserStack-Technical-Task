import org.openqa.selenium.WebDriver;
import org.openqa.selenium.chrome.ChromeDriver;
import org.openqa.selenium.remote.RemoteWebDriver;
import org.openqa.selenium.MutableCapabilities;
import org.openqa.selenium.chrome.ChromeOptions;
import org.testng.annotations.AfterMethod;
import org.testng.annotations.BeforeMethod;
import org.testng.annotations.Parameters;

import java.net.URL;
import java.time.Duration;
import java.util.HashMap;
import java.util.Map;

public class BaseTest {

    protected WebDriver driver;

    String USERNAME = "anujamore_fLiN0R";
    String AUTOMATE_KEY = "bQT2Axz7tpED6xcBqU4J";

    @Parameters({"browser", "browserVersion", "os", "osVersion", "deviceName"})
    @BeforeMethod
    public void setup(String browser,
                      String browserVersion,
                      String os,
                      String osVersion,
                      String deviceName) throws Exception {

        if (browser == null || browser.isEmpty()) {
            driver = new ChromeDriver();
            driver.manage().window().maximize();
            return;
        }

        MutableCapabilities capabilities = new MutableCapabilities();
        Map<String, Object> bstackOptions = new HashMap<>();

        bstackOptions.put("projectName", "ElPais Scraper");
        bstackOptions.put("buildName", "Cross Browser Build");
        bstackOptions.put("sessionName", browser + " Test");

        if (deviceName != null && !deviceName.isEmpty()) {

            bstackOptions.put("deviceName", deviceName);
            bstackOptions.put("realMobile", "true");

        } else {

            capabilities.setCapability("browserName", browser);
            capabilities.setCapability("browserVersion", browserVersion);

            bstackOptions.put("os", os);
            bstackOptions.put("osVersion", osVersion);
        }

        capabilities.setCapability("bstack:options", bstackOptions);

        driver = new RemoteWebDriver(
                new URL("https://" + USERNAME + ":" + AUTOMATE_KEY +
                        "@hub.browserstack.com/wd/hub"),
                capabilities
        );
        driver.manage().timeouts().pageLoadTimeout(Duration.ofSeconds(600));
    }

    @AfterMethod
    public void tearDown() {
        if (driver != null) {
            driver.quit();
        }
    }
}