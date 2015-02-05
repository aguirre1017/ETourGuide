package com.thesis.etourguide;

import com.parse.ParseClassName;
import com.parse.ParseObject;
import com.parse.ParseQuery;
import com.parse.ParseUser;

/**
 * Data model for a rating.
 */
@ParseClassName("Rating")
public class Rating extends ParseObject {

    public String getTouristId() {
        return getString("TouristId");
    }

    public void setTouristId(String touristId) {
        put("TouristId", touristId);
    }

    public String getDestinationId() {
        return getString("DestinationId");
    }

    public void setDestinationId(String destinationId) {
        put("DestinationId", destinationId);
    }

    public int getRate() {
        return getInt("Rate");
    }

    public void setRate(int rate) {
        put("Rate", rate);
    }

    public String getComment() {
        return getString("Comment");
    }

    public void setComment(String comment) {
        put("Comment", comment);
    }

    public ParseUser getUser() {
        return getParseUser("user");
    }

    public void setUser(ParseUser value) {
        put("user", value);
    }

    public static ParseQuery<Rating> getQuery() {
        return ParseQuery.getQuery(Rating.class);
    }

}