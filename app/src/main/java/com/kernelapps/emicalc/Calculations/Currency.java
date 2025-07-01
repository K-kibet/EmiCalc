package com.kernelapps.emicalc.Calculations;

import android.annotation.SuppressLint;
import android.app.Dialog;
import android.content.Context;
import android.content.SharedPreferences;
import android.graphics.drawable.ColorDrawable;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.AsyncTask;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.view.inputmethod.InputMethodManager;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.ListAdapter;
import android.widget.ListView;
import android.widget.RelativeLayout;
import android.widget.Spinner;
import android.widget.SpinnerAdapter;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.kernelapps.emicalc.R;
import com.kernelapps.emicalc.ads.AdsManager;


import java.io.InputStream;
import java.io.InputStreamReader;
import java.math.BigDecimal;
import java.math.RoundingMode;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.ArrayList;
import java.util.List;

import org.json.JSONObject;


public class Currency extends AppCompatActivity implements AdapterView.OnItemSelectedListener {

    ArrayAdapter adapter;

    TextView dateview;
    SharedPreferences frontshare;

    boolean isCalculated;
    ListView listView;

    SharedPreferences sharedPreferences;
    Spinner spiner;
    EditText value;
    List<String> units = new ArrayList();
    int choosen = 0;
    double[] displayanswer = new double[6];
    String[] finalanswer = new String[6];
    String measurement = "0";
    double[] euroto = new double[6];

    @Override
    public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
        this.choosen = position;
    }

    @Override
    public void onNothingSelected(AdapterView<?> parent) {
    }


    public class Downloadtask extends AsyncTask<String, Void, String> {
        public Downloadtask() {
        }


        @Override
        public String doInBackground(String... urls) {
            String result = "";
            try {
                URL url = new URL(urls[0]);
                HttpURLConnection connection = (HttpURLConnection) url.openConnection();
                InputStream inputStream = connection.getInputStream();
                InputStreamReader reader = new InputStreamReader(inputStream);
                for (int data = reader.read(); data != -1; data = reader.read()) {
                    char a = (char) data;
                    result = result + a;
                }
                return result;
            } catch (Exception e) {
                Log.e("Currency Error befre", e.getMessage());
                return "";
            }
        }


        @Override
        public void onPostExecute(String s) {
            super.onPostExecute(s);
            try {
                JSONObject jasonobject = new JSONObject(s);
                String arr = jasonobject.optString("data");
                JSONObject array = new JSONObject(arr);
                Currency.this.euroto[0] = 1.0d / array.optDouble("EUR");
                Currency.this.euroto[1] = array.optDouble("INR") / array.optDouble("EUR");
                Currency.this.euroto[3] = array.optDouble("AED") / array.optDouble("EUR");
                Currency.this.euroto[4] = array.optDouble("JPY") / array.optDouble("EUR");
                Currency.this.euroto[5] = array.optDouble("GBP") / array.optDouble("EUR");
                Currency.this.euroto[2] = array.optDouble("EUR") / array.optDouble("EUR");
                Currency.this.sharedPreferences.edit().putFloat("euroto0", (float) Currency.this.euroto[0]).apply();
                Currency.this.sharedPreferences.edit().putFloat("euroto1", (float) Currency.this.euroto[1]).apply();
                Currency.this.sharedPreferences.edit().putFloat("euroto2", (float) Currency.this.euroto[2]).apply();
                Currency.this.sharedPreferences.edit().putFloat("euroto3", (float) Currency.this.euroto[3]).apply();
                Currency.this.sharedPreferences.edit().putFloat("euroto4", (float) Currency.this.euroto[4]).apply();
                Currency.this.sharedPreferences.edit().putFloat("euroto5", (float) Currency.this.euroto[5]).apply();
            } catch (Exception e) {
                Log.e("THe Currency Error", e.getMessage());
                Currency.this.euroto[0] = 1.209329d;
                Currency.this.euroto[1] = 88.522262d;
                Currency.this.euroto[3] = 4.441877d;
                Currency.this.euroto[4] = 125.802875d;
                Currency.this.euroto[5] = 0.888996d;
                Currency.this.euroto[2] = 1.0d;
                Currency.this.sharedPreferences.edit().putFloat("euroto0", (float) Currency.this.euroto[0]).apply();
                Currency.this.sharedPreferences.edit().putFloat("euroto1", (float) Currency.this.euroto[1]).apply();
                Currency.this.sharedPreferences.edit().putFloat("euroto2", (float) Currency.this.euroto[2]).apply();
                Currency.this.sharedPreferences.edit().putFloat("euroto3", (float) Currency.this.euroto[3]).apply();
                Currency.this.sharedPreferences.edit().putFloat("euroto4", (float) Currency.this.euroto[4]).apply();
                Currency.this.sharedPreferences.edit().putFloat("euroto5", (float) Currency.this.euroto[5]).apply();
            }
        }
    }

    public void calculate(View view) {
        InputMethodManager imm = (InputMethodManager) getSystemService(Context.INPUT_METHOD_SERVICE);
        imm.hideSoftInputFromWindow(view.getWindowToken(), 0);
        if (!this.value.getText().toString().isEmpty()) {
            this.measurement = this.value.getText().toString();
        } else {
            this.value.setText("0");
            this.measurement = "0";
        }
        conversion();
    }

    public void conversion() {
        for (int i = 0; i <= 5; i++) {
            double[] dArr = this.displayanswer;
            double parseDouble = Double.parseDouble(this.measurement);
            double[] dArr2 = this.euroto;
            dArr[i] = parseDouble * (dArr2[i] / dArr2[this.choosen]);
        }
        for (int i2 = 0; i2 <= 5; i2++) {
            double[] dArr3 = this.displayanswer;
            if (dArr3[i2] == ((int) dArr3[i2])) {
                int ans = (int) dArr3[i2];
                dArr3[i2] = ans;
            } else {
                double displaying = dArr3[i2];
                BigDecimal bd = new BigDecimal(displaying).setScale(2, RoundingMode.HALF_UP);
                this.displayanswer[i2] = bd.doubleValue();
            }
        }
        for (int i3 = 0; i3 <= 5; i3++) {
            String[] strArr = this.finalanswer;
            strArr[i3] = this.displayanswer[i3] + "  " + this.units.get(i3);
        }
        ArrayAdapter arrayAdapter = new ArrayAdapter(this, (int) R.layout.align_right, this.finalanswer);
        this.adapter = arrayAdapter;
        this.listView.setAdapter((ListAdapter) arrayAdapter);
    }

    boolean internet_connection() {
        ConnectivityManager cm = (ConnectivityManager) getSystemService(Context.CONNECTIVITY_SERVICE);
        NetworkInfo activeNetwork = cm.getActiveNetworkInfo();
        return activeNetwork != null && activeNetwork.isConnected() && activeNetwork.isAvailable();
    }


    @SuppressLint("ResourceType")
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_currency);
        AdsManager.INSTANCE.loadBanner(this, findViewById(R.id.banner));

        this.frontshare = getApplicationContext().getSharedPreferences(getPackageName(), 0);
        LinearLayout mainlayout = (LinearLayout) findViewById(R.id.mainlinear);
        Animation animation = AnimationUtils.loadAnimation(this, R.anim.slideup);
        mainlayout.startAnimation(animation);
        this.value = (EditText) findViewById(R.id.amount);
        this.spiner = (Spinner) findViewById(R.id.spinner);
        this.sharedPreferences = getApplicationContext().getSharedPreferences(getPackageName(), 0);
        this.listView = (ListView) findViewById(R.id.currency);
        this.dateview = (TextView) findViewById(R.id.date);
        if (internet_connection()) {
            Downloadtask download = new Downloadtask();
            download.execute(getString(R.string.apiLinkCurrencyConverter));
        } else {
            final Dialog dialog2 = new Dialog(this);
            dialog2.setContentView(R.layout.nointernet);
            dialog2.setCancelable(true);
            dialog2.getWindow().setBackgroundDrawable(new ColorDrawable(0));
            dialog2.setCanceledOnTouchOutside(true);
            dialog2.getWindow().setLayout(-2, -2);
            dialog2.getWindow().getAttributes().windowAnimations = 16973826;
            dialog2.show();
            Button button = (Button) dialog2.findViewById(R.id.cancel);
            button.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    dialog2.dismiss();
                }
            });
            this.euroto[0] = this.sharedPreferences.getFloat("euroto0", 0.0f);
            this.euroto[1] = this.sharedPreferences.getFloat("euroto1", 0.0f);
            this.euroto[2] = this.sharedPreferences.getFloat("euroto2", 0.0f);
            this.euroto[3] = this.sharedPreferences.getFloat("euroto3", 0.0f);
            this.euroto[4] = this.sharedPreferences.getFloat("euroto4", 0.0f);
            this.euroto[5] = this.sharedPreferences.getFloat("euroto5", 0.0f);
            Toast.makeText(this, String.valueOf(this.euroto[4]), Toast.LENGTH_LONG).show();
        }
        this.units.add("DOLLAR ($)");
        this.units.add("INR (₹)");
        this.units.add("EURO (€)");
        this.units.add("DIHRAM (د.إ)");
        this.units.add("YEN (¥)");
        this.units.add("POUND (£)");
        for (int i = 0; i <= 5; i++) {
            this.displayanswer[i] = 0.0d;
        }
        for (int i2 = 0; i2 <= 5; i2++) {
            String[] strArr = this.finalanswer;
            strArr[i2] = "0  " + this.units.get(i2);
        }
        ArrayAdapter arrayAdapter = new ArrayAdapter(this, (int) R.layout.align_right, this.finalanswer);
        this.adapter = arrayAdapter;
        this.listView.setAdapter((ListAdapter) arrayAdapter);
        ArrayAdapter<String> dataAdapter = new ArrayAdapter<>(this, 17367048, this.units);
        dataAdapter.setDropDownViewResource(17367049);
        this.spiner.setAdapter((SpinnerAdapter) dataAdapter);
        this.spiner.setOnItemSelectedListener(this);
    }

    @Override
    public void onBackPressed() {

        super.onBackPressed();

    }


}
