package com.saiban.bahawalpurblooddonors;

import android.app.ProgressDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.text.TextUtils;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseAuthInvalidCredentialsException;
import com.google.firebase.auth.FirebaseAuthInvalidUserException;
import com.google.firebase.auth.FirebaseUser;


public class UserLogin extends AppCompatActivity {
    EditText User_Email_Field, User_Password_Field;
    String User_Email, User_Password;
    Button Button_SignIn;
    /////////
    FirebaseUser User;
    FirebaseAuth mAuth;
    FirebaseAuth.AuthStateListener mListener;
    ////////////////////
    ProgressDialog PD;
    AlertDialog.Builder builder;
    AlertDialog.Builder builder2;

    TextView forgetPassword;

//
//    @Override
//    protected void onStart() {
//        super.onStart();
//        mAuth.addAuthStateListener(mListener);
//
//    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN, WindowManager.LayoutParams.FLAG_FULLSCREEN);
        setContentView(R.layout.activity_user_login);
        INIT();
        /////////////
        forgetPassword.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startActivity(new Intent(getApplicationContext(), forgetPass.class));
            }
        });

        PD = new ProgressDialog(this);
        PD.setMessage("Signing In Please Wait...");
        PD.setCancelable(false);
        PD.setIndeterminate(true);
        //////////////
        builder = new AlertDialog.Builder(this);
        builder.setTitle("Sign in failed");
        builder.setMessage("Invalid Password.");
        builder.setIcon(R.drawable.alert);
        builder.setCancelable(false);
        builder.setPositiveButton("OK", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialogInterface, int i) {
                return;
            }
        });
        /////////////
        builder2 = new AlertDialog.Builder(this);
        builder2.setTitle("Authentication Failed");
        builder2.setMessage("User with this email does not exist.");
        builder2.setCancelable(false);
        builder2.setIcon(R.drawable.alert);
        builder2.setPositiveButton("OK", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialogInterface, int i) {
                return;
            }
        });
        /////////////
        mAuth = FirebaseAuth.getInstance();
        if (mAuth.getCurrentUser() != null) {
            startActivity(new Intent(getApplicationContext(), mainContent.class));
            finish();
        }

//        mListener = new FirebaseAuth.AuthStateListener() {
//            @Override
//            public void onAuthStateChanged(@NonNull FirebaseAuth firebaseAuth) {
//                User = FirebaseAuth.getInstance().getCurrentUser();
//                if (User != null) {
//                    startActivity(new Intent(getApplicationContext(), mainContent.class));
//                    finish();
//                }
//            }
//        };
        /////////////


        ////////////

        Button_SignIn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (validation()) {
                    Button_SignIn.setEnabled(false);
                    PD.show();
                    SignInUser(User_Email, User_Password);
                }
            }
        });

    }

    void INIT() {
        User_Email_Field = (EditText) findViewById(R.id.email);
        User_Password_Field = (EditText) findViewById(R.id.password);
        Button_SignIn = (Button) findViewById(R.id.Btn_Signin);
        forgetPassword = (TextView) findViewById(R.id.forgetPass);
    }

    boolean validation() {
        User_Email = User_Email_Field.getText().toString();
        User_Password = User_Password_Field.getText().toString();
        if (TextUtils.isEmpty(User_Email)) {
            User_Email_Field.setError("Email Required.");
            return false;
        } else if (!User_Email.contains("@") || !User_Email.contains(".com")) {
            User_Email_Field.setError("Enter valid email");
            return false;
        } else if (TextUtils.isEmpty(User_Password)) {
            User_Password_Field.setError("Required.");
            return false;
        } else {
            return true;
        }
    }

    void SignInUser(String Em, String Pas) {
        mAuth.signInWithEmailAndPassword(Em, Pas).addOnCompleteListener(this, new OnCompleteListener<AuthResult>() {
            @Override
            public void onComplete(@NonNull Task<AuthResult> task) {
                if (task.isSuccessful()) {
                    startActivity(new Intent(getApplicationContext(), mainContent.class));
                    finish();
                    PD.cancel();
                }
            }
        }).addOnFailureListener(this, new OnFailureListener() {
            @Override
            public void onFailure(@NonNull Exception e) {
                if (e instanceof FirebaseAuthInvalidCredentialsException) {
                    Button_SignIn.setEnabled(true);
                    PD.cancel();
                    builder.show();
                } else if (e instanceof FirebaseAuthInvalidUserException) {
                    Button_SignIn.setEnabled(true);
                    PD.cancel();
                    builder2.show();
                }
            }
        });
    }


}

