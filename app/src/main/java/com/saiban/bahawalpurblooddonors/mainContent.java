package com.saiban.bahawalpurblooddonors;

import android.app.ProgressDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Color;
import android.os.Build;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.RequiresApi;
import android.support.v7.app.AlertDialog;
import android.text.SpannableString;
import android.text.style.TextAppearanceSpan;
import android.view.View;
import android.support.design.widget.NavigationView;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.AdapterView;
import android.widget.GridView;
import android.widget.TextView;
import android.widget.Toast;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.saiban.bahawalpurblooddonors.Adapters.ContentAdapter;
import com.saiban.bahawalpurblooddonors.menuActivity.about;
import com.saiban.bahawalpurblooddonors.menuActivity.bloodCompatibility;
import com.saiban.bahawalpurblooddonors.menuActivity.contact;
import com.saiban.bahawalpurblooddonors.menuActivity.editProfile;

public class mainContent extends AppCompatActivity
        implements NavigationView.OnNavigationItemSelectedListener {

    GridView gridView;
    /////////////////////
    ProgressDialog PD;
    ////////////////////////
    String email, name;
    FirebaseAuth mAuth;
    FirebaseAuth.AuthStateListener listener;
    ////////////////////////
    DatabaseReference reference;
    /////////////////////
    SharedPreferences preferences;
    ////////////////
    AlertDialog.Builder builder;
//    AdView mAdView;
//    InterstitialAd interstitialAd;

    @Override
    protected void onStart() {
        super.onStart();
        mAuth.addAuthStateListener(listener);
    }


    @Override
    protected void onStop() {
        super.onStop();
        if (listener != null) {
            mAuth.removeAuthStateListener(listener);
        }
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main_content);
//        MobileAds.initialize(getApplicationContext(), "ca-app-pub-9632838240122948~7113351037");
//        interstitialAd = new InterstitialAd(this);
//        interstitialAd.setAdUnitId("ca-app-pub-9632838240122948/6575579816");
//        interstitialAd.loadAd(new AdRequest.Builder().build());


        //////////////////////////////////////////////////

        final TextView textView = (TextView) findViewById(R.id.welcomeTxtView);
        setTitle("Dashboard");
        builder = new AlertDialog.Builder(this);
        builder.setMessage("Are you sure you want to logout ?");
        builder.setPositiveButton("Yes", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialogInterface, int i) {
                mAuth.signOut();
                finish();
            }
        });
        builder.setNegativeButton("No", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialogInterface, int i) {
                return;
            }
        });

        preferences = getSharedPreferences("isDonorRegistered", Context.MODE_PRIVATE);
        final int a = preferences.getInt("User_Donor_Registered", 0);
        PD = new ProgressDialog(this);
        PD.setCancelable(true);
        PD.setMessage("Please wait...");
        PD.show();
        PD.setIndeterminate(true);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        mAuth = FirebaseAuth.getInstance();
        final FirebaseUser User = FirebaseAuth.getInstance().getCurrentUser();
        listener = new FirebaseAuth.AuthStateListener() {
            @Override
            public void onAuthStateChanged(@NonNull FirebaseAuth firebaseAuth) {
                FirebaseUser User = firebaseAuth.getCurrentUser();
                if (User == null) {
                    startActivity(new Intent(mainContent.this, UserLogin.class));
                    finish();
                }
            }
        };
        reference = FirebaseDatabase.getInstance().getReference().child("REGISTERED_USERS").child(User.getUid()).child("NAME");


        /////////////////----------------------------------


        //////////////////////////////////////
        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(
                this, drawer, toolbar, R.string.navigation_drawer_open, R.string.navigation_drawer_close);
        drawer.setDrawerListener(toggle);
        toggle.syncState();

        NavigationView navigationView = (NavigationView) findViewById(R.id.nav_view);
        navigationView.setNavigationItemSelectedListener(this);
        View view = navigationView.getHeaderView(0);
        //////////////////setting MENU ITEM 'USER' Color
        Menu menu = navigationView.getMenu();
        MenuItem menuItem = menu.findItem(R.id.title_menu);
        SpannableString s = new SpannableString(menuItem.getTitle());
        s.setSpan(new TextAppearanceSpan(this, R.style.TextAppearance44), 0, s.length(), 0);
        menuItem.setTitle(s);
        //////////new menu ka color change
        Menu menu2 = navigationView.getMenu();
        MenuItem menuItem2 = menu2.findItem(R.id.title_menu_2);
        SpannableString s_2 = new SpannableString(menuItem2.getTitle());
        s_2.setSpan(new TextAppearanceSpan(this, R.style.TextAppearance44), 0, s_2.length(), 0);
        menuItem2.setTitle(s_2);
        //////////////////setting MENU ITEM 'USER' Color
        final TextView userEmail = view.findViewById(R.id.emailoflogineduser);
        userEmail.setText(User.getEmail());
        final TextView userName = view.findViewById(R.id.nameoflogineduser);

        ///////////////////////////////
        reference.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                name = dataSnapshot.getValue().toString();
                userName.setText(name);
                textView.setText("Hi, " + name);
                PD.cancel();
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });
        ////////////////////////////////
        //////////////////////////////////////////////////
        gridView = (GridView) findViewById(R.id.gridView);
        ContentAdapter contentAdapter = new ContentAdapter(this);
        gridView.setAdapter(contentAdapter);
        gridView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
                switch (i) {
                    case 0:
                        startActivity(new Intent(mainContent.this, SearchDonorWithFragment.class));
                        break;
                    case 1:
                        startActivity(new Intent(mainContent.this, findBBanks.class));
                        break;
                    case 2:
                        startActivity(new Intent(mainContent.this, viewRequest.class));
                        break;
                    case 3:
                        startActivity(new Intent(mainContent.this, submitRequest.class));
                        break;
                    case 4:
                        if (a == 1) {
                            Toast.makeText(mainContent.this, "You Already Registered as a Donor.", Toast.LENGTH_LONG).show();
                        } else {
                            startActivity(new Intent(mainContent.this, DonorProfile.class));
                        }
                        break;
                    case 5:
                        startActivity(new Intent(mainContent.this, donorReminder.class));
                        break;
                }
            }
        });


        ////////////////////////////////////////////////////////////////////


    }

    @Override
    public void onBackPressed() {
        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        if (drawer.isDrawerOpen(GravityCompat.START)) {
            drawer.closeDrawer(GravityCompat.START);
//        } else if (interstitialAd.isLoaded()) {
//            interstitialAd.show();
        } else {
            super.onBackPressed();
            finish();
        }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.main_content, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_settings) {
            builder.show();
        } else if (id == R.id.edit_profile) {
            startActivity(new Intent(this, editProfile.class));
        }

        return super.onOptionsItemSelected(item);
    }

    @SuppressWarnings("StatementWithEmptyBody")
    @Override
    public boolean onNavigationItemSelected(MenuItem item) {
        // Handle navigation view item clicks here.
        int id = item.getItemId();

        if (id == R.id.nav_home) {
            // Handle the camera action

        } else if (id == R.id.nav_about) {
            startActivity(new Intent(this, about.class));

        } else if (id == R.id.nav_Contact) {
            startActivity(new Intent(mainContent.this, contact.class));
        } else if (id == R.id.nav_bloodcompatibility) {
            startActivity(new Intent(this, bloodCompatibility.class));
        } else if (id == R.id.nav_logout) {
            builder.show();
//            mAuth.signOut();
//            finish();
        } else if (id == R.id.nav_edit) {
            startActivity(new Intent(this, editProfile.class));
        }

//        else if (id == R.id.nav_edit) {
//        startActivity(new Intent(this, editProfile.class));

        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        drawer.closeDrawer(GravityCompat.START);
        return true;
    }
}
