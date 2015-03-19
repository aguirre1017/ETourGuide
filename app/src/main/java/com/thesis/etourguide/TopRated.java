package com.thesis.etourguide;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.widget.AdapterView;
import android.widget.ListView;
import android.widget.RatingBar;
import android.widget.TextView;

import com.android.volley.toolbox.ImageLoader;
import com.android.volley.toolbox.NetworkImageView;
import com.parse.ParseQuery;
import com.parse.ParseQueryAdapter;
import com.thesis.etourguide.utility.AlertDialogManager;
import com.thesis.etourguide.utility.Utility;

public class TopRated extends Activity {

    ImageLoader imageLoader = Application.getInstance().getImageLoader();

    private String selectedPostObjectId;

    private ParseQueryAdapter<Destination> destinationQueryAdapter;

    AlertDialogManager alert = new AlertDialogManager();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        this.requestWindowFeature(Window.FEATURE_NO_TITLE);
        setContentView(R.layout.main_top_rated);

        if (Utility.isNetworkAvailable(getApplicationContext())) {
            setTopRatedList();

            // Show empty layout template if listview is empty
            ListView placesListView = (ListView) findViewById(R.id.listviewTopRated);
            View empty = getLayoutInflater().inflate(R.layout.list_empty_toprated, null, false);
            ((ViewGroup)placesListView.getParent()).addView(empty);
            placesListView.setEmptyView(empty);

        } else
            alert.showAlertDialog(TopRated.this, "", getResources().getString(R.string.utility_network_notavailable), false);

    }

    void setTopRatedList() {

        // Set up a customized query
        ParseQueryAdapter.QueryFactory<Destination> factory =
            new ParseQueryAdapter.QueryFactory<Destination>() {
                public ParseQuery<Destination> create() {
                    ParseQuery<Destination> query = Destination.getQuery();
                    query.orderByDescending("Rate");
                    query.setLimit(10);
                    query.include("Rating");
                    return query;
                }
            };

        // Set up the query adapter
        destinationQueryAdapter = new ParseQueryAdapter<Destination>(this, factory) {
            @Override
            public View getItemView(Destination destination, View view, ViewGroup parent) {
                if (view == null) {
                    view = View.inflate(getContext(), R.layout.list_top_rated, null);
                }

                if (imageLoader == null)
                    imageLoader = Application.getInstance().getImageLoader();

                NetworkImageView imgPlace = (NetworkImageView) view.findViewById(R.id.imgPlace);
                TextView nameView = (TextView) view.findViewById(R.id.txtName);
                TextView descriptionView = (TextView) view.findViewById(R.id.txtDescription);
                RatingBar ratingBar = (RatingBar) view.findViewById(R.id.ratingBar);

                imgPlace.setImageUrl(destination.getImageURL(), imageLoader);
                nameView.setText(destination.getName());
                descriptionView.setText(destination.getDescription());
                ratingBar.setRating(destination.getRate().floatValue());

                return view;
            }
        };

        // Disable pagination, we'll manage the query limit ourselves
        destinationQueryAdapter.setPaginationEnabled(false);

        // Attach the query adapter to the view
        ListView postsListView = (ListView) findViewById(R.id.listviewTopRated);
        postsListView.setAdapter(destinationQueryAdapter);

        // Set up the handler for an item's selection
        postsListView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {

                final Destination item = destinationQueryAdapter.getItem(position);
                selectedPostObjectId = item.getObjectId();

                Intent in = new Intent(getApplicationContext(), PlaceDetailActivity.class);
                in.putExtra("placeId", selectedPostObjectId);
                startActivity(in);

            }
        });

    }

    protected void onStart() {

        super.onStart();
        setTopRatedList();

    }

}