package it.natter.dao;


import android.content.Context;
import android.database.Cursor;
import android.database.DatabaseUtils;
import android.database.sqlite.SQLiteDatabase;

import java.util.ArrayList;
import java.util.List;

import it.natter.classes.Contact;
import it.natter.classes.MessageNatter;
import it.natter.utility.Hardware;

public class Dao {

    public static void intAfterLogoutDb(SQLiteDatabase db){
        //SETTINGS
        db.execSQL(DataBaseHelper.DROP_TABLE_OPTION);
        db.execSQL(DataBaseHelper.CREATE_TABLE_OPTION);
        //PROFILE
        db.execSQL(DataBaseHelper.DROP_TABLE_PROFILE);
        db.execSQL(DataBaseHelper.CREATE_TABLE_PROFILE);
        //MESSAGE
        db.execSQL(DataBaseHelper.DROP_TABLE_MESSAGE);
        db.execSQL(DataBaseHelper.CREATE_TABLE_MESSAGE);
        //VOICE
        db.execSQL(DataBaseHelper.DROP_TABLE_VOICE);
        db.execSQL(DataBaseHelper.CREATE_TABLE_VOICE);
    }

    /*************************************FRAGMENT TABLE*************************************/
    public static void updateDestroy(SQLiteDatabase db, String id,boolean flag){
        DestroyFragmentOption.updateDestroy(db,id,flag);
    }

	public static boolean isDestroy(SQLiteDatabase db,String id){
        Cursor c = db.query(true,DestroyFragmentOption.TABLE,DestroyFragmentOption.COLUMNS, null, null, null, null, null, null);
        c.moveToFirst();
        while(c.isAfterLast() == false){
            if(c.getString(0).equals(id)){
                if(c.getString(1).equals("1"))  return true;
                else return false;
            }
            c.moveToNext();
        }
        c.close();

        return false;
	}

    public static void initDestroyFragment(SQLiteDatabase db){
        db.execSQL(DataBaseHelper.DROP_TABLE_FRAGMENT);
        db.execSQL(DataBaseHelper.CREATE_TABLE_FRAGMENT);

        DestroyFragmentOption.insertDestroy(db,"map_user",false);
        DestroyFragmentOption.insertDestroy(db,"map",false);
    }
    /*************************************FRAGMENT TABLE*************************************/

    /**************************************SETTING TABLE*************************************/
    public static void insertPositionSetting(SQLiteDatabase db, boolean flag){
        AppSetting.insertPositionSetting(db, flag);
    }

    public static void insertRealtimeSetting(SQLiteDatabase db, boolean flag){
        AppSetting.insertRealtimeSetting(db, flag);
    }
    public static void insertBootSetting(SQLiteDatabase db, boolean flag){
        AppSetting.insertBootSetting(db, flag);
    }

    public static void updatePositionSetting(SQLiteDatabase db, boolean flag){
        AppSetting.updatePositionSetting(db, flag);
    }

    public static void updateRealtimeSetting(SQLiteDatabase db, boolean flag){
        AppSetting.updateRealtimeSetting(db, flag);
    }

    public static void updateBootSetting(SQLiteDatabase db, boolean flag){
        AppSetting.updateBootSetting(db, flag);
    }

    public static int getPositionSetting(SQLiteDatabase db){
        return AppSetting.getPositionSetting(db);
    }

    public static int getRealtimeSetting(SQLiteDatabase db){
        return AppSetting.getRealtimeSetting(db);
    }

    public static int getBootSetting(SQLiteDatabase db){
        return AppSetting.getBootSetting(db);
    }
    /**************************************SETTING TABLE*************************************/

    /***************************************PROFILE TABLE***************************************/
    public static void insertProfile(SQLiteDatabase db,String[] data,boolean[] flag){
        ProfileInfo.insertProfile(db, data, flag);
    }

    public static String getPasswordProfile(SQLiteDatabase db){
        return ProfileInfo.getPassword(db);
    }

    public static int getIdProfile(SQLiteDatabase db){
        return ProfileInfo.getId(db);
    }

    public static String getEmailProfile(SQLiteDatabase db){
        return ProfileInfo.getEmail(db);
    }

    public static String getNameProfile(SQLiteDatabase db){
        return ProfileInfo.getName(db);
    }

    public static String getSurnameProfile(SQLiteDatabase db){
        return ProfileInfo.getSurname(db);
    }

    public static String getPhoneProfile(SQLiteDatabase db){
        return ProfileInfo.getPhone(db);
    }

    public static int getEmailEditableProfile(SQLiteDatabase db){
        return ProfileInfo.getEditableEmail(db);
    }

    public static int getPhoneEditableProfile(SQLiteDatabase db){
        return ProfileInfo.getEditablePhone(db);
    }

    public static String getProfile(SQLiteDatabase db){
        return ProfileInfo.getProfile(db);
    }

    public static void initProfile(SQLiteDatabase db){
        db.execSQL(DataBaseHelper.DROP_TABLE_PROFILE);
        db.execSQL(DataBaseHelper.CREATE_TABLE_PROFILE);
    }

    public static boolean isEmptyProfileTable(SQLiteDatabase db){
        return (getEmailProfile(db).equals("-1"));
    }
    /***************************************PROFILE TABLE***************************************/

    /****************************************INFO TABLE*****************************************/
    public static void insertInfo(SQLiteDatabase db,String latitude,String longitude,String ip,String gmc){
        AppInfo.insertInfo(db,latitude,longitude,ip,gmc);
    }

    public static void insertIdGmc(SQLiteDatabase db,String gmc){
        AppInfo.insertIdGmc(db, gmc);
    }

    public static void updatePosition(SQLiteDatabase db,String latitude,String longitude){
        AppInfo.updatePosition(db,latitude,longitude);
    }

    public static void updateIp(SQLiteDatabase db,String ip){
        AppInfo.updateIp(db, ip);
    }

    public static void updateIdGmc(SQLiteDatabase db,String gmc){
        AppInfo.updateId_Gcm(db, gmc);
    }

    public static String getLatitude(SQLiteDatabase db){
        return AppInfo.getLatitude(db);
    }

    public static String getLongitude(SQLiteDatabase db){
        return AppInfo.getLongitude(db);
    }

    public static String getIp(SQLiteDatabase db){
        return AppInfo.getIp(db);
    }

    public static String getInfo(SQLiteDatabase db){
        return AppInfo.getInfo(db);
    }

    public static String getIdGmc(SQLiteDatabase db){
        return AppInfo.getId_Gmc(db);
    }

    public static boolean isEmptyInfoTable(SQLiteDatabase db){
        return AppInfo.isEmpty(db);
    }

    public static void initInfo(SQLiteDatabase db){
        db.execSQL(DataBaseHelper.DROP_TABLE_INFO);
        db.execSQL(DataBaseHelper.CREATE_TABLE_INFO);
    }
    /****************************************INFO TABLE*****************************************/

    /***************************************RUBRIC TABLE****************************************/
    public static void insertContact(SQLiteDatabase db,String[] data){
        Rubric.insertContact(db, data);
    }

    public static void updateContact(SQLiteDatabase db,String id,String[] data){
        Rubric.updateContact(db,id,data);
    }

    public static String getIdNatterContact(SQLiteDatabase db,String id){
        return Rubric.getIdNatter(db,id);
    }

    public static String getIdPhoneContact(SQLiteDatabase db,String id){
        return Rubric.getIdPhone(db,id);
    }

    public static String getEmailContact(SQLiteDatabase db,String id){
        return Rubric.getEmail(db,id);
    }

    public static String getNameContact(SQLiteDatabase db,String id){
        return Rubric.getName(db,id);
    }

    public static String getSurnameContact(SQLiteDatabase db,String id){
        return Rubric.getSurname(db,id);
    }

    public static String getAllNameByNatterId(SQLiteDatabase db,String id){
        return Rubric.getAllNameByNatterId(db,id);
    }

    public static String getPhoneContact(SQLiteDatabase db,String id){
        return Rubric.getPhone(db,id);
    }

    public static int getErasableContact(SQLiteDatabase db,String id){
        return Rubric.getErasable(db, id);
    }

    public static void updateEmailContact(SQLiteDatabase db,String id,String email){
        Rubric.updateEmail(db,id,email);
    }

    public static void updateNameContact(SQLiteDatabase db,String id,String name){
        Rubric.updateName(db,id,name);
    }

    public static void updateSurnameContact(SQLiteDatabase db,String id,String surname){
        Rubric.updateSurname(db,id,surname);
    }

    public static void updatePhoneContact(SQLiteDatabase db,String id,String phone){
        Rubric.updatePhone(db,id,phone);
    }

    public static void updateErasableContact(SQLiteDatabase db,String id,int flag){
        Rubric.updateErasable(db, id, flag);
    }

    public static String getRubric(SQLiteDatabase db){
        return Rubric.getRubric(db);
    }

    public static boolean ifExistSignInNatter(SQLiteDatabase db, String email){
        return Rubric.ifExistSignInNatter(db, email);
    }

    public static boolean ifExist(SQLiteDatabase db, String id_phone){
        return Rubric.ifExist(db, id_phone);
    }

    public static List<Contact> getListRubric(SQLiteDatabase db, Context context){
        ArrayList<Contact> list = new ArrayList<Contact>();

        Cursor c = Rubric.getCursorRubric(db,getPhoneProfile(db));

        if(c.getCount() > 0){
            while(c.moveToNext()){
                Contact temp = new Contact();
                temp.setId_natter(c.getString(0));
                temp.setId_phone(c.getString(1));
                temp.setName(c.getString(3));
                temp.setSurname(c.getString(4));
                temp.setEmail(c.getString(2));
                temp.setNumber(c.getString(5));
                if(c.getInt(6)==0){
                    temp.setErasable(false);
                }
                else{
                    temp.setErasable(true);
                }

                list.add(temp);
            }
        }

        c.close();

        return list;
    }

    public static Contact getContact(SQLiteDatabase db, String id_phone, Context context){
        Cursor c = Rubric.getContact(db,id_phone);

        Contact temp = new Contact();

        if(c.getCount() > 0){
            if(c.moveToNext()){
                temp.setId_natter(c.getString(0));
                temp.setId_phone(c.getString(1));
                temp.setName(c.getString(3));
                temp.setSurname(c.getString(4));
                temp.setEmail(c.getString(2));
                temp.setNumber(c.getString(5));
                temp.setPhoto(Hardware.getUserImage(context,temp.getId_phone()));
                if(c.getInt(6)==0){
                    temp.setErasable(false);
                }
                else{
                    temp.setErasable(true);
                }
            }
        }

        c.close();

        return temp;
    }

    public static Contact getContactByNatterId(SQLiteDatabase db, String id_natter, Context context){
        Cursor c = Rubric.getContactIdNatter(db,id_natter);

        Contact temp = new Contact();

        if(c.getCount() > 0){
            if(c.moveToNext()){
                temp.setId_natter(c.getString(0));
                temp.setId_phone(c.getString(1));
                temp.setName(c.getString(3));
                temp.setSurname(c.getString(4));
                temp.setEmail(c.getString(2));
                temp.setNumber(c.getString(5));
                temp.setPhoto(Hardware.getUserImage(context,temp.getId_phone()));
                if(c.getInt(6)==0){
                    temp.setErasable(false);
                }
                else{
                    temp.setErasable(true);
                }
            }
        }

        c.close();

        return temp;
    }

    public static String getIdPhoneFromIdNatter(SQLiteDatabase db, String id_natter){
        return Rubric.getIdPhoneFromIdNatter(db,id_natter);
    }

    public static ArrayList<String> getListOnline(SQLiteDatabase db){
        ArrayList<String> list = new ArrayList<String>();

        Cursor c = Rubric.getCursorOnline(db,getPhoneProfile(db));

        if(c.getCount() > 0){
            while(c.moveToNext()){
                list.add(c.getString(0));
            }
        }

        c.close();

        return list;
    }

    public static List<Contact> getListNotOnline(SQLiteDatabase db){
        ArrayList<Contact> list = new ArrayList<Contact>();

        Cursor c = Rubric.getCursorNotOnline(db,getPhoneProfile(db));

        if(c.getCount() > 0){
            while(c.moveToNext()){
                Contact temp = new Contact();
                temp.setId_natter(c.getString(0));
                temp.setId_phone(c.getString(1));
                temp.setName(c.getString(3));
                temp.setSurname(c.getString(4));
                temp.setEmail(c.getString(2));
                temp.setNumber(c.getString(5));
                if(c.getInt(6)==0){
                    temp.setErasable(false);
                }
                else{
                    temp.setErasable(true);
                }

                list.add(temp);
            }
        }

        c.close();

        return list;
    }

    public static int countOnline(SQLiteDatabase db){
        Cursor c = Rubric.getCursorOnline(db,getPhoneProfile(db));
        int count = c.getCount();
        c.close();

        return count;
    }

    public static void deletContact(SQLiteDatabase db, String id){
        Rubric.deletContact(db,id);
    }

    public static void initRubric(SQLiteDatabase db){
        db.execSQL(DataBaseHelper.DROP_TABLE_RUBRIC);
        db.execSQL(DataBaseHelper.CREATE_TABLE_RUBRIC);
    }
    /***************************************RUBRIC TABLE****************************************/

    /*****************************************TASK TABLE****************************************/
    public static void initTaskInfo(SQLiteDatabase db){
        TaskInfo.updateRefreshContact(db, true);
        TaskInfo.updateRefreshOnline(db, true);
        TaskInfo.updateContact(db,true);
    }

    public static boolean lockContactTaskInfo(SQLiteDatabase db){
        if(TaskInfo.getContact(db)==-1){
            TaskInfo.insertContact(db,false);
            return true;
        }
        else if(TaskInfo.getContact(db)==1){
            TaskInfo.updateContact(db, false);
            return true;
        }

        return false;
    }

    public static boolean isLockedContactTaskInfo(SQLiteDatabase  db){
        return TaskInfo.getContact(db)==0;
    }

    public static void unlockContactTaskInfo(SQLiteDatabase  db){
        TaskInfo.updateContact(db,true);
    }

    public static void updateLastIdTaskInfo(SQLiteDatabase db, int id){
        TaskInfo.updateLastId(db,id);
    }

    public static void updateCountOnlineTaskInfo(SQLiteDatabase db, int count){
        TaskInfo.updateCountOnline(db,count);
    }

    public static void setRefreshContactTaskInfo(SQLiteDatabase db){
        TaskInfo.updateRefreshContact(db, true);
    }

    public static void resetRefreshContactTaskInfo(SQLiteDatabase db){
        TaskInfo.updateRefreshContact(db,false);
    }

    public static boolean hasToRefreshContactTaskInfo(SQLiteDatabase  db){
        return TaskInfo.getRefreshContact(db)==1;
    }

    public static void setRefreshOnlineTaskInfo(SQLiteDatabase db){
        TaskInfo.updateRefreshOnline(db, true);
    }

    public static void resetRefreshOnlineTaskInfo(SQLiteDatabase db){
        TaskInfo.updateRefreshOnline(db,false);
    }

    public static boolean hasToRefreshOnlineTaskInfo(SQLiteDatabase  db){
        return TaskInfo.getRefreshOnline(db)==1;
    }

    public static int getLastIdTaskInfo(SQLiteDatabase db){
        return TaskInfo.getLastId(db);
    }

    public static int getCountOnlineTaskInfo(SQLiteDatabase db){
        return TaskInfo.getCountOnline(db);
    }

    public static boolean isFirst(SQLiteDatabase db){
        return (Math.abs(TaskInfo.getFirst(db))==1);
    }

    public static boolean isRestart(SQLiteDatabase db){
        return TaskInfo.getRestart(db)==1;
    }

    public static void setRestart(SQLiteDatabase db){
        TaskInfo.insertRestart(db);
    }

    public static void resetRestart(SQLiteDatabase db){
        TaskInfo.updateRestart(db, 0);
    }

    public static void resetFirst(SQLiteDatabase db){
        TaskInfo.resetFirst(db);
    }

    public static String getTaskInfo(SQLiteDatabase db){
        return TaskInfo.getTaskInfo(db);
    }
    /*****************************************TASK TABLE****************************************/

    /***************************************MESSAGE TABLE***************************************/
    public static String inserMessage(SQLiteDatabase  db,MessageNatter mex){
        String natter_id = Integer.toString(getIdProfile(db));

        String[] data = new String[5];

        if(mex.getSender().equals(natter_id)) {
            data[0] = mex.getSender()+"-"+mex.getReceiver();
        }
        else{
            data[0] = mex.getReceiver()+"-"+mex.getSender();
        }

        data[1] = mex.getSender();
        data[2] = mex.getReceiver();
        data[3] = mex.getMessage();
        data[4] = mex.getTimestamp();

        Message_Table.insertMessage(db,data);

        return data[0];
    }


    public static ArrayList<MessageNatter> getAllMessageByIdConversation(SQLiteDatabase  db,String id){
        ArrayList<MessageNatter> messages = new ArrayList<MessageNatter>();

        Cursor c = Message_Table.getAllMessageByIdConversation(db, id);

        if(c.getCount() > 0){
            while(c.moveToNext()){
                messages.add(new MessageNatter(c.getString(0),c.getString(1),c.getString(2),c.getString(3)));
            }
        }

        c.close();

        return messages;
    }

    public static ArrayList<String> getIdNatterNotRead(SQLiteDatabase  db){
        ArrayList<String> contact = new ArrayList<String>();

        Cursor c = Message_Table.getIdNotRead(db);
        if(c.getCount() > 0) {

            String[] temp;

            while (c.moveToNext()) {
                temp = c.getString(0).split("-");
                contact.add(temp[1]);
            }
        }

        c.close();

        return contact;
    }

    public static ArrayList<String> getIdNatterRead(SQLiteDatabase  db){
        ArrayList<String> contact = new ArrayList<String>();

        Cursor c = Message_Table.getIdRead(db);
        if(c.getCount() > 0) {

            String[] temp;

            while (c.moveToNext()) {
                temp = c.getString(0).split("-");
                contact.add(temp[1]);
            }
        }

        c.close();

        return contact;
    }

    public static void setReadConversation(SQLiteDatabase  db,String id){
        Message_Table.updateRead(db,id,1);
    }

    public static String getAllMessageTable(SQLiteDatabase db){
        return DatabaseUtils.dumpCursorToString(Message_Table.getAllTable(db));
    }

    /***************************************MESSAGE TABLE***************************************/

    /***************************************VOICE TABLE***************************************/
    public static void insertVoice(SQLiteDatabase  db,String sender){
        if(!Voice_Table.senderExist(db,sender)){
            Voice_Table.insertVoice(db,sender);
        }
    }

    public static ArrayList<String> getIdNatterVoice(SQLiteDatabase  db){
        ArrayList<String> contact = new ArrayList<String>();

        Cursor c = Voice_Table.getAllTable(db);

        if(c.getCount() > 0) {
            while (c.moveToNext()) {
                contact.add(c.getString(0));
            }
        }

        c.close();

        return contact;
    }

    /***************************************VOICE TABLE***************************************/
}
