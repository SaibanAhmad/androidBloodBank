package com.saiban.bahawalpurblooddonors.Adapters;

import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.support.v7.widget.RecyclerView;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import com.saiban.bahawalpurblooddonors.R;

import java.util.Locale;

/**
 * Created by maliksaif232 on 7/14/2017.
 */

public class BBAdapter extends RecyclerView.Adapter<BBAdapter.VH> {
    String[] BankNames = {"Ali Zaib Blood Transfusion Services", "Ali Zaib Foundation", "Sundas Foundation", "Husaini blood bank", "Armed Forces Institute of Transfusion", "Husaini Blood Bank", "Pakistan Blood Bank", "fatmeed foundation", "Zainabia Blood Bank | Thalassaemia Centre", "O-Blood", "Husaini Blood Bank", "Sukkur Blood Bank Hospital", "Ali Zaib Blood Bank", "Afzaal Memorial Thalassemia Foundation (AMTF)", "Fatimid Foundation Lahore", "PWA Blood Bank", "Fatmid Foundation (bloodbank | haematogical)", "Husaini Blood Bank", "Rehman Welfare Blood Bank", "Husaini Blood Bank", "Husaini Blood Bank", "Al Minhaj ul Quran Blood Bank", "Minhaj Blood Bank Clinical Lab", "Pak Medical Centre", "Husaini Blood Bank", "South City Hospital", "Blood Bank and Liboratory"};
    String[] BankAddres = {"University Road", "16-Y-L", "880, Shadman-1", "109 Habitat Villas, Jail Road,shadman2", "Convoy Rd", "Shahrah Noor Jahan", "G-1/15", "Multan", "Dadan Shah Rd, Police Lines", "790-A, Q block, Model Town", "Peshawar Rd", "Eid Gah Road", "Faisalabad", "1/C, Shahrah-e-Jhangir, Block 10, FB Area", "72-A, Block# D-II, Johar Town", "Karachi", "Karachi", "Lady Dufferin Hospital Chand Bibi Rd", "Millat Rd, Gulistan Colony 2, Gulistan Colony", "Opp. Abbasi Shaheed Hospital Tabish Dehlavi Road", "Karachi", "Multan", "Nowshera Rd, Gujranwala", "Defence Road", "Shan Hospital", "Karachi", "Lahore"};
    String[] BankCity = {"Faisalabad", "Faisalabad", "Lahore", "Lahore", "Rawalpindi", "Karachi", "Karachi", "Multan", "Hyderabad", "Lahore", "Rawalpindi", "Sukkur", "Faisalabad", "Karachi", "Lahore", "Karachi", "Karachi", "Karachi", "Faisalabad", "Karachi", "Karachi", "Multan", "Gujrawala", "Sialkot", "Karachi", "Karachi", "Lahore"};
    String[] BankContacts = {"0412636329", "0418714848", "04237422131", "04237581067", "0515527565", "N/A", "02136609023", "0614554520", "0222620229", "03084402414", "03324331133", "0715612024", "0412409754", "02136365641", "04235210834", "02132735214", "02132225284", "N/A", "0418848663", "02137639502", "02132597772", "N/A", "0554442559", "03008713337", "02134834782", "03023817108", "N/A"};
    double[] Latitude = {31.420943, 31.417458, 31.534099, 31.536179, 33.5928, 24.959531, 24.919688, 30.192196, 25.395466, 31.473085, 33.598182, 27.6985, 31.4631, 24.9287, 31.457991, 24.859416, 24.881439, 24.858653, 31.445442, 24.91963, 24.90513, 30.204582, 32.154347, 32.495553, 24.919135, 24.8124, 31.480692};
    double[] Longitude = {73.084641, 73.122167, 74.331862, 74.339514, 73.0422, 67.049661, 67.037432, 71.503492, 68.364754, 74.334323, 73.044201, 68.8708, 73.0714, 67.0637, 74.282273, 67.010315, 67.033688, 67.007863, 73.096085, 67.028835, 66.967034, 71.450586, 74.178865, 74.501442, 67.095502, 67.0224, 74.342642};
    Context context;

    public BBAdapter(Context context) {
        this.context = context;
    }

    @Override
    public VH onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(context).inflate(R.layout.bloodbank_d, parent, false);
        VH vh = new VH(view);
        return vh;
    }

    @Override
    public void onBindViewHolder(VH holder, final int position) {
        holder.t1.setText(BankNames[position]);
        holder.t2.setText(BankCity[position]);
        holder.t3.setText(BankAddres[position]);
        holder.t4.setText(BankContacts[position]);
        holder.callBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (BankContacts[position].toString().trim().equals("N/A")) {
                    Toast.makeText(context, "Contact info not available.", Toast.LENGTH_SHORT).show();
                } else {
                    Intent intent = new Intent(Intent.ACTION_DIAL);
                    String User = "tel:" + BankContacts[position];
                    intent.setData(Uri.parse(User));
                    context.startActivity(intent);
                }

            }
        });
        holder.LocBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (Latitude[position] == 000) {
                    Toast.makeText(context, "Location not Available..", Toast.LENGTH_SHORT).show();
                } else {
                    Intent intent = new Intent(Intent.ACTION_VIEW, Uri.parse("geo:<" + Latitude[position] + ">,<" + Longitude[position] + ">?q=<" + Latitude[position] + ">,<" + Longitude[position] + ">(" + BankNames[position] + ")"));
                    context.startActivity(intent);
                }
            }
        });
    }

    @Override
    public int getItemCount() {
        return BankNames.length;
    }

    public class VH extends RecyclerView.ViewHolder {
        TextView t1, t2, t3, t4;
        Button callBtn, LocBtn;

        public VH(View itemView) {
            super(itemView);
            t1 = (TextView) itemView.findViewById(R.id.BBName);
            t2 = (TextView) itemView.findViewById(R.id.BBCity);
            t3 = (TextView) itemView.findViewById(R.id.BBAddress);
            t4 = (TextView) itemView.findViewById(R.id.BBContactNumber);
            callBtn = (Button) itemView.findViewById(R.id.BBCall_Btn);
            LocBtn = (Button) itemView.findViewById(R.id.BBLocation_Btn);
        }
    }
}
