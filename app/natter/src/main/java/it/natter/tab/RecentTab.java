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
import android.view.animation.AlphaAnimation;
import android.view.animation.Animation;
import android.view.animation.DecelerateInterpolator;
import android.widget.AdapterView;
import android.widget.GridView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import java.util.ArrayList;

import it.natter.NatterApplication;
import it.natter.R;
import it.natter.classes.Contact;
import it.natter.dao.Dao;
import it.natter.dao.DataBaseHelper;
import it.natter.fragment.HomeFragment;
import it.natter.utility.CustomProgress;

public class RecentTab extends Fragment{

    private static ArrayList<String> read;
    private static ArrayList<String> notRead;
    private static ArrayList<String> voice;
    private TextView textView;
    private GridView gridRead;
    private GridView gridNotRead;
    private GridView gridVoice;
    private RelativeLayout conteinerNotRead;

    private CustomRecentAdapter adapterRead;
    private CustomRecentAdapter adapterNotRead;
    private CustomRecentAdapter adapterVoice;

    private boolean saveOnIstanceFlag = false;
    private boolean show = false;

    private CustomProgress dialog = new CustomProgress("We are reading your conversation");

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

        View v = inflater.inflate(R.layout.recent_fragment_tab, container, false);

        this.textView = (TextView)v.findViewById(R.id.info_recent);
        this.gridRead = (GridView)v.findViewById(R.id.grid_recent_read);
        this.gridNotRead = (GridView)v.findViewById(R.id.grid_recent_not_read);
        this.conteinerNotRead = (RelativeLayout)v.findViewById(R.id.grid_recent_not_read_conteiner);
        this.gridVoice = (GridView)v.findViewById(R.id.grid_recent_voice);

        dialog.show(getActivity().getFragmentManager(), "");

        this.show = true;

        this.checkRecent();

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

    private void checkRecent(){

        DataBaseHelper databaseHelper = new DataBaseHelper(getActivity().getApplicationContext());
        final SQLiteDatabase db = databaseHelper.getReadableDatabase();

        new Thread(new Runnable() {
            @Override
            public void run() {
                Looper.prepare();

                read = Dao.getIdNatterRead(db);
                notRead = Dao.getIdNatterNotRead(db);
                voice = Dao.getIdNatterVoice(db);

                adjustList();

                adapterNotRead = new CustomRecentAdapter(getActivity().getApplicationContext(),notRead,true);
                adapterRead = new CustomRecentAdapter(getActivity().getApplicationContext(),read,false);
                adapterVoice = new CustomRecentAdapter(getActivity().getApplicationContext(),voice,false);

                db.close();

                getActivity().runOnUiThread(new Runnable() {
                    @Override
                    public void run() {

                        if(!notRead.isEmpty()){
                            textView.setVisibility(View.GONE);
                            gridNotRead.setAdapter(adapterNotRead);
                            gridNotRead.setOnItemClickListener(new manageOnClickGridView(adapterNotRead));

                            Animation fadeIn = new AlphaAnimation(0, 1);
                            fadeIn.setInterpolator(new DecelerateInterpolator()); //add this
                            fadeIn.setDuration(1000);
                            conteinerNotRead.setAnimation(fadeIn);
                            conteinerNotRead.setVisibility(View.VISIBLE);
                        }

                        if(!read.isEmpty()){
                            gridRead.setAdapter(adapterRead);
                            textView.setVisibility(View.GONE);
                            gridRead.setVisibility(View.VISIBLE);
                            gridRead.setOnItemClickListener(new manageOnClickGridView(adapterRead));
                        }

                        if(!voice.isEmpty()){
                            gridVoice.setAdapter(adapterVoice);
                            textView.setVisibility(View.GONE);
                            gridVoice.setVisibility(View.VISIBLE);
                            gridVoice.setOnItemClickListener(new manageOnClickGridView(adapterVoice));
                        }

                        if(read.isEmpty() && notRead.isEmpty() && voice.isEmpty()){
                            conteinerNotRead.setVisibility(View.GONE);
                            gridRead.setVisibility(View.GONE);
                            gridVoice.setVisibility(View.GONE);
                            textView.setVisibility(View.VISIBLE);
                        }

                        if(!saveOnIstanceFlag) {
                            dialog.dismiss();
                            show = false;
                        }
                    }
                });
            }
        }).start();
    }

    private void adjustList(){
        for(String id : this.notRead){

            if(this.read.contains(id)){
                this.read.remove(id);
            }

            if(this.voice.contains(id)){
                this.voice.remove(id);
            }

        }

        for(String id : this.read){
            if(this.voice.contains(id)){
                this.voice.remove(id);
            }
        }
    }

    private class manageOnClickGridView implements AdapterView.OnItemClickListener{

        private CustomRecentAdapter adapter;

        public manageOnClickGridView(CustomRecentAdapter adapter){
            this.adapter = adapter;
        }

        @Override
        public void onItemClick(AdapterView<?> parent, View v, int position, long id){
            DataBaseHelper databaseHelper = new DataBaseHelper(getActivity().getApplicationContext());
            final SQLiteDatabase db = databaseHelper.getReadableDatabase();

            Contact contact = Dao.getContact(db, Dao.getIdPhoneFromIdNatter(db,this.adapter.getItem(position).toString()), getActivity().getApplicationContext());

            db.close();

            Fragment fragment = new HomeFragment(contact);
            FragmentManager fragmentManager = getActivity().getSupportFragmentManager();
            fragmentManager.beginTransaction().replace(R.id.frame_container, fragment).commit();
        }
    }

}