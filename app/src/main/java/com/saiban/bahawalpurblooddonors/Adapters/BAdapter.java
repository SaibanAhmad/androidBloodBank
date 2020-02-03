package com.saiban.bahawalpurblooddonors.Adapters;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import com.saiban.bahawalpurblooddonors.R;

import java.util.List;

/**
 * Created by maliksaif232 on 7/9/2017.
 */

public class BAdapter extends BaseAdapter {
    Context context;
    String[] BG = {"A+","A-","B+","B-","O+","O-","AB+","AB-"};

    public BAdapter(Context context) {
        this.context = context;
    }

    @Override
    public int getCount() {
        return BG.length;
    }

    @Override
    public Object getItem(int i) {
        return BG[i];
    }

    @Override
    public long getItemId(int i) {
        return 0;
    }

    @Override
    public View getView(int i, View view, ViewGroup viewGroup) {
        if (view == null) {
            view = LayoutInflater.from(context).inflate(R.layout.b_,viewGroup,false);
        }
        TextView B = (TextView) view.findViewById(R.id.bTxt);
        B.setText(BG[i]);
        return view;
    }
}
