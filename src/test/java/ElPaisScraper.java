import org.openqa.selenium.*;
import org.openqa.selenium.By;
import java.util.*;
import java.io.*;
import java.net.URL;

public class ElPaisScraper {

    WebDriver driver;

    public ElPaisScraper(WebDriver driver) {
        this.driver = driver;
    }

    public List<String> scrapeArticles() throws Exception {

        List<String> titles = new ArrayList<>();

        driver.get("https://elpais.com/");
        driver.findElement(By.linkText("Opinión")).click();

        Thread.sleep(3000);

        List<WebElement> articles = driver.findElements(By.cssSelector("article")).subList(0, 5);

        int count = 1;

        for (WebElement article : articles) {

            WebElement titleElement = article.findElement(By.cssSelector("h2"));
            String title = titleElement.getText();
            titles.add(title);

            System.out.println("Title: " + title);

            titleElement.click();
            Thread.sleep(2000);

            List<WebElement> paragraphs = driver.findElements(By.cssSelector("p"));
            for (WebElement p : paragraphs) {
                System.out.println(p.getText());
            }

            try {
                WebElement img = driver.findElement(By.cssSelector("figure img"));
                String imgUrl = img.getAttribute("src");
                downloadImage(imgUrl, "image_" + count + ".jpg");
            } catch (Exception ignored) {}

            driver.navigate().back();
            Thread.sleep(2000);

            count++;
        }

        return titles;
    }

    private void downloadImage(String imageUrl, String fileName) throws Exception {
        URL url = new URL(imageUrl);
        InputStream in = url.openStream();
        FileOutputStream fos = new FileOutputStream(fileName);
        byte[] buffer = new byte[2048];
        int length;
        while ((length = in.read(buffer)) != -1) {
            fos.write(buffer, 0, length);
        }
        in.close();
        fos.close();
    }
}