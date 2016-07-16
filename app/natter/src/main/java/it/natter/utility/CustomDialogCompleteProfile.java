package it.natter.utility;

import android.app.Dialog;
import android.app.DialogFragment;
import android.database.sqlite.SQLiteDatabase;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.os.Looper;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import it.natter.R;
import it.natter.classes.Esito;
import it.natter.classes.UpdateEmail;
import it.natter.classes.UpdatePhone;
import it.natter.dao.Dao;
import it.natter.rest.UserService;

/**
 * Created by francesco on 17/03/14.
 */

public class CustomDialogCompleteProfile extends DialogFragment implements View.OnClickListener,TextWatcher{

    private Button button;
    private EditText email;
    private EditText phone;
    private boolean email_visible = true;

    private SQLiteDatabase db;

    public CustomDialogCompleteProfile(SQLiteDatabase database){
        this.db = database;
    }

    @Override
    public Dialog onCreateDialog(Bundle savedInstanceState){
        this.setCancelable(false);

        final Dialog dialog = new Dialog(getActivity());
        dialog.getWindow().requestFeature(Window.FEATURE_NO_TITLE);
        dialog.getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN, WindowManager.LayoutParams.FLAG_FULLSCREEN);
        dialog.setContentView(R.layout.custom_dialog_completed_profile);
        dialog.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));

        this.button = (Button) dialog.findViewById(R.id.button_completed_profile);
        this.button.setTextColor(Color.parseColor("#55AFAFAF"));
        this.button.setOnClickListener(this);

        this.email = (EditText) dialog.findViewById(R.id.email_profile_completed);
        this.phone = (EditText) dialog.findViewById(R.id.phone_profile_completed);
        this.email.addTextChangedListener(this);
        this.phone.addTextChangedListener(this);

        if(!Dao.getEmailProfile(db).equals("Not stated")) {
            dialog.findViewById(R.id.email_relative_completed).setVisibility(View.GONE);
            this.email_visible = false;
        }

        dialog.show();

        return dialog;
    }

    public void onClick(View v){
        final CustomProgress progress = new CustomProgress("We are updating your profile...");
        progress.show(getFragmentManager(), "");

        new Thread(new Runnable() {
            @Override
            public void run() {
                Looper.prepare();

                Esito esito = new Esito();
                boolean outcome = true;

                String email_value;
                boolean email_editable = false;

                int id_value = Dao.getIdProfile(db);

                if(email_visible) {
                    email_value = email.getText().toString();
                    email_editable = true;

                    esito = UserService.updateEmail(new UpdateEmail(id_value, email_value, email_editable));
                    outcome = outcome && esito.isFlag();
                } else {
                    email_value = Dao.getEmailProfile(db);
                }

                String name_value = Dao.getNameProfile(db);
                String surname_value = Dao.getSurnameProfile(db);
                String phone_value = phone.getText().toString();

                esito = UserService.updatePhone(new UpdatePhone(id_value, phone_value, true));
                outcome = outcome && esito.isFlag();

                if(outcome){
                    Dao.initProfile(db);

                    String[] data = {Integer.toString(id_value), "", email_value, name_value, surname_value, phone_value};
                    boolean[] flag = {email_editable, false, false, true};
                    Dao.insertProfile(db, data, flag);

                    db.close();

                    progress.dismiss();
                    dismiss();
                }
                else{
                    db.close();

                    final String result_value = esito.getResult().toString();
                    final String message = esito.getMessage();

                    getActivity().runOnUiThread(new Runnable() {
                        @Override
                        public void run(){
                            if(result_value.equals("Error")){
                                Toast.makeText(getActivity().getApplicationContext(),message, Toast.LENGTH_LONG).show();
                            }
                            else{
                                Toast.makeText(getActivity().getApplicationContext(), "Service temporarily unavailable!", Toast.LENGTH_LONG).show();
                            }
                        }
                    });

                    new Thread(new Runnable(){
                        @Override
                        public void run(){
                            Looper.prepare();
                            try{
                                Thread.sleep(500);

                                progress.dismiss();
                                dismiss();

                                getActivity().setResult(Code.CLOSE_ALL);
                                getActivity().finish();
                            }
                            catch(InterruptedException e){
                                Log.e("Login", e.toString() + " - " + e.getLocalizedMessage());
                            }
                        }
                    }).start();
                }
            }
        }).start();
    }

    public void afterTextChanged(Editable s){
        this.manageButton();
    }

    @Override
    public void beforeTextChanged(CharSequence s, int start, int count, int after) {
    }

    @Override
    public void onTextChanged(CharSequence s, int start, int before, int count) {
    }

    private void manageButton(){
        boolean enable = true;
        if(this.email_visible){
            if(this.email.getText().toString().equals("")){
                enable = false;
            }
        }
        if(this.phone.getText().toString().equals("")) {
            enable = false;
        }

        if(enable){
            this.button.setTextColor(Color.parseColor("#000000"));
        }
        else{
            this.button.setTextColor(Color.parseColor("#55AFAFAF"));
        }

        this.button.setEnabled(enable);
        this.button.setClickable(enable);
    }
}