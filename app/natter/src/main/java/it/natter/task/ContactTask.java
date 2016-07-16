package it.natter.task;

import android.content.ContentResolver;
import android.content.ContentUris;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Looper;
import android.provider.ContactsContract;

import java.io.FileNotFoundException;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.LinkedList;

import it.natter.classes.Contact;
import it.natter.classes.ContactService;
import it.natter.classes.Esito;
import it.natter.dao.Dao;
import it.natter.dao.DataBaseHelper;
import it.natter.rest.ComunicationService;
import it.natter.utility.Code;
import it.natter.utility.Hardware;

/**
 * Created by francesco on 06/04/14.
 */
public class ContactTask extends AsyncTask<String,Void,SQLiteDatabase> {

    private Context context;
    private int id_phone = 0;
    private ArrayList<Contact> contacts;

    private int max = 0;

    private boolean findNew;

    public ContactTask(Context c,boolean findNew){
        this.context = c;
        this.findNew = findNew;
        this.contacts = new ArrayList<Contact>();
    }

    protected SQLiteDatabase doInBackground(String... params){

        DataBaseHelper databaseHelper = new DataBaseHelper(this.context);
        final SQLiteDatabase db = databaseHelper.getWritableDatabase();

        this.max = (new Integer(Dao.getLastIdTaskInfo(db))).intValue();

        String email;
        String phone;
        ContentResolver cr = this.context.getContentResolver();

        Cursor cur;

        if (this.findNew) {
            cur = cr.query(ContactsContract.Contacts.CONTENT_URI, null, ContactsContract.Contacts._ID + " > ?", new String[]{(new Integer(Dao.getLastIdTaskInfo(db))).toString()}, null);
        } else {
            cur = cr.query(ContactsContract.Contacts.CONTENT_URI, null, null, null, null);
        }

        if (cur.getCount() > 0) {

            while (cur.moveToNext()) {

                final Contact contact = new Contact();
                contact.setId_phone(cur.getString(cur.getColumnIndex(ContactsContract.Contacts._ID)));

                if ((new Integer(contact.getId_phone())).intValue() > this.id_phone) {
                    this.id_phone = (new Integer(contact.getId_phone())).intValue();
                }
                String[] temp_name = cur.getString(cur.getColumnIndex(ContactsContract.Contacts.DISPLAY_NAME)).split(" ");
                String name = "";
                for (int y = 0; y < temp_name.length; y++) {
                    if (y == 0) {
                        name = temp_name[y].substring(0, 1).toUpperCase() + temp_name[y].substring(1);
                    } else {
                        name = name + " " + temp_name[y].substring(0, 1).toUpperCase() + temp_name[y].substring(1);
                    }
                }
                contact.setName(name);
                contact.setSurname("");

                Uri photoUri = ContentUris.withAppendedId(ContactsContract.Contacts.CONTENT_URI, Long.parseLong(contact.getId_phone()));
                photoUri = Uri.withAppendedPath(photoUri, ContactsContract.Contacts.Photo.CONTENT_DIRECTORY);
                try {
                    Bitmap bm = BitmapFactory.decodeStream(context.getContentResolver().openInputStream(photoUri));
                    contact.setPhoto(Bitmap.createScaledBitmap(bm, 120, 120, true));
                } catch (FileNotFoundException e) {
                }

                email = "";
                phone = "";

                if (Integer.parseInt(cur.getString(cur.getColumnIndex(ContactsContract.Contacts.HAS_PHONE_NUMBER))) > 0) {
                    String[] PROJECTION = new String[]{ContactsContract.CommonDataKinds.Phone.NUMBER};
                    Cursor phoneCur = cr.query(
                            ContactsContract.CommonDataKinds.Phone.CONTENT_URI,
                            PROJECTION,
                            ContactsContract.CommonDataKinds.Phone.CONTACT_ID + " = ?",
                            new String[]{contact.getId_phone()}, null);
                    if (phoneCur.getCount() > 0) {
                        String[] number = new String[phoneCur.getCount()];
                        int count = 0;
                        while (phoneCur.moveToNext()) {
                            number[count] = phoneCur.getString(0);
                            count++;
                        }
                        for (count = 0; count < number.length; count++) {
                            String[] temp = number[count].split("-");
                            String prepare = "";
                            for (int i = 0; i < temp.length; i++) {
                                prepare = prepare + temp[i];
                            }
                            if (prepare.startsWith("+")) {
                                prepare = prepare.substring(3);
                            }
                            if (!prepare.startsWith("0")) {
                                number[count] = prepare;
                            } else {
                                number[count] = "";
                            }
                        }
                        for (count = 0; count < number.length; count++) {
                            for (int i = 0; i < number.length; i++) {
                                if ((!number[count].equals("")) && (count != i) && (number[count].contains(number[i]))) {
                                    number[i] = "";
                                }
                            }
                            if (!number[count].equals("")) {

                                //number[count].replaceAll(" ", "");
                                number[count] = replaceSpace(number[count]);

                                if ((phone.equals(""))&&((number[count].length()>=8))) {
                                    phone = number[count];
                                } else if(number[count].length()>=8){
                                    phone = phone + " - " + number[count];
                                    ;
                                }

                            }
                        }

                    }
                    phoneCur.close();
                }
                contact.setNumber(phone);
                Cursor emailCur = cr.query(
                        ContactsContract.CommonDataKinds.Email.CONTENT_URI,
                        null,
                        ContactsContract.CommonDataKinds.Email.CONTACT_ID + " = ?",
                        new String[]{contact.getId_phone()}, null);
                while (emailCur.moveToNext()) {
                    LinkedList<String> temp = new LinkedList<String>();
                    while (emailCur.moveToNext()) {
                        boolean flag = true;
                        String value = emailCur.getString(
                                emailCur.getColumnIndex(ContactsContract.CommonDataKinds.Email.DATA)
                        );
                        for (int i = 0; i < temp.size(); i++) {
                            if (temp.get(i).equals(value)) {
                                flag = false;
                                break;
                            }
                        }
                        if (flag) {
                            temp.add(value);
                        }
                    }
                    while (!temp.isEmpty()) {
                        email = email + temp.removeFirst();
                        if (!temp.isEmpty()) {
                            email = email + "\n";
                        }
                    }
                }
                emailCur.close();
                contact.setEmail(email);
                contact.setErasable(false);

                if (!contact.getName().contains("@")) {
                    if (!contact.getEmail().equals("")) {
                        finalizeContact(contact, db);
                    } else if (!contact.getNumber().equals("")) {
                        finalizeContact(contact, db);
                    }
                }
            }
        }

        if(this.contacts.size()>0){
            (new Thread(new RunnableContact(db,this.contacts.iterator()))).start();
        }

        return db;
    }

    private String replaceSpace(String phone){

        String[] temp = phone.split(" ");

        String result = "";

        for(int i=0;i<temp.length;i++){
            result = result + temp[i];
        }

        return result;
    }

    protected void onPostExecute(final SQLiteDatabase db){
        Dao.resetFirst(db);
        Dao.setRefreshContactTaskInfo(db);
        Dao.setRefreshOnlineTaskInfo(db);

        if(this.id_phone>0){
            Dao.updateLastIdTaskInfo(db, this.id_phone);
        }
    }

    private void finalizeContact(Contact contact, SQLiteDatabase db){

        this.contacts.add(contact);

        if(this.contacts.size()==10){
            (new Thread(new RunnableContact(db,this.contacts.iterator()))).start();

            this.contacts = new ArrayList<Contact>();

        }
    }

    private class RunnableContact implements Runnable{

        private SQLiteDatabase db;
        private Iterator<Contact> iterator;

        public RunnableContact(SQLiteDatabase db, Iterator<Contact> it){
            this.db = db;
            this.iterator = it;
        }
        @Override
        public void run(){
            Looper.prepare();

            while(this.iterator.hasNext()){

                Contact contact = iterator.next();

                Esito esito = ComunicationService.isUser(contact);

                if (esito.isFlag()) {
                    ContactService contact_service = (ContactService) esito.getResult();
                    contact.setId_natter(contact_service.getId_natter());
                    contact.setEmail(contact_service.getEmail());
                    contact.setName(contact_service.getName());
                    contact.setSurname(contact_service.getSurname());
                    contact.setNumber(contact_service.getPhone());

                    if (!contact_service.getPhoto().equals(Code.NOT_STATED)) {
                        contact.setPhoto(Hardware.fromStringToImage(contact_service.getPhoto()));
                    } else {
                        contact.setHasPhoto(false);
                    }

                    contact.setErasable(false);
                }

                if (!Dao.lockContactTaskInfo(db)) {
                    while (Dao.isLockedContactTaskInfo(db)) {}

                    Dao.lockContactTaskInfo(db);
                }

                boolean delete = false;

                if (Dao.ifExist(db, contact.getId_phone())) {
                    Dao.updateContact(db, contact.getId_phone(), contact.stringArray());

                    delete = true;
                }
                else {
                    if (!Dao.ifExistSignInNatter(db, contact.getEmail())) {
                        Dao.insertContact(db, contact.stringArray());
                        Dao.updateCountOnlineTaskInfo(db,(Dao.countOnline(db)+1));
                    }
                    else {
                        Dao.deletContact(db, contact.getId_phone());
                    }
                }

                Dao.unlockContactTaskInfo(db);

                if (delete) {
                    Hardware.deleteUserImage(contact.getId_phone());
                }
                Hardware.saveContactImage(contact);

            }

        }
    }
}
