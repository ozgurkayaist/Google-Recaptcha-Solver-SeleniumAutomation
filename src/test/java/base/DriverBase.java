package base;

import org.junit.After;
import org.junit.Before;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.chrome.ChromeDriver;
import org.openqa.selenium.chrome.ChromeOptions;
import org.openqa.selenium.remote.DesiredCapabilities;

public class DriverBase extends PropertyLoader {
    protected static WebDriver driver;
    public static PropertyLoader propertyLoader;
    public String apiKey;
    public String googleKey;
    public String pageUrl;
    public String proxyIp;
    public String proxyPort;
    public String proxyUser;
    public String proxyPw;

    @Before
    public void setUp() {
        String baseUrl = "https://www.google.com/recaptcha";
        DesiredCapabilities capabilities;
        propertyLoader = new PropertyLoader();
        apiKey = propertyLoader.getProperty("apiKey");
        googleKey = propertyLoader.getProperty("googleKey");
        pageUrl = propertyLoader.getProperty("pageUrl");
        proxyIp = propertyLoader.getProperty("proxyIp");
        proxyPort = propertyLoader.getProperty("proxyPort");
        proxyUser = propertyLoader.getProperty("proxyUser");
        proxyPw = propertyLoader.getProperty("proxyPw");
        ChromeOptions options = new ChromeOptions();
        options.addArguments("--start-fullscreen");
        options.addArguments("--disable-notifications");
        System.setProperty("webdriver.chrome.driver", "src/test/resources/drivers/chromedriver.exe");
        driver = new ChromeDriver(options);
        driver.get(baseUrl);
    }

    @After
    public void tearDown() {
        if (driver != null) {
            driver.quit();
        }
    }

    public WebDriver getWebDriver() {
        return driver;
    }

    public void setWebDriver(WebDriver webDriver) {
        this.driver = webDriver;
    }
}
