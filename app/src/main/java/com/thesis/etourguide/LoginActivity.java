package com.thesis.etourguide;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.view.MotionEvent;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.Window;
import android.view.inputmethod.InputMethodManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.app.ProgressDialog;

import com.parse.LogInCallback;
import com.parse.ParseException;
import com.parse.ParseUser;

import com.thesis.etourguide.utility.AlertDialogManager;
import com.thesis.etourguide.utility.Utility;

// test commit here

public class LoginActivity extends Activity {

	AlertDialogManager alert = new AlertDialogManager();
	
	EditText inputEmail;
    EditText inputPassword;
    
    TextView txtLog;
    
	public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        this.requestWindowFeature(Window.FEATURE_NO_TITLE);
        setContentView(R.layout.main_login);
        
        setupActionBar("Login");

        findViewById(R.id.btnLogin).setOnClickListener(new Button.OnClickListener() {
	        public void onClick(View v) {
	            login();
	        }
        });

        findViewById(R.id.txtRegister).setOnClickListener(new Button.OnClickListener() {
	        public void onClick(View v) {
                if (Utility.isNetworkAvailable(getApplicationContext())) {
                    Intent in = new Intent(getApplicationContext(), RegisterActivity.class);
                    startActivity(in);
                    finish();
                } else
                    alert.showAlertDialog(LoginActivity.this, "", getResources().getString(R.string.utility_network_notavailable), false);
            }
        });
        
	}

    void login() {

        inputEmail  = (EditText) findViewById(R.id.txtEmail);
        inputPassword  = (EditText) findViewById(R.id.txtPassword);

        if (inputEmail.getText().toString().equalsIgnoreCase("")) {
            alert.showAlertDialog(LoginActivity.this, "", getResources().getString(R.string.login_entry_email), false);
        }else if (inputPassword.getText().toString().equalsIgnoreCase("")) {
            alert.showAlertDialog(LoginActivity.this, "", getResources().getString(R.string.login_entry_password), false);
        }else {
            if (!Utility.isInputValid(inputEmail.getText().toString(), "^[\\w\\.-]+@([\\w\\-]+\\.)+[A-Z]{2,4}$")) {
                alert.showAlertDialog(LoginActivity.this, "", getResources().getString(R.string.login_entry_email_invalid), false);
            } else {

                String username = inputEmail.getText().toString().trim();
                String password = inputPassword.getText().toString().trim();

                // Set up a progress dialog
                final ProgressDialog dialog = new ProgressDialog(LoginActivity.this);
                dialog.setMessage("Logging your account...");
                dialog.show();

                // Call the Parse login method
                ParseUser.logInInBackground(username, password, new LogInCallback() {
                    @Override
                    public void done(ParseUser user, ParseException e) {
                        dialog.dismiss();
                        if (e != null) {
                            // Show the error message
                            alert.showAlertDialog(LoginActivity.this, "", e.getMessage(), false);
                        } else {
                            Intent intent = new Intent(LoginActivity.this, SurveyLocation.class);
                            intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK | Intent.FLAG_ACTIVITY_NEW_TASK);
                            startActivity(intent);
                        }
                    }
                });

            }
        }

    }

	void setupActionBar(String header) {
		
		findViewById(R.id.imgIcon).setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View v) {
				finish();
			}
		});
		
		TextView txtHeader = (TextView) findViewById(R.id.txtHeader);
		txtHeader.setText(header);
		
	}
	
	@Override
    public boolean dispatchTouchEvent(MotionEvent event) {
        View view = getCurrentFocus();
        boolean ret = super.dispatchTouchEvent(event);

        if (view instanceof EditText) {
            View w = getCurrentFocus();
            int scrcoords[] = new int[2];
            w.getLocationOnScreen(scrcoords);
            float x = event.getRawX() + w.getLeft() - scrcoords[0];
            float y = event.getRawY() + w.getTop() - scrcoords[1];
            
            if (event.getAction() == MotionEvent.ACTION_UP
			     && (x < w.getLeft() || x >= w.getRight() 
			     || y < w.getTop() || y > w.getBottom()) ) { 
                InputMethodManager imm = (InputMethodManager)getSystemService(Context.INPUT_METHOD_SERVICE);
                imm.hideSoftInputFromWindow(getWindow().getCurrentFocus().getWindowToken(), 0);
            }
        }
     return ret;
    }

}