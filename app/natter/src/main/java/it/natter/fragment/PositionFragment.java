package it.natter.fragment;

import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CompoundButton;
import android.widget.Switch;

import it.natter.NatterApplication;
import it.natter.R;
import it.natter.dao.Dao;
import it.natter.dao.DataBaseHelper;
import it.natter.task.UpdateAccess;
import it.natter.utility.Hardware;

/**
 * Created by francesco on 07/03/14.
 */
public class PositionFragment extends Fragment {

    private Switch position;

    private View root;

    public PositionFragment(){}

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState){

        UpdateAccess.go(getActivity().getApplicationContext());

        this.root = inflater.inflate(R.layout.fragment_position, container, false);

        this.position = (Switch) this.root.findViewById(R.id.switch_position);

        this.position.setOnCheckedChangeListener(new onChangedSwitch());

        DataBaseHelper databaseHelper = new DataBaseHelper(getActivity());
        final SQLiteDatabase db = databaseHelper.getWritableDatabase();

        int pos_flag = Dao.getPositionSetting(db);

        if(pos_flag==1) this.position.setChecked(true);
        else this.position.setChecked(false);

        db.close();

        return this.root;
    }

    private class onChangedSwitch implements CompoundButton.OnCheckedChangeListener{

        public onChangedSwitch(){}

        public void onCheckedChanged(CompoundButton buttonView, boolean isChecked){

            DataBaseHelper databaseHelper = new DataBaseHelper(getActivity());
            final SQLiteDatabase db = databaseHelper.getWritableDatabase();

            if(Dao.getPositionSetting(db)==-1){

                if(isChecked){
                    if(!Hardware.checkLocationHardware(getActivity().getApplicationContext())){
                        Hardware.showDialogToActiveGps(getActivity());
                    }
                }

                Dao.insertPositionSetting(db,isChecked);
            }
            else {

                if(isChecked){
                    if(!Hardware.checkLocationHardware(getActivity().getApplicationContext())){
                        Hardware.showDialogToActiveGps(getActivity());
                    }
                }

                Dao.updatePositionSetting(db, isChecked);
            }

            db.close();
        }
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
