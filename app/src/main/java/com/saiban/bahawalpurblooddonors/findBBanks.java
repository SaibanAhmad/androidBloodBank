package com.saiban.bahawalpurblooddonors;


import android.icu.text.DateFormat;
import android.os.Build;
import android.support.annotation.RequiresApi;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.PagerSnapHelper;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.SnapHelper;
import android.widget.TextView;
import android.widget.Toast;

import com.saiban.bahawalpurblooddonors.Adapters.BBAdapter;

import java.util.Date;

public class findBBanks extends AppCompatActivity {
    RecyclerView bankRecView;
    SnapHelper snapHelper;
    BBAdapter bbAdapter;
    TextView t;

    @RequiresApi(api = Build.VERSION_CODES.N)
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_find_bbanks);
        t = (TextView) findViewById(R.id.list_length_banks);
        setTitle("Blood Banks");
        bankRecView = (RecyclerView) findViewById(R.id.bloodBank_RecView);
        snapHelper = new PagerSnapHelper();
        snapHelper.attachToRecyclerView(bankRecView);
        bankRecView.setLayoutManager(new LinearLayoutManager(this, LinearLayoutManager.HORIZONTAL, false));
        bankRecView.setHasFixedSize(true);
        bbAdapter = new BBAdapter(this);
        bankRecView.setAdapter(bbAdapter);

        bankRecView.setOnScrollListener(new RecyclerView.OnScrollListener() {
            @Override
            public void onScrolled(RecyclerView recyclerView, int dx, int dy) {
                super.onScrolled(recyclerView, dx, dy);
                int position = ((LinearLayoutManager) recyclerView.getLayoutManager()).findFirstVisibleItemPosition();
                t.setText(Integer.toString(position + 1) + " / " + bbAdapter.getItemCount());
            }
        });


    }


}
