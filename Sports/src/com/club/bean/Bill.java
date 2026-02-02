package com.club.bean;

public class Bill {

    private int billID;
    private String memberID;
    private String billingMonth;
    private int totalSessions;
    private double subscriptionAmount;
    private double usageCharges;
    private double totalAmount;
    private String status;

    public int getBillID() {
        return billID;
    }

    public void setBillID(int billID) {
        this.billID = billID;
    }

    public String getMemberID() {
        return memberID;
    }

    public void setMemberID(String memberID) {
        this.memberID = memberID;
    }

    public String getBillingMonth() {
        return billingMonth;
    }

    public void setBillingMonth(String billingMonth) {
        this.billingMonth = billingMonth;
    }

    public int getTotalSessions() {
        return totalSessions;
    }

    public void setTotalSessions(int totalSessions) {
        this.totalSessions = totalSessions;
    }

    public double getSubscriptionAmount() {
        return subscriptionAmount;
    }

    public void setSubscriptionAmount(double subscriptionAmount) {
        this.subscriptionAmount = subscriptionAmount;
    }

    public double getUsageCharges() {
        return usageCharges;
    }

    public void setUsageCharges(double usageCharges) {
        this.usageCharges = usageCharges;
    }

    public double getTotalAmount() {
        return totalAmount;
    }

    public void setTotalAmount(double totalAmount) {
        this.totalAmount = totalAmount;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }
}
