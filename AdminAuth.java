package com.mycompany.museum;

import java.util.Scanner;
import java.util.HashMap;

public class AdminAuth {

    private static final HashMap<String, String> adminCredentials = new HashMap<>();
    
    static {
        
        adminCredentials.put("francis", "admin123");
        adminCredentials.put("roberto", "admin123");
        adminCredentials.put("lapitan", "admin123");
    }
    
    private static boolean isLoggedIn = false;
    private static String currentUser = "";
    private static int loginAttempts = 0;
    private static final int MAX_ATTEMPTS = 3;
    
    public static boolean login(Scanner scanner) {
        
        System.out.println("\n" + "=".repeat(40));
        System.out.println("🔐 ADMIN LOGIN");
        System.out.println("=".repeat(40));
        
        System.out.print("Username: ");
        String username = scanner.next().toLowerCase();
        
        System.out.print("Password: ");
        String password = scanner.next();
        
        if (adminCredentials.containsKey(username) && adminCredentials.get(username).equals(password)) {
            isLoggedIn = true;
            currentUser = username;
            loginAttempts = 0;
            System.out.println("\n✅ Login successful!");
            // REMOVED: Don't show role or specific welcome message
            System.out.println("   Access granted.");
            return true;
        } else {
            loginAttempts++;
            int remaining = MAX_ATTEMPTS - loginAttempts;
            System.out.println("\n❌ Invalid username or password.");
            if (remaining > 0) {
                System.out.println("   " + remaining + " attempt(s) remaining.");
            }
            
            if (loginAttempts >= MAX_ATTEMPTS) {
                System.out.println("\n🔒 Too many failed attempts. Access locked.");
                System.out.println("   Please restart the application to try again.");
                loginAttempts = 0;
            }
            return false;
        }
    }
    
    public static void logout() {
        isLoggedIn = false;
        currentUser = "";
        System.out.println("\n🔒 Logged out successfully.");
    }
    
    public static boolean isAdminLoggedIn() { 
        return isLoggedIn; 
    }
    
    public static String getCurrentUser() { 
        return currentUser; 
    }
    
    
    public static void resetLoginAttempts() {
        loginAttempts = 0;
    }
}
