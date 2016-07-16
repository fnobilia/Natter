package it.natter.tab;

import android.content.Context;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.GridView;
import android.widget.ImageView;
import android.widget.TextView;

import java.util.ArrayList;

import it.natter.utility.Hardware;

/**
 * Created by francesco on 09/04/14.
 */
public class CustomOnlineAdapter extends BaseAdapter {

    private Context context;
    private ArrayList<String> contact;
    private TextView text;
    private GridView grid;

    public CustomOnlineAdapter(Context context,ArrayList<String> contact,TextView text, GridView grid){
        this.context = context;
        this.contact = contact;
        this.text = text;
        this.grid = grid;
    }

    @Override
    public int getCount() {
        return this.contact.size();
    }

    @Override
    public Object getItem(int position) {
        return this.contact.get(position);
    }

    @Override
    public long getItemId(int position) {
        return 0;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent){
        ImageView imageView = new ImageView(this.context);
        imageView.setImageBitmap(Hardware.getUserImage(this.context,this.contact.get(position)));
        imageView.setScaleType(ImageView.ScaleType.CENTER_CROP);

        int dp = Hardware.dpToPx(this.context,80);

        imageView.setLayoutParams(new GridView.LayoutParams(dp,dp));
        return imageView;
    }

    public void addItem(String id){
        this.contact.add(id);

        if(this.contact.size()==1){
            this.text.setVisibility(View.GONE);
            this.grid.setVisibility(View.VISIBLE);
        }

        this.notifyDataSetChanged();
    }

}
