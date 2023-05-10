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


import static org.apache.commons.lang3.StringUtils.substring;
import static org.junit.jupiter.api.Assertions.assertEquals;

public class WebDriverTest {
    private   WebDriver driver;
    private final String login=System.getProperty("login");
    private final String password = System.getProperty("password");
     private String  url=System.getProperty("url");

    private static Logger logger = LogManager.getLogger(WebDriverTest.class);
    private ChromeOptions options = new ChromeOptions();




    private void authUser()  {
        WebDriverWait wait = new WebDriverWait(driver, Duration.ofSeconds(50));
        wait.until(ExpectedConditions.presenceOfElementLocated(By.cssSelector(".header3__button-sign-in"))).click();
        WebElement form = driver.findElement(By.xpath("//form[@action = '/login/']"));
        form.findElement(By.xpath(".//input[@name='email']")).sendKeys(login);
        form.findElement(By.xpath(".//input[@name='password']")).sendKeys(password);
        form.findElement(By.xpath(".//button[@type='submit']")).submit();

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
    public void headlessTest()   {
        options.addArguments("--headless");
        options.addArguments("--start-maximized");
        driver = new ChromeDriver(options);
        driver.get("https://duckduckgo.com");
        WebDriverWait wait = new WebDriverWait(driver, Duration.ofSeconds(10));
        wait.until(ExpectedConditions.visibilityOfElementLocated(By.id("search_form_input_homepage")));
        clearAndEnter(By.id("search_form_input_homepage"), "ОТУС");
        driver.findElement(By.id("search_button_homepage")).submit();
        String element = driver.findElement(By.xpath("//article[@id='r1-0']/div[2]/h2/a/span")).getText();
        assertEquals("Онлайн‑курсы для профессионалов, дистанционное обучение современным ...", element);

    }

     @Test
     public void kioskTest()  {
         driver = new ChromeDriver(options);
         driver.get("http://kartushin.space/lesson/");


         By imageElementSecond = By.xpath("//div[@class='pp_hoverContainer']");
         JavascriptExecutor js = (JavascriptExecutor)driver;
         WebElement imageElement = driver.findElement(By.xpath("//img[@src='index_files/p1.jpg']"));
         js.executeScript("arguments[0].click()", imageElement);


         WebDriverWait wait = new WebDriverWait(driver, Duration.ofSeconds(5));
         wait.until(ExpectedConditions.visibilityOf(driver.findElement(imageElementSecond)));


         Assertions.assertTrue(driver.findElement(imageElementSecond).isDisplayed());
         logger.info("Is element visible on webpage: " + imageElement.isDisplayed());
    }

    @Test
    public void otusTest()  {

        convertUrl();
        driver = new ChromeDriver(options);
        driver.get(url);
        authUser();
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

     private void convertUrl() {

        if (url.trim().endsWith("/")) {
            url= substring(url,0,url.length()-1);
        }
        url=url.toLowerCase();

    }
    private void clearAndEnter(By element, String text) {
        driver.findElement(element).clear();
        driver.findElement(element).sendKeys(text);
    }

}