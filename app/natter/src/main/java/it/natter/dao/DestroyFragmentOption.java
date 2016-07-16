package it.natter.dao;

import android.content.ContentValues;
import android.database.Cursor;
import android.database.DatabaseUtils;
import android.database.SQLException;
import android.database.sqlite.SQLiteDatabase;

public class DestroyFragmentOption{

	public static final String ID_FRAGMENT = "ID_FRAGMENT";
	public static final String FLAG = "FLAG";
	public static final String[] COLUMNS = new String[]{ID_FRAGMENT, FLAG};

    public static final String TABLE = "fragment";
	
	public static void updateDestroy(SQLiteDatabase db,String idFragment, boolean flag){
		ContentValues v = new ContentValues();
		v.put(FLAG,flag);
        db.update(TABLE,v,ID_FRAGMENT+" = '"+idFragment+"'", null);
	}

    public static void insertDestroy(SQLiteDatabase db,String idFragment, boolean flag){
        ContentValues v = new ContentValues();
        v.put(ID_FRAGMENT,idFragment);
        v.put(FLAG,flag);
        db.insert(TABLE, null, v);
    }

	public static boolean deleteFragmentTable(SQLiteDatabase db){
        return db.delete(TABLE, null, null) > 0;
    }

	public static String getDestroyFragment(SQLiteDatabase db) throws SQLException{
		Cursor c = db.query(true, TABLE, COLUMNS, null, null, null, null, null, null);
		if(c != null){
			c.moveToFirst();
		}
        String temp = DatabaseUtils.dumpCursorToString(c);
        c.close();
        return temp;
	}

}
