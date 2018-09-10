package com.example.vijethapv.myapplication;

import android.content.Context;
import android.os.Bundle;
import android.os.Environment;
import android.support.v4.app.Fragment;
import android.support.v7.widget.DefaultItemAnimator;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.google.gson.Gson;


import java.io.BufferedReader;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.InputStreamReader;
import java.lang.reflect.Array;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class FavouritesFragment  extends Fragment{

    public static final String TAG ="FromFavouritesFragment";
    private RecyclerView recyclerView;
    private TextView emptyFavourites;
    public FavouritesFragment() {
        // Required empty public constructor
    }



    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
                // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.favourites_fragment, container, false);
            //        Favourite fav = new Favourite();
            //        fav.name ="University of Southern California";
            //        fav.address =" 32nd street , Jeffereson" ;

        String fileContents = "Hello world!";
        FileOutputStream outputStream;
        StringBuilder  readInput = new StringBuilder();
        Log.d(TAG, "onCreateView: here before reading from local storage");
        emptyFavourites = (TextView) view.findViewById(R.id.empty_fav);
        FavWrapper[] elements = new FavouriteHelper(getContext()).readFavouritesFromStorage();
        if(elements!=null) {

            ArrayList<FavWrapper> favlist = new ArrayList<FavWrapper> ( Arrays.asList(elements));
            FavouriteAdapter favadapter = new FavouriteAdapter(favlist, getContext());


            recyclerView = (RecyclerView) view.findViewById(R.id.my_recycler_view_favourites);
            RecyclerView.LayoutManager mLayoutManager = new LinearLayoutManager(getActivity());
            recyclerView.setLayoutManager(mLayoutManager);
            recyclerView.setItemAnimator(new DefaultItemAnimator());
            recyclerView.setAdapter(favadapter);
            FavouriteObserver observer = new FavouriteObserver(recyclerView, emptyFavourites);
            favadapter.registerAdapterDataObserver(observer);

        }



        //        try {
                                //            outputStream = getContext().openFileOutput(filename, Context.MODE_PRIVATE);
                                //
                                //            Favourite obj = new Favourite();
                                //            obj.name= "crunchify.com";
                                //            obj.address = "Author";
                                //            FavWrapper favwrapper  = new FavWrapper(obj, "1234");
                                //            Gson nGson = new Gson();
                                //            String  jsonString = nGson.toJson(favwrapper);
                                //
                                //
                                //            outputStream.write(jsonString.getBytes()); // fileContents.getBytes()
                                //            outputStream.close();
                                //        } catch (Exception e) {
                                //            e.printStackTrace();
                                //        }
                                //        try{
                                //            FileInputStream inputStream = getContext().openFileInput(filename);
                                //            InputStreamReader isr = new InputStreamReader(inputStream );
                                //            BufferedReader bufferedReader = new BufferedReader(isr);
                                //             readInput = new StringBuilder();
                                //            String line;
                                //            while ((line = bufferedReader.readLine()) != null) {
                                //                readInput.append(line);
                                //            }
                                //            Log.d(" StringFromFile" , readInput.toString());
                                //
                                //            FavWrapper[] element= new Gson().fromJson (readInput.toString(), FavWrapper[].class);
                                //
                                ////            Log.d(" FavWrapper" , element.place_id + " " + element.fav.getName());
                                ////            TextView tvName = (TextView) view.findViewById(R.id.favFragTextViewName);
                                ////            TextView tvAddress = (TextView) view.findViewById(R.id.favFragTextViewAddress);
                                ////
                                ////             tvName.setText(element.fav.getName());
                                ////             tvAddress.setText(element.fav.getAddress());
                                //
                                //
                                //            inputStream.close();
                                //        }
                                //        catch(Exception e){
                                //            e.printStackTrace();
                                //        }

        return view;
    }
//    public int checkInFavourites(String placeid)
//    {
//        FavWrapper[] favouritesList = readFavouritesFromStorage();
//        if(favouritesList!=null) {
//            for (int i = 0; i < favouritesList.length; i++) {
//                if (favouritesList[i].place_id.equals(placeid)) {
//                    return i;
//                }
//            }
//        }
//        return -1;
//    }
//    public FavWrapper[] readFavouritesFromStorage()
//    {
//        FavWrapper[] element =null;
//       try{
//           Log.d(TAG, "readFavouritesFromStorage:  "+getActivity() );
////           File file = new File(filename);
//           File root =Environment.getDataDirectory();
//           File file = new File(root,filename);
//           if(!file.exists()){
//               file.createNewFile();
//               file.canWrite();
//               file.canRead();
//           }
//
//           FileInputStream inputStream =  new FileInputStream(file);      // getActivity().openFileInput(filename);
//        StringBuilder readInput = new StringBuilder();
//        InputStreamReader isr = new InputStreamReader(inputStream );
//        BufferedReader bufferedReader = new BufferedReader(isr);
//        readInput = new StringBuilder();
//        String line;
//        while ((line = bufferedReader.readLine()) != null) {
//            readInput.append(line);
//        }
//        Log.d(" StringFromFile" , readInput.toString());
//        if(readInput.toString().length()>0) {
//            element = new Gson().fromJson(readInput.toString(), FavWrapper[].class);
//
//            Log.d(" FavWrapper", Arrays.toString(element));
//        }
//        inputStream.close();
//    }
////    catch(FileNotFoundException fe)
////    {
////        try {
////            FileOutputStream outputStream;
////            File file = new File(filename);
////            file.createNewFile();
////            file.canWrite();
////            file.canRead();
////            outputStream = new FileOutputStream(file);
////            // getActivity().openFileOutput(filename, Context.MODE_PRIVATE);
//////            Gson nGson = ne12w Gson();
//////            String  jsonString = nGson.toJson(elements);
////            outputStream.write("".getBytes()); // fileContents.getBytes()
////            outputStream.close();
////        } catch (Exception e) {
////            e.printStackTrace();
////        }
////
////    }
//        catch(Exception e){
//        e.printStackTrace();
//    }
//    return element;
//
//    }
//    public void deleteFromFavorites(String place_id,int index) {
//
//        FavWrapper[] elements = new FavouritesFragment().readFavouritesFromStorage();
//        Log.d(TAG, "deleteFromFavorites: " +elements);
//        if(elements!=null) {
//            ArrayList<FavWrapper> arrayFav = (ArrayList<FavWrapper>) Arrays.asList(elements);
//            arrayFav.remove(index);
//            elements = arrayFav.toArray(new FavWrapper[arrayFav.size()]);
//            new FavouritesFragment().writeFavouritesToStorage(elements);
//        }
//
//    }
//
//    private void writeFavouritesToStorage(FavWrapper[] elements) {
//        FileOutputStream outputStream;
//        StringBuilder  readInput = new StringBuilder();
//
//        try {
//            File file = new File(filename);
//            file.canWrite();
//            file.canRead();
//            outputStream =  new FileOutputStream(file); //getActivity().openFileOutput(filename, Context.MODE_PRIVATE);
//            Gson nGson = new Gson();
//            String  jsonString = nGson.toJson(elements);
//
//
//            outputStream.write(jsonString.getBytes()); // fileContents.getBytes()
//            outputStream.close();
//        } catch (Exception e) {
//            e.printStackTrace();
//        }
//    }
//
//    public void addtoFavorites(List<String> plRow) {
//        FavWrapper[] elements = new FavouritesFragment().readFavouritesFromStorage();
//        Favourite obj = new Favourite();
//        obj.name= plRow.get(1);
//        obj.address = plRow.get(2);
//        obj.icon= plRow.get(3);
//        FavWrapper favwrapper  = new FavWrapper(obj, plRow.get(0));
//        ArrayList<FavWrapper> arrayFav = new ArrayList<>();
//        if(elements!=null) {
//             arrayFav = (ArrayList<FavWrapper>) Arrays.asList(elements);
//            arrayFav.add(favwrapper);
//        }
//        else
//        {
//
//        arrayFav.add(favwrapper);
//        }
//        elements =  arrayFav.toArray(new FavWrapper[arrayFav.size()]);
//
//        new FavouritesFragment().writeFavouritesToStorage(elements);
//
//    }



}
