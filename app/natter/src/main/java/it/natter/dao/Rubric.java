package it.natter.dao;

import android.content.ContentValues;
import android.database.Cursor;
import android.database.DatabaseUtils;
import android.database.SQLException;
import android.database.sqlite.SQLiteDatabase;

public class Rubric{

    public static final String ID_NATTER = "ID_NATTER";
    public static final String ID_PHONE = "ID_PHONE";
	public static final String EMAIL = "EMAIL";
	public static final String NAME = "NAME";
    public static final String SURNAME = "SURNAME";
    public static final String PHONE = "PHONE";
    public static final String ERASABLE = "ERASABLE";
	public static final String[] COLUMNS = new String[]{ID_NATTER,ID_PHONE,EMAIL,NAME,SURNAME,PHONE,ERASABLE};

    public static final String TABLE = "rubric";

    public static void insertContact(SQLiteDatabase db,String[] data){
        ContentValues v = new ContentValues();
        for(int i=0;i<COLUMNS.length;i++){
            v.put(COLUMNS[i], data[i]);
        }

        db.insert(TABLE, null, v);
    }

    public static void updateContact(SQLiteDatabase db,String id,String[] data){
        ContentValues v = new ContentValues();

        for(int i=0;i<data.length;i++){
            v.put(COLUMNS[i],data[i]);
        }

        db.update(TABLE,v,ID_PHONE+"="+id, null);
    }

    public static String getIdNatter(SQLiteDatabase db,String id){
        String[] column = {ID_NATTER};
        Cursor c = db.query(true,TABLE,column,ID_PHONE+" = ?",new String[]{id},null,null,null,null);
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

    public static String getIdPhone(SQLiteDatabase db,String id){
        String[] column = {ID_PHONE};
        Cursor c = db.query(true,TABLE,column,ID_PHONE+" = ?",new String[]{id},null,null,null,null);
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

    public static String getEmail(SQLiteDatabase db,String id){
        String[] column = {EMAIL};
        Cursor c = db.query(true,TABLE,column,ID_PHONE+" = ?",new String[]{id},null,null,null,null);
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

    public static void updateEmail(SQLiteDatabase db,String id,String email){
        ContentValues v = new ContentValues();
        v.put(EMAIL,email);
        db.update(TABLE,v,ID_PHONE+"="+id, null);
    }

    public static String getName(SQLiteDatabase db,String id){
        String[] column = {NAME};
        Cursor c = db.query(true,TABLE,column,ID_PHONE+" = ?",new String[]{id},null,null,null,null);
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

    public static String getAllNameByNatterId(SQLiteDatabase db,String id){
        String[] column = {NAME,SURNAME};
        Cursor c = db.query(true,TABLE,column,ID_NATTER+" = ?",new String[]{id},null,null,null,null);
        if(c.getCount()>0){
            if(c != null){
                c.moveToFirst();
                String temp = c.getString(0)+" "+c.getString(1);
                c.close();
                return temp;
            }
        }
        c.close();

        return "-1";
    }

    public static void updateName(SQLiteDatabase db,String id,String name){
        ContentValues v = new ContentValues();
        v.put(NAME,name);
        db.update(TABLE,v,ID_PHONE+"="+id, null);
    }

    public static String getSurname(SQLiteDatabase db,String id){
        String[] column = {SURNAME};
        Cursor c = db.query(true,TABLE,column,ID_PHONE+" = ?",new String[]{id},null,null,null,null);
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

    public static void updateSurname(SQLiteDatabase db,String id,String surname){
        ContentValues v = new ContentValues();
        v.put(SURNAME,surname);
        db.update(TABLE,v,ID_PHONE+"="+id, null);
    }

    public static String getPhone(SQLiteDatabase db,String id){
        String[] column = {PHONE};
        Cursor c = db.query(true,TABLE,column,ID_PHONE+" = ?",new String[]{id},null,null,null,null);
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

    public static void updateErasable(SQLiteDatabase db,String id,int erasable){
        ContentValues v = new ContentValues();
        v.put(ERASABLE,erasable);
        db.update(TABLE,v,ID_PHONE+"="+id, null);
    }

    public static int getErasable(SQLiteDatabase db,String id){
        String[] column = {ERASABLE};
        Cursor c = db.query(true,TABLE,column,ID_PHONE+" = ?",new String[]{id},null,null,null,null);
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

    public static void updatePhone(SQLiteDatabase db,String id,String phone){
        ContentValues v = new ContentValues();
        v.put(PHONE,phone);
        db.update(TABLE,v,ID_PHONE+"="+id, null);
    }

    public static String getRubric(SQLiteDatabase db) throws SQLException{
        Cursor c = db.query(true, TABLE, COLUMNS, null, null, null, null, null, null);
        if(c != null){
            c.moveToFirst();
        }
        String temp = DatabaseUtils.dumpCursorToString(c);
        c.close();
        return temp;
    }

    public static boolean deleteRubricTable(SQLiteDatabase db){
        return db.delete(TABLE, null, null) > 0;
    }

    public static Cursor getCursorRubric(SQLiteDatabase db,String phone){
        Cursor c = db.query(true, TABLE, COLUMNS, PHONE+" != ?",new String[]{phone}, null, null,NAME+" ASC", null);
        return c;
    }

    public static Cursor getCursorOnline(SQLiteDatabase db,String phone){
        String[] column = {ID_PHONE};
        Cursor c = db.query(true, TABLE, column, ID_NATTER+" != ? AND "+PHONE+" != ?",new String[]{"",phone}, null, null,NAME+" ASC", null);
        return c;
    }

    public static Cursor getCursorNotOnline(SQLiteDatabase db,String phone){
        Cursor c = db.query(true, TABLE, COLUMNS, ID_NATTER+" = ? AND "+PHONE+" != ?",new String[]{"",phone}, null, null,NAME+" ASC", null);
        return c;
    }

    public static boolean ifExistSignInNatter(SQLiteDatabase db, String email){
        String[] column = {ID_PHONE};
        Cursor c = db.query(true, TABLE, column, ID_NATTER+" != ? AND "+EMAIL+" = ? ",new String[]{"",email}, null, null, null, null);

        boolean flag = c.moveToNext();
        c.close();

        return flag;
    }

    public static boolean ifExist(SQLiteDatabase db, String id_phone){
        String[] column = {ID_PHONE};
        Cursor c = db.query(true, TABLE, column, ID_PHONE+" = ? ",new String[]{id_phone}, null, null, null, null);

        boolean flag = c.moveToNext();
        c.close();

        return flag;
    }

    public static Cursor getContact(SQLiteDatabase db, String id_phone){
        Cursor c = db.query(true, TABLE, COLUMNS, ID_PHONE+" = ? ",new String[]{id_phone}, null, null, null, null);
        return c;
    }

    public static Cursor getContactIdNatter(SQLiteDatabase db, String id_natter){
        Cursor c = db.query(true, TABLE, COLUMNS, ID_NATTER+" = ? ",new String[]{id_natter}, null, null, null, null);
        return c;
    }

    public static String getIdPhoneFromIdNatter(SQLiteDatabase db, String id_natter){
        String[] column = {ID_PHONE};
        Cursor c = db.query(true, TABLE, column, ID_NATTER+" = ? ",new String[]{id_natter}, null, null, null, null);

        if(c != null){
            c.moveToFirst();
            String id_phone = c.getString(0);
            c.close();
            return id_phone;
        }

        c.close();

        return "-1";
    }

    public static void deletContact(SQLiteDatabase db, String id){
        db.delete(TABLE, "ID_NATTER = ?", new String[]{id});
    }

}
