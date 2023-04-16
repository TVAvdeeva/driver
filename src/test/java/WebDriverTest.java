import io.github.bonigarcia.wdm.WebDriverManager;
import org.apache.logging.log4j.LogManager;

import org.openqa.selenium.*;
import org.openqa.selenium.chrome.ChromeDriver;
import org.openqa.selenium.chrome.ChromeOptions;

import java.time.Duration;
import org.junit.jupiter.api.*;


public class WebDriverTest {
    private WebDriver driver;

    private final String login = "websbor@bk.ru";
    private final String password = "Cjgkz2727!!!";
    private org.apache.logging.log4j.Logger logger = LogManager.getLogger(WebDriverTest.class);
    private ChromeOptions options = new ChromeOptions();

    private void clearAndEnter(WebElement element, String text) {
        element.clear();
        element.sendKeys(text);
    }


    private void authUser() {

        driver.findElement(By.cssSelector(".header3__button-sign-in")).click();
        driver.findElement(By.xpath("//form[@action = '/login/']//input[@name='email']")).sendKeys(login);
        driver.findElement(By.xpath("//form[@action = '/login/']//input[@name='password']")).sendKeys(password);
        driver.findElement(By.xpath("//form[@action = '/login/']//button[@type='submit']")).click();
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
    public void headlessTest() {

        options.addArguments("--headless");
        driver = new ChromeDriver(options);
        driver.manage().window().fullscreen();
        driver.manage().timeouts().implicitlyWait(Duration.ofSeconds(1000));
        driver.get("https://duckduckgo.com/");
        driver.findElement(By.id("search_form_input_homepage")).sendKeys("ОТУС", Keys.ENTER);
        String element = driver.findElement(By.xpath("(//*[@data-testid='result-title-a'])[1]")).getText();
        Assertions.assertEquals("Онлайн‑курсы для профессионалов, дистанционное обучение современным ...", element);
    }

    @Test
    public void KioskTest() {

        driver = new ChromeDriver(options);
        driver.manage().window().fullscreen();
        driver.manage().timeouts().implicitlyWait(Duration.ofSeconds(1000));
        driver.get("https://demo.w3layouts.com/demos_new/template_demo/03-10-2020/photoflash-liberty-demo_Free/685659620/web/index.html?_ga=2.181802926.889871791.1632394818-2083132868.1632394818");
        driver.findElement(By.xpath("//span[@class='image-block']/a")).click();
        WebElement element = driver.findElement(By.xpath("//div[@class='pp_pic_holder light_rounded']"));
        Assertions.assertTrue(element.isDisplayed());

    }

    @Test
    public void otusTest(){

        driver = new ChromeDriver(options);
        driver.manage().window().maximize();
         driver.manage().timeouts().implicitlyWait(Duration.ofSeconds(1000));
        driver.get("https://otus.ru/");
        authUser();
        String actual = driver.getTitle();

        Assertions.assertEquals("Онлайн‑курсы для профессионалов, дистанционное обучение современным профессиям", actual);
        String  cookiesGet = String.valueOf(driver.manage().getCookies());
        logger.info(cookiesGet);

    }



    @AfterEach
    public void afterTest() {
        if (driver != null)
            driver.quit();
        logger.info("Все");
    }

}
