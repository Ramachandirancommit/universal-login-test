package com.neptune;

import java.nio.file.Path;
import java.nio.file.Files;
import java.io.IOException;
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

public class LoginTest {
    private WebDriver driver;
    private final String LOGIN_URL = "https://stage.universal.neptunenavigate.com/auth/login";
    private final String USERNAME = "fouzankhan.m@knackforge.com";
    private final String PASSWORD = "F!khan@804621";

   @BeforeTest
public void setup() {
    WebDriverManager.chromedriver().setup();
    ChromeOptions options = new ChromeOptions();
    // options.addArguments("--headless");               // run headless (no UI)
    options.addArguments("--no-sandbox");             // bypass OS security model (required in CI)
    options.addArguments("--disable-dev-shm-usage");  // overcome resource issues in CI containers
    options.addArguments("--disable-gpu");            // disable GPU (safe for Linux)
    options.addArguments("--remote-allow-origins=*"); // needed for newer ChromeDriver versions

    try {
         Path tempDir = Files.createTempDirectory("chrome_" + UUID.randomUUID());
         options.addArguments("--user-data-dir=" + tempDir.toAbsolutePath());

    } catch (IOException e) {
        throw new RuntimeException("Failed to create Chrome temp directory", e);
    }

    driver = new ChromeDriver(options);
}

    // ✅ Add this reusable method
    public void waitForLoaderToDisappear(WebDriver driver) {
        WebDriverWait wait = new WebDriverWait(driver, Duration.ofSeconds(10));
        wait.until(ExpectedConditions.invisibilityOfElementLocated(By.className("loader-wrapper")));
    }

    @Test
    public void validLogin() {
        driver.get(LOGIN_URL);

        driver.findElement(By.id("email")).sendKeys(USERNAME);
        driver.findElement(By.id("password")).sendKeys(PASSWORD);

        // ✅ Wait for loader before clicking login
        waitForLoaderToDisappear(driver);

        WebElement loginButton = driver.findElement(By.xpath("//button[span[text()='Login']]"));
        loginButton.click();

        // ✅ Wait for dashboard to load (optional: can add another wait here too)
        WebDriverWait wait = new WebDriverWait(driver, Duration.ofSeconds(10));
        WebElement dashboardHeading = wait.until(
            ExpectedConditions.visibilityOfElementLocated(By.xpath("//h1[text()='Dashboard']"))
        );

        assertTrue(dashboardHeading.isDisplayed());
    }

    @AfterTest
    public void teardown() {
        if (driver != null) {
            driver.quit();
        }
    }
}
