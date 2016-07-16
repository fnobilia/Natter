package it.natter.fragment;

import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import it.natter.NatterApplication;
import it.natter.R;
import it.natter.classes.Access;
import it.natter.classes.GcmID;
import it.natter.dao.Dao;
import it.natter.dao.DataBaseHelper;
import it.natter.rest.ComunicationService;
import it.natter.task.UpdateAccess;
import it.natter.utility.Code;
import it.natter.utility.CustomDialogGetProfileImage;
import it.natter.utility.Hardware;

/**
 * Created by francesco on 07/03/14.
 */
public class ProfileFragment extends Fragment implements  View.OnClickListener,View.OnLongClickListener,TextWatcher{

    private int email_edit;
    private int phone_edit;

    private View root;
    private Button buttonEdit;
    private ImageView image;

    public ProfileFragment(){}

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,Bundle savedInstanceState){

        UpdateAccess.go(getActivity().getApplicationContext());

        this.root = inflater.inflate(R.layout.fragment_profile, container, false);

        DataBaseHelper databaseHelper = new DataBaseHelper(getActivity());
        final SQLiteDatabase db = databaseHelper.getWritableDatabase();

        ((TextView) this.root.findViewById(R.id.nome_profile)).setText(Dao.getNameProfile(db)+" "+Dao.getSurnameProfile(db));
        ((TextView) this.root.findViewById(R.id.email_profile)).setText(Dao.getEmailProfile(db));
        ((TextView) this.root.findViewById(R.id.phone_profile)).setText(Dao.getPhoneProfile(db));

        this.image = (ImageView) this.root.findViewById(R.id.profile_img);
        this.image.setImageBitmap(Hardware.getProfileImage(getActivity().getApplicationContext()));
        this.image.setOnLongClickListener(this);

        this.email_edit = Dao.getEmailEditableProfile(db);
        this.phone_edit = Dao.getPhoneEditableProfile(db);

        this.buttonEdit = (Button)this.root.findViewById(R.id.profile_button);
        this.buttonEdit.setOnClickListener(this);

        (this.root.findViewById(R.id.logout_button)).setOnClickListener(this);

        ((EditText) this.root.findViewById(R.id.edit_email_profile)).addTextChangedListener(this);
        ((EditText) this.root.findViewById(R.id.edit_phone_profile)).addTextChangedListener(this);
        ((EditText) this.root.findViewById(R.id.edit_passwd_profile)).addTextChangedListener(this);

        db.close();

        return this.root;

    }

    public void onClick(View view){
        if(view.getContentDescription().toString().equals("edit")){
            RelativeLayout editLayout = (RelativeLayout) this.root.findViewById(R.id.edit_profile);
            switch (editLayout.getVisibility()) {
                case View.INVISIBLE:
                    editLayout.setVisibility(View.VISIBLE);

                    if (this.email_edit == 0)
                        (this.root.findViewById(R.id.edit_email_profile)).setVisibility(View.GONE);
                    if (this.phone_edit == 0)
                        (this.root.findViewById(R.id.edit_phone_profile)).setVisibility(View.GONE);

                    this.buttonEdit.setBackgroundResource(R.drawable.list_item_bg_pressed);
                    break;
                case View.VISIBLE:
                    editLayout.setVisibility(View.INVISIBLE);
                    this.buttonEdit.setBackgroundResource(R.drawable.bottone_done);
                    break;
            }
        }
        else if(view.getContentDescription().toString().equals("logout")){
            DataBaseHelper databaseHelper = new DataBaseHelper(getActivity());
            final SQLiteDatabase db = databaseHelper.getWritableDatabase();

            new Thread(new Runnable() {
                @Override
                public void run() {

                    int id_natter = Dao.getIdProfile(db);

                    ComunicationService.removeGCM(new GcmID(id_natter,Dao.getIdGmc(db)));
                    ComunicationService.deleteLastAccess(new Access(Integer.toString(id_natter),""));

                }
            }).start();

            Dao.intAfterLogoutDb(db);
            db.close();

            Hardware.deleteProfileImage();

            getActivity().setResult(Code.CLOSE_UNTILL_LOGIN);
            getActivity().finish();
        }
    }

    public boolean onLongClick(View view){
        CustomDialogGetProfileImage dialogImage = new CustomDialogGetProfileImage(this.root);
        dialogImage.show(getActivity().getFragmentManager(),"");
        this.image.setImageResource(R.drawable.ic_add_profile);
        return true;
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
        boolean enable = false;
        if(this.email_edit==1){
            if(!(((EditText)this.root.findViewById(R.id.edit_email_profile)).getText().toString().equals(""))){
                enable = enable || true;
            }
        }
        if(this.phone_edit==1){
            if(!(((EditText)this.root.findViewById(R.id.edit_phone_profile)).getText().toString().equals(""))){
                enable = enable || true;
            }
        }

        String password = ((EditText) this.root.findViewById(R.id.edit_passwd_profile)).getText().toString();
        if((!(password.equals("")))||(password.length()>5)){
            enable = enable || true;
        }

        Button button = (Button) this.root.findViewById(R.id.done_profile);

        button.setEnabled(enable);
        button.setClickable(enable);
    }

    @Override
    public void onResume() {
        super.onResume();
        this.image.setImageBitmap(Hardware.getProfileImage(getActivity().getApplicationContext()));
        NatterApplication.activityResumed();
    }

    @Override
    public void onPause() {
        super.onPause();
        NatterApplication.activityPaused();
    }
}
