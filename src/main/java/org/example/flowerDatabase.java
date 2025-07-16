package org.example;
import java.util.Map;
import java.util.List;
import java.net.URI;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;
import com.google.gson.Gson;
import software.amazon.awssdk.services.dynamodb.DynamoDbClient;
import software.amazon.awssdk.regions.Region;

public class flowerDatabase
{
    private static final String DB_URL = "jdbc:sqlite:flowers.db";
    private final DynamoDbClient dyndbc;
    private HttpClient httpClient;
    public flowerDatabase()
    {
        Region region = Region.US_EAST_1;
        dyndbc = DynamoDbClient.builder().region(region).build();
        httpClient = HttpClient.newHttpClient();
    }

    public void insertFlower(String userId, Plant plant) throws Exception {
        String json = String.format("""
        {
          "action": "insertFlower",
          "userId": "%s",
          "plant": {
            "name": "%s",
            "wateringInterval": %d,
            "daysSinceWatered": %d
          }
        }
        """, userId, plant.getName(), plant.getWateringInterval(), plant.getDaysSinceWatered());

        HttpRequest request = HttpRequest.newBuilder()
                .uri(URI.create("https://u37i5p6ycl.execute-api.us-east-1.amazonaws.com/test_two/insertFlowers"))
                .header("Content-Type", "application/json")
                .POST(HttpRequest.BodyPublishers.ofString(json))
                .build();

        HttpResponse<String> response = httpClient.send(request, HttpResponse.BodyHandlers.ofString());
        System.out.println("Insert flower response: " + response.body());
    }


    public void printAllFlowers(String userId) throws Exception {
        // Build inner JSON with double-escaped quotes
        String innerJson = String.format("{\\\"action\\\":\\\"getFlowers\\\",\\\"userId\\\":\\\"%s\\\"}", userId);

        // Wrap it under 'body'
        String fullJson = String.format("{\"body\":\"%s\"}", innerJson);
        System.out.println("Outgoing POST body: " + fullJson);
        HttpRequest request = HttpRequest.newBuilder()
                .uri(URI.create("https://u37i5p6ycl.execute-api.us-east-1.amazonaws.com/test_two/grabFlowers"))
                .header("Content-Type", "application/json")
                .POST(HttpRequest.BodyPublishers.ofString(fullJson))
                .build();

        HttpResponse<String> response = httpClient.send(request, HttpResponse.BodyHandlers.ofString());
        String responseBody = response.body();
        System.out.println("Raw Lambda response: " + responseBody);

        // Parse outer Lambda response
        Gson gson = new Gson();
        Map<String, Object> outer = gson.fromJson(responseBody, Map.class);

        String innerBody = (String) outer.get("body");

        List<Map<String, Object>> flowers = gson.fromJson(innerBody, List.class);
        System.out.println("Parsed flowers: " + flowers);
    }

    public String getAllFlowersJson(String userId) throws Exception {
        String innerJson = String.format("{\\\"action\\\":\\\"getFlowers\\\",\\\"userId\\\":\\\"%s\\\"}", userId);
        String fullJson = String.format("{\"body\":\"%s\"}", innerJson);

        HttpRequest request = HttpRequest.newBuilder()
                .uri(URI.create("https://u37i5p6ycl.execute-api.us-east-1.amazonaws.com/test_two/grabFlowers"))
                .header("Content-Type", "application/json")
                .POST(HttpRequest.BodyPublishers.ofString(fullJson))
                .build();

        HttpResponse<String> response = httpClient.send(request, HttpResponse.BodyHandlers.ofString());
        String responseBody = response.body();
        System.out.println("Full Lambda response body: " + responseBody);

        // Directly return responseBody since it is a JSON array string
        return responseBody;
    }

    public void deleteFlower(String userId, String flowerName) throws Exception {
        String json = String.format("""
    {
      "action": "deleteFlower",
      "userId": "%s",
      "flowerName": "%s"
    }
    """, userId, flowerName);

        HttpRequest request = HttpRequest.newBuilder()
                .uri(URI.create("https://u37i5p6ycl.execute-api.us-east-1.amazonaws.com/test/deleteFlowers"))
                .header("Content-Type", "application/json")
                .POST(HttpRequest.BodyPublishers.ofString(json))
                .build();

        HttpResponse<String> response = httpClient.send(request, HttpResponse.BodyHandlers.ofString());
        System.out.println("Delete flower response: " + response.body());
    }


    public String getDatabsePath()
    {
        return "./flowers.db";
    }

    public void close()
    {
        dyndbc.close();
    }
}
