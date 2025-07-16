# BrowserStack Assignment

This repository contains my technical assignment for the **BrowserStack**.

The project demonstrates:

- ✅ Web scraping using **Selenium**
- ✅ Translation using a **third-party Translation API (RapidAPI / Google Translate)**
- ✅ Text analysis (frequency of words in translated article titles)
- ✅ Execution across **5 parallel threads** using **BrowserStack** for cross-browser testing

---

## 🚀 Features

- Scrapes the first 5 articles from [El País – Opinion](https://elpais.com/opinion/)
- Ensures the page is in Spanish
- Extracts:
  - Article title (in Spanish)
  - Full content
  - Cover image (if available)
- Translates article titles to English via API
- Analyzes repeated words in the translated titles
- Executes scraping in parallel across 5 browsers/devices using BrowserStack

---

## 🛠 Tech Stack

- Java
- Selenium WebDriver
- Maven
- BrowserStack Automate
- RapidAPI Translator
- Multi-threading (ExecutorService)

---

## 📁 Project Structure

com.browserstack.assignment/
├── Scraper.java # Core logic for article scraping
├── BrowserStackRunner.java # Launches parallel runs on BrowserStack
├── SpanishLanguageChecker.java# Verifies page language
├── RapidTranslator.java # Translates text to English
├── Analyzer.java # Finds repeated words in headlines
├── WebDriverFactory.java # Manages WebDriver instances


---

## 🧪 Sample Output

🔹 Original Title: El apagón explicado a medias
🌍 Translated Title: The blackout explained in half
📄 Content: ...
📸 Image downloaded: cover_image_2_1.jpg
🔁 Repeated Words:

the: 3 times

to: 2 times

## 👤 Author

**Mayank Rana**  
📍 Noida, India  
🔗 [LinkedIn](https://www.linkedin.com/in/mayank-rana-5a5628310/) 
✉️ mayankrana720@gmail.com

