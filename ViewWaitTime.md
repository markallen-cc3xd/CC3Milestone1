package com.mycompany.museum;

import java.util.LinkedList;
import java.util.Queue;

public class ViewWaitTime {
    private static Queue<QueueVisitor> waitingQueue = new LinkedList<>();
    private static DatabaseHandler dbHandler = new DatabaseHandler();
    
    public static void addVisitor(String name, int groupSize) {
        waitingQueue.add(new QueueVisitor(name, groupSize));
        dbHandler.saveToWaitingQueue(name, groupSize);
        System.out.println(name + " (Group of " + groupSize + ") added to queue.");
    }
    
    public static void estimateWaitTime() {
        if (waitingQueue.isEmpty()) {
            System.out.println("No visitors in queue.");
            return;
        }
        
        System.out.println("\n--- Queue Status ---");
        int position = 1;
        for (QueueVisitor qv : waitingQueue) {
            System.out.println(position + ". " + qv.name + " (Group of " + qv.groupSize + ")");
            position++;
        }
        
        int totalWaitTime = waitingQueue.size() * 5;
        System.out.println("Estimated wait time for next visitor: " + totalWaitTime + " minutes");
    }
    
    static class QueueVisitor {
        String name;
        int groupSize;
        
        QueueVisitor(String name, int groupSize) {
            this.name = name;
            this.groupSize = groupSize;
        }
    }
}
