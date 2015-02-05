package com.thesis.etourguide;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Locale;

import android.app.Dialog;
import android.content.Intent;
import android.location.Address;
import android.location.Geocoder;
import android.os.Bundle;
import android.support.v4.app.DialogFragment;
import android.support.v4.app.FragmentActivity;
import android.util.Log;
import android.view.View;
import android.view.Window;
import android.view.View.OnClickListener;
import android.widget.ImageView;
import android.widget.TextView;

import com.parse.ParseUser;
import com.thesis.etourguide.utility.ActionItem;
import com.thesis.etourguide.utility.GPSTracker;

import com.google.android.gms.common.ConnectionResult;
import com.google.android.gms.common.GooglePlayServicesUtil;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.BitmapDescriptorFactory;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.Marker;
import com.google.android.gms.maps.model.MarkerOptions;
import com.thesis.etourguide.utility.QuickAction;

public class MainMapActivity extends FragmentActivity {

    private GoogleMap mMap;

    GPSTracker gps;

    private static final int ID_LOGOUT = 0;

    private Double latitude, longitude;

    private String name, description;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        this.requestWindowFeature(Window.FEATURE_NO_TITLE);
        setContentView(R.layout.main_map);

        name = getIntent().getExtras().getString("name");
        description = getIntent().getExtras().getString("description");
        latitude = getIntent().getExtras().getDouble("latitude");
        longitude = getIntent().getExtras().getDouble("longitude");

        setupActionBar();
        setupMap();

    }

    void setupActionBar() {

        findViewById(R.id.imgIcon).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });

        TextView txtHeader = (TextView) findViewById(R.id.txtHeader);
        txtHeader.setText(name);

    }

    private void setupMap() {
        if (servicesConnected()) {
            if (mMap == null) {
                mMap = ((SupportMapFragment) getSupportFragmentManager().findFragmentById(R.id.map)).getMap();
                if (mMap != null) {

                    final LatLng currLocation = new LatLng(latitude, longitude);

                    mMap.moveCamera(CameraUpdateFactory.newLatLngZoom(currLocation, 10));

                    MarkerOptions marker = new MarkerOptions()
                            .position(currLocation)
                            .title(name)
                            .snippet(description);
                    marker.icon(BitmapDescriptorFactory.defaultMarker(BitmapDescriptorFactory.HUE_RED));

                    Marker myMarker = mMap.addMarker(marker);
                    myMarker.showInfoWindow();
                    mMap.setMyLocationEnabled(true);

                }
            }
        }
    }

    String getAddress(LatLng currLocation) {

        Geocoder geocoder = new Geocoder(this, Locale.getDefault());
        List <Address> addresses = null;

        try {
            addresses = geocoder.getFromLocation(currLocation.latitude, currLocation.longitude, 1);

        } catch (IOException exception1) {
            Log.e("", exception1.toString());
        } catch (IllegalArgumentException exception2) {
            Log.e("", exception2.toString());
        }

        if (addresses != null && addresses.size() > 0) {
            Address address = addresses.get(0);

            String addressText = getString(R.string.address_output_string,
                    address.getMaxAddressLineIndex() > 0 ?
                            address.getAddressLine(0) : "",
                    address.getLocality(),
                    address.getCountryName()
            );

            return addressText;
        } else {
            return "";
        }
    }

    private boolean servicesConnected() {
        int resultCode = GooglePlayServicesUtil.isGooglePlayServicesAvailable(this);

        if (ConnectionResult.SUCCESS == resultCode) {
            return true;
        } else {
            Dialog dialog = GooglePlayServicesUtil.getErrorDialog(resultCode, this, 0);
            if (dialog != null) {
                ErrorDialogFragment errorFragment = new ErrorDialogFragment();
                errorFragment.setDialog(dialog);
                errorFragment.show(getSupportFragmentManager(), "My Location");
            }
            return false;
        }
    }

    public static class ErrorDialogFragment extends DialogFragment {
        private Dialog mDialog;

        public ErrorDialogFragment() {
            super();
            mDialog = null;
        }

        public void setDialog(Dialog dialog) {
            mDialog = dialog;
        }

        @Override
        public Dialog onCreateDialog(Bundle savedInstanceState) {
            return mDialog;
        }
    }

}