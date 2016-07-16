package it.natter;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;

import it.natter.serviceTask.NatterService;
import it.natter.serviceTask.RunningService;

/**
 * Created by francesco on 23/03/14.
 */
public class BroadcastReceiverNatter extends BroadcastReceiver{
    @Override
    public void onReceive(Context context, Intent intent){

        if(intent.getAction().equalsIgnoreCase(Intent.ACTION_BOOT_COMPLETED)){
            Intent serviceIntent = new Intent(context, NatterService.class);
            context.startService(serviceIntent);
        }

        Intent contactIntent = new Intent(context, RunningService.class);
        context.startService(contactIntent);
    }
}
