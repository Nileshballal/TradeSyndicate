package com.syndicate.lead.fragment;


import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.os.AsyncTask;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.syndicate.lead.R;
import com.syndicate.lead.activity.MainActivity;
import com.syndicate.lead.adapter.LiveLeadAdapter;
import com.syndicate.lead.model.Leads;
import com.syndicate.lead.model.Utility;

import org.json.JSONArray;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.ArrayList;

import static com.syndicate.lead.fragment.LeadsFragmentA.USERINFO;

import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

public class LiveLeadsFragment extends Fragment {

    RecyclerView recyclerView;
    RecyclerView.LayoutManager layoutManager;
    String Token;
    Utility utility;
    SharedPreferences userpreferences;
    private ProgressDialog progressDialog;
    LiveLeadAdapter myLeadAdapter;
    ArrayList<Leads>leadsArrayList;
    TextView txt_record;

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View rootView = inflater.inflate(
                R.layout.list_lay, container, false);
        userpreferences = getActivity().getSharedPreferences(USERINFO,
                Context.MODE_PRIVATE);
        utility=new Utility();
        recyclerView = (RecyclerView) rootView.findViewById(R.id.recycleViewContainer);
        recyclerView.setHasFixedSize(true);
        leadsArrayList=new ArrayList<>();
        layoutManager = new LinearLayoutManager(getActivity());
        txt_record = (TextView) rootView.findViewById(R.id.txt_record);
        recyclerView.setLayoutManager(layoutManager);
        myLeadAdapter = new LiveLeadAdapter(getActivity(), leadsArrayList);
        recyclerView.setAdapter(myLeadAdapter);
        progressDialog=new ProgressDialog(getActivity());
        Token=userpreferences.getString("Token","");

        if (utility.isNet(getActivity())){
            GetLeadsShowData getLeadsShowData=new GetLeadsShowData();
            getLeadsShowData.execute();
        }



        return rootView;




    }


    public class GetLeadsShowData extends AsyncTask<String, Void, String> {

        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            progressDialog.setCancelable(false);
            progressDialog.show();
            progressDialog.setContentView(R.layout.progreass_lay);
            progressDialog.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));

        }

        @Override
        protected String doInBackground(String... params) {



            try {


                String url_string="";

                url_string =getResources().getString(R.string.baseurl)+"/all-requisitions";



                URL mUrl = new URL(url_string);
                HttpURLConnection httpConnection = (HttpURLConnection) mUrl.openConnection();
                httpConnection.setRequestMethod("GET");
                Token=userpreferences.getString("Token","");
                httpConnection.setRequestProperty("Authorization", "Bearer"+" "+Token);
                httpConnection.setConnectTimeout(100000);
                httpConnection.setReadTimeout(100000);
                httpConnection.connect();

                int responseCode = httpConnection.getResponseCode();

                if (responseCode == HttpURLConnection.HTTP_OK) {
                    BufferedReader br = new BufferedReader(new InputStreamReader(httpConnection.getInputStream()));
                    StringBuilder sb = new StringBuilder();
                    String line;
                    while ((line = br.readLine()) != null) {
                        sb.append(line + "\n");
                    }
                    br.close();
                    return sb.toString();
                }
            } catch (IOException e) {
                e.printStackTrace();
            } catch (Exception ex) {
                ex.printStackTrace();
            }
            return null;
        }
        @Override
        protected void onPostExecute(String s) {
            progressDialog.dismiss();
            try {
                JSONArray root = new JSONArray(s);
                leadsArrayList.clear();
                for (int i=0;i<root.length();i++){
                    Leads leads=new Leads();

                    JSONObject jsonObject=root.getJSONObject(i);
                    String id=jsonObject.getString("id");
                    leads.setId(id);
                    String items_master_id=jsonObject.getString("items_master_id");
                    leads.setItems_master_id(items_master_id);
                    String item_description=jsonObject.getString("item_description");
                    leads.setItem_description(item_description);
                    String item_best_price=jsonObject.getString("item_best_price");
                    leads.setItem_best_price(item_best_price);
                    String  remarks=jsonObject.getString("remarks");
                    leads.setRemarks(remarks);
                    String required_on=jsonObject.getString("required_on");
                    leads.setRequired_on(required_on);
                    leads.setQuantity(jsonObject.getString("qty"));
                    leads.setReference_requisition_no(jsonObject.getString("reference_requisition_no"));

                    JSONObject json=jsonObject.getJSONObject("user");
                    String User=json.getString("id");
                    leads.setUser("PSUSR"+User);
                    leadsArrayList.add(leads);
                }



                if (leadsArrayList.size()>0){
                    myLeadAdapter.updateList(leadsArrayList);
                }else {
                    txt_record.setVisibility(View.VISIBLE);
                }

                //  mAdapter.notifyDataSetChanged();



            } catch (Exception e) {
                SharedPreferences.Editor editor = userpreferences.edit();
                editor.remove("Token");
                editor.remove("Type");
                editor.remove("Login");
                editor.remove("Role");
                editor.remove("code");
                editor.commit();
                startActivity(new Intent(getActivity(), MainActivity.class).setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP));
                getActivity().finish();
                progressDialog.dismiss();



                //   progressDialog.dismiss();
            }
        }
    }


    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);


    }
}