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
import android.widget.Toast;

import com.saiban.bahawalpurblooddonors.Models.subRequest;
import com.saiban.bahawalpurblooddonors.R;

import java.util.List;

/**
 * Created by maliksaif232 on 7/8/2017.
 */

public class view_req_adapter extends RecyclerView.Adapter<view_req_adapter.ViewHolderClass> {
    public view_req_adapter(Context context, List<subRequest> listData) {
        this.context = context;
        this.listData = listData;
    }

    Context context;
    public List<subRequest> listData;

    @Override
    public ViewHolderClass onCreateViewHolder(ViewGroup parent, int viewType) {
        View v = LayoutInflater.from(context).inflate(R.layout.viewrequests_d, parent, false);
        ViewHolderClass viewHolderClass = new ViewHolderClass(v);
        return viewHolderClass;
    }

    @Override
    public void onBindViewHolder(ViewHolderClass holder, int position) {
        final subRequest Obj = listData.get(position);
        holder.t1.setText(Obj.getNAME());
        holder.t2.setText(Obj.getONDATE());
        holder.t3.setText(Obj.getBLOODGRP());
        holder.t4.setText(Obj.getCITY());
        holder.t5.setText(Obj.getHOSPITAL());
        holder.t6.setText(Obj.getQUANTITY());
        holder.t7.setText(Obj.getCONTACT());
        holder.t8.setText(Obj.getMESSAGE());
        holder.callbtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(Intent.ACTION_DIAL);
                String User = "tel:" + Obj.getCONTACT();
                intent.setData(Uri.parse(User));
                context.startActivity(intent);
            }
        });
    }

    @Override
    public int getItemCount() {
        return listData.size();
    }

    public class ViewHolderClass extends RecyclerView.ViewHolder {
        TextView t1, t2, t3, t4, t5, t6, t7, t8;
        Button callbtn;

        public ViewHolderClass(View itemView) {
            super(itemView);
            t1 = (TextView) itemView.findViewById(R.id.DonorName);
            t2 = (TextView) itemView.findViewById(R.id.dateOnPosted);
            t3 = (TextView) itemView.findViewById(R.id.bloodGrp);
            t4 = (TextView) itemView.findViewById(R.id.City);
            t5 = (TextView) itemView.findViewById(R.id.HospitalLocation);
            t6 = (TextView) itemView.findViewById(R.id.requiredBag);
            t7 = (TextView) itemView.findViewById(R.id.Contact);
            t8 = (TextView) itemView.findViewById(R.id.SeekerMessage);
            callbtn = (Button) itemView.findViewById(R.id.callButton);

        }
    }
}
