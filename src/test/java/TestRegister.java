import com.aventstack.extentreports.ExtentReports;
import com.aventstack.extentreports.ExtentTest;
import com.aventstack.extentreports.Status;
import com.aventstack.extentreports.reporter.ExtentSparkReporter;
import com.github.javafaker.Faker;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.Select;
import org.testng.Assert;
import org.testng.annotations.Test;
import utils.Driver;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

public class TestRegister extends BaseTest {

    Locators lc;

    ExtentSparkReporter html;
    ExtentReports report;
    ExtentTest test;


    @Test
    public void T1() {

        lc = new Locators();


        html = new ExtentSparkReporter("test-output/Report.html");
        report = new ExtentReports();
        report.attachReporter(html);
        test = report.createTest("Test 1", "AutomationExercises Web Sitesi Testi");


        test.info("Ilk Test icin Info: du bakali noolcek!");
        test.log(Status.WARNING, "Warning Warning Warning with Status");
        test.warning("My log as Warning, without Status");


        Driver.getDriver().get("https://automationexercise.com/");

        Faker faker = new Faker();

        try {
            wait.until(ExpectedConditions.visibilityOfAllElements(lc.eLogo));
            test.info("Basarili bir sekilde sayfa acildi...");
        } catch (Exception e) {
            test.warning("Sayfa acilamadi");
        }


        clickToElement(lc.eLogin);

        lc.eName.sendKeys(faker.name().fullName());


        lc.eEmail.sendKeys(faker.internet().emailAddress());

        try {
            clickToElement(lc.eSignUp);
            test.info("Login olundu..");
        } catch (Exception e) {
            test.warning("Login basarisiz");
        }


        clickByAction(lc.getGender("Mr."));

        lc.ePassword.sendKeys(faker.internet().password());

        new Select(lc.eSelectDays).selectByIndex(10);
        new Select(lc.eSelectMonths).selectByIndex(6);
        new Select(lc.eSelectYears).selectByIndex(20);

        for (WebElement each : lc.eCheckBoxes) {
            System.out.println(each.getText());
        }

        selectHobby("Sign up for our newsletter!", "Receive special offers from our partners!");


        sendKeys(lc.lFirstName, "ilker");
        sendKeys(lc.lLastName, "Kucukaslan");
        sendKeys(lc.lCompany, "EVB Training");
        sendKeys(lc.lAddress1, "Adapazari Meydani 1");
        sendKeys(lc.lAddress2, "Adapazari Meydani 2");

        new Select(lc.eSelectCountry).selectByIndex(4);

        sendKeys(lc.lState, "Tataristan");
        sendKeys(lc.lCity, "Texas");
        sendKeys(lc.lZipcode, "34523");
        sendKeys(lc.lMobileNumber, "+491732433234");

        clickByJS(lc.lCreateAccount);


        try {
            wait.until(ExpectedConditions.visibilityOfElementLocated(lc.lAccountCreated));
            test.info("Account create edildi");
        } catch (Exception e) {
            test.warning("Account create edilemedi");
        }

        // String win = driver.getWindowHandle();

        clickByJS(lc.lContinue);

        //wait.until(ExpectedConditions.visibilityOfAllElements(lc.eLoggedVisiable));

        /*
        String newWin = driver.getWindowHandles()
                .stream()
                .filter(w -> !w.equalsIgnoreCase(win))
                .findFirst().get();

        driver.switchTo().window(newWin).close();
        driver.switchTo().window(win);

         */

        scrollToElement(lc.eAccountDeleted);


        clickByAction(lc.lDeleteAccount);

        try {
            wait.until(ExpectedConditions.visibilityOfAllElements(lc.eAccountDeleted));
            test.info("Account basariyla silindi..");
        } catch (Exception e) {
            test.warning("Account silinemedi");
        }


        try {
            clickByAction(lc.lContinueLast);
            test.pass("Test PASS olduu");
        } catch (Exception e) {
            test.fail("Test FAIL oldu");
        }


        report.setSystemInfo("CID Team", "Test Automatisierer Team");
        report.setSystemInfo("OS", System.getProperty("os.name"));
        report.setSystemInfo("Directory", System.getProperty("user.dir"));
        report.setSystemInfo("Date Time", LocalDateTime.now().format(DateTimeFormatter.ofPattern("dd.MM.yyyy HH.mm.ss")));
        report.flush();
    }

}
