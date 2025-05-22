package com.neptune;

import java.nio.file.Path;
import java.nio.file.Files;
import java.io.IOException;
import java.util.UUID;

import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.chrome.ChromeDriver;
import org.openqa.selenium.chrome.ChromeOptions;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.WebDriverWait;

import org.testng.annotations.AfterTest;
import org.testng.annotations.BeforeTest;
import org.testng.annotations.Test;

import io.github.bonigarcia.wdm.WebDriverManager;

import java.time.Duration;

import static org.testng.Assert.assertTrue;

public class InvalidLoginTest {
    
    private WebDriver driver;
    private final String LOGIN_URL = "https://stage.universal.neptunenavigate.com/auth/login";
    private final String USERNAME = "ramachandiran4234.m@knackforge.com";
    private final String PASSWORD = "Password2342@710";

    @BeforeTest
    public void setup() {
        WebDriverManager.chromedriver().setup();

        ChromeOptions options = new ChromeOptions();
        options.addArguments("--no-sandbox");
        options.addArguments("--disable-dev-shm-usage");
        options.addArguments("--disable-gpu");
        options.addArguments("--remote-allow-origins=*");
    
        Path tempDir = Files.createTempDirectory("chrome_user_data_");
        options.addArguments("--user-data-dir=" + tempDir.toString());

        System.setProperty("webdriver.chrome.logfile", "chromedriver.log");
        System.setProperty("webdriver.chrome.verboseLogging", "true");

        driver = new ChromeDriver(options);
    }

    // ✅ Loader wait function
    public void waitForLoaderToDisappear(WebDriver driver) {
        WebDriverWait wait = new WebDriverWait(driver, Duration.ofSeconds(10));
        wait.until(ExpectedConditions.invisibilityOfElementLocated(By.className("loader-wrapper")));
    }

    @Test
    public void invalidLogin() {
        driver.get(LOGIN_URL);
        driver.findElement(By.id("email")).sendKeys(USERNAME);
        driver.findElement(By.id("password")).sendKeys(PASSWORD);

        // ✅ Wait for any loader to disappear
        waitForLoaderToDisappear(driver);

        WebElement loginButton = driver.findElement(By.xpath("//button[span[text()='Login']]"));
        loginButton.click();

        // ✅ Wait for error message to appear
        WebDriverWait wait = new WebDriverWait(driver, Duration.ofSeconds(10));
        WebElement errorMessage = wait.until(
            ExpectedConditions.visibilityOfElementLocated(By.xpath("//li[contains(text(),'The email and password you entered did not match our records')]"))
        );

        assertTrue(errorMessage.isDisplayed());
    }

    @AfterTest
    public void teardown() {
        if (driver != null) {
            driver.quit();
        }
    }
}
