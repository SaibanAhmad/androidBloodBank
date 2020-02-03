package com.saiban.bahawalpurblooddonors;

import android.app.DatePickerDialog;
import android.app.Dialog;
import android.app.DialogFragment;
import android.app.ProgressDialog;
import android.graphics.Color;
import android.support.annotation.NonNull;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.saiban.bahawalpurblooddonors.Models.Abc;
import com.saiban.bahawalpurblooddonors.Models.date_model;

import java.text.DateFormat;
import java.util.Calendar;
import java.util.Date;
import java.text.ParseException;
import java.util.Locale;

public class donorReminder extends AppCompatActivity {
    TextView donationDateTxt, nextDonationDateTxt, mssgg;
    Button btn;
    //////////////
    ///////////////////
    FirebaseUser User;
    DatabaseReference reference, donorRef;
    String LastDate, DonateOn;
    LinearLayout linearLayout;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_donor_reminder);
        setTitle("Reminder");
        Init();
        final ProgressDialog PD = new ProgressDialog(this);
        PD.setIndeterminate(true);
        PD.setCancelable(false);
        PD.setMessage("Please wait...");
        PD.show();
        User = FirebaseAuth.getInstance().getCurrentUser();
        reference = FirebaseDatabase.getInstance().getReference().child("Blood_donors_Last_donation_date").child(User.getUid());
        donorRef = FirebaseDatabase.getInstance().getReference().child("REGISTERED_DONORS");
        donorRef.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                if (!dataSnapshot.child(User.getUid()).exists()) {
                    linearLayout.setVisibility(View.GONE);
                    ImageView imageView = (ImageView) findViewById(R.id.reminderError);
                    imageView.setVisibility(View.VISIBLE);
                }
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });
        ////////////////////
        reference.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                if (dataSnapshot.getValue() != null) {
                    date_model object = new date_model();
                    object.setLastDonationDate(dataSnapshot.child("lastDonationDate").getValue().toString());
                    object.setNextDonationDate(dataSnapshot.child("nextDonationDate").getValue().toString());

//                    Calendar nd = (Calendar) object.getNextDonationDate();
                    DateFormat DFD = DateFormat.getDateInstance(DateFormat.MEDIUM, Locale.US);
                    try {
                        Date aja = DFD.parse(object.getNextDonationDate());
                        Toast.makeText(donorReminder.this, aja + "", Toast.LENGTH_SHORT).show();
                    } catch (ParseException e) {
                        e.printStackTrace();
                    }

                    donationDateTxt.setText(object.getLastDonationDate());
                    nextDonationDateTxt.setText(object.getNextDonationDate());
                    mssgg.setText("You are now highlighted for last donation date , you can donate blood on following date " + object.getNextDonationDate());
                    donationDateTxt.setTextColor(getResources().getColor(R.color.colorPrimaryOrange));
                    nextDonationDateTxt.setTextColor(getResources().getColor(R.color.color_success));
                    btn.setEnabled(false);
                    PD.cancel();
                } else {
                    PD.cancel();
                    donationDateTxt.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View view) {
                            DialogFragment fragment = new donorReminder.PickerFragment();
                            fragment.show(getFragmentManager(), "Date Picker");
                            donationDateTxt.setTextColor(getResources().getColor(R.color.colorPrimaryOrange));

                        }
                    });


                }
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {
//                Toast.makeText(donorReminder.this, "Something went wrong please try again.", Toast.LENGTH_SHORT).show();
            }
        });


        /////////////////////////////////


        btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                LastDate = donationDateTxt.getText().toString();
                DonateOn = nextDonationDateTxt.getText().toString();
                ////////////
                if (LastDate.trim().equals("DD/MM/YYYY")) {
                    Toast.makeText(donorReminder.this, "Please Select Date First.", Toast.LENGTH_SHORT).show();
                } else {
                    date_model obj = new date_model();
                    obj.setLastDonationDate(LastDate);
                    obj.setNextDonationDate(DonateOn);
                    ////////////
                    reference.setValue(obj).addOnCompleteListener(donorReminder.this, new OnCompleteListener<Void>() {
                        @Override
                        public void onComplete(@NonNull Task<Void> task) {
                            AlertDialog.Builder builder = new AlertDialog.Builder(donorReminder.this);
                            View v = LayoutInflater.from(donorReminder.this).inflate(R.layout.cafreminder, null);
                            builder.setView(v);
                            builder.show();
                        }
                    });
                }
            }
        });


    }

    void Init() {
        mssgg = (TextView) findViewById(R.id.dmsk);
        linearLayout = (LinearLayout) findViewById(R.id.mainContainer);
        donationDateTxt = (TextView) findViewById(R.id.donationDate);
        btn = (Button) findViewById(R.id.getNextDonationDate_Btn);
        nextDonationDateTxt = (TextView) findViewById(R.id.next_donationDate);
    }

//    boolean userReg(){
//        if (donorRef!=null){
//            Toast.makeText(this, "not exist", Toast.LENGTH_SHORT).show();
//        }
//    }


    public static class PickerFragment extends DialogFragment implements DatePickerDialog.OnDateSetListener {


        @Override
        public Dialog onCreateDialog(Bundle savedInstanceState) {
            final Calendar calendar = Calendar.getInstance();
            int YEAR = calendar.get(Calendar.YEAR);
            int MONTH = calendar.get(Calendar.MONTH);
            int DAY = calendar.get(Calendar.DAY_OF_MONTH);

            DatePickerDialog DPD_1 = new DatePickerDialog(getActivity(), android.app.AlertDialog.THEME_DEVICE_DEFAULT_DARK, this, YEAR, MONTH, DAY);

            return DPD_1;
        }

        @Override
        public void onDateSet(DatePicker datePicker, int YEAR, int MONTH, int DAY) {

//            TextView textView
            Calendar calendar = Calendar.getInstance();
            calendar.setTimeInMillis(0);
            calendar.set(YEAR, MONTH, DAY, 0, 0, 0);
            Date date = calendar.getTime();
            DateFormat dateFormat = DateFormat.getDateInstance(DateFormat.MEDIUM, Locale.US);
            String forMatedDate = dateFormat.format(date);
            TextView donationDateTxt = (TextView) getActivity().findViewById(R.id.donationDate);
            TextView nextDonationDateTxt = (TextView) getActivity().findViewById(R.id.next_donationDate);
            donationDateTxt.setText(forMatedDate);
            calendar.add(Calendar.MONTH, 3);
            Date date1 = calendar.getTime();
            DateFormat dateFormat1 = DateFormat.getDateInstance(DateFormat.MEDIUM, Locale.US);
            String formatedNextDate = dateFormat1.format(date1);
            nextDonationDateTxt.setText(formatedNextDate);
            nextDonationDateTxt.setTextColor(Color.parseColor("#6AB344"));
        }
    }
}
