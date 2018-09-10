package com.example.vijethapv.myapplication;


import android.app.Fragment;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;

import android.os.AsyncTask;

import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;





import java.io.InputStream;

import java.util.List;

public class PlacesSearchAdapter extends RecyclerView.Adapter<PlacesSearchAdapter.MyViewHolder> {

    //<placeid, name, vicinity, icon>
    //private List<Movie> placesList;
    private List<List<String>> placeData;
    private DisplayPlaceSearchResults pActivity;
    private FavouritesFragment favFrag;


    public class MyViewHolder extends RecyclerView.ViewHolder {
        public TextView title, addr;
        public ImageView plIcon, favIcon;

        public MyViewHolder(View view) {
            super(view);
            title = (TextView) view.findViewById(R.id.placeTitle);
            addr = (TextView) view.findViewById(R.id.placeAddress);
            plIcon =  (ImageView) view.findViewById(R.id.placeIcon);
            favIcon = (ImageView) view.findViewById(R.id.favIcon);
//            favIcon.setOnClickListener(this);
        }
    }

    //<placeid, name, vicinity, icon>
    public PlacesSearchAdapter(List<List<String>> placesDet, DisplayPlaceSearchResults i) {
        this.placeData = placesDet;
        this.pActivity = i;
//        this.favFrag =  (FavouritesFragment)i.getParent().getFragmentManager().;

    }

    @Override
    public MyViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View itemView = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.places_list_row, parent, false);

        return new MyViewHolder(itemView);
    }

    @Override
    public void onBindViewHolder(final MyViewHolder holder, int position) {

        final List<String> plRow = placeData.get(position);
        holder.title.setText(plRow.get(1));
        holder.addr.setText(plRow.get(2));
        new DownloadImageFromInternet(holder.plIcon)
                .execute(plRow.get(3));
        final FavouriteHelper favhelper = new FavouriteHelper(pActivity);
        if(favhelper.checkInFavourites(plRow.get(0))>-1){
            holder.favIcon.setImageResource(R.drawable.red);
        }
        else{
            holder.favIcon.setImageResource(R.drawable.white);
        }
        //        holder.favIcon.setTag(R.drawable.red,1);
        //        holder.favIcon.setTag(R.drawable.white,2);

        holder.favIcon.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                    String place_id = plRow.get(0);
                    int index =favhelper.checkInFavourites(place_id);
                    if(index>-1)
                    {
                        favhelper.deleteFromFavorites(place_id,index);
                        holder.favIcon.setImageResource(R.drawable.white);

                    }
                    else{
                        favhelper.addtoFavorites(plRow);
                        holder.favIcon.setImageResource(R.drawable.red);

                    }
                // Log.d("tag val :",holder.favIcon.getTag().toString());


                //                if(holder.favIcon.getDrawable().equals(ContextCompat.getDrawable(pActivity.getApplicationContext(),R.drawable.red)))
                //                {
                //                    Log.d("changed if","if");
                //                    holder.favIcon.setImageResource(R.drawable.white);
                //                }
                //
                //                else
                //                {
                //                    Log.d("changed else","else");
                //                    holder.favIcon.setImageResource(R.drawable.red);
                //                }
            }
        });

                //  Log.d(" ",plRow.get(2));

                //  holder.iconUrl.setImageResource(plRow.get(2));
    }

    private class DownloadImageFromInternet extends AsyncTask<String, Void, Bitmap> {
        ImageView imageView;

        public DownloadImageFromInternet(ImageView imageView) {
            this.imageView = imageView;
            //  Toast.makeText(getApplicationContext(), "Please wait, it may take a few minute...", Toast.LENGTH_SHORT).show();
        }

        protected Bitmap doInBackground(String... urls) {
            String imageURL = urls[0];
            Bitmap bimage = null;
            try {
                InputStream in = new java.net.URL(imageURL).openStream();
                bimage = BitmapFactory.decodeStream(in);

            } catch (Exception e) {
                Log.e("Error Message", e.getMessage());
                e.printStackTrace();
            }
            return bimage;
        }

        protected void onPostExecute(Bitmap result) {
            imageView.setImageBitmap(result);
        }
    }


    @Override
    public int getItemCount() {
        return placeData.size();
    }
    public void setPlacesDet(List<List<String>> placesDet, DisplayPlaceSearchResults i) {
        this.placeData = placesDet;
        this.pActivity = i;
    }
}