import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.chrome.ChromeDriver;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.WebDriverWait;
import org.testng.annotations.Test;
import java.io.BufferedReader;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.net.URLEncoder;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.nio.file.StandardCopyOption;
import java.time.Duration;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;


public class BrowserStackTest extends BaseTest {

    @Test
    public void openElPaisAndHandleCookies() throws Exception{

        WebDriverWait wait;

        driver.manage().window().maximize();

        wait = new WebDriverWait(driver, Duration.ofSeconds(15));

        driver.get("https://elpais.com/");

        try {
            WebElement acceptButton = wait.until(
                    ExpectedConditions.elementToBeClickable(
                            By.xpath("//button[contains(.,'Aceptar') or contains(.,'Accept')]")
                    )
            );
            acceptButton.click();
            System.out.println("Cookie popup accepted.");
        } catch (Exception e) {
            System.out.println("No cookie popup displayed.");
        }

        List<WebElement> opinionLinks = wait.until(ExpectedConditions.presenceOfAllElementsLocatedBy(
                By.xpath("//a[contains(@href,'opinion')]")
        ));

        boolean clicked = false;
        for (WebElement link : opinionLinks) {
            try {
                link.click();
                clicked = true;
                break;
            } catch (Exception e) {
                // ignore and try next
            }
        }

        if (!clicked) {
            throw new RuntimeException("Unable to click on opinion link");
        }

        List<WebElement> articles = driver.findElements(By.cssSelector("h2.c_t a"));

        List<String> articleUrls = new ArrayList<>();

        for (int i = 0; i < 5 && i < articles.size(); i++) {
            try {
                articleUrls.add(articles.get(i).getAttribute("href"));
            } catch (org.openqa.selenium.StaleElementReferenceException e) {
                WebElement refreshed = driver.findElements(By.cssSelector("h2.c_t a")).get(i);
                articleUrls.add(refreshed.getAttribute("href"));
            }
        }

        List<String> translatedTitles = new ArrayList<>();

        for (String articleUrl : articleUrls) {

            driver.navigate().to(articleUrl);

            wait.until(ExpectedConditions.presenceOfElementLocated(By.tagName("h1")));

            String title = wait.until(
                    ExpectedConditions.visibilityOfElementLocated(
                            By.tagName("h1")
                    )
            ).getText();

            System.out.println("Title: " + title);
            String content = driver.findElement(By.tagName("article")).getText();

            System.out.println("SPANISH TITLE: " + title);

            try {
                WebElement imageElement = driver.findElement(By.cssSelector("figure img"));
                String imageUrl = imageElement.getAttribute("src");

                InputStream in = new URL(imageUrl).openStream();
                Files.copy(in,
                        Paths.get("images/" + title.replaceAll("[^a-zA-Z0-9]", "") + ".jpg"),
                        StandardCopyOption.REPLACE_EXISTING);

                System.out.println("Image downloaded.");
            } catch (Exception e) {
                System.out.println("No image found.");
            }

            String translated = translateToEnglish(title);
            System.out.println("Translated Title: " + translated);
            translatedTitles.add(translated);

            driver.get("https://elpais.com/opinion/");
                    wait.until(ExpectedConditions.presenceOfElementLocated(By.cssSelector("article")));
        }

        Map<String, Integer> wordCount = new HashMap<>();

        for (String header : translatedTitles) {

            String[] words = header.toLowerCase().split("\\W+");

            for (String word : words) {
                if (word.length() > 2) {
                    wordCount.put(word,
                            wordCount.getOrDefault(word, 0) + 1);
                }
            }
        }

        System.out.println("Repeated words (>2 times):");

        for (Map.Entry<String, Integer> entry : wordCount.entrySet()) {
            if (entry.getValue() > 2) {
                System.out.println(entry.getKey() + " -> " + entry.getValue());
            }
        }

        driver.quit();
    }

    public static String translateToEnglish(String text) throws Exception {

        String urlStr = "https://translate.googleapis.com/translate_a/single?client=gtx&sl=es&tl=en&dt=t&q="
                + URLEncoder.encode(text, "UTF-8");

        URL url = new URL(urlStr);
        HttpURLConnection conn = (HttpURLConnection) url.openConnection();
        conn.setRequestMethod("GET");

        BufferedReader br = new BufferedReader(
                new InputStreamReader(conn.getInputStream()));

        String response = br.readLine();

        String translated = response.split("\"")[1];

        return translated;
    }
}