import java.util.*;

class Visitor {
    String name;
    int groupSize;
    long arrivalTime;

    public Visitor(String name, int groupSize) {
        this.name = name;
        this.groupSize = groupSize;
        this.arrivalTime = System.currentTimeMillis();
    }

    @Override
    public String toString() {
        return name + " (Group of " + groupSize + ")";
    }
}

public class ViewWaitTime {

    private static Queue<Visitor> queue = new LinkedList<>();
    
    // Average time per person in minutes
    private static final int AVG_TIME_PER_PERSON = 2;

    public static void addVisitor(String name, int groupSize) {
        Visitor visitor = new Visitor(name, groupSize);
        queue.add(visitor);
        System.out.println("Added to queue: " + visitor);
    }

    public static void viewQueue() {
        if (queue.isEmpty()) {
            System.out.println("Queue is empty.");
            return;
        }

        System.out.println("\nCurrent Queue:");
        int position = 1;
        for (Visitor v : queue) {
            System.out.println(position++ + ". " + v);
        }
    }

    public static void estimateWaitTime() {
        if (queue.isEmpty()) {
            System.out.println("No waiting time. Queue is empty.");
            return;
        }

        int totalPeopleAhead = 0;

        for (Visitor v : queue) {
            totalPeopleAhead += v.groupSize;
        }

        int estimatedTime = totalPeopleAhead * AVG_TIME_PER_PERSON;

        System.out.println("Estimated wait time: " + estimatedTime + " minutes.");
    }

    public static void serveNextVisitor() {
        if (queue.isEmpty()) {
            System.out.println("No visitors to serve.");
            return;
        }

        Visitor served = queue.poll();
        System.out.println("Now serving: " + served);
    }

    public static void main(String[] args) {
        Scanner scanner = new Scanner(System.in);
        int choice;

        do {
            System.out.println("\n--- Museum Wait-Time System ---");
            System.out.println("1. Add Walk-in Visitor");
            System.out.println("2. View Queue");
            System.out.println("3. Estimate Wait Time");
            System.out.println("4. Serve Next Visitor");
            System.out.println("5. Exit");
            System.out.print("Enter choice: ");
            
            choice = scanner.nextInt();
            scanner.nextLine(); // clear buffer

            switch (choice) {
                case 1:
                    System.out.print("Enter visitor name: ");
                    String name = scanner.nextLine();

                    System.out.print("Enter group size: ");
                    int size = scanner.nextInt();

                    addVisitor(name, size);
                    break;

                case 2:
                    viewQueue();
                    break;

                case 3:
                    estimateWaitTime();
                    break;

                case 4:
                    serveNextVisitor();
                    break;

                case 5:
                    System.out.println("Exiting system...");
                    break;

                default:
                    System.out.println("Invalid choice.");
            }

        } while (choice != 5);

        scanner.close();
    }
}
