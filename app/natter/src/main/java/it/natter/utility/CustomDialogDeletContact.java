package it.natter.utility;

import android.app.Dialog;
import android.app.DialogFragment;
import android.database.sqlite.SQLiteDatabase;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.Button;

import it.natter.R;
import it.natter.classes.Contact;
import it.natter.dao.Dao;
import it.natter.dao.DataBaseHelper;

/**
 * Created by francesco on 17/03/14.
 */

public class CustomDialogDeletContact extends DialogFragment implements View.OnClickListener{

    private Button trash;
    private Contact contact;

    public CustomDialogDeletContact(Contact contact){
        this.contact = contact;
    }

    @Override
    public Dialog onCreateDialog(Bundle savedInstanceState){
        this.setCancelable(true);

        final Dialog dialog = new Dialog(getActivity());
        dialog.getWindow().requestFeature(Window.FEATURE_NO_TITLE);
        dialog.getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN, WindowManager.LayoutParams.FLAG_FULLSCREEN);
        dialog.setContentView(R.layout.custom_dialog_delet_contact);
        dialog.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));

        this.trash = (Button) dialog.findViewById(R.id.button_trash);

        this.trash.setOnClickListener(this);

        dialog.show();

        return dialog;
    }

    public void onClick(View v){
        DataBaseHelper databaseHelper = new DataBaseHelper(getActivity().getApplicationContext());
        final SQLiteDatabase db = databaseHelper.getWritableDatabase();

        Dao.deletContact(db,contact.getId_phone());
        Hardware.deleteUserImage(contact.getId_phone());

        db.close();

        this.dismiss();
    }

}