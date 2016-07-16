package it.natter.task;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;

import it.natter.classes.Position;
import it.natter.dao.Dao;
import it.natter.dao.DataBaseHelper;
import it.natter.rest.UserService;
import it.natter.utility.Hardware;

/**
 * Created by francesco on 13/04/14.
 */
public class InfoOperation {

    public static void updateInfoBoot(final Context context) {

        final String[] position = Hardware.getLocation(context);

        new Thread(new Runnable() {
            @Override
            public void run() {

                String ip = Hardware.getIPAddress(true);

                DataBaseHelper databaseHelper = new DataBaseHelper(context);
                final SQLiteDatabase db = databaseHelper.getWritableDatabase();

                if (Dao.getBootSetting(db) == 1) {
                    if(!Dao.isEmptyInfoTable(db)) {

                        if(!(position[0].equals("error")&&position[1].equals("error"))) {
                            Dao.updatePosition(db, position[0], position[1]);
                        }

                        Dao.updateIp(db, ip);
                    }

                    if ((Dao.getPositionSetting(db) == 1) && (Hardware.isNetworkAvailable(context))) {
                        if(!(position[0].equals("error")&&position[1].equals("error"))) {
                            UserService.updatePosition(new Position((new Integer(Dao.getIdProfile(db))).toString(), position[0], position[1]));
                        }
                    }
                }

                db.close();
            }

            }).start();
    }

    public static void updateInfoRunning(final Context context){

        final String[] position = Hardware.getLocation(context);

        new Thread(new Runnable() {
            @Override
            public void run(){
                String ip = Hardware.getIPAddress(true);

                DataBaseHelper databaseHelper = new DataBaseHelper(context);
                final SQLiteDatabase db = databaseHelper.getWritableDatabase();

                if(!Dao.isEmptyInfoTable(db)){
                    if(!(position[0].equals("error")&&position[1].equals("error"))) {
                        Dao.updatePosition(db, position[0], position[1]);
                    }
                    Dao.updateIp(db,ip);
                }

                if((Dao.getPositionSetting(db) == 1)&&(Hardware.isNetworkAvailable(context))){
                    if(!(position[0].equals("error")&&position[1].equals("error"))) {
                        UserService.updatePosition(new Position((new Integer(Dao.getIdProfile(db))).toString(), position[0], position[1]));
                    }
                }

                db.close();

            }
        }).start();
    }

}
