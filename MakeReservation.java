// File: MakeReservation.md  (updated part)
public static void makeReservation(Visitor visitor, Museum museum) {
    Scanner scanner = new Scanner(System.in);

    System.out.println("\n=== Make Reservation ===");
    System.out.println("Visitor: " + visitor.getName());

    if (visitor.isMember()) {
        System.out.println("Status: MEMBER (Priority Access)");
    }

    System.out.println("\nAvailable Time Slots:");
    museum.displayAvailableSlots();

    System.out.print("\nEnter time slot (e.g., 9:00 AM - 10:00 AM): ");
    String chosenTime = scanner.nextLine().trim();

    boolean success = museum.reserveSlot(visitor.getName(), chosenTime);

    if (success) {
        // Save to SQLite database
        boolean saved = DatabaseManager.getInstance().saveReservation(visitor, chosenTime);

        System.out.println("\n Reservation successful for " + chosenTime);
        if (saved) {
            System.out.println("   (Saved to database)");
        }
        if (visitor.isMember()) {
            System.out.println("   Fast entry allowed!");
        }
    } else {
        System.out.println("\n Reservation failed. Slot may be full or invalid.");
    }
}
