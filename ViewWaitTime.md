// ViewWaitTime.java
import java.util.Scanner;
import java.util.List;

public class ViewWaitTime {

    private static final int AVG_TIME_PER_PERSON = 2;

    public static void run(Scanner scanner) {
        int choice;
        do {
            System.out.println("\n--- Wait Time / Queue ---");
            System.out.println("1. Add Walk-in");
            System.out.println("2. View Queue");
            System.out.println("3. Estimate Wait Time");
            System.out.println("4. Serve Next");
            System.out.println("5. Back");
            System.out.print("Choice: ");
            choice = MakeReservation.getValidInteger(scanner);

            switch (choice) {
                case 1:
                    System.out.print("Enter visitor name: ");
                    String name = scanner.nextLine().trim();
                    System.out.print("Enter group size: ");
                    int groupSize = 1;
                    try {
                        groupSize = Integer.parseInt(scanner.nextLine().trim());
                    } catch (Exception ignored) {}
                    Database.addVisitorToQueue(name, groupSize);
                    System.out.println("Visitor added to queue.");
                    break;

                case 2:
                    List<String[]> queue = Database.getCurrentQueue();
                    if (queue.isEmpty()) {
                        System.out.println("Queue is empty.");
                    } else {
                        for (int i = 0; i < queue.size(); i++) {
                            String[] v = queue.get(i);
                            System.out.println((i + 1) + ". " + v[0] + " (Group of " + v[1] + ")");
                        }
                    }
                    break;

                case 3:
                    int total = Database.getTotalPeopleWaiting();
                    System.out.println("Estimated wait time: " + (total * AVG_TIME_PER_PERSON) + " minutes");
                    break;

                case 4:
                    Database.serveNextVisitor();
                    break;

                case 5:
                    return;

                default:
                    System.out.println("Invalid choice.");
            }
        } while (choice != 5);
    }
}
