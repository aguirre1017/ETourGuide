package com.thesis.etourguide;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.view.Window;
import android.widget.Button;

import com.thesis.etourguide.utility.AlertDialogManager;
import com.thesis.etourguide.utility.Utility;

public class MainStart extends Activity {

    AlertDialogManager alert = new AlertDialogManager();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        this.requestWindowFeature(Window.FEATURE_NO_TITLE);
        setContentView(R.layout.main_start);



        findViewById(R.id.btnRegister).setOnClickListener(new Button.OnClickListener() {
            public void onClick(View v) {

            if (Utility.isNetworkAvailable(getApplicationContext())) {
                Intent in = new Intent(getApplicationContext(), RegisterActivity.class);
                startActivity(in);
            } else
                alert.showAlertDialog(MainStart.this, "", getResources().getString(R.string.utility_network_notavailable), false);

            }
        });

        findViewById(R.id.btnLogin).setOnClickListener(new Button.OnClickListener() {
            public void onClick(View v) {
                Intent in = new Intent(getApplicationContext(), LoginActivity.class);
                startActivity(in);
            }
        });

        findViewById(R.id.btnExit).setOnClickListener(new Button.OnClickListener() {
            public void onClick(View v) {
                finish();
            }
        });

    }
}