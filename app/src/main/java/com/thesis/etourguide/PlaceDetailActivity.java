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
import android.widget.LinearLayout.LayoutParams;
import android.widget.ListAdapter;
import android.widget.ListView;
import android.widget.RatingBar;
import android.widget.TextView;

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

    }

    protected void onStart() {

        super.onStart();
        displayPlaceDetail(getIntent().getExtras().getString("placeId"));

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
                    TextView txtHowToGetThere = (TextView) findViewById(R.id.txtHowToGetThere);

                    imgPlace.setImageUrl(destination.getString("Image"), imageLoader);
                    txtDescription.setText(description);
                    txtLocation.setText(destination.getString("Addresss"));
                    txtCategory.setText(destination.getString("CategoryType"));
                    txtHowToGetThere.setText(destination.getString("HowToGetThere"));
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

                    findViewById(R.id.btnComments).setOnClickListener(new Button.OnClickListener() {
                        public void onClick(View v) {

                            Intent in = new Intent(getApplicationContext(), PlaceCommentActivity.class);
                            in.putExtra("placeId", getIntent().getExtras().getString("placeId"));
                            in.putExtra("name", name);
                            startActivity(in);

                        }
                    });

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