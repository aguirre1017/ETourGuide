package com.thesis.etourguide;

import android.app.Activity;
import android.app.ProgressDialog;
import android.os.Bundle;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.widget.LinearLayout;
import android.widget.ListAdapter;
import android.widget.ListView;
import android.widget.RatingBar;
import android.widget.TextView;

import com.parse.GetCallback;
import com.parse.ParseException;
import com.parse.ParseObject;
import com.parse.ParseQuery;
import com.parse.ParseQueryAdapter;

public class PlaceCommentActivity extends Activity {

    private ParseQueryAdapter<Rating> commentQueryAdapter;

    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        this.requestWindowFeature(Window.FEATURE_NO_TITLE);
        setContentView(R.layout.main_place_comment);

        setupActionBar(getIntent().getExtras().getString("name"));

        displayComment(getIntent().getExtras().getString("placeId"));

        // Show empty layout template if listview is empty
        ListView commentListView = (ListView) findViewById(R.id.listviewComment);
        View empty = getLayoutInflater().inflate(R.layout.list_empty, null, false);
        ((ViewGroup) commentListView.getParent()).addView(empty);
        commentListView.setEmptyView(empty);

    }

    protected void onStart() {

        super.onStart();
        displayComment(getIntent().getExtras().getString("placeId"));

    }

    void displayComment(final String placeId){

        // Set up a customized query
        ParseQueryAdapter.QueryFactory<com.thesis.etourguide.Rating> factory =
                new ParseQueryAdapter.QueryFactory<com.thesis.etourguide.Rating>() {
                    public ParseQuery<Rating> create() {
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
        ListView commentListView = (ListView) findViewById(R.id.listviewComment);
        commentListView.setAdapter(commentQueryAdapter);

        // Controls user's touch on screen for listview inst
        commentListView.setOnTouchListener(new View.OnTouchListener() {
            // Setting on Touch Listener for handling the touch inside ScrollView
            @Override
            public boolean onTouch(View v, MotionEvent event) {
                // Disallow the touch request for parent scroll on touch of child view
                v.getParent().requestDisallowInterceptTouchEvent(true);
                return false;
            }
        });

        setListViewHeightBasedOnChildren(commentListView);

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
                view.setLayoutParams(new ViewGroup.LayoutParams(desiredWidth, LinearLayout.LayoutParams.WRAP_CONTENT));

            view.measure(desiredWidth, View.MeasureSpec.UNSPECIFIED);
            totalHeight += view.getMeasuredHeight();
        }
        ViewGroup.LayoutParams params = listView.getLayoutParams();
        params.height = totalHeight + (listView.getDividerHeight() * (listAdapter.getCount() - 1));
        listView.setLayoutParams(params);
        listView.requestLayout();
    }

}