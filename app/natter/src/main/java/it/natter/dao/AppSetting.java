package it.natter.dao;

import android.content.ContentValues;
import android.database.Cursor;
import android.database.DatabaseUtils;
import android.database.SQLException;
import android.database.sqlite.SQLiteDatabase;

public class AppSetting{
	
	public static final String POSITION = "POSITION";
	public static final String REALTIME = "REALTIME";
    public static final String BOOT = "BOOT";
	public static final String[] COLUMNS = new String[]{POSITION,REALTIME,BOOT};

    public static final String TABLE = "setting";
	
	public static void updatePositionSetting(SQLiteDatabase db,boolean flag){
		ContentValues v = new ContentValues();
		v.put(POSITION,flag);
        db.update(TABLE,v,null, null);
	}

    public static void updateRealtimeSetting(SQLiteDatabase db,boolean flag){
        ContentValues v = new ContentValues();
        v.put(REALTIME,flag);
        db.update(TABLE,v,null, null);
    }

    public static void updateBootSetting(SQLiteDatabase db,boolean flag){
        ContentValues v = new ContentValues();
        v.put(BOOT,flag);
        db.update(TABLE,v,null, null);
    }

    public static void insertPositionSetting(SQLiteDatabase db,boolean flag){
        ContentValues v = new ContentValues();
        v.put(POSITION,flag);
        db.insert(TABLE, null, v);
    }

    public static void insertRealtimeSetting(SQLiteDatabase db,boolean flag){
        ContentValues v = new ContentValues();
        v.put(REALTIME,flag);
        db.insert(TABLE, null, v);
    }

    public static void insertBootSetting(SQLiteDatabase db,boolean flag){
        ContentValues v = new ContentValues();
        v.put(BOOT,flag);
        db.insert(TABLE, null, v);
    }

    public static int getPositionSetting(SQLiteDatabase db) throws SQLException{
        Cursor c = db.query(true, TABLE, COLUMNS, null, null, null, null, null, null);
        if(c.getCount()>0){
            if(c != null){
                c.moveToFirst();
                int temp = c.getInt(0);
                c.close();
                return temp;
            }
        }
        c.close();

        return -1;
    }

    public static int getRealtimeSetting(SQLiteDatabase db) throws SQLException{
        Cursor c = db.query(true, TABLE, COLUMNS, null, null, null, null, null, null);
        if(c.getCount()>0){
            if(c != null){
                c.moveToFirst();
                int temp = c.getInt(1);
                c.close();
                return temp;
            }
        }
        c.close();
        return -1;
    }

	public static int getBootSetting(SQLiteDatabase db) throws SQLException{
		Cursor c = db.query(true, TABLE, COLUMNS, null, null, null, null, null, null);
        if(c.getCount()>0){
            if(c != null){
                c.moveToFirst();
                int temp = c.getInt(2);
                c.close();
                return temp;
            }
        }
        c.close();
        return -1;
	}

    public static String getSetting(SQLiteDatabase db) throws SQLException{
        Cursor c = db.query(true, TABLE, COLUMNS, null, null, null, null, null, null);
        if(c != null){
            c.moveToFirst();
        }
        String temp = DatabaseUtils.dumpCursorToString(c);
        c.close();
        return temp;
    }

    public static boolean deleteSettingTable(SQLiteDatabase db){
        return db.delete(TABLE, null, null) > 0;
    }

}
