package it.natter.utility;

import android.app.Dialog;
import android.app.DialogFragment;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.view.Window;
import android.view.WindowManager;
import android.widget.TextView;

import it.natter.R;

/**
 * Created by francesco on 17/03/14.
 */

public class CustomProgress extends DialogFragment{

    private String text;

    public CustomProgress(String mex){
        this.text = mex;
    }

    @Override
    public Dialog onCreateDialog(Bundle savedInstanceState){
        this.setCancelable(false);

        final Dialog dialog = new Dialog(getActivity());

        dialog.getWindow().requestFeature(Window.FEATURE_NO_TITLE);
        dialog.getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN,
                WindowManager.LayoutParams.FLAG_FULLSCREEN);
        dialog.setContentView(R.layout.custom_progress_dialog);
        dialog.getWindow().setBackgroundDrawable(
                new ColorDrawable(Color.TRANSPARENT));
        dialog.show();
        TextView message = (TextView) dialog.findViewById(R.id.text_progress);
        message.setText(text);

        return dialog;
    }

}