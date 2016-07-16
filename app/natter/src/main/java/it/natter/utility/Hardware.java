package it.natter.utility;

import android.app.Activity;
import android.app.ActivityManager;
import android.app.Notification;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.database.sqlite.SQLiteDatabase;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.Paint;
import android.location.LocationManager;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.Bundle;
import android.os.Environment;
import android.support.v4.app.NotificationCompat;
import android.util.Base64;
import android.util.DisplayMetrics;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.ImageView;

import com.google.android.gms.common.ConnectionResult;
import com.google.android.gms.common.GooglePlayServicesUtil;

import org.apache.commons.io.FileUtils;
import org.apache.http.conn.util.InetAddressUtils;
import org.joda.time.DateTime;
import org.joda.time.Period;
import org.joda.time.format.PeriodFormatter;
import org.joda.time.format.PeriodFormatterBuilder;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.math.BigInteger;
import java.net.InetAddress;
import java.net.NetworkInterface;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.util.Collections;
import java.util.List;

import it.natter.NatterApplication;
import it.natter.R;
import it.natter.SplashScreen;
import it.natter.classes.Contact;
import it.natter.dao.Dao;
import it.natter.dao.DataBaseHelper;

/**
 * Created by francesco on 15/03/14.
 */
public class Hardware{

    private static String folder = "Natter/";
    private static String profile_img = "profile.png";
    private static String path = "/sdcard/"+folder;
    private static String user_folder = "User/";
    private static String audio_folder = "Audio/";
    private static String user_path = "/sdcard/"+folder+user_folder;
    private static String audio_path = "/sdcard/"+folder+audio_folder;

    public static boolean isNetworkAvailable(Context context) {
        ConnectivityManager cm = (ConnectivityManager) context.getSystemService(Context.CONNECTIVITY_SERVICE);
        NetworkInfo networkInfo = cm.getActiveNetworkInfo();
        if (networkInfo != null && networkInfo.isConnected()) {
            return true;
        }

        return false;
    }

    public static void saveProfileImage(Bitmap imageToSave){
        File direct = new File(Environment.getExternalStorageDirectory() + folder);

        if (!direct.exists()) {
            File wallpaperDirectory = new File(path);
            wallpaperDirectory.mkdirs();
        }

        File file = new File(new File(path),profile_img);
        if (file.exists())
            file.delete();
        try{
            FileOutputStream out = new FileOutputStream(file);
            imageToSave.compress(Bitmap.CompressFormat.PNG, 100, out);
            out.flush();
            out.close();
        }catch(Exception e){
            Log.e("SaveImage", e.toString()+" - "+e.getMessage());
        }

    }

    public static Bitmap getProfileImage(Context context){
        File imgFile = new File(path+profile_img);

        try {
            if (imgFile.exists()) {
                Bitmap image = BitmapFactory.decodeFile(imgFile.getAbsolutePath());
                return image;
            }
        }
        catch(NullPointerException e){
            return BitmapFactory.decodeResource(context.getResources(),R.drawable.profile);
        }

        return BitmapFactory.decodeResource(context.getResources(),R.drawable.profile);
    }

    public static boolean deleteProfileImage(){
        File file = new File(path+profile_img);
        return file.delete();
    }

    public static String[] getLocation(Context context){
        LocationTracker gps = new LocationTracker(context);

        if(gps.canGetLocation()){
            String latitude = (new Double(gps.getLatitude())).toString();
            String longitude = (new Double(gps.getLongitude())).toString();

            return new String[]{latitude,longitude};
        }
        else{
            Log.e("Location", "Errore");
            return new String[]{"error","error"};
        }

    }

    public static void managePosition(Context context,SQLiteDatabase db){

        String[] find_me = new String[]{Dao.getLatitude(db),Dao.getLatitude(db)};

        if((find_me[0].equals("null")||find_me[1].equals("null"))||(find_me[0].equals("error")||find_me[1].equals("error"))) {

            find_me = getLocation(context);

            while (find_me[0].equals("error") || find_me[1].equals("error")) {
                find_me = getLocation(context);
            }

            while (find_me[0].equals("0.0") || find_me[1].equals("0.0")) {
                find_me = getLocation(context);
            }

            Dao.updatePosition(db, find_me[0], find_me[1]);
        }
    }

    public static String getIPAddress(boolean useIPv4){
        try{
            List<NetworkInterface> interfaces = Collections.list(NetworkInterface.getNetworkInterfaces());
            for(NetworkInterface intf : interfaces){
                List<InetAddress> addrs = Collections.list(intf.getInetAddresses());
                for(InetAddress addr : addrs){
                    if(!addr.isLoopbackAddress()){
                        String sAddr = addr.getHostAddress().toUpperCase();
                        boolean isIPv4 = InetAddressUtils.isIPv4Address(sAddr);
                        if(useIPv4){
                            if(isIPv4) {
                                return sAddr;
                            }
                        }else{
                            if(!isIPv4){
                                int delim = sAddr.indexOf('%'); // drop ip6 port suffix
                                return delim<0 ? sAddr : sAddr.substring(0, delim);
                            }
                        }
                    }
                }
            }
        }
        catch(Exception ex){
            Log.e("IpAddress",ex.getMessage());
        }
        return "";
    }

    public static String fromImageToString(Context context){
        ByteArrayOutputStream baos = new ByteArrayOutputStream();
        getProfileImage(context).compress(Bitmap.CompressFormat.PNG,100, baos);
        byte [] arr=baos.toByteArray();

        String bitmap = Base64.encodeToString(arr, Base64.URL_SAFE);

        return bitmap;
    }

    public static Bitmap fromStringToImage(String image){
        try{
            byte[] encodeByte = Base64.decode(image,Base64.URL_SAFE);
            Bitmap bitmap = BitmapFactory.decodeByteArray(encodeByte, 0, encodeByte.length);

            return bitmap;
        }catch(Exception e){
            Log.e("fromStringToImage", e.toString() + " - " + e.getLocalizedMessage());
        }

        return null;
    }

    public static void saveContactImage(Contact contact){
        if(contact.hasPhoto()){
            File direct = new File(Environment.getExternalStorageDirectory() + user_folder);

            if (!direct.exists()) {
                File wallpaperDirectory = new File(user_path);
                wallpaperDirectory.mkdirs();
            }

            File file = new File(new File(user_path), contact.getId_phone() + ".png");
            if (file.exists())
                file.delete();
            try {
                FileOutputStream out = new FileOutputStream(file);
                contact.getPhoto().compress(Bitmap.CompressFormat.PNG, 100, out);
                out.flush();
                out.close();
            } catch (Exception e) {
                Log.e("SaveContactImage", e.toString() + " - " + e.getMessage());
            }
        }
    }

    public static Bitmap getUserImage(Context context, String id){
        File imgFile = new File(user_path+id+".png");
        if (imgFile.exists()) {
            Bitmap image = BitmapFactory.decodeFile(imgFile.getAbsolutePath());
            return image;
        }

        return BitmapFactory.decodeResource(context.getResources(),R.drawable.profile);
    }

    public static boolean deleteUserImage(String id){
        File file = new File(user_path+id+".png");
        return file.delete();
    }

    public static boolean deleteAllUserImage(){
        File folder = new File(user_path);

        if (folder.exists()) {
            String[] children = folder.list();
            for (int i = 0; i < children.length; i++) {
                new File(folder, children[i]).delete();
            }
        }

        return folder.delete();
    }

    public static int dpToPx(Context context,int dp) {
        DisplayMetrics displayMetrics = context.getResources().getDisplayMetrics();
        int px = Math.round(dp * (displayMetrics.xdpi / DisplayMetrics.DENSITY_DEFAULT));
        return px;
    }

    public static Bitmap fromLayoutToBitmap(Activity activity, Bitmap user){
        Bitmap bmp = Bitmap.createBitmap(160,180, Bitmap.Config.ARGB_8888);
        Canvas canvas = new Canvas(bmp);

        LayoutInflater inflater = (LayoutInflater) activity.getSystemService(Context.LAYOUT_INFLATER_SERVICE);

        View layout = inflater.inflate(R.layout.custom_marker, null);
        layout.setDrawingCacheEnabled(true);
        ((ImageView) layout.findViewById(R.id.maker_image)).setImageBitmap(user);

        layout.measure(View.MeasureSpec.makeMeasureSpec(canvas.getWidth(),View.MeasureSpec.EXACTLY),View.MeasureSpec.makeMeasureSpec(canvas.getHeight(),View.MeasureSpec.EXACTLY));
        layout.layout(0, 0, layout.getMeasuredWidth(), layout.getMeasuredHeight());

        canvas.drawBitmap(layout.getDrawingCache(), 0, 0, new Paint());

        return bmp;
    }

    public static boolean checkPlayServices(Activity activity){
        int resultCode = GooglePlayServicesUtil.isGooglePlayServicesAvailable(activity);
        if (resultCode != ConnectionResult.SUCCESS) {
            if(GooglePlayServicesUtil.isUserRecoverableError(resultCode)) {
                GooglePlayServicesUtil.getErrorDialog(resultCode,activity,Code.PLAY_SERVICES_RESOLUTION_REQUEST).show();
            }

            return false;
        }
        return true;
    }

    public static void sendNotification(Context context, String sender,int type){

        boolean flag = false;

        if(NatterApplication.getOnLineWith().equals(sender)){
            if(NatterApplication.isFragmentVoiceVisible()){
                if(type==Code.TYPE_TEXT){
                    flag = true;
                }
            }
            else{
                if(type==Code.TYPE_VOICE){
                    flag = true;
                }
            }
        }
        else{
            flag = true;
        }

        if(flag) {
            NotificationManager mNotificationManager = (NotificationManager) context.getSystemService(Context.NOTIFICATION_SERVICE);
            NotificationCompat.Builder notificationBuilder = new NotificationCompat.Builder(context);

            DataBaseHelper databaseHelper = new DataBaseHelper(context);
            SQLiteDatabase db = databaseHelper.getReadableDatabase();

            String allName = Dao.getAllNameByNatterId(db, sender);

            String tempID = "";
            if(type==Code.TYPE_TEXT){
                tempID = Dao.getIdProfile(db) + "-" + sender + "#T";
            }
            else if(type==Code.TYPE_VOICE){
                tempID = Dao.getIdProfile(db) + "-" + sender + "#V";
            }
            final int NOTIFICATION_ID = (tempID).hashCode();

            notificationBuilder.setContentTitle("Natter");

            if (type == Code.TYPE_VOICE) {
                notificationBuilder.setContentText("New audio from " + allName);
            } else if (type == Code.TYPE_TEXT) {
                notificationBuilder.setContentText("New text from " + allName);
            }

            notificationBuilder.setTicker(allName);
            notificationBuilder.setWhen(System.currentTimeMillis());
            notificationBuilder.setSmallIcon(R.drawable.ic_notification);
            notificationBuilder.setLargeIcon(Hardware.getUserImage(context, Dao.getIdPhoneFromIdNatter(db, sender)));

            db.close();

            if (!NatterApplication.isActivityVisible()) {
                Intent toSplashScreen = new Intent(context, SplashScreen.class);
                Bundle bundle = new Bundle();
                bundle.putInt("sender", Integer.parseInt(sender));
                bundle.putInt("type", type);
                toSplashScreen.putExtras(bundle);


                PendingIntent contentIntent = PendingIntent.getActivity(context, 0, toSplashScreen, 0);
                notificationBuilder.setContentIntent(contentIntent);

                notificationBuilder.setDefaults(Notification.DEFAULT_SOUND | Notification.DEFAULT_LIGHTS | Notification.DEFAULT_VIBRATE);
            }

            mNotificationManager.notify(NOTIFICATION_ID, notificationBuilder.build());
        }
    }

    public static void removeNotification(Context context, String idConversation, int type) {

        String tempID = idConversation;
        if(type==Code.TYPE_TEXT){
            tempID = idConversation+"#T";
        }
        else if(type==Code.TYPE_VOICE){
            tempID = idConversation+"#V";
        }

        NotificationManager mNotificationManager = (NotificationManager) context.getSystemService(Context.NOTIFICATION_SERVICE);
        mNotificationManager.cancel(tempID.hashCode());
    }

    public static String getIntervalDataServer(String data){

        String[] mex = data.split(" ");
        String[] first = mex[0].split("-");
        String[] second = mex[1].split(":");
        String third = second[2].substring(0,second.length-1);

        DateTime myBirthDate = new DateTime(Integer.parseInt(first[0]), Integer.parseInt(first[1]), Integer.parseInt(first[2]), Integer.parseInt(second[0]), Integer.parseInt(second[1]), Integer.parseInt(third), 0);
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

    public static String getFolderAudio(){
        File directory = new File(Environment.getExternalStorageDirectory() + audio_path);

        if (!directory.exists()) {
            File audioFolder = new File(audio_path);
            audioFolder.mkdir();
            return audioFolder.getAbsolutePath()+ "/";
        }

        return directory.getAbsolutePath()+ "/";
    }

    public static String getPathForAudionSender(String id_conversation){
        return getFolderAudio()+id_conversation+Code.TYPE_SENDER+Code.FILE_AUDIO;
    }

    public static String getPathForAudionReceiver(String id_conversation){
        return getFolderAudio()+id_conversation+Code.TYPE_RECEIVER+Code.FILE_AUDIO;
    }

    public static String fromFileToString(String id_conversation){
        try {

            File file = new File(getPathForAudionSender(id_conversation));

            byte[] bytes = FileUtils.readFileToByteArray(file);
            String encoded = Base64.encodeToString(bytes,Base64.URL_SAFE);

            /*Log.e("[SENDER] MD5",getMD5EncryptedString(encoded));*/

            return encoded;
        }
        catch(FileNotFoundException e){
            Log.e("FromFileToString",e.toString()+" - "+e.getMessage());
        }
        catch(IOException e){
            Log.e("FromFileToString",e.toString()+" - "+e.getMessage());
        }

        return null;
    }

    public static File fromStringFileToFile(String encoded, String id_conversation){
        try {
            byte[] decoded = Base64.decode(encoded, Base64.URL_SAFE);

            File audio = new File(getPathForAudionReceiver(id_conversation));
            FileOutputStream os = new FileOutputStream(audio, true);
            os.write(decoded);
            os.close();

            /*byte[] bytes_result = FileUtils.readFileToByteArray(new File(getPathForAudionReceiver(id_conversation)));
            String result = Base64.encodeToString(bytes_result,Base64.URL_SAFE);
            Log.e("[RICEVUTO] MD5",getMD5EncryptedString(result));*/

            return new File(getPathForAudionReceiver(id_conversation));
        }
        catch(IOException e){
            Log.e("FromStringToFile",e.toString()+" - "+e.getMessage());
        }

        return null;
    }

    public static void removeAudioMessage(String id_conversation,String type){

        String file = "";

        if(type.equals(Code.TYPE_RECEIVER)){
            file = getPathForAudionReceiver(id_conversation);
        }
        else if(type.equals(Code.TYPE_SENDER)){
            file = getPathForAudionSender(id_conversation);
        }

        File audio = new File(file);
        audio.delete();
    }

    private static String getMD5EncryptedString(String encTarget){
        MessageDigest mdEnc = null;

        try {
            mdEnc = MessageDigest.getInstance("MD5");
        } catch (NoSuchAlgorithmException e) {
            e.printStackTrace();
        }

        mdEnc.update(encTarget.getBytes(), 0, encTarget.length());
        String md5 = new BigInteger(1, mdEnc.digest()).toString(16);

        while ( md5.length() < 32 ) {
            md5 = "0"+md5;
        }

        return md5;
    }

    public static boolean checkAppIsRunning(Context context){

        ActivityManager activityManager = (ActivityManager) context.getSystemService(Context.ACTIVITY_SERVICE);

        List<ActivityManager.RunningTaskInfo> task_list = activityManager.getRunningTasks(Integer.MAX_VALUE);

        for(ActivityManager.RunningTaskInfo task : task_list){

            if(task.topActivity.toString().equals("ComponentInfo{it.natter/it.natter.SplashScreen}"))  return true;
            else if(task.baseActivity.toString().equals("ComponentInfo{it.natter/it.natter.SplashScreen}"))  return true;

        }

        return false;

    }

    public static boolean checkAppIsRunning_RunningApp(Context context) {

        boolean flag = false;

        ActivityManager activityManager = (ActivityManager) context.getSystemService(Context.ACTIVITY_SERVICE);
        List<ActivityManager.RunningAppProcessInfo> procInfos = activityManager.getRunningAppProcesses();

        for (int i = 0; i < procInfos.size(); i++) {
            if (procInfos.get(i).processName.equals("it.natter")) {

                flag = true;

            }
        }

        return flag;

    }

    public static boolean checkLocationHardware(Context context){
        LocationManager locationManager = (LocationManager) context.getSystemService(Context.LOCATION_SERVICE);

        boolean isGPSEnabled = locationManager.isProviderEnabled(LocationManager.GPS_PROVIDER);
        boolean isNetworkEnabled = locationManager.isProviderEnabled(LocationManager.NETWORK_PROVIDER);

        return isGPSEnabled||isNetworkEnabled;
    }

    public static void showDialogToActiveGps(Activity activity){
        CustomDialogGpsHardware dialogImage = new CustomDialogGpsHardware();
        dialogImage.show(activity.getFragmentManager(),"");
    }

    public static String replaceSpecialChar(String string,boolean side){
        if(side){

            char[] array = string.toCharArray();

            String result = "";

            for(int i=0;i<array.length;i++){
                if(array[i]=='à'){
                    result = result + "&agrave";
                }
                else if(array[i]=='á'){
                    result = result + "&aacute";
                }
                else if(array[i]=='è'){
                    result = result + "&egrave";
                }
                else if(array[i]=='é'){
                    result = result + "&eacute";
                }
                else if(array[i]=='ì'){
                    result = result + "&igrave";
                }
                else if(array[i]=='í'){
                    result = result + "&iacute";
                }
                else if(array[i]=='ò'){
                    result = result + "&ograve";
                }
                else if(array[i]=='ó'){
                    result = result + "&oacute";
                }
                else if(array[i]=='ù'){
                    result = result + "&ugrave";
                }
                else if(array[i]=='ú'){
                    result = result + "&uacute";
                }
                else{
                    result = result + array[i];
                }
            }

            return result;
        }
        else{
            char[] array = string.toCharArray();

            String result = "";

            for(int i=0;i<array.length;i++){
                if(array[i]=='&'){

                    i++;

                    if(array[i]=='a'){
                        i++;
                        if(array[i]=='g'){
                            result = result + 'à';
                        }
                        else{
                            result = result + 'á';
                        }
                        i = i + 4;
                    }
                    else if(array[i]=='e'){
                        i++;
                        if(array[i]=='g'){
                            result = result + 'è';
                        }
                        else{
                            result = result + 'é';
                        }
                        i = i + 4;
                    }
                    else if(array[i]=='i'){
                        i++;
                        if(array[i]=='g'){
                            result = result + 'ì';
                        }
                        else{
                            result = result + 'í';
                        }
                        i = i + 4;
                    }
                    else if(array[i]=='o'){
                        i++;
                        if(array[i]=='g'){
                            result = result + 'ò';
                        }
                        else{
                            result = result + 'ó';
                        }
                        i = i + 4;
                    }
                    else if(array[i]=='u'){
                        i++;
                        if(array[i]=='g'){
                            result = result + 'ù';
                        }
                        else{
                            result = result + 'ú';
                        }
                        i = i + 4;
                    }

                }
                else{
                    result = result + array[i];
                }

            }

            return result;
        }
    }

}
