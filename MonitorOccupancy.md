// MonitorOccupancy.java
import java.util.Scanner;

public class MonitorOccupancy {

    private static final int MAX_CAPACITY = 50;

    public static void run(Scanner scanner) {
        int choice;
        do {
            int current = Database.getCurrentOccupancy();
            System.out.println("\n--- Museum Exhibit Occupancy Tracker ---");
            System.out.println("Current Occupancy: " + current + " / " + MAX_CAPACITY);

            System.out.println("1. Person ENTERED");
            System.out.println("2. Person LEFT");
            System.out.println("3. Show Occupancy");
            System.out.println("4. Back to Main Menu");
            System.out.print("Choose option: ");
            choice = MakeReservation.getValidInteger(scanner);

            switch (choice) {
                case 1:
                    if (current < MAX_CAPACITY) {
                        Database.updateOccupancy(current + 1);
                        System.out.println("Person entered. Occupancy: " + (current + 1) + "/" + MAX_CAPACITY);
                    } else {
                        System.out.println("Exhibit is FULL! Cannot enter.");
                    }
                    break;

                case 2:
                    if (current > 0) {
                        Database.updateOccupancy(current - 1);
                        System.out.println("Person left. Occupancy: " + (current - 1) + "/" + MAX_CAPACITY);
                    } else {
                        System.out.println("No one inside.");
                    }
                    break;

                case 3:
                    current = Database.getCurrentOccupancy();
                    System.out.println("Current occupancy: " + current + "/" + MAX_CAPACITY);
                    if (current >= MAX_CAPACITY) {
                        System.out.println("Exhibit is FULL!");
                    } else {
                        System.out.println("Available spots: " + (MAX_CAPACITY - current));
                    }
                    break;

                case 4:
                    return;

                default:
                    System.out.println("Invalid option.");
            }
        } while (choice != 4);
    }
}
