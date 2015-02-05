package com.thesis.etourguide;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.view.Window;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TabHost;
import android.widget.TabHost.TabSpec;
import android.app.TabActivity;

import com.thesis.etourguide.utility.ActionItem;
import com.thesis.etourguide.utility.AlertDialogManager;
import com.thesis.etourguide.utility.QuickAction;

import com.parse.ParseUser;
import com.thesis.etourguide.utility.Utility;

public class MainHomeActivity extends TabActivity {

    private static final int ID_LOGOUT = 0;

    AlertDialogManager alert = new AlertDialogManager();

    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        this.requestWindowFeature(Window.FEATURE_NO_TITLE);
        setContentView(R.layout.main_home);

        setupActionBar();
        setupTabs();

        findViewById(R.id.btnTakeSurvey).setOnClickListener(new Button.OnClickListener() {
            public void onClick(View v) {

                if (Utility.isNetworkAvailable(getApplicationContext())) {
                    Intent in = new Intent(getApplicationContext(), SurveyLocation.class);
                    startActivity(in);
                    finish();
                } else
                    alert.showAlertDialog(MainHomeActivity.this, "", getResources().getString(R.string.utility_network_notavailable), false);

            }
        });

    }

    void setupActionBar() {

        ActionItem logoutItem 	= new ActionItem(ID_LOGOUT, "Logout", getResources().getDrawable(R.drawable.ic_logout));
        logoutItem.setSticky(true);

        final QuickAction quickAction = new QuickAction(this, QuickAction.ANIM_AUTO);
        quickAction.addActionItem(logoutItem);

        quickAction.setOnActionItemClickListener(new QuickAction.OnActionItemClickListener() {
            @Override
            public void onItemClick(QuickAction source, int pos, int actionId) {

                if (actionId == ID_LOGOUT) {

                    ParseUser.logOut();
                    ParseUser currentUser = ParseUser.getCurrentUser(); // this will now be null
                    startActivity(new Intent(MainHomeActivity.this, MainStart.class));
                    finish();

                }
            }
        });

        ImageView imgMenu = (ImageView) this.findViewById(R.id.imgMenu);
        imgMenu.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                quickAction.show(v);
                quickAction.setAnimStyle(QuickAction.ANIM_REFLECT);
            }
        });
    }

    void setupTabs() {

        // create the TabHost that will contain the Tabs
        TabHost tabHost = (TabHost) findViewById(android.R.id.tabhost);

        TabSpec tab1 = tabHost.newTabSpec("Top Rated");

        // Set the Tab name and Activity
        // that will be opened when particular Tab will be selected
        tab1.setIndicator("Top Rated");
        tab1.setContent(new Intent(this, TopRated.class));

        /** Add the tabs  to the TabHost to display. */
        tabHost.addTab(tab1);

    }
}