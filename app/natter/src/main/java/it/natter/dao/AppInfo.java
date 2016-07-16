package it.natter.dao;

import android.content.ContentValues;
import android.database.Cursor;
import android.database.DatabaseUtils;
import android.database.SQLException;
import android.database.sqlite.SQLiteDatabase;

public class AppInfo{

    public static final String ID = "ID";
	public static final String LATITUDE = "LATITUDE";
	public static final String LONGITUDE = "LONGITUDE";
    public static final String IP = "IP";
    public static final String ID_GCM = "ID_GCM";
	public static final String[] COLUMNS = new String[]{ID,LATITUDE,LONGITUDE,IP,ID_GCM};

    public static final String TABLE = "info";
	
	public static void updatePosition(SQLiteDatabase db,String latitude,String longitude){
		ContentValues v = new ContentValues();
		v.put(LATITUDE,latitude);
        v.put(LONGITUDE,longitude);
        db.update(TABLE,v,ID+" = 1", null);
	}

    public static void updateIp(SQLiteDatabase db,String ip){
        ContentValues v = new ContentValues();
        v.put(IP,ip);
        db.update(TABLE,v,ID+" = 1", null);
    }

    public static void updateId_Gcm(SQLiteDatabase db,String gmc){
        ContentValues v = new ContentValues();
        v.put(ID_GCM,gmc);
        db.update(TABLE,v,ID+" = 1", null);
    }

    public static void insertInfo(SQLiteDatabase db,String latitude,String longitude,String ip,String gmc){
        ContentValues v = new ContentValues();
        v.put(LATITUDE,latitude);
        v.put(LONGITUDE,longitude);
        v.put(IP,ip);
        v.put(ID_GCM,gmc);
        db.insert(TABLE, null, v);
    }

    public static void insertIdGmc(SQLiteDatabase db,String gmc){
        ContentValues v = new ContentValues();
        v.put(ID_GCM,gmc);
        db.insert(TABLE, null, v);
    }

    public static String getId_Gmc(SQLiteDatabase db) throws SQLException{
        String[] column = {ID_GCM};
        Cursor c = db.query(true, TABLE, column, null, null, null, null, null, null);
        if(c.getCount()>0){
            if(c != null){
                c.moveToFirst();
                String temp = c.getString(0);
                c.close();
                return temp;
            }
        }
        c.close();

        return "null";
    }

    public static String getLatitude(SQLiteDatabase db) throws SQLException{
        String[] column = {LATITUDE};
        Cursor c = db.query(true, TABLE, column, null, null, null, null, null, null);
        if(c.getCount()>0){
            if(c != null){
                c.moveToFirst();
                String temp = c.getString(0);
                c.close();
                return temp;
            }
        }
        c.close();

        return "null";
    }

    public static String getLongitude(SQLiteDatabase db) throws SQLException{
        String[] column = {LONGITUDE};
        Cursor c = db.query(true, TABLE, column, null, null, null, null, null, null);
        if(c.getCount()>0){
            if(c != null){
                c.moveToFirst();
                String temp = c.getString(0);
                c.close();
                return temp;
            }
        }
        c.close();

        return "null";
    }

    public static String getIp(SQLiteDatabase db) throws SQLException{
        String[] column = {IP};
        Cursor c = db.query(true, TABLE, column, null, null, null, null, null, null);
        if(c.getCount()>0){
            if(c != null){
                c.moveToFirst();
                String temp = c.getString(0);
                c.close();
                return temp;
            }
        }
        c.close();

        return "null";
    }

    public static String getInfo(SQLiteDatabase db) throws SQLException{
        Cursor c = db.query(true, TABLE, COLUMNS, null, null, null, null, null, null);
        if(c != null){
            c.moveToFirst();
        }
        String temp = DatabaseUtils.dumpCursorToString(c);
        c.close();
        return temp;
    }

    public static boolean isEmpty(SQLiteDatabase db){
        Cursor c = db.query(true, TABLE, COLUMNS, null, null, null, null, null, null);
        boolean flag = c.moveToNext();
        c.close();
        return !flag;
    }

    public static boolean deleteAppInfoTable(SQLiteDatabase db){
        return db.delete(TABLE, null, null) > 0;
    }

}
