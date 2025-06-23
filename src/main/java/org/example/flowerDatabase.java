package org.example;
import java.sql.*;

public class flowerDatabase {
    private static final String DB_URL = "jdbc:sqlite:flowers.db";

    public flowerDatabase() {
        createTable();
    }

    public void createTable() {
        String sql = "CREATE TABLE IF NOT EXISTS flowers ("
                + "id INTEGER PRIMARY KEY AUTOINCREMENT, "
                + "name TEXT NOT NULL, "
                + "image_url TEXT NULL, "
                + "water_interval_days INTEGER NOT NULL, "
                + "days_since_watered INTEGER NOT NULL)";
        try (Connection conn = DriverManager.getConnection(DB_URL))
        {
            try (Statement stmt = conn.createStatement())
            {
                stmt.execute(sql);
                System.out.println("Table created.");
            }
            catch (SQLException e)
            {
                System.err.println("Error creating statement or executing SQL.");
                e.printStackTrace();
            }
        }
        catch (SQLException e)
        {
            System.err.println("Error connecting to the database.");
            e.printStackTrace();
        }
    }

    public void insertFlower(String name, String url, int water_interval_days, int days_since_watered)
    {
        String sql = "INSERT INTO flowers(name, image_url, water_interval_days, " +
                "days_since_watered) VALUES(?, ?, ?, ?)";
        try (Connection conn = DriverManager.getConnection(DB_URL))
        {
            try (PreparedStatement pstmt = conn.prepareStatement(sql))
            {
                pstmt.setString(1, name);
                pstmt.setString(2, url);
                pstmt.setInt(3, water_interval_days);
                pstmt.setInt(4, days_since_watered);
                pstmt.executeUpdate();
            }
            catch (SQLException e)
            {
                System.err.println("Statement error.");
                e.printStackTrace();
            }
        }
        catch (SQLException e)
        {
            System.err.println("Connection error.");
            e.printStackTrace();
        }
    }

    public void printAllFlowers() {
        String sql = "SELECT * FROM flowers";
        try (Connection conn = DriverManager.getConnection(DB_URL);
             Statement stmt = conn.createStatement();
             ResultSet rs = stmt.executeQuery(sql))
        {
            while (rs.next()) //be sure to update this for printing out the flower
            {
                System.out.println(rs.getInt("id") + ": " +
                        rs.getString("name") + " - " +
                        rs.getString("image_url"));
            }
        }
        catch (SQLException e)
        {
            e.printStackTrace();
        }
    }

    public String getDatabseName()
    {
        return "flowers.db";
    }
}
