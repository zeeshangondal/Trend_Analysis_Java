


package demo;

import com.google.gson.Gson;
import com.google.gson.JsonArray;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.net.URLEncoder;
import java.util.ArrayList;
import java.util.List;

public class GoogleCustomSearchAPI {

    private static final String API_KEY = "AIzaSyAPsBREeltxL03kjzfEIKKd0YE4-pyUINo";
    private static final String SEARCH_ENGINE_ID = "f322a165106484972";
    
    public List<SearchResult> search(String query) throws IOException {
        if (query == null || query.isEmpty()) {
            throw new IllegalArgumentException("Search query cannot be empty");
        }

        String encodedQuery = URLEncoder.encode(query, "UTF-8");
        String apiUrl = "https://www.googleapis.com/customsearch/v1?key=" + API_KEY + "&cx=" + SEARCH_ENGINE_ID + "&q=" + encodedQuery;

        URL url = new URL(apiUrl);
        HttpURLConnection conn = (HttpURLConnection) url.openConnection();
        conn.setRequestMethod("GET");

        int responseCode = conn.getResponseCode();
        if (responseCode != HttpURLConnection.HTTP_OK) {
            throw new IOException("HTTP error " + responseCode);
        }

        BufferedReader reader = new BufferedReader(new InputStreamReader(conn.getInputStream()));
        StringBuilder response = new StringBuilder();
        String line;
        while ((line = reader.readLine()) != null) {
            response.append(line);
        }
        reader.close();
        conn.disconnect();

        return parseSearchResults(response.toString());
    }

    private List<SearchResult> parseSearchResults(String jsonResponse) {
        List<SearchResult> results = new ArrayList<>();
        Gson gson = new Gson();
        JsonObject jsonObject = gson.fromJson(jsonResponse, JsonObject.class);

        if (jsonObject.has("items")) {
            JsonArray items = jsonObject.getAsJsonArray("items");
            for (JsonElement item : items) {
                if (item instanceof JsonObject) {
                    JsonObject itemObject = (JsonObject) item;
                    String title = itemObject.has("title") ? itemObject.get("title").getAsString() : "";
                    String link = itemObject.has("link") ? itemObject.get("link").getAsString() : "";
                    results.add(new SearchResult(title, link));
                }
            }
        }

        return results;
    }

    public static class SearchResult {
        private String title;
        private String link;

        public SearchResult(String title, String link) {
            this.title = title;
            this.link = link;
        }

        public String getTitle() {
            return title;
        }

        public String getLink() {
            return link;
        }

        @Override
        public String toString() {
            return "SearchResult{" +
                    "title='" + title + '\'' +
                    ", link='" + link + '\'' +
                    '}';
        }
    }
}
