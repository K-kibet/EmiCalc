package com.kernelapps.emicalc.Calculations;

import android.content.ActivityNotFoundException;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.pdf.PdfDocument;
import android.net.Uri;
import android.os.Bundle;
import android.os.Environment;
import android.util.Log;
import android.view.View;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.view.inputmethod.InputMethodManager;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.core.content.FileProvider;

import com.kernelapps.emicalc.Constants;
import com.kernelapps.emicalc.PrasingTheDouble;
import com.kernelapps.emicalc.R;
import com.kernelapps.emicalc.ads.AdsManager;


import java.io.File;
import java.io.FileOutputStream;
import java.math.BigDecimal;
import java.math.RoundingMode;
import java.util.Calendar;


public class loaneligibility extends AppCompatActivity {
    TextView Emiofloan;
    EditText Otheremi;


    Bitmap bmp;
    double eligbleloan;
    String eligibility;
    TextView eligible;
    double eligibleemi;
    TextView emielgible;
    double foir;
    SharedPreferences frontshare;
    double givenemi;
    RadioGroup group;
    double income;

    double interest;
    EditText interestrate;
    boolean isCalculated;
    double loanamout;
    EditText loanaomunt;
    TextView loaneligible;


    EditText monthlyincome;

    double preemi;

    RadioButton radioButton;

    Bitmap scalebmp;
    LinearLayout showeligible;
    LinearLayout showemi;
    double tenure;
    EditText time;

    public void calculate(View view) {
        InputMethodManager imm = (InputMethodManager) getSystemService(Context.INPUT_METHOD_SERVICE);
        imm.hideSoftInputFromWindow(view.getWindowToken(), 0);
        if (this.monthlyincome.getText().toString().isEmpty() || this.interestrate.getText().toString().isEmpty() || this.time.getText().toString().isEmpty()) {
            Toast.makeText(this, "Enter the * value", Toast.LENGTH_SHORT).show();
            return;
        }
        this.tenure = Double.parseDouble(this.time.getText().toString());
        int selectedid = this.group.getCheckedRadioButtonId();
        RadioButton radioButton = (RadioButton) findViewById(selectedid);
        this.radioButton = radioButton;
        if (radioButton.getText().toString().equals("year")) {
            this.tenure = Double.parseDouble(this.time.getText().toString()) * 12.0d;
        } else {
            this.tenure = Double.parseDouble(this.time.getText().toString());
        }
        if (this.tenure <= 360.0d && Double.parseDouble(this.interestrate.getText().toString()) <= 50.0d && Double.parseDouble(this.monthlyincome.getText().toString()) >= 8000.0d) {
            if (this.Otheremi.getText().toString().isEmpty()) {
                this.preemi = 0.0d;
            }
            if (this.loanaomunt.getText().toString().isEmpty()) {
                this.loanamout = 0.0d;
            }
            calculation();
            return;
        }
        if (Double.parseDouble(this.monthlyincome.getText().toString()) < 8000.0d) {
            Toast.makeText(this, "Monthly Income should be greater than 8000Rs", Toast.LENGTH_SHORT).show();
        }
        if (this.tenure > 360.0d) {
            Toast.makeText(this, "tenure should be less than 30 years", Toast.LENGTH_SHORT).show();
        } else {
            Toast.makeText(this, "Interest rate should be less than 50%", Toast.LENGTH_SHORT).show();
        }
    }

    public void share(View view) {
        if (this.monthlyincome.getText().toString().isEmpty() || this.interestrate.getText().toString().isEmpty() || this.time.getText().toString().isEmpty()) {
            Toast.makeText(this, "Enter the mandatory value value", Toast.LENGTH_SHORT).show();
            return;
        }
        this.tenure = Double.parseDouble(this.time.getText().toString());
        int selectedid = this.group.getCheckedRadioButtonId();
        RadioButton radioButton = (RadioButton) findViewById(selectedid);
        this.radioButton = radioButton;
        if (radioButton.getText().toString().equals("year")) {
            this.tenure = Double.parseDouble(this.time.getText().toString()) * 12.0d;
        } else {
            this.tenure = Double.parseDouble(this.time.getText().toString());
        }
        if (this.tenure <= 30.0d && Double.parseDouble(this.interestrate.getText().toString()) <= 50.0d && Double.parseDouble(this.monthlyincome.getText().toString()) >= 8000.0d) {
            if (this.Otheremi.getText().toString().isEmpty()) {
                this.preemi = 0.0d;
            }
            if (this.loanaomunt.getText().toString().isEmpty()) {
                this.loanamout = 0.0d;
            }
            calculation();
            Intent intent = new Intent("android.intent.action.SEND");
            intent.setType("text/plain");
            intent.putExtra("android.intent.extra.SUBJECT", "EMI- A Calculator app");
            intent.putExtra("android.intent.extra.TEXT", "Loan Eligibility-\n \nGross Monthly Income: " + this.income + "\nTotal Monthly EMI: " + this.preemi + "\nLoan Amount: " + this.loanamout + "\nAnnual Interest Rate: " + (this.interest * 1200.0d) + "\nTenure: " + this.tenure + " months\n\nAre you eligible: " + this.eligibility + "\nEMI of Loan: " + this.givenemi + "\nLoan Amount you are eligible for : " + this.eligbleloan + "\nEMI you are eligible for : " + this.eligibleemi + "\n\n" + "https://play.google.com/store/apps/details?id=" + view.getContext().getPackageName());
            startActivity(Intent.createChooser(intent, "Share Using"));
            return;
        }
        if (Double.parseDouble(this.monthlyincome.getText().toString()) > 8000.0d) {
            Toast.makeText(this, "tenure should be less than 8000Rs", Toast.LENGTH_SHORT).show();
        }
        if (this.tenure > 30.0d) {
            Toast.makeText(this, "tenure should be less than 30 years", Toast.LENGTH_SHORT).show();
        } else {
            Toast.makeText(this, "Interest rate should be less than 50%", Toast.LENGTH_SHORT).show();
        }
    }

    public void pdf(View view) {
        if (this.monthlyincome.getText().toString().isEmpty() || this.interestrate.getText().toString().isEmpty() || this.time.getText().toString().isEmpty()) {
            Toast.makeText(this, "Enter the mandatory inputs", Toast.LENGTH_SHORT).show();
            return;
        }
        this.tenure = Double.parseDouble(this.time.getText().toString());
        int selectedid = this.group.getCheckedRadioButtonId();
        RadioButton radioButton = (RadioButton) findViewById(selectedid);
        this.radioButton = radioButton;
        if (radioButton.getText().toString().equals("year")) {
            this.tenure = Double.parseDouble(this.time.getText().toString()) * 12.0d;
        } else {
            this.tenure = Double.parseDouble(this.time.getText().toString());
        }
        if (this.tenure <= 360.0d && Double.parseDouble(this.interestrate.getText().toString()) <= 50.0d && Double.parseDouble(this.monthlyincome.getText().toString()) >= 8000.0d) {
            if (this.Otheremi.getText().toString().isEmpty()) {
                this.preemi = 0.0d;
            }
            if (this.loanaomunt.getText().toString().isEmpty()) {
                this.loanamout = 0.0d;
            }
            calculation();

            ActivityCompat.requestPermissions(this, new String[]{"android.permission.WRITE_EXTERNAL_STORAGE"}, 0);
            PdfDocument myPdf = new PdfDocument();
            Paint myPaint = new Paint();
            PdfDocument.PageInfo myPageInfo = new PdfDocument.PageInfo.Builder(1200, 2010, 1).create();
            PdfDocument.Page myPage = myPdf.startPage(myPageInfo);
            Canvas canvas = myPage.getCanvas();
            canvas.drawBitmap(this.scalebmp, 0.0f, 0.0f, myPaint);
            myPaint.setTextSize(33.0f);
            myPaint.setTextAlign(Paint.Align.CENTER);
            canvas.drawText(this.preemi + " " + Constants.CURRENCY_STORED, 900.0f, 600.0f, myPaint);
            canvas.drawText(this.loanamout + " " + Constants.CURRENCY_STORED, 900.0f, 725.0f, myPaint);
            StringBuilder sb = new StringBuilder();
            sb.append(this.interest * 1200.0d);
            sb.append(" %");
            canvas.drawText(sb.toString(), 900.0f, 875.0f, myPaint);
            canvas.drawText(this.tenure + " months", 900.0f, 1025.0f, myPaint);
            canvas.drawText(this.eligibility, 300.0f, 1325.0f, myPaint);
            canvas.drawText(this.givenemi + " " + Constants.CURRENCY_STORED, 900.0f, 1325.0f, myPaint);
            canvas.drawText(this.eligbleloan + " " + Constants.CURRENCY_STORED, 300.0f, 1660.0f, myPaint);
            canvas.drawText(this.eligibleemi + " " + Constants.CURRENCY_STORED, 900.0f, 1660.0f, myPaint);
            canvas.drawText(String.valueOf(this.income), 900.0f, 480.0f, myPaint);
            myPdf.finishPage(myPage);
            Calendar calendar = Calendar.getInstance();
            Long tsLong = System.currentTimeMillis()/1000;
            String ts = tsLong.toString();


            File externalStoragePublicDirectory = Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_DOWNLOADS);
            File file = new File(externalStoragePublicDirectory, "/LoanEligibility_" + ts + ".pdf");
            try {
                FileOutputStream out = new FileOutputStream(file);
                myPdf.writeTo(out);
                out.flush();
                out.close();
                myPdf.writeTo(new FileOutputStream(file));
            } catch (Exception e) {
                Log.e("PDF ISSUE", e.getMessage());
            }
            myPdf.close();
            if (file.exists()) {
                Intent intent1 = new Intent("android.intent.action.VIEW");
                Uri uri = FileProvider.getUriForFile(this, getApplicationContext().getPackageName() + ".provider", file);
                intent1.setDataAndType(uri, "application/pdf");
                intent1.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                intent1.addFlags(Intent.FLAG_GRANT_READ_URI_PERMISSION);
                try {
                    startActivity(intent1);
                    return;
                } catch (ActivityNotFoundException e2) {
                    Toast.makeText(this, "Download", Toast.LENGTH_SHORT).show();
                    return;
                }
            }
            return;
        }
        if (Double.parseDouble(this.monthlyincome.getText().toString()) < 8000.0d) {
            Toast.makeText(this, "income should be greater than 8000Rs", Toast.LENGTH_SHORT).show();
        }
        if (this.tenure > 30.0d) {
            Toast.makeText(this, "tenure should be less than 30 years", Toast.LENGTH_SHORT).show();
        } else {
            Toast.makeText(this, "Interest rate should be less than 50%", Toast.LENGTH_SHORT).show();
        }
    }

    public void calculation() {
        this.income = Double.parseDouble(this.monthlyincome.getText().toString());
        double parseDouble = Double.parseDouble(this.interestrate.getText().toString());
        this.interest = parseDouble;
        this.interest = parseDouble / 1200.0d;
        this.preemi = Double.parseDouble(this.Otheremi.getText().toString());
        this.loanamout = Double.parseDouble(this.loanaomunt.getText().toString());
        double d = this.income;
        if (d >= 8000.0d && d < 10000.0d) {
            this.foir = 0.35d;
        } else {
            double d2 = this.income;
            if (d2 >= 10000.0d && d2 < 25000.0d) {
                this.foir = 0.4d;
            } else {
                double d3 = this.income;
                if (d3 >= 25000.0d && d3 < 50000.0d) {
                    this.foir = 0.45d;
                } else {
                    double d4 = this.income;
                    if (d4 >= 50000.0d && d4 <= 100000.0d) {
                        this.foir = 0.5d;
                    } else if (this.income > 100000.0d) {
                        this.foir = 0.55d;
                    }
                }
            }
        }
        double d5 = (this.foir * this.income) - this.preemi;
        this.eligibleemi = d5;
        double d6 = this.tenure;
        double total = d5 * d6;
        this.eligbleloan = ((1.0d - Math.pow(this.interest + 1.0d, -d6)) * total) / (this.tenure * this.interest);
        if (!this.loanaomunt.getText().toString().isEmpty()) {
            this.showemi.setVisibility(View.VISIBLE);
            this.showeligible.setVisibility(View.VISIBLE);
            if (this.loanamout <= this.eligbleloan) {
                this.eligibility = "Yes";
                this.eligible.setTextColor(Color.parseColor("#00C853"));
            } else {
                this.eligibility = "No";
                this.eligible.setTextColor(Color.parseColor("#ffcc0000"));
            }
            double p = Math.pow(this.interest + 1.0d, this.tenure);
            this.givenemi = ((this.loanamout * this.interest) * p) / (p - 1.0d);
            this.eligible.setText(this.eligibility);
            double d7 = this.givenemi;
            if (((int) d7) == d7) {
                int l = (int) d7;
                this.Emiofloan.setText(String.valueOf(l));
            } else {
                BigDecimal bd = new BigDecimal(this.givenemi).setScale(1, RoundingMode.HALF_UP);
                double doubleValue = bd.doubleValue();
                this.givenemi = doubleValue;
                this.Emiofloan.setText(String.valueOf(doubleValue));
            }
            PrasingTheDouble prasingTheDouble = new PrasingTheDouble(this.givenemi);
            TextView textView = this.Emiofloan;
            textView.setText(prasingTheDouble.getaDouble() + " " + Constants.CURRENCY_STORED);
        } else {
            this.showemi.setVisibility(View.INVISIBLE);
            this.showeligible.setVisibility(View.INVISIBLE);
        }
        double d8 = this.eligbleloan;
        if (((int) d8) == d8) {
            int l2 = (int) d8;
            this.loaneligible.setText(String.valueOf(l2));
        } else {
            BigDecimal bd2 = new BigDecimal(this.eligbleloan).setScale(1, RoundingMode.HALF_UP);
            double doubleValue2 = bd2.doubleValue();
            this.eligbleloan = doubleValue2;
            this.loaneligible.setText(String.valueOf(doubleValue2));
        }
        PrasingTheDouble prasingTheDouble2 = new PrasingTheDouble(this.eligbleloan);
        TextView textView2 = this.loaneligible;
        textView2.setText(prasingTheDouble2.getaDouble() + " " + Constants.CURRENCY_STORED);
        double d9 = this.eligibleemi;
        if (((int) d9) == d9) {
            int l3 = (int) d9;
            this.emielgible.setText(String.valueOf(l3));
        } else {
            BigDecimal bd3 = new BigDecimal(this.eligibleemi).setScale(1, RoundingMode.HALF_UP);
            double doubleValue3 = bd3.doubleValue();
            this.eligibleemi = doubleValue3;
            this.emielgible.setText(String.valueOf(doubleValue3));
        }
        PrasingTheDouble prasingTheDouble3 = new PrasingTheDouble(this.eligibleemi);
        TextView textView3 = this.emielgible;
        textView3.setText(prasingTheDouble3.getaDouble() + " " + Constants.CURRENCY_STORED);
    }


    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_loaneligibility);

        AdsManager.INSTANCE.loadBanner(this, findViewById(R.id.banner));

        this.frontshare = getApplicationContext().getSharedPreferences(getPackageName(), 0);


        this.eligible = (TextView) findViewById(R.id.eligibility);
        this.Emiofloan = (TextView) findViewById(R.id.emiofloan);
        this.loaneligible = (TextView) findViewById(R.id.eligibleloan);
        this.emielgible = (TextView) findViewById(R.id.eligibleemi);
        this.monthlyincome = (EditText) findViewById(R.id.income);
        this.Otheremi = (EditText) findViewById(R.id.otheremi);
        this.loanaomunt = (EditText) findViewById(R.id.loan);
        this.interestrate = (EditText) findViewById(R.id.interest);
        this.time = (EditText) findViewById(R.id.tenure);
        LinearLayout mainlayout = (LinearLayout) findViewById(R.id.mainlinear);
        Animation animation = AnimationUtils.loadAnimation(this, R.anim.slideup);
        mainlayout.startAnimation(animation);
        this.showeligible = (LinearLayout) findViewById(R.id.showeligible);
        this.showemi = (LinearLayout) findViewById(R.id.showemi);
        this.group = (RadioGroup) findViewById(R.id.togle);
        Bitmap decodeResource = BitmapFactory.decodeResource(getResources(), R.drawable.loaneligibilty);
        this.bmp = decodeResource;
        this.scalebmp = Bitmap.createScaledBitmap(decodeResource, 1200, 2010, false);
    }

    @Override
    public void onBackPressed() {

        super.onBackPressed();

    }


}
