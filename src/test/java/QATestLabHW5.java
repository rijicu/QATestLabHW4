import org.openqa.selenium.By;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.testng.Assert;
import org.testng.annotations.Optional;
import org.testng.annotations.Parameters;
import org.testng.annotations.Test;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;

/**
 * Created by stoykov on 09.09.2018.
 */
public class QATestLabHW5 extends DriverManager {

    @Test
    @Parameters("selenium.browser")
    public void checkBrowserVersion(@Optional("chrome") String browser){
        eventDriver.get("http://prestashop-automation.qatestlab.com.ua/ru/");
        boolean isMobileVersion = eventDriver.findElement(By.xpath("//div[@id='_mobile_cart']")).isDisplayed();
        if (browser.equals("mobile")) {
            Assert.assertTrue(isMobileVersion,"It is Not mobile version!");
        } else {
            Assert.assertTrue(!isMobileVersion, "It is Not desktop version!");
        }
    }

    @Test
    public void createOrder(){
        eventDriver.get("http://prestashop-automation.qatestlab.com.ua/ru/");
        wait.until(ExpectedConditions.elementToBeClickable(eventDriver.findElement(By.className("all-product-link")))).click();
        openRandomProduct();

        String productName = eventDriver.findElement(By.className("h1")).getText().toLowerCase();
        String productPrice = eventDriver.findElement(By.xpath("//span[@itemprop='price']")).getText();
        System.out.println(productName + productPrice);
        String productUrl = driver.getCurrentUrl();

        int productQuantitiesBeforeOrder = getProductQuantities();

        addProductToCartAndOpenCartPage();
        String productNameInBasket = eventDriver.findElement(By.xpath("//div[@class='product-line-info']/a")).getText().toLowerCase();
        String productPriceInBasket = eventDriver.findElement(By.xpath("//div[@class='product-line-grid-body col-md-4 col-xs-8']/div[2]/span")).getText();
        String productQuantityInBasket = eventDriver.findElement(By.xpath("//div[@class='input-group bootstrap-touchspin']//input")).getAttribute("value");
        System.out.println(productNameInBasket + " " + productPriceInBasket);
        Assert.assertEquals(productNameInBasket,productName);
        Assert.assertTrue(productPriceInBasket.contains(productPrice));
        Assert.assertTrue(productQuantityInBasket.equals("1"));

        openOrderDataPage();

        fillOrderData();
        String message = eventDriver.findElement(By.xpath("//div[@class='col-md-12']//h3")).getText().toLowerCase();
        Assert.assertTrue(message.contains("ваш заказ подтверждён"));
        Assert.assertTrue(eventDriver.findElement(By.xpath("//div[@class='col-sm-4 col-xs-9 details']//span")).getText().toLowerCase().contains(productName));
        Assert.assertTrue(eventDriver.findElement(By.xpath("//div[@class='col-xs-5 text-sm-right text-xs-left']")).getText().contains(productPrice));
        Assert.assertEquals(eventDriver.findElement(By.className("col-xs-2")).getText(),"1");

        eventDriver.get(productUrl);
        int productQuantitiesAfterOrder = getProductQuantities();
        Assert.assertTrue(productQuantitiesAfterOrder == productQuantitiesBeforeOrder - 1);
    }

    private void openOrderDataPage() {
        wait.until(ExpectedConditions.elementToBeClickable(eventDriver.findElement(By.xpath("//div[@class='text-xs-center']/a"))));
        eventDriver.findElement(By.xpath("//div[@class='text-xs-center']/a")).click();
    }

    private void addProductToCartAndOpenCartPage() {
        eventDriver.findElement(By.xpath("//div[@class='add']//button")).click();
        wait.until(ExpectedConditions.elementToBeClickable(eventDriver.findElement(By.xpath("//div[@class='cart-content']//a")))).click();
    }

    private void fillOrderData() {
        wait.until(ExpectedConditions.visibilityOf(eventDriver.findElement(By.name("firstname"))));
        WebElement firstnameInput = eventDriver.findElement(By.name("firstname"));
        firstnameInput.sendKeys("Name");
        WebElement lastnameInput = eventDriver.findElement(By.name("lastname"));
        lastnameInput.sendKeys("Lastname");
        WebElement emailInput = eventDriver.findElement(By.name("email"));
        emailInput.clear();
        emailInput.sendKeys("rijicu@gmail.com");
        eventDriver.findElement(By.name("continue")).click();
        eventDriver.findElement(By.name("address1")).sendKeys("Kiev, Verhniy val 4");
        eventDriver.findElement(By.name("postcode")).sendKeys("20647");
        eventDriver.findElement(By.name("city")).sendKeys("Kyiv");
        eventDriver.findElement(By.name("confirm-addresses")).click();
        eventDriver.findElement(By.name("confirmDeliveryOption")).click();
        eventDriver.findElement(By.id("payment-option-2")).click();
        eventDriver.findElement(By.xpath("//div[@class='pull-xs-left']//input")).click();
        eventDriver.findElement(By.xpath("//div[@id='payment-confirmation']//button[@type='submit']")).click();
    }

    private void openRandomProduct() {
        List<WebElement> productsList = new ArrayList<WebElement>();
        productsList = eventDriver.findElements(By.className("product-description"));
        Random ran = new Random();
        int x = ran.nextInt(productsList.size());
        productsList.get(x).click();
    }

    private int getProductQuantities() {
        //open product details and Get product Quantities
        eventDriver.findElement(By.xpath("//a[@href='#product-details']")).click();
        wait.until(ExpectedConditions.visibilityOf(eventDriver.findElement(By.xpath("//div[@class='product-quantities']//span"))));
        String productQuantities = eventDriver.findElement(By.xpath("//div[@class='product-quantities']//span")).getText();
        System.out.println("Product Quantities is " + productQuantities);
        int spaceIndex = productQuantities.indexOf(" ");
        return Integer.parseInt(productQuantities.substring(0,spaceIndex));
    }
}
