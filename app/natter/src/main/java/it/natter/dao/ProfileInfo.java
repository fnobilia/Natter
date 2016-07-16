package it.natter.dao;

import android.content.ContentValues;
import android.database.Cursor;
import android.database.DatabaseUtils;
import android.database.SQLException;
import android.database.sqlite.SQLiteDatabase;

public class ProfileInfo {

    public static final String ID = "ID";
    public static final String PASSWORD = "PASSWORD";
	public static final String EMAIL = "EMAIL";
    public static final String EDIT_EMAIL = "EDIT_EMAIL";
	public static final String NAME = "NAME";
    public static final String EDIT_NAME = "EDIT_NAME";
    public static final String SURNAME = "SURNAME";
    public static final String EDIT_SURNAME = "EDIT_SURNAME";
    public static final String PHONE = "PHONE";
    public static final String EDIT_PHONE = "EDIT_PHONE";
	public static final String[] COLUMNS = new String[]{ID,PASSWORD,EMAIL,EDIT_EMAIL,NAME,EDIT_NAME,SURNAME,EDIT_SURNAME,PHONE,EDIT_PHONE};

    public static final String TABLE = "profile";

    public static void insertProfile(SQLiteDatabase db,String[] data,boolean[] flag){
        ContentValues v = new ContentValues();
        int f = 0;
        int d = 0;
        for(int i=0;i<COLUMNS.length;i++){
            if(i<2){
                v.put(COLUMNS[i], data[d]);
                d++;
            }
            else{
                v.put(COLUMNS[i], data[d]);
                d++;
                i++;
                v.put(COLUMNS[i], flag[f]);
                f++;
            }
        }
        db.insert(TABLE, null, v);
    }

    public static int getId(SQLiteDatabase db){
        String[] column = {ID};
        Cursor c = db.query(true,TABLE,column,null,null,null,null,null,null);
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

    public static void updatePassword(SQLiteDatabase db,String password){
        ContentValues v = new ContentValues();
        v.put(PASSWORD,password);
        db.update(TABLE,v,"ID=1", null);
    }

    public static String getPassword(SQLiteDatabase db){
        String[] column = {PASSWORD};
        Cursor c = db.query(true,TABLE,column,null,null,null,null,null,null);
        if(c.getCount()>0){
            if(c != null){
                c.moveToFirst();
                String temp = c.getString(0);
                c.close();
                return temp;
            }
        }
        c.close();

        return "-1";
    }

    public static int getEditableEmail(SQLiteDatabase db){
        String[] column = {EDIT_EMAIL};
        Cursor c = db.query(true,TABLE,column,null,null,null,null,null,null);
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

	public static int updateEmail(SQLiteDatabase db,String email,boolean editable){
        int flag = getEditableEmail(db);
        if(flag == 1){
            ContentValues v = new ContentValues();
            v.put(EMAIL,email);
            v.put(EDIT_EMAIL,editable);
            db.update(TABLE,v,"ID=1", null);
        }

        return flag;
	}

    public static int getEditablePhone(SQLiteDatabase db){
        String[] column = {EDIT_PHONE};
        Cursor c = db.query(true,TABLE,column,null,null,null,null,null,null);
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

    public static int updatePhone(SQLiteDatabase db,String phone,boolean editable){
        int flag = getEditablePhone(db);
        if(flag == 1){
            ContentValues v = new ContentValues();
            v.put(PHONE,phone);
            v.put(EDIT_PHONE,editable);
            db.update(TABLE,v,"ID=1", null);
        }

        return flag;
    }

    public static int getEditableName(SQLiteDatabase db){
        String[] column = {EDIT_NAME};
        Cursor c = db.query(true,TABLE,column,null,null,null,null,null,null);
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

    public static int updateName(SQLiteDatabase db,String name,boolean editable){
        int flag = getEditableName(db);
        if(flag == 1){
            ContentValues v = new ContentValues();
            v.put(NAME,name);
            v.put(EDIT_NAME,editable);
            db.update(TABLE,v,"ID=1", null);
        }

        return flag;
    }

    public static int getEditableSurname(SQLiteDatabase db){
        String[] column = {EDIT_SURNAME};
        Cursor c = db.query(true,TABLE,column,null,null,null,null,null,null);
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

    public static int updateSurname(SQLiteDatabase db,String surname,boolean editable){
        int flag = getEditableName(db);
        if(flag == 1){
            ContentValues v = new ContentValues();
            v.put(SURNAME,surname);
            v.put(EDIT_SURNAME,editable);
            db.update(TABLE,v,"ID=1", null);
        }

        return flag;
    }

    public static String getEmail(SQLiteDatabase db) throws SQLException{
        String[] column = {EMAIL};
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

        return "-1";
    }

    public static String getName(SQLiteDatabase db) throws SQLException{
        String[] column = {NAME};
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

        return "-1";
    }

    public static String getSurname(SQLiteDatabase db) throws SQLException{
        String[] column = {SURNAME};
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

        return "-1";
    }

    public static String getPhone(SQLiteDatabase db) throws SQLException{
        String[] column = {PHONE};
        Cursor c = db.query(true, TABLE, column, null, null, null, null, null, null);
        if(c.getCount()>0){
            if(c != null){
                c.moveToFirst();
                String temp = c.getString(0);
                c.close();
                return temp;
            }
        }

        return "-1";
    }

    public static String getProfile(SQLiteDatabase db) throws SQLException{
        Cursor c = db.query(true, TABLE, COLUMNS, null, null, null, null, null, null);
        if(c != null){
            c.moveToFirst();
        }
        String temp = DatabaseUtils.dumpCursorToString(c);
        c.close();
        return temp;
    }

    public static boolean deleteProfileTable(SQLiteDatabase db){
        return db.delete(TABLE, null, null) > 0;
    }

}
