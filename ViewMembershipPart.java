class Visitor {
    private final String name;
    private final String membershipID;

    public Visitor(String name, String membershipID) {
        this.name = (name != null) ? name.trim() : "";
        this.membershipID = (membershipID != null && !membershipID.trim().isEmpty()) 
                            ? membershipID.trim() : null;
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

    @Override
    public String toString() {
        return getName() + (isMember() ? " (Member)" : " (Non-Member)");
    }
}

class MuseumSystem {

    public static void viewMembership(Visitor visitor) {
        if (visitor == null) {
            System.out.println("Error: No visitor provided.");
            return;
        }

        System.out.println("Visitor Name: " + visitor.getName());

        if (visitor.isMember()) {
            printMemberInfo(visitor);
        } else {
            printNonMemberInfo();
        }
    }

    private static void printMemberInfo(Visitor visitor) {
        System.out.println("Membership ID: " + visitor.getMembershipID());
        System.out.println("Status: MEMBER");
        System.out.println("Access: Fast Entry Allowed");
    }

    private static void printNonMemberInfo() {
        System.out.println("Status: NON-MEMBER");
        System.out.println("Access: Regular Entry");
    }
}
