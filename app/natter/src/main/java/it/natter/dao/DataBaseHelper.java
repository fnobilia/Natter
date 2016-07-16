package it.natter.dao;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

public class DataBaseHelper extends SQLiteOpenHelper{

    public static final String NAME_DB = "natter_db";
    public static final int VERSION_DB = 1;

    public static final String CREATE_TABLE_FRAGMENT =
            "CREATE TABLE IF NOT EXISTS "+DestroyFragmentOption.TABLE+" (" +
                    DestroyFragmentOption.ID_FRAGMENT+" TEXT PRIMARY KEY, "+
                    DestroyFragmentOption.FLAG+" INTEGER DEFAULT 0"+" ) ; ";

    public static final String CREATE_TABLE_OPTION =
            "CREATE TABLE IF NOT EXISTS "+ AppSetting.TABLE+" ( ID INTEGER PRIMARY KEY AUTOINCREMENT NOT NULL, " +
                    AppSetting.POSITION+" INTEGER DEFAULT 0, "+
                    AppSetting.REALTIME+" INTEGER DEFAULT 0, "+
                    AppSetting.BOOT+" INTEGER DEFAULT 0"+" ) ; ";

    public static final String CREATE_TABLE_PROFILE =
            "CREATE TABLE IF NOT EXISTS "+ ProfileInfo.TABLE+" ( " +
                    ProfileInfo.ID+" INTEGER PRIMARY KEY, "+
                    ProfileInfo.PASSWORD+" TEXT NOT NULL, "+
                    ProfileInfo.EMAIL+" TEXT NOT NULL, "+
                    ProfileInfo.EDIT_EMAIL+" INTEGER DEFAULT 0, "+
                    ProfileInfo.NAME+" TEXT NOT NULL, "+
                    ProfileInfo.EDIT_NAME+" INTEGER DEFAULT 0, "+
                    ProfileInfo.SURNAME+" TEXT NOT NULL, "+
                    ProfileInfo.EDIT_SURNAME+" INTEGER DEFAULT 0, "+
                    ProfileInfo.PHONE+" TEXT NOT NULL, "+
                    ProfileInfo.EDIT_PHONE+" INTEGER DEFAULT 0 "+" ) ; ";

    public static final String CREATE_TABLE_INFO =
            "CREATE TABLE IF NOT EXISTS "+ AppInfo.TABLE+" ( " +
                    AppInfo.ID+" INTEGER PRIMARY KEY, "+
                    AppInfo.LATITUDE+" TEXT , "+
                    AppInfo.LONGITUDE+" TEXT , "+
                    AppInfo.IP+" TEXT ,"+
                    AppInfo.ID_GCM+" TEXT "+" ) ; ";

    public static final String CREATE_TABLE_RUBRIC =
            "CREATE TABLE IF NOT EXISTS "+ Rubric.TABLE+" ( " +
                    Rubric.ID_NATTER+" TEXT, "+
                    Rubric.ID_PHONE+" TEXT PRIMARY KEY, "+
                    Rubric.EMAIL+" TEXT, "+
                    Rubric.NAME+" TEXT NOT NULL, "+
                    Rubric.SURNAME+" TEXT NOT NULL, "+
                    Rubric.PHONE+" TEXT NOT NULL, "+
                    Rubric.ERASABLE+" INTEGER DEFAULT 0"+" ) ; ";

    public static final String CREATE_TABLE_TASK =
            "CREATE TABLE IF NOT EXISTS "+ TaskInfo.TABLE+" ( " +
                    TaskInfo.ID+" INTEGER PRIMARY KEY, "+
                    TaskInfo.CONTACT+" INTEGER DEFAULT 1, "+
                    TaskInfo.REFRESH_CONTACT+" INTEGER DEFAULT 0, "+
                    TaskInfo.REFRESH_ONLINE+" INTEGER DEFAULT 0, "+
                    TaskInfo.COUNT_ONLINE+" INTEGER DEFAULT 0, "+
                    TaskInfo.LAST_ID+" INTEGER DEFAULT -1, "+
                    TaskInfo.FIRST+" INTEGER DEFAULT 1, "+
                    TaskInfo.RESTART+" INTEGER DEFAULT 0 "+" ) ; ";

    public static final String CREATE_TABLE_MESSAGE =
            "CREATE TABLE IF NOT EXISTS "+ Message_Table.TABLE+" ( " +
                    Message_Table.ID+" TEXT, "+
                    Message_Table.SENDER+" TEXT, "+
                    Message_Table.RECEIVER+" TEXT, "+
                    Message_Table.MEX+" TEXT, "+
                    Message_Table.TIME+" TEXT, "+
                    Message_Table.READ+" INTEGER DEFAULT 0, "+
                    Message_Table.TIME_SQL+" DATETIME DEFAULT(STRFTIME('%Y-%m-%d %H:%M:%f', 'NOW')) ," +
                    "PRIMARY KEY ("+Message_Table.ID+","+Message_Table.TIME_SQL+")"+" ) ; ";

    public static final String CREATE_TABLE_VOICE =
            "CREATE TABLE IF NOT EXISTS "+ Voice_Table.TABLE+" ( " +
                    Voice_Table.SENDER+" TEXT PRIMARY KEY "+" ) ; ";

    public static final String DROP_TABLE_FRAGMENT = "DROP TABLE IF EXISTS "+DestroyFragmentOption.TABLE+" ;";
    public static final String DROP_TABLE_OPTION = "DROP TABLE IF EXISTS "+AppSetting.TABLE+" ;";
    public static final String DROP_TABLE_PROFILE = "DROP TABLE IF EXISTS "+ ProfileInfo.TABLE+" ;";
    public static final String DROP_TABLE_INFO = "DROP TABLE IF EXISTS "+ AppInfo.TABLE+" ;";
    public static final String DROP_TABLE_RUBRIC = "DROP TABLE IF EXISTS "+ Rubric.TABLE+" ;";
    public static final String DROP_TABLE_TASK = "DROP TABLE IF EXISTS "+ TaskInfo.TABLE+" ;";
    public static final String DROP_TABLE_MESSAGE = "DROP TABLE IF EXISTS "+ Message_Table.TABLE+" ;";
    public static final String DROP_TABLE_VOICE = "DROP TABLE IF EXISTS "+ Voice_Table.TABLE+" ;";

    public DataBaseHelper(Context context){
        super(context, NAME_DB, null, VERSION_DB);
    }

    @Override
    public void onCreate(SQLiteDatabase db){
        db.execSQL(CREATE_TABLE_FRAGMENT);
        db.execSQL(CREATE_TABLE_OPTION);
        db.execSQL(CREATE_TABLE_PROFILE);
        db.execSQL(CREATE_TABLE_INFO);
        db.execSQL(CREATE_TABLE_RUBRIC);
        db.execSQL(CREATE_TABLE_TASK);
        db.execSQL(CREATE_TABLE_MESSAGE);
        db.execSQL(CREATE_TABLE_VOICE);
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        db.execSQL(DROP_TABLE_FRAGMENT);
        db.execSQL(DROP_TABLE_OPTION);
        db.execSQL(DROP_TABLE_PROFILE);
        db.execSQL(DROP_TABLE_INFO);
        db.execSQL(DROP_TABLE_RUBRIC);
        db.execSQL(DROP_TABLE_TASK);
        db.execSQL(DROP_TABLE_MESSAGE);
        db.execSQL(DROP_TABLE_VOICE);
        onCreate(db);
    }

}
