package com.saiban.bahawalpurblooddonors;

import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Color;
import android.support.annotation.NonNull;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseAuthUserCollisionException;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import java.util.HashMap;
import java.util.Map;

public class UserRegister extends AppCompatActivity {
    EditText Reg_Email_Feild, Reg_Pass_Feild, Reg_Name_Feild, Reg_Contact_Feild;
    Spinner Reg_City_Feild;
    String RegEmail, RegPass, RegName, RegContact, RegCity;
    ArrayAdapter Adapter_Cities;
    Button Reg_Button;
    //
    SharedPreferences preferences;
    ///
    AlertDialog.Builder builder;
    //
    ProgressDialog PD;
    //
    FirebaseUser User;
    //
    FirebaseAuth mAuth;
    FirebaseAuth.AuthStateListener mAuthListener;
    //////////////////////
    DatabaseReference RegisteredUser_Ref;

    @Override
    protected void onStart() {
        super.onStart();
        mAuth.addAuthStateListener(mAuthListener);
    }

    @Override
    protected void onStop() {
        super.onStop();
        if (mAuthListener != null) {
            mAuth.removeAuthStateListener(mAuthListener);
        }
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN, WindowManager.LayoutParams.FLAG_FULLSCREEN);
        setContentView(R.layout.activity_user_register);
        InitViews();
        /////////
        preferences = getSharedPreferences("isDonorRegistered", Context.MODE_PRIVATE);
        final SharedPreferences.Editor editor = preferences.edit();
        if (preferences != null) {
            editor.clear();
            editor.apply();
            editor.commit();
        }
//        setting progress dialoug
        PD = new ProgressDialog(this);
        PD.setProgressStyle(ProgressDialog.STYLE_SPINNER);
        PD.setMessage("Please wait...");
        PD.setCancelable(false);
        PD.setIndeterminate(true);
        //////////////////////////
        builder = new AlertDialog.Builder(this);
        View v = LayoutInflater.from(UserRegister.this).inflate(R.layout.error_dialog,null);
        builder.setView(v);
        builder.setCancelable(true);
        /////////////////////////////////////

        mAuth = FirebaseAuth.getInstance();
        User = FirebaseAuth.getInstance().getCurrentUser();
        mAuthListener = new FirebaseAuth.AuthStateListener() {
            @Override
            public void onAuthStateChanged(@NonNull FirebaseAuth firebaseAuth) {
                User = firebaseAuth.getCurrentUser();
                if (User != null) {
                    startActivity(new Intent(getApplicationContext(), mainContent.class));
                    finish();
                }
            }
        };////////////////////////////


        Adapter_Cities = ArrayAdapter.createFromResource(this, R.array.Cities, android.R.layout.simple_spinner_item);
        Adapter_Cities.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        Reg_City_Feild.setAdapter(Adapter_Cities);
        Reg_City_Feild.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l) {
                TextView t = (TextView) adapterView.getChildAt(0);
                t.setTextColor(Color.WHITE);
                RegCity = adapterView.getItemAtPosition(i).toString();
            }

            @Override
            public void onNothingSelected(AdapterView<?> adapterView) {

            }
        });
        ////////////////////////////////////////////
        Reg_Button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (Validations()) {
                    Reg_Button.setEnabled(false);
                    PD.show();
                    UserSignUp(RegEmail, RegPass);
                }
            }
        });

    }

    void InitViews() {
        Reg_Email_Feild = (EditText) findViewById(R.id.user_email);
        Reg_Pass_Feild = (EditText) findViewById(R.id.user_Password);
        Reg_Name_Feild = (EditText) findViewById(R.id.user_Name);
        Reg_Contact_Feild = (EditText) findViewById(R.id.user_Contact);
        Reg_City_Feild = (Spinner) findViewById(R.id.Reg_City);
        Reg_Button = (Button) findViewById(R.id.RegisterBTN);
    }

    boolean Validations() {
        RegName = Reg_Name_Feild.getText().toString();
        RegEmail = Reg_Email_Feild.getText().toString();
        RegPass = Reg_Pass_Feild.getText().toString();
        RegContact = Reg_Contact_Feild.getText().toString();
        if (TextUtils.isEmpty(RegName) || TextUtils.isEmpty(RegEmail) || TextUtils.isEmpty(RegPass) || TextUtils.isEmpty(RegContact)) {
            Reg_Name_Feild.setError("Could not be empty.");
            Reg_Email_Feild.setError("Could not be empty.");
            Reg_Pass_Feild.setError("Could not be empty.");
            Reg_Contact_Feild.setError("Could not be empty.");
            return false;
        } else if (!RegEmail.contains("@") || !RegEmail.contains(".com")) {
            Reg_Email_Feild.setError("Please enter a valid email.");
            return false;
        } else if (RegPass.length() < 6) {
            Reg_Pass_Feild.setError("Password must be 6 characters.");
            return false;
        } else if (RegName.length() < 3) {
            Reg_Name_Feild.setError("Enter a valid name.");
            return false;
        } else if (RegContact.length() < 11 || RegContact.length() > 11 && TextUtils.isDigitsOnly(RegContact)) {
            Reg_Contact_Feild.setError("Please eneter a valid number.");
            return false;
        } else if (Reg_City_Feild.getSelectedItem().toString().trim().equals("Select Your City")) {
            Toast.makeText(this, "Please select your city.", Toast.LENGTH_SHORT).show();
            return false;
        } else {
            return true;
        }


    }

    void UserSignUp(final String Email, final String Pass) {
        mAuth.createUserWithEmailAndPassword(Email, Pass).addOnCompleteListener(this, new OnCompleteListener<AuthResult>() {
            @Override
            public void onComplete(@NonNull Task<AuthResult> task) {
                if (task.isSuccessful()) {
                    RegisteredUser_Ref = FirebaseDatabase.getInstance().getReference().child("REGISTERED_USERS");
                    Map<String, String> USERSMAP = new HashMap<String, String>();
                    USERSMAP.put("NAME", RegName);
                    USERSMAP.put("EMAIL", RegEmail);
                    USERSMAP.put("PASS", Pass);
                    USERSMAP.put("CONTACT", RegContact);
                    USERSMAP.put("CITY", RegCity);
                    RegisteredUser_Ref.child(User.getUid()).setValue(USERSMAP);
//                    startActivity(new Intent(getApplicationContext(), mainContent.class));
                    finish();
//                    Toast.makeText(UserRegister.this, "User Registered.", Toast.LENGTH_SHORT).show();


                } else {
                    PD.cancel();
//                    Toast.makeText(UserRegister.this, "Something went wrong, try again !", Toast.LENGTH_SHORT).show();
                }
            }
        }).addOnFailureListener(this, new OnFailureListener() {
            @Override
            public void onFailure(@NonNull Exception e) {
                if (e instanceof FirebaseAuthUserCollisionException) {
                    Reg_Button.setEnabled(true);
                    builder.show();
                }
            }
        });
    }


}
