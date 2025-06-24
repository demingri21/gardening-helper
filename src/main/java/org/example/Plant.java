package org.example;

public class Plant {
    private String name;
    private final int wateringIntervalDays;
    private int daysSinceWatered;

    public Plant(String name, int wateringIntervalDays, int daysSinceWatered)
    {
        this.name = name;
        this.wateringIntervalDays = wateringIntervalDays;
        this.daysSinceWatered = daysSinceWatered;
    }

    public boolean NeedsWatering()
    {
        Water();
        return (daysSinceWatered == wateringIntervalDays);
    }

    private void Water()
    {
        this.daysSinceWatered++;
    }

    public void Reset()
    {
        daysSinceWatered = 0;
    }

}
