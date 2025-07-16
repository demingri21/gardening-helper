package org.example;

import static spark.Spark.*;
import com.google.gson.Gson;

public class GardenReminder {

    static Gson gson = new Gson();
    static flowerDatabase db = new flowerDatabase();
    static String currentUsername = null;

    public static void main(String[] args) {
        staticFileLocation("/public");
        port(8080);
        enableCORS();
        //POST /setUsername
        post("/setUsername", (req, res) -> {
            UsernameRequest data = gson.fromJson(req.body(), UsernameRequest.class);
            currentUsername = data.username;
            System.out.println("Username received: " + currentUsername);
            res.type("text/plain");
            return "Username saved: " + currentUsername;
        });

        //POST /addFlower
        post("/addFlower", (req, res) -> {
            if (currentUsername == null) {
                res.status(400);
                return "Error: Username not set yet. Please log in first.";
            }

            AddFlowerRequest data = gson.fromJson(req.body(), AddFlowerRequest.class);
            Plant p = new Plant(data.flowerName, data.waterInterval, data.daysSinceWatered);
            db.insertFlower(currentUsername, p); // use the global username
            res.type("text/plain");
            return "Flower added for user " + currentUsername;
        });

        get("/getFlowers", (req, res) -> {
            if (currentUsername == null) {
                res.status(400);
                return "Error: Username not set yet. Please log in first.";
            }

            System.out.println("Username in /getFlowers route: " + currentUsername);

            try {
                String flowersJson = db.getAllFlowersJson(currentUsername);
                System.out.println("Flowers JSON retrieved: " + flowersJson);
                res.type("application/json");
                return flowersJson;
            } catch (Exception e) {
                System.out.println("Exception in /getFlowers route: " + e.getMessage());
                e.printStackTrace();
                res.status(500);
                return "Error fetching flowers: " + e.getMessage();
            }
        });

        post("/deleteFlower", (req, res) -> {
            if (currentUsername == null) {
                res.status(400);
                return "Error: Username not set.";
            }

            DeleteFlowerRequest data = gson.fromJson(req.body(), DeleteFlowerRequest.class);
            db.deleteFlower(currentUsername, data.flowerName);
            res.type("text/plain");
            return "Deleted flower: " + data.flowerName;
        });


        System.out.println("Server running on http://localhost:8080/");
    }

    private static void enableCORS() {
        options("/*", (req, res) -> {
            String headers = req.headers("Access-Control-Request-Headers");
            if (headers != null) res.header("Access-Control-Allow-Headers", headers);
            String methods = req.headers("Access-Control-Request-Method");
            if (methods != null) res.header("Access-Control-Allow-Methods", methods);
            return "OK";
        });

        before((req, res) -> {
            res.header("Access-Control-Allow-Origin", "*");
            res.header("Access-Control-Allow-Methods", "*");
            res.header("Access-Control-Allow-Headers", "*");
        });
    }

    //Helper classes
    static class UsernameRequest { String username; }
    static class AddFlowerRequest {
        String username;
        String flowerName;
        int waterInterval;
        int daysSinceWatered;
    }
    static class DeleteFlowerRequest {
        String username;
        String flowerName;
    }

}
