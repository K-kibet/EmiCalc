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
import android.view.inputmethod.InputMethodManager;
import android.widget.EditText;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.core.content.FileProvider;

import com.kernelapps.emicalc.R;
import com.kernelapps.emicalc.Constants;
import com.kernelapps.emicalc.PrasingTheDouble;
import com.kernelapps.emicalc.ads.AdsManager;


import java.io.File;
import java.io.FileOutputStream;
import java.math.BigDecimal;
import java.math.RoundingMode;
import java.util.Calendar;

public class compareloan extends AppCompatActivity {

    double amount1;
    double amount2;

    Bitmap bmp;
    TextView difEmi;
    TextView difInter;
    TextView difTotal;
    double diffEmiVal;
    double diffInterest;
    double diffPayment;
    Bitmap dusrabmp;
    TextView emi1;
    TextView emi2;
    double emiVal1;
    double emiVal2;
    SharedPreferences frontshare;
    RadioGroup group;

    TextView interest1;
    TextView interest2;
    boolean isCalculated;



    EditText principal1;
    EditText principal2;
    RadioButton radioButton;
    EditText rate1;
    EditText rate2;
    double rateVal1;
    double rateVal2;
    Bitmap scalebmp;
    Bitmap scaledusrabmp;
    double tenure1;
    double tenure2;
    EditText time1;
    EditText time2;
    TextView total1;
    TextView total2;
    double totalInterest1;
    double totalInterest2;
    double totalPayment1;
    double totalPayment2;

    public void calculate(View view) {
        InputMethodManager imm = (InputMethodManager) getSystemService(Context.INPUT_METHOD_SERVICE);
        imm.hideSoftInputFromWindow(view.getWindowToken(), 0);
        if (this.principal1.getText().toString().isEmpty() || this.principal2.getText().toString().isEmpty() || this.rate1.getText().toString().isEmpty() || this.rate2.getText().toString().isEmpty() || this.time1.getText().toString().isEmpty() || this.time2.getText().toString().isEmpty()) {
            Toast.makeText(this, "Enter the * value", Toast.LENGTH_SHORT).show();
            return;
        }
        this.tenure1 = Double.parseDouble(this.time1.getText().toString());
        this.tenure2 = Double.parseDouble(this.time2.getText().toString());
        int selectedid = this.group.getCheckedRadioButtonId();
        RadioButton radioButton = findViewById(selectedid);
        this.radioButton = radioButton;
        if (radioButton.getText().toString().equals("year")) {
            this.tenure1 = Double.parseDouble(this.time1.getText().toString()) * 12.0d;
            this.tenure2 = Double.parseDouble(this.time2.getText().toString()) * 12.0d;
        } else {
            this.tenure1 = Double.parseDouble(this.time1.getText().toString());
            this.tenure2 = Double.parseDouble(this.time2.getText().toString());
        }
        if (this.tenure1 <= 30.0d || this.tenure2 <= 30.0d || Double.parseDouble(this.rate1.getText().toString()) <= 50.0d || Double.parseDouble(this.rate2.getText().toString()) <= 50.0d) {
            calculation();
        } else if (this.tenure1 > 30.0d || this.tenure2 > 30.0d) {
            Toast.makeText(this, "tenure should be less than 30 years", Toast.LENGTH_SHORT).show();
        } else {
            Toast.makeText(this, "Interest rate should be less than 50%", Toast.LENGTH_SHORT).show();
        }
    }

    public void calculation() {
        this.isCalculated = true;
        this.amount1 = Double.parseDouble(this.principal1.getText().toString());
        this.amount2 = Double.parseDouble(this.principal2.getText().toString());
        this.rateVal1 = Double.parseDouble(this.rate1.getText().toString()) / 1200.0d;
        this.rateVal2 = Double.parseDouble(this.rate2.getText().toString()) / 1200.0d;
        double d = this.amount1;
        double d2 = this.rateVal1;
        double pmt = (d * d2) / (1.0d - Math.pow(d2 + 1.0d, -this.tenure1));
        double d3 = this.tenure1;
        double d4 = this.amount1;
        double d5 = (pmt * d3) - d4;
        this.totalInterest1 = d5;
        this.totalPayment1 = d5 + d4;
        double p = Math.pow(this.rateVal1 + 1.0d, d3);
        this.emiVal1 = ((this.amount1 * this.rateVal1) * p) / (p - 1.0d);
        double d6 = this.amount2;
        double d7 = this.rateVal2;
        double pmt2 = (d6 * d7) / (1.0d - Math.pow(d7 + 1.0d, -this.tenure2));
        double pmt3 = this.tenure2;
        double d8 = this.amount2;
        double d9 = (pmt2 * pmt3) - d8;
        this.totalInterest2 = d9;
        this.totalPayment2 = d9 + d8;
        double p2 = Math.pow(this.rateVal2 + 1.0d, pmt3);
        this.emiVal2 = ((this.amount2 * this.rateVal2) * p2) / (p2 - 1.0d);
        double d10 = this.totalPayment2;
        if (((int) d10) == d10) {
            int emi = (int) d10;
            this.total2.setText(String.valueOf(emi));
        } else {
            BigDecimal bd = new BigDecimal(this.totalPayment2).setScale(1, RoundingMode.HALF_UP);
            this.totalPayment2 = bd.doubleValue();
            TextView textView = this.total2;
            textView.setText(this.totalPayment2 + " " + Constants.CURRENCY_STORED);
        }
        double d11 = this.totalPayment1;
        if (((int) d11) == d11) {
            int emi2 = (int) d11;
            this.total1.setText(String.valueOf(emi2));
        } else {
            BigDecimal bd2 = new BigDecimal(this.totalPayment1).setScale(1, RoundingMode.HALF_UP);
            this.totalPayment1 = bd2.doubleValue();
            TextView textView2 = this.total1;
            textView2.setText(this.totalPayment1 + " " + Constants.CURRENCY_STORED);
        }
        PrasingTheDouble prasingTheDouble = new PrasingTheDouble(this.totalPayment1);
        TextView textView3 = this.total1;
        textView3.setText(prasingTheDouble.getaDouble() + " " + Constants.CURRENCY_STORED);
        PrasingTheDouble prasingTheDouble2 = new PrasingTheDouble(this.totalPayment2);
        TextView textView4 = this.total2;
        textView4.setText(prasingTheDouble2.getaDouble() + " " + Constants.CURRENCY_STORED);
        double d12 = this.totalPayment1;
        double d13 = this.totalPayment2;
        if (d12 > d13) {
            this.total1.setTextColor(Color.parseColor("#26CA6A"));
            this.total2.setTextColor(Color.parseColor("#F80606"));
            this.diffPayment = this.totalPayment1 - this.totalPayment2;
        } else if (d12 == d13) {
            this.total1.setTextColor(Color.parseColor("#FF6D00"));
            this.total2.setTextColor(Color.parseColor("#FF6D00"));
            this.diffPayment = 0.0d;
        } else {
            this.total2.setTextColor(Color.parseColor("#26CA6A"));
            this.total1.setTextColor(Color.parseColor("#F80606"));
            this.diffPayment = this.totalPayment2 - this.totalPayment1;
        }
        BigDecimal bd3 = new BigDecimal(this.diffPayment).setScale(1, RoundingMode.HALF_UP);
        this.diffPayment = bd3.doubleValue();
        TextView textView5 = this.difTotal;
        textView5.setText("Difference:  " + this.diffPayment + " " + Constants.CURRENCY_STORED);
        double d14 = this.totalInterest2;
        if (((int) d14) == d14) {
            int emi3 = (int) d14;
            this.interest2.setText(String.valueOf(emi3));
        } else {
            BigDecimal bd4 = new BigDecimal(this.totalInterest2).setScale(1, RoundingMode.HALF_UP);
            this.totalInterest2 = bd4.doubleValue();
            TextView textView6 = this.interest2;
            textView6.setText(this.totalInterest2 + " " + Constants.CURRENCY_STORED);
        }
        PrasingTheDouble prasingTheDouble3 = new PrasingTheDouble(this.totalInterest2);
        TextView textView7 = this.interest2;
        textView7.setText(prasingTheDouble3.getaDouble() + " " + Constants.CURRENCY_STORED);
        double d15 = this.totalInterest1;
        if (((int) d15) == d15) {
            int emi4 = (int) d15;
            this.interest1.setText(String.valueOf(emi4));
        } else {
            BigDecimal bd5 = new BigDecimal(this.totalInterest1).setScale(1, RoundingMode.HALF_UP);
            this.totalInterest1 = bd5.doubleValue();
            TextView textView8 = this.interest1;
            textView8.setText(this.totalInterest1 + " " + Constants.CURRENCY_STORED);
        }
        PrasingTheDouble prasingTheDouble4 = new PrasingTheDouble(this.totalInterest1);
        TextView textView9 = this.interest1;
        textView9.setText(prasingTheDouble4.getaDouble() + " " + Constants.CURRENCY_STORED);
        double d16 = this.totalInterest1;
        double d17 = this.totalInterest2;
        if (d16 > d17) {
            this.diffInterest = d16 - d17;
            this.interest1.setTextColor(Color.parseColor("#26CA6A"));
            this.interest2.setTextColor(Color.parseColor("#F80606"));
        } else if (d16 == d17) {
            this.diffInterest = 0.0d;
            this.interest1.setTextColor(Color.parseColor("#FF6D00"));
            this.interest2.setTextColor(Color.parseColor("#FF6D00"));
        } else {
            this.diffInterest = d17 - d16;
            this.interest2.setTextColor(Color.parseColor("#26CA6A"));
            this.interest1.setTextColor(Color.parseColor("#F80606"));
        }
        BigDecimal bd6 = new BigDecimal(this.diffInterest).setScale(1, RoundingMode.HALF_UP);
        this.diffInterest = bd6.doubleValue();
        TextView textView10 = this.difInter;
        textView10.setText("Difference:  " + this.diffInterest + " " + Constants.CURRENCY_STORED);
        double d18 = this.emiVal2;
        if (((int) d18) == d18) {
            int emi5 = (int) d18;
            this.emi2.setText(String.valueOf(emi5));
        } else {
            BigDecimal bd7 = new BigDecimal(this.emiVal2).setScale(1, RoundingMode.HALF_UP);
            this.emiVal2 = bd7.doubleValue();
            TextView textView11 = this.emi2;
            textView11.setText(this.emiVal2 + " " + Constants.CURRENCY_STORED);
        }
        PrasingTheDouble prasingTheDouble5 = new PrasingTheDouble(this.emiVal2);
        TextView textView12 = this.emi2;
        textView12.setText(prasingTheDouble5.getaDouble() + " " + Constants.CURRENCY_STORED);
        double d19 = this.emiVal1;
        if (((int) d19) == d19) {
            int emi6 = (int) d19;
            this.emi1.setText(String.valueOf(emi6));
        } else {
            BigDecimal bd8 = new BigDecimal(this.emiVal1).setScale(1, RoundingMode.HALF_UP);
            this.emiVal1 = bd8.doubleValue();
            TextView textView13 = this.emi1;
            textView13.setText(this.emiVal1 + " " + Constants.CURRENCY_STORED);
        }
        PrasingTheDouble prasingTheDouble6 = new PrasingTheDouble(this.emiVal1);
        TextView textView14 = this.emi1;
        textView14.setText(prasingTheDouble6.getaDouble() + " " + Constants.CURRENCY_STORED);
        double d20 = this.emiVal1;
        double d21 = this.emiVal2;
        if (d20 > d21) {
            this.diffEmiVal = d20 - d21;
            this.emi1.setTextColor(Color.parseColor("#26CA6A"));
            this.emi2.setTextColor(Color.parseColor("#F80606"));
        } else if (d20 == d21) {
            this.diffEmiVal = 0.0d;
            this.emi1.setTextColor(Color.parseColor("#FF6D00"));
            this.emi2.setTextColor(Color.parseColor("#FF6D00"));
        } else {
            this.diffEmiVal = d21 - d20;
            this.emi2.setTextColor(Color.parseColor("#26CA6A"));
            this.emi1.setTextColor(Color.parseColor("#F80606"));
        }
        BigDecimal bd9 = new BigDecimal(this.diffEmiVal).setScale(1, RoundingMode.HALF_UP);
        this.diffEmiVal = bd9.doubleValue();
        TextView textView15 = this.difEmi;
        textView15.setText("Difference:  " + this.diffEmiVal + " " + Constants.CURRENCY_STORED);
    }

    public void pdf(View view) {
        InputMethodManager imm = (InputMethodManager) getSystemService(Context.INPUT_METHOD_SERVICE);
        imm.hideSoftInputFromWindow(view.getWindowToken(), 0);
        if (!this.principal1.getText().toString().isEmpty() && !this.principal2.getText().toString().isEmpty() && !this.rate1.getText().toString().isEmpty() && !this.rate2.getText().toString().isEmpty() && !this.time1.getText().toString().isEmpty()) {
            if (!this.time2.getText().toString().isEmpty()) {
                this.tenure1 = Double.parseDouble(this.time1.getText().toString());
                this.tenure2 = Double.parseDouble(this.time2.getText().toString());
                int selectedid = this.group.getCheckedRadioButtonId();
                RadioButton radioButton = findViewById(selectedid);
                this.radioButton = radioButton;
                if (radioButton.getText().toString().equals("year")) {
                    this.tenure1 = Double.parseDouble(this.time1.getText().toString()) * 12.0d;
                    this.tenure2 = Double.parseDouble(this.time2.getText().toString()) * 12.0d;
                } else {
                    this.tenure1 = Double.parseDouble(this.time1.getText().toString());
                    this.tenure2 = Double.parseDouble(this.time2.getText().toString());
                }
                if (this.tenure1 <= 30.0d || this.tenure2 <= 30.0d || Double.parseDouble(this.rate1.getText().toString()) <= 50.0d || Double.parseDouble(this.rate2.getText().toString()) <= 50.0d) {
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
                    int selectedd = this.group.getCheckedRadioButtonId();
                    this.radioButton = findViewById(selectedd);
                    canvas.drawText(this.amount1 + " " + Constants.CURRENCY_STORED, 300.0f, 735.0f, myPaint);
                    canvas.drawText(this.amount2 + " " + Constants.CURRENCY_STORED, 900.0f, 735.0f, myPaint);
                    canvas.drawText((this.rateVal1 * 1200.0d) + " " + Constants.CURRENCY_STORED, 300.0f, 985.0f, myPaint);
                    canvas.drawText((this.rateVal2 * 1200.0d) + " " + Constants.CURRENCY_STORED, 900.0f, 985.0f, myPaint);
                    String sb = this.tenure1 +
                            " months";
                    canvas.drawText(sb, 300.0f, 1235.0f, myPaint);
                    canvas.drawText(this.tenure2 + " months", 900.0f, 1235.0f, myPaint);
                    canvas.drawText(this.emiVal1 + " " + Constants.CURRENCY_STORED, 300.0f, 1550.0f, myPaint);
                    canvas.drawText(this.emiVal2 + " " + Constants.CURRENCY_STORED, 900.0f, 1550.0f, myPaint);
                    canvas.drawText(String.valueOf(this.diffEmiVal), 750.0f, 1695.0f, myPaint);
                    myPdf.finishPage(myPage);
                    PdfDocument.PageInfo myPageInfo2 = new PdfDocument.PageInfo.Builder(1200, 2010, 2).create();
                    PdfDocument.Page myPage2 = myPdf.startPage(myPageInfo2);
                    Canvas canvas2 = myPage2.getCanvas();
                    canvas2.drawBitmap(this.scaledusrabmp, 0.0f, 0.0f, myPaint);
                    canvas2.drawText(this.totalInterest1 + " " + Constants.CURRENCY_STORED, 300.0f, 340.0f, myPaint);
                    canvas2.drawText(this.totalInterest2 + " " + Constants.CURRENCY_STORED, 900.0f, 340.0f, myPaint);
                    canvas2.drawText(String.valueOf(this.diffInterest), 750.0f, 480.0f, myPaint);
                    canvas2.drawText(this.totalPayment1 + " " + Constants.CURRENCY_STORED, 300.0f, 825.0f, myPaint);
                    canvas2.drawText(this.totalPayment2 + " " + Constants.CURRENCY_STORED, 900.0f, 825.0f, myPaint);
                    canvas2.drawText(String.valueOf(this.diffInterest), 750.0f, 960.0f, myPaint);
                    myPdf.finishPage(myPage2);
                    Calendar calendar = Calendar.getInstance();
                    File externalStoragePublicDirectory = Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_DOWNLOADS);

                    Long tsLong = System.currentTimeMillis()/1000;
                    String ts = tsLong.toString();

                    File file = new File(externalStoragePublicDirectory, "/CompareLoan_" + ts + ".pdf");
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
                } else if (this.tenure1 > 30.0d || this.tenure2 > 30.0d) {
                    Toast.makeText(this, "tenure should be less than 30 years", Toast.LENGTH_SHORT).show();
                    return;
                } else {
                    Toast.makeText(this, "Interest rate should be less than 50%", Toast.LENGTH_SHORT).show();
                    return;
                }
            }
        }
        Toast.makeText(this, "Enter the * value", Toast.LENGTH_SHORT).show();
    }


    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_compareloan);
        AdsManager.INSTANCE.loadBanner(this, findViewById(R.id.banner));


        this.principal1 = findViewById(R.id.principalamoount1);
        this.principal2 = findViewById(R.id.principalamoount2);
        this.rate1 = findViewById(R.id.interestamoount1);
        this.rate2 = findViewById(R.id.interestamoount2);
        this.time1 = findViewById(R.id.tenure1);
        this.time2 = findViewById(R.id.tenure2);
        this.emi1 = findViewById(R.id.emi1);
        this.emi2 = findViewById(R.id.emi2);
        this.interest1 = findViewById(R.id.totalinterest1);
        this.interest2 = findViewById(R.id.totalinterest2);
        this.total1 = findViewById(R.id.totalamount1);
        this.total2 = findViewById(R.id.totalamount2);
        this.difEmi = findViewById(R.id.emidifference);
        this.difInter = findViewById(R.id.interestdifference);
        this.difTotal = findViewById(R.id.paymentdifference);
        this.group = findViewById(R.id.togle);
        Bitmap decodeResource = BitmapFactory.decodeResource(getResources(), R.drawable.compareone);
        this.bmp = decodeResource;
        this.scalebmp = Bitmap.createScaledBitmap(decodeResource, 1200, 2010, false);
        Bitmap decodeResource2 = BitmapFactory.decodeResource(getResources(), R.drawable.comparetwo);
        this.dusrabmp = decodeResource2;
        this.scaledusrabmp = Bitmap.createScaledBitmap(decodeResource2, 1200, 2010, false);

        this.frontshare = getApplicationContext().getSharedPreferences(getPackageName(), 0);
    }

    @Override
    public void onBackPressed() {

            super.onBackPressed();
    }


}
