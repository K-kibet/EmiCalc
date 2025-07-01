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
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.FrameLayout;
import android.widget.ImageButton;
import android.widget.LinearLayout;
import android.widget.ListAdapter;
import android.widget.ListView;
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


public class ppf extends AppCompatActivity implements AdapterView.OnItemSelectedListener {


    TextView ansInvestment;

    Bitmap bmp;
    int date;
    Button day;
    double expectedRate;
    FrameLayout frameLayout;
    SharedPreferences frontshare;
    RadioGroup group;

    TextView interestText;
    double investmentAmount;
    EditText investmentEdit;
    boolean isCalculated;
    int mMonth;
    int mYear;


    TextView maturityDate;
    double maturityValue;
    int month;
    String msMonth;
    SQLiteDatabase myDatabase;
    String name;


    EditText rateEdit;

    String sMonth;
    Bitmap scalebmp;
    DatePickerDialog.OnDateSetListener setListener;
    Spinner spiner;
    double totalInterest;
    double totalInvestment;
    TextView valueMaturity;
    int year;
    double Tenure = 15.0d;
    List<String> spinner = new ArrayList();
    String choosen = "15 Years";

    public void calculate(View view) {
        InputMethodManager imm = (InputMethodManager) getSystemService(Context.INPUT_METHOD_SERVICE);
        imm.hideSoftInputFromWindow(view.getWindowToken(), 0);
        if (this.investmentEdit.getText().toString().isEmpty() || this.rateEdit.getText().toString().isEmpty()) {
            Toast.makeText(getApplicationContext(), "Enter Inputs", Toast.LENGTH_SHORT).show();
            return;
        }
        timing();
        if (Double.parseDouble(this.rateEdit.getText().toString()) <= 50.0d) {
            calculation();
        } else {
            Toast.makeText(this, "interestText rate should be less than 50%", Toast.LENGTH_SHORT).show();
        }
    }

    public void share(View view) {
        if (this.investmentEdit.getText().toString().isEmpty() || this.rateEdit.getText().toString().isEmpty()) {
            Toast.makeText(getApplicationContext(), "Enter Inputs", Toast.LENGTH_SHORT).show();
        } else if (Double.parseDouble(this.rateEdit.getText().toString()) <= 50.0d) {
            calculation();
            Intent intent = new Intent("android.intent.action.SEND");
            intent.setType("text/plain");
            intent.putExtra("android.intent.extra.SUBJECT", "EMI- A Calculator app");
            intent.putExtra("android.intent.extra.TEXT", "PPF Details-\n\ninvestmentEdit Amount : " + this.investmentAmount + "\nTenure : " + this.Tenure + "Year\nFirst SIP: " + this.date + " " + this.sMonth + " " + this.year + "\n\nTotal investmentEdit Amount: " + this.totalInvestment + "\nTotal interestText: " + this.totalInterest + "\nMaturity Value: " + this.maturityValue + "\nMaturity Date: " + this.date + " " + this.msMonth + " " + this.mYear + "\n\nCalculate by EMI\n" + "https://play.google.com/store/apps/details?id=" + view.getContext().getPackageName());
            startActivity(Intent.createChooser(intent, "Share Using"));
        } else {
            Toast.makeText(this, "interestText rate should be less than 50%", Toast.LENGTH_SHORT).show();
        }
    }

    public void pdf(View view) {
        if (this.investmentEdit.getText().toString().isEmpty() || this.rateEdit.getText().toString().isEmpty()) {
            Toast.makeText(getApplicationContext(), "Enter Inputs", Toast.LENGTH_SHORT).show();
        } else if (Double.parseDouble(this.rateEdit.getText().toString()) > 50.0d) {
            Toast.makeText(this, "interestText rate should be less than 50%", Toast.LENGTH_SHORT).show();
        } else {
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
            canvas.drawText(this.investmentAmount + " " + Constants.CURRENCY_STORED, 900.0f, 575.0f, myPaint);
            StringBuilder sb = new StringBuilder();
            sb.append(this.expectedRate * 100.0d);
            sb.append(" %");
            canvas.drawText(sb.toString(), 900.0f, 700.0f, myPaint);
            canvas.drawText(this.Tenure + "  months", 900.0f, 850.0f, myPaint);
            canvas.drawText(this.totalInvestment + " " + Constants.CURRENCY_STORED, 300.0f, 1150.0f, myPaint);
            canvas.drawText(this.totalInterest + " " + Constants.CURRENCY_STORED, 900.0f, 1150.0f, myPaint);
            canvas.drawText(this.maturityValue + " " + Constants.CURRENCY_STORED, 600.0f, 1450.0f, myPaint);
            canvas.drawText(this.date + " " + this.msMonth + " " + this.mYear, 600.0f, 1660.0f, myPaint);
            canvas.drawText(this.date + " " + this.sMonth + " " + this.year, 900.0f, 480.0f, myPaint);
            myPdf.finishPage(myPage);
            Calendar calendar = Calendar.getInstance();
            File externalStoragePublicDirectory = Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_DOWNLOADS);
            Long tsLong = System.currentTimeMillis()/1000;
            String ts = tsLong.toString();

            File file = new File(externalStoragePublicDirectory, "/Ppf_" + ts + ".pdf");
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
    }

    public void calculation() {
        NestedScrollView scrollView = (NestedScrollView) findViewById(R.id.scroller);

        scrollView.smoothScrollTo(0, 800);
        this.investmentAmount = Double.parseDouble(this.investmentEdit.getText().toString());
        timing();
        double parseDouble = Double.parseDouble(this.rateEdit.getText().toString());
        this.expectedRate = parseDouble;
        double d = parseDouble / 100.0d;
        this.expectedRate = d;
        double d2 = this.investmentAmount;
        double d3 = this.Tenure;
        this.totalInvestment = d2 * d3;
        double pow = (d2 * (Math.pow(d + 1.0d, d3) - 1.0d)) / this.expectedRate;
        this.maturityValue = pow;
        double d4 = pow - this.totalInvestment;
        this.totalInterest = d4;
        if (((int) d4) == d4) {
            int t = (int) d4;
            this.interestText.setText(String.valueOf(t));
        } else {
            BigDecimal bd = new BigDecimal(this.totalInterest).setScale(1, RoundingMode.HALF_UP);
            this.totalInterest = bd.doubleValue();
            TextView textView = this.interestText;
            textView.setText(this.totalInterest + " " + Constants.CURRENCY_STORED);
        }
        PrasingTheDouble prasingTheDouble = new PrasingTheDouble(this.totalInterest);
        TextView textView2 = this.interestText;
        textView2.setText(prasingTheDouble.getaDouble() + " " + Constants.CURRENCY_STORED);
        double d5 = this.totalInvestment;
        if (((int) d5) == d5) {
            int total = (int) d5;
            TextView textView3 = this.ansInvestment;
            textView3.setText(total + " " + Constants.CURRENCY_STORED);
        } else {
            BigDecimal bd2 = new BigDecimal(this.totalInvestment).setScale(1, RoundingMode.HALF_UP);
            this.totalInvestment = bd2.doubleValue();
            TextView textView4 = this.ansInvestment;
            textView4.setText(this.totalInvestment + " " + Constants.CURRENCY_STORED);
        }
        PrasingTheDouble prasingTheDouble2 = new PrasingTheDouble(this.totalInvestment);
        TextView textView5 = this.ansInvestment;
        textView5.setText(prasingTheDouble2.getaDouble() + " " + Constants.CURRENCY_STORED);
        double d6 = this.maturityValue;
        if (((int) d6) == d6) {
            int e = (int) d6;
            this.valueMaturity.setText(String.valueOf(e));
        } else {
            BigDecimal bd3 = new BigDecimal(this.maturityValue).setScale(1, RoundingMode.HALF_UP);
            this.maturityValue = bd3.doubleValue();
            TextView textView6 = this.valueMaturity;
            textView6.setText(this.maturityValue + " " + Constants.CURRENCY_STORED);
        }
        PrasingTheDouble prasingTheDouble3 = new PrasingTheDouble(this.maturityValue);
        TextView textView7 = this.valueMaturity;
        textView7.setText(prasingTheDouble3.getaDouble() + " " + Constants.CURRENCY_STORED);
        double d7 = (double) this.month;
        double d8 = this.Tenure;
        Double.isNaN(d7);
        int i = (int) (d7 + d8);
        this.mMonth = i;
        int index = (i - 1) / 12;
        this.mYear = this.year + index;
        int i2 = i - (index * 12);
        this.mMonth = i2;
        switch (i2) {
            case 1:
                this.msMonth = "Jan";
                break;
            case 2:
                this.msMonth = "Feb";
                break;
            case 3:
                this.msMonth = "Mar";
                break;
            case 4:
                this.msMonth = "April";
                break;
            case 5:
                this.msMonth = "May";
                break;
            case 6:
                this.msMonth = "Jun";
                break;
            case 7:
                this.msMonth = "July";
                break;
            case 8:
                this.msMonth = "Aug";
                break;
            case 9:
                this.msMonth = "Sep";
                break;
            case 10:
                this.msMonth = "Oct";
                break;
            case 11:
                this.msMonth = "Nov";
                break;
            case 12:
                this.msMonth = "Dec";
                break;
        }
        TextView textView8 = this.maturityDate;
        textView8.setText(this.date + " " + this.msMonth + " " + this.mYear);
    }

    public void timing() {
        String str = this.choosen;
        if (str == "15 Years") {
            this.Tenure = 15.0d;
        } else if (str == "20 Years") {
            this.Tenure = 20.0d;
        } else if (str == "25 Years") {
            this.Tenure = 25.0d;
        } else {
            this.Tenure = 30.0d;
        }
    }


    @SuppressLint({"MissingInflatedId", "ResourceType"})
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_ppf);


        AdsManager.INSTANCE.loadBanner(this, findViewById(R.id.banner));

//
//        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
//        setSupportActionBar(toolbar);


        this.frontshare = getApplicationContext().getSharedPreferences(getPackageName(), 0);


        LinearLayout mainlayout = (LinearLayout) findViewById(R.id.mainlinear);
        Animation animation = AnimationUtils.loadAnimation(this, R.anim.slideup);
        mainlayout.startAnimation(animation);
        SQLiteDatabase openOrCreateDatabase = openOrCreateDatabase("EMI", 0, null);
        this.myDatabase = openOrCreateDatabase;
        openOrCreateDatabase.execSQL("CREATE TABLE IF NOT EXISTS ppfTable(name TEXT,principalAmount DOUBLE,interest DOUBLE,tenure DOUBLE,date TEXT,id INTEGER PRIMARY KEY)");
        this.investmentEdit = (EditText) findViewById(R.id.InvestmentAmoount);
        this.rateEdit = (EditText) findViewById(R.id.interestAmount);
        this.day = (Button) findViewById(R.id.date);
        this.ansInvestment = (TextView) findViewById(R.id.totalInvestment);
        this.interestText = (TextView) findViewById(R.id.totalInterest);
        this.valueMaturity = (TextView) findViewById(R.id.MaturityValue);
        this.maturityDate = (TextView) findViewById(R.id.Matuirtydate);
        this.spiner = (Spinner) findViewById(R.id.spinnertenure);
        this.spinner.add("15 Years");
        this.spinner.add("20 Years");
        this.spinner.add("25 Years");
        this.spinner.add("30 Years");
        ArrayAdapter<String> dataAdapter = new ArrayAdapter<>(this, 17367048, this.spinner);
        dataAdapter.setDropDownViewResource(17367049);
        this.spiner.setAdapter((SpinnerAdapter) dataAdapter);
        this.spiner.setOnItemSelectedListener(this);
        this.frameLayout = (FrameLayout) findViewById(R.id.blur);
        this.group = (RadioGroup) findViewById(R.id.togle);
        Bitmap decodeResource = BitmapFactory.decodeResource(getResources(), R.drawable.ppfdetails);
        this.bmp = decodeResource;
        this.scalebmp = Bitmap.createScaledBitmap(decodeResource, 1200, 2010, false);
        Calendar c = Calendar.getInstance();
        this.year = c.get(1);
        this.month = c.get(2) + 1;
        this.date = c.get(5);
        switch (this.month) {
            case 1:
                this.sMonth = "Jan";
                break;
            case 2:
                this.sMonth = "Feb";
                break;
            case 3:
                this.sMonth = "Mar";
                break;
            case 4:
                this.sMonth = "April";
                break;
            case 5:
                this.sMonth = "May";
                break;
            case 6:
                this.sMonth = "Jun";
                break;
            case 7:
                this.sMonth = "July";
                break;
            case 8:
                this.sMonth = "Aug";
                break;
            case 9:
                this.sMonth = "Sep";
                break;
            case 10:
                this.sMonth = "Oct";
                break;
            case 11:
                this.sMonth = "Nov";
                break;
            case 12:
                this.sMonth = "Dec";
                break;
        }
        String presentDate = "First EMI: " + this.date + " " + this.sMonth + " " + this.year;
        this.day.setText(presentDate);
        this.setListener = new DatePickerDialog.OnDateSetListener() {
            @Override
            public void onDateSet(DatePicker datePicker, int i, int i1, int i2) {
                ppf.this.date = i2;
                ppf.this.month = i1 + 1;
                ppf.this.year = i;
                switch (ppf.this.month) {
                    case 1:
                        ppf.this.sMonth = "Jan";
                        break;
                    case 2:
                        ppf.this.sMonth = "Feb";
                        break;
                    case 3:
                        ppf.this.sMonth = "Mar";
                        break;
                    case 4:
                        ppf.this.sMonth = "April";
                        break;
                    case 5:
                        ppf.this.sMonth = "May";
                        break;
                    case 6:
                        ppf.this.sMonth = "Jun";
                        break;
                    case 7:
                        ppf.this.sMonth = "July";
                        break;
                    case 8:
                        ppf.this.sMonth = "Aug";
                        break;
                    case 9:
                        ppf.this.sMonth = "Sep";
                        break;
                    case 10:
                        ppf.this.sMonth = "Oct";
                        break;
                    case 11:
                        ppf.this.sMonth = "Nov";
                        break;
                    case 12:
                        ppf.this.sMonth = "Dec";
                        break;
                }
                Button button = ppf.this.day;
                button.setText("First EMI: " + String.valueOf(ppf.this.date) + " " + ppf.this.sMonth + " " + String.valueOf(ppf.this.year));
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
                ppf ppfVar = ppf.this;
                @SuppressLint("ResourceType") DatePickerDialog datePickerDialog = new DatePickerDialog(ppfVar, 16973936, ppfVar.setListener, ppf.this.year, ppf.this.month - 1, ppf.this.date);
                datePickerDialog.getWindow().setBackgroundDrawable(new ColorDrawable(0));
                datePickerDialog.show();
            }
        });

    }

    @Override
    public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
        this.choosen = parent.getItemAtPosition(position).toString();
        timing();
    }

    @Override
    public void onNothingSelected(AdapterView<?> parent) {
    }

    @Override
    public void onBackPressed() {
        this.frameLayout.setBackgroundColor(Color.parseColor("#00FFFFFF"));

            super.onBackPressed();

    }


    public void history(View view) {
        historyFunction();
    }

    public void historyFunction() {
        Cursor cursor = this.myDatabase.rawQuery("SELECT * FROM ppfTable", null);
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
        if (this.investmentEdit.getText().toString().isEmpty() || this.rateEdit.getText().toString().isEmpty()) {
            Toast.makeText(getApplicationContext(), "Enter Inputs", Toast.LENGTH_SHORT).show();
            return;
        }
        timing();
        if (Double.valueOf(this.rateEdit.getText().toString()).doubleValue() > 50.0d) {
            Toast.makeText(this, "Interest rate should be less than 50%", Toast.LENGTH_SHORT).show();
            return;
        }
        calculation();
        final double loanSql = Double.parseDouble(this.investmentEdit.getText().toString());
        final double interestSql = Double.parseDouble(this.rateEdit.getText().toString());
        final String sqlDate = this.date + " " + this.month + " " + this.year;
        final double periodSql = this.Tenure;
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
                ppf.this.name = naming.getText().toString();
                if (ppf.this.name.isEmpty()) {
                    ppf.this.name = "EMPTY";
                }
                SQLiteDatabase sQLiteDatabase = ppf.this.myDatabase;
                sQLiteDatabase.execSQL("INSERT INTO ppfTable(name,principalAmount,interest,tenure,date) VALUES ('" + ppf.this.name + "'," + loanSql + "," + interestSql + "," + periodSql + ",'" + sqlDate + "')");
                entry.dismiss();
            }
        });
    }


    public void openingSaved(final int position) {
        char c;
        char c2;
        Cursor cursor = this.myDatabase.rawQuery("SELECT * FROM ppfTable", null);
        List<Double> principle = new ArrayList<>();
        List<Integer> id = new ArrayList<>();
        List<String> name = new ArrayList<>();
        List<Double> interest = new ArrayList<>();
        final List<Double> time = new ArrayList<>();
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
        this.investmentEdit.setText(String.valueOf(principle.get(position)));
        this.spiner.post(new Runnable() {
            @Override
            public void run() {
                ppf.this.spiner.setSelection((int) ((((Double) time.get(position)).doubleValue() / 5.0d) - 3.0d));
            }
        });
        this.rateEdit.setText(String.valueOf(interest.get(position)));
        this.day.setText(String.valueOf(dateHistory.get(position)));
        String[] dateregex = dateHistory.get(position).split(" ");
        this.date = Integer.parseInt(dateregex[0]);
        String str = dateregex[1];
        this.sMonth = str;
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
        this.Tenure = time.get(position).doubleValue();
        calculation();
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
