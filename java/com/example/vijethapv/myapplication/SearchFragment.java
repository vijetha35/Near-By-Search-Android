package com.example.vijethapv.myapplication;


import android.Manifest;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.Color;
import android.location.Location;

import android.location.LocationManager;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.ActivityCompat;
import android.support.v4.app.Fragment;
import android.support.v4.content.ContextCompat;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.AutoCompleteTextView;
import android.widget.Button;
import android.widget.EditText;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;


import com.android.volley.Request;
import com.android.volley.Request.Method;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.google.android.gms.common.api.GoogleApiClient;
import com.google.android.gms.maps.GoogleMap;
import com.google.gson.Gson;
import com.google.gson.JsonArray;
import com.google.gson.JsonObject;


import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.UnsupportedEncodingException;

import java.net.URLEncoder;
import java.util.ArrayList;
import java.util.List;


public class SearchFragment extends Fragment {


    AutoCompleteTextView auto_text_complete;
    private String  custom_location_name;
    boolean custom_location=false;
    String autoCompleteUrl = "https://maps.googleapis.com/maps/api/place/autocomplete/json?";
    String key = "AIzaSyBlpAgDCYR09sI3v4op8V9EBeOx4ABW4X0";
    Double longitude, latitude;
    public Double custom_latitude, custom_longitude;

    public String getCustom_location_name() {
        return custom_location_name;
    }

    public void setCustom_location_name(String custom_location_name) {
        this.custom_location_name = custom_location_name;
    }

    public Double getLongitude() {
        return longitude;
    }

    public void setLongitude(Double longitude) {
        this.longitude = longitude;
    }

    public Double getLatitude() {
        return latitude;
    }

    public void setLatitude(Double latitude) {
        this.latitude = latitude;
    }

    public Double getCustom_latitude() {
        return custom_latitude;
    }

    public void setCustom_latitude(Double custom_latitude) {
        this.custom_latitude = custom_latitude;
    }

    public Double getCustom_longitude() {
        return custom_longitude;
    }

    public void setCustom_longitude(Double custom_longitude) {
        this.custom_longitude = custom_longitude;
    }

    private static final int MY_PERMISSIONS_FINE_LOCATION = 1;

    public SearchFragment() {

    }
    private OnFragmentInteractionListener mListener;
    Button buttonSrch,buttonClr;
    private EditText keywordInput;
    private Spinner categOption;
    private EditText locInput, distanceEntered ;
    private TextView errTextKWord;
    private TextView errTextLoc;
    private RadioButton otherLocRadio;
    private String urlSearchPlaces;
    private Double finalDistanceText;


    private static final String TAG_RESULT = "predictions";

    ArrayAdapter<String> adapter;

    private static final String TAG = "ErorrLogVj";
    ArrayList<String> auto_complete_names;
    private GoogleMap mMap;

    private GoogleApiClient mGoogleApiClient;



    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {

        super.onCreate(savedInstanceState);

        if (ContextCompat.checkSelfPermission(getActivity(), android.Manifest.permission.ACCESS_FINE_LOCATION)
                != PackageManager.PERMISSION_GRANTED) {
            ActivityCompat.requestPermissions(getActivity(),
                    new String[]{Manifest.permission.ACCESS_FINE_LOCATION},
                    MY_PERMISSIONS_FINE_LOCATION);

        } else {
            LocationManager lm = (LocationManager) getActivity().getSystemService(Context.LOCATION_SERVICE);
//            Location location = lm.getLastKnownLocation(LocationManager.GPS_PROVIDER);

            List<String> providers = lm.getProviders(true);
            Location bestLocation = null;
            for (String provider : providers) {
                Location l = lm.getLastKnownLocation(provider);
                if (l == null) {
                    continue;
                }
                if (bestLocation == null || l.getAccuracy() < bestLocation.getAccuracy()) {
                    // Found best last known location: %s", l);
                    bestLocation = l;
                }
            }
            if(bestLocation !=null) {
                longitude = bestLocation.getLongitude();
                latitude = bestLocation.getLatitude();
            }
            else{
                longitude =-118.2831;
                latitude = 34.0266;
            }

//            if (Build.FINGERPRINT.startsWith("generic")
//                    || Build.FINGERPRINT.startsWith("unknown")
//                    || Build.MODEL.contains("google_sdk")
//                    || Build.MODEL.contains("Emulator")
//                    || Build.MODEL.contains("Android SDK built for x86")
//                    || Build.MANUFACTURER.contains("Genymotion")
//                    || (Build.BRAND.startsWith("generic") && Build.DEVICE.startsWith("generic"))
//                    || "google_sdk".equals(Build.PRODUCT)) {
//                longitude = 34.0266;
//                latitude = -118.2831;
//
//            }
                Log.d("tagggg :", String.valueOf(longitude) + " " + String.valueOf(latitude));

//            if(location !=null) {
//                 longitude = location.getLongitude();
//                 latitude = location.getLatitude();
//                Log.d("tagggg :", String.valueOf(longitude) + " " + String.valueOf(latitude));
//            }
//            else{
//               Toast toast = Toast.makeText(getContext(),"Could not fetch current locations",Toast.LENGTH_SHORT);
//               toast.show();
//            }
        }

    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, Bundle savedInstanceState) {

        View view = inflater.inflate(R.layout.search_fragment, container, false);
        Spinner spinner = (Spinner) view.findViewById(R.id.categ_spinner);
        ArrayAdapter<CharSequence> adapter = ArrayAdapter.createFromResource(getActivity(),
                R.array.categ_array, android.R.layout.simple_spinner_item);
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spinner.setAdapter(adapter);
        auto_text_complete = (AutoCompleteTextView) view.findViewById(R.id.locName);
        auto_text_complete.setThreshold(0);

        auto_complete_names = new ArrayList<String>();


        keywordInput = (EditText) view.findViewById(R.id.EditTextName);
        categOption = (Spinner) view.findViewById(R.id.categ_spinner);
        locInput  = (EditText) view.findViewById(R.id.locName);
        otherLocRadio = (RadioButton) view.findViewById(R.id.otherLoc);
        errTextKWord  = (TextView) view.findViewById(R.id.keywordErrorTxt);
        errTextLoc = (TextView) view.findViewById(R.id.locErrorTxt);
        distanceEntered = (EditText) view.findViewById(R.id.DistanceName);
        String distanceText = distanceEntered.getText().toString();
        if(distanceText.trim().length()==0)
            distanceText ="10";
        //Search button handling
        buttonSrch = view.findViewById(R.id.searchBttn);
         finalDistanceText = Double.parseDouble(distanceText) * 1609.34;
        buttonSrch.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {

                if(keywordInput.getText().toString().trim().isEmpty() || otherLocRadio.isChecked() && locInput.getText().toString().trim().isEmpty())
                {
                    if(keywordInput.getText().toString().trim().isEmpty())
                        errTextKWord.setVisibility(View.VISIBLE);
                    if ( otherLocRadio.isChecked() && locInput.getText().toString().trim().isEmpty())
                        errTextLoc.setVisibility(View.VISIBLE);
                }
               /* if(otherLocRadio.isChecked() && locInput.getText().toString().trim().isEmpty())
                {
                    errTextLoc.setVisibility(View.VISIBLE);
                }
                */
                else
                {
                    Log.d("reached","reached" +custom_location);
//                    urlSearchPlaces = "http://csci571-nodemq.us-east-1.elasticbeanstalk.com/dispNearByPlaces?keywordName="+keywordInput.getText().toString().trim()+
//                            "&categoryName="+categOption.getSelectedItem().toString()+"&distanceName=&locationTypeName=Here&latName="+latitude+"&longName="+longitude+"&locationName=";
                    if(!custom_location) {
                        urlSearchPlaces = "http://travelandentertainmentsearch-env.us-east-2.elasticbeanstalk.com/api/place/nearbysearch/json?location=" + latitude + "," + longitude + "&radius=" + finalDistanceText + "&type=" + categOption.getSelectedItem().toString().toLowerCase().replace(" ","_") + "&keyword=" + keywordInput.getText().toString().trim();
                        getSearchResults(urlSearchPlaces);
                    }
                    else {
                        Log.d(TAG, "onClick:here for custom location");

                        getCustomLatandLong();

                    }

                }
            }
        });

        final Button buttonClr = view.findViewById(R.id.clearBttn);
        buttonClr.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                errTextKWord.setVisibility(View.GONE);
                errTextLoc.setVisibility(View.GONE);
            }
        });


        auto_text_complete.setOnItemClickListener(new AdapterView.OnItemClickListener() {

            @Override
            public void onItemClick(AdapterView<?> parent, View arg1, int position, long arg3) {
                Object item = parent.getItemAtPosition(position);
                if (item instanceof String){
                    Log.d(TAG, "onItemClick:chosse" + item.toString());
                    custom_location_name =item.toString();

                }
            }
        });

        auto_text_complete.addTextChangedListener(new TextWatcher() {

            public void afterTextChanged(Editable s) {

            }

            public void beforeTextChanged(CharSequence s, int start, int count,
                                          int after) {

            }

            public void onTextChanged(CharSequence s, int start, int before,
                                      int count) {

                if (s.toString().length() <= 8) {
                    auto_complete_names = new ArrayList<String>();
                    getAutoSearchResults(s.toString());
                }

            }
        });
        RadioGroup rg = (RadioGroup) view.findViewById(R.id.fromLocRadio);
        rg.setOnCheckedChangeListener(new RadioGroup.OnCheckedChangeListener()
        {
            @Override
            public void onCheckedChanged(RadioGroup group, int checkedId)
            {
                switch(checkedId)
                {
                    case R.id.currLoc:
                        locInput.setEnabled(false);
                        custom_location =false;

                        break;
                    case R.id.otherLoc:
                        locInput.setEnabled(true);
                        custom_location =true;
                        break;
                }
            }
        });
        return view;
    }

    @Override
    public void onRequestPermissionsResult(int requestCode,
                                           String permissions[], int[] grantResults) {
        switch (requestCode) {
            case MY_PERMISSIONS_FINE_LOCATION: {
                // If request is cancelled, the result arrays are empty.
                if (grantResults.length > 0
                        && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                } else {
                    Log.d("debugvj", " not granted");
                    return;
                }
            }
        }


    }
    public void getCustomLatandLong(){
        String customURL="";
        try {
            Log.d(TAG, "getCustomLatandLong: hereee");
             customURL ="https://maps.googleapis.com/maps/api/geocode/json?address="+ URLEncoder.encode(custom_location_name, "UTF-8") + "&key=" + key;
        } catch (UnsupportedEncodingException e) {
            e.printStackTrace();
        }
            RequestQueue queue = Volley.newRequestQueue(getActivity());

// Request a string response from the provided URL.
            StringRequest stringRequest = new StringRequest(Request.Method.GET, customURL,
                    new Response.Listener<String>() {

                        @Override
                        public void onResponse(String response) {
                            try {
                                JSONObject js = new JSONObject(response);
                                Log.d(TAG, "onResponse: " +js.toString());
                                JSONArray array =  js.getJSONArray("results");
                                Log.d(TAG, "onResponse: " + array.toString());
                               JSONObject geometry = (JSONObject) array.getJSONObject(0).get("geometry");
                                geometry = (JSONObject) geometry.get("location");
                                setCustom_latitude((Double) geometry.get("lat"));
                                setCustom_longitude((Double) geometry.get("lng"));
                                Log.d(TAG, "onResponse: lati" +getCustom_latitude() + getCustom_longitude());
                                urlSearchPlaces = "http://travelandentertainmentsearch-env.us-east-2.elasticbeanstalk.com/api/place/nearbysearch/json?location=" + getCustom_latitude() + "," + getCustom_longitude() + "&radius=" + finalDistanceText + "&type=" + categOption.getSelectedItem().toString().toLowerCase().replace(" ","_") + "&keyword=" + keywordInput.getText().toString().trim();
                                getSearchResults(urlSearchPlaces);

                            } catch (Exception e)
                            {
                                Log.d(TAG, "Errrror: " + e.getMessage() + e.toString());
                            }

                        }
                    }, new Response.ErrorListener() {
                @Override
                public void onErrorResponse(VolleyError error) {
                    //Log.d("errornmnn","dvhsqdkvqdkwvj");
                    Log.d("error :", error.toString());
                }
            });
            queue.add(stringRequest);




    }

    public void onRadioButtonClicked(View view) {
    }

    public void onButtonPressed(Uri uri) {
        if (mListener != null) {
            mListener.onFragmentInteraction(uri);
        }
    }
    public void getAutoSearchResults(String text) {


        Log.d("autoSearchResults", autoCompleteUrl);
        autoCompleteUrl= "https://maps.googleapis.com/maps/api/place/autocomplete/json?";
        RequestQueue queue = Volley.newRequestQueue(getActivity());
        try {
            autoCompleteUrl+="input="+ URLEncoder.encode(text,"utf-8")+"&key="+key;
        } catch (UnsupportedEncodingException e) {
            e.printStackTrace();
        }
        Log.d(TAG, "getSearchResults: Came?");
        JsonObjectRequest jsonObjReq = new JsonObjectRequest(Method.GET, autoCompleteUrl,
                null, new Response.Listener<JSONObject>() {

            @Override
            public void onResponse(JSONObject response) {

                try {
                    Log.d(TAG, "onResponse: Came?");
                    JSONArray ja = response.getJSONArray(TAG_RESULT);

                    for (int i = 0; i < ja.length(); i++) {
                        JSONObject c = ja.getJSONObject(i);
                        String description = c.getString("description");
                        Log.d("description", description);
                        auto_complete_names.add(description);
                    }

                    adapter = new ArrayAdapter<String>(
                            getActivity().getApplicationContext(),
                            android.R.layout.simple_list_item_1, auto_complete_names) {
                        @Override
                        public View getView(int position,
                                            View convertView, ViewGroup parent) {
                            View view = super.getView(position,
                                    convertView, parent);
                            TextView text = (TextView) view
                                    .findViewById(android.R.id.text1);
                            text.setTextColor(Color.BLACK);
                            return view;
                        }
                    };
                    auto_text_complete.setAdapter(adapter);
                    adapter.notifyDataSetChanged();

                } catch (Exception e) {
                }
            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                Log.d(TAG, "onErrorResponse: " +"Errrrorrrrr here" +error.getMessage());
            }
        });
        queue.add(jsonObjReq);
//// Request a string response from the provided URL.
//        JsonObjectRequest jsonObjReq = new JsonObjectRequest(Request.Method.GET, autoCompleteUrl,
//                new Response.Listener<JSONObject>() {
//                    @Override
//                    public void onResponse(JSONObject response) {
//
//                        try {
//                            JSONArray predictions = response.getJSONArray("predictions");
//                            for (int i = 0; i < predictions.length(); i++) {
//                                JSONObject c = predictions.getJSONObject(i);
//                                String description = c.getString("description");
//                                Log.d("description", description);
////                                names.add(description);
//                            }
//
//                        } catch (Exception JsonE) {
//                            JsonE.printStackTrace();
//                        }
//
//
//
//                },new Response.ErrorListener() {
//            @Override
//            public void onErrorResponse(VolleyError error) {
//                Log.d("errornmnn", "dvhsqdkvqdkwvj");
//                Log.d("error :", error.toString());
//            }
//        });


    }
    public void getSearchResults(String url) {
        Log.d("jqlcbskj", url);
        RequestQueue queue = Volley.newRequestQueue(getActivity());

// Request a string response from the provided URL.
        StringRequest stringRequest = new StringRequest(Request.Method.GET, url,
                new Response.Listener<String>() {
               
                    @Override
                    public void onResponse(String response) {
                        Log.d(TAG, "onResponse:  cameeeee here on response");
                        Intent i = new Intent(getActivity(), DisplayPlaceSearchResults.class);
                        i.putExtra("jsonresponse", response.toString());
                        //  Log.d("adsgh",response.toString());
                        // Starts TargetActivity
                        startActivity(i);
                        //mTextView.setText("Response is: "+ response.substring(0,500));
                    }
                }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                //Log.d("errornmnn","dvhsqdkvqdkwvj");
                Log.d("error :", error.toString());
            }
        });
        queue.add(stringRequest);
    }
    public void getSearchResults()
    {
        Log.d("jqlcbskj",urlSearchPlaces);
        RequestQueue queue = Volley.newRequestQueue(getActivity());

// Request a string response from the provided URL.
        StringRequest stringRequest = new StringRequest(Request.Method.GET, urlSearchPlaces,
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                        Intent i = new Intent(getActivity(), DisplayPlaceSearchResults.class);
                        i.putExtra("jsonresponse", response.toString());
                        //  Log.d("adsgh",response.toString());
                        // Starts TargetActivity
                        startActivity(i);
                        //mTextView.setText("Response is: "+ response.substring(0,500));
                    }
                }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                Log.d("errornmnn","dvhsqdkvqdkwvj");
                Log.d("error :",error.toString());
            }
        });

// Add the request to the RequestQueue.
        queue.add(stringRequest);
    }
    public interface OnFragmentInteractionListener {
        // TODO: Update argument type and name
        void onFragmentInteraction(Uri uri);
    }
}

