package it.natter.fragment;

import android.content.res.TypedArray;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.ListView;
import android.widget.TextView;

import it.natter.NatterApplication;
import it.natter.R;
import it.natter.classes.Contact;
import it.natter.dao.Dao;
import it.natter.dao.DataBaseHelper;
import it.natter.fragment_call.ChatFragment;
import it.natter.fragment_call.MapUserFragment;
import it.natter.fragment_call.ProfileUserFragment;
import it.natter.fragment_call.VoiceFragment;
import it.natter.task.UpdateAccess;
import it.natter.task.UpdateUserAccess;
import it.natter.utility.Code;

/**
 * Created by francesco on 07/03/14.
 */
public class HomeFragment extends Fragment{

    private View root;

    private TextView user_access;
    private ImageButton profile;
    private Button chat;
    private Button voice;
    private Button map;

    private Contact contact;

    public HomeFragment(){
        this.contact = null;
    }

    public HomeFragment(Contact contact){
        this.contact = contact;
    }

    @Override
    public View onCreateView(final LayoutInflater inflater, final ViewGroup container,Bundle savedInstanceState){

        UpdateAccess.go(getActivity().getApplicationContext());

        DataBaseHelper databaseHelper = new DataBaseHelper(getActivity().getApplicationContext());
        SQLiteDatabase db = databaseHelper.getReadableDatabase();

        View rootView = inflater.inflate(R.layout.fragment_home, container, false);

        Bundle bundle = this.getArguments();

        boolean emptyMessage = false;

        if(bundle != null){

            if((bundle.getInt("type") == Code.TYPE_TEXT)||(bundle.getInt("type") == Code.TYPE_VOICE)){
                this.contact = Dao.getContactByNatterId(db,Integer.valueOf(bundle.getInt("sender")).toString(),getActivity().getApplicationContext());
            }
        }

        if(this.contact != null){

            emptyMessage = Dao.getAllMessageByIdConversation(db,Integer.toString(Dao.getIdProfile(db))+"-"+this.contact.getId_natter()).isEmpty();

            this.updateMenu();

            ((TextView) rootView.findViewById(R.id.call_user_name)).setText((new String(""+contact.getName().charAt(0))).toUpperCase()+". "+contact.getSurname());

            ((ImageButton) rootView.findViewById(R.id.call_user_img)).setImageBitmap(contact.getPhoto());

            this.user_access = (TextView) rootView.findViewById(R.id.call_user_access);

            UpdateUserAccess.go(this.user_access,getActivity(),this.contact.getId_natter(),true, null);

        }
        else{
            rootView.findViewById(R.id.relative_bar_call).setVisibility(View.GONE);
            rootView.findViewById(R.id.frame_container_call).setVisibility(View.GONE);
            rootView.findViewById(R.id.info_home).setVisibility(View.VISIBLE);
        }

        this.profile = (ImageButton) rootView.findViewById(R.id.call_user_img);
        this.chat = (Button) rootView.findViewById(R.id.call_chat);
        this.voice = (Button) rootView.findViewById(R.id.call_voice);
        this.map = (Button) rootView.findViewById(R.id.call_position);

        this.profile.setOnClickListener(new onClickCallButton());
        this.chat.setOnClickListener(new onClickCallButton());
        this.voice.setOnClickListener(new onClickCallButton());
        this.map.setOnClickListener(new onClickCallButton());

        this.root = rootView;

        db.close();

        if(this.contact==null){
            displayView(2);
        }
        else if(bundle != null){
            if(bundle.getInt("type") == Code.TYPE_TEXT){
                displayView(1);
            }
        }
        else{

            if(emptyMessage) {
                displayView(2);
            }
            else{
                displayView(1);
            }

        }

        return rootView;
    }

    private void displayView(int position){
        Fragment fragment = null;

        switch(position){
            case 0:
                fragment = new ProfileUserFragment(this.contact);
                break;
            case 1:
                fragment = new ChatFragment(this.root,this.contact,this.user_access);
                break;
            case 2:
                fragment = new VoiceFragment(this.contact,this.user_access);
                break;
            case 3:
                fragment = new MapUserFragment(this.contact);
                break;
            case 4:
                fragment = new VoiceFragment(this.contact,this.user_access);
                break;
            case 5:
                fragment = new MapUserFragment(this.contact);
                break;

            default:
                break;
        }

        if(fragment != null){
            FragmentManager fragmentManager = getActivity().getSupportFragmentManager();
            fragmentManager.beginTransaction().replace(R.id.frame_container_call, fragment).commit();
            this.manageButton(position);
        }
        else{
            Log.e("HomeFragment", "Error in creating fragment");
        }
    }

    private void manageButton(int position){
        switch(position){
            case 0:
                this.chat.setBackgroundResource(R.drawable.ic_chat);
                this.voice.setBackgroundResource(R.drawable.ic_voice);
                this.map.setBackgroundResource(R.drawable.ic_position);
                this.map.setClickable(true);
                break;
            case 1:
                this.chat.setBackgroundResource(R.drawable.ic_chat_selected);
                this.voice.setBackgroundResource(R.drawable.ic_voice);
                this.map.setBackgroundResource(R.drawable.ic_position);
                this.map.setClickable(true);
                break;
            case 2:
                this.chat.setBackgroundResource(R.drawable.ic_chat);
                this.voice.setBackgroundResource(R.drawable.ic_voice_selected);
                this.map.setBackgroundResource(R.drawable.ic_position);
                this.map.setClickable(true);
                break;
            case 3:
                this.chat.setBackgroundResource(R.drawable.ic_chat);
                this.voice.setBackgroundResource(R.drawable.ic_voice);
                this.map.setBackgroundResource(R.drawable.ic_position_selected);
                this.map.setClickable(false);
                break;

            default:
                break;
        }
    }

    private class onClickCallButton implements View.OnClickListener{

        public void onClick(View v){
            displayView(Integer.parseInt(v.getContentDescription().toString()));
        }
    }

    private void updateMenu(){
        ListView menuList = (ListView) getActivity().findViewById(R.id.list_slidermenu);

        menuList.setItemChecked(0, true);
        menuList.setSelection(0);
        getActivity().getActionBar().setTitle("Home");
        TypedArray menuItemIcons = getResources().obtainTypedArray(R.array.menu_icons);
        getActivity().getActionBar().setIcon(menuItemIcons.getResourceId(0, -1));
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
