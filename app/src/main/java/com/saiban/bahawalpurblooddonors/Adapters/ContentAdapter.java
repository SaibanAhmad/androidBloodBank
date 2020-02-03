package com.saiban.bahawalpurblooddonors.Adapters;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.saiban.bahawalpurblooddonors.R;

public class ContentAdapter extends BaseAdapter {

    String[] Content_Titles = {"SEARCH DONOR", "LOCATE BLOOD BANKS", "BLOOD REQUESTS", "SUBMIT REQUESTS", "CREATE DONOR PROFILE", "REMINDER"};
    int[] Content_Images = {R.drawable.search_donor,R.drawable.locate_banks,R.drawable.blood_requests,R.drawable.submit_request,R.drawable.add_donor,R.drawable.create_alarm};
    Context context;

    public ContentAdapter(Context context) {
        this.context = context;
    }

    @Override
    public int getCount() {
        return Content_Titles.length;
    }

    @Override
    public Object getItem(int i) {
        return null;
    }

    @Override
    public long getItemId(int i) {
        return 0;
    }

    @Override
    public View getView(int i, View view, ViewGroup viewGroup) {
        if (view == null) {
            view = LayoutInflater.from(context).inflate(R.layout.gried_item, viewGroup, false);
        }
        ImageView img = (ImageView) view.findViewById(R.id.thumbnail);
        TextView txt = (TextView) view.findViewById(R.id.titleText);
        img.setImageResource(Content_Images[i]);
        txt.setText(Content_Titles[i]);
        return view;
    }
}
