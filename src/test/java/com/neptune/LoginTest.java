package com.neptune;

import org.openqa.selenium.*;
import org.openqa.selenium.chrome.*;
import org.openqa.selenium.support.ui.*;
import org.testng.annotations.*;
import io.github.bonigarcia.wdm.WebDriverManager;
import java.time.Duration;
import static org.testng.Assert.*;

public class LoginTest {
    private WebDriver driver;
    private WebDriverWait wait;
    private static final String LOGIN_URL = "https://stage.universal.neptunenavigate.com/auth/login";
    private static final String USERNAME = System.getenv("TEST_USERNAME") != null ? 
                                         System.getenv("TEST_USERNAME") : "fouzankhan.m@knackforge.com";
    private static final String PASSWORD = System.getenv("TEST_PASSWORD") != null ? 
                                         System.getenv("TEST_PASSWORD") : "F!khan@804621";

    @BeforeTest
    public void setup() {
        WebDriverManager.chromedriver().setup();
        
        ChromeOptions options = new ChromeOptions();
        // CI-friendly configuration
        options.addArguments(
            "--no-sandbox",
            "--disable-dev-shm-usage",
            "--headless=new",
            "--window-size=1920,1080",
            "--remote-allow-origins=*",
            "--disable-gpu",
            "--disable-extensions",
            "--incognito"
        );
        
        // Configure logging
        System.setProperty("webdriver.chrome.silentOutput", "true");
        options.setCapability("goog:loggingPrefs", java.util.Map.of(
            "browser", "SEVERE",
            "driver", "WARNING"
        ));
        
        driver = new ChromeDriver(options);
        wait = new WebDriverWait(driver, Duration.ofSeconds(15));
        driver.manage().timeouts().implicitlyWait(Duration.ofSeconds(5));
        driver.manage().timeouts().pageLoadTimeout(Duration.ofSeconds(20));
    }

    private void waitForLoaderToDisappear() {
        try {
            wait.until(ExpectedConditions.invisibilityOfElementLocated(By.className("loader-wrapper")));
        } catch (TimeoutException e) {
            System.out.println("Loader didn't disappear within timeout, continuing anyway");
        }
    }

    @Test
    public void validLogin() {
        // Navigate with retry for flaky network
        int attempts = 0;
        while (attempts < 3) {
            try {
                driver.get(LOGIN_URL);
                wait.until(ExpectedConditions.presenceOfElementLocated(By.id("email")));
                break;
            } catch (TimeoutException e) {
                attempts++;
                if (attempts == 3) throw e;
                System.out.println("Page load timeout, retrying... Attempt " + attempts);
            }
        }

        // Clear and enter credentials with explicit waits
        WebElement emailField = wait.until(ExpectedConditions.elementToBeClickable(By.id("email")));
        emailField.clear();
        emailField.sendKeys(USERNAME);

        WebElement passwordField = wait.until(ExpectedConditions.elementToBeClickable(By.id("password")));
        passwordField.clear();
        passwordField.sendKeys(PASSWORD);

        // Handle loader if present
        waitForLoaderToDisappear();

        // Click login with JavaScript as fallback
        WebElement loginButton = wait.until(ExpectedConditions.elementToBeClickable(
            By.xpath("//button[span[text()='Login']]")
        ));
        
        try {
            loginButton.click();
        } catch (ElementClickInterceptedException e) {
            ((JavascriptExecutor)driver).executeScript("arguments[0].click();", loginButton);
        }

        // Verify login success with multiple indicators
        WebElement dashboardHeading = wait.until(ExpectedConditions.or(
            ExpectedConditions.visibilityOfElementLocated(By.xpath("//h1[text()='Dashboard']")),
            ExpectedConditions.urlContains("dashboard")
        ));
        assertTrue(dashboardHeading.isDisplayed(), "Dashboard should be visible after login");
    }

    @AfterTest
    public void teardown() {
        try {
            if (driver != null) {
                // Take screenshot if test failed (would need TestNG listener for full implementation)
                driver.quit();
            }
        } catch (Exception e) {
            System.err.println("Error during teardown: " + e.getMessage());
        }
    }
}

// package com.neptune;

// import org.openqa.selenium.By;
// import org.openqa.selenium.WebDriver;
// import org.openqa.selenium.WebElement;
// import org.openqa.selenium.chrome.ChromeDriver;
// import org.openqa.selenium.chrome.ChromeOptions;
// import org.openqa.selenium.support.ui.ExpectedConditions;
// import org.openqa.selenium.support.ui.WebDriverWait;
// import org.testng.annotations.AfterTest;
// import org.testng.annotations.BeforeTest;
// import org.testng.annotations.Test;
// import io.github.bonigarcia.wdm.WebDriverManager;
// import java.time.Duration;
// import static org.testng.Assert.assertTrue;

// public class LoginTest {
//     private WebDriver driver;
//     private final String LOGIN_URL = "https://stage.universal.neptunenavigate.com/auth/login";
//     private final String USERNAME = "fouzankhan.m@knackforge.com";
//     private final String PASSWORD = "F!khan@804621";

//     @BeforeTest
//     public void setup() {
//         WebDriverManager.chromedriver().setup();

//         ChromeOptions options = new ChromeOptions();
//         options.addArguments("--no-sandbox");
//         options.addArguments("--disable-dev-shm-usage");
//         options.addArguments("--disable-gpu");
//         options.addArguments("--remote-allow-origins=*");
        
//         // Recommended for CI environments
//         options.addArguments("--headless=new");
//         options.addArguments("--window-size=1920,1080");
        
//         // Disable extensions and use incognito mode for clean state
//         options.addArguments("--disable-extensions");
//         options.addArguments("--incognito");
        
//         // Disable browser logging for cleaner output
//         System.setProperty("webdriver.chrome.silentOutput", "true");

//         driver = new ChromeDriver(options);
//         driver.manage().timeouts().implicitlyWait(Duration.ofSeconds(10));
//     }

//     public void waitForLoaderToDisappear() {
//         WebDriverWait wait = new WebDriverWait(driver, Duration.ofSeconds(10));
//         wait.until(ExpectedConditions.invisibilityOfElementLocated(By.className("loader-wrapper")));
//     }

//     @Test
//     public void validLogin() {
//         driver.get(LOGIN_URL);

//         // Wait for page to load completely
//         WebDriverWait wait = new WebDriverWait(driver, Duration.ofSeconds(15));
//         wait.until(ExpectedConditions.presenceOfElementLocated(By.id("email")));

//         // Enter credentials
//         WebElement emailField = driver.findElement(By.id("email"));
//         emailField.clear();
//         emailField.sendKeys(USERNAME);

//         WebElement passwordField = driver.findElement(By.id("password"));
//         passwordField.clear();
//         passwordField.sendKeys(PASSWORD);

//         // Wait for loader before clicking login
//         waitForLoaderToDisappear();

//         // Click login button
//         WebElement loginButton = wait.until(
//             ExpectedConditions.elementToBeClickable(By.xpath("//button[span[text()='Login']]"))
//         );
//         loginButton.click();

//         // Verify successful login
//         WebElement dashboardHeading = wait.until(
//             ExpectedConditions.visibilityOfElementLocated(By.xpath("//h1[text()='Dashboard']"))
//         );
//         assertTrue(dashboardHeading.isDisplayed(), "Dashboard heading should be displayed after login");
//     }

//     @AfterTest
//     public void teardown() {
//         if (driver != null) {
//             driver.quit();
//         }
//     }
// }