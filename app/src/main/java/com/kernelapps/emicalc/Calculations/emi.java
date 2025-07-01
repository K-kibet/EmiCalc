package com.kernelapps.emicalc.Calculations;

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
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.core.content.FileProvider;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;

import com.kernelapps.emicalc.Constants;
import com.kernelapps.emicalc.Fragments.Statement;
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


public class emi extends AppCompatActivity implements Statement.onMessageReadListner {


    Bitmap bmp;
    Button date;
    Bitmap dusrabmp;
    double emi;
    String fmonth;
    FrameLayout frameLayout;
    SharedPreferences frontshare;
    int fyear;
    RadioGroup group;

    double interest;
    EditText interestamount;
    boolean isCalculated;
    double loanamount;


    SQLiteDatabase myDatabase;
    String name;

    double period;

    EditText principalamount;
    RadioButton radioButton;

    Bitmap scalebmp;
    Bitmap scaledusrabmp;
    DatePickerDialog.OnDateSetListener setListener;
    double tinterest;
    String tmonth;
    int todate;
    int tomonth;
    double total;
    TextView totalemi;
    TextView totalinterest;
    TextView totalpayement;
    TextView totalprincipal;
    int toyear;
    EditText year;

    public void calculate(View view) {
        InputMethodManager imm = (InputMethodManager) getSystemService(Context.INPUT_METHOD_SERVICE);
        imm.hideSoftInputFromWindow(view.getWindowToken(), 0);
        if (this.principalamount.getText().toString().isEmpty() || this.interestamount.getText().toString().isEmpty() || this.year.getText().toString().isEmpty()) {
            Toast.makeText(getApplicationContext(), "Enter Inputs", Toast.LENGTH_SHORT).show();
            return;
        }
        this.period = Double.parseDouble(this.year.getText().toString());
        int selectedid = this.group.getCheckedRadioButtonId();
        RadioButton radioButton = (RadioButton) findViewById(selectedid);
        this.radioButton = radioButton;
        if (radioButton.getText().toString().equals("year")) {
            this.period = Double.parseDouble(this.year.getText().toString()) * 12.0d;
        } else {
            this.period = Double.parseDouble(this.year.getText().toString());
        }
        if (this.period <= 360.0d && Double.parseDouble(this.interestamount.getText().toString()) <= 50.0d) {
            calculation();
        } else if (this.period > 360.0d) {
            Toast.makeText(this, "tenure should be less than 30 years", Toast.LENGTH_SHORT).show();
        } else {
            Toast.makeText(this, "Interest rate should be less than 50%", Toast.LENGTH_SHORT).show();
        }
    }

    public void calculation() {
        this.loanamount = Double.parseDouble(this.principalamount.getText().toString());
        double parseDouble = Double.parseDouble(this.interestamount.getText().toString());
        this.interest = parseDouble;
        double d = parseDouble / 1200.0d;
        this.interest = d;
        double pmt = (this.loanamount * d) / (1.0d - Math.pow(d + 1.0d, -this.period));
        double d2 = this.period;
        double d3 = this.loanamount;
        double d4 = (pmt * d2) - d3;
        this.tinterest = d4;
        this.total = d4 + d3;
        double p = Math.pow(this.interest + 1.0d, d2);
        this.emi = ((this.loanamount * this.interest) * p) / (p - 1.0d);
        PrasingTheDouble prasingTheDouble = new PrasingTheDouble(this.loanamount);
        TextView textView = this.totalprincipal;
        textView.setText(prasingTheDouble.getaDouble() + " " + Constants.CURRENCY_STORED);
        double d5 = this.tinterest;
        if (((int) d5) == d5) {
            int tinterest = (int) d5;
            this.totalinterest.setText(String.valueOf(tinterest));
        } else {
            BigDecimal bd = new BigDecimal(this.tinterest).setScale(1, RoundingMode.HALF_UP);
            this.tinterest = bd.doubleValue();
            TextView textView2 = this.totalinterest;
            textView2.setText(this.tinterest + " " + Constants.CURRENCY_STORED);
        }
        PrasingTheDouble prasingTheDouble2 = new PrasingTheDouble(this.tinterest);
        TextView textView3 = this.totalinterest;
        textView3.setText(prasingTheDouble2.getaDouble() + " " + Constants.CURRENCY_STORED);
        double d6 = this.total;
        if (((int) d6) == d6) {
            int total = (int) d6;
            TextView textView4 = this.totalpayement;
            textView4.setText(total + " " + Constants.CURRENCY_STORED);
        } else {
            BigDecimal bd2 = new BigDecimal(this.total).setScale(1, RoundingMode.HALF_UP);
            this.total = bd2.doubleValue();
            TextView textView5 = this.totalpayement;
            textView5.setText(this.total + " " + Constants.CURRENCY_STORED);
        }
        PrasingTheDouble prasingTheDouble3 = new PrasingTheDouble(this.total);
        TextView textView6 = this.totalpayement;
        textView6.setText(prasingTheDouble3.getaDouble() + " " + Constants.CURRENCY_STORED);
        double d7 = this.emi;
        if (((int) d7) == d7) {
            int emi = (int) d7;
            this.totalemi.setText(String.valueOf(emi));
        } else {
            BigDecimal bd3 = new BigDecimal(this.emi).setScale(1, RoundingMode.HALF_UP);
            this.emi = bd3.doubleValue();
            TextView textView7 = this.totalemi;
            textView7.setText(this.emi + " " + Constants.CURRENCY_STORED);
        }
        PrasingTheDouble prasingTheDouble4 = new PrasingTheDouble(this.emi);
        TextView textView8 = this.totalemi;
        textView8.setText(prasingTheDouble4.getaDouble() + " " + Constants.CURRENCY_STORED);
        double d8 = (double) this.tomonth;
        double d9 = this.period;
        Double.isNaN(d8);
        int fomonth = (int) (d8 + d9);
        int index = (fomonth - 1) / 12;
        this.fyear = this.toyear + index;
        switch (fomonth - (index * 12)) {
            case 1:
                this.fmonth = "Jan";
                break;
            case 2:
                this.fmonth = "Feb";
                break;
            case 3:
                this.fmonth = "Mar";
                break;
            case 4:
                this.fmonth = "April";
                break;
            case 5:
                this.fmonth = "May";
                break;
            case 6:
                this.fmonth = "Jun";
                break;
            case 7:
                this.fmonth = "July";
                break;
            case 8:
                this.fmonth = "Aug";
                break;
            case 9:
                this.fmonth = "Sep";
                break;
            case 10:
                this.fmonth = "Oct";
                break;
            case 11:
                this.fmonth = "Nov";
                break;
            case 12:
                this.fmonth = "Dec";
                break;
        }
        TextView finaldate = (TextView) findViewById(R.id.finaldate);
        finaldate.setText(this.todate + " " + this.fmonth + " " + this.fyear);
    }

    public void statistic(View view) {
        if (!this.principalamount.getText().toString().isEmpty() && !this.interestamount.getText().toString().isEmpty() && !this.year.getText().toString().isEmpty()) {
            this.period = Double.parseDouble(this.year.getText().toString());
            int selectedid = this.group.getCheckedRadioButtonId();
            RadioButton radioButton = (RadioButton) findViewById(selectedid);
            this.radioButton = radioButton;
            if (radioButton.getText().toString().equals("year")) {
                this.period = Double.parseDouble(this.year.getText().toString()) * 12.0d;
            } else {
                this.period = Double.parseDouble(this.year.getText().toString());
            }
            if (this.period <= 360.0d && Double.parseDouble(this.interestamount.getText().toString()) <= 50.0d) {
                calculation();
                this.frameLayout.setBackgroundColor(Color.parseColor("#59000000"));
                Statement statement = new Statement(this.period, this.interest, this.loanamount, this.emi, this.tinterest, this.todate, this.tomonth, this.toyear);
                FragmentManager manager = getSupportFragmentManager();
                FragmentTransaction transaction = manager.beginTransaction();
                transaction.addToBackStack(null);
                transaction.setCustomAnimations(R.anim.segmentup, R.anim.segmentdown);
                transaction.add(R.id.frame, statement, "Fragment").commit();
                return;
            } else if (this.period > 360.0d) {
                Toast.makeText(this, "tenure should be less than 30 years", Toast.LENGTH_SHORT).show();
                return;
            } else {
                Toast.makeText(this, "Interest rate should be less than 50%", Toast.LENGTH_SHORT).show();
                return;
            }
        }
        Toast.makeText(getApplicationContext(), "Enter Inputs", Toast.LENGTH_SHORT).show();
    }

    public void share(View view) {
        if (this.principalamount.getText().toString().isEmpty() || this.interestamount.getText().toString().isEmpty() || this.year.getText().toString().isEmpty()) {
            Toast.makeText(this, "Enter the value", Toast.LENGTH_SHORT).show();
            return;
        }
        this.period = Double.parseDouble(this.year.getText().toString());
        int selectedid = this.group.getCheckedRadioButtonId();
        RadioButton radioButton = (RadioButton) findViewById(selectedid);
        this.radioButton = radioButton;
        if (radioButton.getText().toString().equals("year")) {
            this.period = Double.parseDouble(this.year.getText().toString()) * 12.0d;
        } else {
            this.period = Double.parseDouble(this.year.getText().toString());
        }
        if (this.period <= 30.0d || Double.parseDouble(this.interestamount.getText().toString()) <= 50.0d) {
            calculation();
            Intent intent = new Intent("android.intent.action.SEND");
            intent.setType("text/plain");
            intent.putExtra("android.intent.extra.SUBJECT", "EMI- A Calculator app");
            intent.putExtra("android.intent.extra.TEXT", "EMI Details-\n \nPrincipal Loan Amount: " + String.valueOf(this.loanamount) + "\nLoan term: " + String.valueOf(this.period) + "\nFirst EMI at: " + String.valueOf(this.todate) + " " + this.tmonth + " " + String.valueOf(this.toyear) + "\n\nMonthly EMI: " + this.emi + "\nTotal Interest: " + String.valueOf(this.tinterest) + "\nTotal payment: " + String.valueOf(this.tinterest + this.loanamount) + "\nLast Loan Date: " + String.valueOf(this.todate) + " " + this.fmonth + " " + String.valueOf(this.fyear) + "\n\nCalculate by EMI\n" + "https://play.google.com/store/apps/details?id=" + view.getContext().getPackageName());
            startActivity(Intent.createChooser(intent, "Share Using"));
        } else if (this.period > 30.0d) {
            Toast.makeText(this, "tenure should be less than 30 years", Toast.LENGTH_SHORT).show();
        } else {
            Toast.makeText(this, "Interest rate should be less than 50%", Toast.LENGTH_SHORT).show();
        }
    }

    public void pdf(View view) {
        if (this.principalamount.getText().toString().isEmpty() || this.interestamount.getText().toString().isEmpty() || this.year.getText().toString().isEmpty()) {
            Toast.makeText(getApplicationContext(), "Enter Inputs", Toast.LENGTH_SHORT).show();
            return;
        }
        this.period = Double.parseDouble(this.year.getText().toString());
        int selectedid = this.group.getCheckedRadioButtonId();
        RadioButton radioButton = (RadioButton) findViewById(selectedid);
        this.radioButton = radioButton;
        if (radioButton.getText().toString().equals("year")) {
            this.period = Double.parseDouble(this.year.getText().toString()) * 12.0d;
        } else {
            this.period = Double.parseDouble(this.year.getText().toString());
        }
        if (this.period <= 30.0d || Double.parseDouble(this.interestamount.getText().toString()) <= 50.0d) {
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
            canvas.drawText(this.loanamount + " " + Constants.CURRENCY_STORED, 900.0f, 575.0f, myPaint);
            StringBuilder sb = new StringBuilder();
            sb.append(this.interest * 1200.0d);
            sb.append(" %");
            canvas.drawText(sb.toString(), 900.0f, 700.0f, myPaint);
            canvas.drawText(this.period + "  " + this.radioButton.getText().toString(), 900.0f, 850.0f, myPaint);
            canvas.drawText(this.tinterest + " " + Constants.CURRENCY_STORED, 300.0f, 1150.0f, myPaint);
            canvas.drawText(this.loanamount + " " + Constants.CURRENCY_STORED, 900.0f, 1150.0f, myPaint);
            canvas.drawText(this.total + " " + Constants.CURRENCY_STORED, 600.0f, 1450.0f, myPaint);
            canvas.drawText(this.todate + " " + this.fmonth + " " + this.fyear, 600.0f, 1660.0f, myPaint);
            canvas.drawText(this.todate + " " + this.tmonth + " " + this.toyear, 900.0f, 480.0f, myPaint);
            myPdf.finishPage(myPage);
            PdfDocument.PageInfo myPageInfo2 = new PdfDocument.PageInfo.Builder(1200, 2010, 2).create();
            PdfDocument.Page myPage2 = myPdf.startPage(myPageInfo2);
            Canvas canvas2 = myPage2.getCanvas();
            canvas2.drawBitmap(this.scaledusrabmp, 0.0f, 0.0f, myPaint);
            canvas2.drawText(String.valueOf(this.emi) + " " + Constants.CURRENCY_STORED, 600.0f, 375.0f, myPaint);
            myPdf.finishPage(myPage2);
            Calendar calendar = Calendar.getInstance();
            File externalStoragePublicDirectory = Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_DOWNLOADS);


            Long tsLong = System.currentTimeMillis()/1000;
            String ts = tsLong.toString();

            File file = new File(externalStoragePublicDirectory, "/Emi_" + ts + ".pdf");
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
        } else if (this.period > 30.0d) {
            Toast.makeText(this, "tenure should be less than 30 years", Toast.LENGTH_SHORT).show();
        } else {
            Toast.makeText(this, "Interest rate should be less than 50%", Toast.LENGTH_SHORT).show();
        }
    }


    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_emi);
        AdsManager.INSTANCE.loadBanner(this, findViewById(R.id.banner));



//        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
//        setSupportActionBar(toolbar);


        this.frontshare = getApplicationContext().getSharedPreferences(getPackageName(), 0);


        this.bmp = BitmapFactory.decodeResource(getResources(), R.drawable.emidetails);
        this.date = (Button) findViewById(R.id.date);
        this.scalebmp = Bitmap.createScaledBitmap(this.bmp, 1200, 2010, false);
        Bitmap decodeResource = BitmapFactory.decodeResource(getResources(), R.drawable.emipart);
        this.dusrabmp = decodeResource;
        this.scaledusrabmp = Bitmap.createScaledBitmap(decodeResource, 1200, 2010, false);
        LinearLayout mainlayout = (LinearLayout) findViewById(R.id.mainlinear);
        Animation animation = AnimationUtils.loadAnimation(this, R.anim.slideup);
        mainlayout.startAnimation(animation);
        SQLiteDatabase openOrCreateDatabase = openOrCreateDatabase("EMI", 0, null);
        this.myDatabase = openOrCreateDatabase;
        openOrCreateDatabase.execSQL("CREATE TABLE IF NOT EXISTS emiTable(name TEXT,principalAmount DOUBLE,interest DOUBLE,tenure DOUBLE,date TEXT,id INTEGER PRIMARY KEY)");
        this.principalamount = (EditText) findViewById(R.id.principalamoount);
        this.interestamount = (EditText) findViewById(R.id.interestamoount);
        this.year = (EditText) findViewById(R.id.tenure);
        this.totalemi = (TextView) findViewById(R.id.emi);
        this.totalinterest = (TextView) findViewById(R.id.totalinterest);
        this.totalprincipal = (TextView) findViewById(R.id.totalprncipal);
        this.totalpayement = (TextView) findViewById(R.id.totalpayement);
        this.frameLayout = (FrameLayout) findViewById(R.id.blur);
        this.group = (RadioGroup) findViewById(R.id.togle);
        Calendar c = Calendar.getInstance();
        this.toyear = c.get(1);
        this.tomonth = c.get(2) + 1;
        this.todate = c.get(5);
        switch (this.tomonth) {
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
        Button button = this.date;
        button.setText("First EMI: " + this.todate + " " + this.tmonth + " " + this.toyear);
        this.setListener = new DatePickerDialog.OnDateSetListener() {
            @Override
            public void onDateSet(DatePicker datePicker, int i, int i1, int i2) {
                emi.this.todate = i2;
                emi.this.tomonth = i1 + 1;
                emi.this.toyear = i;
                switch (emi.this.tomonth) {
                    case 1:
                        emi.this.tmonth = "Jan";
                        break;
                    case 2:
                        emi.this.tmonth = "Feb";
                        break;
                    case 3:
                        emi.this.tmonth = "Mar";
                        break;
                    case 4:
                        emi.this.tmonth = "April";
                        break;
                    case 5:
                        emi.this.tmonth = "May";
                        break;
                    case 6:
                        emi.this.tmonth = "Jun";
                        break;
                    case 7:
                        emi.this.tmonth = "July";
                        break;
                    case 8:
                        emi.this.tmonth = "Aug";
                        break;
                    case 9:
                        emi.this.tmonth = "Sep";
                        break;
                    case 10:
                        emi.this.tmonth = "Oct";
                        break;
                    case 11:
                        emi.this.tmonth = "Nov";
                        break;
                    case 12:
                        emi.this.tmonth = "Dec";
                        break;
                }
                Button button2 = emi.this.date;
                button2.setText("First EMI: " + emi.this.todate + " " + emi.this.tmonth + " " + emi.this.toyear);
            }
        };
        Intent intent = getIntent();
        String ext = intent.getStringExtra("Open");
        if (ext != null) {


            openingSaved(Integer.parseInt(ext));
        }
        this.date.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                emi emiVar = emi.this;
                DatePickerDialog datePickerDialog = new DatePickerDialog(emiVar, 16973936, emiVar.setListener, emi.this.toyear, emi.this.tomonth, emi.this.todate);
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

    public void historyFunction() {
        Cursor cursor = this.myDatabase.rawQuery("SELECT * FROM emiTable", null);
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


    public void openingSaved(int position) {
        char c;
        char c2;
        Cursor cursor = this.myDatabase.rawQuery("SELECT * FROM emiTable", null);
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
        this.principalamount.setText(String.valueOf(principle.get(position)));
        this.year.setText(String.valueOf(time.get(position)));
        this.interestamount.setText(String.valueOf(interest.get(position)));
        this.date.setText(String.valueOf(dateHistory.get(position)));
        String[] dateregex = dateHistory.get(position).split(" ");
        this.todate = Integer.parseInt(dateregex[0]);
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
                this.tomonth = 1;
                break;
            case 1:
                c2 = 2;
                this.tomonth = 2;
                break;
            case 2:
                this.tomonth = 3;
                c2 = 2;
                break;
            case 3:
                this.tomonth = 4;
                c2 = 2;
                break;
            case 4:
                this.tomonth = 5;
                c2 = 2;
                break;
            case 5:
                this.tomonth = 6;
                c2 = 2;
                break;
            case 6:
                this.tomonth = 7;
                c2 = 2;
                break;
            case 7:
                this.tomonth = 8;
                c2 = 2;
                break;
            case '\b':
                this.tomonth = 9;
                c2 = 2;
                break;
            case '\t':
                this.tomonth = 10;
                c2 = 2;
                break;
            case '\n':
                this.tomonth = 11;
                c2 = 2;
                break;
            case 11:
                this.tomonth = 12;
                c2 = 2;
                break;
            default:
                c2 = 2;
                break;
        }
        this.toyear = Integer.parseInt(dateregex[c2]);
        this.period = Double.valueOf(this.year.getText().toString()).doubleValue() * 12.0d;
        calculation();
    }

    public void save(View view) {
        double periodSql;
        if (!this.principalamount.getText().toString().isEmpty() && !this.interestamount.getText().toString().isEmpty() && !this.year.getText().toString().isEmpty()) {
            this.period = Double.valueOf(this.year.getText().toString()).doubleValue();
            int selectedid = this.group.getCheckedRadioButtonId();
            RadioButton radioButton = (RadioButton) findViewById(selectedid);
            this.radioButton = radioButton;
            if (radioButton.getText().toString().equals("year")) {
                this.period = Double.valueOf(this.year.getText().toString()).doubleValue() * 12.0d;
            } else {
                this.period = Double.valueOf(this.year.getText().toString()).doubleValue();
            }
            if (this.period <= 360.0d && Double.valueOf(this.interestamount.getText().toString()).doubleValue() <= 50.0d) {
                calculation();
                final double loanSql = Double.parseDouble(this.principalamount.getText().toString());
                final double interestSql = Double.parseDouble(this.interestamount.getText().toString());
                final String sqlDate = this.todate + " " + this.tmonth + " " + this.toyear;
                if (this.radioButton.getText().toString().equals("year")) {
                    periodSql = Double.valueOf(this.year.getText().toString()).doubleValue();
                } else {
                    periodSql = Double.valueOf(this.year.getText().toString()).doubleValue() / 12.0d;
                }
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
                final double d = periodSql;
                save.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        emi.this.name = naming.getText().toString();
                        if (emi.this.name.isEmpty()) {
                            emi.this.name = "EMPTY";
                        }
                        SQLiteDatabase sQLiteDatabase = emi.this.myDatabase;
                        sQLiteDatabase.execSQL("INSERT INTO emiTable(name,principalAmount,interest,tenure,date) VALUES ('" + emi.this.name + "'," + loanSql + "," + interestSql + "," + d + ",'" + sqlDate + "')");
                        entry.dismiss();
                    }
                });
                return;
            } else if (this.period > 360.0d) {
                Toast.makeText(this, "tenure should be less than 30 years", Toast.LENGTH_SHORT).show();
                return;
            } else {
                Toast.makeText(this, "Interest rate should be less than 50%", Toast.LENGTH_SHORT).show();
                return;
            }
        }
        Toast.makeText(getApplicationContext(), "Enter Inputs", Toast.LENGTH_SHORT).show();
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
