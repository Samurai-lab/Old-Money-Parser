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
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.chrome.ChromeDriver;
import org.openqa.selenium.chrome.ChromeOptions;

import java.io.IOException;

import static org.example.FileFun.updateFile;

public class HtmlFun {

    protected static void getTextFromHtml(WebDriver driver, String searchText, Boolean rewriteFile) {
        OkHttpClient client = new OkHttpClient();

        String url = driver.getCurrentUrl();
        WebDriver newDriver;

        ChromeOptions options = new ChromeOptions();
        options.addArguments("--headless");
        /*options.setProxy(proxy);*/
        newDriver = new ChromeDriver(options);

        // Открытие сайта
        newDriver.get(driver.getCurrentUrl());
        String webElementText = "";
        try {
            WebElement webElement = newDriver.findElement(By.id("swap-block"));
            webElementText += webElement.getText();
        } catch (Exception e) {
            System.out.println("Table with price is not found");
        }

        
        Request request = new Request.Builder()
                .url(url)
                .build();

        driver.quit();

        String finalWebElementText = webElementText;

        client.newCall(request).enqueue(new Callback() {
            @Override
            public void onFailure(@NotNull okhttp3.Call call, @NotNull IOException e) {
                e.printStackTrace();
            }

            @Override
            public void onResponse(@NotNull okhttp3.Call call, @NotNull Response response) throws IOException {
                if (response.isSuccessful()) {
                    String htmlContent = response.body().string();
                    updateFile("all information", searchText
                            + "\n\n" + parsTableInfo(htmlContent, 1)
                            + "\n" + parsTablePrice(htmlContent, 4)
                            + "\n\n" + finalWebElementText, rewriteFile);
                    try {
                        updateFile("all information", "\n" + parsTablePrice(htmlContent, 5), true);
                    } finally {
                    }

                } else {
                    System.out.println("Ошибка при выполнении запроса: " + response.code());
                }
            }
        });
        newDriver.quit();
    }

    private static String parsTableInfo(String htmlContent, Integer tableNumber) {
        String tableInfo = "";
        Document doc = Jsoup.parse(htmlContent);
        Element table = doc.select("table").get(tableNumber);
        Elements rows = table.select("tr");
        for (Element row : rows) {
            Elements columnsOne = row.select("th");
            Elements columnsTwo = row.select("td");
            Element valueElementOne = columnsOne.get(0);
            Element valueElementTwo = columnsTwo.get(0);
            String value = valueElementOne.text() + ": " + valueElementTwo.text();
            tableInfo += value + "\n";
        }
        return tableInfo;
    }

    private static String parsTablePrice(String htmlContent, Integer tableNumber) {
        String tableInfo = "";
        String value = "";
        Document doc = Jsoup.parse(htmlContent);
        Element table = doc.select("table").get(tableNumber);
        Elements rows = table.select("tr");
        for (Element row : rows) {
            Elements columnsOne = row.select("th");
            Elements columnsTwo = row.select("td");
            if (columnsTwo.size() > 0) {
                for (int i = 0; i < columnsTwo.size(); i++) {
                    Element valueElementTwo = columnsTwo.get(i);
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
