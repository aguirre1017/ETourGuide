package com.thesis.etourguide;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.os.StrictMode;
import android.view.MotionEvent;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.Window;
import android.view.inputmethod.InputMethodManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

import com.parse.ParseException;
import com.parse.ParseUser;
import com.parse.SignUpCallback;
import com.thesis.etourguide.utility.AlertDialogManager;
import com.thesis.etourguide.utility.Utility;

public class RegisterActivity extends Activity {
	
	AlertDialogManager alert = new AlertDialogManager();
	
	Button btnRegister;
    EditText inputFullName;
    EditText inputAddress;
    EditText inputAge;
    EditText inputEmail;
    EditText inputPassword;

    String name, address, age, email, password;
    
    @SuppressLint("NewApi")
	@Override
    public void onCreate(Bundle savedInstanceState) {
    	super.onCreate(savedInstanceState);
    	this.requestWindowFeature(Window.FEATURE_NO_TITLE);
        setContentView(R.layout.main_register);
        
        if (android.os.Build.VERSION.SDK_INT > 9) {
            StrictMode.ThreadPolicy policy = new StrictMode.ThreadPolicy.Builder().permitAll().build();
            StrictMode.setThreadPolicy(policy);
        }

        setupActionBar("Register");

        findViewById(R.id.btnRegister).setOnClickListener(new View.OnClickListener() {
            public void onClick(View view) {
                register();
            }
        });

        findViewById(R.id.txtSignIn).setOnClickListener(new Button.OnClickListener() {
	        public void onClick(View v) {
                Intent in = new Intent(getApplicationContext(), LoginActivity.class);
                startActivity(in);
                finish();
            }
        });

        inputFullName = (EditText) findViewById(R.id.registerName);
        inputFullName.setFocusable(true);
    }

    void register() {
        if (Utility.isNetworkAvailable(this)) {

            inputFullName = (EditText) findViewById(R.id.registerName);
            inputAddress = (EditText) findViewById(R.id.registerAddress);
            inputAge = (EditText) findViewById(R.id.registerAge);
            inputEmail = (EditText) findViewById(R.id.registerEmail);
            inputPassword = (EditText) findViewById(R.id.registerPassword);

            name = inputFullName.getText().toString();
            address = inputAddress.getText().toString();
            age = inputAge.getText().toString();
            email = inputEmail.getText().toString();
            password = inputPassword.getText().toString();

            if (name.equalsIgnoreCase("")) {
                alert.showAlertDialog(RegisterActivity.this, "", "Please enter your name.", false);
            }else if (address.equalsIgnoreCase("")) {
                alert.showAlertDialog(RegisterActivity.this, "", "Please enter your address.", false);
            }else if (age.equalsIgnoreCase("")) {
                alert.showAlertDialog(RegisterActivity.this, "", "Please enter your age.", false);
            }else if (email.equalsIgnoreCase("")) {
                alert.showAlertDialog(RegisterActivity.this, "", "Please enter your email address.", false);
            }else if (password.equalsIgnoreCase("")) {
                alert.showAlertDialog(RegisterActivity.this, "", "Please enter your password.", false);
            }else {
                if (!Utility.isInputValid(email, "^[\\w\\.-]+@([\\w\\-]+\\.)+[A-Z]{2,4}$")) {
                    alert.showAlertDialog(RegisterActivity.this, "", "Email address is invalid.", false);
                }else if (!Utility.isInputValid(password, "^[a-zA-Z0-9]{6,}$")) {
                    alert.showAlertDialog(RegisterActivity.this, "", "Password must be minumum of 6 characters.", false);
                }else {

                    // Set up a progress dialog
                    final ProgressDialog dialog = new ProgressDialog(RegisterActivity.this);
                    dialog.setMessage("Registering your account...");
                    dialog.show();

                    // Set up a new Parse user
                    ParseUser user = new ParseUser();
                    user.put("Name", name);
                    user.put("Address", address);
                    user.put("Age", age);
                    user.setUsername(email);
                    user.setPassword(password);

                    // Call the Parse signup method
                    user.signUpInBackground(new SignUpCallback() {
                        @Override
                        public void done(ParseException e) {
                            dialog.dismiss();
                            if (e != null) {
                                // Show the error message
                                alert.showAlertDialog(RegisterActivity.this, "", e.getMessage(), false);
                            } else {
                                // Start an intent for the dispatch activity
                                Intent intent = new Intent(RegisterActivity.this, MainHomeActivity.class);
                                intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK | Intent.FLAG_ACTIVITY_NEW_TASK);
                                startActivity(intent);
                            }
                        }
                    });

                }
            }
        }
        else
            alert.showAlertDialog(RegisterActivity.this, "", getResources().getString(R.string.utility_network_notavailable), false);
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