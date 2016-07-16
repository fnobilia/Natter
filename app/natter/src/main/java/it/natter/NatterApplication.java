package it.natter;

import android.app.Application;

/**
 * Created by francesco on 04/05/14.
 */
public class NatterApplication extends Application {

    private static boolean activityVisible = false;

    private static boolean fragmentVoiceVisible = false;

    private static String onLineWith = "";

    public static boolean isActivityVisible() {
        return activityVisible;
    }

    public static void activityResumed(){
        activityVisible = true;
    }

    public static void activityPaused(){
        activityVisible = false;
    }

    public static boolean isFragmentVoiceVisible() {
        return fragmentVoiceVisible;
    }

    public static void fragmentVoiceResumed(){
        fragmentVoiceVisible = true;
    }

    public static void fragmentVoicePaused(){
        fragmentVoiceVisible = false;
    }

    public static void setOnLineWith(String user){
        onLineWith = user;
    }

    public static String getOnLineWith(){
        return onLineWith;
    }

    public static void resetOnLineWith(){
        onLineWith = "";
    }
}