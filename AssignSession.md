import java.util.ArrayList;

class TimeSlot {
    String time;
    int capacity;
    ArrayList<String> visitors;

    public TimeSlot(String time, int capacity) {
        this.time = time;
        this.capacity = capacity;
        visitors = new ArrayList<>();
    }

    public boolean addVisitor(String visitorName) {
        if (visitors.size() < capacity) {
            visitors.add(visitorName);
            return true;
        }
        return false;
    }
}

class Museum {
    ArrayList<TimeSlot> slots;

    public Museum() {
        slots = new ArrayList<>();
    }

    public void addSlot(TimeSlot slot) {
        slots.add(slot);
    }

    public void assignSlot(String visitorName) {
        for (TimeSlot slot : slots) {
            if (slot.addVisitor(visitorName)) {
                System.out.println(visitorName + " assigned to time-slot: " + slot.time);
                return;
            }
        }
        System.out.println("No available time-slot for the exhibit.");
    }
}

public class Main {
    public static void main(String[] args) {

        Museum museum = new Museum();

        museum.addSlot(new TimeSlot("9:00 AM - 10:00 AM", 2));
        museum.addSlot(new TimeSlot("10:00 AM - 11:00 AM", 2));
        museum.addSlot(new TimeSlot("11:00 AM - 12:00 PM", 2));

        museum.assignSlot("Alice");
        museum.assignSlot("Bob");
        museum.assignSlot("Charlie");
        museum.assignSlot("David");
        museum.assignSlot("Emma");
    }
}
