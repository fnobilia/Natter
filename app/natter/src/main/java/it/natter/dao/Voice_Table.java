package it.natter.dao;

import android.content.ContentValues;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;

public class Voice_Table {

    public static final String SENDER = "SENDER";

	public static final String[] COLUMNS = new String[]{SENDER};

    public static final String TABLE = "voice";

    public static void insertVoice(SQLiteDatabase db,String sender){
        ContentValues v = new ContentValues();
        v.put(SENDER, sender);

        db.insert(TABLE, null, v);
    }

    public static boolean senderExist(SQLiteDatabase db,String sender){
        String[] column = {SENDER};
        Cursor c = db.query(true,TABLE,column,SENDER+" = ? ",new String[]{sender},null,null,null,null);
        if(c.getCount()>0){
            if(c != null){
                c.moveToFirst();
                String temp = c.getString(0);
                c.close();
                return true;
            }
        }
        c.close();

        return false;
    }

    public static Cursor getAllTable(SQLiteDatabase db){
        return db.query(true, TABLE, COLUMNS, null,null, null, null,null, null);
    }
}
