package it.natter.serviceTask;

import android.app.Service;
import android.content.Intent;
import android.database.sqlite.SQLiteDatabase;
import android.os.Handler;
import android.os.IBinder;

import it.natter.dao.Dao;
import it.natter.dao.DataBaseHelper;
import it.natter.task.ContactTask;
import it.natter.task.InfoOperation;
import it.natter.task.OnlineContactTask;
import it.natter.utility.Code;

/**
 * Created by francesco on 14/04/14.
 */
public class RunningService extends Service {

    private Handler handler = new Handler();

    private Runnable managePosition = new Runnable(){
        public void run(){
            InfoOperation.updateInfoRunning(getApplicationContext());
            handler.postDelayed(managePosition, Code.ONE_MINUTE);
        }
    };

    private Runnable updateRubricTask = new Runnable(){
        public void run(){

            DataBaseHelper databaseHelper = new DataBaseHelper(getApplicationContext());
            final SQLiteDatabase db = databaseHelper.getWritableDatabase();

            if(!Dao.isFirst(db)){

                new ContactTask(getApplicationContext(), Code.FIND_NEW).execute("");

                new OnlineContactTask(getApplicationContext(), null).execute("");

            }
            else if (Dao.getLastIdTaskInfo(db) == -1) {

                new ContactTask(getApplicationContext(), Code.UPDATE_ALL).execute("");

            }
        }
    };

    @Override
    public void onCreate(){
        super.onCreate();
        handler.post(managePosition);
        handler.post(updateRubricTask);
    }

    @Override
    public void onDestroy(){
        super.onDestroy();
        handler.removeCallbacks(managePosition);
        handler.removeCallbacks(updateRubricTask);
    }

    @Override
    public IBinder onBind(Intent intent) {
        return null;
    }

    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {
        return Service.START_STICKY;
    }

}