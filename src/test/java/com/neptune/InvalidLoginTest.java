package com.neptune;

import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.chrome.ChromeDriver;
import org.testng.annotations.AfterTest;
import org.testng.annotations.BeforeTest;
import org.testng.annotations.Test;
import static org.testng.Assert.assertTrue;
import org.openqa.selenium.chrome.ChromeOptions;


public class InvalidLoginTest {
    
    private WebDriver driver;
    private final String CHROME_DRIVER_PATH = "/usr/local/bin/chromedriver";
    private final String LOGIN_URL = "https://stage.universal.neptunenavigate.com/auth/login";
    // private final String USERNAME = "ramachandiran.m@knackforge.com";
    // private final String PASSWORD = "Password@710";
    private final String USERNAME = "ramachandiran4234.m@knackforge.com";
    private final String PASSWORD = "Password2342@710";

    @BeforeTest
public void setup() {
    ChromeOptions options = new ChromeOptions();
    options.addArguments("--headless");               // run headless (no UI)
    options.addArguments("--no-sandbox");             // bypass OS security model (required in CI)
    options.addArguments("--disable-dev-shm-usage");  // overcome resource issues in CI containers
    options.addArguments("--disable-gpu");            // disable GPU (safe for Linux)
    options.addArguments("--remote-allow-origins=*"); // needed for newer ChromeDriver versions

    driver = new ChromeDriver(options);
}


    @Test
    public void testLogin() throws InterruptedException {
        driver.get(LOGIN_URL);
        driver.findElement(By.id("email")).sendKeys(USERNAME);
        driver.findElement(By.id("password")).sendKeys(PASSWORD);
        driver.findElement(By.xpath("//button[span[text()='Login']]")).click();
        Thread.sleep(6000);
        assertTrue(driver.findElement(By.xpath("//li[contains(text(),'The email and password you entered did not match our records')]")).isDisplayed());
    }

    @AfterTest
    public void teardown() {
        if (driver != null) {
            driver.quit();
        }
    }
}
