package org.example;
import java.util.Scanner;
import java.util.ArrayList;

public class GardenReminder
{
    public static void main(String[] args)
    {
        flowerDatabase db = new flowerDatabase();
        Scanner scanner = new Scanner(System.in);
        String input = "";
        String plant_name = null;
        String file_path = null;
        int days_since_watered;
        int water_interval_days;
        ArrayList<Plant> plantList = new ArrayList<>();
        int choice = 0;
        while (choice != 2)
        {
            System.out.println("type 1 to add a new plant, 2 to upload data, 3 to download data, 4 to view all plants");
            choice = scanner.nextInt();
            switch (choice)
            {
                case 1:
                    System.out.println("please enter the name of the plant");
                    plant_name = scanner.nextLine();
                    System.out.println("please enter how often you water the plant in days");
                    water_interval_days = scanner.nextInt();
                    System.out.println("please enter how many days its been since you last watered the plant");
                    days_since_watered = scanner.nextInt();
                    System.out.println("type y if you want to add an image, n otherwise");
                    file_path = scanner.nextLine();
                    if (file_path.equals("y"))
                    {
                        System.out.println("please enter the filepath as an absolute path from your local device");

                    }
                    Plant newPlant = new Plant(plant_name, water_interval_days, days_since_watered);
                    plantList.add(newPlant);
                    break;
                case 2:
                    break;
                case 3:
                    break;
                case 4:
                    break;
            }
        }
    }
}
