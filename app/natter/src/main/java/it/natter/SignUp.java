package it.natter;

import android.content.Intent;
import android.content.IntentSender.SendIntentException;
import android.database.sqlite.SQLiteDatabase;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Color;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.Looper;
import android.support.v4.app.FragmentActivity;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.facebook.Session;
import com.facebook.SessionState;
import com.facebook.UiLifecycleHelper;
import com.facebook.model.GraphUser;
import com.facebook.widget.LoginButton;
import com.google.android.gms.common.ConnectionResult;
import com.google.android.gms.common.GooglePlayServicesUtil;
import com.google.android.gms.common.SignInButton;
import com.google.android.gms.common.api.GoogleApiClient;
import com.google.android.gms.common.api.GoogleApiClient.ConnectionCallbacks;
import com.google.android.gms.common.api.GoogleApiClient.OnConnectionFailedListener;
import com.google.android.gms.plus.Plus;
import com.google.android.gms.plus.model.people.Person;

import org.apache.http.HttpEntity;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.impl.client.DefaultHttpClient;

import java.io.InputStream;
import java.util.Arrays;
import java.util.List;

import it.natter.classes.Esito;
import it.natter.classes.GcmID;
import it.natter.classes.SignIn;
import it.natter.dao.Dao;
import it.natter.dao.DataBaseHelper;
import it.natter.rest.ComunicationService;
import it.natter.rest.UserService;
import it.natter.utility.Code;
import it.natter.utility.Cryptography;
import it.natter.utility.CustomProgress;
import it.natter.utility.Hardware;

/**
 * Created by francesco on 05/03/14.
 */

public class SignUp extends FragmentActivity implements View.OnClickListener,ConnectionCallbacks,OnConnectionFailedListener,TextWatcher{

    private Intent toCore;
    private boolean flag_dialog;

    CustomProgress dialog;
    private static final int PROFILE_PIC_SIZE = 150;
    private SignInButton signInButtonGoogle;
    private GoogleApiClient googleApiClient;
    private static final int RC_SIGN_IN = 0;
    private boolean intentInProgress;
    private boolean signInGoogleClicked;
    private ConnectionResult connectionResult;

    private LoginButton signInFacebook;
    private static final List<String> PERMISSIONS = Arrays.asList("basic_info","email","user_photos");
    private UiLifecycleHelper uiHelper;
    private Session my_session;

    private SignIn signIn;

    private EditText edit_email;
    private EditText edit_password;
    private EditText edit_name;
    private EditText edit_surname;
    private EditText edit_phone;

    private Button done;

    private boolean read = false;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.sign_up);

        this.toCore = new Intent(this,Core.class);

        this.initGoogle();

        this.initFacebook();
        this.uiHelper.onCreate(savedInstanceState);

        this.initEditText();

        this.done = (Button) findViewById(R.id.sign_in);
        this.done.setOnClickListener(this);
    }

    private void initEditText(){
        this.edit_email = (EditText) findViewById(R.id.email_sign_in);
        this.edit_name = (EditText)findViewById(R.id.name_sign_in);
        this.edit_surname = (EditText)findViewById(R.id.surname_sign_in);
        this.edit_phone = (EditText)findViewById(R.id.phone_sign_in);
        this.edit_password = (EditText)findViewById(R.id.passwd_sign_in);

        this.edit_email.addTextChangedListener(this);
        this.edit_name.addTextChangedListener(this);
        this.edit_surname.addTextChangedListener(this);
        this.edit_phone.addTextChangedListener(this);
        this.edit_password.addTextChangedListener(this);
    }

    //Gestione firma
    public void firma(View view){
        Uri uri = Uri.parse("https://twitter.com/FrancescoNobila");
        Intent intent = new Intent(Intent.ACTION_VIEW, uri);
        startActivity(intent);
    }

    private void testoGoogle(){
        for(int i=0;i<signInButtonGoogle.getChildCount();i++){
            View v = signInButtonGoogle.getChildAt(i);
            if(v instanceof TextView){
                TextView tv = (TextView) v;
                tv.setText("Log in with Google +");
                tv.setTextSize(16);
                return;
            }
        }
    }

    @Override
    public void onResume(){
        super.onResume();

        NatterApplication.activityResumed();

        uiHelper.onResume();
        Session.getActiveSession().isOpened();

        if(this.read){
            this.edit_email.setText("");
            this.edit_name.setText("");
            this.edit_surname.setText("");
            this.edit_phone.setText("");
            this.edit_password.setText("");

            this.read = false;
        }
    }

    @Override
    public void onPause() {
        super.onPause();

        NatterApplication.activityPaused();

        uiHelper.onPause();
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        uiHelper.onDestroy();
    }

    protected void onActivityResult(int requestCode, int responseCode,Intent intent){
        this.uiHelper.onActivityResult(requestCode,responseCode,intent);
        if(requestCode == RC_SIGN_IN) {
            if (responseCode != RESULT_OK) {
                this.signInGoogleClicked = false;
            }

            this.intentInProgress = false;

            if (!this.googleApiClient.isConnecting()) {
                this.googleApiClient.connect();
            }
        }
        else if(responseCode == Code.CLOSE_ALL){
            setResult(Code.CLOSE_ALL);
            finish();
        }
        else if(responseCode == Code.CLOSE_UNTILL_LOGIN){
            setResult(Code.CLOSE_UNTILL_LOGIN);
            finish();
        }
    }

    /****** GOOGLE SIGN IN *****/
    private void initGoogle(){
        this.signInButtonGoogle = (SignInButton)findViewById(R.id.google_sign_in);
        this.signInButtonGoogle.setOnClickListener(this);

        this.testoGoogle();

        this.googleApiClient = new GoogleApiClient
                .Builder(this)
                .addConnectionCallbacks(this)
                .addOnConnectionFailedListener(this)
                .addApi(Plus.API, null)
                .addScope(Plus.SCOPE_PLUS_LOGIN)
                .build();
    }

    protected void onStart(){
        super.onStart();
        this.googleApiClient.connect();
    }

    protected void onStop(){
        super.onStop();
        if(this.googleApiClient.isConnected()){
            this.googleApiClient.disconnect();
        }
    }

    public void onConnectionFailed(ConnectionResult result){
        if (!result.hasResolution()){
            GooglePlayServicesUtil.getErrorDialog(result.getErrorCode(),this,0).show();
            return;
        }

        if(!this.intentInProgress){
            this.connectionResult = result;

            if(this.signInGoogleClicked){
                resolveSignInError();
            }
        }
    }

    private void resolveSignInError(){
        if(this.connectionResult.hasResolution()){

            this.showDialog("We are contacting Google...");

            try{
                this.intentInProgress = true;
                this.connectionResult.startResolutionForResult(this, RC_SIGN_IN);
            }catch(SendIntentException e){
                this.intentInProgress = false;
                this.googleApiClient.connect();
            }
        }
    }

    public void onConnected(Bundle arg0){
        this.signInGoogleClicked = false;

        this.goGoogle();
    }

    private void goGoogle(){
        this.updateProfileInformation();
    }

    private void updateProfileInformation(){
        try{
            if(Plus.PeopleApi.getCurrentPerson(this.googleApiClient) != null){

                Hardware.deleteProfileImage();

                Person currentPerson = Plus.PeopleApi.getCurrentPerson(this.googleApiClient);

                String[] personName = currentPerson.getDisplayName().split(" ");
                String name = "";
                String surname = "";
                for (int i = 0; i < personName.length; i++) {
                    if (i == 0) {
                        name = personName[0];
                    } else {
                        surname = surname + personName[i];
                    }
                }

                String email = Plus.AccountApi.getAccountName(this.googleApiClient);
                String personPhotoUrl = currentPerson.getImage().getUrl();
                personPhotoUrl = personPhotoUrl.substring(0, (personPhotoUrl.length() - 2));
                personPhotoUrl = personPhotoUrl + "80";

                personPhotoUrl = personPhotoUrl.substring(0, personPhotoUrl.length() - 2) + PROFILE_PIC_SIZE;

                new LoadProfileImage().execute(personPhotoUrl);

                boolean[] flag = {false, false, false, true};
                this.signIn = new SignIn(email,Code.NOT_STATED,name,surname,Code.NOT_STATED,flag,Code.NOT_STATED,Code.GOOGLE);

                disconnectAccount();
            }else{
                if(flag_dialog){
                    dialog.dismiss();
                }
                Toast.makeText(getApplicationContext(), "Problem with Google connection", Toast.LENGTH_SHORT).show();
            }
        } catch (Exception e) {
            Log.e("GOOGLE", e.toString());
        }
    }

    private void disconnectAccount() {
        if(this.googleApiClient.isConnected()) {
            Plus.AccountApi.clearDefaultAccount(this.googleApiClient);
            this.googleApiClient.disconnect();
            this.googleApiClient.connect();
        }
    }

    public void onConnectionSuspended(int cause) {
        this.googleApiClient.connect();
    }

    public void onClick(View v){
        if(v.getContentDescription().toString().equals("done_signup")){
            boolean[] flag = {true, true, true, true};
            String email = this.edit_email.getText().toString().trim();
            String name = this.edit_name.getText().toString().trim();
            String surname = this.edit_surname.getText().toString().trim();
            String phone = this.edit_phone.getText().toString().trim();
            String password = this.edit_password.getText().toString().trim();

            signIn = new SignIn(email,password,name,surname,phone,flag,Code.NOT_STATED,Code.APP);

            this.showDialog("We are saving your data..");

            this.callLogin();
        }
        else if(v.getContentDescription().toString().equals("google_signup")){
            this.signInWithGplus();
        }
    }

    private void signInWithGplus(){
        if(!this.googleApiClient.isConnecting()){
            this.signInGoogleClicked= true;
            resolveSignInError();
        }
    }
    /*********** FINE **********/

    /********* FACEBOOK ********/
    private void initFacebook(){
        Hardware.deleteProfileImage();

        this.uiHelper= new UiLifecycleHelper(this, statusCallback);
        this.signInFacebook = (LoginButton) findViewById(R.id.facebook_sign_in);
        this.signInFacebook.setReadPermissions(this.PERMISSIONS);
        this.signInFacebook.setUserInfoChangedCallback(new LoginButton.UserInfoChangedCallback() {
            @Override
            public void onUserInfoFetched(GraphUser user) {
                if (user != null){
                    String email = Code.NOT_STATED;
                    if(user.asMap().get("email") != null) email = user.asMap().get("email").toString();;

                    new LoadProfileImage().execute("http://graph.facebook.com/" + user.getId().toString() + "/picture?width="+PROFILE_PIC_SIZE+"&height="+PROFILE_PIC_SIZE);

                    boolean[] flag = {false, false, false, true};
                    signIn = new SignIn(email,Code.NOT_STATED,user.getFirstName(),user.getLastName(),Code.NOT_STATED,flag,Code.NOT_STATED,Code.FACEBOOK);

                    my_session.closeAndClearTokenInformation();
                }
            }
        });
    }

    private Session.StatusCallback statusCallback = new Session.StatusCallback() {
        @Override
        public void call(Session session, SessionState state,Exception exception) {
            if (state.isOpened()){
                showDialog("We are contacting Facebook..");
                my_session = session;
            } else if (state.isClosed()) {
            }
        }
    };
    /*********** FINE **********/

    private class LoadProfileImage extends AsyncTask<String, Void, Bitmap>{

        public LoadProfileImage(){}

        protected Bitmap doInBackground(String... urls){
            String urldisplay = urls[0];
            Bitmap bitmap = null;
            try{
                /*HttpGet httpRequest = new HttpGet(urldisplay);
                HttpClient httpclient = new DefaultHttpClient();
                HttpResponse response = (HttpResponse) httpclient.execute(httpRequest);
                final HttpEntity entity = response.getEntity();*/

                final HttpEntity entity = ((new DefaultHttpClient()).execute(new HttpGet(urldisplay))).getEntity();

                if(entity != null){
                    InputStream inputStream = entity.getContent();
                    BitmapFactory.Options options = new BitmapFactory.Options();
                    bitmap = BitmapFactory.decodeStream(inputStream, null, options);

                    return bitmap;
                }
            }catch(Exception e){
                Log.e("LoadProfileImage", e.toString()+" - "+e.getMessage());
                e.printStackTrace();
            }
            return bitmap;
        }

        protected void onPostExecute(Bitmap result){
            Hardware.saveProfileImage(result);
            signIn.setImage(Hardware.fromImageToString(getApplicationContext()));
            callLogin();
        }
    }

    private void showDialog(String mex){
        final String temp = mex;
        runOnUiThread(new Runnable() {
            @Override
            public void run(){
                dialog = new CustomProgress(temp);
                dialog.show(getFragmentManager(), "");
                flag_dialog = true;
            }
        });
    }

    private void callLogin(){
        new Thread(new Runnable() {
            @Override
            public void run() {
                Looper.prepare();

                signIn.setPassword(Cryptography.md5(signIn.getPassword()));

                final Esito esito = UserService.signIn(signIn);

                read = true;

                if(esito.isFlag()){
                    DataBaseHelper databaseHelper = new DataBaseHelper(getApplicationContext());
                    final SQLiteDatabase db = databaseHelper.getWritableDatabase();

                    Dao.initProfile(db);

                    String[] data = {((Integer)esito.getResult()).toString(),signIn.getPassword(),signIn.getEmail(),signIn.getName(),signIn.getSurname(),signIn.getPhone()};
                    Dao.insertProfile(db, data, signIn.getFlag());

                    Esito esito_gcm = ComunicationService.registerGCM(new GcmID(Dao.getIdProfile(db), Dao.getIdGmc(db)));

                    db.close();

                    if(esito_gcm.isFlag()){
                        if(flag_dialog){
                            dialog.dismiss();
                        }

                        startActivityForResult(toCore,Code.CLOSE_ALL);
                    }
                    else{
                        runOnUiThread(new Runnable() {
                            public void run(){
                                if(flag_dialog) {
                                    dialog.dismiss();
                                }

                                if(esito.getResult().toString().equals("Error")){
                                    Toast.makeText(getApplicationContext(),esito.getMessage(), Toast.LENGTH_LONG).show();
                                }
                                else{
                                    Toast.makeText(getApplicationContext(), "Service temporarily unavailable!", Toast.LENGTH_LONG).show();
                                }
                            }
                        });
                    }
                }
                else{
                    runOnUiThread(new Runnable() {
                        public void run(){
                            if(flag_dialog) {
                                dialog.dismiss();
                            }

                            if(esito.getResult().toString().equals("Error")){
                                Toast.makeText(getApplicationContext(),esito.getMessage(), Toast.LENGTH_LONG).show();
                            }
                            else{
                                Toast.makeText(getApplicationContext(), "Service temporarily unavailable!", Toast.LENGTH_LONG).show();
                            }
                        }
                    });
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

        if(this.edit_email.getText().toString().equals("")){
            enable = false;
        }
        String password = this.edit_password.getText().toString();
        if(password.equals("")||(password.length()<6)){
            enable = false;
        }
        if(this.edit_name.getText().toString().equals("")){
            enable = false;
        }
        if(this.edit_surname.getText().toString().equals("")){
            enable = false;
        }
        if(this.edit_phone.getText().toString().equals("")){
            enable = false;
        }

        if(enable){
            this.done.setTextColor(Color.parseColor("#000000"));
        }
        else{
            this.done.setTextColor(Color.parseColor("#55AFAFAF"));
        }

        this.done.setEnabled(enable);
        this.done.setClickable(enable);
    }
}
