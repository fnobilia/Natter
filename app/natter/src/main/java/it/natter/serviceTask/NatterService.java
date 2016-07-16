package it.natter.serviceTask;

import android.app.AlarmManager;
import android.app.PendingIntent;
import android.app.Service;
import android.content.Context;
import android.content.Intent;
import android.database.sqlite.SQLiteDatabase;
import android.os.Handler;
import android.os.IBinder;
import android.os.SystemClock;

import it.natter.dao.Dao;
import it.natter.dao.DataBaseHelper;
import it.natter.task.ContactTask;
import it.natter.task.InfoOperation;
import it.natter.task.OnlineContactTask;
import it.natter.utility.Code;

/**
 * Created by francesco on 23/03/14.
 */
public class NatterService extends Service{

    //Decidere se fare aggiornare la posizione anche qui (Sicuramente si per la versione della provincia)

    private Handler handler = new Handler();

    private Runnable infoBoot = new Runnable(){
        public void run(){
            InfoOperation.updateInfoBoot(getApplicationContext());
            handler.postDelayed(infoBoot, Code.FIVE_MINUTE);
        }
    };

    private Runnable updateRubricTask = new Runnable(){
        public void run(){

            DataBaseHelper databaseHelper = new DataBaseHelper(getApplicationContext());
            final SQLiteDatabase db = databaseHelper.getWritableDatabase();

            if(!Dao.isLockedContactTaskInfo(db)){
                if (Dao.getLastIdTaskInfo(db) == -1) {

                    new ContactTask(getApplicationContext(), Code.UPDATE_ALL).execute("");

                }
                else {
                    if(!Dao.isRestart(db)) {

                        new OnlineContactTask(getApplicationContext(), null).execute("");

                    }
                    else{

                        Dao.resetRestart(db);

                    }
                }
            }

            handler.postDelayed(updateRubricTask, Code.TWO_HOUR);
        }
    };

    @Override
    public void onCreate(){
        super.onCreate();
        handler.post(infoBoot);
        handler.postDelayed(updateRubricTask, Code.TWO_HOUR);
    }

    @Override
    public void onDestroy(){
        super.onDestroy();
        handler.removeCallbacks(infoBoot);
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

    @Override
    public void onTaskRemoved(Intent rootIntent){
        Intent restartServiceIntent = new Intent(getApplicationContext(), this.getClass());
        restartServiceIntent.setPackage(getPackageName());

        PendingIntent restartServicePendingIntent = PendingIntent.getService(getApplicationContext(), 1, restartServiceIntent, PendingIntent.FLAG_ONE_SHOT);
        AlarmManager alarmService = (AlarmManager) getApplicationContext().getSystemService(Context.ALARM_SERVICE);
        alarmService.set(AlarmManager.ELAPSED_REALTIME,SystemClock.elapsedRealtime() + 1000,restartServicePendingIntent);

        super.onTaskRemoved(rootIntent);
    }

}