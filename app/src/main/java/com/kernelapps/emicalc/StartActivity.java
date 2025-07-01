package com.kernelapps.emicalc;

import android.content.ActivityNotFoundException;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.kernelapps.emicalc.FIles.MainActivity;
import com.kernelapps.emicalc.ads.AdsManager;
import com.kernelapps.emicalc.databinding.ActivityStartBinding;
import com.scrounger.countrycurrencypicker.library.BuildConfig;

public class StartActivity extends AppCompatActivity {
    ActivityStartBinding binding;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding= ActivityStartBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());
        AdsManager.INSTANCE.loadNative(this, binding.nativeId);
        binding.btnPrivacy.setOnClickListener(view -> {
            startActivity(new Intent(Intent.ACTION_VIEW, Uri.parse("http://www.stackoverflow.com")));
        });
        binding.btnRate.setOnClickListener(view -> {
            Uri uri = Uri.parse("market://details?id=" + getPackageName());
            Intent myAppLinkToMarket = new Intent(Intent.ACTION_VIEW, uri);
            try {
                startActivity(myAppLinkToMarket);
            } catch (ActivityNotFoundException e) {
                Toast.makeText(this, " unable to find market app", Toast.LENGTH_LONG).show();
            }
        });
        binding.btnShare.setOnClickListener(view -> {
            try {
                Intent shareIntent = new Intent(Intent.ACTION_SEND);
                shareIntent.setType("text/plain");
                shareIntent.putExtra(Intent.EXTRA_SUBJECT, getString(R.string.app_name));
                String shareMessage= "\nLet me recommend you this application\n\n";
                shareMessage = shareMessage + "https://play.google.com/store/apps/details?id=" + BuildConfig.APPLICATION_ID +"\n\n";
                shareIntent.putExtra(Intent.EXTRA_TEXT, shareMessage);
                startActivity(Intent.createChooser(shareIntent, "choose one"));
            } catch(Exception e) {
                //e.toString();
            }
        });
        binding.btnStart.setOnClickListener(view -> {

            AdsManager.INSTANCE.showInterstitial(this, () -> {
                startActivity(new Intent(this, MainActivity.class));
                finish();
            });
        });

    }
}