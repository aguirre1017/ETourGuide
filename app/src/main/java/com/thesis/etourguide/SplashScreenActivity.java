package com.thesis.etourguide;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.Window;
import android.widget.ProgressBar;

import com.parse.ParseUser;
import com.thesis.etourguide.utility.LoadingTask;
import com.thesis.etourguide.utility.LoadingTask.LoadingTaskFinishedListener;

public class SplashScreenActivity extends Activity implements LoadingTaskFinishedListener {

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        this.requestWindowFeature(Window.FEATURE_NO_TITLE);
        setContentView(R.layout.splash_screen);

        ProgressBar progressBar = (ProgressBar) findViewById(R.id.activity_splash_progress_bar);
        new LoadingTask(progressBar, this).execute();
    }

    @Override
    public void onTaskFinished() {
        // Check if there is current user info
        if (ParseUser.getCurrentUser() != null) {
            // Start an intent for the logged in activity
            startActivity(new Intent(SplashScreenActivity.this, MainHomeActivity.class));
        } else {
            // Start and intent for the logged out activity
            startActivity(new Intent(SplashScreenActivity.this, MainStart.class));
        }

        finish();
    }

}