import java.net.URI;
import java.net.http.*;
import org.json.*;

public class TranslationService {

    private static final String API_KEY = "YOUR_API_KEY";

    public String translate(String text) throws Exception {

        String body = "{ \"q\": \"" + text + "\", \"target\": \"en\" }";

        HttpRequest request = HttpRequest.newBuilder()
                .uri(URI.create("https://translation.googleapis.com/language/translate/v2?key=" + API_KEY))
                .header("Content-Type", "application/json")
                .POST(HttpRequest.BodyPublishers.ofString(body))
                .build();

        HttpClient client = HttpClient.newHttpClient();
        HttpResponse<String> response = client.send(request, HttpResponse.BodyHandlers.ofString());

        JSONObject json = new JSONObject(response.body());
        return json.getJSONObject("data")
                .getJSONArray("translations")
                .getJSONObject(0)
                .getString("translatedText");
    }
}