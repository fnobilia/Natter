package it.natter.fragment;

import android.database.sqlite.SQLiteDatabase;
import android.graphics.Bitmap;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import com.google.android.gms.maps.CameraUpdate;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.BitmapDescriptorFactory;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.MarkerOptions;

import java.util.ArrayList;
import java.util.Iterator;

import it.natter.NatterApplication;
import it.natter.R;
import it.natter.classes.Esito;
import it.natter.classes.Position;
import it.natter.dao.Dao;
import it.natter.dao.DataBaseHelper;
import it.natter.rest.ComunicationService;
import it.natter.rest.UserService;
import it.natter.task.UpdateAccess;
import it.natter.utility.CustomProgress;
import it.natter.utility.Hardware;

/**
 * Created by francesco on 07/03/14.
 */
public class MapNatterFragment extends Fragment{

    private GoogleMap googleMap;
    private int pos_flag = 0;
    private View rootView;

    public MapNatterFragment(){}

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,Bundle savedInstanceState){

        UpdateAccess.go(getActivity().getApplicationContext());

        this.rootView = inflater.inflate(R.layout.fragment_map, container, false);

        DataBaseHelper databaseHelper = new DataBaseHelper(getActivity());
        final SQLiteDatabase db = databaseHelper.getWritableDatabase();

        this.pos_flag = Dao.getPositionSetting(db);

        if(this.pos_flag==1){

            if(!Hardware.checkLocationHardware(getActivity().getApplicationContext())){
                rootView.findViewById(R.id.relative_map_info).setVisibility(View.VISIBLE);
                rootView.findViewById(R.id.relative_map).setVisibility(View.GONE);

                if(Dao.getPositionSetting(db)==-1){
                    Dao.insertPositionSetting(db,false);
                }
                else{
                    Dao.updatePositionSetting(db,false);
                }
            }
            else {
                this.showMap();
            }
        }
        else{
            rootView.findViewById(R.id.relative_map_info).setVisibility(View.VISIBLE);
            rootView.findViewById(R.id.relative_map).setVisibility(View.GONE);
        }

        Dao.updateDestroy(db, "map", false);

        try{
            initilizeMap();
        }catch (Exception e){
            e.printStackTrace();
        }

        return rootView;
    }

    private void showMap(){
        final CustomProgress dialog = new CustomProgress("We are locating your friends!");
        dialog.show(getActivity().getFragmentManager(), "");

        DataBaseHelper databaseHelper = new DataBaseHelper(getActivity());
        final SQLiteDatabase db = databaseHelper.getWritableDatabase();

        Hardware.managePosition(getActivity().getApplicationContext(),db);

        new Thread(new Runnable() {
            @Override
            public void run() {

                ArrayList<Position> position = new ArrayList<Position>();

                UserService.updatePosition(new Position((new Integer(Dao.getIdProfile(db))).toString(), Dao.getLatitude(db), Dao.getLongitude(db)));

                Position me = new Position((new Integer(Dao.getIdProfile(db))).toString(), Dao.getLatitude(db), Dao.getLongitude(db));
                position.add(me);

                Esito esito = UserService.updatePosition(me);

                if (esito.isFlag()) {

                    Iterator<String> i = Dao.getListOnline(db).iterator();

                    while (i.hasNext()) {

                        String id_phone = i.next();

                        esito = ComunicationService.getPosition(new Position(Dao.getIdNatterContact(db, id_phone), "0.0", "0.0"));
                        Position friend = (Position) esito.getResult();

                        if (esito.isFlag()) {
                            getActivity().runOnUiThread(new Runnable() {
                                @Override
                                public void run() {
                                    rootView.findViewById(R.id.relative_map_info).setVisibility(View.GONE);
                                    rootView.findViewById(R.id.relative_map).setVisibility(View.VISIBLE);
                                }
                            });

                            friend.setId_natter(id_phone);

                            position.add(friend);
                        }
                    }
                }

                putOnMap(position);

                getActivity().runOnUiThread(new Runnable() {
                    public void run() {
                        Toast.makeText(getActivity().getApplicationContext(), "All of your friends who share their position have been located", Toast.LENGTH_LONG).show();
                    }
                });

                dialog.dismiss();
            }
        }).start();

    }

    private void initilizeMap() {
        if(googleMap == null){
            googleMap = ((SupportMapFragment) getActivity().getSupportFragmentManager().findFragmentById(R.id.map)).getMap();

            googleMap.setMapType(GoogleMap.MAP_TYPE_HYBRID);

            if(googleMap == null){
                Toast.makeText(getActivity(),"Sorry! unable to create maps", Toast.LENGTH_SHORT).show();
            }
        }
    }

    @Override
    public void onResume(){
        super.onResume();
        NatterApplication.activityResumed();
        initilizeMap();
    }

    @Override
    public void onPause() {
        super.onPause();
        NatterApplication.activityPaused();
    }

    public void onDestroyView(){

        DataBaseHelper databaseHelper = new DataBaseHelper(getActivity().getApplicationContext());
        final SQLiteDatabase db = databaseHelper.getWritableDatabase();

        if(!Dao.isDestroy(db, "map")){

            FragmentManager fragmentManager = getActivity().getSupportFragmentManager();
            SupportMapFragment fragment = (SupportMapFragment) fragmentManager.findFragmentById(R.id.map);
            FragmentTransaction transaction = fragmentManager.beginTransaction();
            transaction.remove(fragment);
            transaction.commit();

            Dao.updateDestroy(db, "map", true);
        }
        db.close();

        super.onDestroyView();
    }

    private void putOnMap(final ArrayList<Position> position){

        final Iterator<Position> iterator = position.iterator();

        getActivity().runOnUiThread(new Runnable() {
            @Override
            public void run(){

                Bitmap bitmap_me = Hardware.fromLayoutToBitmap(getActivity(),Hardware.getProfileImage(getActivity().getApplicationContext()));
                LatLng me = iterator.next().getLatLng();

                googleMap.addMarker(new MarkerOptions()
                        .position(me)
                        .icon(BitmapDescriptorFactory.fromBitmap(bitmap_me)));

                Position temp;
                while(iterator.hasNext()){

                    temp = iterator.next();

                    Bitmap bitmap_user = Hardware.fromLayoutToBitmap(getActivity(),Hardware.getUserImage(getActivity().getApplicationContext(),temp.getId_natter()));

                    googleMap.addMarker(new MarkerOptions()
                            .position(temp.getLatLng())
                            .icon(BitmapDescriptorFactory.fromBitmap(bitmap_user)));

                    CameraUpdate center = CameraUpdateFactory.newLatLng(me);

                    googleMap.moveCamera(center);
                    googleMap.animateCamera(CameraUpdateFactory.zoomIn());
                    googleMap.animateCamera(CameraUpdateFactory.zoomTo(15), 5000, null);
                }
            }
        });
    }

}
