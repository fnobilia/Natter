package it.natter;

import android.app.Activity;
import android.content.Intent;
import android.database.sqlite.SQLiteDatabase;
import android.graphics.Color;
import android.os.Bundle;
import android.os.Handler;
import android.os.Looper;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import it.natter.classes.Esito;
import it.natter.classes.GcmID;
import it.natter.classes.Login;
import it.natter.classes.SignIn;
import it.natter.dao.Dao;
import it.natter.dao.DataBaseHelper;
import it.natter.rest.ComunicationService;
import it.natter.rest.UserService;
import it.natter.utility.Code;
import it.natter.utility.Cryptography;
import it.natter.utility.CustomProgress;
import it.natter.utility.Hardware;

public class MainActivity extends Activity implements TextWatcher{

    private EditText email;
    private EditText password;
    private boolean read = false;
    private boolean doubleBackToExitPressedOnce = false;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        this.email = (EditText)findViewById(R.id.email);
        this.password = (EditText)findViewById(R.id.password);

        this.email.addTextChangedListener(this);
        this.password.addTextChangedListener(this);
    }

    protected void onResume(){
        super.onResume();

        NatterApplication.activityResumed();

        if(this.read){
            this.email.setText("");
            this.password.setText("");

            this.read = false;
        }
    }

    @Override
    public void onPause() {
        super.onPause();
        NatterApplication.activityPaused();
    }

    //Gestione firma
    public void signUp(View view){
        if(Hardware.isNetworkAvailable(this)) {
            final Intent toSignUp = new Intent(this, SignUp.class);
            startActivityForResult(toSignUp,Code.CLOSE_ALL);
        }
        else{
            Toast.makeText(getApplicationContext(), "No network available!", Toast.LENGTH_SHORT).show();
        }
    }

    //Gestione login
    public void login(View view){
        this.login();
    }

    protected void onActivityResult(int requestCode, int resultCode, Intent data){
        if(resultCode == Code.CLOSE_ALL){
            setResult(Code.CLOSE_SPLASH);
            finish();
        }
    }

    private void login(){
        if(Hardware.isNetworkAvailable(this)){

            final CustomProgress dialog = new CustomProgress("We are checking your data");
            dialog.show(getFragmentManager(), "");

            new Thread(new Runnable() {
                @Override
                public void run() {
                    Looper.prepare();

                    Login login_class= new Login(email.getText().toString().trim(), Cryptography.md5(password.getText().toString().trim()));
                    read = true;

                    Esito esito = UserService.login(login_class);

                    if(esito.isFlag()){

                        DataBaseHelper databaseHelper = new DataBaseHelper(getApplicationContext());
                        final SQLiteDatabase db = databaseHelper.getWritableDatabase();

                        esito = UserService.userInfo(new SignIn(esito.getResult().toString()));

                        SignIn signIn = new SignIn();

                        if(esito.isFlag()) {
                            Dao.initProfile(db);

                            signIn = (SignIn) esito.getResult();

                            String[] data = {signIn.getId(), signIn.getPassword(), signIn.getEmail(), signIn.getName(), signIn.getSurname(), signIn.getPhone()};
                            Dao.insertProfile(db, data, signIn.getFlag());

                            esito = ComunicationService.registerGCM(new GcmID(Dao.getIdProfile(db), Dao.getIdGmc(db)));
                        }
                        else{
                            dialog.dismiss();

                            runOnUiThread(new Runnable() {
                                public void run() {
                                    Toast.makeText(getApplicationContext(), "Service temporarily unavailable!", Toast.LENGTH_LONG).show();
                                }
                            });
                        }

                        final String image = signIn.getImage();

                        db.close();

                        new Thread(new Runnable(){
                            @Override
                            public void run(){
                                Looper.prepare();
                                Hardware.saveProfileImage(Hardware.fromStringToImage(image));
                            }
                        }).start();

                        if(esito.isFlag()) {
                            dialog.dismiss();

                            final Intent toHome = new Intent(getApplicationContext(), Core.class);
                            startActivityForResult(toHome, Code.CLOSE_ALL);
                        }
                        else{
                            dialog.dismiss();

                            runOnUiThread(new Runnable() {
                                public void run() {
                                    Toast.makeText(getApplicationContext(), "Service temporarily unavailable!", Toast.LENGTH_LONG).show();
                                }
                            });
                        }
                    }
                    else{
                        dialog.dismiss();

                        final String mex = esito.getMessage();
                        runOnUiThread(new Runnable() {
                            public void run() {
                                Toast.makeText(getApplicationContext(),mex, Toast.LENGTH_LONG).show();
                            }
                        });
                    }
                }
            }).start();
        }
        else{
            Toast.makeText(getApplicationContext(), "No network available!", Toast.LENGTH_SHORT).show();
        }
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

        if(this.email.getText().toString().equals("")) {
            enable = false;
        }

        if((this.password.getText().toString().equals(""))||(this.password.getText().toString().length()<6)){
            enable = false;
        }

        Button login_button = (Button) findViewById(R.id.login);
        if(enable){
            login_button.setTextColor(Color.parseColor("#FFFFFF"));
        }
        else{
            login_button.setTextColor(Color.parseColor("#40FFFFFF"));
        }

        login_button.setEnabled(enable);
        login_button.setClickable(enable);
    }

    @Override
    public void onBackPressed(){
        if(this.doubleBackToExitPressedOnce){
            setResult(Code.CLOSE_SPLASH);
            finish();
        }

        this.doubleBackToExitPressedOnce = true;
        Toast.makeText(this, "Please click BACK again to exit",Toast.LENGTH_SHORT).show();

        new Handler().postDelayed(new Runnable(){
            @Override
            public void run(){doubleBackToExitPressedOnce = false;
            }
        }, 2000);
    }
}
