package it.natter.fragment_call;

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
import com.google.android.gms.maps.model.MarkerOptions;

import it.natter.NatterApplication;
import it.natter.R;
import it.natter.classes.Contact;
import it.natter.classes.Esito;
import it.natter.classes.Position;
import it.natter.dao.Dao;
import it.natter.dao.DataBaseHelper;
import it.natter.rest.ComunicationService;
import it.natter.rest.UserService;
import it.natter.utility.CustomProgress;
import it.natter.utility.Hardware;

/**
 * Created by francesco on 07/03/14.
 */
public class MapUserFragment extends Fragment{

    private GoogleMap googleMap;
    private Contact contact;

    public MapUserFragment(Contact contact){
        this.contact = contact;
    }

    @Override
    public View onCreateView(LayoutInflater inflater, final ViewGroup container,Bundle savedInstanceState) {

        final View rootView = inflater.inflate(R.layout.fragment_map_user, container, false);

        DataBaseHelper databaseHelper = new DataBaseHelper(getActivity());
        final SQLiteDatabase db = databaseHelper.getWritableDatabase();

        int pos_flag = Dao.getPositionSetting(db);

        if(pos_flag==1){

            if(!Hardware.checkLocationHardware(getActivity().getApplicationContext())){
                rootView.findViewById(R.id.relative_user_map_info_me).setVisibility(View.VISIBLE);
                rootView.findViewById(R.id.relative_map_user).setVisibility(View.GONE);

                if(Dao.getPositionSetting(db)==-1){
                    Dao.insertPositionSetting(db,false);
                }
                else{
                    Dao.updatePositionSetting(db,false);
                }
            }
            else {

                final CustomProgress dialog = new CustomProgress("We are locating the " + this.contact.getName() + "!");
                dialog.show(getActivity().getFragmentManager(), "");

                Hardware.managePosition(getActivity().getApplicationContext(),db);

                new Thread(new Runnable() {
                    @Override
                    public void run() {

                        Position me = new Position((new Integer(Dao.getIdProfile(db))).toString(), Dao.getLatitude(db), Dao.getLongitude(db));

                        Esito esito = UserService.updatePosition(me);

                        if (esito.isFlag()) {

                            esito = ComunicationService.getPosition(new Position(contact.getId_natter(), "0.0", "0.0"));
                            Position friend = (Position) esito.getResult();

                            if (esito.isFlag()) {
                                getActivity().runOnUiThread(new Runnable() {
                                    @Override
                                    public void run() {
                                        rootView.findViewById(R.id.relative_user_map_info_me).setVisibility(View.GONE);
                                        rootView.findViewById(R.id.relative_map_user).setVisibility(View.VISIBLE);
                                    }
                                });

                                putOnMap(me, friend);
                            } else {
                                rootView.findViewById(R.id.relative_user_map_info_me).setVisibility(View.GONE);
                                rootView.findViewById(R.id.relative_user_map_info).setVisibility(View.VISIBLE);
                                getActivity().runOnUiThread(new Runnable() {
                                    public void run() {
                                        Toast.makeText(getActivity().getApplicationContext(), "It was not possible to localize " + contact.getAllName(), Toast.LENGTH_LONG).show();
                                    }
                                });
                            }
                        }

                        dialog.dismiss();
                    }
                }).start();
            }

        }
        else{
            rootView.findViewById(R.id.relative_user_map_info_me).setVisibility(View.VISIBLE);
            rootView.findViewById(R.id.relative_map_user).setVisibility(View.GONE);
        }

        Dao.updateDestroy(db, "map_user", false);

        try{
            initilizeMap();
        }catch (Exception e){
            e.printStackTrace();
        }

        return rootView;
    }

    private void initilizeMap(){
        if(googleMap == null){
            googleMap = ((SupportMapFragment) getActivity().getSupportFragmentManager().findFragmentById(R.id.map_user)).getMap();

            googleMap.setMapType(GoogleMap.MAP_TYPE_HYBRID);

            if(googleMap == null){
                Toast.makeText(getActivity(), "Sorry! unable to create maps", Toast.LENGTH_SHORT).show();
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
        if(!Dao.isDestroy(db, "map_user")){

            FragmentManager fragmentManager = getActivity().getSupportFragmentManager();
            SupportMapFragment fragment = (SupportMapFragment) fragmentManager.findFragmentById(R.id.map_user);
            FragmentTransaction transaction = fragmentManager.beginTransaction();
            transaction.remove(fragment);
            transaction.commit();

            Dao.updateDestroy(db, "map_user", true);
        }
        db.close();

        super.onDestroyView();
    }

    private void putOnMap(final Position me,final Position friend){
        getActivity().runOnUiThread(new Runnable() {
            @Override
            public void run(){

                Bitmap bitmap_me = Hardware.fromLayoutToBitmap(getActivity(),Hardware.getProfileImage(getActivity().getApplicationContext()));

                googleMap.addMarker(new MarkerOptions()
                        .position(me.getLatLng())
                        .icon(BitmapDescriptorFactory.fromBitmap(bitmap_me)));

                Bitmap bitmap_user = Hardware.fromLayoutToBitmap(getActivity(),contact.getPhoto());

                googleMap.addMarker(new MarkerOptions()
                        .position(friend.getLatLng())
                        .icon(BitmapDescriptorFactory.fromBitmap(bitmap_user)));

                CameraUpdate center = CameraUpdateFactory.newLatLng(friend.getLatLng());

                googleMap.moveCamera(center);
                googleMap.animateCamera(CameraUpdateFactory.zoomIn());
                googleMap.animateCamera(CameraUpdateFactory.zoomTo(15), 5000, null);
            }
        });
    }

}
