package it.natter.fragment_call;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import org.joda.time.DateTime;
import org.joda.time.Period;
import org.joda.time.format.PeriodFormatter;
import org.joda.time.format.PeriodFormatterBuilder;

import java.util.ArrayList;

import it.natter.R;
import it.natter.classes.MessageNatter;
import it.natter.dao.Dao;
import it.natter.dao.DataBaseHelper;
import it.natter.utility.Hardware;

/**
 * Created by francesco on 09/04/14.
 */
public class CustomMessageAdapter extends ArrayAdapter<MessageNatter> {
    private Context context;
    private ArrayList<MessageNatter> messages;
    private String id;

    public CustomMessageAdapter(Context context, ArrayList<MessageNatter> messages, String id){
        super(context, R.layout.message_item_friend, messages);
        this.context = context;
        this.messages = messages;
        this.id = id;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {

        LayoutInflater inflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        MessageNatter mex = this.messages.get(position);

        View rowView;

        if(mex.getSender().equals(this.id)){
            rowView = inflater.inflate(R.layout.message_item_me, parent, false);
            ((ImageView) rowView.findViewById(R.id.image_mex_me)).setImageBitmap(Hardware.getProfileImage(this.context));
            ((TextView) rowView.findViewById(R.id.text_mex_me)).setText(Hardware.replaceSpecialChar(mex.getMessage(),false));
            ((TextView) rowView.findViewById(R.id.time_mex_me)).setText(this.getTimestamp(mex.getTimestamp()));
        }
        else{
            DataBaseHelper databaseHelper = new DataBaseHelper(this.context);
            SQLiteDatabase db = databaseHelper.getReadableDatabase();

            rowView = inflater.inflate(R.layout.message_item_friend, parent, false);
            ((ImageView) rowView.findViewById(R.id.image_mex_friend)).setImageBitmap(Hardware.getUserImage(this.context, Dao.getIdPhoneFromIdNatter(db, mex.getSender())));
            ((TextView) rowView.findViewById(R.id.text_mex_friend)).setText(mex.getMessage());
            ((TextView) rowView.findViewById(R.id.time_mex_friend)).setText(this.getTimestamp(mex.getTimestamp()));

            db.close();
        }

        return rowView;
    }

    private String getTimestamp(String data){

        String[] mex = data.split(" ");
        String[] first = mex[0].split("/");
        String[] second = mex[1].split(":");

        DateTime myBirthDate = new DateTime(Integer.parseInt(first[0]), Integer.parseInt(first[1]), Integer.parseInt(first[2]), Integer.parseInt(second[0]), Integer.parseInt(second[1]), Integer.parseInt(second[2]), 0);
        DateTime now = new DateTime();
        Period period = new Period(myBirthDate, now);

        PeriodFormatter formatter = new PeriodFormatterBuilder().appendYears().appendSuffix("years").printZeroNever().toFormatter();
        String temp = formatter.print(period);
        if(temp==""){
            formatter = new PeriodFormatterBuilder().appendMonths().appendSuffix("months").printZeroNever().toFormatter();
            temp = formatter.print(period);
            if(temp==""){
                formatter = new PeriodFormatterBuilder().appendWeeks().appendSuffix("weeks").printZeroNever().toFormatter();
                temp = formatter.print(period);
                if(temp==""){
                    formatter = new PeriodFormatterBuilder().appendDays().appendSuffix("days").printZeroNever().toFormatter();
                    temp = formatter.print(period);
                    if(temp==""){
                        formatter = new PeriodFormatterBuilder().appendHours().appendSuffix("hours").printZeroNever().toFormatter();
                        temp = formatter.print(period);
                        if(temp==""){
                            formatter = new PeriodFormatterBuilder().appendMinutes().appendSuffix("min").printZeroNever().toFormatter();
                            temp = formatter.print(period);
                            if(temp==""){
                                formatter = new PeriodFormatterBuilder().appendSeconds().appendSuffix("sec").printZeroNever().toFormatter();
                                temp = formatter.print(period);
                                if(temp==""){
                                    temp = "Now";
                                }
                            }
                        }
                    }
                }
            }
        }

        return temp;
    }


}
