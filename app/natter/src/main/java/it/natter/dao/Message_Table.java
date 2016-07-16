package it.natter.dao;

import android.content.ContentValues;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;

public class Message_Table {

    public static final String ID = "ID";  //Corrente - amico
    public static final String SENDER = "SENDER";
    public static final String RECEIVER = "RECEIVER";
    public static final String MEX = "MEX";
    public static final String TIME = "TIME";
    public static final String READ = "READ";
	public static final String TIME_SQL = "TIME_SQL";
	public static final String[] COLUMNS = new String[]{ID,SENDER,RECEIVER,MEX,TIME,READ,TIME_SQL};

    public static final String TABLE = "message";

    public static void insertMessage(SQLiteDatabase db,String[] data){
        ContentValues v = new ContentValues();
        for(int i=0;i<(COLUMNS.length-2);i++){
            v.put(COLUMNS[i], data[i]);
        }

        db.insert(TABLE, null, v);
    }

    public static Cursor getAllMessageByIdConversation(SQLiteDatabase db,String id){
        String[] column = {SENDER,RECEIVER,MEX,TIME};
        Cursor c = db.query(true, TABLE, column, ID+" = ? ",new String[]{id}, null, null,TIME_SQL+" ASC", null);
        return c;
    }

    public static Cursor getIdNotRead(SQLiteDatabase db){
        String[] column = {ID};
        Cursor c = db.query(true, TABLE, column, READ+" = 0",null, null, null,null, null);
        return c;
    }

    public static Cursor getIdRead(SQLiteDatabase db){
        String[] column = {ID};
        Cursor c = db.query(true, TABLE, column, READ+" = 1",null, null, null,null, null);
        return c;
    }

    public static void updateRead(SQLiteDatabase db,String id, int value){
        ContentValues v = new ContentValues();
        v.put(READ,1);
        db.update(TABLE,v,ID+" = ? ", new String[]{id});
    }

    public static Cursor getAllTable(SQLiteDatabase db){
        return db.query(true, TABLE, COLUMNS, null,null, null, null,null, null);
    }
}
