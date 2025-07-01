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


public class Fd extends AppCompatActivity implements Statementsip.onMessageReadListner {


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
    int mDate;
    int mMonth;
    int mYear;


    TextView maturityDate;
    double maturityValue;
    int month;
    String msMonth;
    SQLiteDatabase myDatabase;
    String name;


    RadioButton radioButton;
    EditText rateEdit;

    String sMonth;
    Bitmap scalebmp;
    DatePickerDialog.OnDateSetListener setListener;
    double tenure;
    EditText timeEdit;
    double totalInterest;
    TextView valueMaturity;
    int year;

    public void calculate(View view) {
        InputMethodManager imm = (InputMethodManager) getSystemService(Context.INPUT_METHOD_SERVICE);
        imm.hideSoftInputFromWindow(view.getWindowToken(), 0);
        if (this.investmentEdit.getText().toString().isEmpty() || this.rateEdit.getText().toString().isEmpty() || this.timeEdit.getText().toString().isEmpty()) {
            Toast.makeText(getApplicationContext(), "Enter Inputs", Toast.LENGTH_SHORT).show();
            return;
        }
        this.tenure = Double.parseDouble(this.timeEdit.getText().toString());
        int selectedid = this.group.getCheckedRadioButtonId();
        RadioButton radioButton = (RadioButton) findViewById(selectedid);
        this.radioButton = radioButton;
        if (radioButton.getText().toString().equals("year")) {
            this.tenure = Double.parseDouble(this.timeEdit.getText().toString()) * 12.0d;
        } else {
            this.tenure = Double.parseDouble(this.timeEdit.getText().toString());
        }
        if (this.tenure <= 360.0d && Double.parseDouble(this.rateEdit.getText().toString()) <= 50.0d) {
            calculation();
        } else if (this.tenure > 360.0d) {
            Toast.makeText(this, "tenure should be less than 30 years", Toast.LENGTH_SHORT).show();
        } else {
            Toast.makeText(this, "interestText rate should be less than 50%", Toast.LENGTH_SHORT).show();
        }
    }

    public void share(View view) {
        if (this.investmentEdit.getText().toString().isEmpty() || this.rateEdit.getText().toString().isEmpty() || this.timeEdit.getText().toString().isEmpty()) {
            Toast.makeText(getApplicationContext(), "Enter Inputs", Toast.LENGTH_SHORT).show();
            return;
        }
        this.tenure = Double.parseDouble(this.timeEdit.getText().toString());
        int selectedid = this.group.getCheckedRadioButtonId();
        RadioButton radioButton = (RadioButton) findViewById(selectedid);
        this.radioButton = radioButton;
        if (radioButton.getText().toString().equals("year")) {
            this.tenure = Double.parseDouble(this.timeEdit.getText().toString()) * 12.0d;
        } else {
            this.tenure = Double.parseDouble(this.timeEdit.getText().toString());
        }
        if (this.tenure <= 360.0d && Double.parseDouble(this.rateEdit.getText().toString()) <= 50.0d) {
            calculation();
            Intent intent = new Intent("android.intent.action.SEND");
            intent.setType("text/plain");
            intent.putExtra("android.intent.extra.SUBJECT", "EMI- A Calculator app");
            intent.putExtra("android.intent.extra.TEXT", "FD Details-\n\ninvestmentEdit Amount : " + this.investmentAmount + "\ntenure : " + this.tenure + "months\nFirst SIP: " + this.date + " " + this.msMonth + " " + this.year + "\n\nTotal investmentEdit Amount: " + this.investmentAmount + "\nTotal interestText: " + this.totalInterest + "\nMaturity Value: " + this.maturityValue + "\nMaturity Date: " + this.date + " " + this.mMonth + " " + this.mYear + "\n\nCalculate by EMI\n" + "https://play.google.com/store/apps/details?id=" + view.getContext().getPackageName());
            startActivity(Intent.createChooser(intent, "Share Using"));
        } else if (this.tenure > 360.0d) {
            Toast.makeText(this, "tenure should be less than 30 years", Toast.LENGTH_SHORT).show();
        } else {
            Toast.makeText(this, "interestText rate should be less than 50%", Toast.LENGTH_SHORT).show();
        }
    }

    public void pdf(View view) {
        if (this.investmentEdit.getText().toString().isEmpty() || this.rateEdit.getText().toString().isEmpty() || this.timeEdit.getText().toString().isEmpty()) {
            Toast.makeText(getApplicationContext(), "Enter Inputs", Toast.LENGTH_SHORT).show();
            return;
        }
        this.tenure = Double.parseDouble(this.timeEdit.getText().toString());
        int selectedid = this.group.getCheckedRadioButtonId();
        RadioButton radioButton = (RadioButton) findViewById(selectedid);
        this.radioButton = radioButton;
        if (radioButton.getText().toString().equals("year")) {
            this.tenure = Double.parseDouble(this.timeEdit.getText().toString()) * 12.0d;
        } else {
            this.tenure = Double.parseDouble(this.timeEdit.getText().toString());
        }
        if (this.tenure <= 360.0d && Double.parseDouble(this.rateEdit.getText().toString()) <= 50.0d) {
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
            sb.append(this.expectedRate * 1200.0d);
            sb.append(" %");
            canvas.drawText(sb.toString(), 900.0f, 700.0f, myPaint);
            canvas.drawText(this.tenure + "  months", 900.0f, 850.0f, myPaint);
            canvas.drawText(this.investmentAmount + " " + Constants.CURRENCY_STORED, 300.0f, 1150.0f, myPaint);
            canvas.drawText(this.totalInterest + " " + Constants.CURRENCY_STORED, 900.0f, 1150.0f, myPaint);
            canvas.drawText(this.maturityValue + " " + Constants.CURRENCY_STORED, 600.0f, 1450.0f, myPaint);
            canvas.drawText(this.date + " " + this.msMonth + " " + this.mYear, 600.0f, 1660.0f, myPaint);
            canvas.drawText(this.date + " " + this.mMonth + " " + this.year, 900.0f, 480.0f, myPaint);
            myPdf.finishPage(myPage);
            Calendar calendar = Calendar.getInstance();
            File externalStoragePublicDirectory = Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_DOWNLOADS);

            Long tsLong = System.currentTimeMillis()/1000;
            String ts = tsLong.toString();
            File file = new File(externalStoragePublicDirectory, "/FD_" + ts + ".pdf");
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
        } else if (this.tenure > 360.0d) {
            Toast.makeText(this, "tenure should be less than 30 years", Toast.LENGTH_SHORT).show();
        } else {
            Toast.makeText(this, "interestText rate should be less than 50%", Toast.LENGTH_SHORT).show();
        }
    }

    public void calculation() {
        this.investmentAmount = Double.parseDouble(this.investmentEdit.getText().toString());
        this.tenure = Double.parseDouble(this.timeEdit.getText().toString());
        int selectedid = this.group.getCheckedRadioButtonId();
        RadioButton radioButton = (RadioButton) findViewById(selectedid);
        this.radioButton = radioButton;
        if (radioButton.getText().toString().equals("year")) {
            this.tenure = Double.parseDouble(this.timeEdit.getText().toString()) * 12.0d;
        } else {
            this.tenure = Double.parseDouble(this.timeEdit.getText().toString());
        }
        double parseDouble = Double.parseDouble(this.rateEdit.getText().toString());
        this.expectedRate = parseDouble;
        double d = parseDouble / 1200.0d;
        this.expectedRate = d;
        double pow = this.investmentAmount * Math.pow(d + 1.0d, this.tenure);
        this.maturityValue = pow;
        double d2 = pow - this.investmentAmount;
        this.totalInterest = d2;
        if (((int) d2) == d2) {
            int t = (int) d2;
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
        double d3 = this.investmentAmount;
        if (((int) d3) == d3) {
            int total = (int) d3;
            TextView textView3 = this.ansInvestment;
            textView3.setText(total + " " + Constants.CURRENCY_STORED);
        } else {
            BigDecimal bd2 = new BigDecimal(this.investmentAmount).setScale(1, RoundingMode.HALF_UP);
            this.investmentAmount = bd2.doubleValue();
            TextView textView4 = this.ansInvestment;
            textView4.setText(this.investmentAmount + " " + Constants.CURRENCY_STORED);
        }
        PrasingTheDouble prasingTheDouble2 = new PrasingTheDouble(this.investmentAmount);
        TextView textView5 = this.ansInvestment;
        textView5.setText(prasingTheDouble2.getaDouble() + " " + Constants.CURRENCY_STORED);
        double d4 = this.maturityValue;
        if (((int) d4) == d4) {
            int e = (int) d4;
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
        double d5 = (double) this.month;
        double d6 = this.tenure;
        Double.isNaN(d5);
        int i = (int) (d5 + d6);
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

    public void statistic(View view) {
        InputMethodManager imm = (InputMethodManager) getSystemService(Context.INPUT_METHOD_SERVICE);
        imm.hideSoftInputFromWindow(view.getWindowToken(), 0);
        if (!this.investmentEdit.getText().toString().isEmpty() && !this.rateEdit.getText().toString().isEmpty() && !this.timeEdit.getText().toString().isEmpty()) {
            this.tenure = Double.parseDouble(this.timeEdit.getText().toString());
            int selectedid = this.group.getCheckedRadioButtonId();
            RadioButton radioButton = (RadioButton) findViewById(selectedid);
            this.radioButton = radioButton;
            if (radioButton.getText().toString().equals("year")) {
                this.tenure = Double.parseDouble(this.timeEdit.getText().toString()) * 12.0d;
            } else {
                this.tenure = Double.parseDouble(this.timeEdit.getText().toString());
            }
            if (this.tenure <= 360.0d && Double.parseDouble(this.rateEdit.getText().toString()) <= 50.0d) {
                calculation();
                this.frameLayout.setBackgroundColor(Color.parseColor("#59000000"));
                Statementsip statement = new Statementsip(0.0d, this.tenure, this.expectedRate, this.investmentAmount, this.maturityValue, this.totalInterest, this.date, this.month, this.year, "FD");
                FragmentManager manager = getSupportFragmentManager();
                FragmentTransaction transaction = manager.beginTransaction();
                transaction.addToBackStack(null);
                transaction.setCustomAnimations(R.anim.segmentup, R.anim.segmentdown);
                transaction.add(R.id.frame, statement, "Fragment").commit();
                return;
            } else if (this.tenure > 360.0d) {
                Toast.makeText(this, "tenure should be less than 30 years", Toast.LENGTH_SHORT).show();
                return;
            } else {
                Toast.makeText(this, "interestText rate should be less than 50%", Toast.LENGTH_SHORT).show();
                return;
            }
        }
        Toast.makeText(getApplicationContext(), "Enter Inputs", Toast.LENGTH_SHORT).show();
    }


    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_fd);


        AdsManager.INSTANCE.loadBanner(this, findViewById(R.id.banner));



//        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
//        setSupportActionBar(toolbar);

        this.frontshare = getApplicationContext().getSharedPreferences(getPackageName(), 0);


        LinearLayout mainlayout = (LinearLayout) findViewById(R.id.mainlinear);
        Animation animation = AnimationUtils.loadAnimation(this, R.anim.slideup);
        mainlayout.startAnimation(animation);
        SQLiteDatabase openOrCreateDatabase = openOrCreateDatabase("EMI", 0, null);
        this.myDatabase = openOrCreateDatabase;
        openOrCreateDatabase.execSQL("CREATE TABLE IF NOT EXISTS fdTable(name TEXT,principalAmount DOUBLE,interest DOUBLE,tenure DOUBLE,date TEXT,id INTEGER PRIMARY KEY)");
        this.investmentEdit = (EditText) findViewById(R.id.InvestmentAmoount);
        this.rateEdit = (EditText) findViewById(R.id.interestAmount);
        this.timeEdit = (EditText) findViewById(R.id.tenure);
        this.day = (Button) findViewById(R.id.date);
        this.ansInvestment = (TextView) findViewById(R.id.totalInvestment);
        this.interestText = (TextView) findViewById(R.id.totalInterest);
        this.valueMaturity = (TextView) findViewById(R.id.MaturityValue);
        this.maturityDate = (TextView) findViewById(R.id.Matuirtydate);
        this.frameLayout = (FrameLayout) findViewById(R.id.blur);
        this.group = (RadioGroup) findViewById(R.id.togle);
        Bitmap decodeResource = BitmapFactory.decodeResource(getResources(), R.drawable.fddetails);
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
        Button button = this.day;
        button.setText("First EMI: " + this.date + " " + this.sMonth + " " + this.year);
        this.setListener = new DatePickerDialog.OnDateSetListener() {
            @Override
            public void onDateSet(DatePicker datePicker, int i, int i1, int i2) {
                Fd.this.date = i2;
                Fd.this.month = i1 + 1;
                Fd.this.year = i;
                switch (Fd.this.month) {
                    case 1:
                        Fd.this.sMonth = "Jan";
                        break;
                    case 2:
                        Fd.this.sMonth = "Feb";
                        break;
                    case 3:
                        Fd.this.sMonth = "Mar";
                        break;
                    case 4:
                        Fd.this.sMonth = "April";
                        break;
                    case 5:
                        Fd.this.sMonth = "May";
                        break;
                    case 6:
                        Fd.this.sMonth = "Jun";
                        break;
                    case 7:
                        Fd.this.sMonth = "July";
                        break;
                    case 8:
                        Fd.this.sMonth = "Aug";
                        break;
                    case 9:
                        Fd.this.sMonth = "Sep";
                        break;
                    case 10:
                        Fd.this.sMonth = "Oct";
                        break;
                    case 11:
                        Fd.this.sMonth = "Nov";
                        break;
                    case 12:
                        Fd.this.sMonth = "Dec";
                        break;
                }
                Button button2 = Fd.this.day;
                button2.setText("First EMI: " + Fd.this.date + " " + Fd.this.sMonth + " " + Fd.this.year);
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
                Fd fd = Fd.this;
                @SuppressLint("ResourceType") DatePickerDialog datePickerDialog = new DatePickerDialog(fd, 16973936, fd.setListener, Fd.this.year, Fd.this.month - 1, Fd.this.date);
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


    public void history(View view) {
        historyFunction();
    }

    public void historyFunction() {
        Cursor cursor = this.myDatabase.rawQuery("SELECT * FROM fdTable", null);
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
        double periodSql;
        if (!this.investmentEdit.getText().toString().isEmpty() && !this.rateEdit.getText().toString().isEmpty() && !this.timeEdit.getText().toString().isEmpty()) {
            this.tenure = Double.parseDouble(this.timeEdit.getText().toString());
            int selectedid = this.group.getCheckedRadioButtonId();
            RadioButton radioButton = (RadioButton) findViewById(selectedid);
            this.radioButton = radioButton;
            if (radioButton.getText().toString().equals("year")) {
                this.tenure = Double.parseDouble(this.timeEdit.getText().toString()) * 12.0d;
            } else {
                this.tenure = Double.parseDouble(this.timeEdit.getText().toString());
            }
            if (this.tenure <= 360.0d && Double.parseDouble(this.rateEdit.getText().toString()) <= 50.0d) {
                calculation();
                final double loanSql = Double.parseDouble(this.investmentEdit.getText().toString());
                final double interestSql = Double.parseDouble(this.rateEdit.getText().toString());
                final String sqlDate = this.date + " " + this.month + " " + this.year;
                if (this.radioButton.getText().toString().equals("year")) {
                    periodSql = Double.parseDouble(this.timeEdit.getText().toString());
                } else {
                    periodSql = Double.parseDouble(this.timeEdit.getText().toString()) / 12.0d;
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
                        Fd.this.name = naming.getText().toString();
                        if (Fd.this.name.isEmpty()) {
                            Fd.this.name = "EMPTY";
                        }
                        SQLiteDatabase sQLiteDatabase = Fd.this.myDatabase;
                        sQLiteDatabase.execSQL("INSERT INTO fdTable(name,principalAmount,interest,tenure,date) VALUES ('" + Fd.this.name + "'," + loanSql + "," + interestSql + "," + d + ",'" + sqlDate + "')");
                        entry.dismiss();
                    }
                });
                return;
            } else if (this.tenure > 360.0d) {
                Toast.makeText(this, "tenure should be less than 30 years", Toast.LENGTH_SHORT).show();
                return;
            } else {
                Toast.makeText(this, "Interest rate should be less than 50%", Toast.LENGTH_SHORT).show();
                return;
            }
        }
        Toast.makeText(getApplicationContext(), "Enter Inputs", Toast.LENGTH_SHORT).show();
    }


    public void openingSaved(int position) {
        char c;
        char c2;
        Cursor cursor = this.myDatabase.rawQuery("SELECT * FROM fdTable", null);
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
        this.investmentEdit.setText(String.valueOf(principle.get(position)));
        this.timeEdit.setText(String.valueOf(time.get(position)));
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
        this.tenure = Double.valueOf(this.timeEdit.getText().toString()).doubleValue() * 12.0d;
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
