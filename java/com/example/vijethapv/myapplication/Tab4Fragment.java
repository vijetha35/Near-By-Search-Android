package com.example.vijethapv.myapplication;

import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v7.widget.DefaultItemAnimator;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.Spinner;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;

import org.json.JSONArray;
import org.json.JSONObject;

import java.net.URLEncoder;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.Date;
import java.util.List;

import static android.view.View.VISIBLE;

public class Tab4Fragment extends android.support.v4.app.Fragment {
    private static final String TAG = "Tab4Fragment";
    RecyclerView mRecyclerView;
    //protected LayoutManagerType mCurrentLayoutManagerType;
    protected RecyclerView.LayoutManager mLayoutManager;
    public List<Review> yelpReviewsList =new ArrayList<Review>();
    public ReviewsAdapter rva;
    //true = google , false = yelp
    public boolean selectedReview = true;
    public String sortOrderVal = "Default Order";

    private Button btnTest;
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        // Initialize dataset, this data would usually come from a local content provider or
        // remote server.
        //initDataset();
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        final List<Review> googleReviewsList =new ArrayList<Review>();

        View view = inflater.inflate(R.layout.fragment_tabbed4, container, false);
        String reviewInput;
        String addressComponents;
        String address1= "", city = "", state = "", country = "";

        if(getArguments() != null){
            reviewInput = getArguments().getString("reviews");
            addressComponents = getArguments().getString("yelp_components");
            Log.d("reviewInput", reviewInput);
            try {
                JSONArray google_reviews_array = new JSONArray(reviewInput);
                Log.d("google_review_length", String.valueOf(google_reviews_array.length()));
                for (int i = 0; i < google_reviews_array.length(); i++){
                    Review rev = new Review();
                    JSONObject jb = (JSONObject) google_reviews_array.get(i);
                    rev.setText(jb.getString("text"));
                    rev.setRating(jb.getString("rating"));
                    //rev.setRating(String.valueOf(5));
                    rev.setName(jb.getString("author_name"));
                    rev.setPhoto_url(jb.getString("profile_photo_url"));
                    //rev.setUnix_time(Long.parseLong(jb.getString("time")));
                    rev.setUser_url(jb.getString("author_url"));
                    String time_to_convert = jb.getString("time");
                    Long dv = Long.valueOf(time_to_convert) * 1000;
                    Date df = new Date(dv);
                    String time_input = new SimpleDateFormat("yyyy-MM-dd kk:mm:ss").format(df);
                    rev.setTime_created(time_input);
                    googleReviewsList.add(rev);
                }
                Log.d("reviewList",new JSONArray(reviewInput).toString());
                Log.d("yelpComponents", getArguments().getString("yelp_components").toString());

                JSONArray address_components = new JSONArray(addressComponents);
                for (int i = 0; i < address_components.length(); i++){
                    Log.d("addressComp", address_components.get(i).toString());
                    JSONObject jb = (JSONObject) address_components.get(i);
                    JSONArray jbInner = (JSONArray) jb.get("types");
                    if(jbInner.get(0).equals("route")){
                        address1 = jb.get("long_name").toString();
                    }
                    else if(jbInner.get(0).equals("locality")){
                        city = jb.get("long_name").toString();
                    }

                    else if(jbInner.get(0).equals("administrative_area_level_1")){
                        state = jb.get("short_name").toString();
                    }
                    else if(jbInner.get(0).equals("country")){
                        country = jb.get("short_name").toString();
                    }
                }
                final RequestQueue queue = Volley.newRequestQueue(getActivity().getApplicationContext());
                String placeName = getArguments().getString("name");
                String yelpUrl = "http://travelandentertainmentsearch-env.us-east-2.elasticbeanstalk.com/yelpname/name="+ URLEncoder.encode(placeName)+"&city="+URLEncoder.encode(city)+"&country="+country+"&state="+state+"&address1="+URLEncoder.encode(address1);
                //String encodedUrl = URLEncoder.encode(yelpUrl);
                //String yelpID = "";
                StringRequest stringRequest = new StringRequest(Request.Method.GET, yelpUrl,
                        new Response.Listener<String>() {
                            @Override
                            public void onResponse(String response) {
                                // Display the first 500 characters of the response string.
                                try {
                                    JSONObject json = new JSONObject(response);
                                    JSONArray businessArr = (JSONArray) json.get("businesses");
                                    String yelpID = "";
                                    if (businessArr.length() > 0 ){
                                        JSONObject busJson = new JSONObject(businessArr.get(0).toString());
                                        yelpID = busJson.getString("id").toString();

                                        String yelpSecondUrl = "http://travelandentertainmentsearch-env.us-east-2.elasticbeanstalk.com/businesses/"+yelpID+"/reviews";
                                        StringRequest yelpSecondRequest = new StringRequest(Request.Method.GET, yelpSecondUrl,
                                                new Response.Listener<String>() {
                                                    @Override
                                                    public void onResponse(String response) {
                                                        // Display the first 500 characters of the response string.
                                                        try {
                                                            JSONObject yelpJson = new JSONObject(response);
                                                            JSONArray yelpArray = (JSONArray) yelpJson.get("reviews");
                                                            for (int i = 0; i < yelpArray.length(); i++){
                                                                Review rev = new Review();
                                                                JSONObject jb = (JSONObject) yelpArray.get(i);
                                                                rev.setText(jb.getString("text"));
                                                                rev.setRating(jb.getString("rating"));
                                                                rev.setUser_url(jb.getString("url"));
                                                                JSONObject userObj = new JSONObject(jb.get("user").toString());
                                                                rev.setName(userObj.getString("name"));
                                                                rev.setPhoto_url(userObj.getString("image_url"));
                                                                rev.setTime_created(jb.getString("time_created"));

//                                                                Log.d("singleReview", rev.toString());
                                                                yelpReviewsList.add(rev);
                                                            }

                                                            Log.d("yelpReviewsList", yelpReviewsList.get(0).getName().toString());

                                                        } catch (Exception e) {
                                                            Log.i("SUNJANA", "Shravya Loves wins");
                                                        }

//                        mTextView.setText("Response is: "+ response.substring(0,500));
                                                        //bundle.putString("key", "response is: ");
                                                    }
                                                }, new Response.ErrorListener() {
                                            @Override
                                            public void onErrorResponse(VolleyError error) {
                                                //mTextView.setText("That didn't work!");
                                                //bundle.putString("key", "that didn't work");
                                            }
                                        });

                                        queue.add(yelpSecondRequest);

                                    }
//                                    String name = new JSONObject(json.get("result").toString()).get("name").toString();
                                }catch (Exception e) {
                                    Log.i("SUNJANA", "VJ LOVES STEVEN");
                                }

//                        mTextView.setText("Response is: "+ response.substring(0,500));
                                //bundle.putString("key", "response is: ");
                            }
                        }, new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {
                        //mTextView.setText("That didn't work!");
                        //bundle.putString("key", "that didn't work");
                    }
                });

                queue.add(stringRequest);
            } catch (Exception e){

            }

        }

        view.setTag(TAG);
        mRecyclerView = (RecyclerView) view.findViewById(R.id.my_recycler_view);
        mLayoutManager = new LinearLayoutManager(getActivity());
        mRecyclerView.setLayoutManager(mLayoutManager);
        mRecyclerView.setItemAnimator(new DefaultItemAnimator());
//        getArguments();



        Log.d("hello there", String.valueOf(yelpReviewsList.size()));
        rva = new ReviewsAdapter(googleReviewsList);
        mRecyclerView.setAdapter(rva);
//        mLayoutManager = new LinearLayoutManager(getActivity());
//        setRecyclerViewLayoutManager(LinearLayoutManager);


        //get the spinner from the xml.
        final Spinner google_yelp = view.findViewById(R.id.spinner_google_yelp);
        google_yelp.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener(){
            @Override
            public void onItemSelected(AdapterView<?> parentView, View selectedItemView, int position, long id) {
                // your code here
                String imc_met= google_yelp.getSelectedItem().toString();
                List<Review> revList ;
                if(imc_met.equalsIgnoreCase("Yelp Reviews")){
                    selectedReview = false;
                    revList =  new ArrayList<>(yelpReviewsList);
                    if(yelpReviewsList.isEmpty()){
                        getView().findViewById(R.id.emptyReviewsAlert).setVisibility(VISIBLE);
                    }
                    else{
                        getView().findViewById(R.id.emptyReviewsAlert).setVisibility(View.INVISIBLE);
                    }
//                    rva = new ReviewsAdapter(yelpReviewsList);
//                    mRecyclerView.setAdapter(rva);
//                    rva.notifyDataSetChanged();

                } else {
                    selectedReview = true;
                    revList =  new ArrayList<>(googleReviewsList);
                    if(googleReviewsList.isEmpty()){
                        getView().findViewById(R.id.emptyReviewsAlert).setVisibility(VISIBLE);
                    }
                    else{
                        getView().findViewById(R.id.emptyReviewsAlert).setVisibility(View.INVISIBLE);
                    }
//                    rva = new ReviewsAdapter(googleReviewsList);
//                    Log.d("google reviews ", googleReviewsList.toString());
//                    mRecyclerView.setAdapter(rva);
//                    rva.notifyDataSetChanged();
                }

                setOrder(revList);
                Log.d("google or yelp ",imc_met);
            }

            @Override
            public void onNothingSelected(AdapterView<?> arg0) {
                // TODO Auto-generated method stub

            }
        });




//create a list of items for the spinner.
//        String[] items = new String[]{"1", "2", "three"};
//create an adapter to describe how the items are displayed, adapters are used in several places in android.
//There are multiple variations of this, but this is the basic variant.

        ArrayAdapter adapter = ArrayAdapter.createFromResource(this.getActivity(), R.array.review_type, android.R.layout.simple_spinner_item);
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
//set the spinners adapter to the previously created one.
        google_yelp.setAdapter(adapter);

        final Spinner filter_type = view.findViewById(R.id.spinner_filter);
        filter_type.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener(){
            @Override
            public void onItemSelected(AdapterView<?> parentView, View selectedItemView, int position, long id) {
                // your code here
                String imc_met= filter_type.getSelectedItem().toString();
                List<Review> revList = new ArrayList<>();

                if(selectedReview == true){
                    revList = new ArrayList<>(googleReviewsList);
                } else {
                    revList = new ArrayList<>(yelpReviewsList);
                }

                sortOrderVal = imc_met;
                setOrder(revList);

//                if (revList.size() == 0){
//                    Log.d("empty", getView().toString());
//                    getView().findViewById(R.id.emptyReviewsAlert).setVisibility(View.VISIBLE);
//                }
//                else{
//                    getView().findViewById(R.id.emptyReviewsAlert).setVisibility(View.INVISIBLE);
//                }

                Log.d("sorting order ",imc_met);
            }

            @Override
            public void onNothingSelected(AdapterView<?> arg0) {
                // TODO Auto-generated method stub

            }
        });



        adapter = ArrayAdapter.createFromResource(this.getActivity(), R.array.filter_type, android.R.layout.simple_spinner_item);
        filter_type.setAdapter(adapter);


        return view;
    }

    public void setOrder(List<Review> revList) {
        if(sortOrderVal.equalsIgnoreCase("Default Order")){
            Log.d("revListLength", String.valueOf(revList.size()));
            rva = new ReviewsAdapter(revList);
            mRecyclerView.setAdapter(rva);
            rva.notifyDataSetChanged();

        }
        else if(sortOrderVal.equalsIgnoreCase("Most Recent")){
            Collections.sort(revList, new Comparator<Review>(){
                public int compare(Review r1, Review r2){
                    return (r1.getTime_created().compareTo(r2.getTime_created()));
                }
            });
            Collections.reverse(revList);
            rva = new ReviewsAdapter(revList);
            mRecyclerView.setAdapter(rva);
            rva.notifyDataSetChanged();
        }

        else if(sortOrderVal.equalsIgnoreCase("Least Recent")) {
            Collections.sort(revList, new Comparator<Review>(){
                public int compare(Review r1, Review r2){
                    return r1.getTime_created().compareTo(r2.getTime_created());
                }
            });
            rva = new ReviewsAdapter(revList);
            mRecyclerView.setAdapter(rva);
            rva.notifyDataSetChanged();
        }

        else if(sortOrderVal.equalsIgnoreCase("Highest Rated")){
            Collections.sort(revList, new Comparator<Review>(){
                public int compare(Review r1, Review r2){
                    return r1.getRating().compareTo(r2.getRating());
                }
            });
            Collections.reverse(revList);
            rva = new ReviewsAdapter(revList);
            mRecyclerView.setAdapter(rva);
            rva.notifyDataSetChanged();
        }

        else if(sortOrderVal.equalsIgnoreCase("Lowest Rated")){
            Collections.sort(revList, new Comparator<Review>(){
                public int compare(Review r1, Review r2){
                    return r1.getRating().compareTo(r2.getRating());
                }
            });
            rva = new ReviewsAdapter(revList);
            mRecyclerView.setAdapter(rva);
            rva.notifyDataSetChanged();
        }
    }

}
