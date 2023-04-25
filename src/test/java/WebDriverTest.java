import io.github.bonigarcia.wdm.WebDriverManager;
import org.apache.logging.log4j.LogManager;

import org.apache.logging.log4j.Logger;
import org.openqa.selenium.*;
import org.openqa.selenium.chrome.ChromeDriver;
import org.openqa.selenium.chrome.ChromeOptions;

import java.time.Duration;
import org.junit.jupiter.api.*;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.WebDriverWait;
import java.util.*;

import java.util.concurrent.TimeUnit;

import static org.apache.commons.lang3.StringUtils.substring;
import static org.junit.jupiter.api.Assertions.assertEquals;

public class WebDriverTest {
    protected  WebDriver driver;
    private final String login=System.getProperty("login");
    private final String password = System.getProperty("password");
     private String  url=System.getProperty("url");

    private static Logger logger = LogManager.getLogger(WebDriverTest.class);
    private ChromeOptions options = new ChromeOptions();

    private void clearAndEnter(By element, String text) {
        driver.findElement(element).clear();
        driver.findElement(element).sendKeys(text);
    }


    private void authUser() throws InterruptedException {
        driver.findElement(By.cssSelector(".header3__button-sign-in")).click();
        WebElement form = driver.findElement(By.xpath("//form[@action = '/login/']"));
        Thread.sleep(100);
        form.findElement(By.xpath(".//input[@name='email']")).sendKeys(login);
        form.findElement(By.xpath(".//input[@name='password']")).sendKeys(password);
        form.findElement(By.xpath(".//button[@type='submit']")).click();

    }

    @BeforeAll
    public static void beforeTest() {
        WebDriverManager.chromedriver().setup();


    }

    @BeforeEach
    public void beforeEachTest() {
        options.addArguments("--remote-allow-origins=*");
    }


    @Test
    public void headlessTest() throws InterruptedException {
        options.addArguments("--headless");
        options.addArguments("--start-maximized");
        driver = new ChromeDriver(options);
        driver.manage().timeouts().implicitlyWait(1000, TimeUnit.SECONDS);
        driver.get("https://duckduckgo.com");
        WebDriverWait wait = new WebDriverWait(driver, Duration.ofSeconds(150));
        wait.until(ExpectedConditions.visibilityOfElementLocated(By.id("search_form_input_homepage")));
        clearAndEnter(By.id("search_form_input_homepage"), "ОТУС");
        driver.findElement(By.id("search_button_homepage")).submit();
        Thread.sleep(1000);
        String element = driver.findElement(By.xpath("//article[@id='r1-0']/div[2]/h2/a/span")).getText();
        assertEquals("Онлайн‑курсы для профессионалов, дистанционное обучение современным ...", element);

    }

    // @Test-   сайт упал!!! :((
    // public void kioskTest() {
    //     driver = new ChromeDriver(options);
    //     driver.get("https://demo.w3layouts.com/demos_new/template_demo/03-10-2020/photoflash-liberty-demo_Free/685659620/web/index.html?_ga=2.181802926.889871791.1632394818-2083132868.1632394818");
    //  driver.findElement(By.xpath("//span[@class='image-block']/a")).click();
    //    WebElement element = driver.findElement(By.cssSelector("div.pp_pic_holder.light_rounded"));
    //    Assertions.assertTrue(element.isDisplayed());

    //  }

    @Test
    public void otusTest() throws InterruptedException {

         if (url.trim().endsWith("/")) {
            url= substring(url,0,url.length()-1);
        }
        driver = new ChromeDriver(options);
        driver.manage().timeouts().implicitlyWait(1000, TimeUnit.SECONDS);
        driver.get(url);
        authUser();
        Thread.sleep(1000);
        String actual = driver.getTitle();
        assertEquals("Онлайн‑курсы для профессионалов, дистанционное обучение современным профессиям", actual);
        String  cookiesGet = String.valueOf(driver.manage().getCookies());

        logger.info(cookiesGet);

    }

    @AfterEach
    public void afterTest() {
        if (driver != null)
            driver.quit();

    }
}