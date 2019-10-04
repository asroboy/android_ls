package com.lapaksembako.app;

import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.MenuItem;
import android.view.View;
import android.widget.ExpandableListAdapter;
import android.widget.ExpandableListView;
import android.widget.Toast;

import com.lapaksembako.app.adapter.CustomExpandableListAdapter;
import com.lapaksembako.app.api.ApiClient;
import com.lapaksembako.app.api.ApiInterface;
import com.lapaksembako.app.api_model.GetFaq;
import com.lapaksembako.app.helper.Common;
import com.lapaksembako.app.model.Faq;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class FaqActivity extends AppCompatActivity {


    ExpandableListView expandableListView;
    ExpandableListAdapter expandableListAdapter;
    List<String> expandableListTitle;
    HashMap<String, List<String>> expandableListDetail;
    ApiInterface apiInterface;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_faq);

        ActionBar ab = getSupportActionBar();
        ab.setDisplayHomeAsUpEnabled(true);
        ab.setHomeAsUpIndicator(R.drawable.ic_back_white);
        apiInterface = ApiClient.getClient().create(ApiInterface.class);
        expandableListView = findViewById(R.id.expandableListView);


        expandableListView.setOnGroupExpandListener(new ExpandableListView.OnGroupExpandListener() {

            @Override
            public void onGroupExpand(int groupPosition) {
//                Toast.makeText(getApplicationContext(),
//                        expandableListTitle.get(groupPosition) + " List Expanded.",
//                        Toast.LENGTH_SHORT).show();
            }
        });

        expandableListView.setOnGroupCollapseListener(new ExpandableListView.OnGroupCollapseListener() {

            @Override
            public void onGroupCollapse(int groupPosition) {
//                Toast.makeText(getApplicationContext(),
//                        expandableListTitle.get(groupPosition) + " List Collapsed.",
//                        Toast.LENGTH_SHORT).show();

            }
        });

        expandableListView.setOnChildClickListener(new ExpandableListView.OnChildClickListener() {
            @Override
            public boolean onChildClick(ExpandableListView parent, View v,
                                        int groupPosition, int childPosition, long id) {
//                Toast.makeText(
//                        getApplicationContext(),
//                        expandableListTitle.get(groupPosition)
//                                + " -> "
//                                + expandableListDetail.get(
//                                expandableListTitle.get(groupPosition)).get(
//                                childPosition), Toast.LENGTH_SHORT
//                ).show();
                return false;
            }
        });

        getData();
    }


    public void getData() {
        Call<GetFaq> faq = apiInterface.getFaq();
        final ArrayList<Faq> faqs = new ArrayList<>();
        faq.enqueue(new Callback<GetFaq>() {
            @Override
            public void onResponse(Call<GetFaq> call, Response<GetFaq> response) {
                Log.d(Common.TAG, "FAQ Status : " + response.body().getStatus() + " data : " + response.body().getFaqs().size());
                if (response.isSuccessful()) {
                    Log.d(Common.TAG, "FAQ Content Size : " + faqs.size());
                    expandableListDetail = new HashMap<String, List<String>>();

                    for (int i = 0; i < response.body().getFaqs().size(); i++) {
                        faqs.add(response.body().getFaqs().get(i));
                        Faq faq = response.body().getFaqs().get(i);

                        Log.d(Common.TAG, "FAQ Content title : " + faq.getQuestion());
                        List<String> cricket = new ArrayList<String>();
                        cricket.add(faq.getAnswer());
                        expandableListDetail.put(faq.getQuestion(), cricket);
                    }

                    expandableListTitle = new ArrayList<String>(expandableListDetail.keySet());
                    Log.d(Common.TAG, "FAQ Title Size: " + expandableListTitle.size());
                    expandableListAdapter = new CustomExpandableListAdapter(getApplicationContext(), expandableListTitle, expandableListDetail);
                    expandableListView.setAdapter(expandableListAdapter);
                }
            }

            @Override
            public void onFailure(Call<GetFaq> call, Throwable t) {
                Log.d(Common.TAG, "FAQ Failure : " + t.getMessage());
            }
        });
    }


    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int id = item.getItemId();

        if (id == android.R.id.home) {
            finish();
        }
        return super.onOptionsItemSelected(item);

    }
}
