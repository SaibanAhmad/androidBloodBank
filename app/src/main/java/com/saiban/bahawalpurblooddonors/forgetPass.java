package com.saiban.bahawalpurblooddonors;

import android.content.DialogInterface;
import android.support.annotation.NonNull;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseAuthInvalidUserException;

public class forgetPass extends AppCompatActivity {

    EditText FieldEMail;
    Button ResetPass;
    String EMAIL;
    FirebaseAuth auth;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN, WindowManager.LayoutParams.FLAG_FULLSCREEN);
        setContentView(R.layout.activity_forget_pass);
        init();
        auth = FirebaseAuth.getInstance();
        ResetPass.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (valid()) {
                    auth.sendPasswordResetEmail(EMAIL).addOnCompleteListener(forgetPass.this, new OnCompleteListener<Void>() {
                        @Override
                        public void onComplete(@NonNull Task<Void> task) {
                            if (task.isSuccessful()) {
                                AlertDialog.Builder builder = new AlertDialog.Builder(forgetPass.this);
                                builder.setMessage("A Password Recovery Email has been sent to your email address, open your email and click the link.");
                                builder.setPositiveButton("OK", new DialogInterface.OnClickListener() {
                                    @Override
                                    public void onClick(DialogInterface dialogInterface, int i) {
                                        finish();
                                    }
                                });
                                builder.show();
                            }
                        }
                    }).addOnFailureListener(forgetPass.this, new OnFailureListener() {
                        @Override
                        public void onFailure(@NonNull Exception e) {
                            if (e instanceof FirebaseAuthInvalidUserException) {
                                Toast.makeText(forgetPass.this, "User With this email not found", Toast.LENGTH_LONG).show();
                            }
                        }
                    });
                }
            }
        });
    }

    void init() {
        FieldEMail = (EditText) findViewById(R.id.reset_email);
        ResetPass = (Button) findViewById(R.id.reset_btn);
    }

    boolean valid() {
        EMAIL = FieldEMail.getText().toString();
        if (TextUtils.isEmpty(EMAIL)) {
            FieldEMail.setError("Required.");
            return false;
        } else if (!EMAIL.contains("@") || !EMAIL.contains(".com")) {
            FieldEMail.setError("Enter a valid email.");
            return false;
        } else {
            return true;
        }
    }
}
