package com.kernelapps.emicalc.Calculations;

import android.annotation.SuppressLint;
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
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.Spinner;
import android.widget.SpinnerAdapter;
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
import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;


public class Tax extends AppCompatActivity implements AdapterView.OnItemSelectedListener {

    double am;
    EditText amount;


    Bitmap bmp;
    SharedPreferences frontshare;
    double gst;
    double gsta;
    TextView gstamount;
    EditText gstrate;

    boolean isCalculated;


    double n;

    TextView net;


    Bitmap scalebmp;
    Spinner spiner;
    double t;
    TextView total;
    List<String> spinner = new ArrayList();
    String choosen = "GST is Added";

    public void calculate(View view) {
        InputMethodManager imm = (InputMethodManager) getSystemService(Context.INPUT_METHOD_SERVICE);
        imm.hideSoftInputFromWindow(view.getWindowToken(), 0);
        calculation();
    }

    public void calculation() {
        if (String.valueOf(this.amount.getText()).isEmpty() || String.valueOf(this.amount.getText()).isEmpty()) {
            Toast.makeText(this, "Enter the value", Toast.LENGTH_SHORT).show();
        } else if (Double.parseDouble(this.gstrate.getText().toString()) > 50.0d) {
            Toast.makeText(this, "Interest rate should be less than 50%", Toast.LENGTH_SHORT).show();
        } else {
            this.am = Double.parseDouble(String.valueOf(this.amount.getText()));
            this.gst = Double.parseDouble(String.valueOf(this.gstrate.getText()));
            if (this.choosen.contentEquals("GST is Added")) {
                double d = this.am;
                this.t = d;
                double d2 = this.gst;
                double d3 = (d2 * d) / (d2 + 100.0d);
                this.gsta = d3;
                this.n = d - d3;
            } else {
                double d4 = this.gst;
                double d5 = this.am;
                double d6 = (d4 * d5) / 100.0d;
                this.gsta = d6;
                this.n = d5;
                this.t = d5 + d6;
            }
            double d7 = this.gsta;
            if (((int) d7) == d7) {
                int gsta = (int) d7;
                this.gstamount.setText(String.valueOf(gsta));
            } else {
                BigDecimal bd = new BigDecimal(this.gsta).setScale(2, RoundingMode.HALF_UP);
                double doubleValue = bd.doubleValue();
                this.gsta = doubleValue;
                this.gstamount.setText(String.valueOf(doubleValue));
            }
            PrasingTheDouble prasingTheDouble = new PrasingTheDouble(this.gsta);
            TextView textView = this.gstamount;
            textView.setText(prasingTheDouble.getaDouble() + " " + Constants.CURRENCY_STORED);
            double d8 = this.n;
            if (((int) d8) == d8) {
                int n = (int) d8;
                this.net.setText(String.valueOf(n));
            } else {
                BigDecimal bd2 = new BigDecimal(this.n).setScale(2, RoundingMode.HALF_UP);
                double doubleValue2 = bd2.doubleValue();
                this.n = doubleValue2;
                this.net.setText(String.valueOf(doubleValue2));
            }
            PrasingTheDouble prasingTheDouble2 = new PrasingTheDouble(this.n);
            TextView textView2 = this.net;
            textView2.setText(prasingTheDouble2.getaDouble() + " " + Constants.CURRENCY_STORED);
            double d9 = this.t;
            if (((int) d9) == d9) {
                int t = (int) d9;
                this.total.setText(String.valueOf(t));
            } else {
                BigDecimal bd3 = new BigDecimal(this.t).setScale(2, RoundingMode.HALF_UP);
                double doubleValue3 = bd3.doubleValue();
                this.t = doubleValue3;
                this.total.setText(String.valueOf(doubleValue3));
            }
            PrasingTheDouble prasingTheDouble3 = new PrasingTheDouble(this.t);
            TextView textView3 = this.total;
            textView3.setText(prasingTheDouble3.getaDouble() + " " + Constants.CURRENCY_STORED);
        }
    }

    public void share(View view) {
        if (String.valueOf(this.amount.getText()).isEmpty() || String.valueOf(this.amount.getText()).isEmpty()) {
            Toast.makeText(this, "Enter the value", Toast.LENGTH_SHORT).show();
            return;
        }
        calculation();
        Intent intent = new Intent("android.intent.action.SEND");
        intent.setType("text/plain");
        intent.putExtra("android.intent.extra.SUBJECT", "EMI- A Calculator app");
        intent.putExtra("android.intent.extra.TEXT", "Tax Details-\n \nAmount: " + this.am + "\nRate of GST: " + this.gst + "\n\nNet Amount: " + this.n + "\nGST Amount: " + this.gsta + "\nTotal Amount: " + this.t + "\n\nCalculate by EMI\n" + "https://play.google.com/store/apps/details?id=" + view.getContext().getPackageName());
        startActivity(Intent.createChooser(intent, "Share Using"));
    }

    public void pdf(View view) {


        ActivityCompat.requestPermissions(this, new String[]{"android.permission.WRITE_EXTERNAL_STORAGE"}, 0);
        PdfDocument myPdf = new PdfDocument();
        Paint myPaint = new Paint();
        PdfDocument.PageInfo myPageInfo = new PdfDocument.PageInfo.Builder(1200, 2010, 1).create();
        PdfDocument.Page myPage = myPdf.startPage(myPageInfo);
        Canvas canvas = myPage.getCanvas();
        canvas.drawBitmap(this.scalebmp, 0.0f, 0.0f, myPaint);
        myPaint.setTextSize(27.0f);
        myPaint.setTextAlign(Paint.Align.RIGHT);
        canvas.drawText(this.choosen, 1100.0f, 500.0f, myPaint);
        Paint p = new Paint();
        p.setColor(Color.parseColor("#000000"));
        p.setTextSize(29.0f);
        p.setTextAlign(Paint.Align.CENTER);
        canvas.drawText(this.am + " " + Constants.CURRENCY_STORED, 900.0f, 575.0f, p);
        StringBuilder sb = new StringBuilder();
        sb.append(this.gst);
        sb.append(" %");
        canvas.drawText(sb.toString(), 900.0f, 715.0f, p);
        canvas.drawText(this.n + " " + Constants.CURRENCY_STORED, 900.0f, 980.0f, p);
        canvas.drawText(this.gsta + " " + Constants.CURRENCY_STORED, 900.0f, 1110.0f, p);
        canvas.drawText(this.t + " " + Constants.CURRENCY_STORED, 900.0f, 1245.0f, p);
        myPdf.finishPage(myPage);
        Calendar calendar = Calendar.getInstance();
        File externalStoragePublicDirectory = Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_DOWNLOADS);


        Long tsLong = System.currentTimeMillis()/1000;
        String ts = tsLong.toString();
        File file = new File(externalStoragePublicDirectory, "/Tax_" + ts + ".pdf");
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
            } catch (ActivityNotFoundException e2) {
                Toast.makeText(this, "Download", Toast.LENGTH_SHORT).show();
            }
        }
    }


    @SuppressLint("ResourceType")
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_tax);


        AdsManager.INSTANCE.loadBanner(this, findViewById(R.id.banner));


        this.frontshare = getApplicationContext().getSharedPreferences(getPackageName(), 0);


        Bitmap decodeResource = BitmapFactory.decodeResource(getResources(), R.drawable.tax);
        this.bmp = decodeResource;
        this.scalebmp = Bitmap.createScaledBitmap(decodeResource, 1200, 2010, false);
        this.spiner = (Spinner) findViewById(R.id.spinner);
        this.spinner.add("GST is Added");
        this.spinner.add("GST is Removed");
        LinearLayout mainlayout = (LinearLayout) findViewById(R.id.mainlinear);
        Animation animation = AnimationUtils.loadAnimation(this, R.anim.slideup);
        mainlayout.startAnimation(animation);
        ArrayAdapter<String> dataAdapter = new ArrayAdapter<>(this, 17367048, this.spinner);
        dataAdapter.setDropDownViewResource(17367049);
        this.spiner.setAdapter((SpinnerAdapter) dataAdapter);
        this.spiner.setOnItemSelectedListener(this);
        this.amount = (EditText) findViewById(R.id.amount);
        this.gstrate = (EditText) findViewById(R.id.rateoftax);
        this.net = (TextView) findViewById(R.id.netamount);
        this.gstamount = (TextView) findViewById(R.id.gstamount);
        this.total = (TextView) findViewById(R.id.totalamount);
    }

    @Override
    public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l) {
        this.choosen = adapterView.getItemAtPosition(i).toString();
        if (!this.amount.getText().toString().isEmpty() && !this.gstrate.getText().toString().isEmpty()) {
            calculation();
        }
    }

    @Override
    public void onNothingSelected(AdapterView<?> adapterView) {
    }

    @Override
    public void onBackPressed() {

        super.onBackPressed();

    }
}
