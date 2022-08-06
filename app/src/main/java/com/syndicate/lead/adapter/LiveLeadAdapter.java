package com.syndicate.lead.adapter;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import android.widget.Toast;

import androidx.cardview.widget.CardView;
import androidx.recyclerview.widget.RecyclerView;

import com.syndicate.lead.R;
import com.syndicate.lead.activity.EnquiryActivity;
import com.syndicate.lead.model.Leads;

import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;


public class LiveLeadAdapter extends RecyclerView.Adapter<LiveLeadAdapter.ViewHolder> {

    private  String code="";
    private Context context;
    private List<Leads> leadsList;
    public static String USERINFO="USER";
    String DonorUserid;
    SharedPreferences userpreferences;
    private ViewHolder viewHolder;

    public LiveLeadAdapter(Context context, ArrayList<Leads>leadsList) {
        this.context = context;
        this.leadsList = leadsList;
        userpreferences = context.getSharedPreferences(USERINFO,
                Context.MODE_PRIVATE);
        code=userpreferences.getString("code","");
    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View v = LayoutInflater.from(parent.getContext()).inflate(R.layout.liveleads_item_lay, parent, false);
        viewHolder = new ViewHolder(v);
        return viewHolder;
    }

    @Override
    public void onBindViewHolder(ViewHolder holder, final int position) {
        holder.itemView.setTag(leadsList.get(position));
        viewHolder=holder;
         final Leads leads = leadsList.get(position);
         holder.txt_budget.setText("Budget : " + leads.getItem_best_price());
         holder.txt_desc.setText(leads.getRemarks());
         holder.txt_item_desc.setText(leads.getItem_description());
         holder.txt_usercode.setText(leads.getUser());
         holder.txt_qty.setText("Qty : " + leads.getQuantity());
         holder.txt_enq.setText("Enquiry No : " + leads.getReference_requisition_no());

            DateFormat inputFormat = new SimpleDateFormat("yyyy-MM-dd hh:mm:ss");
            DateFormat outputFormat = new SimpleDateFormat("dd MMM");
            String Endate = leads.getRequired_on();

            Date date = null;
            try {
                date = inputFormat.parse(Endate);
                String outputDateStr = outputFormat.format(date);
                holder.txt_date.setText("Valid : "+ outputDateStr);
            } catch (ParseException e) {
                e.printStackTrace();
            }


            holder.card_digital.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    if (leadsList.get(position).getUser().equals(code)) {
                        Toast.makeText(context,"You cant bid your self requisition",Toast.LENGTH_LONG).show();
                    } else {
                        context.startActivity(new Intent(context,
                                EnquiryActivity.class).
                                putExtra("requisitions_id",
                                        leadsList.get(position).getId()).setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP));
                    }
                }
            });



    }

    public void updateList(List<Leads> categoryList) {
        this.leadsList=categoryList;
        notifyDataSetChanged();


    }
    @Override
    public int getItemCount() {

        return leadsList.size();
    }



    public class ViewHolder extends RecyclerView.ViewHolder{

        public TextView txt_budget,txt_date,txt_qty,txt_desc,txt_item_desc,txt_enq,txt_usercode;
        CardView card_digital;
        View view_1;



        public ViewHolder(View itemView) {
            super(itemView);

            txt_budget = (TextView) itemView.findViewById(R.id.txt_budget);
            txt_date = (TextView) itemView.findViewById(R.id.txt_date);
            txt_qty = (TextView) itemView.findViewById(R.id.txt_qty);
            txt_desc = (TextView) itemView.findViewById(R.id.txt_desc);
            txt_item_desc = (TextView) itemView.findViewById(R.id.txt_item_desc);
            txt_enq = (TextView) itemView.findViewById(R.id.txt_enq);
            txt_usercode = (TextView) itemView.findViewById(R.id.txt_usercode);
            card_digital = (CardView) itemView.findViewById(R.id.card_digital);






        }
    }

}