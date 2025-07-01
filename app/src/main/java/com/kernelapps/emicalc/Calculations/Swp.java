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


public class Swp extends AppCompatActivity implements Statementsip.onMessageReadListner {
    String Mmonth;
    double Tenure;
    EditText Time;


    Bitmap bmp;
    int date;
    Button day;
    double expectedRate;
    FrameLayout frameLayout;
    SharedPreferences frontshare;
    RadioGroup group;

    EditText investment;
    double investmentAmount;
    boolean isCalculated;


    TextView maturity;
    TextView maturityDate;
    double maturityValue;
    int mmonth;
    int month;
    SQLiteDatabase myDatabase;
    int myear;
    String name;


    RadioButton radioButton;
    EditText rate;

    Bitmap scalebmp;
    DatePickerDialog.OnDateSetListener setListener;
    String tmonth;
    TextView totalInterest;
    double totalInterestAmount;
    double totalInvestAmount;
    TextView totalInvestment;
    EditText withdrawal;
    double withdrawalAmount;
    int year;

    public void calculate(View view) {
        InputMethodManager imm = (InputMethodManager) getSystemService(Context.INPUT_METHOD_SERVICE);
        imm.hideSoftInputFromWindow(view.getWindowToken(), 0);
        if (this.investment.getText().toString().isEmpty() || this.rate.getText().toString().isEmpty() || this.Time.getText().toString().isEmpty() || this.withdrawal.getText().toString().isEmpty()) {
            Toast.makeText(getApplicationContext(), "Enter Inputs", Toast.LENGTH_SHORT).show();
            return;
        }
        this.Tenure = Double.parseDouble(this.Time.getText().toString());
        int selectedid = this.group.getCheckedRadioButtonId();
        RadioButton radioButton = (RadioButton) findViewById(selectedid);
        this.radioButton = radioButton;
        if (radioButton.getText().toString().equals("year")) {
            this.Tenure = Double.parseDouble(this.Time.getText().toString()) * 12.0d;
        } else {
            this.Tenure = Double.parseDouble(this.Time.getText().toString());
        }
        if (this.Tenure <= 360.0d && Double.parseDouble(this.rate.getText().toString()) <= 50.0d) {
            calculation();
        } else if (this.Tenure > 360.0d) {
            Toast.makeText(this, "Tenure should be less than 30 years", Toast.LENGTH_SHORT).show();
        } else {
            Toast.makeText(this, "Interest rate should be less than 50%", Toast.LENGTH_SHORT).show();
        }
    }

    public void share(View view) {
        if (this.investment.getText().toString().isEmpty() || this.rate.getText().toString().isEmpty() || this.Time.getText().toString().isEmpty() || this.withdrawal.getText().toString().isEmpty()) {
            Toast.makeText(getApplicationContext(), "Enter Inputs", Toast.LENGTH_SHORT).show();
            return;
        }
        this.Tenure = Double.parseDouble(this.Time.getText().toString());
        int selectedid = this.group.getCheckedRadioButtonId();
        RadioButton radioButton = (RadioButton) findViewById(selectedid);
        this.radioButton = radioButton;
        if (radioButton.getText().toString().equals("year")) {
            this.Tenure = Double.parseDouble(this.Time.getText().toString()) * 12.0d;
        } else {
            this.Tenure = Double.parseDouble(this.Time.getText().toString());
        }
        if (this.Tenure <= 360.0d && Double.parseDouble(this.rate.getText().toString()) <= 50.0d) {
            calculation();
            Intent intent = new Intent("android.intent.action.SEND");
            intent.setType("text/plain");
            intent.putExtra("android.intent.extra.SUBJECT", "EMi- A Calculator app");
            intent.putExtra("android.intent.extra.TEXT", "SWP Details-\n\ninvestment Amount : " + this.investmentAmount + "\nTenure : " + this.Tenure + "months\nFirst SIP: " + this.date + " " + this.tmonth + " " + this.year + "\n\nTotal investment Amount: " + this.totalInvestAmount + "\nTotal Interest: " + this.totalInterestAmount + "\nmaturity Value: " + this.maturityValue + "\nmaturity Date: " + this.date + " " + this.Mmonth + " " + this.myear + "\n\nCalculate by EMI\n" + "https://play.google.com/store/apps/details?id=" + view.getContext().getPackageName());
            startActivity(Intent.createChooser(intent, "Share Using"));
        } else if (this.Tenure > 360.0d) {
            Toast.makeText(this, "tenure should be less than 30 years", Toast.LENGTH_SHORT).show();
        } else {
            Toast.makeText(this, "Interest rate should be less than 50%", Toast.LENGTH_SHORT).show();
        }
    }

    public void pdf(View view) {
        if (!this.investment.getText().toString().isEmpty() && !this.rate.getText().toString().isEmpty() && !this.Time.getText().toString().isEmpty() && !this.withdrawal.getText().toString().isEmpty()) {
            this.Tenure = Double.parseDouble(this.Time.getText().toString());
            int selectedid = this.group.getCheckedRadioButtonId();
            RadioButton radioButton = (RadioButton) findViewById(selectedid);
            this.radioButton = radioButton;
            if (radioButton.getText().toString().equals("year")) {
                this.Tenure = Double.parseDouble(this.Time.getText().toString()) * 12.0d;
            } else {
                this.Tenure = Double.parseDouble(this.Time.getText().toString());
            }
            if (this.Tenure <= 360.0d && Double.parseDouble(this.rate.getText().toString()) <= 50.0d) {
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
                canvas.drawText(this.investmentAmount + " ₹", 900.0f, 575.0f, myPaint);
                canvas.drawText((this.expectedRate * 1200.0d) + " %", 900.0f, 700.0f, myPaint);
                canvas.drawText(this.Tenure + "  " + this.radioButton.getText().toString(), 900.0f, 850.0f, myPaint);
                StringBuilder sb = new StringBuilder();
                sb.append(this.totalInvestAmount);
                sb.append(" ₹");
                canvas.drawText(sb.toString(), 300.0f, 1150.0f, myPaint);
                canvas.drawText(this.totalInterestAmount + " ₹", 900.0f, 1150.0f, myPaint);
                canvas.drawText(this.maturityValue + " ₹", 600.0f, 1450.0f, myPaint);
                canvas.drawText(this.date + " " + this.Mmonth + " " + this.myear, 600.0f, 1660.0f, myPaint);
                canvas.drawText(this.date + " " + this.tmonth + " " + this.year, 900.0f, 480.0f, myPaint);
                myPdf.finishPage(myPage);
                Calendar calendar = Calendar.getInstance();
                File externalStoragePublicDirectory = Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_DOWNLOADS);

                Long tsLong = System.currentTimeMillis()/1000;
                String ts = tsLong.toString();

                File file = new File(externalStoragePublicDirectory, "/swp_" + ts + ".pdf");
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
                Toast.makeText(this, "Interest rate should be less than 50%", Toast.LENGTH_SHORT).show();
                return;
            }
        }
        Toast.makeText(getApplicationContext(), "Enter Inputs", Toast.LENGTH_SHORT).show();
    }

    public void calculation() {
        double d;
        this.investmentAmount = Double.parseDouble(this.investment.getText().toString());
        this.Tenure = Double.parseDouble(this.Time.getText().toString());
        this.withdrawalAmount = Double.parseDouble(this.withdrawal.getText().toString());
        int selectedid = this.group.getCheckedRadioButtonId();
        RadioButton radioButton = (RadioButton) findViewById(selectedid);
        this.radioButton = radioButton;
        if (radioButton.getText().toString().equals("year")) {
            this.Tenure = Double.parseDouble(this.Time.getText().toString()) * 12.0d;
        } else {
            this.Tenure = Double.parseDouble(this.Time.getText().toString());
        }
        double parseDouble = Double.parseDouble(this.rate.getText().toString());
        this.expectedRate = parseDouble;
        this.expectedRate = parseDouble / 1200.0d;
        double ans = 0.0d;
        int i = 1;
        while (true) {
            d = this.Tenure;
            if (i > d) {
                break;
            }
            ans += Math.pow(this.expectedRate + 1.0d, d - 1.0d);
            i++;
        }
        double pow = this.investmentAmount * Math.pow(this.expectedRate + 1.0d, d);
        double d2 = this.withdrawalAmount;
        double d3 = pow - (d2 * ans);
        this.maturityValue = d3;
        this.totalInvestAmount = this.investmentAmount;
        double d4 = d3 - (d2 * this.Tenure);
        this.totalInterestAmount = d4;
        if (((int) d4) == d4) {
            int t = (int) d4;
            this.totalInterest.setText(String.valueOf(t));
        } else {
            BigDecimal bd = new BigDecimal(this.totalInterestAmount).setScale(1, RoundingMode.HALF_UP);
            this.totalInterestAmount = bd.doubleValue();
            TextView textView = this.totalInterest;
            textView.setText(this.totalInterestAmount + " " + Constants.CURRENCY_STORED);
        }
        PrasingTheDouble prasingTheDouble = new PrasingTheDouble(this.totalInterestAmount);
        TextView textView2 = this.totalInterest;
        textView2.setText(prasingTheDouble.getaDouble() + " ₹");
        double d5 = this.totalInvestAmount;
        if (((int) d5) == d5) {
            int total = (int) d5;
            TextView textView3 = this.totalInvestment;
            textView3.setText(total + " ₹");
        } else {
            BigDecimal bd2 = new BigDecimal(this.totalInvestAmount).setScale(1, RoundingMode.HALF_UP);
            this.totalInvestAmount = bd2.doubleValue();
            TextView textView4 = this.totalInvestment;
            textView4.setText(this.totalInvestAmount + " ₹");
        }
        PrasingTheDouble prasingTheDouble2 = new PrasingTheDouble(this.totalInvestAmount);
        TextView textView5 = this.totalInvestment;
        textView5.setText(prasingTheDouble2.getaDouble() + " ₹");
        double d6 = this.maturityValue;
        if (((int) d6) == d6) {
            int e = (int) d6;
            this.maturity.setText(String.valueOf(e));
        } else {
            BigDecimal bd3 = new BigDecimal(this.maturityValue).setScale(1, RoundingMode.HALF_UP);
            this.maturityValue = bd3.doubleValue();
            TextView textView6 = this.maturity;
            textView6.setText(this.maturityValue + " ₹");
        }
        PrasingTheDouble prasingTheDouble3 = new PrasingTheDouble(this.maturityValue);
        TextView textView7 = this.maturity;
        textView7.setText(prasingTheDouble3.getaDouble() + " ₹");
        double d7 = (double) this.month;
        double d8 = this.Tenure;
        Double.isNaN(d7);
        int i2 = (int) (d7 + d8);
        this.mmonth = i2;
        int index = (i2 - 1) / 12;
        this.myear = this.year + index;
        int i3 = i2 - (index * 12);
        this.mmonth = i3;
        switch (i3) {
            case 1:
                this.Mmonth = "Jan";
                break;
            case 2:
                this.Mmonth = "Feb";
                break;
            case 3:
                this.Mmonth = "Mar";
                break;
            case 4:
                this.Mmonth = "April";
                break;
            case 5:
                this.Mmonth = "May";
                break;
            case 6:
                this.Mmonth = "Jun";
                break;
            case 7:
                this.Mmonth = "July";
                break;
            case 8:
                this.Mmonth = "Aug";
                break;
            case 9:
                this.Mmonth = "Sep";
                break;
            case 10:
                this.Mmonth = "Oct";
                break;
            case 11:
                this.Mmonth = "Nov";
                break;
            case 12:
                this.Mmonth = "Dec";
                break;
        }
        TextView textView8 = this.maturityDate;
        textView8.setText(this.date + " " + this.Mmonth + " " + this.myear);
    }

    public void statistic(View view) {
        InputMethodManager imm = (InputMethodManager) getSystemService(Context.INPUT_METHOD_SERVICE);
        imm.hideSoftInputFromWindow(view.getWindowToken(), 0);
        if (!this.investment.getText().toString().isEmpty() && !this.rate.getText().toString().isEmpty() && !this.Time.getText().toString().isEmpty()) {
            if (!this.withdrawal.getText().toString().isEmpty()) {
                this.Tenure = Double.parseDouble(this.Time.getText().toString());
                int selectedid = this.group.getCheckedRadioButtonId();
                RadioButton radioButton = (RadioButton) findViewById(selectedid);
                this.radioButton = radioButton;
                if (radioButton.getText().toString().equals("year")) {
                    this.Tenure = Double.parseDouble(this.Time.getText().toString()) * 12.0d;
                } else {
                    this.Tenure = Double.parseDouble(this.Time.getText().toString());
                }
                if (this.Tenure <= 360.0d && Double.parseDouble(this.rate.getText().toString()) <= 50.0d) {
                    calculation();
                    this.frameLayout.setBackgroundColor(Color.parseColor("#59000000"));
                    Statementsip statement = new Statementsip(this.withdrawalAmount, this.Tenure, this.expectedRate, this.investmentAmount, this.maturityValue, this.totalInterestAmount, this.date, this.month, this.year, "SWP");
                    FragmentManager manager = getSupportFragmentManager();
                    FragmentTransaction transaction = manager.beginTransaction();
                    transaction.addToBackStack(null);
                    transaction.setCustomAnimations(R.anim.segmentup, R.anim.segmentdown);
                    transaction.add(R.id.frame, statement, "Fragment").commit();
                    return;
                } else if (this.Tenure > 360.0d) {
                    Toast.makeText(this, "tenure should be less than 30 years", Toast.LENGTH_SHORT).show();
                    return;
                } else {
                    Toast.makeText(this, "Interest rate should be less than 50%", Toast.LENGTH_SHORT).show();
                    return;
                }
            }
        }
        Toast.makeText(getApplicationContext(), "Enter Inputs", Toast.LENGTH_SHORT).show();
    }


    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_swp);
        AdsManager.INSTANCE.loadBanner(this, findViewById(R.id.banner));


//
//        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
//        setSupportActionBar(toolbar);

        this.frontshare = getApplicationContext().getSharedPreferences(getPackageName(), 0);


        this.investment = (EditText) findViewById(R.id.InvestmentAmoount);
        this.rate = (EditText) findViewById(R.id.interestAmount);
        this.Time = (EditText) findViewById(R.id.tenure);
        this.withdrawal = (EditText) findViewById(R.id.withdrawalAmoount);
        this.totalInvestment = (TextView) findViewById(R.id.totalInvestment);
        this.totalInterest = (TextView) findViewById(R.id.totalInterest);
        this.maturityDate = (TextView) findViewById(R.id.Matuirtydate);
        this.maturity = (TextView) findViewById(R.id.MaturityValue);


        SQLiteDatabase openOrCreateDatabase = openOrCreateDatabase("EMI", 0, null);
        this.myDatabase = openOrCreateDatabase;
        openOrCreateDatabase.execSQL("CREATE TABLE IF NOT EXISTS swpTable(name TEXT,principalAmount DOUBLE,interest DOUBLE,tenure DOUBLE,date TEXT,id INTEGER PRIMARY KEY,withdrawal DOUBLE)");
        this.day = (Button) findViewById(R.id.date);
        Bitmap decodeResource = BitmapFactory.decodeResource(getResources(), R.drawable.swpdetail);
        this.bmp = decodeResource;
        this.scalebmp = Bitmap.createScaledBitmap(decodeResource, 1200, 2010, false);
        LinearLayout mainlayout = (LinearLayout) findViewById(R.id.mainlinear);
        Animation animation = AnimationUtils.loadAnimation(this, R.anim.slideup);
        mainlayout.startAnimation(animation);
        this.frameLayout = (FrameLayout) findViewById(R.id.blur);
        this.group = (RadioGroup) findViewById(R.id.togle);
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
                Swp.this.date = i2;
                Swp.this.month = i1 + 1;
                Swp.this.year = i;
                switch (Swp.this.month) {
                    case 1:
                        Swp.this.tmonth = "Jan";
                        break;
                    case 2:
                        Swp.this.tmonth = "Feb";
                        break;
                    case 3:
                        Swp.this.tmonth = "Mar";
                        break;
                    case 4:
                        Swp.this.tmonth = "April";
                        break;
                    case 5:
                        Swp.this.tmonth = "May";
                        break;
                    case 6:
                        Swp.this.tmonth = "Jun";
                        break;
                    case 7:
                        Swp.this.tmonth = "July";
                        break;
                    case 8:
                        Swp.this.tmonth = "Aug";
                        break;
                    case 9:
                        Swp.this.tmonth = "Sep";
                        break;
                    case 10:
                        Swp.this.tmonth = "Oct";
                        break;
                    case 11:
                        Swp.this.tmonth = "Nov";
                        break;
                    case 12:
                        Swp.this.tmonth = "Dec";
                        break;
                }
                Button button2 = Swp.this.day;
                button2.setText("First EMI: " + Swp.this.date + " " + Swp.this.tmonth + " " + Swp.this.year);
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
                Swp swp = Swp.this;
                @SuppressLint("ResourceType") DatePickerDialog datePickerDialog = new DatePickerDialog(swp, 16973936, swp.setListener, Swp.this.year, Swp.this.month - 1, Swp.this.date);
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
        Cursor cursor = this.myDatabase.rawQuery("SELECT * FROM swpTable", null);
        List<Double> principle = new ArrayList<>();
        List<Integer> id = new ArrayList<>();
        List<String> name = new ArrayList<>();
        List<Double> interest = new ArrayList<>();
        List<Double> time = new ArrayList<>();
        List<String> dateHistory = new ArrayList<>();
        List<Double> withdraw = new ArrayList<>();
        int withIndex = cursor.getColumnIndex("withdrawal");
        int idIndex = cursor.getColumnIndex("id");
        int nameIndex = cursor.getColumnIndex("name");
        int principalIndex = cursor.getColumnIndex("principalAmount");
        int interetIndex = cursor.getColumnIndex("interest");
        int tenureIndex = cursor.getColumnIndex("tenure");
        int dateIndex = cursor.getColumnIndex("date");
        cursor.moveToFirst();
        while (!cursor.isAfterLast()) {
            withdraw.add(Double.valueOf(cursor.getDouble(withIndex)));
            name.add(cursor.getString(nameIndex));
            id.add(Integer.valueOf(cursor.getInt(idIndex)));
            principle.add(Double.valueOf(cursor.getDouble(principalIndex)));
            interest.add(Double.valueOf(cursor.getDouble(interetIndex)));
            time.add(Double.valueOf(cursor.getDouble(tenureIndex)));
            dateHistory.add(cursor.getString(dateIndex));
            cursor.moveToNext();
            withIndex = withIndex;
        }
        this.withdrawal.setText(String.valueOf(withdraw.get(position)));
        this.investment.setText(String.valueOf(principle.get(position)));
        this.Time.setText(String.valueOf(time.get(position)));
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
        this.Tenure = Double.valueOf(this.Time.getText().toString()).doubleValue() * 12.0d;
        calculation();
    }

    public void history(View view) {
        historyFunction();
    }

    public void historyFunction() {
        Cursor cursor = this.myDatabase.rawQuery("SELECT * FROM swpTable", null);
        List<Double> principle = new ArrayList<>();
        List<Integer> id = new ArrayList<>();
        List<String> name = new ArrayList<>();
        List<Double> interest = new ArrayList<>();
        List<Double> time = new ArrayList<>();
        List<String> dateHistory = new ArrayList<>();
        List<Double> withdrawal = new ArrayList<>();
        int withIndex = cursor.getColumnIndex("withdrawal");
        int idIndex = cursor.getColumnIndex("id");
        int nameIndex = cursor.getColumnIndex("name");
        int principalIndex = cursor.getColumnIndex("principalAmount");
        int interetIndex = cursor.getColumnIndex("interest");
        int tenureIndex = cursor.getColumnIndex("tenure");
        int dateIndex = cursor.getColumnIndex("date");
        cursor.moveToFirst();
        while (!cursor.isAfterLast()) {
            withdrawal.add(Double.valueOf(cursor.getDouble(withIndex)));
            name.add(cursor.getString(nameIndex));
            id.add(Integer.valueOf(cursor.getInt(idIndex)));
            principle.add(Double.valueOf(cursor.getDouble(principalIndex)));
            interest.add(Double.valueOf(cursor.getDouble(interetIndex)));
            time.add(Double.valueOf(cursor.getDouble(tenureIndex)));
            dateHistory.add(cursor.getString(dateIndex));
            cursor.moveToNext();
            withIndex = withIndex;
        }
        final Dialog history = new Dialog(this);
        history.setContentView(R.layout.history);
        history.setCancelable(true);
        history.getWindow().setBackgroundDrawable(new ColorDrawable(0));
        history.setCanceledOnTouchOutside(true);
        history.getWindow().setLayout(-2, -1);
        history.getWindow().getAttributes().windowAnimations = 16973826;
        history.show();
        MyListAdapter adapter = new MyListAdapter(this, name, principle, dateHistory, id, time, withdrawal);
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
        if (!this.investment.getText().toString().isEmpty() && !this.rate.getText().toString().isEmpty() && !this.Time.getText().toString().isEmpty()) {
            this.Tenure = Double.parseDouble(this.Time.getText().toString());
            int selectedid = this.group.getCheckedRadioButtonId();
            RadioButton radioButton = (RadioButton) findViewById(selectedid);
            this.radioButton = radioButton;
            if (radioButton.getText().toString().equals("year")) {
                this.Tenure = Double.parseDouble(this.Time.getText().toString()) * 12.0d;
            } else {
                this.Tenure = Double.parseDouble(this.Time.getText().toString());
            }
            if (this.Tenure <= 360.0d && Double.parseDouble(this.rate.getText().toString()) <= 50.0d) {
                calculation();
                final double loanSql = Double.parseDouble(this.investment.getText().toString());
                final double interestSql = Double.parseDouble(this.rate.getText().toString());
                this.withdrawalAmount = Double.parseDouble(this.withdrawal.getText().toString());
                final String sqlDate = this.date + " " + this.month + " " + this.year;
                if (this.radioButton.getText().toString().equals("year")) {
                    periodSql = Double.parseDouble(this.Time.getText().toString());
                } else {
                    periodSql = Double.parseDouble(this.Time.getText().toString()) / 12.0d;
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
                        Swp.this.name = naming.getText().toString();
                        if (Swp.this.name.isEmpty()) {
                            Swp.this.name = "EMPTY";
                        }
                        SQLiteDatabase sQLiteDatabase = Swp.this.myDatabase;
                        sQLiteDatabase.execSQL("INSERT INTO swpTable(name,principalAmount,interest,tenure,date,withdrawal) VALUES ('" + Swp.this.name + "'," + loanSql + "," + interestSql + "," + d + ",'" + sqlDate + "'," + Swp.this.withdrawalAmount + ")");
                        entry.dismiss();
                    }
                });
                return;
            } else if (this.Tenure > 360.0d) {
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
