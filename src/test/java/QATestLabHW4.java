import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.chrome.ChromeDriver;
import org.openqa.selenium.interactions.Actions;
import org.openqa.selenium.support.events.EventFiringWebDriver;
import org.openqa.selenium.support.ui.ExpectedCondition;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.WebDriverWait;
import org.testng.Assert;
import org.testng.annotations.AfterClass;
import org.testng.annotations.BeforeClass;
import org.testng.annotations.DataProvider;
import org.testng.annotations.Test;

import java.util.concurrent.TimeUnit;

/**
 * Created by stoykov on 02.09.2018.
 */

public class QATestLabHW4 extends DriverManager {
/*    WebDriver driver = getChromeDriver();
    EventFiringWebDriver eventDriver = new EventFiringWebDriver(driver);
    WebDriverWait wait = new WebDriverWait(driver, 10);*/

    @DataProvider(name = "Authentication")
    public static Object[][] credentials(){
        return new Object[][] {{"webinar.test@gmail.com","Xcg7299bnSmMuRLp9ITw"}};
    }

/*    @BeforeClass
    public void setUp(){
        eventDriver.register(new WebEventListener());
        eventDriver.manage().timeouts().implicitlyWait(5,TimeUnit.SECONDS);
        eventDriver.manage().window().maximize();
    }*/

    @Test(dataProvider = "Authentication")
    public void addNewProduct(String adminEmail,String adminPass){
        eventDriver.get("http://prestashop-automation.qatestlab.com.ua/admin147ajyvk0/");
        loginAsAdmin(adminEmail, adminPass);
        openProductsPage();
        addNewProduct();
    }

    @Test(dependsOnMethods = "addNewProduct")
    public void checkIsNewProductAdded(){
        eventDriver.get("http://prestashop-automation.qatestlab.com.ua/ru/");
        wait.until(ExpectedConditions.elementToBeClickable(eventDriver.findElement(By.className("all-product-link")))).click();
        WebElement newProduct = eventDriver.findElement(By.xpath("//a[text()='test1']"));
        Assert.assertTrue(newProduct.isDisplayed());
        newProduct.click();
        Assert.assertEquals(eventDriver.findElement(By.className("h1")).getText().toLowerCase(),"test1");
        //System.out.println(driver.findElement(By.xpath("//span[@itemprop='price']")).getText());
        Assert.assertTrue(eventDriver.findElement(By.xpath("//span[@itemprop='price']")).getText().contains("25,00"));
        Assert.assertEquals(eventDriver.findElement(By.xpath("//div[@class='product-quantities']//span")).getText(),"5 Товары");

    }

/*    @AfterClass
    public void closeDriver(){
        eventDriver.quit();
    }*/

    private void loginAsAdmin(String adminEmail, String adminPass) {
        WebElement emailInput = eventDriver.findElement(By.id("email"));
        WebElement passInput = eventDriver.findElement(By.id("passwd"));
        WebElement submitButton = eventDriver.findElement(By.name("submitLogin"));

        emailInput.clear();
        emailInput.sendKeys(adminEmail);
        passInput.clear();
        passInput.sendKeys(adminPass);
        submitButton.click();
        wait.until(ExpectedConditions.visibilityOf(eventDriver.findElement(By.id("header_employee_box"))));
    }

    private void openProductsPage() {
        WebElement adminCatalog = eventDriver.findElement(By.id("subtab-AdminCatalog"));
        adminCatalog.click();
        wait.until(ExpectedConditions.elementToBeClickable(eventDriver.findElement(By.xpath("//a[@id='page-header-desc-configuration-add']//span"))));
    }

    private void addNewProduct() {
        eventDriver.findElement(By.xpath("//a[@id='page-header-desc-configuration-add']//span")).click();
        wait.until(ExpectedConditions.visibilityOf(eventDriver.findElement(By.id("form_step1_name_1"))));
        WebElement inputName = eventDriver.findElement(By.id("form_step1_name_1"));
        inputName.sendKeys("test1");
        WebElement inputCount = eventDriver.findElement(By.id("form_step1_qty_0_shortcut"));
        inputCount.clear();
        inputCount.sendKeys("5");
        WebElement inputPrice = eventDriver.findElement(By.id("form_step1_price_shortcut"));
        inputPrice.clear();
        inputPrice.sendKeys("25");
        WebElement activateButton = eventDriver.findElement(By.className("switch-input "));
        activateButton.click();
        wait.until(ExpectedConditions.visibilityOf(eventDriver.findElement(By.className("growl-message"))));
        System.out.println(eventDriver.findElement(By.className("growl-message")).getText());
        Assert.assertEquals(eventDriver.findElement(By.className("growl-message")).getText(),"Настройки обновлены.");
        eventDriver.findElement(By.className("growl-close")).click();
        wait.until(ExpectedConditions.invisibilityOf(eventDriver.findElement(By.className("growl-message"))));
        WebElement saveButton = eventDriver.findElement(By.xpath("//button[@type='submit']//span")); //button[@type='submit']//span, //input[@type='submit'] - old path
        saveButton.click();
        wait.until(ExpectedConditions.visibilityOf(eventDriver.findElement(By.className("growl-message"))));
        Assert.assertEquals(eventDriver.findElement(By.className("growl-message")).getText(),"Настройки обновлены.");
        eventDriver.findElement(By.className("growl-close")).click();
        wait.until(ExpectedConditions.invisibilityOf(eventDriver.findElement(By.className("growl-message"))));
    }

/*    public static WebDriver getChromeDriver(){
        System.setProperty("webdriver.chrome.driver", QATestLabHW4.class.getResource("chromedriver.exe").getPath());
        return new ChromeDriver();
    }*/

}
