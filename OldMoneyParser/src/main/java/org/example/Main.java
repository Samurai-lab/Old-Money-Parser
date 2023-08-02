package org.example;

import okhttp3.Callback;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.Response;
import org.jetbrains.annotations.NotNull;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;
import org.openqa.selenium.By;
import org.openqa.selenium.Keys;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.chrome.ChromeDriver;
import org.openqa.selenium.chrome.ChromeOptions;
import org.openqa.selenium.devtools.v85.io.IO;
import org.openqa.selenium.safari.SafariDriver;
import org.openqa.selenium.safari.SafariOptions;

import java.io.FileWriter;
import java.io.IOException;
import java.util.Scanner;


public class Main {
    public static void main(String[] args) throws InterruptedException {

        for (int i = 0; i < 10; i++) {
            Scanner scanner = new Scanner(System.in);
            System.out.println("Введите название необходимой монеты: ");
            String searchText = scanner.nextLine();

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

            // Отправка формы поиска


            Thread.sleep(1000);

            searchInput = driver.findElement(By.className("value"));

            searchInput.sendKeys(Keys.ENTER);

            getTextFromHtml(driver, searchText);
        }


    }

    private static void getTextFromHtml(WebDriver driver, String searchText) {
        String textFromSite = "";
        OkHttpClient client = new OkHttpClient();
        // URL сайта и текст, который нужно найти
        String url = driver.getCurrentUrl();
        // Создаем запрос
        Request request = new Request.Builder()
                .url(url)
                .build();

        // Закрытие браузера
        driver.quit();

        // Выполняем запрос асинхронно
        client.newCall(request).enqueue(new Callback() {

            @Override
            public void onFailure(@NotNull okhttp3.Call call, @NotNull IOException e) {
                e.printStackTrace();
            }

            @Override
            public void onResponse(@NotNull okhttp3.Call call, @NotNull Response response) throws IOException {
                if (response.isSuccessful()) {
                    String htmlContent = response.body().string();
                    updateFile("hello", searchText
                            + "\n\n" + parsTableInfo(htmlContent, 1)
                            + "\n" + parsTablePrice(htmlContent, 4));

                } else {
                    System.out.println("Ошибка при выполнении запроса: " + response.code());
                }
            }
        });
    }

    private static void updateFile(String fileName, String text) {
        try (FileWriter writer = new FileWriter(fileName + ".txt", false)) {
            // запись всей строки
            writer.write(text);
            // запись по символам
            writer.append('\n');
            /*            writer.append('E');*/
            writer.flush();
        } catch (IOException ex) {

            System.out.println(ex.getMessage());
        }
    }

    private static String parsTableInfo(String htmlContent, Integer tableNumber) {
        String tableInfo = "";
        Document doc = Jsoup.parse(htmlContent);
        Element table = doc.select("table").get(tableNumber); // получить первую таблицу
        Elements rows = table.select("tr"); // получить все строки
        for (Element row : rows) {
            Elements columnsOne = row.select("th"); // получить все столбцы
            Elements columnsTwo = row.select("td"); // получить все столбцы
            Element valueElementOne = columnsOne.get(0);// получить первый столбец
            Element valueElementTwo = columnsTwo.get(0);// получить первый столбец
            String value = valueElementOne.text() + ": " + valueElementTwo.text(); // получить текст значения
            tableInfo += value + "\n";
        }
        return tableInfo;
    }

    private static String parsTablePrice(String htmlContent, Integer tableNumber) {
        String tableInfo = "";
        String value = "";
        Document doc = Jsoup.parse(htmlContent);
        Element table = doc.select("table").get(tableNumber); // получить первую таблицу
        Elements rows = table.select("tr"); // получить все строки
        for (Element row : rows) {
            Elements columnsOne = row.select("th"); // получить все столбцы
            Elements columnsTwo = row.select("td"); // получить все столбцы
            if (columnsTwo.size() > 0) {
                for (int i = 0; i < columnsTwo.size(); i++) {
                    Element valueElementTwo = columnsTwo.get(i);// получить первый столбец
                    value += " " + valueElementTwo.text();
                }
            } else if (columnsOne.size() > 0) {
                for (int i = 0; i < columnsOne.size(); i++) {
                    Element valueElementOne = columnsOne.get(i);
                    value += " " + valueElementOne.text();
                }
            }
            tableInfo += value + "\n";
            value = "";
        }
        return tableInfo;
    }
}