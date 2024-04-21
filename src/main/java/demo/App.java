package demo;
import com.sun.net.httpserver.HttpServer;
import com.sun.net.httpserver.HttpHandler;
import com.sun.net.httpserver.HttpExchange;

import java.io.IOException;
import java.io.OutputStream;
import java.net.InetSocketAddress;
import java.net.URI;
import java.util.ArrayList;
import java.util.List;

import demo.GoogleCustomSearchAPI;

public class App {

    public static void main(String[] args) throws IOException {
        // Create HttpServer instance on localhost at port 3001
        HttpServer server = HttpServer.create(new InetSocketAddress(3001), 0);

        // Create contexts for each specific path and assign corresponding handlers
        server.createContext("/term_volume", new TermVolumeHandler());
        server.createContext("/trending_data", new TrendingDataHandler());
        server.createContext("/news", new NewsHandler());

        // Start the server
        server.start();
        System.out.println("Server is running on port 3001");
    }

    static class TermVolumeHandler implements HttpHandler {
        @Override
        public void handle(HttpExchange exchange) throws IOException {
            // Set the response headers (200 status and content type)
            exchange.getResponseHeaders().set("Content-Type", "text/plain");
            exchange.sendResponseHeaders(200, 0);

            
            // Parse query parameters
            List<String> terms = getQueryParameters(exchange.getRequestURI(), "term");
            List<String> locations = getQueryParameters(exchange.getRequestURI(), "location");

            // Process the received parameters
            System.out.println("Received terms for term_volume: " + terms);
            System.out.println("Received locations for term_volume: " + locations);
            
            // Perform search using Google Custom Search API
            String query = "Volume of terms " + String.join(" and ", terms) + " in " + String.join(", ", locations);

            GoogleCustomSearchAPI customSearchAPI = new GoogleCustomSearchAPI();
            List<GoogleCustomSearchAPI.SearchResult> searchResults = customSearchAPI.search(query);

            // Prepare response content
            StringBuilder response = new StringBuilder();
            response.append("Search Results:\n");
            for (GoogleCustomSearchAPI.SearchResult result : searchResults) {
                response.append("- ").append(result.getTitle()).append(": ").append(result.getLink()).append("\n");
            }

            // Write the response content to the output stream
            OutputStream responseBody = exchange.getResponseBody();
            responseBody.write(response.toString().getBytes());

            // Close the output stream
            responseBody.close();
        }
    }

    static class TrendingDataHandler implements HttpHandler {
        @Override
        public void handle(HttpExchange exchange) throws IOException {
            // Set the response headers (200 status and content type)
            exchange.getResponseHeaders().set("Content-Type", "text/plain");
            exchange.sendResponseHeaders(200, 0);

            // Parse query parameters
            List<String> terms = getQueryParameters(exchange.getRequestURI(), "term");
            List<String> locations = getQueryParameters(exchange.getRequestURI(), "location");

            // Process the received parameters
            System.out.println("Received terms for trending_data: " + terms);
            System.out.println("Received locations for trending_data: " + locations);

            // Perform search using Google Custom Search API
            String query = "Trending data about " + String.join(", ", terms);

            GoogleCustomSearchAPI customSearchAPI = new GoogleCustomSearchAPI();
            List<GoogleCustomSearchAPI.SearchResult> searchResults = customSearchAPI.search(query);

            // Prepare response content
            StringBuilder response = new StringBuilder();
            response.append("Search Results:\n");
            for (GoogleCustomSearchAPI.SearchResult result : searchResults) {
                response.append("- ").append(result.getTitle()).append(": ").append(result.getLink()).append("\n");
            }

            // Write the response content to the output stream
            OutputStream responseBody = exchange.getResponseBody();
            responseBody.write(response.toString().getBytes());

            // Close the output stream
            responseBody.close();
        }
    }

    static class NewsHandler implements HttpHandler {
        @Override
        public void handle(HttpExchange exchange) throws IOException {
            // Set the response headers (200 status and content type)
            exchange.getResponseHeaders().set("Content-Type", "text/plain");
            exchange.sendResponseHeaders(200, 0);

            // Parse query parameters
            List<String> terms = getQueryParameters(exchange.getRequestURI(), "term");
            List<String> locations = getQueryParameters(exchange.getRequestURI(), "location");

            // Process the received parameters
            System.out.println("Received terms for news: " + terms);
            System.out.println("Received locations for news: " + locations);

            // Perform search using Google Custom Search API
            String query = "News related to " + String.join(", ", terms);
            GoogleCustomSearchAPI customSearchAPI = new GoogleCustomSearchAPI();
            List<GoogleCustomSearchAPI.SearchResult> searchResults = customSearchAPI.search(query);

            // Prepare response content
            StringBuilder response = new StringBuilder();
            response.append("Search Results:\n");
            for (GoogleCustomSearchAPI.SearchResult result : searchResults) {
                response.append("- ").append(result.getTitle()).append(": ").append(result.getLink()).append("\n");
            }

            // Write the response content to the output stream
            OutputStream responseBody = exchange.getResponseBody();
            responseBody.write(response.toString().getBytes());

            // Close the output stream
            responseBody.close();
        }
    }

    // Helper method to parse query parameters from the request URI
    private static List<String> getQueryParameters(URI uri, String paramName) {
        List<String> values = new ArrayList<>();
        String query = uri.getQuery();
        if (query != null) {
            String[] params = query.split("&");
            for (String param : params) {
                String[] pair = param.split("=");
                if (pair.length == 2 && pair[0].equals(paramName)) {
                    values.add(pair[1]);
                }
            }
        }
        return values;
    }
}
