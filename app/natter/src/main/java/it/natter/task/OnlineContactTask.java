package it.natter.task;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.os.AsyncTask;

import java.util.Iterator;
import java.util.List;

import it.natter.classes.Contact;
import it.natter.classes.ContactService;
import it.natter.classes.Esito;
import it.natter.dao.Dao;
import it.natter.dao.DataBaseHelper;
import it.natter.rest.ComunicationService;
import it.natter.tab.CustomOnlineAdapter;
import it.natter.utility.Code;
import it.natter.utility.Hardware;

/**
 * Created by francesco on 06/04/14.
 */
public class OnlineContactTask extends AsyncTask<String,Void,SQLiteDatabase> {

    private Context context;
    private CustomOnlineAdapter adapter;

    public OnlineContactTask(Context context, CustomOnlineAdapter adapter){
        this.context = context;
        this.adapter = adapter;
    }

    protected SQLiteDatabase doInBackground(String... params){

        DataBaseHelper databaseHelper = new DataBaseHelper(this.context);
        final SQLiteDatabase db = databaseHelper.getWritableDatabase();

        this.lockDb(db);

        final List<Contact> list = Dao.getListNotOnline(db);

        Dao.unlockContactTaskInfo(db);

        final Iterator<Contact> iterator = list.iterator();

        Contact contact;

        while(iterator.hasNext()){

            contact = iterator.next();

            Esito esito = ComunicationService.isUser(contact);

            if (esito.isFlag()) {
                ContactService contact_service = (ContactService) esito.getResult();
                contact.setId_natter(contact_service.getId_natter());
                contact.setEmail(contact_service.getEmail());
                contact.setName(contact_service.getName());
                contact.setSurname(contact_service.getSurname());
                contact.setNumber(contact_service.getPhone());

                if(!contact_service.getPhoto().equals(Code.NOT_STATED)){
                    contact.setPhoto(Hardware.fromStringToImage(contact_service.getPhoto()));
                } else {
                    contact.setHasPhoto(false);
                }

                contact.setErasable(false);

                Hardware.deleteUserImage(contact.getId_phone());

                this.lockDb(db);

                if(Dao.ifExistSignInNatter(db, contact.getEmail())){
                    Dao.deletContact(db,contact.getId_phone());
                }
                else{
                    Dao.updateContact(db, contact.getId_phone(), contact.stringArray());
                    Hardware.saveContactImage(contact);
                }

                Dao.setRefreshContactTaskInfo(db);
                Dao.setRefreshOnlineTaskInfo(db);

                Dao.unlockContactTaskInfo(db);

                if(this.adapter!=null){
                    this.adapter.addItem(contact.getId_phone());
                }
            }
        }

       return db;
    }

    protected void onPostExecute(final SQLiteDatabase db){
        Dao.setRefreshContactTaskInfo(db);
        Dao.setRefreshOnlineTaskInfo(db);

        Dao.unlockContactTaskInfo(db);

        db.close();
    }

    private void lockDb(SQLiteDatabase db){
        if(!Dao.lockContactTaskInfo(db)){

            while(Dao.isLockedContactTaskInfo(db)){
                /*try{
                    Thread.sleep(5);
                }
                catch(InterruptedException e){
                    Log.e("ContactTask",e.toString()+" - "+e.getMessage());
                }*/
            }

            Dao.lockContactTaskInfo(db);

        }
    }

}
