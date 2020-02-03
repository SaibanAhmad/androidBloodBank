package com.saiban.bahawalpurblooddonors;

import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.PagerSnapHelper;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.SnapHelper;
import android.view.View;
import android.widget.AdapterView;
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
import com.saiban.bahawalpurblooddonors.Adapters.BAdapter;
import com.saiban.bahawalpurblooddonors.Adapters.view_req_adapter;
import com.saiban.bahawalpurblooddonors.Models.subRequest;

import org.w3c.dom.Text;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

public class viewRequest extends AppCompatActivity {

    FirebaseAuth mAuth;
    FirebaseAuth.AuthStateListener mListener;
    FirebaseUser User;
    DatabaseReference Ref;

    List<subRequest> InComingDataList;

    view_req_adapter adapter;

    RecyclerView RecView;

    SnapHelper snapHelper;

    GridView gridView;
    BAdapter bAdapter;
    TextView t, t22, listLength;

    @Override
    protected void onStart() {
        super.onStart();
        mAuth.addAuthStateListener(mListener);
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_view_request);
        setTitle("Blood Requests");
        t = (TextView) findViewById(R.id.nothingTxt_2);
        t22 = (TextView) findViewById(R.id.ttt);
        listLength = (TextView) findViewById(R.id.list_length);

        gridView = (GridView) findViewById(R.id.blood_request_grid);
        bAdapter = new BAdapter(this);
        gridView.setAdapter(bAdapter);


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
        ////////////////////////////////////////////////////////////
        ////////////////////////////////////////////////////////////
        InComingDataList = new ArrayList<>();
        RecView = (RecyclerView) findViewById(R.id.recView);
        RecView.setHasFixedSize(true);
        RecView.setLayoutManager(new LinearLayoutManager(this, LinearLayoutManager.HORIZONTAL, false));
        snapHelper = new PagerSnapHelper();
        snapHelper.attachToRecyclerView(RecView);
        adapter = new view_req_adapter(viewRequest.this, InComingDataList);

        RecView.setAdapter(adapter);
        ////////////////////////////////////
        ///////////////////////////

        gridView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
                view.setSelected(true);
                RecView.removeAllViews();
                adapter.listData.clear();
                InComingDataList.clear();
                t.setVisibility(View.VISIBLE);
                listLength.setVisibility(View.GONE);
                final String BG = adapterView.getItemAtPosition(i).toString();
                t22.setText("No request found for Blood Group " + BG);
                Ref = FirebaseDatabase.getInstance().getReference().child("RequestsOrderByBlood").child(BG);
                Ref.addChildEventListener(new ChildEventListener() {
                    @Override
                    public void onChildAdded(DataSnapshot dataSnapshot, String s) {
                        if (dataSnapshot.getValue(subRequest.class) != null) {
                            InComingDataList.add(dataSnapshot.getValue(subRequest.class));
                            Collections.reverse(InComingDataList);
                            t.setVisibility(View.GONE);
                            t22.setText("Showing BloodRequests for " + BG);
                            adapter.notifyDataSetChanged();
                            RecView.setOnScrollListener(new RecyclerView.OnScrollListener() {
                                @Override
                                public void onScrolled(RecyclerView recyclerView, int dx, int dy) {
                                    super.onScrolled(recyclerView, dx, dy);
                                    int position = ((LinearLayoutManager) recyclerView.getLayoutManager()).findFirstVisibleItemPosition();
                                    listLength.setText(Integer.toString(position + 1) + " / " + adapter.getItemCount());
                                    listLength.setVisibility(View.VISIBLE);
                                }
                            });
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
//                listLength.setText(" / " + adapter.getItemCount());


            }
        });

        ///////////////////////////


    }
}
