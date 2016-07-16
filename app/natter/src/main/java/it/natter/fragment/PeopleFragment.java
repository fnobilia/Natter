package it.natter.fragment;

import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentTabHost;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import it.natter.NatterApplication;
import it.natter.R;
import it.natter.dao.Dao;
import it.natter.dao.DataBaseHelper;
import it.natter.tab.ContactsTab;
import it.natter.tab.OnlineTab;
import it.natter.tab.RecentTab;
import it.natter.task.UpdateAccess;

/**
 * Created by francesco on 07/03/14.
 */
public class PeopleFragment extends Fragment{
    private FragmentTabHost TabHost;

    @Override
    public View onCreateView(LayoutInflater inflater,ViewGroup container,Bundle savedInstanceState){

        UpdateAccess.go(getActivity().getApplicationContext());

        TabHost = new FragmentTabHost(getActivity());

        this.manageTab();

        return TabHost;
    }

    private void manageTab(){
        TabHost.setup(getActivity(), getChildFragmentManager(), R.id.tabhost_people);

        TabHost.addTab(TabHost.newTabSpec("online").setIndicator("Online",getResources().getDrawable(R.drawable.tab_indicator)),OnlineTab.class, null);
        TabHost.addTab(TabHost.newTabSpec("recent").setIndicator("Recent",getResources().getDrawable(R.drawable.tab_indicator)),RecentTab.class, null);
        TabHost.addTab(TabHost.newTabSpec("contacts").setIndicator("Contacts",getResources().getDrawable(R.drawable.tab_indicator)),ContactsTab.class, null);

        TabHost.getTabWidget().getChildAt(0).setBackgroundResource(R.drawable.tab_indicator);
        TabHost.getTabWidget().getChildAt(1).setBackgroundResource(R.drawable.tab_indicator);
        TabHost.getTabWidget().getChildAt(2).setBackgroundResource(R.drawable.tab_indicator);

        DataBaseHelper databaseHelper = new DataBaseHelper(getActivity().getApplicationContext());
        SQLiteDatabase db = databaseHelper.getReadableDatabase();

        if(Dao.getIdNatterRead(db).isEmpty()&&Dao.getIdNatterNotRead(db).isEmpty()){
            TabHost.setCurrentTab(0);
        }
        else{
            TabHost.setCurrentTab(1);
        }
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        TabHost = null;
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
}

