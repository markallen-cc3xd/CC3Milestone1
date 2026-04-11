package meseumdb;
import java.util.*;
public class MeseumDB {

   
    public static void main(String[] args) {
      
  

class Visitor {
    private String name;
    private String session;

    public Visitor(String name) {
        this.name = name;
        this.session = "";
    }

    public String getName() {
        return name;
    }

    public String getSession() {
        return session;
    }

    public void setSession(String session) {
        this.session = session;
    }
}


    // Available sessions with capacity
    private static Map<String, Integer> sessions = new HashMap<>();

    static {
        sessions.put("09:00 - 10:00", 5); // 5 visitors per slot
        sessions.put("10:00 - 11:00", 5);
        sessions.put("11:00 - 12:00", 5);
    }

    public static void assignSession(Visitor visitor) {
        for (Map.Entry<String, Integer> entry : sessions.entrySet()) {
            if (entry.getValue() > 0) {
                visitor.setSession(entry.getKey());
                sessions.put(entry.getKey(), entry.getValue() - 1); // reduce capacity
                System.out.println(visitor.getName() + " assigned to session " + visitor.getSession());
                return;
            }
        }
        System.out.println("No available sessions for " + visitor.getName());
    }

    public static void main(String[] args) {
        Scanner sc = new Scanner(System.in);

        List<Visitor> visitors = new ArrayList<>();
        System.out.println("Enter number of visitors:");
        int n = sc.nextInt();
        sc.nextLine(); // consume newline

        for (int i = 0; i < n; i++) {
            System.out.println("Enter visitor name:");
            String name = sc.nextLine();
            visitors.add(new Visitor(name));
        }

        // Assign sessions
        for (Visitor v : visitors) {
            assignSession(v);
        }

        // Display all assignments
        System.out.println("\nSession Assignments:");
        for (Visitor v : visitors) {
            System.out.println(v.getName() + " -> " + v.getSession());
        }

        sc.close();
    }
}
    }
    }
