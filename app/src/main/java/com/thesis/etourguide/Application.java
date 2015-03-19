package com.thesis.etourguide;

import android.content.Context;
import android.content.SharedPreferences;
import android.text.TextUtils;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.toolbox.ImageLoader;
import com.android.volley.toolbox.Volley;
import com.parse.Parse;
import com.parse.ParseObject;
import com.thesis.etourguide.utility.LruBitmapCache;

public class Application extends android.app.Application {
  // Debugging switch
  public static final boolean APPDEBUG = false;

  // Debugging tag for the application
  public static final String APPTAG = "ETourGuide";

  // Used to pass location from MainActivity to PostActivity
  public static final String INTENT_EXTRA_LOCATION = "location";

  // Key for saving the search distance preference
  private static final String KEY_SEARCH_DISTANCE = "searchDistance";

  private static final float DEFAULT_SEARCH_DISTANCE = 250.0f;

  private static SharedPreferences preferences;

  private static ConfigHelper configHelper;

  private RequestQueue mRequestQueue;

  private ImageLoader mImageLoader;

  private static Application mInstance;

  public Application() {
  }

  @Override
  public void onCreate() {
    super.onCreate();

    mInstance = this;

    ParseObject.registerSubclass(Destination.class);
    ParseObject.registerSubclass(Survey.class);
    ParseObject.registerSubclass(Rating.class);

    Parse.initialize(this, "BcXm7BiRThwY5rb7PsbQe5M6bQWmQr0qKYFBkFXH",
              "t4bIMJdaCi8W6ylD7BUSXT9TqDwmSNdfbMKhCWgZ");

    preferences = getSharedPreferences("com.thesis.etourguide", Context.MODE_PRIVATE);

    configHelper = new ConfigHelper();
    configHelper.fetchConfigIfNeeded();
    }

    public static synchronized Application getInstance() {
        return mInstance;
    }

    public static float getSearchDistance() {
    return preferences.getFloat(KEY_SEARCH_DISTANCE, DEFAULT_SEARCH_DISTANCE);
    }

    public static ConfigHelper getConfigHelper() {
    return configHelper;
    }

    public static void setSearchDistance(float value) {
    preferences.edit().putFloat(KEY_SEARCH_DISTANCE, value).commit();
    }

    public RequestQueue getRequestQueue() {
        if (mRequestQueue == null) {
            mRequestQueue = Volley.newRequestQueue(getApplicationContext());
        }

        return mRequestQueue;
    }

    public ImageLoader getImageLoader() {
        getRequestQueue();
        if (mImageLoader == null) {
            mImageLoader = new ImageLoader(this.mRequestQueue,
                    new LruBitmapCache());
        }
        return this.mImageLoader;
    }

    public <T> void addToRequestQueue(Request<T> req, String tag) {
        // set the default tag if tag is empty
        req.setTag(TextUtils.isEmpty(tag) ? APPTAG : tag);
        getRequestQueue().add(req);
    }

    public <T> void addToRequestQueue(Request<T> req) {
        req.setTag(APPTAG);
        getRequestQueue().add(req);
    }

    public void cancelPendingRequests(Object tag) {
        if (mRequestQueue != null) {
            mRequestQueue.cancelAll(tag);
        }
    }

}
