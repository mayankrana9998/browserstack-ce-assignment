package com.browserstack.assignment;

import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.WebDriverWait;

import java.time.Duration;
import java.util.Arrays;
import java.util.List;

public class SpanishLanguageChecker {

    public static void main(String[] args) {
        WebDriver driver = WebDriverFactory.createLocalDriver();

        try {
            driver.get("https://elpais.com/");
            WebDriverWait wait = new WebDriverWait(driver, Duration.ofSeconds(5));
            wait.until(ExpectedConditions.visibilityOfElementLocated(By.tagName("body")));

            String langAttr = driver.findElement(By.tagName("html")).getAttribute("lang");

            List<String> spanishKeywords = Arrays.asList(
                    "Última hora", "Internacional", "Economía", "Sociedad",
                    "Cultura", "Deportes", "Opinión", "América", "Suscríbete", "Edición España", "El País Exprés"
            );

            boolean foundSpanishKeyword = false;
            String pageSource = driver.getPageSource();

            for (String keyword : spanishKeywords) {
                if (pageSource.contains(keyword)) {
                    foundSpanishKeyword = true;
                    break;
                }
            }

            if ((langAttr != null && (langAttr.equalsIgnoreCase("es") || langAttr.equalsIgnoreCase("es-ES")))
                    && foundSpanishKeyword) {
                System.out.println("✅ The website is displayed in Spanish.");
            } else {
                System.out.println("❌ The website is NOT in Spanish.");
                if (langAttr == null) {
                    System.out.println("   Reason: 'lang' attribute not found.");
                } else {
                    System.out.println("   Reason: 'lang' attribute = '" + langAttr + "'");
                }
                if (!foundSpanishKeyword) {
                    System.out.println("   Reason: None of the Spanish keywords were found.");
                    System.out.println("   Checked keywords: " + spanishKeywords);
                }
            }

        } catch (Exception e) {
            System.err.println("An unexpected error occurred: " + e.getMessage());
            e.printStackTrace();
        } finally {
            if (driver != null) {
                driver.quit();
            }
        }
    }
}
