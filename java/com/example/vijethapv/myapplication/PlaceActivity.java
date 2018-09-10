package com.example.vijethapv.myapplication;

import android.graphics.Movie;
import android.support.design.widget.TabLayout;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.DefaultItemAnimator;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;

import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;
import android.support.v4.view.ViewPager;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;

import android.widget.TextView;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;

import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

public class PlaceActivity extends AppCompatActivity {
    public static final String EXTRA_MESSAGE = "com.example.myfirstapp.MESSAGE";
    private static final String TAG = "MainActivity";
    private TabLayout tabLayout;
    private SectionsPageAdapter mSectionsPageAdapter;

    private List<Movie> movieList = new ArrayList<>();
    private RecyclerView recyclerView;
    //private MoviesAdapter mAdapter;


    private ViewPager mViewPager;

    private int[] tabIcons = {
            R.drawable.info_outline,
            R.drawable.photos,
            R.drawable.maps,
            R.drawable.review
    };

    Bundle bundle = new Bundle();
    SectionsPageAdapter adapter = new SectionsPageAdapter(getSupportFragmentManager());

    //final TextView mTextView = (TextView) findViewById(R.id.textView3);





    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.place_details);
        //Toolbar toolbar = findViewById(R.id.toolbar);
        Log.d(TAG, "onCreate: Starting.");


        mSectionsPageAdapter = new SectionsPageAdapter(getSupportFragmentManager());

        // Set up the ViewPager with the sections adapter.
        mViewPager = (ViewPager) findViewById(R.id.container);
        setupViewPager(mViewPager);
        //setSupportActionBar(toolbar);

        //prepareMovieData();

        TabLayout tabLayout = (TabLayout) findViewById(R.id.tabs);
        tabLayout.setupWithViewPager(mViewPager);
        tabLayout.getTabAt(0).setIcon(R.drawable.info_outline);
        tabLayout.getTabAt(1).setIcon(R.drawable.photos);
        tabLayout.getTabAt(2).setIcon(R.drawable.maps);
        tabLayout.getTabAt(3).setIcon(R.drawable.review);
        //setupTabIcons();
        //Log.d(tabLayout.getTabCount());

        final TextView mTextView = (TextView) findViewById(R.id.textView5);
// ...

// Instantiate the RequestQueue.
        RequestQueue queue = Volley.newRequestQueue(this);
        String placeID = "ChIJOaegwbTHwoARg7zN_9nq5Uc";
        //usc: ChIJ7aVxnOTHwoARxKIntFtakKo Dominos: ChIJOaegwbTHwoARg7zN_9nq5Uc
        String url ="https://maps.googleapis.com/maps/api/place/details/json?placeid="+placeID+"&key=AIzaSyAAPdwltp5ESHLjlL39b0cB1fwPknX8FB0";
//        String url ="http://webtechschow3-env.us-east-2.elasticbeanstalk.com/search-nearby/?lat=34.0266&lon=-118.2831&mile=10&type=default&keyword=USC";

// Request a string response from the provided URL.
        final Fragment fragobj = adapter.getFragment("INFO");
        final Fragment fragobj4 = adapter.getFragment("Reviews");
        StringRequest stringRequest = new StringRequest(Request.Method.GET, url,
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                        // Display the first 500 characters of the response string.
                        try {
                            Log.i("before", "before JSON object");
                            JSONObject json = new JSONObject(response);
                            String name = new JSONObject(json.get("result").toString()).get("name").toString();
                            String address = new JSONObject(json.get("result").toString()).get("formatted_address").toString();
                            String phone = new JSONObject(json.get("result").toString()).get("formatted_phone_number").toString();
                            String website = new JSONObject(json.get("result").toString()).get("website").toString();
                            String price_level = new JSONObject(json.get("result").toString()).get("price_level").toString();
                            String google_url = new JSONObject(json.get("result").toString()).get("url").toString();
                            String rating = new JSONObject(json.get("result").toString()).get("rating").toString();
                            String reviews = new JSONObject(json.get("result").toString()).get("reviews").toString();
                            String yelp_components = new JSONObject(json.get("result").toString()).get("address_components").toString();
                            //Log.i("after", "after JSON Object");
                            //Log.i("SHRAVYA", obj.getString("next_page_token"));
                            bundle.putString("name", name);
                            bundle.putString("address", address);
                            bundle.putString("phone", phone);
                            bundle.putString("website", website);
                            bundle.putString("price_level", price_level);
                            bundle.putString("google_url", google_url);
                            bundle.putString("rating", rating);
                            bundle.putString("reviews", reviews);
                            bundle.putString("yelp_components", yelp_components);
                            Log.d("bundle", reviews);
//                            bundle.putO("reviews", reviews);
                            fragobj.setArguments(bundle);
                            fragobj4.setArguments(bundle);
                        } catch (Exception e) {
                            Log.i("SUNJANA", "Steven wins");
                        }

//                        mTextView.setText("Response is: "+ response.substring(0,500));
                        //bundle.putString("key", "response is: ");
                    }
                }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                //mTextView.setText("That didn't work!");
                bundle.putString("key", "that didn't work");
            }
        });
        queue.add(stringRequest);



//        fragobj.setArguments(bundle);

// Add the request to the RequestQueue.
    }



    private void setupViewPager(ViewPager viewPager) {

        adapter.addFragment(new Tab1Fragment(), "INFO");
        adapter.addFragment(new Tab2Fragment(), "PHOTO");
        adapter.addFragment(new Tab3Fragment(), "MAP");
        adapter.addFragment(new Tab4Fragment(), "Reviews");
        viewPager.setAdapter(adapter);
    }

}