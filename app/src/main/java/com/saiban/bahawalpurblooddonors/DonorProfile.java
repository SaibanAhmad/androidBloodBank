package com.saiban.bahawalpurblooddonors;

import android.app.DatePickerDialog;
import android.app.Dialog;
import android.app.DialogFragment;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.support.annotation.NonNull;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.Spinner;
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
import com.saiban.bahawalpurblooddonors.Models.Reg_Donor;

import java.text.DateFormat;
import java.util.Date;
import java.util.Calendar;
import java.util.Locale;

public class DonorProfile extends AppCompatActivity {

    EditText Feild_Email, Feild_Name, Feild_Number, Feild_City;
    Button Donor_Btn;
    public TextView Feild_Age, dobb;
    Spinner Feild_BloodGrps;
    String Email, Name, Number, City, Age, BloodGroup, Address;
    /////////////////////////
    ArrayAdapter BloodGrpAdapter;
    ////////////////////////////
    EditText Feild_Address;
    FirebaseAuth mAuth;
    FirebaseAuth.AuthStateListener mListiner;
    DatabaseReference Registered_Donors, FromRegistered, BloodGroupRef;
    FirebaseUser User;
    int age_value;
    /////////////////////
    SharedPreferences preferences;

    ///////////
    ProgressDialog PD;

    @Override
    protected void onStart() {
        super.onStart();
        mAuth.addAuthStateListener(mListiner);
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_donor_profile);
        setTitle("Register Donor");
        preferences = getSharedPreferences("isDonorRegistered", Context.MODE_PRIVATE);
        final SharedPreferences.Editor editor = preferences.edit();
        Init();
        //////////////////////////////////////////////////views initialized
        PD = new ProgressDialog(this);
        PD.setMessage("please wait....");
        PD.setCancelable(false);
        PD.setIndeterminate(true);
        PD.show();
        ///////////////////////////////////////////////
        mAuth = FirebaseAuth.getInstance();
        User = FirebaseAuth.getInstance().getCurrentUser();
        mListiner = new FirebaseAuth.AuthStateListener() {
            @Override
            public void onAuthStateChanged(@NonNull FirebaseAuth firebaseAuth) {
                User = firebaseAuth.getCurrentUser();

            }
        };
        /////////////////////////////////////////////////get user instance signed in
        FromRegistered = FirebaseDatabase.getInstance().getReference().child("REGISTERED_USERS").child(User.getUid());
        FromRegistered.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                Email = dataSnapshot.child("EMAIL").getValue().toString();
                Name = dataSnapshot.child("NAME").getValue().toString();
                City = dataSnapshot.child("CITY").getValue().toString();
                Number = dataSnapshot.child("CONTACT").getValue().toString();
                Feild_Email.setText(Email);
                Feild_Name.setText(Name);
                Feild_City.setText(City);
                Feild_Number.setText(Number);
                PD.cancel();
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });
        ////////////////////////////////

        BloodGrpAdapter = ArrayAdapter.createFromResource(this, R.array.bloodGrps, android.R.layout.simple_spinner_item);
        BloodGrpAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        Feild_BloodGrps.setAdapter(BloodGrpAdapter);
        Feild_BloodGrps.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l) {
                BloodGroup = adapterView.getItemAtPosition(i).toString();
            }

            @Override
            public void onNothingSelected(AdapterView<?> adapterView) {

            }
        });
        //////////////////////

        Feild_Age.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                DialogFragment fragment = new DatePickerFragment();
                fragment.show(getFragmentManager(), "Date Picker");
            }
        });


        /////////////////////


        Donor_Btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Age = Feild_Age.getText().toString();
                if (!Age.equals("SELECT YOUR AGE")) {
                    age_value = Integer.parseInt(dobb.getText().toString());
                }
                Address = Feild_Address.getText().toString();
                if (Validation()) {
                    PD.show();
                    BloodGroupRef = FirebaseDatabase.getInstance().getReference().child("Blood_Donors_OrderByBloodGroup").child(BloodGroup);
                    Registered_Donors = FirebaseDatabase.getInstance().getReference().child("REGISTERED_DONORS").child(User.getUid());
                    final Reg_Donor Obj = new Reg_Donor();
                    Obj.setDonorAge(Age);
                    Obj.setDonorBloodGroup(BloodGroup);
                    Obj.setDonorCity(City);
                    Obj.setDonorContact(Number);
                    Obj.setDonorEmail(Email);
                    Obj.setDonorName(Name);
                    Registered_Donors.setValue(Obj).addOnCompleteListener(DonorProfile.this, new OnCompleteListener<Void>() {
                        @Override
                        public void onComplete(@NonNull Task<Void> task) {
                            if (task.isSuccessful()) {
                                Abc a = new Abc();
                                a.setName(Name);
                                a.setBloodGroup(BloodGroup);
                                a.setCity(City);
                                a.setAddress(Address);
                                a.setContact(Number);
                                a.setUserId(User.getUid());
                                BloodGroupRef.push().setValue(a);
                                editor.putInt("User_Donor_Registered", 1);
                                editor.putString("User_Blood_Group", BloodGroup);
                                editor.apply();
                                PD.cancel();
                                AlertDialog.Builder ab = new AlertDialog.Builder(DonorProfile.this);
                                View v = LayoutInflater.from(DonorProfile.this).inflate(R.layout.custom_alert_registrerdonor, null);
                                TextView b = (TextView) v.findViewById(R.id.blood_group_reg_donor);
                                Button c = (Button) v.findViewById(R.id.btn_startdonate);
                                Button d = (Button) v.findViewById(R.id.btn_ok);
                                b.setText(BloodGroup);
                                c.setOnClickListener(new View.OnClickListener() {
                                    @Override
                                    public void onClick(View view) {
                                        startActivity(new Intent(DonorProfile.this, viewRequest.class));
                                        finish();
                                    }
                                });
                                d.setOnClickListener(new View.OnClickListener() {
                                    @Override
                                    public void onClick(View view) {
                                        finish();
                                    }
                                });
                                ab.setView(v);
                                ab.show();
                            }
                        }
                    });

                }

            }
        });
    }

    void Init() {
        Feild_Email = (EditText) findViewById(R.id.email_of_user);
        Feild_Name = (EditText) findViewById(R.id.name_of_user);
        Feild_Number = (EditText) findViewById(R.id.contact_of_user);
        dobb = (TextView) findViewById(R.id.year_ofDOB);
        Feild_City = (EditText) findViewById(R.id.city_of_user);
        Feild_Age = (TextView) findViewById(R.id.age_of_user);
        Feild_BloodGrps = (Spinner) findViewById(R.id.spinner_BG);
        Donor_Btn = (Button) findViewById(R.id.RegisterBTN);
        Feild_Address = (EditText) findViewById(R.id.address_of_user);
    }

    boolean Validation() {
        if (Feild_BloodGrps.getSelectedItem().toString().trim().equals("Select Your Blood Group")) {
            Toast.makeText(this, "Please select your blood group.", Toast.LENGTH_SHORT).show();
            return false;
        } else if (Age.trim().toString().equals("SELECT YOUR AGE")) {
            Toast.makeText(this, "Please Select Age", Toast.LENGTH_SHORT).show();
            return false;
        } else if (age_value > 1998) {
            Toast.makeText(this, "Should Be Older than 18 to donate blood.", Toast.LENGTH_SHORT).show();
            return false;
        } else if (TextUtils.isEmpty(Address)) {
            Feild_Address.setError("Required.");
            return false;
        } else {
            return true;
        }
    }

    public static class DatePickerFragment extends DialogFragment implements DatePickerDialog.OnDateSetListener {


        @Override
        public Dialog onCreateDialog(Bundle savedInstanceState) {
            final Calendar calendar = Calendar.getInstance();
            int YEAR = calendar.get(Calendar.YEAR);
            int MONTH = calendar.get(Calendar.MONTH);
            int DAY = calendar.get(Calendar.DAY_OF_MONTH);

            DatePickerDialog DPD = new DatePickerDialog(getActivity(), android.app.AlertDialog.THEME_DEVICE_DEFAULT_DARK, this, YEAR, MONTH, DAY);

            return DPD;
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
            TextView Feild_Age = (TextView) getActivity().findViewById(R.id.age_of_user);
            Feild_Age.setText(forMatedDate);
            if (!Feild_Age.getText().toString().trim().equals("SELECT YOUR AGE")) {
                TextView dobb = (TextView) getActivity().findViewById(R.id.year_ofDOB);
                dobb.setText(String.valueOf(YEAR));
            }
        }
    }
}