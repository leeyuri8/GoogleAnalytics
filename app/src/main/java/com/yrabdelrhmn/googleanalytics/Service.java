package com.yrabdelrhmn.googleanalytics;

import android.os.Bundle;

import com.google.firebase.analytics.FirebaseAnalytics;

import java.sql.Time;
import java.util.Calendar;
import java.util.Date;
import java.util.TimeZone;

public class Service {
    public Date calender(){
        Calendar c = Calendar.getInstance(TimeZone.getTimeZone("UTC"));
        return c.getTime();
    }
    public void selectContent(FirebaseAnalytics firebaseAnalytics,String id, String name,String contentType){
        Bundle bundle = new Bundle();
        bundle.putString(FirebaseAnalytics.Param.ITEM_ID,id);
        bundle.putString(FirebaseAnalytics.Param.ITEM_NAME,name);
        bundle.putString(FirebaseAnalytics.Param.CONTENT_TYPE,contentType);
       firebaseAnalytics.logEvent(FirebaseAnalytics.Event.SELECT_CONTENT,bundle);
    }
    public  void trackScreen(FirebaseAnalytics firebaseAnalytics,String scName){
        Bundle bundle = new Bundle();
        bundle.putString(FirebaseAnalytics.Param.SCREEN_NAME, scName);
        bundle.putString(FirebaseAnalytics.Param.SCREEN_CLASS, "MainActivity");
        firebaseAnalytics.logEvent(FirebaseAnalytics.Event.SCREEN_VIEW, bundle);
    }


}
