package com.mycompany.museum.payment;

public abstract class PaymentFramework {
    
    protected String customerName;
    protected double balance;
    protected boolean hasValidPaymentMethod;
    protected static double taxRate = 0.12;
    
    protected String paymentId;
    protected double amount;
    protected String status;
    protected String paymentMethod;
    
    public PaymentFramework(double amount, String paymentMethod) {
        this.amount = amount;
        this.paymentMethod = paymentMethod;
        this.status = "INITIATED";
        this.balance = amount;
        this.hasValidPaymentMethod = true;
        this.paymentId = "PAY" + System.currentTimeMillis();
    }
    
    protected abstract boolean validatePayment(double amount);
    protected abstract double applyDiscount(double amount);
    
    protected double applyTax(double amount) {
        return amount * (1 + taxRate);
    }
    
    protected void finalizeTransaction(double amount) {
        this.balance -= amount;
        this.amount = amount;
        System.out.printf("💰 Final amount charged: $%.2f%n", amount);
    }
    
    public void processInvoice(double rawAmount) {
        System.out.println("\n📄 PROCESSING INVOICE");
        System.out.printf("   Raw amount: $%.2f%n", rawAmount);
        
        double afterDiscount = applyDiscount(rawAmount);
        System.out.printf("   After discount: $%.2f%n", afterDiscount);
        
        double withTax = applyTax(afterDiscount);
        double taxAmount = withTax - afterDiscount;
        System.out.printf("   Tax (%.0f%%): $%.2f%n", taxRate * 100, taxAmount);
        System.out.printf("   Total due: $%.2f%n", withTax);
        
        if (validatePayment(withTax)) {
            finalizeTransaction(withTax);
            this.status = "COMPLETED";
        } else {
            System.out.println("❌ Payment validation failed!");
            this.status = "FAILED";
        }
    }
    
    public abstract boolean processPayment();
    public abstract void generateReceipt();
    
    public boolean refund() {
        if (status.equals("COMPLETED")) {
            System.out.println("💰 Processing refund of $" + String.format("%.2f", amount));
            System.out.println("   Refund to: " + paymentMethod);
            this.status = "REFUNDED";
            return true;
        } else {
            System.out.println("❌ Cannot refund - payment status is: " + status);
            return false;
        }
    }
    
    public String getPaymentId() { return paymentId; }
    public double getAmount() { return amount; }
    public String getStatus() { return status; }
    public String getPaymentMethod() { return paymentMethod; }
    public String getCustomerName() { return customerName; }
    public double getBalance() { return balance; }
    public static double getTaxRate() { return taxRate; }
}
