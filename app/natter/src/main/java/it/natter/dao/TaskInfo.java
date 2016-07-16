package it.natter.dao;

import android.content.ContentValues;
import android.database.Cursor;
import android.database.DatabaseUtils;
import android.database.SQLException;
import android.database.sqlite.SQLiteDatabase;

public class TaskInfo {

    public static final String ID = "ID";
	public static final String CONTACT = "CONTACT"; //If 0 is used otherwise you can use it
    public static final String REFRESH_CONTACT = "REFRESH_CONTACT"; //if 1 have to refresh contact
    public static final String REFRESH_ONLINE = "REFRESH_ONLINE"; //if 1 have to refresh contact
    public static final String COUNT_ONLINE = "COUNT_ONLINE"; //if 1 have to refresh contact
	public static final String LAST_ID = "LAST_ID";
    public static final String FIRST = "FIRST";
    public static final String RESTART = "RESTART";
	public static final String[] COLUMNS = new String[]{ID,CONTACT,REFRESH_CONTACT,REFRESH_ONLINE,COUNT_ONLINE,LAST_ID,FIRST,RESTART};

    public static final String TABLE = "taskinfo";

    public static void insertContact(SQLiteDatabase db,boolean flag){
        ContentValues v = new ContentValues();
        if(flag)
            v.put(CONTACT,1);
        else{
            v.put(CONTACT,0);
        }
        db.insert(TABLE,null, v);
    }

	public static void updateContact(SQLiteDatabase db,boolean flag){
		ContentValues v = new ContentValues();
        if(flag)
		    v.put(CONTACT,1);
        else{
            v.put(CONTACT,0);
        }
        db.update(TABLE,v,"ID=1", null);
	}

    public static void updateLastId(SQLiteDatabase db,int id){
        ContentValues v = new ContentValues();
        v.put(LAST_ID,id);
        db.update(TABLE,v,"ID=1", null);
    }

    public static void updateRefreshContact(SQLiteDatabase db,boolean flag){
        ContentValues v = new ContentValues();
        if(flag)
            v.put(REFRESH_CONTACT,1);
        else
            v.put(REFRESH_CONTACT,0);
        db.update(TABLE,v,"ID=1", null);
    }

    public static void updateRefreshOnline(SQLiteDatabase db,boolean flag){
        ContentValues v = new ContentValues();
        if(flag)
            v.put(REFRESH_ONLINE,1);
        else
            v.put(REFRESH_ONLINE,0);
        db.update(TABLE,v,"ID=1", null);
    }

    public static void updateCountOnline(SQLiteDatabase db,int count){
        ContentValues v = new ContentValues();
        v.put(COUNT_ONLINE,count);
        db.update(TABLE,v,"ID=1", null);
    }

    public static void resetFirst(SQLiteDatabase db){
        ContentValues v = new ContentValues();
        v.put(FIRST,0);
        db.update(TABLE,v,"ID=1", null);
    }

    public static int getCountOnline(SQLiteDatabase db){
        String[] column = {COUNT_ONLINE};
        Cursor c = db.query(true,TABLE,column,null,null,null,null,null,null);
        if(c.getCount()>0){
            if(c != null){
                c.moveToFirst();
                int temp = c.getInt(0);
                c.close();
                return temp;
            }
        }

        return -1;
    }

    public static void insertRestart(SQLiteDatabase db){
        ContentValues v = new ContentValues();
        v.put(RESTART,1);
        db.insert(TABLE,null,v);
    }

    public static int getRestart(SQLiteDatabase db){
        String[] column = {RESTART};
        Cursor c = db.query(true,TABLE,column,null,null,null,null,null,null);
        if(c.getCount()>0){
            if(c != null){
                c.moveToFirst();
                int temp = c.getInt(0);
                c.close();
                return temp;
            }
        }

        return -1;
    }

    public static void updateRestart(SQLiteDatabase db,int value){
        ContentValues v = new ContentValues();
        v.put(RESTART,value);
        db.update(TABLE,v,"ID=1", null);
    }

    public static int getContact(SQLiteDatabase db){
        String[] column = {CONTACT};
        Cursor c = db.query(true,TABLE,column,null,null,null,null,null,null);
        if(c.getCount()>0){
            if(c != null){
                c.moveToFirst();
                int temp = c.getInt(0);
                c.close();
                return temp;
            }
        }

        return -1;
    }

    public static int getFirst(SQLiteDatabase db){
        String[] column = {FIRST};
        Cursor c = db.query(true,TABLE,column,null,null,null,null,null,null);

        if(c.getCount()>0){
            if(c != null){
                c.moveToFirst();
                int temp = c.getInt(0);
                c.close();
                return temp;
            }
        }

        return -1;
    }

    public static int getLastId(SQLiteDatabase db){
        String[] column = {LAST_ID};
        Cursor c = db.query(true,TABLE,column,null,null,null,null,null,null);
        if(c.getCount()>0){
            if(c != null){
                c.moveToFirst();
                int temp = c.getInt(0);
                c.close();
                return temp;
            }
        }

        return -1;
    }

    public static int getRefreshContact(SQLiteDatabase db){
        String[] column = {REFRESH_CONTACT};
        Cursor c = db.query(true,TABLE,column,null,null,null,null,null,null);
        if(c.getCount()>0){
            if(c != null){
                c.moveToFirst();
                int temp = c.getInt(0);
                c.close();
                return temp;
            }
        }

        return -1;
    }

    public static int getRefreshOnline(SQLiteDatabase db){
        String[] column = {REFRESH_ONLINE};
        Cursor c = db.query(true,TABLE,column,null,null,null,null,null,null);
        if(c.getCount()>0){
            if(c != null){
                c.moveToFirst();
                int temp = c.getInt(0);
                c.close();
                return temp;
            }
        }

        return -1;
    }

    public static String getTaskInfo(SQLiteDatabase db) throws SQLException{
        Cursor c = db.query(true, TABLE, COLUMNS, null, null, null, null, null, null);
        if(c != null){
            c.moveToFirst();
        }
        String temp = DatabaseUtils.dumpCursorToString(c);
        c.close();
        return temp;
    }

    public static boolean deleteTaskInfoTable(SQLiteDatabase db){
        return db.delete(TABLE, null, null) > 0;
    }

}
