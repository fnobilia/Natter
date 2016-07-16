package it.natter.gcm;

import android.app.IntentService;
import android.content.Intent;
import android.database.sqlite.SQLiteDatabase;
import android.media.MediaPlayer;
import android.os.Bundle;
import android.os.Handler;
import android.os.Looper;
import android.util.Log;

import com.google.android.gms.gcm.GoogleCloudMessaging;

import java.io.File;
import java.io.IOException;
import java.util.concurrent.Executors;
import java.util.concurrent.TimeUnit;

import it.natter.NatterApplication;
import it.natter.classes.Esito;
import it.natter.classes.MessageNatter;
import it.natter.dao.Dao;
import it.natter.dao.DataBaseHelper;
import it.natter.rest.ComunicationService;
import it.natter.utility.Code;
import it.natter.utility.Hardware;

/**
 * Created by francesco on 15/04/14.
 */
public class GCMNotificationIntentService extends IntentService {

    private String id_conversation;
    private String id_natter;
    private String id_contact;

    private Handler handler = new Handler();

    public GCMNotificationIntentService() {
        super("GcmIntentService");
    }

    @Override
    protected void onHandleIntent(Intent intent) {

        Bundle extras = intent.getExtras();
        GoogleCloudMessaging gcm = GoogleCloudMessaging.getInstance(this);

        String messageType = gcm.getMessageType(intent);

        if (!extras.isEmpty()){
            if (GoogleCloudMessaging.MESSAGE_TYPE_SEND_ERROR.equals(messageType)) {
                Log.e("GCMNotificationIntentService","Send error: " + extras.toString());
            }
            else if (GoogleCloudMessaging.MESSAGE_TYPE_DELETED.equals(messageType)) {
                Log.e("GCMNotificationIntentService","Deleted messages on server: "+ extras.toString());
            }
            else if (GoogleCloudMessaging.MESSAGE_TYPE_MESSAGE.equals(messageType)){

                //Log.e("GCMNotificationIntentService", "Received: " + extras.toString());

                String[] info = extras.get(Code.MESSAGE_KEY).toString().split("#");

                this.id_contact = info[0];

                boolean voice = false;

                if(info[1].equals("V")) {
                    Hardware.sendNotification(this, this.id_contact, Code.TYPE_VOICE);

                    voice = true;
                }
                else if(info[1].equals("T")) {
                    Hardware.sendNotification(this, this.id_contact, Code.TYPE_TEXT);
                }


                if((!NatterApplication.isFragmentVoiceVisible())&&(voice)){

                    DataBaseHelper databaseHelper = new DataBaseHelper(getApplicationContext());
                    SQLiteDatabase db = databaseHelper.getReadableDatabase();

                    this.id_natter = Integer.toString(Dao.getIdProfile(db));

                    this.id_conversation = this.id_natter+"-"+this.id_contact;

                    if(Dao.getRealtimeSetting(db)==1){
                        this.handler.post(getVoiceMessage);
                    }

                    db.close();
                }

            }
        }
        GcmBroadcastReceiver.completeWakefulIntent(intent);
    }

    private Runnable getVoiceMessage = new Runnable(){
        public void run(){

            new Thread(new Runnable() {
                @Override
                public void run() {

                    Looper.prepare();

                    Hardware.removeNotification(getApplicationContext(),id_conversation,Code.TYPE_VOICE);

                    Esito esito = ComunicationService.getVoice(new MessageNatter(id_contact, id_natter, "", ""));

                    if(esito.isFlag()){
                        if(esito.getMessage().equals("OK")){

                            play(((MessageNatter) esito.getResult()).getMessage());

                            DataBaseHelper databaseHelper = new DataBaseHelper(getApplicationContext());
                            SQLiteDatabase db = databaseHelper.getWritableDatabase();

                            Dao.insertVoice(db,id_contact);

                            db.close();
                        }
                    }

                }
            }).start();

        }
    };

    private void play(String encode){
        try {

            File file = Hardware.fromStringFileToFile(encode, this.id_conversation);

            final MediaPlayer player = new MediaPlayer();
            player.setDataSource(file.getAbsolutePath());
            player.prepare();
            player.start();

            Runnable task = new Runnable() {
                public void run() {
                    player.release();
                    Hardware.removeAudioMessage(id_conversation, Code.TYPE_RECEIVER);
                }
            };
            (Executors.newSingleThreadScheduledExecutor()).schedule(task, (player.getDuration() + 5), TimeUnit.MILLISECONDS);
        }
        catch(IOException e){
            Log.e("GCMNotificationIntentService",e.toString()+" - "+e.getMessage());
        }
    }

}
