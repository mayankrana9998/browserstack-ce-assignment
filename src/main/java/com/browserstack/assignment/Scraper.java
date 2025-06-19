package com.browserstack.assignment;

import org.openqa.selenium.*;

import java.io.FileOutputStream;
import java.io.InputStream;
import java.net.URL;
import java.util.ArrayList;
import java.util.List;

public class Scraper {

    public static void main(String[] args) {
        WebDriver driver = WebDriverFactory.createLocalDriver();
        run(driver, 0);
    }

    public static void run(WebDriver driver, int threadNum) {
        List<String> translatedTitles = new ArrayList<>();
        StringBuilder output = new StringBuilder();

        try {
            driver.get("https://elpais.com/opinion/");
            Thread.sleep(1000);

            String langAttr = driver.findElement(By.tagName("html")).getAttribute("lang");
            if (!"es-ES".equalsIgnoreCase(langAttr)) {
                output.append("❌ Website is not in Spanish. Exiting...\n");
                System.out.print(output.toString());
                return;
            }

            List<String> uniqueLinks = new ArrayList<>();
            for (int i = 1; i <= 5; i++) {
                try {
                    WebElement link = driver.findElement(By.xpath("(//article/header/h2/a[contains(@href,'https://elpais.com/opinion/')])[" + i + "]"));
                    String href = link.getAttribute("href");
                    if (href != null && href.startsWith("https://elpais.com/opinion/")) {
                        uniqueLinks.add(href);
                    }
                } catch (NoSuchElementException e) {
                    break;
                }
            }

            output.append("🧵 Thread ").append(threadNum).append(": Found ").append(uniqueLinks.size()).append(" articles.\n\n");

            int index = 1;
            for (String url : uniqueLinks) {
                driver.get(url);
                String title = "";
                String content = "";
                String imageUrl = null;

                try {
                    WebElement article = driver.findElement(By.xpath("//article[@id='main-content']"));
                    WebElement articleContent = article.findElement(By.xpath("//div/h2[@class='a_st']"));
                    ((JavascriptExecutor) driver).executeScript("arguments[0].scrollIntoView(true);", articleContent);
                    WebElement titleElement = driver.findElement(By.xpath("//article[@id='main-content']//h1"));
                    ((JavascriptExecutor) driver).executeScript("arguments[0].scrollIntoView(true);", titleElement);
                    title = titleElement.getText();
                    content = articleContent.getText();

                    try {
                        WebElement img = article.findElement(By.xpath(".//header//div/figure/span/img"));
                        String src = img.getAttribute("src");
                        if (src != null && src.startsWith("https://imagenes.elpais.com/")) {
                            imageUrl = src;
                        }
                    } catch (NoSuchElementException e) {
                        // no image found
                    }

                    if (imageUrl != null) {
                        downloadImage(imageUrl, "cover_image_" + threadNum + "_" + index + ".jpg");
                        output.append("📸 Image downloaded: cover_image_").append(threadNum).append("_").append(index).append(".jpg\n");
                    } else {
                        output.append("⚠️ No cover image found.\n");
                    }

                } catch (NoSuchElementException e) {
                    output.append("⚠️ Skipping non-article page: ").append(url).append("\n");
                    continue;
                }

                String translated = RapidTranslator.translateToEnglish(title);
                translatedTitles.add(translated);

                output.append("🔹 Original Title: ").append(title).append("\n");
                output.append("📄 Content (first 300 chars): ")
                        .append(content.substring(0, Math.min(content.length(), 300))).append("...\n");
                output.append("🌍 Translated Title: ").append(translated).append("\n");
                output.append("------------------------------------------------------\n\n");

                index++;
            }

            // Collect Analyzer output
            StringBuilder analyzerOutput = new StringBuilder();
            Analyzer.analyzeRepeatedWordsToBuilder(translatedTitles, analyzerOutput);
            output.append(analyzerOutput);

        } catch (Exception e) {
            output.append("❌ Exception: ").append(e.getMessage()).append("\n");
        }

        System.out.print(output.toString());
    }

    public static void downloadImage(String imageUrl, String fileName) {
        try (InputStream in = new URL(imageUrl).openStream();
             FileOutputStream out = new FileOutputStream(fileName)) {
            byte[] buffer = new byte[2048];
            int length;
            while ((length = in.read(buffer)) != -1) {
                out.write(buffer, 0, length);
            }
        } catch (Exception e) {
            // Image download errors are not included in thread output
            System.out.println("❌ Failed to download image: " + imageUrl);
        }
    }
}