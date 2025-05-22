package com.neptune;

import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.chrome.ChromeDriver;
import org.testng.annotations.AfterMethod;
import org.testng.annotations.BeforeMethod;

public class BaseClass {
    protected WebDriver driver;
    protected final String LOGIN_URL = "https://stage.universal.neptunenavigate.com/auth/login";
    protected final String USERNAME = "fouzankhan.m@knackforge.com";
    protected final String PASSWORD = "F!khan@804621";
    protected final String CHROME_DRIVER_PATH = "/usr/local/bin/chromedriver";

    @BeforeMethod
    public void setUpAndLogin() throws InterruptedException {
        System.setProperty("webdriver.chrome.driver", CHROME_DRIVER_PATH);
        driver = new ChromeDriver();
        driver.manage().window().maximize();

        // Do login
        driver.get(LOGIN_URL);
        driver.findElement(By.id("email")).sendKeys(USERNAME);
        driver.findElement(By.id("password")).sendKeys(PASSWORD);
        driver.findElement(By.xpath("//button[span[text()='Login']]")).click();
        Thread.sleep(5000); // Better: use WebDriverWait
    }

    @AfterMethod
        public void logoutAndTearDown() {
            try {
                // Click logout button (adjust XPath or locator as per your app)
                driver.findElement(By.xpath("//*[contains(@class, 'mb-0') and contains(@class, 'font-roboto')]")).click();
                Thread.sleep(2000);  // Wait for logout to complete (better to use explicit waits)
            } catch (Exception e) {
                System.out.println("Logout not found or skipped.");
            }

            if (driver != null) {
                driver.quit();
            }
        }
}