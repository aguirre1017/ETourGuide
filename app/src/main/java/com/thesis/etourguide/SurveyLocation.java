package com.thesis.etourguide;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.view.Window;
import android.widget.Button;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.TextView;

import com.thesis.etourguide.utility.AlertDialogManager;

public class SurveyLocation extends Activity {

    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        this.requestWindowFeature(Window.FEATURE_NO_TITLE);
        setContentView(R.layout.main_survey_location);

        setupActionBar("Question No. 1");

        TextView txtQuestion = (TextView) findViewById(R.id.txtQuestion);
        txtQuestion.setText("1. Where do you want to travel around?");

        findViewById(R.id.btnNext).setOnClickListener(new Button.OnClickListener() {
            public void onClick(View v) {

                AlertDialogManager alert = new AlertDialogManager();
                RadioGroup radioGroup = (RadioGroup) findViewById(R.id.radioGroup);

                if(radioGroup.getCheckedRadioButtonId()!=-1) {

                    int id= radioGroup.getCheckedRadioButtonId();
                    View radioButton = radioGroup.findViewById(id);
                    int radioId = radioGroup.indexOfChild(radioButton);
                    RadioButton btn = (RadioButton) radioGroup.getChildAt(radioId);
                    String location = (String) btn.getText();

                    Intent in = new Intent(getApplicationContext(), SurveyCategory.class);
                    in.putExtra("location", location);
                    startActivity(in);
                    finish();

                } else
                    alert.showAlertDialog(SurveyLocation.this, "", "Please select location.", false);

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