package com.kernelapps.emicalc.Calculations;

import android.annotation.SuppressLint;
import android.app.DatePickerDialog;
import android.app.Dialog;
import android.content.ActivityNotFoundException;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.drawable.ColorDrawable;
import android.graphics.pdf.PdfDocument;
import android.net.Uri;
import android.os.Bundle;
import android.os.Environment;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.view.inputmethod.InputMethodManager;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.FrameLayout;
import android.widget.ImageButton;
import android.widget.LinearLayout;
import android.widget.ListAdapter;
import android.widget.ListView;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.core.content.FileProvider;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;

import com.kernelapps.emicalc.Constants;
import com.kernelapps.emicalc.Fragments.Statementsip;
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


public class Rd extends AppCompatActivity implements Statementsip.onMessageReadListner {


    Bitmap bmp;
    int date;
    Button day;
    double expectedRate;
    FrameLayout frameLayout;
    SharedPreferences frontshare;

    EditText investAmount;
    double investmentAmountValue;
    boolean isCalculated;
    String mMonth;


    TextView maturityDate;
    double maturityVal;
    TextView maturityValue;
    int mmonth;
    int month;
    SQLiteDatabase myDatabase;
    int myear;
    String name;


    EditText rate;

    Bitmap scalebmp;
    DatePickerDialog.OnDateSetListener setListener;
    double tenure;
    EditText time;
    String tmonth;
    double totalInt;
    TextView totalInterest;
    double totalInvest;
    TextView totalInvestment;
    int year;

    public void calculate(View view) {
        InputMethodManager imm = (InputMethodManager) getSystemService(Context.INPUT_METHOD_SERVICE);
        imm.hideSoftInputFromWindow(view.getWindowToken(), 0);
        if (this.investAmount.getText().toString().isEmpty() || this.rate.getText().toString().isEmpty() || this.time.getText().toString().isEmpty()) {
            Toast.makeText(getApplicationContext(), "Enter Inputs", Toast.LENGTH_SHORT).show();
            return;
        }
        double parseDouble = Double.parseDouble(this.time.getText().toString());
        this.tenure = parseDouble;
        if (parseDouble % 3.0d != 0.0d || Double.parseDouble(this.rate.getText().toString()) > 50.0d) {
            if (this.tenure % 3.0d != 0.0d) {
                Toast.makeText(this, "tenure should be multiple of 3 months", Toast.LENGTH_SHORT).show();
                return;
            } else {
                Toast.makeText(this, "Interest rate should be less than 50%", Toast.LENGTH_SHORT).show();
                return;
            }
        }
        calculation();
    }

    public void share(View view) {
        if (this.investAmount.getText().toString().isEmpty() || this.rate.getText().toString().isEmpty() || this.time.getText().toString().isEmpty()) {
            Toast.makeText(getApplicationContext(), "Enter Inputs", Toast.LENGTH_SHORT).show();
            return;
        }
        double parseDouble = Double.parseDouble(this.time.getText().toString());
        this.tenure = parseDouble;
        if (parseDouble % 3.0d != 0.0d || Double.parseDouble(this.rate.getText().toString()) > 50.0d) {
            if (this.tenure % 3.0d != 0.0d) {
                Toast.makeText(this, "tenure should be multiple of 3 months", Toast.LENGTH_SHORT).show();
                return;
            } else {
                Toast.makeText(this, "Interest rate should be less than 50%", Toast.LENGTH_SHORT).show();
                return;
            }
        }
        calculation();
        Intent intent = new Intent("android.intent.action.SEND");
        intent.setType("text/plain");
        intent.putExtra("android.intent.extra.SUBJECT", "EMI- A Calculator app");
        intent.putExtra("android.intent.extra.TEXT", "RD Details-\n\nInvestment Amount : " + this.investmentAmountValue + "\ntenure : " + String.valueOf(this.tenure) + "months\nFirst RD: " + this.date + " " + this.tmonth + " " + this.year + "\n\nTotal Investment Amount: " + this.totalInvest + "\nTotal Interest: " + this.totalInt + "\nMaturity Value: " + this.maturityVal + "\nMaturity Date: " + this.date + " " + this.mMonth + " " + String.valueOf(this.myear) + "\n\nCalculate by EMI\n" + "https://play.google.com/store/apps/details?id=" + view.getContext().getPackageName());
        startActivity(Intent.createChooser(intent, "Share Using"));
    }

    public void pdf(View view) {
        if (this.investAmount.getText().toString().isEmpty() || this.rate.getText().toString().isEmpty() || this.time.getText().toString().isEmpty()) {
            Toast.makeText(getApplicationContext(), "Enter Inputs", Toast.LENGTH_SHORT).show();
            return;
        }
        double parseDouble = Double.parseDouble(this.time.getText().toString());
        this.tenure = parseDouble;
        if (parseDouble % 3.0d != 0.0d || parseDouble > 360.0d || Double.parseDouble(this.rate.getText().toString()) > 50.0d) {
            if (this.tenure % 3.0d != 0.0d) {
                Toast.makeText(this, "tenure should be multiple of 3 months", Toast.LENGTH_SHORT).show();
                return;
            } else {
                Toast.makeText(this, "Interest rate should be less than 50%", Toast.LENGTH_SHORT).show();
                return;
            }
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
        canvas.drawText(this.investmentAmountValue + " " + Constants.CURRENCY_STORED, 900.0f, 575.0f, myPaint);
        StringBuilder sb = new StringBuilder();
        sb.append(this.expectedRate * 1200.0d);
        sb.append(" %");
        canvas.drawText(sb.toString(), 900.0f, 700.0f, myPaint);
        canvas.drawText(this.tenure + "  months", 900.0f, 850.0f, myPaint);
        canvas.drawText(this.totalInvest + " " + Constants.CURRENCY_STORED, 300.0f, 1150.0f, myPaint);
        canvas.drawText(this.totalInt + " " + Constants.CURRENCY_STORED, 900.0f, 1150.0f, myPaint);
        canvas.drawText(this.maturityVal + " " + Constants.CURRENCY_STORED, 600.0f, 1450.0f, myPaint);
        canvas.drawText(this.date + " " + this.mMonth + " " + this.myear, 600.0f, 1660.0f, myPaint);
        canvas.drawText(this.date + " " + this.tmonth + " " + this.year, 900.0f, 480.0f, myPaint);
        myPdf.finishPage(myPage);
        Calendar calendar = Calendar.getInstance();
        File externalStoragePublicDirectory = Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_DOWNLOADS);

        Long tsLong = System.currentTimeMillis()/1000;
        String ts = tsLong.toString();
        File file = new File(externalStoragePublicDirectory, "/Rd_" + ts + ".pdf");
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

    public void calculation() {
        this.investmentAmountValue = Double.parseDouble(this.investAmount.getText().toString());
        this.tenure = Double.parseDouble(this.time.getText().toString());
        double parseDouble = Double.parseDouble(this.rate.getText().toString());
        this.expectedRate = parseDouble;
        double d = parseDouble / 1200.0d;
        this.expectedRate = d;
        double pow = this.investmentAmountValue * (Math.pow(d + 1.0d, this.tenure) - 1.0d);
        this.maturityVal = pow;
        double d2 = this.expectedRate;
        double d3 = (pow * (1.0d + d2)) / d2;
        this.maturityVal = d3;
        double d4 = this.investmentAmountValue * this.tenure;
        this.totalInvest = d4;
        double d5 = d3 - d4;
        this.totalInt = d5;
        if (((int) d5) == d5) {
            int t = (int) d5;
            this.totalInterest.setText(String.valueOf(t));
        } else {
            BigDecimal bd = new BigDecimal(this.totalInt).setScale(1, RoundingMode.HALF_UP);
            this.totalInt = bd.doubleValue();
            TextView textView = this.totalInterest;
            textView.setText(this.totalInt + " " + Constants.CURRENCY_STORED);
        }
        PrasingTheDouble prasingTheDouble = new PrasingTheDouble(this.totalInt);
        TextView textView2 = this.totalInterest;
        textView2.setText(prasingTheDouble.getaDouble() + " " + Constants.CURRENCY_STORED);
        double d6 = this.totalInvest;
        if (((int) d6) == d6) {
            int total = (int) d6;
            TextView textView3 = this.totalInvestment;
            textView3.setText(total + " " + Constants.CURRENCY_STORED);
        } else {
            BigDecimal bd2 = new BigDecimal(this.totalInvest).setScale(1, RoundingMode.HALF_UP);
            this.totalInvest = bd2.doubleValue();
            TextView textView4 = this.totalInvestment;
            textView4.setText(this.totalInvest + " " + Constants.CURRENCY_STORED);
        }
        PrasingTheDouble prasingTheDouble2 = new PrasingTheDouble(this.totalInvest);
        TextView textView5 = this.totalInvestment;
        textView5.setText(prasingTheDouble2.getaDouble() + " " + Constants.CURRENCY_STORED);
        double d7 = this.maturityVal;
        if (((int) d7) == d7) {
            int e = (int) d7;
            this.maturityValue.setText(String.valueOf(e));
        } else {
            BigDecimal bd3 = new BigDecimal(this.maturityVal).setScale(1, RoundingMode.HALF_UP);
            this.maturityVal = bd3.doubleValue();
            TextView textView6 = this.maturityValue;
            textView6.setText(this.maturityVal + " " + Constants.CURRENCY_STORED);
        }
        PrasingTheDouble prasingTheDouble3 = new PrasingTheDouble(this.maturityVal);
        TextView textView7 = this.maturityValue;
        textView7.setText(prasingTheDouble3.getaDouble() + " " + Constants.CURRENCY_STORED);
        double d8 = (double) this.month;
        double d9 = this.tenure;
        Double.isNaN(d8);
        int i = (int) (d8 + d9);
        this.mmonth = i;
        int index = (i - 1) / 12;
        this.myear = this.year + index;
        int i2 = i - (index * 12);
        this.mmonth = i2;
        switch (i2) {
            case 1:
                this.mMonth = "Jan";
                break;
            case 2:
                this.mMonth = "Feb";
                break;
            case 3:
                this.mMonth = "Mar";
                break;
            case 4:
                this.mMonth = "April";
                break;
            case 5:
                this.mMonth = "May";
                break;
            case 6:
                this.mMonth = "Jun";
                break;
            case 7:
                this.mMonth = "July";
                break;
            case 8:
                this.mMonth = "Aug";
                break;
            case 9:
                this.mMonth = "Sep";
                break;
            case 10:
                this.mMonth = "Oct";
                break;
            case 11:
                this.mMonth = "Nov";
                break;
            case 12:
                this.mMonth = "Dec";
                break;
        }
        TextView textView8 = this.maturityDate;
        textView8.setText(this.date + " " + this.mMonth + " " + this.myear);
    }

    public void statistic(View view) {
        InputMethodManager imm = (InputMethodManager) getSystemService(Context.INPUT_METHOD_SERVICE);
        imm.hideSoftInputFromWindow(view.getWindowToken(), 0);
        if (this.investAmount.getText().toString().isEmpty() || this.rate.getText().toString().isEmpty() || this.time.getText().toString().isEmpty()) {
            Toast.makeText(getApplicationContext(), "Enter Inputs", Toast.LENGTH_SHORT).show();
            return;
        }
        double parseDouble = Double.parseDouble(this.time.getText().toString());
        this.tenure = parseDouble;
        if (parseDouble % 3.0d != 0.0d || Double.parseDouble(this.rate.getText().toString()) > 50.0d) {
            if (this.tenure % 3.0d != 0.0d) {
                Toast.makeText(this, "tenure should be multiple of 3 months", Toast.LENGTH_SHORT).show();
                return;
            } else {
                Toast.makeText(this, "Interest rate should be less than 50%", Toast.LENGTH_SHORT).show();
                return;
            }
        }
        calculation();
        this.frameLayout.setBackgroundColor(Color.parseColor("#59000000"));
        Statementsip statement = new Statementsip(0.0d, this.tenure, this.expectedRate, this.investmentAmountValue, this.maturityVal, this.totalInt, this.date, this.month, this.year, "RD");
        FragmentManager manager = getSupportFragmentManager();
        FragmentTransaction transaction = manager.beginTransaction();
        transaction.addToBackStack(null);
        transaction.setCustomAnimations(R.anim.segmentup, R.anim.segmentdown);
        transaction.add(R.id.frame, statement, "Fragment").commit();
    }


    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_rd);


        AdsManager.INSTANCE.loadBanner(this, findViewById(R.id.banner));

//
//        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
//        setSupportActionBar(toolbar);


        this.frontshare = getApplicationContext().getSharedPreferences(getPackageName(), 0);


        this.investAmount = (EditText) findViewById(R.id.InvestmentAmoount);
        this.rate = (EditText) findViewById(R.id.interestAmount);
        this.time = (EditText) findViewById(R.id.tenure);
        this.day = (Button) findViewById(R.id.date);
        Bitmap decodeResource = BitmapFactory.decodeResource(getResources(), R.drawable.rddetails);
        this.bmp = decodeResource;
        this.scalebmp = Bitmap.createScaledBitmap(decodeResource, 1200, 2010, false);
        SQLiteDatabase openOrCreateDatabase = openOrCreateDatabase("EMI", 0, null);
        this.myDatabase = openOrCreateDatabase;
        openOrCreateDatabase.execSQL("CREATE TABLE IF NOT EXISTS rdTable(name TEXT,principalAmount DOUBLE,interest DOUBLE,tenure DOUBLE,date TEXT,id INTEGER PRIMARY KEY)");
        this.totalInterest = (TextView) findViewById(R.id.totalInterest);
        this.totalInvestment = (TextView) findViewById(R.id.totalInvestment);
        this.maturityValue = (TextView) findViewById(R.id.MaturityValue);
        this.maturityDate = (TextView) findViewById(R.id.Matuirtydate);
        LinearLayout mainlayout = (LinearLayout) findViewById(R.id.mainlinear);
        Animation animation = AnimationUtils.loadAnimation(this, R.anim.slideup);
        mainlayout.startAnimation(animation);
        this.frameLayout = (FrameLayout) findViewById(R.id.blur);
        Calendar c = Calendar.getInstance();
        this.year = c.get(1);
        this.month = c.get(2) + 1;
        this.date = c.get(5);
        switch (this.month) {
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
        Button button = this.day;
        button.setText("First EMI: " + this.date + " " + this.tmonth + " " + this.year);
        this.setListener = new DatePickerDialog.OnDateSetListener() {
            @Override
            public void onDateSet(DatePicker datePicker, int i, int i1, int i2) {
                Rd.this.date = i2;
                Rd.this.month = i1 + 1;
                Rd.this.year = i;
                switch (Rd.this.month) {
                    case 1:
                        Rd.this.tmonth = "Jan";
                        break;
                    case 2:
                        Rd.this.tmonth = "Feb";
                        break;
                    case 3:
                        Rd.this.tmonth = "Mar";
                        break;
                    case 4:
                        Rd.this.tmonth = "April";
                        break;
                    case 5:
                        Rd.this.tmonth = "May";
                        break;
                    case 6:
                        Rd.this.tmonth = "Jun";
                        break;
                    case 7:
                        Rd.this.tmonth = "July";
                        break;
                    case 8:
                        Rd.this.tmonth = "Aug";
                        break;
                    case 9:
                        Rd.this.tmonth = "Sep";
                        break;
                    case 10:
                        Rd.this.tmonth = "Oct";
                        break;
                    case 11:
                        Rd.this.tmonth = "Nov";
                        break;
                    case 12:
                        Rd.this.tmonth = "Dec";
                        break;
                }
                Button button2 = Rd.this.day;
                button2.setText("First EMI: " + Rd.this.date + " " + Rd.this.tmonth + " " + Rd.this.year);
            }
        };
        Intent intent = getIntent();
        String ext = intent.getStringExtra("Open");
        if (ext != null) {


            openingSaved(Integer.parseInt(ext));
        }
        this.day.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Rd rd = Rd.this;
                @SuppressLint("ResourceType") DatePickerDialog datePickerDialog = new DatePickerDialog(rd, 16973936, rd.setListener, Rd.this.year, Rd.this.month - 1, Rd.this.date);
                datePickerDialog.getWindow().setBackgroundDrawable(new ColorDrawable(0));
                datePickerDialog.show();
            }
        });

    }

    @Override
    public void onMessage() {
        this.frameLayout.setBackgroundColor(Color.parseColor("#00FFFFFF"));
    }

    @Override
    public void onBackPressed() {
        this.frameLayout.setBackgroundColor(Color.parseColor("#00FFFFFF"));

            super.onBackPressed();

    }


    public void openingSaved(int position) {
        char c;
        char c2;
        Cursor cursor = this.myDatabase.rawQuery("SELECT * FROM rdTable", null);
        List<Double> principle = new ArrayList<>();
        List<Integer> id = new ArrayList<>();
        List<String> name = new ArrayList<>();
        List<Double> interest = new ArrayList<>();
        List<Double> time = new ArrayList<>();
        List<String> dateHistory = new ArrayList<>();
        int idIndex = cursor.getColumnIndex("id");
        int nameIndex = cursor.getColumnIndex("name");
        int principalIndex = cursor.getColumnIndex("principalAmount");
        int interetIndex = cursor.getColumnIndex("interest");
        int tenureIndex = cursor.getColumnIndex("tenure");
        int dateIndex = cursor.getColumnIndex("date");
        cursor.moveToFirst();
        while (!cursor.isAfterLast()) {
            name.add(cursor.getString(nameIndex));
            id.add(Integer.valueOf(cursor.getInt(idIndex)));
            principle.add(Double.valueOf(cursor.getDouble(principalIndex)));
            interest.add(Double.valueOf(cursor.getDouble(interetIndex)));
            time.add(Double.valueOf(cursor.getDouble(tenureIndex)));
            dateHistory.add(cursor.getString(dateIndex));
            cursor.moveToNext();
        }
        this.investAmount.setText(String.valueOf(principle.get(position)));
        this.time.setText(String.valueOf(time.get(position)));
        this.rate.setText(String.valueOf(interest.get(position)));
        this.day.setText(String.valueOf(dateHistory.get(position)));
        String[] dateregex = dateHistory.get(position).split(" ");
        this.date = Integer.parseInt(dateregex[0]);
        String str = dateregex[1];
        this.tmonth = str;
        switch (str.hashCode()) {
            case 66195:
                if (str.equals("Aug")) {
                    c = 7;
                    break;
                }
                c = 65535;
                break;
            case 68578:
                if (str.equals("Dec")) {
                    c = 11;
                    break;
                }
                c = 65535;
                break;
            case 70499:
                if (str.equals("Feb")) {
                    c = 1;
                    break;
                }
                c = 65535;
                break;
            case 74231:
                if (str.equals("Jan")) {
                    c = 0;
                    break;
                }
                c = 65535;
                break;
            case 74851:
                if (str.equals("Jun")) {
                    c = 5;
                    break;
                }
                c = 65535;
                break;
            case 77118:
                if (str.equals("Mar")) {
                    c = 2;
                    break;
                }
                c = 65535;
                break;
            case 77125:
                if (str.equals("May")) {
                    c = 4;
                    break;
                }
                c = 65535;
                break;
            case 78517:
                if (str.equals("Nov")) {
                    c = '\n';
                    break;
                }
                c = 65535;
                break;
            case 79104:
                if (str.equals("Oct")) {
                    c = '\t';
                    break;
                }
                c = 65535;
                break;
            case 83006:
                if (str.equals("Sep")) {
                    c = '\b';
                    break;
                }
                c = 65535;
                break;
            case 2320440:
                if (str.equals("July")) {
                    c = 6;
                    break;
                }
                c = 65535;
                break;
            case 63478374:
                if (str.equals("April")) {
                    c = 3;
                    break;
                }
                c = 65535;
                break;
            default:
                c = 65535;
                break;
        }
        switch (c) {
            case 0:
                c2 = 2;
                this.month = 1;
                break;
            case 1:
                c2 = 2;
                this.month = 2;
                break;
            case 2:
                this.month = 3;
                c2 = 2;
                break;
            case 3:
                this.month = 4;
                c2 = 2;
                break;
            case 4:
                this.month = 5;
                c2 = 2;
                break;
            case 5:
                this.month = 6;
                c2 = 2;
                break;
            case 6:
                this.month = 7;
                c2 = 2;
                break;
            case 7:
                this.month = 8;
                c2 = 2;
                break;
            case '\b':
                this.month = 9;
                c2 = 2;
                break;
            case '\t':
                this.month = 10;
                c2 = 2;
                break;
            case '\n':
                this.month = 11;
                c2 = 2;
                break;
            case 11:
                this.month = 12;
                c2 = 2;
                break;
            default:
                c2 = 2;
                break;
        }
        this.year = Integer.parseInt(dateregex[c2]);
        calculation();
    }

    public void history(View view) {
        historyFunction();
    }

    public void historyFunction() {
        Cursor cursor = this.myDatabase.rawQuery("SELECT * FROM rdTable", null);
        List<Double> principle = new ArrayList<>();
        List<Integer> id = new ArrayList<>();
        List<String> name = new ArrayList<>();
        List<Double> interest = new ArrayList<>();
        List<Double> time = new ArrayList<>();
        List<String> dateHistory = new ArrayList<>();
        int idIndex = cursor.getColumnIndex("id");
        int nameIndex = cursor.getColumnIndex("name");
        int principalIndex = cursor.getColumnIndex("principalAmount");
        int interetIndex = cursor.getColumnIndex("interest");
        int tenureIndex = cursor.getColumnIndex("tenure");
        int dateIndex = cursor.getColumnIndex("date");
        cursor.moveToFirst();
        while (!cursor.isAfterLast()) {
            name.add(cursor.getString(nameIndex));
            id.add(Integer.valueOf(cursor.getInt(idIndex)));
            principle.add(Double.valueOf(cursor.getDouble(principalIndex)));
            interest.add(Double.valueOf(cursor.getDouble(interetIndex)));
            time.add(Double.valueOf(cursor.getDouble(tenureIndex)));
            dateHistory.add(cursor.getString(dateIndex));
            cursor.moveToNext();
        }
        final Dialog history = new Dialog(this);
        history.setContentView(R.layout.history);
        history.setCancelable(true);
        history.getWindow().setBackgroundDrawable(new ColorDrawable(0));
        history.setCanceledOnTouchOutside(true);
        history.getWindow().setLayout(-2, -1);
        history.getWindow().getAttributes().windowAnimations = 16973826;
        history.show();
        MyListAdapter adapter = new MyListAdapter(this, name, principle, dateHistory, id, time, null);
        ListView historyList = (ListView) history.findViewById(R.id.historyList);
        historyList.setAdapter((ListAdapter) adapter);
        ImageButton cancel = (ImageButton) history.findViewById(R.id.canceling);
        cancel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                history.dismiss();
            }
        });
    }

    public void save(View view) {
        if (this.investAmount.getText().toString().isEmpty() || this.rate.getText().toString().isEmpty() || this.time.getText().toString().isEmpty()) {
            Toast.makeText(getApplicationContext(), "Enter Inputs", Toast.LENGTH_SHORT).show();
            return;
        }
        double parseDouble = Double.parseDouble(this.time.getText().toString());
        this.tenure = parseDouble;
        if (parseDouble > 360.0d || Double.parseDouble(this.rate.getText().toString()) > 50.0d) {
            if (this.tenure > 360.0d) {
                Toast.makeText(this, "tenure should be less than 30 years", Toast.LENGTH_SHORT).show();
                return;
            } else {
                Toast.makeText(this, "Interest rate should be less than 50%", Toast.LENGTH_SHORT).show();
                return;
            }
        }
        calculation();
        final double loanSql = Double.parseDouble(this.investAmount.getText().toString());
        final double interestSql = Double.parseDouble(this.rate.getText().toString());
        final String sqlDate = this.date + " " + this.month + " " + this.year;
        final double periodSql = this.tenure / 12.0d;
        final Dialog entry = new Dialog(this);
        entry.setContentView(R.layout.file_name);
        entry.setCancelable(true);
        entry.getWindow().setBackgroundDrawable(new ColorDrawable(0));
        entry.setCanceledOnTouchOutside(true);
        entry.getWindow().setLayout(-2, -2);
        entry.getWindow().getAttributes().windowAnimations = 16973826;
        entry.show();
        final EditText naming = (EditText) entry.findViewById(R.id.naming);
        Button save = (Button) entry.findViewById(R.id.save);
        ImageButton cancel = (ImageButton) entry.findViewById(R.id.canceling);
        cancel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                entry.dismiss();
            }
        });
        save.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Rd.this.name = naming.getText().toString();
                if (Rd.this.name.isEmpty()) {
                    Rd.this.name = "EMPTY";
                }
                SQLiteDatabase sQLiteDatabase = Rd.this.myDatabase;
                sQLiteDatabase.execSQL("INSERT INTO rdTable(name,principalAmount,interest,tenure,date) VALUES ('" + Rd.this.name + "'," + loanSql + "," + interestSql + "," + periodSql + ",'" + sqlDate + "')");
                entry.dismiss();
            }
        });
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.history, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        historyFunction();
        return super.onOptionsItemSelected(item);
    }
}
