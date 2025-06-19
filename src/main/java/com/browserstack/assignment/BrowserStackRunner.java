package com.browserstack.assignment;

import org.openqa.selenium.WebDriver;
import org.openqa.selenium.MutableCapabilities;
import org.openqa.selenium.remote.RemoteWebDriver;

import java.net.URL;
import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

public class BrowserStackRunner {

    static String USERNAME = "mayankrana_4sPDEb";
    static String ACCESS_KEY = "ABpxyAyyNTYaUYZboP9G";

    public static void main(String[] args) {

        ExecutorService executor = Executors.newFixedThreadPool(5);

        MutableCapabilities[] caps = new MutableCapabilities[5];

        // ✅ 1. Chrome on Windows 11
        caps[0] = createDesktopCapabilities("Chrome", "latest", "Windows", "11", "Thread 1");

        // ✅ 2. Safari on macOS
        caps[1] = createDesktopCapabilities("Safari", "latest", "OS X", "Monterey", "Thread 2");

        // ✅ 3. Edge on Windows 10
        caps[2] = createDesktopCapabilities("Edge", "latest", "Windows", "10", "Thread 3");

        // ✅ 4. iPhone 14
        caps[3] = createMobileCapabilities("iPhone", "iPhone 14", "16", "Thread 4");

        // ✅ 5. Samsung Galaxy S22
        caps[4] = createMobileCapabilities("Android", "Samsung Galaxy S22", "12.0", "Thread 5");

        for (int i = 0; i < 5; i++) {
            int index = i;
            executor.execute(() -> {
                try {
                    runScraperOnBrowserStack(caps[index], index + 1);
                } catch (Exception e) {
                    e.printStackTrace();
                }
            });
        }

        executor.shutdown();
    }

    private static MutableCapabilities createDesktopCapabilities(String browserName, String browserVersion, String os, String osVersion, String threadName) {
        MutableCapabilities options = new MutableCapabilities();
        options.setCapability("browserName", browserName);
        options.setCapability("browserVersion", browserVersion);

        Map<String, Object> bstackOptions = new HashMap<>();
        bstackOptions.put("os", os);
        bstackOptions.put("osVersion", osVersion);
        bstackOptions.put("sessionName", threadName + " - El Pais Scraper");
        bstackOptions.put("buildName", "BrowserStack CE Assignment Submission");
        bstackOptions.put("idleTimeout", 180);

        options.setCapability("bstack:options", bstackOptions);
        return options;
    }

    private static MutableCapabilities createMobileCapabilities(String browserName, String deviceName, String osVersion, String threadName) {
        MutableCapabilities options = new MutableCapabilities();
        options.setCapability("browserName", browserName);

        Map<String, Object> bstackOptions = new HashMap<>();
        bstackOptions.put("deviceName", deviceName);
        bstackOptions.put("osVersion", osVersion);
        bstackOptions.put("realMobile", true);
        bstackOptions.put("sessionName", threadName + " - El Pais Scraper");
        bstackOptions.put("buildName", "BrowserStack CE Assignment Submission");
        bstackOptions.put("idleTimeout", 180);

        options.setCapability("bstack:options", bstackOptions);
        return options;
    }

    public static void runScraperOnBrowserStack(MutableCapabilities caps, int threadNum) throws Exception {
        WebDriver driver = new RemoteWebDriver(
                new URL("https://" + USERNAME + ":" + ACCESS_KEY + "@hub-cloud.browserstack.com/wd/hub"),
                caps
        );

        try {
            Scraper.run(driver, threadNum);  // ✅ Executes the full scraper
        } finally {
            driver.quit();
        }
    }
}