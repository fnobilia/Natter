package it.natter.fragment_call;

import android.database.sqlite.SQLiteDatabase;
import android.media.MediaPlayer;
import android.media.MediaRecorder;
import android.os.Bundle;
import android.os.Handler;
import android.os.Looper;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import java.io.File;
import java.io.IOException;
import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.TimeUnit;

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
public class VoiceFragment extends Fragment implements View.OnTouchListener{

    private Contact contact;

    private MediaRecorder recorder;
    private MediaPlayer player;

    private TextView user_access;
    private ImageView button;

    private String id_natter;
    private String id_conversation;

    private boolean firsTime = true;

    private Handler handler = new Handler();
    private static final ScheduledExecutorService worker = Executors.newSingleThreadScheduledExecutor();

    public VoiceFragment(Contact contact, TextView user_access){

        this.contact = contact;
        this.user_access = user_access;

    }

    @Override
    public View onCreateView(LayoutInflater inflater,ViewGroup container,Bundle savedInstanceState) {

        View rootView = inflater.inflate(R.layout.fragment_voice, container, false);

        this.button = (ImageView) rootView.findViewById(R.id.button_voice);
        this.button.setTag(Code.GREEN);
        this.button.setOnTouchListener(this);

        if(this.contact!=null) {

            UpdateUserAccess.go(user_access, getActivity(), contact.getId_natter(), false, button);

            DataBaseHelper databaseHelper = new DataBaseHelper(getActivity().getApplicationContext());
            SQLiteDatabase db = databaseHelper.getReadableDatabase();

            this.id_natter = Integer.toString(Dao.getIdProfile(db));
            this.id_conversation = this.id_natter+"-"+this.contact.getId_natter();

            db.close();

            handler.post(getVoiceMessage);
            handler.post(getLastAccess);

        }

        return rootView;
    }

    @Override
    public void onDestroyView (){
        super.onDestroyView();
        this.handler.removeCallbacks(getVoiceMessage);
        this.handler.removeCallbacks(getLastAccess);
    }

    @Override
    public boolean onTouch(View v, MotionEvent event) {

        switch (event.getAction()) {
            case MotionEvent.ACTION_UP:

                this.stopRecord();

                this.changeButtonColor(Code.GREEN,true);

                UpdateAccess.go(getActivity().getApplicationContext());

                break;

            case MotionEvent.ACTION_DOWN:

                this.changeButtonColor(Code.RED,true);

                DataBaseHelper databaseHelper = new DataBaseHelper(getActivity().getApplicationContext());
                SQLiteDatabase db = databaseHelper.getWritableDatabase();

                Dao.insertVoice(db,this.contact.getId_natter());

                db.close();

                this.startRecord();

                UpdateAccess.recording(getActivity().getApplicationContext());

                break;
        }

        return false;
    }

    private void startRecord(){

        File file = new File(Hardware.getPathForAudionSender(this.id_conversation));

        try {
            this.recorder = new MediaRecorder();
            this.recorder.setAudioSource(MediaRecorder.AudioSource.MIC);
            this.recorder.setOutputFormat(MediaRecorder.OutputFormat.THREE_GPP);
            this.recorder.setOutputFile(file.getAbsolutePath());
            this.recorder.setAudioEncoder(MediaRecorder.AudioEncoder.AMR_NB);
            this.recorder.prepare();
            this.recorder.start();
        }
        catch(IOException e){
            Toast.makeText(getActivity().getApplicationContext(),"Impossible to record message!",Toast.LENGTH_SHORT).show();
            Log.e("PlayMessage",e.toString()+" - "+e.getMessage());
        }
    }

    private void stopRecord(){

        Runnable task = new Runnable() {
            public void run() {
                recorder.stop();
                recorder.release();

                ComunicationService.sendVoice(new MessageNatter(id_natter, contact.getId_natter(), Hardware.fromFileToString(id_conversation), ""));

                Hardware.removeAudioMessage(id_conversation, Code.TYPE_SENDER);
            }
        };
        worker.schedule(task, 1, TimeUnit.SECONDS);

    }

    private void play(String encode){

        Log.e("VoiceFragment","Play");

        try {

            File file = Hardware.fromStringFileToFile(encode, this.id_conversation);

            this.player = new MediaPlayer();
            this.player.setDataSource(file.getAbsolutePath());
            this.player.prepare();
            this.player.start();

            Runnable task = new Runnable() {
                public void run() {

                    player.release();

                    Hardware.removeAudioMessage(id_conversation, Code.TYPE_RECEIVER);

                    changeButtonColor(Code.GREEN,true);

                    handler.post(getVoiceMessage);

                }
            };
            worker.schedule(task,(this.player.getDuration()+5), TimeUnit.MILLISECONDS);

        }
        catch(IOException e){
            Toast.makeText(getActivity().getApplicationContext(),"Impossible to play message!",Toast.LENGTH_SHORT).show();
            Log.e("PlayMessage",e.toString()+" - "+e.getMessage());
        }
    }

    private Runnable getVoiceMessage = new Runnable(){
        public void run(){

            new Thread(new Runnable() {
                @Override
                public void run() {

                    Looper.prepare();

                    if((button.getTag().equals(Code.YELLOW))||(firsTime)) {

                        firsTime = false;

                        Hardware.removeNotification(getActivity().getApplicationContext(), id_conversation, Code.TYPE_VOICE);

                        Esito esito = ComunicationService.getVoice(new MessageNatter(contact.getId_natter(), id_natter, "", ""));

                        if (esito.isFlag()) {
                            if (esito.getMessage().equals("OK")) {

                                changeButtonColor(Code.RED, false);

                                play(((MessageNatter) esito.getResult()).getMessage());

                                DataBaseHelper databaseHelper = new DataBaseHelper(getActivity().getApplicationContext());
                                SQLiteDatabase db = databaseHelper.getWritableDatabase();

                                Dao.insertVoice(db, contact.getId_natter());

                                db.close();

                            }
                        } else {

                            handler.postDelayed(getVoiceMessage, Code.HALF_SECOND);

                        }
                    }
                    else {

                        handler.postDelayed(getVoiceMessage, Code.HALF_SECOND);

                    }

                }
            }).start();

        }
    };

    private Runnable getLastAccess = new Runnable(){
        public void run(){
            UpdateUserAccess.go(user_access, getActivity(), contact.getId_natter(),false,button);
            handler.postDelayed(getLastAccess,Code.HALF_SECOND);
        }
    };

    private void changeButtonColor(String color,final boolean clickable){
        if(color.equals(Code.RED)){
            getActivity().runOnUiThread(new Runnable() {
                @Override
                public void run() {
                    button.setClickable(clickable);
                    button.setImageResource(R.drawable.red);
                    button.setTag(Code.RED);
                }
            });
        }
        else if(color.equals(Code.YELLOW)){
            getActivity().runOnUiThread(new Runnable() {
                @Override
                public void run() {
                    button.setClickable(clickable);
                    button.setImageResource(R.drawable.yellow);
                    button.setTag(Code.YELLOW);
                }
            });
        }
        else if(color.equals(Code.GREEN)){
            getActivity().runOnUiThread(new Runnable() {
                @Override
                public void run() {
                    button.setClickable(clickable);
                    button.setImageResource(R.drawable.green);
                    button.setTag(Code.GREEN);
                }
            });
        }
    }

    @Override
    public void onResume() {
        super.onResume();

        this.firsTime = true;

        NatterApplication.activityResumed();
        NatterApplication.fragmentVoiceResumed();

        if(this.contact!=null) {
            NatterApplication.setOnLineWith(this.contact.getId_natter());
        }
    }

    @Override
    public void onPause() {
        super.onPause();

        this.firsTime = true;

        NatterApplication.activityPaused();
        NatterApplication.fragmentVoicePaused();
        NatterApplication.resetOnLineWith();
    }


}
