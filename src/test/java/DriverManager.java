import org.openqa.selenium.WebDriver;
import org.openqa.selenium.chrome.ChromeDriver;
import org.openqa.selenium.chrome.ChromeOptions;
import org.openqa.selenium.firefox.FirefoxDriver;
import org.openqa.selenium.ie.InternetExplorerDriver;
import org.openqa.selenium.remote.RemoteWebDriver;
import org.openqa.selenium.support.events.EventFiringWebDriver;
import org.openqa.selenium.support.ui.WebDriverWait;
import org.testng.annotations.AfterClass;
import org.testng.annotations.BeforeClass;
import org.testng.annotations.Optional;
import org.testng.annotations.Parameters;

import java.io.File;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.TimeUnit;

/**
 * Created by stoykov on 09.09.2018.
 */
public class DriverManager {
    protected WebDriver driver;
    protected WebDriverWait wait;
    EventFiringWebDriver eventDriver;

    private WebDriver getDriver(String browser){
        switch (browser){
            case "firefox":
                System.setProperty(
                        "webdriver.gecko.driver",
                        new File(DriverManager.class.getResource("/geckodriver.exe").getFile()).getPath());
                return new FirefoxDriver();

            case "ie":
            case "internet explorer":
                System.setProperty(
                        "webdriver.ie.driver",
                        new File(DriverManager.class.getResource("/IEDriverServer.exe").getFile()).getPath());
                return new InternetExplorerDriver();
            case "mobile":
                System.setProperty(
                        "webdriver.chrome.driver",
                        new File(DriverManager.class.getResource("/chromedriver.exe").getFile()).getPath());
                Map<String, String> mobileEmulation = new HashMap<>();
                mobileEmulation.put("deviceName","iPhone 6");

                ChromeOptions chromeOptions = new ChromeOptions();
                chromeOptions.setExperimentalOption("mobileEmulation", mobileEmulation);
                return new ChromeDriver(chromeOptions);
            case "remote-chrome":
                ChromeOptions optionsRemote = new ChromeOptions();
                try {
                    return new RemoteWebDriver(new URL("http://localhost:4444/wd/hub"), optionsRemote);
                } catch (MalformedURLException e){
                    e.printStackTrace();
                }
                return null;
            case "chrome":
            default:
                System.setProperty(
                        "webdriver.chrome.driver",
                        new File(DriverManager.class.getResource("/chromedriver.exe").getFile()).getPath());
                return new ChromeDriver();
        }
    }

    @BeforeClass
    @Parameters("selenium.browser")
    public void setUp(@Optional("chrome") String browser){
        driver = getDriver(browser);
        eventDriver = new EventFiringWebDriver(driver);
        eventDriver.register(new WebEventListener());

        driver.manage().timeouts().implicitlyWait(10, TimeUnit.SECONDS);
        driver.manage().timeouts().pageLoadTimeout(30,TimeUnit.SECONDS);
        driver.manage().window().maximize();

        wait = new WebDriverWait(driver, 5);
        String bv = browser;
    }

    @AfterClass
    public void tearDown(){
        if (driver != null){
            driver.quit();
        }
    }
}
