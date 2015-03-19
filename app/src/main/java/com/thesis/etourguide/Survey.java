package com.thesis.etourguide;

import com.parse.ParseClassName;
import com.parse.ParseObject;
import com.parse.ParseQuery;

/**
 * Data model for a Survey.
 */
@ParseClassName("Survey")
public class Survey extends ParseObject {

    public String getActivityType() {
        return getString("ActivityType");
    }

    public void setActivityType(String activityType) {
        put("ActivityType", activityType);
    }

    public static ParseQuery<Survey> getQuery() {
        return ParseQuery.getQuery(Survey.class);
    }
}
