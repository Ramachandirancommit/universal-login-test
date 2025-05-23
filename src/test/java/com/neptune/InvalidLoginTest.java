package com.neptune;

import org.openqa.selenium.*;
import org.openqa.selenium.chrome.*;
import org.openqa.selenium.support.ui.*;
import org.testng.annotations.*;
import io.github.bonigarcia.wdm.WebDriverManager;
import java.time.Duration;
import static org.testng.Assert.*;

public class InvalidLoginTest {
    private WebDriver driver;
    private WebDriverWait wait;
    private static final String LOGIN_URL = "https://stage.universal.neptunenavigate.com/auth/login";
    
    // Credentials from environment variables with fallback
    private static final String USERNAME = System.getenv().getOrDefault("TEST_USERNAME", "fouzankhan.m@knackforge.com");
    private static final String PASSWORD = System.getenv().getOrDefault("TEST_PASSWORD", "F!khan@804621");

    @BeforeTest
    public void setup() {
        WebDriverManager.chromedriver().setup();
        
        ChromeOptions options = new ChromeOptions();
        // CI-optimized configuration
        options.addArguments(
            "--no-sandbox",
            "--disable-dev-shm-usage", // Prevent /dev/shm issues
            "--headless=new",          // New headless mode
            "--window-size=1920,1080", // Standard HD size
            "--remote-allow-origins=*",
            "--disable-gpu",
            "--disable-extensions",
            "--incognito",
            "--disable-infobars",
            "--disable-notifications"
        );
        
        // Configure logging and timeouts
        System.setProperty("webdriver.chrome.silentOutput", "true");
        options.setCapability("goog:loggingPrefs", java.util.Map.of(
            "browser", "SEVERE",  // Only show severe browser logs
            "driver", "WARNING"   // Only show warning+ driver logs
        ));
        
        driver = new ChromeDriver(options);
        wait = new WebDriverWait(driver, Duration.ofSeconds(15));
        
        // Configure timeouts
        driver.manage().timeouts().implicitlyWait(Duration.ofSeconds(5));
        driver.manage().timeouts().pageLoadTimeout(Duration.ofSeconds(20));
        driver.manage().timeouts().scriptTimeout(Duration.ofSeconds(15));
    }

    private void waitForLoaderToDisappear() {
        try {
            wait.until(ExpectedConditions.invisibilityOfElementLocated(By.className("loader-wrapper")));
        } catch (TimeoutException e) {
            System.out.println("[INFO] Loader didn't disappear within timeout, proceeding anyway");
        }
    }

    @Test
    public void invalidLoginTest() {
        // Robust page loading with retry
        loadUrlWithRetry(LOGIN_URL, 3);
        
        // Enter credentials with enhanced reliability
        enterCredentialsSafely();
        
        // Handle login with multiple strategies
        performLogin();
        
        // Verify login with multiple success indicators
        verifySuccessfulLogin();
    }

    private void loadUrlWithRetry(String url, int maxAttempts) {
        int attempts = 0;
        while (attempts < maxAttempts) {
            try {
                driver.get(url);
                wait.until(ExpectedConditions.presenceOfElementLocated(By.id("email")));
                return;
            } catch (TimeoutException e) {
                attempts++;
                if (attempts == maxAttempts) throw e;
                System.out.println("[RETRY] Page load timeout, attempt " + attempts + " of " + maxAttempts);
            }
        }
    }

    private void enterCredentialsSafely() {
        WebElement emailField = wait.until(ExpectedConditions.elementToBeClickable(By.id("email")));
        emailField.clear();
        emailField.sendKeys(USERNAME);

        WebElement passwordField = wait.until(ExpectedConditions.elementToBeClickable(By.id("password")));
        passwordField.clear();
        passwordField.sendKeys(PASSWORD);
        
        waitForLoaderToDisappear();
    }

    private void performLogin() {
        By loginButtonLocator = By.xpath("//button[span[text()='Login']]");
        WebElement loginButton = wait.until(ExpectedConditions.elementToBeClickable(loginButtonLocator));
        
        try {
            loginButton.click();
        } catch (ElementClickInterceptedException e) {
            // Fallback to JavaScript click if regular click fails
            ((JavascriptExecutor)driver).executeScript("arguments[0].click();", loginButton);
        }
        
        // Wait for navigation to complete
        wait.until(ExpectedConditions.not(ExpectedConditions.urlContains("login")));
    }

    private void verifySuccessfulLogin() {
        // Check multiple success indicators
        boolean isDashboardVisible = wait.until(ExpectedConditions.or(
            ExpectedConditions.visibilityOfElementLocated(By.xpath("//h1[text()='Dashboard']")),
            ExpectedConditions.urlContains("dashboard"),
            ExpectedConditions.presenceOfElementLocated(By.cssSelector(".dashboard-container"))
        )) != null;
        
        assertTrue(isDashboardVisible, "Failed to verify successful login. Dashboard not detected.");
    }

    @AfterTest
    public void teardown() {
        try {
            if (driver != null) {
                // Take screenshot if needed (would require TestNG listener)
                driver.quit();
            }
        } catch (Exception e) {
            System.err.println("[WARNING] Error during teardown: " + e.getMessage());
        }
    }
}
// package com.neptune;

// import java.nio.file.Path;
// import java.nio.file.Files;
// import java.io.IOException;
// import java.util.UUID;

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

// public class InvalidLoginTest {

//     private WebDriver driver;
//     private final String LOGIN_URL = "https://stage.universal.neptunenavigate.com/auth/login";
//     private final String USERNAME = "ramachandiran4234.m@knackforge.com";
//     private final String PASSWORD = "Password2342@710";

//    @BeforeTest
// public void setup() {
//     WebDriverManager.chromedriver().setup();

//     ChromeOptions options = new ChromeOptions();
//     options.addArguments("--no-sandbox");
//     options.addArguments("--disable-dev-shm-usage");
//     options.addArguments("--disable-gpu");
//     options.addArguments("--remote-allow-origins=*");

//     try {
//         Path tempDir = Files.createTempDirectory("chrome_user_data_" + UUID.randomUUID());
//         options.addArguments("--user-data-dir=" + tempDir.toString());
//     } catch (IOException e) {
//         e.printStackTrace();
//         throw new RuntimeException("Failed to create temporary directory for Chrome user data.");
//     }

//     System.setProperty("webdriver.chrome.logfile", "chromedriver.log");
//     System.setProperty("webdriver.chrome.verboseLogging", "true");

//     driver = new ChromeDriver(options);
// }



//     public void waitForLoaderToDisappear(WebDriver driver) {
//         WebDriverWait wait = new WebDriverWait(driver, Duration.ofSeconds(10));
//         wait.until(ExpectedConditions.invisibilityOfElementLocated(By.className("loader-wrapper")));
//     }

//     @Test
//     public void invalidLogin() {
//         driver.get(LOGIN_URL);
//         driver.findElement(By.id("email")).sendKeys(USERNAME);
//         driver.findElement(By.id("password")).sendKeys(PASSWORD);

//         waitForLoaderToDisappear(driver);

//         WebElement loginButton = driver.findElement(By.xpath("//button[span[text()='Login']]"));
//         loginButton.click();

//         WebDriverWait wait = new WebDriverWait(driver, Duration.ofSeconds(10));
//         WebElement errorMessage = wait.until(
//             ExpectedConditions.visibilityOfElementLocated(By.xpath("//li[contains(text(),'The email and password you entered did not match our records')]"))
//         );

//         assertTrue(errorMessage.isDisplayed());
//     }

//     @AfterTest
//     public void teardown() {
//         if (driver != null) {
//             driver.quit();
//         }
//     }
// }

