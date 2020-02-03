package com.saiban.bahawalpurblooddonors.Adapters;

import android.Manifest;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.net.Uri;
import android.support.v4.app.ActivityCompat;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;

import com.saiban.bahawalpurblooddonors.Models.Abc;
import com.saiban.bahawalpurblooddonors.Models.Reg_Donor;
import com.saiban.bahawalpurblooddonors.R;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by maliksaif232 on 7/9/2017.
 */

public class DonorsAdapter extends RecyclerView.Adapter<DonorsAdapter.CVH> {
    Context context;
    public List<Abc> ListData;

    public DonorsAdapter(Context context, List<Abc> listData) {
        this.context = context;
        ListData = listData;
    }

    @Override
    public CVH onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(context).inflate(R.layout.donors_row, parent, false);
        CVH cvh = new CVH(view);
        return cvh;
    }

    @Override
    public void onBindViewHolder(CVH holder, int position) {
        final Abc Obj = ListData.get(position);
        holder.t1.setText(Obj.getName());
        holder.t2.setText(Obj.getBloodGroup());
        holder.t3.setText(Obj.getCity());
        holder.t4.setText(Obj.getAddress());
        holder.t5.setText(Obj.getContact());
        holder.t6Id.setText(Obj.getUserId());
        holder.Btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(Intent.ACTION_DIAL);
//                String User = "tel:" + Obj.getContact();
                intent.setData(Uri.parse("tel:" + Obj.getContact()));
                context.startActivity(intent);

            }
        });
    }

    @Override
    public int getItemCount() {
        return ListData.size();
    }


    public class CVH extends RecyclerView.ViewHolder {
        TextView t1, t2, t3, t4, t5, t6Id;
        Button Btn;

        public CVH(View itemView) {
            super(itemView);
            t1 = (TextView) itemView.findViewById(R.id.Name_of_Donor);
            t2 = (TextView) itemView.findViewById(R.id.Donor_BloodGrp);
            t3 = (TextView) itemView.findViewById(R.id.donor_city);
            t4 = (TextView) itemView.findViewById(R.id.donor_address);
            t5 = (TextView) itemView.findViewById(R.id.donor_contact);
            t6Id = (TextView) itemView.findViewById(R.id.userIDHere);
            Btn = (Button) itemView.findViewById(R.id.call_Button);

        }

    }

    public void setFilter(ArrayList<Abc> filterList) {
        ListData = new ArrayList<>();
        ListData.addAll(filterList);
        notifyDataSetChanged();
    }


}

