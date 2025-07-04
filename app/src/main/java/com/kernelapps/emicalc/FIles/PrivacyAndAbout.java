package com.kernelapps.emicalc.FIles;

import android.app.Dialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.app.AppCompatDelegate;

import com.kernelapps.emicalc.Constants;
import com.kernelapps.emicalc.R;


public class PrivacyAndAbout extends AppCompatActivity {


    boolean priv;
    SharedPreferences themePreference;

    public void mail(View view) {
        String mail = getString(R.string.mail);
        Intent intent = new Intent("android.intent.action.SEND");
        intent.putExtra("android.intent.extra.EMAIL", mail);
        intent.setPackage("com.google.android.gm");
        intent.setType("text/html");
        startActivity(intent);
    }


    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        getWindow().setFlags(512, 512);
        getWindow().setStatusBarColor(0);
        SharedPreferences sharedPreferences = getApplicationContext().getSharedPreferences(getPackageName(), 0);
        this.themePreference = sharedPreferences;
        Constants.LIGHT_THEME = sharedPreferences.getBoolean("THEME", true);
        if (!Constants.LIGHT_THEME) {
            AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_YES);
        }
        setContentView(R.layout.activity_privacy_and_about);

        TextView title = (TextView) findViewById(R.id.text);
        TextView maintext = (TextView) findViewById(R.id.maintext);
        Intent intent = getIntent();
        boolean booleanExtra = intent.getBooleanExtra("name", true);
        this.priv = booleanExtra;
        if (booleanExtra) {
            title.setText("Privacy");
            maintext.setText("Privacy Policy\n\n\n\nAneesh built the EMI app as an Ad Supported app. This SERVICE is provided by Aneesh at no cost and is intended for use as is.\n\nThis page is used to inform visitors regarding my policies with the collection, use, and disclosure of Personal Information if anyone decided to use my Service.\n\nIf you choose to use my Service, then you agree to the collection and use of information in relation to this policy. The Personal Information that I collect is used for providing and improving the Service. I will not use or share your information with anyone except as described in this Privacy Policy.\n\nThe terms used in this Privacy Policy have the same meanings as in our Terms and Conditions, which is accessible at EMI unless otherwise defined in this Privacy Policy.\n\nInformation Collection and Use\n\nFor a better experience, while using our Service, I may require you to provide us with certain personally identifiable information. The information that I request will be retained on your device and is not collected by me in any way.\n\nThe app does use third party services that may collect information used to identify you.\n\nLink to privacy policy of third party service providers used by the app\n\n•\tGoogle Play Services\n\n•\tAdMob\n\nLog Data\n\nI want to inform you that whenever you use my Service, in a case of an error in the app I collect data and information (through third party products) on your phone called Log Data. This Log Data may include information such as your device Internet Protocol (“IP”) address, device name, operating system version, the configuration of the app when utilizing my Service, the time and date of your use of the Service, and other statistics.\n\nCookies\n\nCookies are files with a small amount of data that are commonly used as anonymous unique identifiers. These are sent to your browser from the websites that you visit and are stored on your device's internal memory.\n\nThis Service does not use these “cookies” explicitly. However, the app may use third party code and libraries that use “cookies” to collect information and improve their services. You have the option to either accept or refuse these cookies and know when a cookie is being sent to your device. If you choose to refuse our cookies, you may not be able to use some portions of this Service.\n\nService Providers\n\nI may employ third-party companies and individuals due to the following reasons:\n\n•\tTo facilitate our Service;\n\n•\tTo provide the Service on our behalf;\n\n•\tTo perform Service-related services; or\n\n•\tTo assist us in analyzing how our Service is used.\n\nI want to inform users of this Service that these third parties have access to your Personal Information. The reason is to perform the tasks assigned to them on our behalf. However, they are obligated not to disclose or use the information for any other purpose.\n\nSecurity\n\nI value your trust in providing us your Personal Information, thus we are striving to use commercially acceptable means of protecting it. But remember that no method of transmission over the internet, or method of electronic storage is 100% secure and reliable, and I cannot guarantee its absolute security.\n\nLinks to Other Sites\n\nThis Service may contain links to other sites. If you click on a third-party link, you will be directed to that site. Note that these external sites are not operated by me. Therefore, I strongly advise you to review the Privacy Policy of these websites. I have no control over and assume no responsibility for the content, privacy policies, or practices of any third-party sites or services.\n\nChildren’s Privacy\n\nThese Services do not address anyone under the age of 13. I do not knowingly collect personally identifiable information from children under 13. In the case I discover that a child under 13 has provided me with personal information, I immediately delete this from our servers. If you are a parent or guardian and you are aware that your child has provided us with personal information, please contact me so that I will be able to do necessary actions.\n\nChanges to This Privacy Policy\n\nI may update our Privacy Policy from time to time. Thus, you are advised to review this page periodically for any changes. I will notify you of any changes by posting the new Privacy Policy on this page.\n\nThis policy is effective as of 2020-11-15\n\nContact Us\n\nIf you have any questions or suggestions about my Privacy Policy, do not hesitate to contact me at \n");
            return;
        }
        title.setText("About Us");
        maintext.setText("About us\n\n\nThe team ARPA focuses on providing some of the useful and productivity application. \nThe Emi Calculator app does not only provides calculation for EMI related problems but also can be used to calculate financial related calculations such as FD, RD, PPF, SIP, SWP and many more to discover inside the app. The app will also provide you the feature to convert the calculation in pdf for the purpose of documentation. It also gives you the accessibility to share the results.\nFor any support, queries and improvements related to the app please contact to\n\n");
    }

    @Override
    public void onBackPressed() {
        final Dialog dialog1 = new Dialog(this);
        dialog1.setContentView(R.layout.exit_confi);
        dialog1.setCancelable(true);
        dialog1.getWindow().setBackgroundDrawable(new ColorDrawable(0));
        dialog1.setCanceledOnTouchOutside(false);
        dialog1.getWindow().setLayout(-2, -2);
        dialog1.getWindow().getAttributes().windowAnimations = 16973826;
        dialog1.show();
        Button button = (Button) dialog1.findViewById(R.id.yes);
        button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dialog1.dismiss();
                PrivacyAndAbout.super.onBackPressed();
            }
        });
        Button button2 = (Button) dialog1.findViewById(R.id.no);
        button2.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dialog1.dismiss();
                return;
            }
        });
        dialog1.setOnDismissListener(new DialogInterface.OnDismissListener() {
            @Override
            public void onDismiss(DialogInterface dialog) {
                PrivacyAndAbout.super.onBackPressed();
            }
        });
    }
}
