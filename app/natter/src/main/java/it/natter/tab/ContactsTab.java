package it.natter.tab;

/**
 * Created by francesco on 12/03/14.
 */

import android.database.sqlite.SQLiteDatabase;
import android.graphics.Color;
import android.os.Bundle;
import android.os.Looper;
import android.support.v4.app.ListFragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ListView;

import java.util.List;

import it.natter.NatterApplication;
import it.natter.R;
import it.natter.classes.Contact;
import it.natter.dao.Dao;
import it.natter.dao.DataBaseHelper;
import it.natter.utility.CustomDialogDeletContact;
import it.natter.utility.CustomProgress;

public class ContactsTab extends ListFragment{

    private ListView listView;
    private CustomContactAdapter mAdapter;
    private CustomProgress dialog = new CustomProgress("We are reading your contacts");

    private static List<Contact> contacts;

    @Override
    public View onCreateView(LayoutInflater inflater,ViewGroup container,Bundle savedInstanceState){
        View rootView = inflater.inflate(R.layout.tab_fragment_contact,container, false);

        listView = (ListView) rootView.findViewById(android.R.id.list);

        return rootView;
    }

    @Override
    public void onResume() {
        super.onResume();
        NatterApplication.activityResumed();
    }

    @Override
    public void onPause() {
        super.onPause();
        NatterApplication.activityPaused();
    }

    @Override
    public void onActivityCreated(Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);

        dialog.show(getActivity().getFragmentManager(), "");

        DataBaseHelper databaseHelper = new DataBaseHelper(getActivity().getApplicationContext());
        final SQLiteDatabase db = databaseHelper.getWritableDatabase();

        new Thread(new Runnable() {
            @Override
            public void run() {

                Looper.prepare();

                if(Dao.hasToRefreshContactTaskInfo(db)){

                    while(Dao.isLockedContactTaskInfo(db)){
                        try {
                            Thread.sleep(500);
                        } catch (InterruptedException e) {
                            Log.e("ContactTask", e.toString() + " - " + e.getMessage());
                        }
                    }

                    Dao.lockContactTaskInfo(db);

                    contacts = Dao.getListRubric(db, getActivity().getApplicationContext());

                    Dao.unlockContactTaskInfo(db);

                    Dao.resetRefreshContactTaskInfo(db);

                    db.close();
                }

                mAdapter = new CustomContactAdapter(getActivity(), android.R.id.list, contacts);

                listView.setOnItemLongClickListener(new manageLongClickList());

                getActivity().runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        listView.setAdapter(mAdapter);
                        dialog.dismiss();
                    }
                });
            }
        }).start();
    }

    private class manageLongClickList implements AdapterView.OnItemLongClickListener{

        public boolean onItemLongClick(AdapterView<?> arg0, View arg1,int pos, long id){
            Contact contact = contacts.get(pos);

            if((!contact.getId_natter().equals(""))&&(contact.getId_phone().contains("natter"))){
                arg1.setBackgroundColor(Color.parseColor("#992416"));

                CustomDialogDeletContact dialog = new CustomDialogDeletContact(contact);
                dialog.show(getActivity().getFragmentManager(),"");
            }

            arg1.setBackgroundResource(R.drawable.list_item_background);
            return true;
        }
    }

}