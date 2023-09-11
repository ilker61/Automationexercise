import org.apache.commons.io.FileUtils;
import org.openqa.selenium.*;
import org.openqa.selenium.interactions.Actions;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.WebDriverWait;
import org.testng.annotations.AfterTest;
import org.testng.annotations.BeforeTest;
import org.testng.annotations.Optional;
import org.testng.annotations.Parameters;
import utils.Driver;

import java.io.File;
import java.io.IOException;
import java.time.Duration;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

public class BaseTest {

    protected WebDriver driver;

    protected WebDriverWait wait;

    @BeforeTest
    @Parameters({"browser"})
    public void setup(@Optional("chrome") String browser){
        driver= Driver.getDriver(browser);
        driver.manage().window().maximize();
        wait=new WebDriverWait(driver, Duration.ofSeconds(10));
    }


    @AfterTest
    public void tearDown(){
        driver.quit();
    }


    public void scrollToElement(WebElement element){
        new Actions(driver).scrollToElement(element).perform();
    }

    public void clickToElement(WebElement element){
        scrollToElement(element);
        wait.until(ExpectedConditions.elementToBeClickable(element)).click();
    }

    public void clickToLocator(By locator){
        wait.until(ExpectedConditions.elementToBeClickable(locator)).click();
    }

    public void clickByAction(By locator){
        WebElement element=wait.until(ExpectedConditions.visibilityOfElementLocated(locator));
        new Actions(driver).scrollToElement(element).moveToElement(element).click().perform();
    }

    public void clickByJS(WebElement element){
        JavascriptExecutor js=(JavascriptExecutor) driver;
        js.executeScript("arguments[0].click();", element);
    }

    public void clickByJS(By locator){
        WebElement element=wait.until(ExpectedConditions.visibilityOfElementLocated(locator));
        JavascriptExecutor js=(JavascriptExecutor) driver;
        js.executeScript("arguments[0].click();", element);
    }

    public void sendKeys(By locator, CharSequence...text){
        WebElement element= wait.until(ExpectedConditions.visibilityOfElementLocated(locator));
        element.clear();
        element.sendKeys(text);
    }

    public void takeScreenShot(String fileName){
        takeScreenShot(fileName, null);
    }

    public void selectHobby(String... hobbies) {
        String locator = "//div[./label[contains(., '%s')]]//input";


        for (String hobby : hobbies) {

            clickByJS(By.xpath(String.format(locator, hobby)));

        }
    }

    public void takeScreenShot(String fileName, WebElement element){
        TakesScreenshot screenshot = (TakesScreenshot) driver;
        File sourceFile, targetFile;
        if (element != null)
            sourceFile = element.getScreenshotAs(OutputType.FILE);
        else
            sourceFile = screenshot.getScreenshotAs(OutputType.FILE);

        String now = LocalDateTime.now().format(DateTimeFormatter.ofPattern("yyyyMMddHHmmss"));
        String name = fileName + "_" + now + ".png";
        targetFile = new File("screenshots/" + name);
        try {
            FileUtils.copyFile(sourceFile, targetFile);
        } catch (IOException e) {
            throw new RuntimeException(e);
        }

    }

}
