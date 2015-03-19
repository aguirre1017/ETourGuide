package com.thesis.etourguide;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.view.Window;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.Spinner;
import android.widget.TextView;

public class SurveyActivityType extends Activity {

    String category;

    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        this.requestWindowFeature(Window.FEATURE_NO_TITLE);
        setContentView(R.layout.main_survey_activity_type);

        setupActionBar("Question No. 3");

        TextView txtQuestion = (TextView) findViewById(R.id.txtQuestion);
        txtQuestion.setText("Select from the activities that you want:");

        category = getIntent().getExtras().getString("category");

        populateActivityType(category);

        findViewById(R.id.btnNext).setOnClickListener(new Button.OnClickListener() {
            public void onClick(View v) {

                Spinner spnActivity = (Spinner) findViewById(R.id.spnActivity);

                String location = getIntent().getExtras().getString("location");
                String activity = spnActivity.getSelectedItem().toString();

                Intent in = new Intent(getApplicationContext(), RecommendedPlaces.class);
                in.putExtra("location", location);
                in.putExtra("category", category);
                in.putExtra("activity", activity);
                startActivity(in);
                finish();

            }
        });

    }

    void populateActivityType(String category) {

        String[] activity = new String[] {"Nothing"};

        if (category.equals("Adventure"))
            activity = new String[] {"ATV", "Island hopping", "Water rafting", "Water Sports", "Zipline", "Hiking"};
        else if (category.equals("Cultural"))
            activity = new String[] {"Religious Sites", "Historical Places/Event"};
        else if (category.equals("Night Life"))
            activity = new String[] {" Bars", " Club"};
        else if (category.equals("Outdoors"))
            activity = new String[] {"Resorts", "Islands", "Waterfalls", "Caves/Geologic Formations", "Volcanoes", "Mountains", "Parks"};

        Spinner spnActivity = (Spinner) findViewById(R.id.spnActivity);
        ArrayAdapter dataAdapter = new ArrayAdapter(SurveyActivityType.this, android.R.layout.simple_spinner_item, activity);
        spnActivity.setAdapter(dataAdapter);

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

