// MakeReservation.java
import java.util.Scanner;
import java.util.List;

public class MakeReservation {

    public static void run(Scanner scanner) {
        int choice;
        do {
            System.out.println("\n--- Make a Reservation ---");
            System.out.println("1. New Reservation");
            System.out.println("2. View Reservations");
            System.out.println("3. Cancel Reservation");
            System.out.println("4. Back");
            System.out.print("Choice: ");
            choice = AssignSession.getValidInteger(scanner);

            switch (choice) {
                case 1: createNewReservation(scanner); break;
                case 2: displayReservations(); break;
                case 3: cancelExistingReservation(scanner); break;
                case 4: return;
                default: System.out.println("Invalid choice.");
            }
        } while (choice != 4);
    }

    private static void createNewReservation(Scanner scanner) {
        System.out.print("Enter name: ");
        String name = scanner.nextLine().trim();

        System.out.print("Group size: ");
        int groupSize = 1;
        try {
            groupSize = Integer.parseInt(scanner.nextLine().trim());
        } catch (Exception ignored) {}

        System.out.print("Membership ID (optional): ");
        String membership = scanner.nextLine().trim();

        AssignSession.museum.listSlots();

        System.out.print("\nPreferred slot (or press Enter for auto-assign): ");
        String preferredSlot = scanner.nextLine().trim();

        boolean slotAssigned = false;
        if (!preferredSlot.isEmpty()) {
            slotAssigned = Database.assignToSlot(preferredSlot, name);
        }
        if (!slotAssigned) {
            for (String[] slotInfo : Database.getAllTimeSlots()) {
                if (Database.assignToSlot(slotInfo[0], name)) {
                    slotAssigned = true;
                    preferredSlot = slotInfo[0];
                    break;
                }
            }
        }

        if (slotAssigned) {
            Database.saveReservation(name, groupSize, preferredSlot, membership);
            Database.addVisitorToQueue(name, groupSize);
            System.out.println("Reservation successful for " + name);
        } else {
            System.out.println("No available slots. Added to wait queue.");
            Database.addVisitorToQueue(name, groupSize);
        }
    }

    private static void displayReservations() {
        List<String[]> reservations = Database.getAllReservations();
        if (reservations.isEmpty()) {
            System.out.println("No reservations found.");
            return;
        }
        System.out.printf("%-4s %-20s %-10s %-25s %-10s%n", "#", "Name", "Group", "Slot", "Membership");
        for (int i = 0; i < reservations.size(); i++) {
            String[] r = reservations.get(i);
            System.out.printf("%-4d %-20s %-10s %-25s %-10s%n", (i + 1), r[0], r[1], r[2], r[3]);
        }
    }

    private static void cancelExistingReservation(Scanner scanner) {
        List<String[]> reservations = Database.getAllReservations();
        if (reservations.isEmpty()) {
            System.out.println("No reservations found.");
            return;
        }
        displayReservations();
        System.out.print("\nEnter reservation number to cancel (0 to go back): ");
        int num = AssignSession.getValidInteger(scanner);
        if (num < 1 || num > reservations.size()) {
            System.out.println("Invalid number.");
            return;
        }
        String name = reservations.get(num - 1)[0];
        Database.removeReservation(name);
        Database.removeVisitorFromQueue(name);
        System.out.println("Reservation for " + name + " has been cancelled.");
    }
}
