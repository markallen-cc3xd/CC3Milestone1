package com.mycompany.museum;

import com.mycompany.museum.payment.MuseumTransaction;
import java.util.Scanner;

public class AdminMenu {
    public static void showAdminMenu(Scanner scanner, Museum museum) {
        boolean adminRunning = true;
        
        while (adminRunning) {
            System.out.println("\n" + "=".repeat(40));
            System.out.println("👑 ADMIN CONTROL PANEL");
            System.out.println("=".repeat(40));
            System.out.println("1. 📊 View Income Statement");
            System.out.println("2. 📋 View All Bookings");
            System.out.println("3. 📈 View Revenue Summary");
            System.out.println("4. 🔒 Logout");
            System.out.println("-".repeat(40));
            System.out.print("Choice: ");
            
            int choice;
            try {
                choice = scanner.nextInt();
            } catch (Exception e) {
                System.out.println("❌ Invalid input.");
                scanner.next();
                continue;
            }
            
            switch (choice) {
                case 1 -> {
                    System.out.println("\n📊 Generating Income Statement...");
                    MuseumTransaction.viewAllTransactions();
                }
                case 2 -> {
                    DatabaseHandler db = new DatabaseHandler();
                    db.displayAllBookings();
                }
                case 3 -> {
                    DatabaseHandler db = new DatabaseHandler();
                    double revenue = db.getTotalRevenue();
                    int count = db.getBookingCount();
                    System.out.println("\n📈 REVENUE SUMMARY");
                    System.out.println("─".repeat(40));
                    System.out.printf("💰 Total Revenue: $%.2f%n", revenue);
                    System.out.printf("📝 Total Bookings: %d%n", count);
                    System.out.printf("📊 Average per Booking: $%.2f%n", count > 0 ? revenue / count : 0);
                    System.out.println("─".repeat(40));
                }
                case 4 -> {
                    AdminAuth.logout();
                    adminRunning = false;
                }
                default -> System.out.println("❌ Invalid choice. Please choose 1-4.");
            }
        }
    }
}
