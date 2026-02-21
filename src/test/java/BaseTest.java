import org.openqa.selenium.WebDriver;
import org.openqa.selenium.chrome.ChromeDriver;
import org.openqa.selenium.remote.RemoteWebDriver;
import org.openqa.selenium.MutableCapabilities;
import org.testng.annotations.AfterMethod;
import org.testng.annotations.BeforeMethod;
import org.testng.annotations.Parameters;
import java.io.FileInputStream;
import java.util.Properties;
import java.net.URL;
import java.time.Duration;
import java.util.HashMap;
import java.util.Map;

public class BaseTest {

    protected WebDriver driver;

    protected String USERNAME;
    protected String AUTOMATE_KEY;

    @Parameters({"browser", "browserVersion", "os", "osVersion", "deviceName"})
    @BeforeMethod
    public void setup(String browser,
                      String browserVersion,
                      String os,
                      String osVersion,
                      String deviceName) throws Exception {

        Properties prop = new Properties();
        prop.load(new FileInputStream("src/test/resources/application.properties"));
        USERNAME = prop.getProperty("browserstack.username");
        AUTOMATE_KEY = prop.getProperty("browserstack.accessKey");

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