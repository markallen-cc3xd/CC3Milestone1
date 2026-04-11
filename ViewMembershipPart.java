package com.mycompany.museum;

public class Visitor {
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

    @Override
    public String toString() {
        return name + (isMember() ? " (Member)" : " (Non-member)");
    }
}
