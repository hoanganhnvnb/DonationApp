package main;

import android.app.Application;
import android.util.Log;
import android.widget.Toast;

import java.util.ArrayList;
import java.util.List;

import database.DBManager;
import models.Donation;

public class DonationApp extends Application {

    public final int target = 10000;
    public int totalDonated = 0;
    public static List<Donation> donations = new ArrayList<Donation>();
//    public DBManager dbManager;

    public boolean newDonation(Donation donation) {
        boolean targetAchieved = totalDonated > target;
        if (!targetAchieved) {
//            dbManager.add(donation);
            donations.add(donation);
            totalDonated += donation.amount;
        } else {
            Toast toast = Toast.makeText(this, "Target Exceeded!", Toast.LENGTH_SHORT);
            toast.show();
        }
        return targetAchieved;
    }

    @Override
    public void onCreate() {
        super.onCreate();
        Log.v("Donate", "DonationApp started");
//        dbManager = new DBManager(this);
//        Log.v("Donate", "Created db");
    }
}
