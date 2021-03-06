package models;

public class Donation {
    public int id;
    public int amount;
    public String paymenttype;
    public int upvotes;

    public Donation() {
        this.amount = 0;
        this.paymenttype = "";
        this.upvotes = 0;
    }

    public Donation(int amount, String method, int upvotes) {
        this.amount = amount;
        this.paymenttype = method;
        this.upvotes = upvotes;
    }

    public String toString() {
        return id + ", " + amount + ", " + paymenttype + ", " + upvotes;
    }
}
