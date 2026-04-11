package com.mycompany.museum;

import java.util.ArrayList;

public class Museum {
    private ArrayList<TimeSlot> timeSlots;
    private ArrayList<String> reservations;
    private DatabaseHandler dbHandler;

    public Museum() {
        timeSlots = new ArrayList<>();
        reservations = new ArrayList<>();
        dbHandler = new DatabaseHandler();
        dbHandler.initializeDatabase();
        loadReservationsFromDB();
    }

    public void addSlot(TimeSlot slot) {
        timeSlots.add(slot);
    }

    public void displayAvailableSlots() {
        int index = 1;
        for (TimeSlot slot : timeSlots) {
            int available = slot.capacity - slot.visitors.size();
            System.out.println(index + ". " + slot.time + " - Available: " + available + "/" + slot.capacity);
            index++;
        }
    }

    public boolean reserveSlot(Visitor visitor, int slotNumber) {
        if (slotNumber < 1 || slotNumber > timeSlots.size()) {
            return false;
        }
        
        TimeSlot slot = timeSlots.get(slotNumber - 1);
        
        if (slot.addVisitor(visitor)) {
            reservations.add(visitor.getName() + " - " + slot.time);
            dbHandler.saveReservation(visitor, slot.time);
            return true;
        }
        return false;
    }

    public void displayAllReservations() {
        System.out.println("\n--- All Reservations ---");
        if (reservations.isEmpty()) {
            System.out.println("No reservations yet.");
        } else {
            for (String reservation : reservations) {
                System.out.println(reservation);
            }
        }
        System.out.println("\n--- Detailed View ---");
        for (TimeSlot slot : timeSlots) {
            slot.displayVisitors();
        }
        
        dbHandler.displayAllReservations();
    }
    
    private void loadReservationsFromDB() {
        reservations = dbHandler.loadReservations();
    }
}
