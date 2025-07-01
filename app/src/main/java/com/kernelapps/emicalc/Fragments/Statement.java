package com.kernelapps.emicalc.Fragments;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RatingBar;
import android.widget.TextView;

import androidx.fragment.app.Fragment;

import com.kernelapps.emicalc.R;


import java.math.BigDecimal;
import java.math.RoundingMode;


public class Statement extends Fragment {
    Button Share;
    double actualinterest;

    double balance;
    Button cancel;
    String fmonth;
    int fomonth;
    int fyear;

    double interestinmonth;
    onMessageReadListner messageReadListner;
    double monthlyemi;


    double principal;
    double principia;
    int tdate;
    double time;
    String tmonth;
    int tomonth;
    double totalinterest;
    int tyear;


    public interface onMessageReadListner {
        void onMessage();
    }

    public Statement(double period, double inte, double princi, double emi, double totalint, int todate, int tomonth, int toyear) {
        this.time = period;
        this.interestinmonth = inte;
        this.principal = princi;
        this.principia = princi;
        this.monthlyemi = emi;
        this.totalinterest = totalint;
        this.tdate = todate;
        this.tomonth = tomonth;
        this.tyear = toyear;
        switch (tomonth) {
            case 1:
                this.tmonth = "Jan";
                break;
            case 2:
                this.tmonth = "Feb";
                break;
            case 3:
                this.tmonth = "Mar";
                break;
            case 4:
                this.tmonth = "April";
                break;
            case 5:
                this.tmonth = "May";
                break;
            case 6:
                this.tmonth = "Jun";
                break;
            case 7:
                this.tmonth = "July";
                break;
            case 8:
                this.tmonth = "Aug";
                break;
            case 9:
                this.tmonth = "Sep";
                break;
            case 10:
                this.tmonth = "Oct";
                break;
            case 11:
                this.tmonth = "Nov";
                break;
            case 12:
                this.tmonth = "Dec";
                break;
        }
        double d = tomonth;
        Double.isNaN(d);
        int i = (int) (d + period);
        this.fomonth = i;
        int index = (i - 1) / 12;
        this.fyear = toyear + index;
        int i2 = i - (index * 12);
        this.fomonth = i2;
        switch (i2) {
            case 1:
                this.fmonth = "Jan";
                return;
            case 2:
                this.fmonth = "Feb";
                return;
            case 3:
                this.fmonth = "Mar";
                return;
            case 4:
                this.fmonth = "April";
                return;
            case 5:
                this.fmonth = "May";
                return;
            case 6:
                this.fmonth = "Jun";
                return;
            case 7:
                this.fmonth = "July";
                return;
            case 8:
                this.fmonth = "Aug";
                return;
            case 9:
                this.fmonth = "Sep";
                return;
            case 10:
                this.fmonth = "Oct";
                return;
            case 11:
                this.fmonth = "Nov";
                return;
            case 12:
                this.fmonth = "Dec";
                return;
            default:
                return;
        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        final View v = inflater.inflate(R.layout.statistics, container, false);
        this.cancel = (Button) v.findViewById(R.id.cancel_action);
        this.Share = (Button) v.findViewById(R.id.Share);
        LinearLayout month = (LinearLayout) v.findViewById(R.id.months);
        LinearLayout interset = (LinearLayout) v.findViewById(R.id.Interest);
        LinearLayout balan = (LinearLayout) v.findViewById(R.id.Balance);
        LinearLayout princip = (LinearLayout) v.findViewById(R.id.principal);


        int i = 1;
        while (i <= this.time) {
            TextView months = new TextView(getContext());
            months.setLayoutParams(new ViewGroup.LayoutParams(-1, 60));
            months.setText(String.valueOf(i));
            months.setGravity(17);
            month.addView(months);
            this.actualinterest = this.interestinmonth * this.principal;
            TextView ainterest = new TextView(getContext());
            ainterest.setLayoutParams(new ViewGroup.LayoutParams(-1, 60));
            ainterest.setGravity(17);
            interset.addView(ainterest);
            LinearLayout month2 = month;
            this.balance = this.monthlyemi - this.actualinterest;
            TextView pr = new TextView(getContext());
            pr.setLayoutParams(new ViewGroup.LayoutParams(-1, 60));
            pr.setGravity(17);
            princip.addView(pr);
            this.principal -= this.balance;
            TextView ba = new TextView(getContext());
            ba.setLayoutParams(new ViewGroup.LayoutParams(-1, 60));
            ba.setGravity(17);
            balan.addView(ba);
            if (i % 2 == 0) {
                months.setBackgroundColor(Color.parseColor("#F1DED0"));
                ba.setBackgroundColor(Color.parseColor("#F1DED0"));
                pr.setBackgroundColor(Color.parseColor("#F1DED0"));
                ainterest.setBackgroundColor(Color.parseColor("#F1DED0"));
            } else {
                months.setBackgroundColor(Color.parseColor("#F8F4F4"));
                ba.setBackgroundColor(Color.parseColor("#F8F4F4"));
                pr.setBackgroundColor(Color.parseColor("#F8F4F4"));
                ainterest.setBackgroundColor(Color.parseColor("#F8F4F4"));
            }
            double d = this.actualinterest;
            LinearLayout interset2 = interset;
            LinearLayout balan2 = balan;
            if (d == ((int) d)) {
                ainterest.setText(String.valueOf(Integer.valueOf((int) d)));
            } else {
                BigDecimal bd = new BigDecimal(this.actualinterest).setScale(1, RoundingMode.HALF_UP);
                double doubleValue = bd.doubleValue();
                this.actualinterest = doubleValue;
                ainterest.setText(String.valueOf(doubleValue));
            }
            double d2 = this.balance;
            if (d2 == ((int) d2)) {
                pr.setText(String.valueOf(Integer.valueOf((int) d2)));
            } else {
                BigDecimal bd2 = new BigDecimal(this.balance).setScale(1, RoundingMode.HALF_UP);
                double doubleValue2 = bd2.doubleValue();
                this.balance = doubleValue2;
                pr.setText(String.valueOf(doubleValue2));
            }
            double d3 = this.principal;
            if (d3 == ((int) d3)) {
                ba.setText(String.valueOf(Integer.valueOf((int) d3)));
            } else {
                BigDecimal bd3 = new BigDecimal(this.principal).setScale(1, RoundingMode.HALF_UP);
                double doubleValue3 = bd3.doubleValue();
                this.principal = doubleValue3;
                ba.setText(String.valueOf(doubleValue3));
            }
            i++;
            month = month2;
            interset = interset2;
            balan = balan2;
        }
        this.cancel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Statement.this.messageReadListner.onMessage();
                Statement.this.getFragmentManager().popBackStack();
            }
        });
        this.Share.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent("android.intent.action.SEND");
                intent.setType("text/plain");
                intent.putExtra("android.intent.extra.SUBJECT", "EMI- A Financial Calculator");
                intent.putExtra("android.intent.extra.TEXT", "EMI Details-\n \nPrincipal Loan Amount: " + Statement.this.principia + "\nLoan term: " + Statement.this.time + "\nFirst EMI at: " + Statement.this.tdate + " " + Statement.this.tmonth + " " + Statement.this.tyear + "\n\nMonthly EMI: " + Statement.this.monthlyemi + "\nTotal Interest: " + Statement.this.totalinterest + "\nTotal payment: " + (Statement.this.totalinterest + Statement.this.principal) + "\nLast Loan Date: " + Statement.this.tdate + " " + Statement.this.fmonth + " " + Statement.this.fyear + "\n\nCalculate by EMI\n" + "https://play.google.com/store/apps/details?id=" + view.getContext().getPackageName());
                Statement.this.startActivity(Intent.createChooser(intent, "Share Using"));
            }
        });
        return v;
    }

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        Activity activity = (Activity) context;
        this.messageReadListner = (onMessageReadListner) activity;
    }
}
