package it.natter.fragment_call;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.os.Handler;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.WindowManager;
import android.view.inputmethod.InputMethodManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.TextView;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.Iterator;

import it.natter.NatterApplication;
import it.natter.R;
import it.natter.classes.Contact;
import it.natter.classes.Esito;
import it.natter.classes.MessageNatter;
import it.natter.dao.Dao;
import it.natter.dao.DataBaseHelper;
import it.natter.rest.ComunicationService;
import it.natter.task.UpdateAccess;
import it.natter.task.UpdateUserAccess;
import it.natter.utility.Code;
import it.natter.utility.Hardware;

/**
 * Created by francesco on 07/03/14.
 */
public class ChatFragment extends Fragment implements TextWatcher{

    private View rootCall;

    private Button voice_bottom;
    private Button map_bottom;
    private Button chat_bottom;
    private EditText text;
    private CustomMessageAdapter adapter;
    private ListView list_mex;
    private TextView user_access;

    private Contact contact;
    private String id_natter;
    private ArrayList<MessageNatter> array_message = new ArrayList<MessageNatter>();

    private int time = 60;

    public ChatFragment(View rC,Contact contact,TextView user_access){
        this.contact = contact;
        this.rootCall = rC;
        this.user_access = user_access;
    }

    private Handler handler = new Handler();

    private Runnable showLastMex = new Runnable(){
        public void run(){
            getActivity().runOnUiThread(new Runnable() {
                @Override
                public void run() {
                list_mex.setSelection(array_message.size()-1);
                }
            });
        }
    };

    private Runnable getLastAccess = new Runnable(){
        public void run(){
            UpdateUserAccess.go(user_access, getActivity(), contact.getId_natter(), false, null);
            handler.postDelayed(getLastAccess,Code.FIVE_SECOND);
        }
    };

    private Runnable getMessage = new Runnable(){
        public void run(){

            new Thread(new Runnable() {
                @Override
                public void run() {

                    Hardware.removeNotification(getActivity().getApplicationContext(), id_natter + "-" + contact.getId_natter(), Code.TYPE_TEXT);

                    Esito esito = ComunicationService.getMessage(new MessageNatter("", id_natter, "", ""));

                    if(esito.isFlag()){

                        DataBaseHelper databaseHelper = new DataBaseHelper(getActivity().getApplicationContext());
                        SQLiteDatabase db = databaseHelper.getReadableDatabase();

                        DateFormat dateFormat = new SimpleDateFormat("yyyy/MM/dd HH:mm:ss");
                        Date date = new Date();

                        String idConversation = id_natter+"-"+contact.getId_natter();

                        Iterator<MessageNatter> i = ((ArrayList<MessageNatter>) esito.getResult()).iterator();
                        while(i.hasNext()){
                            MessageNatter mex = i.next();

                            if(mex.getReceiver().equals(id_natter)) {
                                mex.setTimestamp(dateFormat.format(date));
                            }

                            Dao.inserMessage(db,mex);

                            if(mex.getSender().equals(contact.getId_natter())) {
                                array_message.add(mex);
                            }

                        }

                        getActivity().runOnUiThread(new Runnable() {
                            @Override
                            public void run() {
                                adapter.notifyDataSetChanged();
                                list_mex.setSelection(array_message.size()-1);
                            }
                        });

                        Dao.setReadConversation(db,idConversation);

                        time = 61;

                        db.close();

                    }

                    if(time==0){
                        getActivity().runOnUiThread(new Runnable() {
                            @Override
                            public void run() {
                                adapter.notifyDataSetChanged();
                            }
                        });
                        time = 60;
                    }
                    else{
                        time--;
                    }

                }
            }).start();

            handler.postDelayed(getMessage, Code.ONE_SECOND);
        }
    };

    @Override
    public View onCreateView(LayoutInflater inflater,ViewGroup container,Bundle savedInstanceState){

        DataBaseHelper databaseHelper = new DataBaseHelper(getActivity().getApplicationContext());
        SQLiteDatabase db = databaseHelper.getReadableDatabase();

        this.id_natter = (new Integer(Dao.getIdProfile(db))).toString();

        Hardware.removeNotification(getActivity().getApplicationContext(),this.id_natter+"-"+this.contact.getId_natter(),Code.TYPE_TEXT);

        this.array_message.addAll(Dao.getAllMessageByIdConversation(db,this.id_natter+"-"+this.contact.getId_natter()));

        Dao.setReadConversation(db,this.id_natter+"-"+this.contact.getId_natter());

        db.close();

        //getActivity().getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_STATE_VISIBLE|WindowManager.LayoutParams.SOFT_INPUT_ADJUST_RESIZE|WindowManager.LayoutParams.SOFT_INPUT_ADJUST_PAN);
        getActivity().getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_ADJUST_PAN);

        View rootView = inflater.inflate(R.layout.fragment_chat, container, false);

        this.voice_bottom= (Button) rootView.findViewById(R.id.call_voice_bottom);
        this.map_bottom = (Button) rootView.findViewById(R.id.call_position_bottom);
        this.chat_bottom = (Button) rootView.findViewById(R.id.call_send_bottom);
        this.text = (EditText) rootView.findViewById(R.id.text);

        this.voice_bottom.setOnClickListener(new onClickChatButton());
        this.map_bottom.setOnClickListener(new onClickChatButton());
        this.chat_bottom.setOnClickListener(new onClickSendButton());

        this.text.addTextChangedListener(this);

        this.list_mex = (ListView) rootView.findViewById(R.id.list_mex);
        this.adapter = new CustomMessageAdapter(getActivity().getApplicationContext(),this.array_message,this.id_natter);
        this.list_mex.setAdapter(this.adapter);

        handler.postDelayed(showLastMex,5);
        handler.post(getMessage);
        handler.postDelayed(getLastAccess,Code.ONE_MINUTE);

        return rootView;
    }

    @Override
    public void onDestroyView (){
        super.onDestroyView();
        this.handler.removeCallbacks(getMessage);
        this.handler.removeCallbacks(getLastAccess);
    }

    private void displayView(int position){
        Fragment fragment = null;

        switch(position){
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
        switch (position) {
            case 4:
                this.rootCall.findViewById(R.id.call_voice).setBackgroundResource(R.drawable.ic_voice_selected);
                break;
            case 5:
                Button map = (Button) this.rootCall.findViewById(R.id.call_position);
                map.setBackgroundResource(R.drawable.ic_position_selected);
                map.setClickable(false);
                break;

            default:
                break;
        }

        this.rootCall.findViewById(R.id.call_chat).setBackgroundResource(R.drawable.ic_chat);
    }

    private class onClickChatButton implements View.OnClickListener{

        public void onClick(View v){
            displayView(Integer.parseInt(v.getContentDescription().toString()));
        }
    }

    private class onClickSendButton implements View.OnClickListener{

        public void onClick(View v){

            DataBaseHelper databaseHelper = new DataBaseHelper(getActivity().getApplicationContext());
            SQLiteDatabase db = databaseHelper.getReadableDatabase();

            DateFormat dateFormat = new SimpleDateFormat("yyyy/MM/dd HH:mm:ss");
            Date date = new Date();

            final MessageNatter message = new MessageNatter(id_natter,contact.getId_natter(),text.getText().toString(),dateFormat.format(date));

            array_message.add(message);

            Dao.inserMessage(db,message);

            db.close();

            adapter.notifyDataSetChanged();

            new Thread(new Runnable() {
                @Override
                public void run() {
                    ComunicationService.sendMessage(message);
                }
            }).start();

            UpdateAccess.go(getActivity().getApplicationContext());

            text.setText("");

            InputMethodManager imm = (InputMethodManager) getActivity().getSystemService(Context.INPUT_METHOD_SERVICE);
            imm.hideSoftInputFromWindow(text.getWindowToken(), 0);
        }
    }

    public void afterTextChanged(Editable s){
        this.manageButton();
    }

    @Override
    public void beforeTextChanged(CharSequence s, int start, int count, int after) {this.manageButton();}

    @Override
    public void onTextChanged(CharSequence s, int start, int before, int count) {
        this.manageButton();

        if(text.getText().toString().equals("")){
            UpdateAccess.go(getActivity().getApplicationContext());
        }
        else{
            UpdateAccess.writing(getActivity().getApplicationContext());
        }
    }

    private void manageButton(){
       getActivity().runOnUiThread(new Runnable() {
           @Override
           public void run() {
               if(text.getText().toString().equals("")){
                   voice_bottom.setVisibility(View.VISIBLE);
                   chat_bottom.setVisibility(View.GONE);
               }
               else{
                   voice_bottom.setVisibility(View.GONE);
                   chat_bottom.setVisibility(View.VISIBLE);
               }
           }
       });
    }

    @Override
    public void onResume() {
        super.onResume();
        NatterApplication.activityResumed();
        NatterApplication.setOnLineWith(this.contact.getId_natter());
    }

    @Override
    public void onPause() {
        super.onPause();
        NatterApplication.activityPaused();
        NatterApplication.resetOnLineWith();
    }
}
