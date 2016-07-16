package it.natter.task;

import android.app.Activity;
import android.view.View;
import android.view.animation.AlphaAnimation;
import android.view.animation.Animation;
import android.view.animation.DecelerateInterpolator;
import android.widget.ImageView;
import android.widget.TextView;

import it.natter.R;
import it.natter.classes.Access;
import it.natter.classes.Esito;
import it.natter.rest.ComunicationService;
import it.natter.utility.Code;
import it.natter.utility.Hardware;

/**
 * Created by francesco on 02/05/14.
 */
public class UpdateUserAccess {

    public static void go(final TextView user_access, final Activity activity, final String id_natter_contact, final boolean animation, final ImageView button){
        new Thread(new Runnable() {
            @Override
            public void run() {
                final Esito esito = ComunicationService.getLastAccess(new Access(id_natter_contact, ""));

                if(esito.isFlag()){
                    activity.runOnUiThread(new Runnable() {
                        @Override
                        public void run() {

                            String interval = ((Access) esito.getResult()).getTimestamp();

                            if(!(interval.equals(Code.RECORD))&&(!interval.equals(Code.WRITE))){

                                interval = Hardware.getIntervalDataServer(interval);

                                if (interval.equals("Now")) {
                                    interval = "Online";
                                } else if (interval.contains("sec")) {
                                    interval = "Online";
                                } else {
                                    interval = "Last access " + interval + " ago";
                                }

                                /*if(button!=null) {
                                    activity.runOnUiThread(new Runnable() {
                                        @Override
                                        public void run() {

                                            if(!button.getTag().equals(Code.RED)) {
                                                button.setClickable(true);
                                                button.setImageResource(R.drawable.green);
                                                button.setTag(Code.GREEN);
                                            }
                                        }
                                    });
                                }*/
                            }
                            else{
                                if(button!=null) {
                                    activity.runOnUiThread(new Runnable() {
                                        @Override
                                        public void run() {

                                            if(!button.getTag().equals(Code.RED)) {

                                                button.setClickable(false);
                                                button.setImageResource(R.drawable.yellow);
                                                button.setTag(Code.YELLOW);
                                            }
                                        }
                                    });
                                }
                            }

                            if(animation){
                                Animation fadeIn = new AlphaAnimation(0, 1);
                                fadeIn.setInterpolator(new DecelerateInterpolator());
                                fadeIn.setDuration(1000);
                                user_access.setAnimation(fadeIn);
                            }

                            user_access.setText(interval);

                            user_access.setVisibility(View.VISIBLE);
                        }
                    });
                }
            }
        }).start();

    }
}
