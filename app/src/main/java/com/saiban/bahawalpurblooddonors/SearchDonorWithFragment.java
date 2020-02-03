package com.saiban.bahawalpurblooddonors;

import android.icu.text.DateFormat;
import android.icu.util.Calendar;
import android.os.Build;
import android.support.annotation.NonNull;
import android.support.annotation.RequiresApi;
import android.support.v4.app.FragmentManager;
import android.support.v4.view.MenuItemCompat;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.PagerSnapHelper;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.SearchView;
import android.support.v7.widget.SnapHelper;
import android.support.v7.widget.Toolbar;
import android.text.TextUtils;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.GridLayout;
import android.widget.GridView;
import android.widget.TextView;
import android.widget.Toast;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.ChildEventListener;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.saiban.bahawalpurblooddonors.Adapters.BAdapter;
import com.saiban.bahawalpurblooddonors.Adapters.DonorsAdapter;
import com.saiban.bahawalpurblooddonors.Models.Abc;
import com.miguelcatalan.materialsearchview.MaterialSearchView;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.Date;
import java.util.List;
import java.util.Locale;

public class SearchDonorWithFragment extends AppCompatActivity {
    Toolbar toolbar;
    RecyclerView recyclerView;
    List<Abc> IncomingListhere;
    SnapHelper snapHelper;
    DonorsAdapter adapter;
    TextView t;
    //////////////////////
    FirebaseAuth auth;
    FirebaseAuth.AuthStateListener Listenerl;
    FirebaseUser User;
    //////////////////////////////////////////
    GridView grid;
    BAdapter bAdapter;

    TextView t2, listLength;
    View v;


    @Override
    protected void onStart() {
        super.onStart();
        auth.addAuthStateListener(Listenerl);
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_search_donor_with_fragment);
//        setTitle("Search Blood Donor");
        listLength = (TextView) findViewById(R.id.list_length_donor);


        t2 = (TextView) findViewById(R.id.ttt);
        grid = (GridView) findViewById(R.id.findBloodGridd);
        bAdapter = new BAdapter(this);
        grid.setAdapter(bAdapter);
        ////////////
        auth = FirebaseAuth.getInstance();
        User = FirebaseAuth.getInstance().getCurrentUser();
        Listenerl = new FirebaseAuth.AuthStateListener() {
            @Override
            public void onAuthStateChanged(@NonNull FirebaseAuth firebaseAuth) {
                User = firebaseAuth.getCurrentUser();
            }
        };
        recyclerView = (RecyclerView) findViewById(R.id.donor_of_result);
        recyclerView.setLayoutManager(new LinearLayoutManager(this, LinearLayoutManager.HORIZONTAL, false));
        recyclerView.setHasFixedSize(true);
        IncomingListhere = new ArrayList<>();
        snapHelper = new PagerSnapHelper();
        snapHelper.attachToRecyclerView(recyclerView);
        adapter = new DonorsAdapter(this, IncomingListhere);
        recyclerView.setAdapter(adapter);
        t = (TextView) findViewById(R.id.nothingTxt);
//        DBRef = FirebaseDatabase.getInstance().getReference().child("Blood_Donors_OrderByBloodGroup").child("B-");
        grid.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(final AdapterView<?> adapterView, final View view, int i, long l) {
                view.setSelected(true);
                recyclerView.removeAllViews();
                adapter.ListData.clear();
                IncomingListhere.clear();
                t.setVisibility(View.VISIBLE);
                listLength.setVisibility(View.GONE);
                final String Blooood = adapterView.getItemAtPosition(i).toString();
                t2.setText("No donor found for blood group " + Blooood);
                final DatabaseReference reference = FirebaseDatabase.getInstance().getReference().child("Blood_Donors_OrderByBloodGroup").child(Blooood);
                reference.addChildEventListener(new ChildEventListener() {
                    @Override
                    public void onChildAdded(final DataSnapshot dataSnapshot, String s) {
                        if (dataSnapshot.getValue(Abc.class) != null) {
                            t2.setText("Showing Donors with Blood Group " + Blooood);
                            IncomingListhere.add(dataSnapshot.getValue(Abc.class));
                            Collections.reverse(IncomingListhere);
                            t.setVisibility(View.GONE);
                            recyclerView.setOnScrollListener(new RecyclerView.OnScrollListener() {
                                @Override
                                public void onScrolled(RecyclerView recyclerView, int dx, int dy) {
                                    super.onScrolled(recyclerView, dx, dy);
                                    int position = ((LinearLayoutManager) recyclerView.getLayoutManager()).findFirstVisibleItemPosition();
                                    v = ((LinearLayoutManager) recyclerView.getLayoutManager()).findViewByPosition(position);
                                    final TextView ops = (TextView) v.findViewById(R.id.userIDHere);
                                    final String txtValue = ops.getText().toString();
                                    final DatabaseReference lstDate = FirebaseDatabase.getInstance().getReference().child("Blood_donors_Last_donation_date").child(txtValue);
                                    lstDate.addValueEventListener(new ValueEventListener() {
                                        @Override
                                        public void onDataChange(DataSnapshot dataSnapshot) {
                                            if (dataSnapshot.getValue() != null) {
                                                String Last_Date = dataSnapshot.child("lastDonationDate").getValue().toString();
                                                TextView textView = (TextView) v.findViewById(R.id.isAvailableTxt);
                                                textView.setTextColor(getResources().getColor(R.color.colorPrimaryOrange));
                                                textView.setText("Last Donated : " + Last_Date);
                                            } else {
                                                TextView textView = (TextView) v.findViewById(R.id.isAvailableTxt);
                                                textView.setTextColor(getResources().getColor(R.color.color_success));
                                                textView.setText("Available");
                                            }
                                        }

                                        @Override
                                        public void onCancelled(DatabaseError databaseError) {
//                                            Toast.makeText(SearchDonorWithFragment.this, "Something went wrong. try again", Toast.LENGTH_SHORT).show();
                                        }
                                    });
                                    listLength.setText(Integer.toString(position + 1) + " / " + adapter.getItemCount());
                                    listLength.setVisibility(View.VISIBLE);
                                }
                            });


                            adapter.notifyDataSetChanged();

                            //us id k lehaz se donor search krna ha
                        }

                    }

                    @Override
                    public void onChildChanged(DataSnapshot dataSnapshot, String s) {

                    }

                    @Override
                    public void onChildRemoved(DataSnapshot dataSnapshot) {

                    }

                    @Override
                    public void onChildMoved(DataSnapshot dataSnapshot, String s) {

                    }

                    @Override
                    public void onCancelled(DatabaseError databaseError) {

                    }
                });
                view.setSelected(true);

            }
        });





    }


}
