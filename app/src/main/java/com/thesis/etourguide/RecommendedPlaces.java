package com.thesis.etourguide;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.ListView;
import android.widget.RatingBar;
import android.widget.TextView;
import android.widget.RadioButton;

import com.android.volley.toolbox.ImageLoader;
import com.android.volley.toolbox.NetworkImageView;
import com.parse.ParseQuery;
import com.parse.ParseQueryAdapter;

public class RecommendedPlaces extends Activity {

    ImageLoader imageLoader = Application.getInstance().getImageLoader();

    private String selectedPostObjectId;

    private ParseQueryAdapter<Destination> destinationQueryAdapter;

    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        this.requestWindowFeature(Window.FEATURE_NO_TITLE);
        setContentView(R.layout.main_recommended_places);

        setupActionBar("Recommended Places");

        displayRecommended();

        // Show empty layout template if listview is empty
        ListView placesListView = (ListView) findViewById(R.id.listviewTopRated);
        View empty = getLayoutInflater().inflate(R.layout.list_empty_recommended, null, false);
        ((ViewGroup)placesListView.getParent()).addView(empty);
        placesListView.setEmptyView(empty);

    }

    void displayRecommended() {

        final String location = getIntent().getExtras().getString("location");
        final String category = getIntent().getExtras().getString("category");
        final String activity = getIntent().getExtras().getString("activity");


        // Set up a customized query
        ParseQueryAdapter.QueryFactory<Destination> factory =
                new ParseQueryAdapter.QueryFactory<Destination>() {
                    public ParseQuery<Destination> create() {
                        ParseQuery<Destination> query = Destination.getQuery();
                        query.whereEqualTo("Location", location);
                        query.whereEqualTo("CategoryType", category);
                        query.whereEqualTo("ActivityType", activity);
                        query.orderByDescending("Rate");
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
        ListView placesListView = (ListView) findViewById(R.id.listviewTopRated);
        placesListView.setAdapter(destinationQueryAdapter);

        // Set up the handler for an item's selection
        placesListView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {

                final Destination item = destinationQueryAdapter.getItem(position);
                selectedPostObjectId = item.getObjectId();

                Intent in = new Intent(getApplicationContext(), PlaceDetailActivity.class);
                in.putExtra("placeId", selectedPostObjectId);
                startActivity(in);

            }
        });

    }

    void setupActionBar(String header) {

        findViewById(R.id.imgIcon).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent in = new Intent(getApplicationContext(), MainHomeActivity.class);
                startActivity(in);
                finish();
            }
        });

        TextView txtHeader = (TextView) findViewById(R.id.txtHeader);
        txtHeader.setText(header);

    }

    protected void onStart() {

        super.onStart();
        displayRecommended();

    }
}