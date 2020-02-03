package com.saiban.bahawalpurblooddonors.menuActivity;

import android.content.Context;
import android.os.Build;
import android.support.annotation.NonNull;
import android.support.annotation.RequiresApi;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.inputmethod.InputMethodManager;
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
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.saiban.bahawalpurblooddonors.R;

import java.util.HashMap;
import java.util.Map;

public class editProfile extends AppCompatActivity {
    EditText field_email, field_name, field_contact, field_pass;
    TextView city;
    Spinner field_citySpinner;
    Button saveBtn;
    String NAME, EMAIL, CONTACT, CITY, PASS;
    ArrayAdapter adapter;
    ///////////////////////
    FirebaseAuth mAuth;
    FirebaseAuth.AuthStateListener Listener;
    FirebaseUser user;
    DatabaseReference reference;
    Menu menu;

//    String NAME_OF_USER, EMAIL_OF_USER, PASS_OF_USER, CONTACT_OFUSER, CITY_OF_USER;

    @Override
    protected void onStart() {
        super.onStart();
        mAuth.addAuthStateListener(Listener);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.edit, menu);
        this.menu = menu;
        return super.onCreateOptionsMenu(menu);
    }


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_edit_profile);
        setTitle("Profile");
        ViewsInit();

        mAuth = FirebaseAuth.getInstance();
        user = FirebaseAuth.getInstance().getCurrentUser();
        Listener = new FirebaseAuth.AuthStateListener() {
            @Override
            public void onAuthStateChanged(@NonNull FirebaseAuth firebaseAuth) {
                user = firebaseAuth.getCurrentUser();
            }
        };

        reference = FirebaseDatabase.getInstance().getReference().child("REGISTERED_USERS").child(user.getUid());
        reference.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                String NAME_OF_USER = dataSnapshot.child("NAME").getValue().toString();
                String EMAIL_OF_USER = dataSnapshot.child("EMAIL").getValue().toString();
                String PASS_OF_USER = dataSnapshot.child("PASS").getValue().toString();
                String CONTACT_OFUSER = dataSnapshot.child("CONTACT").getValue().toString();
                String CITY_OF_USER = dataSnapshot.child("CITY").getValue().toString();

                field_name.setText(NAME_OF_USER);
                field_email.setText(EMAIL_OF_USER);
                field_pass.setText(PASS_OF_USER);
                field_contact.setText(CONTACT_OFUSER);
                int position = adapter.getPosition(CITY_OF_USER);
                field_citySpinner.setSelection(position);


            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });
        adapter = ArrayAdapter.createFromResource(this, R.array.Cities, android.R.layout.simple_spinner_item);
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        field_citySpinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l) {
                CITY = adapterView.getItemAtPosition(i).toString();
            }

            @Override
            public void onNothingSelected(AdapterView<?> adapterView) {

            }
        });

        field_citySpinner.setAdapter(adapter);


        /////////////////////////////////////////////////////////////////////
        saveBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (valid()) {
                    HashMap<String, String> map = new HashMap<String, String>();
                    map.put("NAME", field_name.getText().toString());
                    map.put("EMAIL", field_email.getText().toString());
                    map.put("PASS", field_pass.getText().toString());
                    map.put("CONTACT", field_contact.getText().toString());
                    map.put("CITY", CITY);
                    reference.setValue(map).addOnCompleteListener(new OnCompleteListener<Void>() {
                        @Override
                        public void onComplete(@NonNull Task<Void> task) {
                            if (task.isSuccessful()) {
                                AlertDialog.Builder builder = new AlertDialog.Builder(editProfile.this);
                                builder.setView(LayoutInflater.from(editProfile.this).inflate(R.layout.success_updated, null));
                                builder.show();
                                field_email.setEnabled(false);
                                field_name.setEnabled(false);
                                field_contact.setEnabled(false);
                                field_pass.setEnabled(false);
                                saveBtn.setEnabled(false);
                            }
                        }
                    });
                }
            }
        });


    }

    void ViewsInit() {
        field_email = (EditText) findViewById(R.id.update_email);
        field_name = (EditText) findViewById(R.id.update_name);
        field_contact = (EditText) findViewById(R.id.update_contact);
        city = (TextView) findViewById(R.id.cit);
        saveBtn = (Button) findViewById(R.id.update_profileBTN);
        field_citySpinner = (Spinner) findViewById(R.id.update_city_spinner);
        field_pass = (EditText) findViewById(R.id.update_password);
    }

    @RequiresApi(api = Build.VERSION_CODES.O)
    @Override
    public boolean onOptionsItemSelected(MenuItem item) {


        int id = item.getItemId();
        if (id == R.id.profile_edit) {
            field_email.setEnabled(true);
            field_name.setEnabled(true);
            field_name.requestFocus();
            InputMethodManager imm = (InputMethodManager) getSystemService(Context.INPUT_METHOD_SERVICE);
            imm.showSoftInput(field_name, InputMethodManager.SHOW_IMPLICIT);
            field_contact.setEnabled(true);
            saveBtn.setEnabled(true);
            menu.getItem(0).setVisible(false);
            menu.getItem(1).setVisible(true);

        } else if (id == R.id.profile_edit_cross) {
            field_email.setEnabled(false);
            field_name.setEnabled(false);
            field_contact.setEnabled(false);
            saveBtn.setEnabled(false);
            menu.getItem(1).setVisible(false);
            menu.getItem(0).setVisible(true);
        }
        return super.onOptionsItemSelected(item);
    }

    boolean valid() {
        EMAIL = field_email.getText().toString();
        NAME = field_name.getText().toString();
        CONTACT = field_contact.getText().toString();
        PASS = field_pass.getText().toString();
        if (TextUtils.isEmpty(EMAIL) || TextUtils.isEmpty(NAME) || TextUtils.isEmpty(CONTACT) || TextUtils.isEmpty(PASS)) {
            field_email.setError("Required.");
            field_name.setError("Required.");
            field_contact.setError("Required.");
            field_pass.setError("Required.");
            return false;
        } else if (TextUtils.isEmpty(NAME) || TextUtils.isEmpty(CONTACT)) {
            field_name.setError("Required.");
            field_contact.setError("Required.");
            return false;
        } else if (TextUtils.isEmpty(CONTACT)) {
            field_contact.setError("Required.");
            return false;
        } else if (field_citySpinner.getSelectedItem().toString().trim().equals("Select Your City")) {
            Toast t = Toast.makeText(this, "Please select city.", Toast.LENGTH_SHORT);
            t.setGravity(Gravity.CENTER, 0, 0);
            t.show();
            return false;
        } else if (!EMAIL.contains("@") || !EMAIL.contains(".com") || !EMAIL.contains("mail")) {
            field_email.setError("Please enter a valid email");
            return false;
        } else if (PASS.length() < 4) {
            field_pass.setError("Password too short.");
            return false;
        } else {
            return true;
        }
    }
}
