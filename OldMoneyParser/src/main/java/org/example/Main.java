package org.example;

import org.openqa.selenium.By;
import org.openqa.selenium.Keys;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.chrome.ChromeDriver;
import org.openqa.selenium.chrome.ChromeOptions;

import java.util.ArrayList;

import static org.example.HtmlFun.getTextFromHtml;


public class Main {
    public static void main(String[] args) throws InterruptedException {

        Boolean rewriteFile = false;
        FileFun fileFun = new FileFun();

        ArrayList<String> arrayListOfWords = fileFun.readFileInfo("money");

        for (int i = 0; i < arrayListOfWords.size(); i++) {
            if (i != 0) rewriteFile = true;
            String searchText = arrayListOfWords.get(i);

            // Создание экземпляра WebDriver
            WebDriver driver;

            ChromeOptions options = new ChromeOptions();
            options.addArguments("--headless");
            driver = new ChromeDriver(options);

            // Открытие сайта
            driver.get("https://ru.ucoin.net/catalog");

            // Нахождение поисковой строки
            WebElement searchInput = driver.findElement(By.id("search"));

            // Ввод данных в поисковую строку
            searchInput.sendKeys(searchText);
            searchInput.sendKeys(Keys.ENTER);

            searchInput = driver.findElement(By.className("value"));

            searchInput.sendKeys(Keys.ENTER);

            getTextFromHtml(driver, searchText, rewriteFile);
        }
    }
}