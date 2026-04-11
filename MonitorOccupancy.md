package com.mycompany.museum;

import java.util.Random;

public class MonitorOccupancy {
    private static final int MAX_OCCUPANCY = 50;
    private static int currentOccupancy = 0;
    private Random random = new Random();
    private DatabaseHandler dbHandler = new DatabaseHandler();
    
    public void start() {
        System.out.println("\n--- Occupancy Tracker ---");
        
        int change = random.nextInt(21) - 10;
        currentOccupancy += change;
        
        if (currentOccupancy < 0) currentOccupancy = 0;
        if (currentOccupancy > MAX_OCCUPANCY) currentOccupancy = MAX_OCCUPANCY;
        
        dbHandler.logOccupancy(currentOccupancy);
        
        System.out.println("Current Museum Occupancy: " + currentOccupancy + "/" + MAX_OCCUPANCY);
        
        if (currentOccupancy >= MAX_OCCUPANCY) {
            System.out.println("MUSEUM AT FULL CAPACITY!");
        } else if (currentOccupancy >= MAX_OCCUPANCY * 0.8) {
            System.out.println("Approaching capacity limit!");
        } else {
            System.out.println("Museum has available space.");
        }
        
        int availableSpots = MAX_OCCUPANCY - currentOccupancy;
        System.out.println("Available spots: " + availableSpots);
        System.out.println("Total reservations in DB: " + dbHandler.getTotalReservationCount());
    }
}
