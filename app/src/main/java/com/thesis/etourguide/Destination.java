package com.thesis.etourguide;

import com.parse.ParseClassName;
import com.parse.ParseGeoPoint;
import com.parse.ParseObject;
import com.parse.ParseQuery;

/**
 * Data model for a destination.
 */
@ParseClassName("Destination")
public class Destination extends ParseObject {

    public String getName() {
        return getString("Name");
    }

    public void setName(String name) {
        put("Name", name);
    }

    public String getDescription() {
        return getString("Description");
    }

    public void setDescription(String description) {
        put("Description", description);
    }

    public Double getRate() {
        return getDouble("Rate");
    }

    public void setRate(Double rate) {
        put("Rate", rate);
    }

    public String getImageURL() {
        return getString("Image");
    }

    public void setImageURL(String imageURL) {
        put("Image", imageURL);
    }

    public ParseGeoPoint getMapLocation() {
        return getParseGeoPoint("Map");
    }

    public void setMapLocation(ParseGeoPoint value) {
        put("Map", value);
    }

    public String getCategory() {
        return getString("Category");
    }

    public void setCategory(String category) {
        put("Category", category);
    }

    public String getLocation() {
        return getString("Location");
    }

    public void setLocation(String location) {
        put("Location", location);
    }

    public static ParseQuery<Destination> getQuery() {
        return ParseQuery.getQuery(Destination.class);
    }
}
