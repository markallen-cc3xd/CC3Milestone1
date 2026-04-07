import java.util.Scanner;

class MakeReservation {

    // Method to make a reservation for a visitor
    public static void makeReservation(Visitor visitor, Museum museum) {
        Scanner scanner = new Scanner(System.in);

        System.out.println("\n=== Museum Reservation System ===");
        System.out.println("Visitor Name: " + visitor.getName());

        if (visitor.isMember()) {
            System.out.println("Membership ID: " + visitor.getMembershipID());
            System.out.println("Status: MEMBER - You are eligible for priority booking!");
        } else {
            System.out.println("Status: NON-MEMBER");
        }

        System.out.println("\nAvailable Time Slots:");
        museum.displayAvailableSlots();  // We'll add this method to Museum

        System.out.print("\nEnter the time slot you want to reserve (e.g., 9:00 AM - 10:00 AM): ");
        String chosenTime = scanner.nextLine().trim();

        boolean success = museum.reserveSlot(visitor.getName(), chosenTime);

        if (success) {
            System.out.println("\n✅ Reservation successful!");
            if (visitor.isMember()) {
                System.out.println("Fast entry will be available for your visit.");
            }
        } else {
            System.out.println("\n❌ Sorry, the selected time slot is full or does not exist.");
            System.out.println("Please try another available slot.");
        }
    }
}
