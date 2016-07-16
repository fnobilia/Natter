package it.natter.gcm;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.os.AsyncTask;
import android.util.Log;

import com.google.android.gms.gcm.GoogleCloudMessaging;

import java.io.IOException;

import it.natter.dao.Dao;
import it.natter.dao.DataBaseHelper;
import it.natter.utility.Code;
import it.natter.utility.Hardware;

/**
 * Created by francesco on 15/04/14.
 */
public class GMCRegistration{

    private static GoogleCloudMessaging gcm;
    private static String regId;
    private static Context context;

    public static void registerGCM(Context c){
        context = c;
        registerInBackground();
    }

    private static void registerInBackground() {
            new AsyncTask<Void, Void, String>() {
                @Override
                protected String doInBackground(Void... params) {
                    try {
                        DataBaseHelper databaseHelper = new DataBaseHelper(context);
                        final SQLiteDatabase db = databaseHelper.getWritableDatabase();

                        if(Dao.isEmptyInfoTable(db)){
                            gcm = GoogleCloudMessaging.getInstance(context);
                            regId = gcm.register(Code.GOOGLE_PROJECT_ID);

                            String[] position = Hardware.getLocation(context);

                            Dao.insertInfo(db,position[0],position[1],Hardware.getIPAddress(true),regId);
                        }
                        else{
                            regId = "The value already exists -> "+Dao.getIdGmc(db);
                        }
                        db.close();

                    } catch (IOException ex) {
                        Log.e("registerGCM",ex.toString()+" - "+ex.getMessage());
                    }

                    if(regId!=null) {
                        return regId;
                    }
                    else{
                        registerGCM(context);
                    }

                    return null;
                }

                @Override
                protected void onPostExecute(String msg) {
                    //Log.e("registerInBackground", "RegId: " + msg);
                }
            }.execute(null, null, null);
    }

}
