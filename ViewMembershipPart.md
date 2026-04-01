class Visitor {
    private String name;
    private String membershipID;

  
    public Visitor(String name, String membershipID) {
        this.name = name;
        this.membershipID = membershipID;
    }

    
    public String getName() {
        return name;
    }

    public String getMembershipID() {
        return membershipID;
    }

    
    public boolean isMember() {
        return membershipID != null && !membershipID.isEmpty();
    }
}

class MuseumSystem {

    // Method to view membership and allow fast entry
    public static void viewMembership(Visitor visitor) {
        System.out.println("Visitor Name: " + visitor.getName());

        if (visitor.isMember()) {
            System.out.println("Membership ID: " + visitor.getMembershipID());
            System.out.println("Status: MEMBER");
            System.out.println("Access: Fast Entry Allowed");
        } else {
            System.out.println("Status: NON-MEMBER");
            System.out.println("Access: Regular Entry");
        }
    }
