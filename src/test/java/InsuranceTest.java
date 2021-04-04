import org.junit.After;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;
import org.openqa.selenium.By;
import org.openqa.selenium.JavascriptExecutor;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.chrome.ChromeDriver;
import org.openqa.selenium.firefox.FirefoxDriver;
import org.openqa.selenium.safari.SafariDriver;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.Wait;
import org.openqa.selenium.support.ui.WebDriverWait;
import java.util.concurrent.TimeUnit;

public class InsuranceTest {

    WebDriver driver;
    String baseUrl;

    @Before
    public void beforeTest() {
        baseUrl = "http://www.sberbank.ru/ru/person";
        driver = new FirefoxDriver();
        driver.manage().timeouts().implicitlyWait(30, TimeUnit.SECONDS);
//        driver.manage().window().maximize();
        driver.manage().window().fullscreen();
        driver.get(baseUrl);
    }

    @Test
    public void testInsurance() throws InterruptedException {
        // Нажимаем на Страхование
        driver.findElement(By.xpath("//*[@aria-label = 'Страхование']")).click();

        //Нажать на "Перейти в каталог"
        driver.findElement(By.xpath("//*[@data-cga_click_top_menu = 'Страхование_Перейти в каталог_type_important']")).click();

        Wait<WebDriver> wait = new WebDriverWait(driver, 5, 1000);

        // Нажимаем на "Страхование для путешественников"
        driver.findElement(By.xpath("//*[@class = 'kitt-cookie-warning__close']")).click();  // закрываем окно с куками

        JavascriptExecutor scroll = (JavascriptExecutor)driver;
        scroll.executeScript("window.scrollBy(0,500)", "");                      // скроллим вниз

        driver.findElement(By.xpath("//*[text() = 'Страхование для путешественников']")).click();

        scroll.executeScript("window.scrollBy(0,-500)", "");                     // скроллим вверх

        //Проверяем наличие на странице заголовка – Страхование путешественников
        WebElement insuranceText = driver.findElement(By
                .xpath("//*[@class = 'kitt-heading  page-teaser-dict__header kitt-heading_size_l']"));
        Assert.assertEquals("Страхование путешественников", insuranceText.getText());

        // Жмем на кнопку
        driver.findElement(By.xpath("//*[@class = 'kitt-button__text']")).click();

        // Выбираем минимальный тариф
        driver.findElement(By.xpath("//*[@class = 'online-card-program selected']")).click();

        scroll.executeScript("window.scrollBy(0,250)", "");
        driver.findElement(By.xpath("//*[@class = 'col-xl-4 col-md-6 col-12']")).click();

        // Нажимаем на кнопку "Оформить"
        scroll.executeScript("window.scrollBy(0, 1000)", "");
        driver.findElement(By.xpath("//*[@class = 'btn btn-primary btn-large']")).click();  // иногда не срабатывает c ChromeDriver? В Firefox - ок

        // Вводим данные
        /*
•       Данные страхователя: Фамилия, Имя, Отчество, Дата рождения, Пол
•       Паспортные данные
         */
        WebElement field = driver.findElement(By.id("surname_vzr_ins_0"));
        wait.until(ExpectedConditions.visibilityOf(field));
        fillField(By.id("surname_vzr_ins_0"), "Иванов");
        fillField(By.id("name_vzr_ins_0"), "Иван");
        fillField(By.id("birthDate_vzr_ins_0"), "20.10.1999");

        Thread.sleep(3000);
        fillField(By.id("person_lastName"), "Страховщиков");
        fillField(By.id("person_firstName"), "Страховщик");
        fillField(By.id("person_middleName"), "Страховщикович");
        fillField(By.id("person_birthDate"), "10.06.1979");

        Thread.sleep(3000);
        driver.findElement(By.xpath("//*[text() = 'Женский']")).click();
        fillField(By.id("passportSeries"), "4545");
        fillField(By.id("passportNumber"), "200000");
        fillField(By.id("documentDate"), "10.06.2019");
        fillField(By.id("documentIssue"), "ТП УФМС AUTOTEST123");



        // Проверяем заполнение полей
        Assert.assertEquals("Иванов", driver.findElement(By.id("surname_vzr_ins_0")).getAttribute("value"));
        Assert.assertEquals("Иван", driver.findElement(By.id("name_vzr_ins_0")).getAttribute("value"));
        Assert.assertEquals("20.10.1999", driver.findElement(By.id("birthDate_vzr_ins_0")).getAttribute("value"));
        Assert.assertEquals("Страховщиков", driver.findElement(By.id("person_lastName")).getAttribute("value"));
        Assert.assertEquals("Страховщик", driver.findElement(By.id("person_firstName")).getAttribute("value"));
        Assert.assertEquals("Страховщикович", driver.findElement(By.id("person_middleName")).getAttribute("value"));
        Assert.assertEquals("10.06.1979", driver.findElement(By.id("person_birthDate")).getAttribute("value"));
        Assert.assertEquals("4545", driver.findElement(By.id("passportSeries")).getAttribute("value"));
        Assert.assertEquals("200000", driver.findElement(By.id("passportNumber")).getAttribute("value"));
        Assert.assertEquals("10.06.2019", driver.findElement(By.id("documentDate")).getAttribute("value"));
        Assert.assertEquals("ТП УФМС AUTOTEST123", driver.findElement(By.id("documentIssue")).getAttribute("value"));

        // Жмем "Продолжить"
        scroll.executeScript("window.scrollBy(0,1550)", "");
        driver.findElement(By.xpath("//*[@class = 'btn btn-primary page__btn']")).click();

        // Проверяем появление ошибок заполнения
        scroll.executeScript("window.scrollBy(0,1550)", "");
        WebElement errorField = driver.findElement(By.xpath("//*[@class = 'alert-form alert-form-error']"));
        wait.until(ExpectedConditions.visibilityOf(errorField));
        Assert.assertEquals("При заполнении данных произошла ошибка",
                driver.findElement(By.xpath("//*[@class = 'alert-form alert-form-error']")).getText());
        Assert.assertEquals("Поле не заполнено.",
                driver.findElement(By.xpath("//*[text()='Поле не заполнено.']/..//*[@class = 'invalid-validate form-control__message']")).getText()); // не смог найти оригинальный xpath


    }

    public void fillField(By locator, String value){
        driver.findElement(locator).clear();
        driver.findElement(locator).sendKeys(value);
    }

    @After
    public void afterTest()  throws InterruptedException {
        Thread.sleep(3000);
        driver.quit();
    }
}
