// Museum.java - Simple wrapper for time slots (keeps original structure)
import java.util.List;

public class Museum {

    public void listSlots() {
        System.out.println("\n--- Available Time Slots ---");
        List<String[]> slots = Database.getAllTimeSlots();
        for (int i = 0; i < slots.size(); i++) {
            String[] s = slots.get(i);
            System.out.println((i + 1) + ". " + s[0] + " [" + s[2] + "/" + s[1] + " booked]");
        }
    }
}
