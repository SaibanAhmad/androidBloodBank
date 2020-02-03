package com.saiban.bahawalpurblooddonors.menuActivity;

import android.content.Intent;
import android.net.Uri;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageView;
import android.widget.Toast;

//import com.google.android.gms.ads.AdRequest;
//import com.google.android.gms.ads.AdView;
import com.saiban.bahawalpurblooddonors.R;

public class contact extends AppCompatActivity implements View.OnClickListener {

    ImageView  saiban_ph, saiban_msg, saiban_email;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_contact);
        setTitle("Contact");

        ViewsInit();



    }

    void ViewsInit() {
        saiban_ph = (ImageView) findViewById(R.id.SAIBAN_PHONE);
        saiban_ph.setOnClickListener(this);
        saiban_msg = (ImageView) findViewById(R.id.SAIBAN_MSG);
        saiban_msg.setOnClickListener(this);
        saiban_email = (ImageView) findViewById(R.id.SAIBAN_EMAIL);
        saiban_email.setOnClickListener(this);
    }

    @Override
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.SAIBAN_EMAIL:
                Intent mail_ = new Intent(Intent.ACTION_SEND);
                Intent choose_ = new Intent();
                mail_.setData(Uri.parse("mailto:"));
                String[] email_ = {"saibanahmadcs@gmail.com"};
                mail_.setType("text/plain");
                mail_.putExtra(Intent.EXTRA_EMAIL, email_);
                mail_.setType("message/rfc888");
                choose_ = Intent.createChooser(mail_, "Send Email Via");
                startActivity(choose_);
                break;
            case R.id.SAIBAN_MSG:
                Intent sms_ = new Intent(Intent.ACTION_VIEW, Uri.fromParts("sms", "03351555502", null));
                startActivity(sms_);
                break;
            case R.id.SAIBAN_PHONE:
                Intent call = new Intent(Intent.ACTION_DIAL);
                call.setData(Uri.parse("tel:03351555502"));
                startActivity(call);
                break;
        }
    }
}
