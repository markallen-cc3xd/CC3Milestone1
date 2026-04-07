import java.util.Scanner;

public class MonitorOccupancy {

    public static void main(String[] args) {
        Scanner scanner = new Scanner(System.in);

        int occupancy = 0;
        int maxCapacity = 50;

        while (true) {
            System.out.println("\n--- Museum Exhibit Occupancy Tracker ---");
            System.out.println("1. Person ENTERED");
            System.out.println("2. Person LEFT");
            System.out.println("3. Show Occupancy");
            System.out.println("4. Exit");
            System.out.print("Choose option: ");

            int choice = scanner.nextInt();

            switch (choice) {
                case 1:
                    occupancy++;
                    System.out.println("Person entered.");
                    break;

                case 2:
                    if (occupancy > 0) {
                        occupancy--;
                        System.out.println("Person left.");
                    } else {
                        System.out.println("No one inside.");
                    }
                    break;

                case 3:
                    System.out.println("Current occupancy: " + occupancy);

                    if (occupancy >= maxCapacity) {
                        System.out.println("⚠ Exhibit is FULL!");
                    }
                    break;

                case 4:
                    System.out.println("Exiting...");
                    scanner.close();
                    return;

                default:
                    System.out.println("Invalid option.");
            }
        }
    }
}
