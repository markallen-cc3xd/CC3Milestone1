package com.mycompany.museum;

import java.util.Scanner;

public class MakeReservation {

    public static void makeReservation(Visitor visitor, Museum museum, Scanner scanner) {
        System.out.println("\nVisitor: " + visitor);
        if (visitor.isMember()) {
            System.out.println("Priority booking available!");
        }

        System.out.println("\nAvailable Time Slots:");
        museum.displayAvailableSlots();

        System.out.print("\nEnter time slot number (1, 2, or 3): ");
        int slotNumber;
        try {
            slotNumber = scanner.nextInt();
        } catch (Exception e) {
            System.out.println("Invalid input.");
            scanner.next();
            return;
        }

        boolean success = museum.reserveSlot(visitor, slotNumber);
        if (success) {
            System.out.println("Reservation successful for " + visitor.getName());
        } else {
            System.out.println("Time slot full or invalid. Try another slot.");
        }
    }
}
