// AssignSession.java - MAIN ENTRY POINT
import java.util.Scanner;

public class AssignSession {

    public static Museum museum = new Museum();   // kept for backward compatibility with original design

    public static void main(String[] args) {
        Database.connect();

        Scanner scanner = new Scanner(System.in);
        int choice;

        System.out.println("========================================");
        System.out.println("   Museum Session Management System     ");
        System.out.println("========================================");

        do {
            System.out.println("\n--- Main Menu ---");
            System.out.println("1. Make a Reservation");
            System.out.println("2. View Wait Time / Queue");
            System.out.println("3. View Available Time Slots");
            System.out.println("4. View Membership");
            System.out.println("5. Monitor Occupancy");
            System.out.println("6. Exit");
            System.out.print("Enter choice: ");

            choice = getValidInteger(scanner);

            switch (choice) {
                case 1:
                    MakeReservation.run(scanner);
                    break;
                case 2:
                    ViewWaitTime.run(scanner);
                    break;
                case 3:
                    museum.listSlots();
                    break;
                case 4:
                    ViewMembership.run(scanner);
                    break;
                case 5:
                    MonitorOccupancy.run(scanner);
                    break;
                case 6:
                    System.out.println("Goodbye!");
                    break;
                default:
                    System.out.println("Invalid choice. Please try again.");
            }
        } while (choice != 6);

        scanner.close();
        Database.close();
    }

    public static int getValidInteger(Scanner scanner) {
        try {
            return Integer.parseInt(scanner.nextLine().trim());
        } catch (Exception e) {
            return -1;
        }
    }
}
