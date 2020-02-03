package com.saiban.bahawalpurblooddonors;

import android.app.ProgressDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.icu.text.SimpleDateFormat;
import android.icu.util.Calendar;
import android.os.Build;
import android.support.annotation.NonNull;
import android.support.annotation.RequiresApi;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.saiban.bahawalpurblooddonors.Models.subRequest;

import org.w3c.dom.Text;

public class submitRequest extends AppCompatActivity {
    EditText FeildName, FeildContact, FeildQuantity, FeildHospital, FieldMessage;
    Spinner BloodGroup_Spinner, City_Spinner;
    String NAME, CONTACT, BLOODGRP, QUANTITY, CITY, HOSPITAL, MESSAGE;
    ArrayAdapter BloodAdapter, CityAdapter;
    Button SubmitRequest_btn;
    ///////////////////////////////////////////
    FirebaseAuth mAuth;
    FirebaseAuth.AuthStateListener mListener;
    DatabaseReference BloodRequests_Ref;
    FirebaseUser User;
    /////////////////////////////
    ProgressDialog PD;
    AlertDialog.Builder b;

    @Override
    protected void onStart() {
        super.onStart();
        mAuth.addAuthStateListener(mListener);
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_submit_request);
        setTitle("Submit Request");
        init();
        //////////////////
        b = new AlertDialog.Builder(this);
        View v = LayoutInflater.from(this).inflate(R.layout.custom_alert, null);
        final TextView blood_requested_txt = (TextView) v.findViewById(R.id.blood_group_requested);
        final Button search_donor = (Button) v.findViewById(R.id.btn_requestd);
        Button OK = (Button) v.findViewById(R.id.btn_requestd_);
        OK.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                finish();
            }
        });
        search_donor.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startActivity(new Intent(submitRequest.this, SearchDonorWithFragment.class));
                finish();
            }
        });

//        b.setTitle("Done");
//        b.setMessage("Your Blood Request Successfuly Posted.");
//        b.setIcon(R.drawable.tick);
        b.setView(v);
        /////////////////////
        PD = new ProgressDialog(this);
        PD.setIndeterminate(true);
        PD.setMessage("Please wait...");
        PD.setCancelable(false);
        ////////////////////////////////////////////////
        mAuth = FirebaseAuth.getInstance();
        User = FirebaseAuth.getInstance().getCurrentUser();
        mListener = new FirebaseAuth.AuthStateListener() {
            @Override
            public void onAuthStateChanged(@NonNull FirebaseAuth firebaseAuth) {
                User = firebaseAuth.getCurrentUser();
                if (User == null) {
                    finish();
                }
            }
        };
        /////////////////////////////////////////////////
        BloodRequests_Ref = FirebaseDatabase.getInstance().getReference().child("Blood_Requests");
        /////////////////////////////////////////////////
        BloodAdapter = ArrayAdapter.createFromResource(this, R.array.bloodGrps, android.R.layout.simple_spinner_item);
        BloodAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        BloodGroup_Spinner.setAdapter(BloodAdapter);
        BloodGroup_Spinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l) {
                BLOODGRP = adapterView.getItemAtPosition(i).toString();
            }

            @Override
            public void onNothingSelected(AdapterView<?> adapterView) {

            }
        });
        ////////////////////////////////////////////////
        CityAdapter = ArrayAdapter.createFromResource(this, R.array.Cities, android.R.layout.simple_spinner_item);
        CityAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        City_Spinner.setAdapter(CityAdapter);
        City_Spinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l) {
                CITY = adapterView.getItemAtPosition(i).toString();
            }

            @Override
            public void onNothingSelected(AdapterView<?> adapterView) {

            }
        });
        ///////////////////////////////////////////////


        SubmitRequest_btn.setOnClickListener(new View.OnClickListener() {
            @RequiresApi(api = Build.VERSION_CODES.N)
            @Override
            public void onClick(View view) {
                if (validation()) {
                    SubmitRequest_btn.setEnabled(false);
                    PD.show();
                    Calendar c = Calendar.getInstance();
                    SimpleDateFormat DF = new SimpleDateFormat("dd-MMM-yyyy");
                    String Date = DF.format(c.getTime());
                    final subRequest Obj = new subRequest();
                    Obj.setNAME(NAME);
                    Obj.setCONTACT(CONTACT);
                    Obj.setBLOODGRP(BLOODGRP);
                    Obj.setCITY(CITY);
                    Obj.setHOSPITAL(HOSPITAL);
                    Obj.setMESSAGE(MESSAGE);
                    Obj.setQUANTITY(QUANTITY);
                    Obj.setONDATE(Date);
                    DatabaseReference RequestsOrderByBlood = FirebaseDatabase.getInstance().getReference().child("RequestsOrderByBlood").child(BLOODGRP);
                    RequestsOrderByBlood.push().setValue(Obj).addOnCompleteListener(submitRequest.this, new OnCompleteListener<Void>() {
                        @Override
                        public void onComplete(@NonNull Task<Void> task) {
                            if (task.isSuccessful()) {
                                PD.cancel();
                                blood_requested_txt.setText(BLOODGRP);
                                b.show();
                            }
                        }
                    });
                }
            }
        });


    }

    void init() {
        FeildName = (EditText) findViewById(R.id.req_Name);
        FeildContact = (EditText) findViewById(R.id.req_Contact);
        BloodGroup_Spinner = (Spinner) findViewById(R.id.bloodgrouptorequest);
        FeildQuantity = (EditText) findViewById(R.id.BloodQuantity);
        City_Spinner = (Spinner) findViewById(R.id.CityFromRequesting);
        FeildHospital = (EditText) findViewById(R.id.HospitalFromRequesting);
        FieldMessage = (EditText) findViewById(R.id.RequestingMessage);
        SubmitRequest_btn = (Button) findViewById(R.id.SubmitRequest_btn);
    }

    boolean validation() {
        NAME = FeildName.getText().toString();
        CONTACT = FeildContact.getText().toString();
        QUANTITY = FeildQuantity.getText().toString();
        HOSPITAL = FeildHospital.getText().toString();
        MESSAGE = FieldMessage.getText().toString();
        if (TextUtils.isEmpty(NAME) || TextUtils.isEmpty(CONTACT) || TextUtils.isEmpty(QUANTITY) || TextUtils.isEmpty(HOSPITAL) || TextUtils.isEmpty(MESSAGE)) {
            FeildName.setError("Required.");
            FeildContact.setError("Required.");
            FeildQuantity.setError("Required.");
            FeildHospital.setError("Required.");
            FieldMessage.setError("Required.");
            return false;
        } else if (NAME.length() < 3) {
            FeildName.setError("Please enter a valid name.");
            return false;
        } else if (CONTACT.length() < 11 || CONTACT.length() > 11 && TextUtils.isEmpty(CONTACT)) {
            FeildContact.setError("Please enter valid contact number.");
            return false;
        } else if (TextUtils.isEmpty(QUANTITY)) {
            FeildQuantity.setError("Required.");
            return false;
        } else if (TextUtils.isEmpty(HOSPITAL)) {
            FeildHospital.setError("Required.");
            return false;
        } else if (TextUtils.isEmpty(MESSAGE)) {
            FieldMessage.setError("Required.");
            return false;
        } else if (City_Spinner.getSelectedItem().toString().trim().equals("Select Your City")) {
            Toast.makeText(this, "Please Select City", Toast.LENGTH_SHORT).show();
            return false;
        } else if (BloodGroup_Spinner.getSelectedItem().toString().trim().equals("Select Your Blood Group")) {
            Toast.makeText(this, "Please Select Blood Group", Toast.LENGTH_SHORT).show();
            return false;
        } else {
            return true;
        }

    }
}
