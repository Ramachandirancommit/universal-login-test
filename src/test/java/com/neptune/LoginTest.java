package com.neptune;
import io.github.bonigarcia.wdm.WebDriverManager;
import org.openqa.selenium.*;
import org.openqa.selenium.chrome.ChromeDriver;
import org.openqa.selenium.support.ui.*;
import org.testng.annotations.*;

import java.time.Duration;

import static org.testng.Assert.assertTrue;

public class LoginTest {

    private WebDriver driver;
    private WebDriverWait wait;

    private final String LOGIN_URL = "https://stage.universal.neptunenavigate.com/auth/login";
    private final String USERNAME = "fouzankhan.m@knackforge.com";
    private final String PASSWORD = "F!khan@804621";

    @BeforeClass
    public void setUp() {
        WebDriverManager.chromedriver().setup();
        driver = new ChromeDriver();
        // Correct usage: use Duration.ofSeconds
        wait = new WebDriverWait(driver, Duration.ofSeconds(15));
        driver.manage().window().maximize();
    }

    @Test
    public void testValidLogin() {
        driver.get(LOGIN_URL);

        WebElement emailField = wait.until(ExpectedConditions.elementToBeClickable(By.id("email")));
        emailField.clear();
        emailField.sendKeys(USERNAME);

        WebElement passwordField = wait.until(ExpectedConditions.elementToBeClickable(By.id("password")));
        passwordField.clear();
        passwordField.sendKeys(PASSWORD);

        WebElement loginButton = wait.until(ExpectedConditions.elementToBeClickable(By.xpath("//button[span[text()='Login']]")));
        loginButton.click();

        wait.until(ExpectedConditions.urlContains("/dashboard"));

        WebElement dashboardHeader = wait.until(
                ExpectedConditions.visibilityOfElementLocated(By.xpath("//h1[text()='Dashboard']"))
        );
        assertTrue(dashboardHeader.isDisplayed(), "Dashboard should be visible after login.");
        System.out.println("hello");
    }

    @AfterClass
    public void tearDown() {
        if (driver != null) {
            driver.quit();
        }
    }
}
