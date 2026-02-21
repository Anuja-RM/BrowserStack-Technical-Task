# El País Opinion Scraper – Selenium + BrowserStack

## Project Overview

This project automates web scraping and cross-browser testing using Selenium and BrowserStack.

The automation script performs the following:

- Opens the El País website in Spanish.
- Navigates to the **Opinion** section.
- Extracts the first five articles.
- Prints the title and content of each article in Spanish.
- Downloads the cover image of each article (if available).
- Translates each article title to English using a translation API.
- Analyzes translated titles to identify words repeated more than twice.
- Executes tests locally and on BrowserStack across multiple desktop and mobile browsers in parallel.

---

## Tech Stack

- Java
- Selenium WebDriver
- TestNG
- Maven
- BrowserStack Automate

---

## How to Run

### 1. Clone the Repository

```bash
git clone <your-repo-url>
cd assignment
```

### 2. Add BrowserStack Credentials

Create the file:

```
src/test/resources/application.properties
```

Create your BrowserStack account and Add your credentials:
```
browserstack.username=YOUR_USERNAME
browserstack.accessKey=YOUR_ACCESS_KEY
```

---

### 3. Run Test Locally

Open `BrowserStackTest.java` and run the test directly from your IDE.

---

### 4. Run Cross-Browser Tests on BrowserStack

Run the `testng.xml` file to execute parallel tests across multiple browsers and devices.

---

### 5. View Execution Results

Log in to the BrowserStack Automate dashboard to view session recordings, logs, screenshots, and execution details.