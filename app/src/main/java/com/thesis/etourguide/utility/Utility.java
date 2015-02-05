package com.thesis.etourguide.utility;

import android.annotation.SuppressLint;
import android.content.Context;
import android.graphics.Shader.TileMode;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.Drawable;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.text.format.DateFormat;
import android.util.Log;
import android.view.View;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

@SuppressLint("SimpleDateFormat")
public class Utility {
	
	public static void fixBackgroundRepeat(View view) {
	      Drawable bg = view.getBackground();
	      if(bg != null) {
	           if(bg instanceof BitmapDrawable) {
	                BitmapDrawable bmp = (BitmapDrawable) bg;
	                bmp.mutate(); // make sure that we aren't sharing state anymore
	                bmp.setTileModeXY(TileMode.REPEAT, TileMode.REPEAT);
	           }
	      }
	 }
	
	public static boolean isNetworkAvailable (Context context){
		ConnectivityManager connectivityManager = (ConnectivityManager) context.getSystemService(Context.CONNECTIVITY_SERVICE);
			NetworkInfo activeNetworkInfo = connectivityManager.getActiveNetworkInfo();
		return activeNetworkInfo != null && activeNetworkInfo.isConnected();
	}
	
	public static boolean isInputValid(String input, String expression) {
	    boolean isValid = false;
	    CharSequence inputStr = input;

	    Pattern pattern = Pattern.compile(expression, Pattern.CASE_INSENSITIVE);
	    Matcher matcher = pattern.matcher(inputStr);
	    if (matcher.matches()) {
	        isValid = true;
	    }
	    return isValid;
	}
	
	public static String formatDate(String inputDate, String format) {
		SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
		String dateFormated = "";
		
		try {
			Date dDate = dateFormat.parse(inputDate);
			dateFormated = (String) DateFormat.format(format, dDate);
		} catch (ParseException e) {
			e.printStackTrace();
		}
		return dateFormated;

	}
	
	public static String getCurrentDateTime() {
		SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
    	String currentDateandTime = sdf.format(new Date());
    	return currentDateandTime;
	}
	
	public static Date formatDateReturnDate(String inputDate, String format) {
		SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
		Date dateFormated = null;
		
		try {
			Date dDate = dateFormat.parse(inputDate);
			dateFormated = (Date) DateFormat.format(format, dDate);
		} catch (ParseException e) {
			Log.e("format date", e.toString());
			e.printStackTrace();
		}
		return dateFormated;

	}

}