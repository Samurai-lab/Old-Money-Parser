package org.example;

import org.openqa.selenium.*;
import org.openqa.selenium.chrome.ChromeDriver;
import org.openqa.selenium.chrome.ChromeOptions;
import org.openqa.selenium.remote.CapabilityType;
import org.openqa.selenium.remote.DesiredCapabilities;

import java.net.InetSocketAddress;
import java.security.DrbgParameters;
import java.util.ArrayList;


import static org.example.HtmlFun.getTextFromHtml;


public class Main {
    public static void main(String[] args) throws InterruptedException {

        Boolean rewriteFile = false;
        FileFun fileFun = new FileFun();

        ArrayList<String> arrayListOfWords = fileFun.readFileInfo("money");
        IPAddressesPool pool = new IPAddressesPool();
        String nextIPAddress;
        for (int i = 0; i < arrayListOfWords.size(); i++) {
            if (i != 0) rewriteFile = true;
            String searchText = arrayListOfWords.get(i);

            WebDriver driver;

            Proxy proxy = new Proxy();

            DesiredCapabilities dc = new DesiredCapabilities();

            nextIPAddress = pool.getNextIPAddress();

            proxy.setHttpProxy(nextIPAddress);
            proxy.setSslProxy(nextIPAddress);

            dc.setCapability(CapabilityType.PROXY, proxy);

            ChromeOptions options = new ChromeOptions();
            options.addArguments("--headless");
            /*options.setProxy(proxy);*/
            driver = new ChromeDriver(options);

            driver.get("https://ru.ucoin.net/catalog");

            WebElement searchInput = driver.findElement(By.id("search"));

            searchInput.sendKeys(searchText);
            searchInput.sendKeys(Keys.ENTER);

            searchInput = driver.findElement(By.className("value"));

            searchInput.sendKeys(Keys.ENTER);

            getTextFromHtml(driver, searchText, rewriteFile);
        }
    }
}