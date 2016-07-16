package it.natter.fragment_call;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import it.natter.NatterApplication;
import it.natter.R;
import it.natter.classes.Contact;

/**
 * Created by francesco on 07/03/14.
 */
public class ProfileUserFragment extends Fragment{

    private Contact contact;

    public ProfileUserFragment(Contact contact){
        this.contact = contact;
    }

    @Override
    public View onCreateView(LayoutInflater inflater,ViewGroup container,Bundle savedInstanceState){

        View rootView = inflater.inflate(R.layout.fragment_profile_user, container, false);

        if(this.contact != null){
            ((ImageView) rootView.findViewById(R.id.user_img)).setImageBitmap(this.contact.getPhoto());
            ((TextView) rootView.findViewById(R.id.nome_user)).setText(this.contact.getAllName());
            ((TextView) rootView.findViewById(R.id.email_user)).setText(this.contact.getEmail());
            ((TextView) rootView.findViewById(R.id.phone_user)).setText(this.contact.getNumber());
        }

        return rootView;
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
