package com.thesis.etourguide;

import android.app.Activity;
import android.app.ProgressDialog;
import android.content.Intent;
import android.os.Bundle;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.widget.Button;
import android.widget.ListAdapter;
import android.widget.ListView;
import android.widget.RatingBar;
import android.widget.TextView;
import android.widget.LinearLayout.LayoutParams;

import com.android.volley.toolbox.ImageLoader;
import com.android.volley.toolbox.NetworkImageView;
import com.parse.GetCallback;
import com.parse.ParseException;
import com.parse.ParseObject;
import com.parse.ParseQuery;
import com.parse.ParseQueryAdapter;
import com.thesis.etourguide.utility.AlertDialogManager;

public class PlaceDetailActivity extends Activity {

    ImageLoader imageLoader = Application.getInstance().getImageLoader();

    AlertDialogManager alert = new AlertDialogManager();

    private Double latitude, longitude;
    private String name, description;

    private ParseQueryAdapter<com.thesis.etourguide.Rating> commentQueryAdapter;

    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        this.requestWindowFeature(Window.FEATURE_NO_TITLE);
        setContentView(R.layout.main_place_detail);

        displayPlaceDetail(getIntent().getExtras().getString("placeId"));

        displayComment(getIntent().getExtras().getString("placeId"));

        // Show empty layout template if listview is empty
        ListView placesListView = (ListView) findViewById(R.id.listviewComment);
        View empty = getLayoutInflater().inflate(R.layout.list_empty, null, false);
        ((ViewGroup)placesListView.getParent()).addView(empty);
        placesListView.setEmptyView(empty);

    }

    protected void onStart() {

        super.onStart();
        displayPlaceDetail(getIntent().getExtras().getString("placeId"));
        displayComment(getIntent().getExtras().getString("placeId"));

    }

    void displayPlaceDetail(String placeId) {

        // Set up a progress dialog
        final ProgressDialog dialog = new ProgressDialog(PlaceDetailActivity.this);
        dialog.setMessage("Fetching place details...");
        dialog.show();

        ParseQuery<ParseObject> query = ParseQuery.getQuery("Destination");
        query.getInBackground(placeId, new GetCallback<ParseObject>() {
            public void done(ParseObject destination, ParseException e) {

                dialog.dismiss();
                if (e == null) {

                    name = destination.getString("Name");
                    description =  destination.getString("Description");
                    setupActionBar(name);

                    NetworkImageView imgPlace = (NetworkImageView) findViewById(R.id.imgPlace);
                    RatingBar ratingBar = (RatingBar) findViewById(R.id.ratingBar);
                    TextView txtDescription = (TextView) findViewById(R.id.txtDescription);
                    TextView txtLocation = (TextView) findViewById(R.id.txtLocation);
                    TextView txtCategory = (TextView) findViewById(R.id.txtCategory);

                    imgPlace.setImageUrl(destination.getString("Image"), imageLoader);
                    txtDescription.setText(description);
                    txtLocation.setText(destination.getString("Location"));
                    txtCategory.setText(destination.getString("CategoryType"));
                    ratingBar.setRating((float) destination.getDouble("Rate"));

                    if (destination.getParseGeoPoint("Map") != null){
                        latitude = destination.getParseGeoPoint("Map").getLatitude();
                        longitude = destination.getParseGeoPoint("Map").getLongitude();

                        findViewById(R.id.btnViewMap).setOnClickListener(new Button.OnClickListener() {
                            public void onClick(View v) {

                                Intent in = new Intent(getApplicationContext(), MainMapActivity.class);
                                in.putExtra("name", name);
                                in.putExtra("description", description);
                                in.putExtra("latitude", latitude);
                                in.putExtra("longitude", longitude);
                                startActivity(in);

                            }
                        });
                    }

                    findViewById(R.id.btnRateIt).setOnClickListener(new Button.OnClickListener() {
                        public void onClick(View v) {

                            Intent in = new Intent(getApplicationContext(), RatePlaceActivity.class);
                            in.putExtra("placeId", getIntent().getExtras().getString("placeId"));
                            in.putExtra("name", name);
                            startActivity(in);

                        }
                    });

                } else {
                    alert.showAlertDialog(PlaceDetailActivity.this, "", e.getMessage(), false);
                }
            }
        });
    }

    void displayComment(final String placeId){

        // Set up a customized query
        ParseQueryAdapter.QueryFactory<com.thesis.etourguide.Rating> factory =
                new ParseQueryAdapter.QueryFactory<com.thesis.etourguide.Rating>() {
                    public ParseQuery<com.thesis.etourguide.Rating> create() {
                        ParseQuery<com.thesis.etourguide.Rating> query = com.thesis.etourguide.Rating.getQuery();
                        query.whereEqualTo("DestinationId", placeId);
                        query.orderByDescending("createdAt");
                        return query;
                    }
                };

        // Set up the query adapter
        commentQueryAdapter = new ParseQueryAdapter<com.thesis.etourguide.Rating>(this, factory) {
            @Override
            public View getItemView(com.thesis.etourguide.Rating rating, View view, ViewGroup parent) {
                if (view == null) {
                    view = View.inflate(getContext(), R.layout.list_comment, null);
                }

                final TextView nameView = (TextView) view.findViewById(R.id.txtName);
                RatingBar ratingBar = (RatingBar) view.findViewById(R.id.ratingBar);
                TextView commentView = (TextView) view.findViewById(R.id.txtComment);
                TextView dateTimeView = (TextView) view.findViewById(R.id.txtDateTime);

                ParseQuery<ParseObject> query = ParseQuery.getQuery("_User");
                query.getInBackground(rating.getTouristId(), new GetCallback<ParseObject>() {
                    public void done(ParseObject user, ParseException e) {
                        if (e == null) {
                            nameView.setText(user.getString("Name"));
                        }
                    }
                });

                ratingBar.setRating(rating.getRate());
                commentView.setText(rating.getComment());
                dateTimeView.setText(rating.getCreatedAt().toLocaleString());

                return view;
            }
        };

        // Disable pagination, we'll manage the query limit ourselves
        commentQueryAdapter.setPaginationEnabled(false);

        // Attach the query adapter to the view
        ListView placesListView = (ListView) findViewById(R.id.listviewComment);
        placesListView.setAdapter(commentQueryAdapter);

        // Controls user's touch on screen for listview inst
        placesListView.setOnTouchListener(new View.OnTouchListener() {
            // Setting on Touch Listener for handling the touch inside ScrollView
            @Override
            public boolean onTouch(View v, MotionEvent event) {
                // Disallow the touch request for parent scroll on touch of child view
                v.getParent().requestDisallowInterceptTouchEvent(true);
                return false;
            }
        });
        setListViewHeightBasedOnChildren(placesListView);

    }

    public static void setListViewHeightBasedOnChildren(ListView listView) {
        ListAdapter listAdapter = listView.getAdapter();
        if (listAdapter == null)
            return;

        int desiredWidth = View.MeasureSpec.makeMeasureSpec(listView.getWidth(), View.MeasureSpec.UNSPECIFIED);
        int totalHeight = 0;
        View view = null;
        for (int i = 0; i < listAdapter.getCount(); i++) {
            view = listAdapter.getView(i, view, listView);
            if (i == 0)
                view.setLayoutParams(new ViewGroup.LayoutParams(desiredWidth, LayoutParams.WRAP_CONTENT));

            view.measure(desiredWidth, View.MeasureSpec.UNSPECIFIED);
            totalHeight += view.getMeasuredHeight();
        }
        ViewGroup.LayoutParams params = listView.getLayoutParams();
        params.height = totalHeight + (listView.getDividerHeight() * (listAdapter.getCount() - 1));
        listView.setLayoutParams(params);
        listView.requestLayout();
    }

    void setupActionBar(String header) {

        findViewById(R.id.imgIcon).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });

        TextView txtHeader = (TextView) findViewById(R.id.txtHeader);
        txtHeader.setText(header);

    }

}