package it.natter.task;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;

import it.natter.classes.Access;
import it.natter.dao.Dao;
import it.natter.dao.DataBaseHelper;
import it.natter.rest.ComunicationService;
import it.natter.utility.Code;

/**
 * Created by francesco on 30/04/14.
 */
public class UpdateAccess {

    public static void go(final Context context){
        new Thread(new Runnable() {
            @Override
            public void run() {
                DataBaseHelper databaseHelper = new DataBaseHelper(context);
                final SQLiteDatabase db = databaseHelper.getWritableDatabase();

                ComunicationService.saveLastAccess(new Access(Integer.toString(Dao.getIdProfile(db)),""));

                db.close();
            }
        }).start();
    }

    public static void recording(final Context context){
        new Thread(new Runnable() {
            @Override
            public void run() {
                DataBaseHelper databaseHelper = new DataBaseHelper(context);
                final SQLiteDatabase db = databaseHelper.getWritableDatabase();

                ComunicationService.saveLastAccess(new Access(Integer.toString(Dao.getIdProfile(db)), Code.RECORD));

                db.close();
            }
        }).start();
    }

    public static void writing(final Context context){
        new Thread(new Runnable() {
            @Override
            public void run() {
                DataBaseHelper databaseHelper = new DataBaseHelper(context);
                final SQLiteDatabase db = databaseHelper.getWritableDatabase();

                ComunicationService.saveLastAccess(new Access(Integer.toString(Dao.getIdProfile(db)), Code.WRITE));

                db.close();
            }
        }).start();
    }

}
