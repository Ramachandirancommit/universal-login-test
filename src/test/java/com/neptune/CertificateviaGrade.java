package com.neptune;
import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.chrome.ChromeDriver;
import org.testng.annotations.AfterTest;
import org.testng.annotations.BeforeTest;
import org.testng.annotations.Test;
import static org.testng.Assert.assertTrue;

public class CertificateviaGrade {

    private WebDriver driver;
    private final String CHROME_DRIVER_PATH = "/usr/local/bin/chromedriver";
    private final String LOGIN_URL = "https://stage.universal.neptunenavigate.com/auth/login";
    private final String USERNAME = "ramachandiran.m@knackforge.com";
    private final String PASSWORD = "Password@710";
    
    @BeforeTest
    public void setup() {
        System.setProperty("webdriver.chrome.driver", CHROME_DRIVER_PATH);
        driver = new ChromeDriver();
        driver.manage().window().maximize();
    }

    @Test
    public void testLogin() throws InterruptedException {
        driver.get(LOGIN_URL);
        driver.findElement(By.id("email")).sendKeys(USERNAME);
        driver.findElement(By.id("password")).sendKeys(PASSWORD);
        driver.findElement(By.xpath("//button[span[text()='Login']]")).click();
        Thread.sleep(6000);
        assertTrue(driver.findElement(By.xpath("//h1[text()='Dashboard']")).isDisplayed());

        driver.findElement(By.xpath("//span[@class='menuItem' and normalize-space()='Reports & Tools']")).click();
        driver.findElement(By.xpath("//span[@class='submenu' and normalize-space()='Gradebook Tools']")).click();
        driver.findElement(By.xpath("//span[@class='childmenu' and normalize-space()='Gradebook Tools']")).click();
    }

    @AfterTest
    public void teardown() {
        if (driver != null) {
            driver.quit();
        }
    }
}