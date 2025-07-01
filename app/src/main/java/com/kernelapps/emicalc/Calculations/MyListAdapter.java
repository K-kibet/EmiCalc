package com.kernelapps.emicalc.Calculations;

import android.app.Activity;
import android.content.Intent;
import android.database.sqlite.SQLiteDatabase;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.TextView;
import android.widget.Toast;

import com.kernelapps.emicalc.R;

import java.util.ArrayList;
import java.util.List;


class MyListAdapter extends ArrayAdapter<String> {
    private final Activity context;
    private String contextname;
    private List<String> historyDate;
    private List<Double> historyPrincipal;
    private List<Integer> id;
    private List<String> maintitle;
    SQLiteDatabase myDatabase;
    private List<Double> time;
    private List<Double> withdrawal;

    public MyListAdapter(Activity context, List<String> maintitle, List<Double> historyPrincipal, List<String> historyDate, List<Integer> id, List<Double> time, List<Double> withdrawal) {
        super(context, (int) R.layout.history_element, maintitle);
        this.maintitle = new ArrayList();
        this.historyPrincipal = new ArrayList();
        this.historyDate = new ArrayList();
        this.id = new ArrayList();
        this.time = new ArrayList();
        this.withdrawal = new ArrayList();
        this.context = context;
        String localClassName = context.getLocalClassName();
        this.contextname = localClassName;
        Toast.makeText(context, localClassName, Toast.LENGTH_LONG).show();
        this.maintitle = maintitle;
        this.historyPrincipal = historyPrincipal;
        this.historyDate = historyDate;
        this.id = id;
        this.time = time;
        if (this.contextname.equals("Calculations.Swp")) {
            this.withdrawal = withdrawal;
        }
    }

    @Override
    public View getView(final int position, View view, ViewGroup parent) {
        LayoutInflater inflater = this.context.getLayoutInflater();
        View rowView = inflater.inflate(R.layout.history_element, (ViewGroup) null, true);
        TextView titleText = (TextView) rowView.findViewById(R.id.nameid);
        TextView principal = (TextView) rowView.findViewById(R.id.historyPrincipal);
        TextView date = (TextView) rowView.findViewById(R.id.historyDate);
        this.myDatabase = getContext().openOrCreateDatabase("EMI", 0, null);
        if (this.contextname.equals("Calculations.emi")) {
            this.myDatabase.execSQL("CREATE TABLE IF NOT EXISTS emiTable(name TEXT,principalAmount DOUBLE,interest DOUBLE,tenure DOUBLE,date TEXT,id INTEGER PRIMARY KEY)");
        } else if (this.contextname.equals("Calculations.Fd")) {
            this.myDatabase.execSQL("CREATE TABLE IF NOT EXISTS fdTable(name TEXT,principalAmount DOUBLE,interest DOUBLE,tenure DOUBLE,date TEXT,id INTEGER PRIMARY KEY)");
        } else if (this.contextname.equals("Calculations.Rd")) {
            this.myDatabase.execSQL("CREATE TABLE IF NOT EXISTS rdTable(name TEXT,principalAmount DOUBLE,interest DOUBLE,tenure DOUBLE,date TEXT,id INTEGER PRIMARY KEY)");
        } else if (this.contextname.equals("Calculations.ppf")) {
            this.myDatabase.execSQL("CREATE TABLE IF NOT EXISTS ppfTable(name TEXT,principalAmount DOUBLE,interest DOUBLE,tenure DOUBLE,date TEXT,id INTEGER PRIMARY KEY)");
        } else if (this.contextname.equals("Calculations.lumpsum")) {
            this.myDatabase.execSQL("CREATE TABLE IF NOT EXISTS lumpsumTable(name TEXT,principalAmount DOUBLE,interest DOUBLE,tenure DOUBLE,date TEXT,id INTEGER PRIMARY KEY)");
        } else if (this.contextname.equals("Calculations.saving")) {
            this.myDatabase.execSQL("CREATE TABLE IF NOT EXISTS savingTable(name TEXT,principalAmount DOUBLE,interest DOUBLE,tenure DOUBLE,date TEXT,id INTEGER PRIMARY KEY)");
        } else if (this.contextname.equals("Calculations.Sip")) {
            this.myDatabase.execSQL("CREATE TABLE IF NOT EXISTS sipTable(name TEXT,principalAmount DOUBLE,interest DOUBLE,tenure DOUBLE,date TEXT,id INTEGER PRIMARY KEY)");
        } else {
            this.myDatabase.execSQL("CREATE TABLE IF NOT EXISTS swpTable(name TEXT,principalAmount DOUBLE,interest DOUBLE,tenure DOUBLE,date TEXT,id INTEGER PRIMARY KEY)");
        }
        ImageButton delete = (ImageButton) rowView.findViewById(R.id.delete);
        delete.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (!MyListAdapter.this.contextname.equals("Calculations.emi")) {
                    if (!MyListAdapter.this.contextname.equals("Calculations.Fd")) {
                        if (!MyListAdapter.this.contextname.equals("Calculations.Rd")) {
                            if (!MyListAdapter.this.contextname.equals("Calculations.ppf")) {
                                if (!MyListAdapter.this.contextname.equals("Calculations.lumpsum")) {
                                    if (!MyListAdapter.this.contextname.equals("Calculations.saving")) {
                                        if (MyListAdapter.this.contextname.equals("Calculations.Sip")) {
                                            SQLiteDatabase sQLiteDatabase = MyListAdapter.this.myDatabase;
                                            sQLiteDatabase.execSQL("DELETE FROM sipTable WHERE id=" + MyListAdapter.this.id.get(position));
                                        } else {
                                            SQLiteDatabase sQLiteDatabase2 = MyListAdapter.this.myDatabase;
                                            sQLiteDatabase2.execSQL("DELETE FROM swpTable WHERE id=" + MyListAdapter.this.id.get(position));
                                            MyListAdapter.this.withdrawal.remove(position);
                                        }
                                    } else {
                                        SQLiteDatabase sQLiteDatabase3 = MyListAdapter.this.myDatabase;
                                        sQLiteDatabase3.execSQL("DELETE FROM savingTable WHERE id=" + MyListAdapter.this.id.get(position));
                                    }
                                } else {
                                    SQLiteDatabase sQLiteDatabase4 = MyListAdapter.this.myDatabase;
                                    sQLiteDatabase4.execSQL("DELETE FROM lumpsumTable WHERE id=" + MyListAdapter.this.id.get(position));
                                }
                            } else {
                                SQLiteDatabase sQLiteDatabase5 = MyListAdapter.this.myDatabase;
                                sQLiteDatabase5.execSQL("DELETE FROM ppfTable WHERE id=" + MyListAdapter.this.id.get(position));
                            }
                        } else {
                            SQLiteDatabase sQLiteDatabase6 = MyListAdapter.this.myDatabase;
                            sQLiteDatabase6.execSQL("DELETE FROM rdTable WHERE id=" + MyListAdapter.this.id.get(position));
                        }
                    } else {
                        SQLiteDatabase sQLiteDatabase7 = MyListAdapter.this.myDatabase;
                        sQLiteDatabase7.execSQL("DELETE FROM fdTable WHERE id=" + MyListAdapter.this.id.get(position));
                    }
                } else {
                    SQLiteDatabase sQLiteDatabase8 = MyListAdapter.this.myDatabase;
                    sQLiteDatabase8.execSQL("DELETE FROM emiTable WHERE id=" + MyListAdapter.this.id.get(position));
                }
                MyListAdapter.this.id.remove(position);
                MyListAdapter.this.time.remove(position);
                MyListAdapter.this.historyPrincipal.remove(position);
                MyListAdapter.this.maintitle.remove(position);
                MyListAdapter.this.historyDate.remove(position);
                MyListAdapter.this.notifyDataSetChanged();
            }
        });
        Button apply = (Button) rowView.findViewById(R.id.apply);
        apply.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent i;
                if (!MyListAdapter.this.contextname.equals("Calculations.emi")) {
                    if (!MyListAdapter.this.contextname.equals("Calculations.Fd")) {
                        if (!MyListAdapter.this.contextname.equals("Calculations.Rd")) {
                            if (!MyListAdapter.this.contextname.equals("Calculations.ppf")) {
                                if (!MyListAdapter.this.contextname.equals("Calculations.saving")) {
                                    if (!MyListAdapter.this.contextname.equals("Calculations.Sip")) {
                                        if (MyListAdapter.this.contextname.equals("Calculations.lumpsum")) {
                                            i = new Intent(MyListAdapter.this.getContext(), lumpsum.class);
                                        } else {
                                            i = new Intent(MyListAdapter.this.getContext(), Swp.class);
                                        }
                                    } else {
                                        i = new Intent(MyListAdapter.this.getContext(), Sip.class);
                                    }
                                } else {
                                    i = new Intent(MyListAdapter.this.getContext(), saving.class);
                                }
                            } else {
                                i = new Intent(MyListAdapter.this.getContext(), ppf.class);
                            }
                        } else {
                            i = new Intent(MyListAdapter.this.getContext(), Rd.class);
                        }
                    } else {
                        i = new Intent(MyListAdapter.this.getContext(), Fd.class);
                    }
                } else {
                    i = new Intent(MyListAdapter.this.getContext(), emi.class);
                }
                i.putExtra("Open", String.valueOf(position));
                MyListAdapter.this.getContext().startActivity(i);
            }
        });
        titleText.setText(this.maintitle.get(position));
        principal.setText("Principal : ₹" + this.historyPrincipal.get(position));
        date.setText("First EMI: " + this.historyDate.get(position));
        return rowView;
    }
}
