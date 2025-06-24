package com.neptune;

import io.github.bonigarcia.wdm.WebDriverManager;
import org.openqa.selenium.*;
import org.openqa.selenium.chrome.*;
import org.openqa.selenium.support.ui.*;
import org.testng.annotations.*;

import java.time.Duration;

import static org.testng.Assert.assertTrue;

public class InvalidLoginTest {

    private WebDriver driver;
    private WebDriverWait wait;

    // URL and valid credentials
    private static final String LOGIN_URL = "https://stage.universal.neptunenavigate.com/auth/login";
    private static final String USERNAME = "fouzankhan.m@knackforge.com";
    private static final String PASSWORD = "F!khan@804621";

    @BeforeClass
    public void setUp() {
        
        WebDriverManager.chromedriver().setup();

        ChromeOptions options = new ChromeOptions();
        options.addArguments(
                "--no-sandbox",
                "--disable-dev-shm-usage",
                // Comment headless if you want to see the browser
                // "--headless=new",
                "--window-size=1920,1080",
                "--disable-gpu",
                "--disable-extensions"
        );

        driver = new ChromeDriver(options);
        // Explicit wait with 15 seconds timeout
        wait = new WebDriverWait(driver, Duration.ofSeconds(15));
        driver.manage().window().maximize();
    }

    @Test
    public void validLoginTest() {
        // Open login page
        driver.get(LOGIN_URL);

        // Wait and input username
        WebElement emailField = wait.until(ExpectedConditions.elementToBeClickable(By.id("email")));
        emailField.clear();
        emailField.sendKeys(USERNAME);

        // Wait and input password
        WebElement passwordField = wait.until(ExpectedConditions.elementToBeClickable(By.id("password")));
        passwordField.clear();
        passwordField.sendKeys(PASSWORD);

        // Click login button
        WebElement loginButton = wait.until(ExpectedConditions.elementToBeClickable(
                By.xpath("//button[span[text()='Login']]")));
        loginButton.click();

        // Wait for URL to contain "/dashboard" after successful login
        wait.until(ExpectedConditions.urlContains("/dashboard"));

        // Verify dashboard header is visible
        WebElement dashboardHeader = wait.until(ExpectedConditions.visibilityOfElementLocated(
                By.xpath("//h1[text()='Dashboard']")));
        assertTrue(dashboardHeader.isDisplayed(), "Dashboard should be visible after login.");
    }

    @AfterClass
    public void tearDown() {
        if (driver != null) {
            driver.quit();
        }
    }
}