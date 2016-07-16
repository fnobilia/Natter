package it.natter.tab;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;

import java.util.ArrayList;

import it.natter.R;
import it.natter.dao.Dao;
import it.natter.dao.DataBaseHelper;
import it.natter.utility.Hardware;

/**
 * Created by francesco on 27/04/14.
 */
public class CustomRecentAdapter extends BaseAdapter {
    private Context context;
    private ArrayList<String> contacts;
    private boolean notRead;

    public CustomRecentAdapter(Context context, ArrayList<String> contacts , boolean notRead) {
        this.context = context;
        this.contacts = contacts;
        this.notRead = notRead;
    }

    public View getView(int position, View convertView, ViewGroup parent) {

        LayoutInflater inflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);

        View gridView;

        if(convertView == null){

            gridView = inflater.inflate(R.layout.custom_grid_recent, null);

            ImageView imageProfile = (ImageView) gridView.findViewById(R.id.recent_image);

            if(notRead) {
                (gridView.findViewById(R.id.recent_notification_conteiner)).setVisibility(View.VISIBLE);
            }
            else{
                (gridView.findViewById(R.id.recent_notification_conteiner)).setVisibility(View.GONE);
            }

            DataBaseHelper databaseHelper = new DataBaseHelper(this.context);
            final SQLiteDatabase db = databaseHelper.getReadableDatabase();

            imageProfile.setImageBitmap(Hardware.getUserImage(this.context, Dao.getIdPhoneFromIdNatter(db,this.contacts.get(position))));

            db.close();


        } else {
            gridView = (View) convertView;
        }

        return gridView;
    }

    @Override
    public int getCount() {
        return this.contacts.size();
    }

    @Override
    public Object getItem(int position) {
        return this.contacts.get(position);
    }

    @Override
    public long getItemId(int position) {
        return 0;
    }

}
