package it.natter;

import android.app.Activity;
import android.content.Intent;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.os.Looper;
import android.view.View;
import android.widget.Toast;

import it.natter.classes.Esito;
import it.natter.classes.GcmID;
import it.natter.classes.Login;
import it.natter.dao.Dao;
import it.natter.dao.DataBaseHelper;
import it.natter.gcm.GMCRegistration;
import it.natter.rest.ComunicationService;
import it.natter.rest.UserService;
import it.natter.serviceTask.NatterService;
import it.natter.serviceTask.RunningService;
import it.natter.utility.Code;
import it.natter.utility.Hardware;

public class SplashScreen extends Activity{

    private Login login_class;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.splash_screen);

        NatterApplication.resetOnLineWith();

        if(Hardware.checkPlayServices(this)){

            new Thread(new Runnable() {
                @Override
                public void run() {
                    DataBaseHelper databaseHelper = new DataBaseHelper(getApplicationContext());
                    final SQLiteDatabase db = databaseHelper.getWritableDatabase();

                    init_set_DB(db);

                    GMCRegistration.registerGCM(getApplicationContext());

                    startService();

                    if (!Dao.isEmptyProfileTable(db)) {
                        login_class = new Login(Dao.getEmailProfile(db), Dao.getPasswordProfile(db));
                        db.close();

                        login();
                    } else {
                        final Intent toMain = new Intent(getApplicationContext(), MainActivity.class);
                        startActivityForResult(toMain, Code.CLOSE_SPLASH);
                    }

                    db.close();
                }
            }).start();

        }
        else{
            this.infoToast("You have to update Google Play Service to use Natter!");
        }
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

    protected void onActivityResult(int requestCode, int resultCode, Intent data){
        if(resultCode == Code.CLOSE_SPLASH){
            finish();
        }
        else if(resultCode == Code.CLOSE_ALL){
            finish();
        }
        else if(resultCode == Code.CLOSE_UNTILL_LOGIN){
            final Intent toMain = new Intent(getApplicationContext(), MainActivity.class);
            startActivityForResult(toMain, Code.CLOSE_SPLASH);
        }
    }

    private void login(){
        if(Hardware.isNetworkAvailable(this)){

            runOnUiThread(new Runnable() {
                public void run() {
                findViewById(R.id.logo_splash).setVisibility(View.GONE);
                findViewById(R.id.progress_splash).setVisibility(View.VISIBLE);
                }
            });

            new Thread(new Runnable() {
                @Override
                public void run() {
                    Looper.prepare();

                    Esito esito = UserService.login(login_class);

                    DataBaseHelper databaseHelper = new DataBaseHelper(getApplicationContext());
                    final SQLiteDatabase db = databaseHelper.getWritableDatabase();

                    if(esito.isFlag()){

                        esito = ComunicationService.registerGCM(new GcmID(Dao.getIdProfile(db),Dao.getIdGmc(db)));

                        if(esito.isFlag()) {

                            final Intent toHome = new Intent(getApplicationContext(), Core.class);

                            Bundle bundle = getIntent().getExtras();

                            Bundle bundleStart = new Bundle();

                            if(bundle!=null) {
                                int sender = bundle.getInt("sender");
                                int type = bundle.getInt("type");

                                bundleStart.putInt("sender",sender);
                                bundleStart.putInt("type",type);

                                toHome.putExtras(bundleStart);
                            }

                            startActivityForResult(toHome, Code.CLOSE_SPLASH);
                        }
                        else{
                            infoToast(esito.getMessage());

                            db.close();
                        }
                    }
                    else{
                        infoToast(esito.getMessage());

                        startService();

                        init_set_DB(db);

                        db.close();

                        final Intent toMain = new Intent(getApplicationContext(), MainActivity.class);
                        startActivityForResult(toMain, Code.CLOSE_SPLASH);
                    }
                }
            }).start();
        }
        else{
            infoToast("No network available!");
        }
    }

    private void infoToast(final String mex){
        runOnUiThread(new Runnable() {
            public void run() {
                Toast.makeText(getApplicationContext(), mex, Toast.LENGTH_LONG).show();
                findViewById(R.id.progress_splash).setVisibility(View.GONE);
                findViewById(R.id.logo_splash).setVisibility(View.VISIBLE);
            }
        });
    }

    private void init_set_DB(SQLiteDatabase db){
        Dao.initDestroyFragment(db);
        Dao.setRestart(db);
    }

    private void startService(){
        Intent serviceIntent = new Intent(this, NatterService.class);
        this.startService(serviceIntent);

        Intent contactIntent = new Intent(this, RunningService.class);
        this.startService(contactIntent);
    }

}
