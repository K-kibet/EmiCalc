package com.kernelapps.emicalc.FIles;

import android.app.Dialog;
import android.content.ActivityNotFoundException;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.net.Uri;
import android.os.Bundle;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.CompoundButton;
import android.widget.FrameLayout;
import android.widget.LinearLayout;
import android.widget.Toast;

import androidx.appcompat.app.ActionBarDrawerToggle;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.app.AppCompatDelegate;
import androidx.appcompat.widget.SwitchCompat;
import androidx.core.view.GravityCompat;
import androidx.drawerlayout.widget.DrawerLayout;

import com.kernelapps.emicalc.Calculations.Currency;
import com.kernelapps.emicalc.Calculations.Fd;
import com.kernelapps.emicalc.Calculations.Rd;
import com.kernelapps.emicalc.Calculations.SimpleAndCompound;
import com.kernelapps.emicalc.Calculations.Sip;
import com.kernelapps.emicalc.Calculations.Swp;
import com.kernelapps.emicalc.Calculations.Tax;
import com.kernelapps.emicalc.Calculations.compareloan;
import com.kernelapps.emicalc.Calculations.emi;
import com.kernelapps.emicalc.Calculations.loaneligibility;
import com.kernelapps.emicalc.Calculations.lumpsum;
import com.kernelapps.emicalc.Calculations.ppf;
import com.kernelapps.emicalc.Calculations.saving;
import com.kernelapps.emicalc.Constants;
import com.kernelapps.emicalc.R;
import com.kernelapps.emicalc.ads.AdsManager;
import com.google.android.material.navigation.NavigationView;
import com.scrounger.countrycurrencypicker.library.Country;
import com.scrounger.countrycurrencypicker.library.CountryCurrencyPicker;
import com.scrounger.countrycurrencypicker.library.Listener.CountryCurrencyPickerListener;
import com.scrounger.countrycurrencypicker.library.PickerType;
public class MainActivity extends AppCompatActivity implements NavigationView.OnNavigationItemSelectedListener {
    Dialog dialog;
    Dialog dialog1;
    SharedPreferences frontshare;
    SharedPreferences sharedPreferences;
    SharedPreferences themePreference;

    public void finder(final View view) {
        String find;
        if (view.getTag().toString().equals("0")) {
            find = "bank";
        } else {
            find = "atm";
        }
        try {
            Uri uri3 = Uri.parse("https://www.google.co.in/maps/search/" + find);
            Intent intent3 = new Intent("android.intent.action.VIEW", uri3);
            intent3.setPackage("com.google.android.apps.maps");
            intent3.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
            startActivity(intent3);
        } catch (ActivityNotFoundException e2) {
            Uri uri4 = Uri.parse("https://play.google.com/store/apps/details?id=com.google.android.apps.maps");
            Intent intent4 = new Intent("android.intent.action.VIEW", uri4);
            intent4.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
            startActivity(intent4);
        }
    }

    public void emi(View view) {

        AdsManager.INSTANCE.showInterstitial(this, new AdsManager.AdsCallBack() {
            @Override
            public void onClosed() {
                Intent intent2 = new Intent(MainActivity.this.getApplicationContext(), emi.class);
                MainActivity.this.startActivity(intent2);
                MainActivity.this.overridePendingTransition(R.anim.fadein, R.anim.fadeeout);
            }
        });
    }

    public void tax(View view) {
        AdsManager.INSTANCE.showInterstitial(this, () -> startActivity(Tax.class));


    }

    public void startActivity(Class<?> cls) {
        Intent intent = new Intent(MainActivity.this.getApplicationContext(), cls);
        MainActivity.this.startActivity(intent);
        MainActivity.this.overridePendingTransition(R.anim.fadein, R.anim.fadeeout);
    }

    public void loanelgibility(View view) {
        AdsManager.INSTANCE.showInterstitial(this, () -> startActivity(loaneligibility.class));

    }

    public void compareloan(View view) {
       AdsManager.INSTANCE.showInterstitial(this, () -> startActivity(compareloan.class));
    }

    public void currency(View view) {
        AdsManager.INSTANCE.showInterstitial(this, () -> startActivity(Currency.class));

    }

    public void fd(View view) {
        AdsManager.INSTANCE.showInterstitial(this, () -> startActivity(Fd.class));

    }

    public void sip(View view) {
        AdsManager.INSTANCE.showInterstitial(this, () -> startActivity(Sip.class));
    }

    public void ppf(View view) {
        AdsManager.INSTANCE.showInterstitial(this, () -> startActivity(ppf.class));
    }

    public void rd(View view) {
        AdsManager.INSTANCE.showInterstitial(this, () -> startActivity(Rd.class));
      }

    public void swp(View view) {
        AdsManager.INSTANCE.showInterstitial(this, () -> startActivity(Swp.class));
    }

    public void saving(View view) {
        AdsManager.INSTANCE.showInterstitial(this, () -> startActivity(saving.class));
    }

    public void lumpsum(View view) {
        AdsManager.INSTANCE.showInterstitial(this, () -> startActivity(lumpsum.class));
    }

    public void simpleandcomp(View view) {
        AdsManager.INSTANCE.showInterstitial(this, () -> startActivity(SimpleAndCompound.class));
    }


    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        AdsManager.INSTANCE.loadNative(this, findViewById(R.id.nativeId));
        AdsManager.INSTANCE.loadBanner(this, findViewById(R.id.bannerId));

        this.frontshare = getApplicationContext().getSharedPreferences(getPackageName(), 0);

        SharedPreferences sharedPreferences = getApplicationContext().getSharedPreferences(getPackageName(), 0);
        this.themePreference = sharedPreferences;
        Constants.LIGHT_THEME = sharedPreferences.getBoolean("THEME", true);
        if (!Constants.LIGHT_THEME) {
            AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_YES);
        }
//        CollapsingToolbarLayout collapsingToolbarLayout = (CollapsingToolbarLayout) findViewById(R.id.collapsing);
//        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
//        setSupportActionBar(toolbar);
        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(this, drawer,
                 R.string.navigation_drawer_open, R.string.navigation_drawer_close);
        drawer.addDrawerListener(toggle);
        toggle.syncState();

        findViewById(R.id.btnMenu).setOnClickListener(view -> {
          drawer.openDrawer(GravityCompat.START);
        });
        NavigationView navigationView = (NavigationView) findViewById(R.id.nav_view);
        navigationView.setNavigationItemSelectedListener(this);

        this.dialog1 = new Dialog(this);
        this.dialog = new Dialog(this);

        int times = this.frontshare.getInt("times", 0);
        if (times == 0) {
            this.frontshare.edit().putInt("times", 1).apply();
            final Dialog dialog2 = new Dialog(this);
            dialog2.setContentView(R.layout.frontpage);
            dialog2.setCancelable(true);
            dialog2.getWindow().setGravity(17);
            dialog2.getWindow().setBackgroundDrawable(new ColorDrawable(0));
            dialog2.setCanceledOnTouchOutside(true);
            dialog2.getWindow().setLayout(-2, -2);
            dialog2.getWindow().getAttributes().windowAnimations = 16973826;
            dialog2.show();
            Button button3 = (Button) dialog2.findViewById(R.id.okay);
            button3.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    dialog2.dismiss();
                }
            });
        }
    }



    public void yes(View view) {
        super.onBackPressed();
    }

    public void no(View view) {
        FrameLayout frameLayout = (FrameLayout) findViewById(R.id.blur);
        frameLayout.setBackgroundColor(Color.parseColor("#00FFFFFF"));
        LinearLayout exit = (LinearLayout) findViewById(R.id.exit);
        exit.setVisibility(View.GONE);
    }


    @Override
    public boolean onNavigationItemSelected(MenuItem menuItem) {
        int id = menuItem.getItemId();
        if (id == R.id.rate) {
            Intent intent = new Intent("android.intent.action.VIEW");
            intent.setData(Uri.parse("https://play.google.com/store/apps/details?id=" + getPackageName()));
            startActivity(intent);
            finish();
        } else if (id == R.id.theme) {
            newTheme();
        } else if (id == R.id.currency) {
            newCurrency();
        } else if (id == R.id.share) {
            Intent intent2 = new Intent("android.intent.action.SEND");
            intent2.setType("text/plain");
            intent2.putExtra("android.intent.extra.TEXT", "https://play.google.com/store/apps/details?id=" + getPackageName());
            intent2.putExtra("android.intent.extra.SUBJECT", "EMI A Financial Calculator");
            startActivity(Intent.createChooser(intent2, "Share Using"));
        } else if (id == R.id.about) {
            String url = "http://google.com";
            Intent i = new Intent(Intent.ACTION_VIEW);
            i.setData(Uri.parse(url));
            startActivity(i);
        } else if (id == R.id.privacy) {
          AdsManager.INSTANCE.showInterstitial(this, () -> {
              Intent intent4 = new Intent(getApplicationContext(), PrivacyAndAbout.class);
              intent4.putExtra("name", true);
              startActivity(intent4);
          });
        }
        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        drawer.closeDrawer(GravityCompat.START);
        return true;
    }

    private void newCurrency() {
        try {
            this.sharedPreferences = getApplicationContext().getSharedPreferences(getPackageName(), 0);
            CountryCurrencyPicker pickerDialog = CountryCurrencyPicker.newInstance(PickerType.CURRENCY, new CountryCurrencyPickerListener() {
                @Override
                public void onSelectCountry(Country country) {
                    MainActivity.this.sharedPreferences.edit().putString(Constants.CURRENCY, country.getCurrency().getSymbol()).apply();
                    Toast.makeText(MainActivity.this, country.getCurrency().getSymbol(), Toast.LENGTH_SHORT).show();
                    Constants.CURRENCY_STORED = country.getCurrency().getSymbol();
                }

                @Override
                public void onSelectCurrency(com.scrounger.countrycurrencypicker.library.Currency currency) {
                    MainActivity.this.sharedPreferences.edit().putString(Constants.CURRENCY, currency.getSymbol()).apply();
                    Toast.makeText(MainActivity.this, currency.getSymbol(), Toast.LENGTH_SHORT).show();
                    Constants.CURRENCY_STORED = currency.getSymbol();
                }
            });
            //pickerDialog.show(getSupportFragmentManager(), CountryCurrencyPicker.DIALOG_NAME);
        } catch (Exception e) {
            Toast.makeText(this, e.getMessage(), Toast.LENGTH_LONG).show();
        }
    }

    private void newTheme() {
        final Dialog theme = new Dialog(this);
        theme.setContentView(R.layout.theme_dialog);
        theme.setCancelable(true);
        theme.getWindow().setBackgroundDrawable(new ColorDrawable(0));
        theme.getWindow().setLayout(-2, -2);
        theme.getWindow().setGravity(17);
        theme.show();
        SwitchCompat dark = (SwitchCompat) theme.findViewById(R.id.dark);
        Constants.LIGHT_THEME = this.themePreference.getBoolean("THEME", true);
        if (!Constants.LIGHT_THEME) {
            dark.setChecked(true);
        }
        dark.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton compoundButton, boolean b) {
                if (b) {
                    AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_YES);
                    MainActivity.this.themePreference.edit().putBoolean("THEME", false).apply();
                    Constants.LIGHT_THEME = false;
                    theme.dismiss();
                    return;
                }
                AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_NO);
                MainActivity.this.themePreference.edit().putBoolean("THEME", true).apply();
                Constants.LIGHT_THEME = true;
                theme.dismiss();
            }
        });
    }

    @Override
    public void onPointerCaptureChanged(boolean hasCapture) {
    }
}
