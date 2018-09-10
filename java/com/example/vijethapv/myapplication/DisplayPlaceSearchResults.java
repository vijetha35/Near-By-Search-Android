package com.example.vijethapv.myapplication;

import android.graphics.Movie;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.DefaultItemAnimator;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.View;
import android.widget.Toast;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.DefaultItemAnimator;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.Toast;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Stack;

public class DisplayPlaceSearchResults extends AppCompatActivity {

    Bundle extras;

    private List<List<String>> placesList;
    private RecyclerView recyclerView;
    private PlacesSearchAdapter pAdapter;
    private Stack<String> prevResponseStk =  new Stack<>();
    public String prevJsonResp;
    Button pButton;
    Button nButton;
    public String nextPageToken;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.activity_display_place_search_results);

        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        toolbar.setTitle( "Search results");

        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        recyclerView = (RecyclerView) findViewById(R.id.my_recycler_view_srch_results);

        pButton = (Button) findViewById(R.id.prevBttn);
        nButton = (Button) findViewById(R.id.nxtBttn);

        pAdapter = new PlacesSearchAdapter(placesList,this);
        RecyclerView.LayoutManager mLayoutManager = new LinearLayoutManager(getApplicationContext());
        recyclerView.setLayoutManager(mLayoutManager);
        recyclerView.setItemAnimator(new DefaultItemAnimator());
        recyclerView.setAdapter(pAdapter);

        extras = getIntent().getExtras();
        final String jsonStr = extras.getString("jsonresponse");
        prevJsonResp = new String(jsonStr);
        constructPlacesData(jsonStr);

        pButton.setEnabled(false);

        nButton.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v){

                if(prevResponseStk.isEmpty())
                    prevResponseStk.push(new String(jsonStr));
                else
                    prevResponseStk.push(new String(prevJsonResp));
                String strUrl = "http://travelandentertainmentsearch-env.us-east-2.elasticbeanstalk.com/api/place/nearbysearch/json?pagetoken="+nextPageToken;
                getSearchResults(strUrl);
                pButton.setEnabled(true);
            }
        });
        pButton.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v){

                constructPlacesData(new String(prevResponseStk.pop()));
                if(prevResponseStk.isEmpty()) {
                    pButton.setEnabled(false);
                    //  prevJsonResp = new String(jsonStr);
                }
                // nextPageToken=new String("");
            }
        });
    }

    public void getSearchResults(String url)
    {
        RequestQueue queue = Volley.newRequestQueue(this);

// Request a string response from the provided URL.
        StringRequest stringRequest = new StringRequest(Request.Method.GET, url,
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                        constructPlacesData(response);
                        prevJsonResp = new String(response);
                    }
                }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                //Log.d("errornmnn","dvhsqdkvqdkwvj");
                Log.d("error :",error.toString());
            }
        });

// Add the request to the RequestQueue.
        queue.add(stringRequest);
    }


    private void constructPlacesData(String jsonStr) {

        Log.d("response",jsonStr);

        try {
            JSONObject jsonObj = new JSONObject(jsonStr);
            JSONArray plResults = jsonObj.getJSONArray("results");

            if(!jsonObj.has("next_page_token") || jsonObj.getString("next_page_token").isEmpty() )
            {
                nButton.setEnabled(false);
            }
            else
            {
                nextPageToken = jsonObj.getString("next_page_token");
                Log.d("next page token :",nextPageToken);
                nButton.setEnabled(true);
            }

            //if(plResults.htm)
            placesList = new ArrayList<>();
            // looping through All Contacts
            for (int i = 0; i < plResults.length(); i++) {
                JSONObject c = plResults.getJSONObject(i);
                String plId = c.getString("id");
                String plName = c.getString("name");
                String plAddr = c.getString("vicinity");
                String plIconUrl = c.getString("icon");


                List<String> plListItem = new ArrayList<>();
                plListItem.add(plId);
                plListItem.add(plName);
                plListItem.add(plAddr);
                // Log.d("vicinity ",plAddr);
                plListItem.add(plIconUrl);
                // adding each child node to HashMap key => value

                // adding contact to contact list
                placesList.add(plListItem);
            }
        } catch (final JSONException e)
        {
            Log.e("JSON Parsing Error :", "Json parsing error: " + e.getMessage());
            runOnUiThread(new Runnable() {
                @Override
                public void run() {
                    Toast.makeText(getApplicationContext(),
                            "Json parsing error: " + e.getMessage(),
                            Toast.LENGTH_LONG).show();
                }
            });

        }
        //pAdapter = new PlacesSearchAdapter(placesList,this);
        //RecyclerView.LayoutManager mLayoutManager = new LinearLayoutManager(getApplicationContext());
        //recyclerView.setLayoutManager(mLayoutManager);
        //recyclerView.setItemAnimator(new DefaultItemAnimator());
        //recyclerView.setAdapter(pAdapter);
        pAdapter.setPlacesDet(placesList,this);
        pAdapter.notifyDataSetChanged();
    }

}

