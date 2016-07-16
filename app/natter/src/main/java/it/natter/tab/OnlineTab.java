package it.natter.tab;

/**
 * Created by francesco on 12/03/14.
 */

import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.os.Looper;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.GridView;
import android.widget.TextView;

import java.util.ArrayList;

import it.natter.NatterApplication;
import it.natter.R;
import it.natter.classes.Contact;
import it.natter.dao.Dao;
import it.natter.dao.DataBaseHelper;
import it.natter.fragment.HomeFragment;
import it.natter.task.OnlineContactTask;
import it.natter.utility.CustomProgress;

public class OnlineTab extends Fragment{

    private static ArrayList<String> contacts;
    private TextView textView;
    private GridView gridView;

    private boolean saveOnIstanceFlag = false;
    private boolean show = false;

    private CustomOnlineAdapter mAdapter;

    private CustomProgress dialog = new CustomProgress("We are reading your contacts");

    @Override
    public void onCreate(Bundle savedInstanceState){
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,Bundle savedInstanceState){

        this.saveOnIstanceFlag = false;

        if(this.show){
            dialog.dismiss();
        }

        View v = inflater.inflate(R.layout.online_fragment_tab, container, false);

        this.textView = (TextView)v.findViewById(R.id.info_online);
        this.gridView = (GridView)v.findViewById(R.id.grid_online);

        dialog.show(getActivity().getFragmentManager(), "");

        this.show = true;

        DataBaseHelper databaseHelper = new DataBaseHelper(getActivity().getApplicationContext());
        final SQLiteDatabase db = databaseHelper.getWritableDatabase();

        new Thread(new Runnable() {
            @Override
            public void run() {

                Looper.prepare();

                if(Dao.hasToRefreshOnlineTaskInfo(db)){

                    while(Dao.isLockedContactTaskInfo(db)){}

                    Dao.lockContactTaskInfo(db);

                    contacts = Dao.getListOnline(db);

                    Dao.unlockContactTaskInfo(db);

                    Dao.resetRefreshOnlineTaskInfo(db);
                }

                mAdapter = new CustomOnlineAdapter(getActivity().getApplicationContext(),contacts,textView,gridView);

                new OnlineContactTask(getActivity().getApplicationContext(),mAdapter).execute("");

                getActivity().runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        if(contacts!=null) {
                            if (contacts.size() > 0) {
                                Dao.updateCountOnlineTaskInfo(db,contacts.size());

                                gridView.setAdapter(mAdapter);
                                gridView.setOnItemClickListener(new manageOnClickGridView(mAdapter));

                                textView.setVisibility(View.GONE);
                                gridView.setVisibility(View.VISIBLE);
                            }
                        }
                        else{
                            textView.setVisibility(View.VISIBLE);
                        }

                        db.close();

                        if(!saveOnIstanceFlag) {
                            dialog.dismiss();
                            show = false;
                        }
                    }
                });
            }
        }).start();

        return v;
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
    public void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);
        this.saveOnIstanceFlag = true;
    }

    private class manageOnClickGridView implements AdapterView.OnItemClickListener{

        private CustomOnlineAdapter adapter;

        public manageOnClickGridView(CustomOnlineAdapter adapter){
            this.adapter = adapter;
        }

        @Override
        public void onItemClick(AdapterView<?> parent, View v, int position, long id){
            DataBaseHelper databaseHelper = new DataBaseHelper(getActivity().getApplicationContext());
            final SQLiteDatabase db = databaseHelper.getWritableDatabase();

            Contact contact = Dao.getContact(db, this.adapter.getItem(position).toString(), getActivity().getApplicationContext());

            db.close();

            Fragment fragment = new HomeFragment(contact);
            FragmentManager fragmentManager = getActivity().getSupportFragmentManager();
            fragmentManager.beginTransaction().replace(R.id.frame_container, fragment).commit();
        }
    }
}