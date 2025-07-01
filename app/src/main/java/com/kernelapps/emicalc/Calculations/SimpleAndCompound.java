package com.kernelapps.emicalc.Calculations;

import android.annotation.SuppressLint;
import android.content.ActivityNotFoundException;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
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
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.RelativeLayout;
import android.widget.Spinner;
import android.widget.SpinnerAdapter;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.core.content.FileProvider;
import androidx.core.widget.NestedScrollView;

import com.kernelapps.emicalc.Constants;
import com.kernelapps.emicalc.PrasingTheDouble;
import com.kernelapps.emicalc.R;
import com.kernelapps.emicalc.ads.AdsManager;


import java.io.File;
import java.io.FileOutputStream;
import java.math.BigDecimal;
import java.math.RoundingMode;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;


public class SimpleAndCompound extends AppCompatActivity implements AdapterView.OnItemSelectedListener {
    double Tenure;


    Bitmap bmp;
    double expectedRate;
    SharedPreferences frontshare;
    RadioGroup group;

    TextView interest;
    double investmentAmount;
    EditText investmentEdit;
    boolean isCalculated;


    TextView maturity;
    double maturityValue;

    TextView netAmount;

    RadioButton radioButton;
    EditText rateEdit;

    Bitmap scalebmp;
    Spinner spiner;
    EditText time;
    double totalInterest;
    double totalInvestment;
    List<String> spinner = new ArrayList();
    String choosen = "Compound interest";

    public void calculate(View view) {
        InputMethodManager imm = (InputMethodManager) getSystemService(Context.INPUT_METHOD_SERVICE);
        imm.hideSoftInputFromWindow(view.getWindowToken(), 0);
        if (this.investmentEdit.getText().toString().isEmpty() || this.rateEdit.getText().toString().isEmpty() || this.time.getText().toString().isEmpty()) {
            Toast.makeText(getApplicationContext(), "Enter Inputs", Toast.LENGTH_SHORT).show();
            return;
        }
        this.Tenure = Double.parseDouble(this.time.getText().toString());
        int selectedid = this.group.getCheckedRadioButtonId();
        RadioButton radioButton = (RadioButton) findViewById(selectedid);
        this.radioButton = radioButton;
        if (radioButton.getText().toString().equals("year")) {
            this.Tenure = Double.parseDouble(this.time.getText().toString()) * 12.0d;
        } else {
            this.Tenure = Double.parseDouble(this.time.getText().toString());
        }
        if (this.Tenure <= 360.0d && Double.parseDouble(this.rateEdit.getText().toString()) <= 50.0d) {
            calculation();
        } else if (this.Tenure > 360.0d) {
            Toast.makeText(this, "Tenure should be less than 30 years", Toast.LENGTH_SHORT).show();
        } else {
            Toast.makeText(this, "interest rate should be less than 50%", Toast.LENGTH_SHORT).show();
        }
    }

    public void share(View view) {
        if (this.investmentEdit.getText().toString().isEmpty() || this.rateEdit.getText().toString().isEmpty() || this.time.getText().toString().isEmpty()) {
            Toast.makeText(getApplicationContext(), "Enter Inputs", Toast.LENGTH_SHORT).show();
            return;
        }
        this.Tenure = Double.parseDouble(this.time.getText().toString());
        int selectedid = this.group.getCheckedRadioButtonId();
        RadioButton radioButton = (RadioButton) findViewById(selectedid);
        this.radioButton = radioButton;
        if (radioButton.getText().toString().equals("year")) {
            this.Tenure = Double.parseDouble(this.time.getText().toString()) * 12.0d;
        } else {
            this.Tenure = Double.parseDouble(this.time.getText().toString());
        }
        if (this.Tenure <= 360.0d && Double.parseDouble(this.rateEdit.getText().toString()) <= 50.0d) {
            calculation();
            Intent intent = new Intent("android.intent.action.SEND");
            intent.setType("text/plain");
            intent.putExtra("android.intent.extra.SUBJECT", "EMI- A Calculator app");
            intent.putExtra("android.intent.extra.TEXT", "Simple & Compound interest Details-\n\ninvestmentEdit Amount : " + this.investmentAmount + "\nTenure : " + this.Tenure + "months\n\nTotal investmentEdit Amount: " + this.totalInvestment + "\ninterest Value: " + this.totalInterest + "\nmaturity Value: " + this.maturityValue + "\n\nCalculate by EMI\n" + "https://play.google.com/store/apps/details?id=" + view.getContext().getPackageName());
            startActivity(Intent.createChooser(intent, "Share Using"));
        } else if (this.Tenure > 360.0d) {
            Toast.makeText(this, "tenure should be less than 30 years", Toast.LENGTH_SHORT).show();
        } else {
            Toast.makeText(this, "interest rate should be less than 50%", Toast.LENGTH_SHORT).show();
        }
    }

    public void pdf(View view) {
        if (!this.investmentEdit.getText().toString().isEmpty() && !this.rateEdit.getText().toString().isEmpty() && !this.time.getText().toString().isEmpty()) {
            this.Tenure = Double.parseDouble(this.time.getText().toString());
            int selectedid = this.group.getCheckedRadioButtonId();
            RadioButton radioButton = (RadioButton) findViewById(selectedid);
            this.radioButton = radioButton;
            if (radioButton.getText().toString().equals("year")) {
                this.Tenure = Double.parseDouble(this.time.getText().toString()) * 12.0d;
            } else {
                this.Tenure = Double.parseDouble(this.time.getText().toString());
            }
            if (this.Tenure <= 360.0d && Double.parseDouble(this.rateEdit.getText().toString()) <= 50.0d) {
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
                this.radioButton = (RadioButton) findViewById(selectedd);
                canvas.drawText(this.investmentAmount + " " + Constants.CURRENCY_STORED, 900.0f, 575.0f, myPaint);
                StringBuilder sb = new StringBuilder();
                sb.append(this.expectedRate * 1200.0d);
                sb.append(" %");
                canvas.drawText(sb.toString(), 900.0f, 700.0f, myPaint);
                canvas.drawText(this.Tenure + "  months", 900.0f, 850.0f, myPaint);
                canvas.drawText(this.totalInvestment + " " + Constants.CURRENCY_STORED, 300.0f, 1150.0f, myPaint);
                canvas.drawText(this.totalInterest + " " + Constants.CURRENCY_STORED, 900.0f, 1150.0f, myPaint);
                canvas.drawText(this.maturityValue + " " + Constants.CURRENCY_STORED, 600.0f, 1450.0f, myPaint);
                canvas.drawText(this.choosen, 900.0f, 480.0f, myPaint);
                myPdf.finishPage(myPage);
                Calendar calendar = Calendar.getInstance();
                File externalStoragePublicDirectory = Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_DOWNLOADS);

                Long tsLong = System.currentTimeMillis()/1000;
                String ts = tsLong.toString();
                File file = new File(externalStoragePublicDirectory, "/Interest_" + ts + ".pdf");
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
            } else if (this.Tenure > 360.0d) {
                Toast.makeText(this, "tenure should be less than 30 years", Toast.LENGTH_SHORT).show();
                return;
            } else {
                Toast.makeText(this, "interest rate should be less than 50%", Toast.LENGTH_SHORT).show();
                return;
            }
        }
        Toast.makeText(getApplicationContext(), "Enter Inputs", Toast.LENGTH_SHORT).show();
    }


    public void calculation() {
        NestedScrollView scrollView = (NestedScrollView) findViewById(R.id.scroller);
        scrollView.smoothScrollTo(0, 800);
        this.investmentAmount = Double.parseDouble(this.investmentEdit.getText().toString());
        this.Tenure = Double.parseDouble(this.time.getText().toString());
        int selectedid = this.group.getCheckedRadioButtonId();
        RadioButton radioButton = (RadioButton) findViewById(selectedid);
        this.radioButton = radioButton;
        if (radioButton.getText().toString().equals("year")) {
            this.Tenure = Double.parseDouble(this.time.getText().toString()) * 12.0d;
        } else {
            this.Tenure = Double.parseDouble(this.time.getText().toString());
        }
        double parseDouble = Double.parseDouble(this.rateEdit.getText().toString());
        this.expectedRate = parseDouble;
        this.expectedRate = parseDouble / 1200.0d;
        if (this.choosen.equals("Compound interest")) {
            this.maturityValue = this.investmentAmount * Math.pow(this.expectedRate + 1.0d, this.Tenure);
        } else {
            double d = this.investmentAmount;
            this.maturityValue = d + (this.expectedRate * d * this.Tenure);
        }
        double d2 = this.investmentAmount;
        this.totalInvestment = d2;
        double d3 = this.maturityValue - d2;
        this.totalInterest = d3;
        if (((int) d3) == d3) {
            int t = (int) d3;
            this.interest.setText(String.valueOf(t));
        } else {
            BigDecimal bd = new BigDecimal(this.totalInterest).setScale(1, RoundingMode.HALF_UP);
            this.totalInterest = bd.doubleValue();
            TextView textView = this.interest;
            textView.setText(this.totalInterest + " " + Constants.CURRENCY_STORED);
        }
        PrasingTheDouble prasingTheDouble = new PrasingTheDouble(this.totalInterest);
        TextView textView2 = this.interest;
        textView2.setText(prasingTheDouble.getaDouble() + " " + Constants.CURRENCY_STORED);
        double d4 = this.totalInvestment;
        if (((int) d4) == d4) {
            int total = (int) d4;
            TextView textView3 = this.netAmount;
            textView3.setText(total + " " + Constants.CURRENCY_STORED);
        } else {
            BigDecimal bd2 = new BigDecimal(this.totalInvestment).setScale(1, RoundingMode.HALF_UP);
            this.totalInvestment = bd2.doubleValue();
            TextView textView4 = this.netAmount;
            textView4.setText(this.totalInvestment + " " + Constants.CURRENCY_STORED);
        }
        PrasingTheDouble prasingTheDouble2 = new PrasingTheDouble(this.totalInvestment);
        TextView textView5 = this.netAmount;
        textView5.setText(prasingTheDouble2.getaDouble() + " " + Constants.CURRENCY_STORED);
        double d5 = this.maturityValue;
        if (((int) d5) == d5) {
            int e = (int) d5;
            this.maturity.setText(String.valueOf(e));
        } else {
            BigDecimal bd3 = new BigDecimal(this.maturityValue).setScale(1, RoundingMode.HALF_UP);
            this.maturityValue = bd3.doubleValue();
            TextView textView6 = this.maturity;
            textView6.setText(this.maturityValue + " " + Constants.CURRENCY_STORED);
        }
        PrasingTheDouble prasingTheDouble3 = new PrasingTheDouble(this.maturityValue);
        TextView textView7 = this.maturity;
        textView7.setText(prasingTheDouble3.getaDouble() + " " + Constants.CURRENCY_STORED);
    }


    @SuppressLint("ResourceType")
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_simple_and_compound);
        AdsManager.INSTANCE.loadBanner(this, findViewById(R.id.banner));

//
//        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
//        setSupportActionBar(toolbar);


        this.frontshare = getApplicationContext().getSharedPreferences(getPackageName(), 0);
        this.investmentEdit = (EditText) findViewById(R.id.amount);
        this.rateEdit = (EditText) findViewById(R.id.rateoftax);
        this.time = (EditText) findViewById(R.id.tenure);
        Bitmap decodeResource = BitmapFactory.decodeResource(getResources(), R.drawable.simpledetails);
        this.bmp = decodeResource;
        this.scalebmp = Bitmap.createScaledBitmap(decodeResource, 1200, 2010, false);
        this.interest = (TextView) findViewById(R.id.interestValue);
        this.netAmount = (TextView) findViewById(R.id.netamount);
        this.maturity = (TextView) findViewById(R.id.maturityAmount);
        LinearLayout mainlayout = (LinearLayout) findViewById(R.id.mainlinear);
        Animation animation = AnimationUtils.loadAnimation(this, R.anim.slideup);
        mainlayout.startAnimation(animation);
        this.group = (RadioGroup) findViewById(R.id.togle);
        this.spiner = (Spinner) findViewById(R.id.spinner);
        this.spinner.add("Compound interest");
        this.spinner.add("Simple interest");
        ArrayAdapter<String> dataAdapter = new ArrayAdapter<>(this, 17367048, this.spinner);
        dataAdapter.setDropDownViewResource(17367049);
        this.spiner.setAdapter((SpinnerAdapter) dataAdapter);
        this.spiner.setOnItemSelectedListener(this);

    }

    @Override
    public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
        this.choosen = parent.getItemAtPosition(position).toString();
    }

    @Override
    public void onNothingSelected(AdapterView<?> parent) {
    }

    @Override
    public void onBackPressed() {

            super.onBackPressed();

    }
}
