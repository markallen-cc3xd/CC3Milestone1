package com.mycompany.museum;

import java.util.ArrayList;

public class TimeSlot {
    String time;
    int capacity;
    ArrayList<Visitor> visitors;

    public TimeSlot(String time, int capacity) {
        this.time = time;
        this.capacity = capacity;
        this.visitors = new ArrayList<>();
    }

    public boolean addVisitor(Visitor visitor) {
        if (visitors.size() < capacity) {
            visitors.add(visitor);
            return true;
        }
        return false;
    }

    public void displayVisitors() {
        System.out.println("Time Slot: " + time);
        if (visitors.isEmpty()) {
            System.out.println("  No visitors yet.");
        } else {
            for (Visitor v : visitors) {
                System.out.println("  - " + v);
            }
        }
    }
}
