package com.openway;

import io.github.cdimascio.dotenv.Dotenv;
import org.openqa.selenium.By;
import org.openqa.selenium.JavascriptExecutor;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.chrome.ChromeDriver;
import org.openqa.selenium.interactions.Actions;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.WebDriverWait;
import org.testng.annotations.AfterMethod;
import org.testng.annotations.BeforeMethod;
import org.testng.annotations.Test;

import java.time.Duration;
import java.util.List;
import java.util.Random;

public class PeriplusTest {

    WebDriver driver;
    WebDriverWait wait;
    JavascriptExecutor js;
    Dotenv dotenv;

    By signInMenuLoc = By.cssSelector("#nav-signin-text a");
    By emailInputLoc = By.name("email");
    By passwordInputLoc = By.name("password");
    By btnLoginLoc   = By.id("button-login");
    By searchBoxLoc  = By.id("filter_name_desktop");
    By cartBadgeLoc  = By.id("cart_total");
    By productGridLoc = By.cssSelector(".row-category-grid .col-xl-3");
    By preloaderLoc  = By.className("preloader");

    @BeforeMethod
    public void setup() {
        dotenv = Dotenv.load(); 
        driver = new ChromeDriver();
        driver.manage().window().maximize();
        wait = new WebDriverWait(driver, Duration.ofSeconds(10));
        js = (JavascriptExecutor) driver;
    }

    private void login(String email, String password) {
        wait.until(ExpectedConditions.elementToBeClickable(signInMenuLoc)).click();
        wait.until(ExpectedConditions.visibilityOfElementLocated(emailInputLoc)).sendKeys(email);
        driver.findElement(passwordInputLoc).sendKeys(password);
        driver.findElement(btnLoginLoc).click();
    }

    private void searchProduct(String keyword) {
        WebElement searchBox = wait.until(ExpectedConditions.visibilityOfElementLocated(searchBoxLoc));
        searchBox.sendKeys(keyword);
        searchBox.submit();
        wait.until(ExpectedConditions.invisibilityOfElementLocated(preloaderLoc));
    }

    private int getCurrentCartCount() {
        try {
            WebElement cartBadge = driver.findElement(cartBadgeLoc);
            String text = cartBadge.getText().replaceAll("[^0-9]", "");
            return text.isEmpty() ? 0 : Integer.parseInt(text);
        } catch (Exception e) {
            return 0;
        }
    }

    @Test
    public void testAddMultipleProductToCart() {
        int itemsToAdd = 1;
        Random random = new Random();

        String userEmail = dotenv.get("PERIPLUS_EMAIL");
        String userPass  = dotenv.get("PERIPLUS_PASSWORD");
        String searchKey = dotenv.get("SEARCH_KEYWORD");

        driver.get("https://www.periplus.com/");

        login(userEmail, userPass);
        searchProduct(searchKey);

        int currentCartCount = getCurrentCartCount();
        System.out.println("Total Cart Item: " + currentCartCount);

        for (int i = 0; i < itemsToAdd; i++) {
            List<WebElement> products = wait.until(ExpectedConditions.presenceOfAllElementsLocatedBy(productGridLoc));
            int randomIndex = random.nextInt(products.size());
            WebElement product = products.get(randomIndex);

            try {
                js.executeScript("arguments[0].scrollIntoView({block: 'center'});", product);
                Thread.sleep(1000);

                Actions action = new Actions(driver);
                WebElement thumbnail = product.findElement(By.className("product-img"));
                action.moveToElement(thumbnail).build().perform();
                Thread.sleep(500);

                WebElement btnAddToCart = product.findElement(By.cssSelector("a.addtocart, i.ti-shopping-cart"));
                btnAddToCart.click();

                int expectedTarget = currentCartCount + 1;
                wait.until(ExpectedConditions.textToBePresentInElementLocated(cartBadgeLoc, String.valueOf(expectedTarget)));
                
                currentCartCount = expectedTarget;
                System.out.println("Successfully added item-" + (i + 1) + ". Total Cart Item: " + currentCartCount);

            } catch (Exception e) {
                System.out.println("Failed to add item " + randomIndex + ". Retrying...");
                i--;
                js.executeScript("window.scrollBy(0,-300)");
            }
        }

        driver.navigate().refresh();
        try { Thread.sleep(3000); } catch (Exception ignored) {}
        System.out.println("Test completed!");
    }

    @AfterMethod
    public void tearDown() {
        try { Thread.sleep(3000); } catch (Exception ignored) {}
        if (driver != null) {
            driver.quit();
        }
    }
}