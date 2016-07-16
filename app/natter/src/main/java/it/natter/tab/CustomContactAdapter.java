package it.natter.tab;

import android.app.Activity;
import android.content.Context;
import android.os.Looper;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import java.util.List;

import it.natter.R;
import it.natter.classes.Contact;
import it.natter.utility.Hardware;

/**
 * Created by francesco on 22/03/14.
 */
public class CustomContactAdapter extends ArrayAdapter<Contact>{

    private Context context;
    private List<Contact> contacts;
    private Activity activity;

    public CustomContactAdapter(Activity acivity, int textViewResourceId, List<Contact> c){
        super(acivity.getApplicationContext(), textViewResourceId,c);
        this.context = acivity.getApplicationContext();
        this.contacts = c;
        this.activity = acivity;
    }

    /*private view holder class*/
    private class ViewHolder{
        ImageView image;
        TextView name;
        TextView email;
        TextView mobile;
        ImageView isNatter;
        Button delete;
    }

    public View getView(int position, View convertView, ViewGroup parent){
        ViewHolder holder = null;
        final Contact rowItem = getItem(position);

        LayoutInflater mInflater = (LayoutInflater) context.getSystemService(Activity.LAYOUT_INFLATER_SERVICE);
        if (convertView == null) {
            convertView = mInflater.inflate(R.layout.contact_list_item, null);
            holder = new ViewHolder();
            holder.image = (ImageView) convertView.findViewById(R.id.image_contact);
            holder.name = (TextView) convertView.findViewById(R.id.name_surname_contact);
            holder.email = (TextView) convertView.findViewById(R.id.email_contact);
            holder.mobile = (TextView) convertView.findViewById(R.id.phone_contact);
            holder.isNatter = (ImageView) convertView.findViewById(R.id.is_natter);
            holder.delete = (Button) convertView.findViewById(R.id.delete_contact);
            convertView.setTag(holder);
        } else {
            holder = (ViewHolder) convertView.getTag();
        }

        final ImageView temp_image = holder.image;
        new Thread(new Runnable(){
            @Override
            public void run(){
                Looper.prepare();
                activity.runOnUiThread(new Runnable() {
                    public void run() {
                    temp_image.setImageBitmap(Hardware.getUserImage(context,rowItem.getId_phone()));
                }});
            }
        }).start();

        if(!rowItem.getId_natter().equals("")){
            holder.isNatter.setVisibility(View.VISIBLE);
        }
        else{
            holder.isNatter.setVisibility(View.GONE);
        }

        holder.name.setText(rowItem.getAllName());

        holder.email.setText(rowItem.getEmail());

        holder.mobile.setText(rowItem.getNumber());

        /*if(rowItem.isErasable())
            holder.delete.setVisibility(View.VISIBLE);
        else
            holder.delete.setVisibility(View.GONE);*/

        return convertView;
    }
}
