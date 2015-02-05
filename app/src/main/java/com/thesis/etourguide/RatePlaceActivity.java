package com.thesis.etourguide;

import android.app.Activity;
import android.app.ProgressDialog;
import android.os.Bundle;
import android.view.View;
import android.view.Window;
import android.widget.Button;
import android.widget.EditText;
import android.widget.RatingBar;
import android.widget.TextView;

import com.parse.FindCallback;
import com.parse.GetCallback;
import com.parse.ParseException;
import com.parse.ParseObject;
import com.parse.ParseQuery;
import com.parse.ParseUser;
import com.parse.SaveCallback;
import com.thesis.etourguide.utility.AlertDialogManager;

import java.util.List;

public class RatePlaceActivity extends Activity {

    private String name, placeId, userId;
    private int totalRating = 0;
    private double averageRating = 0;

    AlertDialogManager alert = new AlertDialogManager();

    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        this.requestWindowFeature(Window.FEATURE_NO_TITLE);
        setContentView(R.layout.main_rate_place);

        placeId = getIntent().getExtras().getString("placeId");
        name = getIntent().getExtras().getString("name");

        setupActionBar(name);

        findViewById(R.id.btnRate).setOnClickListener(new Button.OnClickListener() {
            public void onClick(View v) {

                RatingBar ratingBar = (RatingBar) findViewById(R.id.ratingBar);
                EditText txtComment = (EditText) findViewById(R.id.txtComment);

                if (ratingBar.getRating() < 1) {
                    alert.showAlertDialog(RatePlaceActivity.this, "", "Your rating is required.", false);
                }
                else {
                    // Set up a progress dialog
                    final ProgressDialog dialog = new ProgressDialog(RatePlaceActivity.this);
                    dialog.setMessage("Saving your rating..");
                    dialog.show();

                    Rating ratingPost = new Rating();
                    ratingPost.setTouristId(ParseUser.getCurrentUser().getObjectId());
                    ratingPost.setDestinationId(placeId);
                    ratingPost.setRate((int) ratingBar.getRating());
                    ratingPost.setComment(txtComment.getText().toString());

                    // Save the post
                    ratingPost.saveInBackground(new SaveCallback() {
                        @Override
                        public void done(ParseException e) {

                            ParseQuery<ParseObject> query = ParseQuery.getQuery("Rating");
                            query.whereEqualTo("DestinationId", placeId);
                            query.findInBackground(new FindCallback<ParseObject>() {
                                public void done(final List<ParseObject> ratingList, ParseException e) {

                                    totalRating = 0;
                                    averageRating = 0;

                                    for (ParseObject rating : ratingList) {
                                        totalRating = totalRating + rating.getInt("Rate");
                                    }

                                    if(ratingList.size() > 1)
                                        averageRating = ((double) totalRating / (double) ratingList.size());
                                    else
                                        averageRating = (double) totalRating;

                                    ParseQuery<ParseObject> queryPlace = ParseQuery.getQuery("Destination");
                                    queryPlace.getInBackground(placeId, new GetCallback<ParseObject>() {
                                        public void done(ParseObject place, ParseException e) {
                                            if (e == null) {
                                                place.put("Rate", averageRating);
                                                place.saveInBackground();
                                            }
                                        }
                                    });

                                }
                            });

                            dialog.dismiss();
                            finish();
                        }
                    });
                }
            }
        });

        findViewById(R.id.btnCancel).setOnClickListener(new Button.OnClickListener() {
            public void onClick(View v) {
                finish();
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