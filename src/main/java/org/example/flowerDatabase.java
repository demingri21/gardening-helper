package org.example;
import java.util.HashMap;
import java.util.Map;
import java.util.List;
import java.net.URI;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;
import software.amazon.awssdk.services.dynamodb.DynamoDbClient;
import software.amazon.awssdk.regions.Region;
import software.amazon.awssdk.services.dynamodb.model.AttributeValue;
import software.amazon.awssdk.services.dynamodb.model.PutItemRequest;
import software.amazon.awssdk.services.dynamodb.model.QueryRequest;
import software.amazon.awssdk.services.dynamodb.model.QueryResponse;

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

    public void insertFlower(String userId, Plant plant)
    {
        HashMap<String, AttributeValue> tableEntries = new HashMap<>();
        AttributeValue user = AttributeValue.builder().s(userId).build();
        tableEntries.put("UserId", user);

        AttributeValue flowerName = AttributeValue.builder().s(plant.getName()).build();
        tableEntries.put("FlowerName", flowerName);

        String waterIntervalString = Integer.toString(plant.getWateringInterval());
        AttributeValue waterInterval = AttributeValue.builder().n(waterIntervalString).build();
        tableEntries.put("WaterInterval", waterInterval);

        String daysSinceWateredString = Integer.toString(plant.getDaysSinceWatered());
        AttributeValue daysSinceWatered = AttributeValue.builder().n(daysSinceWateredString).build();
        tableEntries.put("daysSinceWatered", daysSinceWatered);

        PutItemRequest request = PutItemRequest.builder()
                .tableName("FlowerDatabase")
                .item(tableEntries)
                .build();

        dyndbc.putItem(request);
    }

    /** public void insertFlower(String userId, Plant plant) {
        try
        {
            String jsonPayload = String.format("""
                {
                "UserID": "%s",
                "FlowerName": "%s",
                "WateringInterval": %d,
                "DaysSinceWatered": %d
            }
            """,
                    userId,
                    plant.getName(),
                    plant.getWateringInterval(),
                    plant.getDaysSinceWatered()
            );
            HttpRequest request = HttpRequest.newBuilder()
                    .uri(URI.create(""))
                    .header("Content-Type", "application/json")
                    .POST(HttpRequest.BodyPublishers.ofString(jsonPayload))
                    .build();
            HttpResponse<String> response = httpClient.send(request, HttpResponse.BodyHandlers.ofString());
        }
        catch (Exception e)
        {
            System.out.println("Error inserting flower: " + e.getMessage());
        }
    }**/


    public void printAllFlowers(String userId)
    {
        AttributeValue userAttribute = AttributeValue.builder().s(userId).build();
        QueryRequest queryRequest = QueryRequest.builder().
                tableName("FlowerDatabase").keyConditionExpression("UserId = :userId")
                .expressionAttributeValues(Map.of(":userId", userAttribute))
                .build();
        QueryResponse response = dyndbc.query(queryRequest);
        List<Map<String, AttributeValue>> flowers = response.items();
        if (flowers.isEmpty())
        {
            System.out.println("you have no flowers in your list");
        }
        for (int i = 0; i < flowers.size(); i++)
        {
            Map<String, AttributeValue> item = flowers.get(i);
            String flowerName = item.get("FlowerName").s();
            String wateringInterval = item.get("WaterInterval").n();
            String lastWatered = item.get("daysSinceWatered").n();
            System.out.println("Flower: " + flowerName);
            System.out.println("Watering Interval: " + wateringInterval + " days");
            System.out.println("Last Watered: " + lastWatered);
        }
    }

    public void clearTable()
    {

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
