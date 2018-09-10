package com.example.vijethapv.myapplication;

import android.app.Fragment;
import android.media.Rating;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.RatingBar;
import android.widget.TableLayout;
import android.widget.TableRow;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.RequestQueue;
import com.android.volley.toolbox.Volley;

import org.w3c.dom.Text;

public class Tab1Fragment extends android.support.v4.app.Fragment {
    private static final String TAG = "Tab1Fragment";
    RequestQueue queue;
    String url;


    private Button btnTest1;

    public Tab1Fragment() {
        //queue = Volley.newRequestQueue(getActivity());
        //url = "http://www.google.com";
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        View view = inflater.inflate(R.layout.fragment_tabbed, container, false);
        String message = "University of Southern California";
        //TextView textView = view.findViewById(R.id.textTab1);
        //textView.setText(message);
        String name = "";
        String address="";
        String rating = "";
        String website = "";
        String google_url = "";
        String phone = "";
        String price_level = "";
        if(getArguments() != null){
             name = getArguments().getString("name");
            address = getArguments().getString("address");
            rating = getArguments().getString("rating");
            website = getArguments().getString("website");
            google_url = getArguments().getString("google_url");
            phone = getArguments().getString("phone");
            price_level = getArguments().getString("price_level");
        }
        TableRow tr_address = view.findViewById(R.id.address_row);
        TableRow tr_rating= view.findViewById(R.id.rating_row);
        TableRow tr_website = view.findViewById(R.id.website_row);
        TableRow tr_google_url = view.findViewById(R.id.google_page_row);
        TableRow tr_phone = view.findViewById(R.id.phone_row);
        TableRow tr_price_level = view.findViewById(R.id.price_level_row);

        if(!address.isEmpty()){
            TextView addressTextView = (TextView) view.findViewById(R.id.address_text);
            addressTextView.setText(address);
            tr_address.setVisibility(View.VISIBLE);
        }
        else{
            tr_address.setVisibility(View.GONE);
        }
        if(!rating.isEmpty()){
            RatingBar ratingTextView = (RatingBar) view.findViewById(R.id.rating_text);
            ratingTextView.setNumStars(5);
            ratingTextView.setRating(Float.parseFloat(rating));
            Log.i("max", String.valueOf( ratingTextView.getNumStars()));
            tr_rating.setVisibility(View.VISIBLE);
        }
        else{
            tr_rating.setVisibility(View.GONE);
        }
        if(!price_level.isEmpty()){
            String dollar_count = "";
            for (int i = 0; i < Integer.parseInt(price_level); i++){
                dollar_count += "$";
            }
            TextView textView = (TextView) view.findViewById(R.id.price_level_text);
            textView.setText(dollar_count);
            tr_price_level.setVisibility(View.VISIBLE);
        }
        else{
            tr_price_level.setVisibility(View.GONE);
        }
        if(!phone.isEmpty()){
            TextView textView = view.findViewById(R.id.phone_text);
            textView.setText(phone);
            tr_phone.setVisibility(View.VISIBLE);
        }
        else{
            tr_phone.setVisibility(View.GONE);
        }
        if(!google_url.isEmpty()){
            TextView textView = view.findViewById(R.id.google_page_text);
            textView.setText(google_url);
            tr_google_url.setVisibility(View.VISIBLE);
        }
        else{
            tr_google_url.setVisibility(View.GONE);
        }
        if(!website.isEmpty()){
            TextView textView = view.findViewById(R.id.website_text);
            textView.setText(website);
            tr_website.setVisibility(View.VISIBLE);
        }
        else{
            tr_website.setVisibility(View.GONE);
        }

        //textView.setText(strtext);

        Log.i("text", "value");



        return view;
    }
}
