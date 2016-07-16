package it.natter.fragment;

import android.content.Intent;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CompoundButton;
import android.widget.Switch;
import android.widget.Toast;

import it.natter.NatterApplication;
import it.natter.R;
import it.natter.dao.Dao;
import it.natter.dao.DataBaseHelper;
import it.natter.serviceTask.NatterService;
import it.natter.task.UpdateAccess;
import it.natter.utility.Hardware;

/**
 * Created by francesco on 07/03/14.
 */
public class SettingsFragment extends Fragment{

    private Switch position;
    private Switch realtime;
    private Switch boot;

    private View root;

    public SettingsFragment(){}

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,Bundle savedInstanceState){

        UpdateAccess.go(getActivity().getApplicationContext());

        this.root = inflater.inflate(R.layout.fragment_settings, container, false);

        this.switchOnOffManage();

        return this.root;
    }

    private void switchOnOffManage(){
        this.position = (Switch) this.root.findViewById(R.id.switch_position_settings);
        this.realtime = (Switch) this.root.findViewById(R.id.switch_realtime_settings);
        this.boot = (Switch) this.root.findViewById(R.id.switch_boot_settings);

        this.position.setOnCheckedChangeListener(new onChangedSwitch(0));
        this.realtime.setOnCheckedChangeListener(new onChangedSwitch(1));
        this.boot.setOnCheckedChangeListener(new onChangedSwitch(2));

        DataBaseHelper databaseHelper = new DataBaseHelper(getActivity());
        final SQLiteDatabase db = databaseHelper.getWritableDatabase();

        int pos_flag = Dao.getPositionSetting(db);
        int real_flag = Dao.getRealtimeSetting(db);
        int boot_flag = Dao.getBootSetting(db);

        if(pos_flag==1) this.position.setChecked(true);
        else this.position.setChecked(false);

        if(real_flag==1) this.realtime.setChecked(true);
        else this.realtime.setChecked(false);

        if(boot_flag==1) this.boot.setChecked(true);
        else this.boot.setChecked(false);

        db.close();
    }

    private class onChangedSwitch implements CompoundButton.OnCheckedChangeListener{

        private int setting;

        public onChangedSwitch(int s){
            setting = s;
        }

        public void onCheckedChanged(CompoundButton buttonView, boolean isChecked){

            DataBaseHelper databaseHelper = new DataBaseHelper(getActivity());
            final SQLiteDatabase db = databaseHelper.getWritableDatabase();

            switch(setting){
                case 0:
                    if(Dao.getPositionSetting(db)==-1){
                        if(isChecked){
                            if(!Hardware.checkLocationHardware(getActivity().getApplicationContext())){
                                Hardware.showDialogToActiveGps(getActivity());
                            }
                        }
                        Dao.insertPositionSetting(db,isChecked);
                    }
                    else{
                        if(isChecked){
                            if(!Hardware.checkLocationHardware(getActivity().getApplicationContext())){
                                Hardware.showDialogToActiveGps(getActivity());
                            }
                        }
                        Dao.updatePositionSetting(db,isChecked);
                    }
                    break;
                case 1:
                    if(Dao.getRealtimeSetting(db)==-1)  Dao.insertRealtimeSetting(db, isChecked);
                    else Dao.updateRealtimeSetting(db, isChecked);
                    break;
                case 2:
                    if(Dao.getBootSetting(db)==-1)  Dao.insertBootSetting(db, isChecked);
                    else Dao.updateBootSetting(db, isChecked);

                    String mex = "";

                    if(isChecked){
                        getActivity().startService(new Intent(getActivity().getApplicationContext(),NatterService.class));
                        mex = "App will START in background during next reboot!";
                    }
                    else{
                        getActivity().stopService(new Intent(getActivity().getApplicationContext(),NatterService.class));
                        mex = "App will NOT start in background during next reboot!";
                    }

                    Toast toast = Toast.makeText(getActivity().getApplicationContext(),mex,Toast.LENGTH_SHORT);
                    toast.show();

                    break;

                default: break;
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
