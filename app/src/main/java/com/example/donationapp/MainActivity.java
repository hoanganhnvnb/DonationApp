package com.example.donationapp;

import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;

import com.google.android.material.snackbar.Snackbar;

import androidx.appcompat.app.AppCompatActivity;

import android.util.Log;
import android.view.View;

import androidx.navigation.NavController;
import androidx.navigation.Navigation;
import androidx.navigation.ui.AppBarConfiguration;
import androidx.navigation.ui.NavigationUI;

import com.example.donationapp.databinding.ActivityMainBinding;

import android.view.Menu;
import android.view.MenuItem;
import android.widget.Button;
import android.widget.EditText;
import android.widget.NumberPicker;
import android.widget.ProgressBar;
import android.widget.RadioGroup;
import android.widget.TextView;
import android.widget.Toast;

import java.util.List;

import api.DonationApi;
import models.Donation;

public class MainActivity extends Base {

    private AppBarConfiguration appBarConfiguration;
    private ActivityMainBinding binding;

    private Button donateButton;
    private RadioGroup paymentMethod;
    private ProgressBar progressBar;
    private NumberPicker amountPicker;
    private EditText numberAmount;

//    private int totalDonated = 0;
    private TextView totalDonate;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        binding = ActivityMainBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());

        setSupportActionBar(binding.toolbar);

//        NavController navController = Navigation.findNavController(this, R.id.nav_host_fragment_content_main);
//        appBarConfiguration = new AppBarConfiguration.Builder(navController.getGraph()).build();
//        NavigationUI.setupActionBarWithNavController(this, navController, appBarConfiguration);

//        binding.fab.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View view) {
//                Snackbar.make(view, "Replace with your own action", Snackbar.LENGTH_LONG)
//                        .setAction("Action", null).show();
//            }
//        });

        donateButton = (Button) findViewById(R.id.donateButton);

        if (donateButton != null) {
            Log.v("Donate", "Really got the donate button");
        }

        paymentMethod = (RadioGroup) findViewById(R.id.paymentMethod);
        progressBar = (ProgressBar) findViewById(R.id.progressBar);
        amountPicker = (NumberPicker) findViewById(R.id.amountPicker);
        totalDonate = (TextView) findViewById(R.id.totalDonate);
        numberAmount = (EditText) findViewById(R.id.numberAmount);

        amountPicker.setMinValue(0);
        amountPicker.setMaxValue(1000);

        progressBar.setMax(10000);
        progressBar.setProgress(app.totalDonated);
        totalDonate.setText("Total so far    $" + app.totalDonated);
    }

    public void donateButtonPressed(View view) {
        int amount = amountPicker.getValue();
        if (amount == 0) {
            String amountValStr = numberAmount.getText().toString();
            try {
                amount = Integer.parseInt(amountValStr);
            } catch (Exception e) {
                Log.v("Donate", "Parse integer" + e.toString());
            }
        }
        String method = paymentMethod.getCheckedRadioButtonId() == R.id.paypal ? "PayPal" : "Direct";

        if (amount > 0) {
//            app.dbManager.open();
//            app.dbManager.setTotalDonated(this);
            app.newDonation(new Donation(amount, method, 0));
            progressBar.setProgress(app.totalDonated);
            totalDonate.setText("Total so far    $" + app.totalDonated);
        }
        numberAmount.setText("");
    }

    @Override
    public void onResume() {
        super.onResume();
        new GetAllTask(this).execute("/getalldonation");
    }

    @Override
    public void reset(MenuItem item) {
//        app.dbManager.reset();
        app.donations.clear();
        app.totalDonated = 0;
        progressBar.setProgress(app.totalDonated);
        totalDonate.setText("Total so far    $" + app.totalDonated);
        numberAmount.setText("");
    }

    private class GetAllTask extends AsyncTask<String, Void, List<Donation>> {
        protected ProgressDialog dialog;
        protected Context context;

        public GetAllTask(Context context) {
            this.context = context;
        }

        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            this.dialog = new ProgressDialog(context, 1);
            this.dialog.setMessage("Retrieving Donations List");
            this.dialog.show();
        }

        @Override
        protected List<Donation> doInBackground(String... params) {
            try {
                Log.v("Donate", "Donation App Getting All Donations");
                return (List<Donation>) DonationApi.getAll((String) params[0]);
            } catch (Exception e) {
                Log.v("Donate", "ERROR: " + e);
                e.printStackTrace();
            }
            return null;
        }

        @Override
        protected void onPostExecute(List<Donation> result) {
            super.onPostExecute(result);

            app.donations = result;
            progressBar.setProgress(app.totalDonated);
            totalDonate.setText("Total so far    $" + app.totalDonated);
            Log.v("Donate", "Get result onPostExecute" + app.totalDonated);

            if (dialog.isShowing()) {
                dialog.dismiss();
            }
        }
    }
}
