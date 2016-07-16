package it.natter.utility;

import android.app.Dialog;
import android.app.DialogFragment;
import android.content.Intent;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.provider.Settings;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;

import it.natter.R;

/**
 * Created by francesco on 17/03/14.
 */

public class CustomDialogGpsHardware extends DialogFragment{

    public CustomDialogGpsHardware(){
    }

    @Override
    public Dialog onCreateDialog(Bundle savedInstanceState){
        this.setCancelable(false);

        final Dialog dialog = new Dialog(getActivity());
        dialog.getWindow().requestFeature(Window.FEATURE_NO_TITLE);
        dialog.getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN, WindowManager.LayoutParams.FLAG_FULLSCREEN);
        dialog.setContentView(R.layout.custom_dialog_gps_hardware);
        dialog.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));

        (dialog.findViewById(R.id.go_to_settings)).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(Settings.ACTION_LOCATION_SOURCE_SETTINGS);
                startActivity(intent);
                dialog.cancel();
            }
        });

        (dialog.findViewById(R.id.cancel_gps)).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dialog.cancel();
            }
        });

        dialog.show();

        return dialog;
    }

}