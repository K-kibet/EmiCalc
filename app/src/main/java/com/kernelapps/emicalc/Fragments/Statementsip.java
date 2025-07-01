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


public class Statementsip extends Fragment {
    Button Share;
    double actualinterest;

    double balance;
    Button cancel;
    String fmonth;
    int fomonth;
    int fyear;

    double interestinmonth;
    double maturityvalue;
    onMessageReadListner messageReadListner;


    double principal;
    double principia;
    int tdate;
    double time;
    TextView title;
    String tmonth;
    int tomonth;
    double totalinterest;
    int tyear;
    String what;


    public interface onMessageReadListner {
        void onMessage();
    }

    public Statementsip(double with, double period, double inte, double princi, double maurity, double totalint, int todate, int tomonth, int toyear, String which) {
        this.time = period;
        this.what = which;
        this.interestinmonth = inte;
        this.principal = princi;
        if (which == "SIP" || which == "RD") {
            double d = this.principal;
            this.principia = d;
            this.balance = d;
        } else if (which == "SWP") {
            this.principia = 0.0d - with;
            this.balance = princi;
        } else {
            this.balance = princi;
            this.principia = 0.0d;
        }
        this.maturityvalue = maurity;
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
        double d2 = tomonth;
        Double.isNaN(d2);
        int i = (int) (d2 + period);
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
        String str;
        final View v = inflater.inflate(R.layout.statementsip, container, false);
        this.cancel = (Button) v.findViewById(R.id.cancel_action);
        this.Share = (Button) v.findViewById(R.id.Share);
        TextView textView = (TextView) v.findViewById(R.id.which);
        this.title = textView;
        textView.setText(this.what);
        LinearLayout month = (LinearLayout) v.findViewById(R.id.months);
        LinearLayout balan = (LinearLayout) v.findViewById(R.id.Balance);
        LinearLayout interest = (LinearLayout) v.findViewById(R.id.interest);

        int i = 1;
        while (i <= this.time) {
            TextView months = new TextView(getContext());
            months.setLayoutParams(new ViewGroup.LayoutParams(-1, 60));
            months.setText(String.valueOf(i));
            months.setGravity(17);
            month.addView(months);
            this.actualinterest = this.interestinmonth * this.balance;
            TextView ainterest = new TextView(getContext());
            ainterest.setLayoutParams(new ViewGroup.LayoutParams(-1, 60));
            ainterest.setGravity(17);
            double d = this.actualinterest;
            LinearLayout month2 = month;
            if (d == ((int) d)) {
                int l = (int) d;
                ainterest.setText(String.valueOf(Integer.valueOf(l)));
            } else {
                BigDecimal bd = new BigDecimal(this.actualinterest).setScale(1, RoundingMode.HALF_UP);
                double actual = bd.doubleValue();
                ainterest.setText(String.valueOf(actual));
            }
            interest.addView(ainterest);
            if (i == this.time && ((str = this.what) == "SIP" || str == "RD")) {
                this.principia = 0.0d;
            }
            this.balance = this.balance + this.actualinterest + this.principia;
            TextView pr = new TextView(getContext());
            pr.setLayoutParams(new ViewGroup.LayoutParams(-1, 60));
            pr.setGravity(17);
            balan.addView(pr);
            double d2 = this.balance;
            if (d2 == ((int) d2)) {
                int l2 = ((int) d2) + ((int) this.principia);
                pr.setText(String.valueOf(Integer.valueOf(l2)));
            } else {
                BigDecimal bd2 = new BigDecimal(this.balance).setScale(1, RoundingMode.HALF_UP);
                double bal = bd2.doubleValue();
                pr.setText(String.valueOf(bal));
            }
            if (i % 2 == 0) {
                months.setBackgroundColor(Color.parseColor("#F1DED0"));
                pr.setBackgroundColor(Color.parseColor("#F1DED0"));
                ainterest.setBackgroundColor(Color.parseColor("#F1DED0"));
            } else {
                months.setBackgroundColor(Color.parseColor("#F8F4F4"));
                pr.setBackgroundColor(Color.parseColor("#F8F4F4"));
                ainterest.setBackgroundColor(Color.parseColor("#F8F4F4"));
            }
            i++;
            month = month2;
        }
        this.cancel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Statementsip.this.messageReadListner.onMessage();
                Statementsip.this.getFragmentManager().popBackStack();
            }
        });
        this.Share.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Double total;
                Intent intent = new Intent("android.intent.action.SEND");
                intent.setType("text/plain");
                if (Statementsip.this.what == "SIP" || Statementsip.this.what == "RD") {
                    total = Double.valueOf(Statementsip.this.principal * Statementsip.this.time);
                } else {
                    total = Double.valueOf(Statementsip.this.principal);
                }
                intent.putExtra("android.intent.extra.SUBJECT", "EMI- A Calculator app");
                intent.putExtra("android.intent.extra.TEXT", Statementsip.this.what + "Details-\n\nInvestment Amount : " + Statementsip.this.principal + "\nTenure : " + Statementsip.this.time + "months\nFirst " + Statementsip.this.what + ": " + Statementsip.this.tdate + " " + Statementsip.this.tmonth + " " + Statementsip.this.tyear + "\n\nTotal Investment Amount: " + total + "\nTotal Interest: " + Statementsip.this.totalinterest + "\nMaturity Value: " + (Statementsip.this.totalinterest + Statementsip.this.principal) + "\nMaturity Date: " + Statementsip.this.tdate + " " + Statementsip.this.fmonth + " " + Statementsip.this.fyear + "\n\nCalculate by EMI\n" + "https://play.google.com/store/apps/details?id=" + view.getContext().getPackageName());
                Statementsip.this.startActivity(Intent.createChooser(intent, "Share Using"));
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
