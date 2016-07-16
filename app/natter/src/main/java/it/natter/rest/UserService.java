package it.natter.rest;

import android.util.Log;

import com.google.gson.Gson;

import org.apache.http.HttpResponse;
import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.entity.StringEntity;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.params.BasicHttpParams;
import org.apache.http.params.HttpParams;
import org.json.JSONObject;

import java.io.InputStreamReader;
import java.io.Reader;

import it.natter.classes.Esito;
import it.natter.classes.ImageService;
import it.natter.classes.Login;
import it.natter.classes.Position;
import it.natter.classes.SignIn;
import it.natter.classes.UpdateEmail;
import it.natter.classes.UpdatePhone;
import it.natter.utility.Code;

/**
 * Created by francesco on 25/03/14.
 */
public class UserService{

    private static String service_add = Code.SERVER+"user/";

    public static Esito login(Login login){
        Esito esito = new Esito();

        try {
            final HttpParams httpParams = new BasicHttpParams();
            //HttpConnectionParams.setConnectionTimeout(httpParams,3000);
            HttpClient httpclient = new DefaultHttpClient(httpParams);
            HttpPost httpPost = new HttpPost(service_add+"login");

            Gson gson = new Gson();
            String json = gson.toJson(login);

            StringEntity se = new StringEntity(json);

            httpPost.setEntity(se);

            httpPost.setHeader("Accept", "application/json");
            httpPost.setHeader("Content-type", "application/json");

            HttpResponse httpResponse = httpclient.execute(httpPost);

            Reader reader = new InputStreamReader(httpResponse.getEntity().getContent());

            esito = gson.fromJson(reader,Esito.class);

            if(esito.isFlag()){
                JSONObject jsonObj = new JSONObject(gson.toJson(esito.getResult()));
                esito.setResult(jsonObj.getString("$"));
            }

        } catch (Exception e) {
            Log.e("Login", e.toString()+" - "+e.getLocalizedMessage());
        }

        //Log.e("Login",esito.toString());

        return esito;
    }

    public static Esito signIn(SignIn signIn){
        Esito esito = new Esito();

        try {
            HttpClient httpclient = new DefaultHttpClient();
            HttpPost httpPost = new HttpPost(service_add+"signin");

            Gson gson = new Gson();
            String json = gson.toJson(signIn);

            StringEntity se = new StringEntity(json);

            httpPost.setEntity(se);

            httpPost.setHeader("Accept", "application/json");
            httpPost.setHeader("Content-type", "application/json");

            HttpResponse httpResponse = httpclient.execute(httpPost);

            Reader reader = new InputStreamReader(httpResponse.getEntity().getContent());

            esito = gson.fromJson(reader,Esito.class);

            JSONObject jsonObj = new JSONObject(gson.toJson(esito.getResult()));
            if(!esito.isFlag()) {
                esito.setResult(jsonObj.getString("$"));
            }
            else{
                esito.setResult(new Integer(jsonObj.getInt("$")));
            }

        } catch(Exception e){
            Log.e("SignIn", e.toString()+" - "+e.getLocalizedMessage());
        }

        //Log.e("signIn",esito.toString());

        return esito;
    }

    public static Esito updateEmail(UpdateEmail updateEmail){
        Esito esito = new Esito();

        try {
            HttpClient httpclient = new DefaultHttpClient();
            HttpPost httpPost = new HttpPost(service_add+"updateEmail");

            Gson gson = new Gson();
            String json = gson.toJson(updateEmail);

            StringEntity se = new StringEntity(json);

            httpPost.setEntity(se);

            httpPost.setHeader("Accept", "application/json");
            httpPost.setHeader("Content-type", "application/json");

            HttpResponse httpResponse = httpclient.execute(httpPost);

            Reader reader = new InputStreamReader(httpResponse.getEntity().getContent());

            esito = gson.fromJson(reader,Esito.class);

            JSONObject jsonObj = new JSONObject(gson.toJson(esito.getResult()));
            esito.setResult(jsonObj.getString("$"));
        }catch(Exception e){
            Log.e("UpdateEmail", e.toString()+" - "+e.getLocalizedMessage());
        }

        //Log.e("UpdateEmail",esito.toString());

        return esito;
    }

    public static Esito updatePhone(UpdatePhone updatePhone){
        Esito esito = new Esito();

        try {
            HttpClient httpclient = new DefaultHttpClient();
            HttpPost httpPost = new HttpPost(service_add+"updatePhone");

            Gson gson = new Gson();
            String json = gson.toJson(updatePhone);

            StringEntity se = new StringEntity(json);

            httpPost.setEntity(se);

            httpPost.setHeader("Accept", "application/json");
            httpPost.setHeader("Content-type", "application/json");

            HttpResponse httpResponse = httpclient.execute(httpPost);

            Reader reader = new InputStreamReader(httpResponse.getEntity().getContent());

            esito = gson.fromJson(reader,Esito.class);

            JSONObject jsonObj = new JSONObject(gson.toJson(esito.getResult()));
            esito.setResult(jsonObj.getString("$"));
        }catch(Exception e){
            Log.e("UpdatePhone", e.toString()+" - "+e.getLocalizedMessage());
        }

        //Log.e("UpdatePhone",esito.toString());

        return esito;
    }

    public static Esito updateProfileImage(ImageService image){
        Esito esito = new Esito();

        try {
            HttpClient httpclient = new DefaultHttpClient();
            HttpPost httpPost = new HttpPost(service_add+"updateImageProfile");

            Gson gson = new Gson();
            String json = gson.toJson(image);

            StringEntity se = new StringEntity(json);

            httpPost.setEntity(se);

            httpPost.setHeader("Accept", "application/json");
            httpPost.setHeader("Content-type", "application/json");

            HttpResponse httpResponse = httpclient.execute(httpPost);

            Reader reader = new InputStreamReader(httpResponse.getEntity().getContent());

            esito = gson.fromJson(reader,Esito.class);

            JSONObject jsonObj = new JSONObject(gson.toJson(esito.getResult()));
            esito.setResult(jsonObj.getString("$"));

        }catch(Exception e){
            Log.e("UpdateProfileImage", e.toString()+" - "+e.getLocalizedMessage());
        }

        //Log.e("UpdateProfileImage",esito.toString());

        return esito;
    }

    public static Esito userInfo(SignIn signIn){
        Esito esito = new Esito();

        try {
            HttpClient httpclient = new DefaultHttpClient();
            HttpPost httpPost = new HttpPost(service_add+"userInfo");

            Gson gson = new Gson();
            String json = gson.toJson(signIn);

            StringEntity se = new StringEntity(json);

            httpPost.setEntity(se);

            httpPost.setHeader("Accept", "application/json");
            httpPost.setHeader("Content-type", "application/json");

            HttpResponse httpResponse = httpclient.execute(httpPost);

            Reader reader = new InputStreamReader(httpResponse.getEntity().getContent());

            esito = gson.fromJson(reader,Esito.class);

            JSONObject jsonObj = new JSONObject(gson.toJson(esito.getResult()));
            esito.setResult(gson.fromJson(jsonObj.toString(),SignIn.class));

        } catch (Exception e) {
            Log.e("UserInfo", e.toString()+" - "+e.getLocalizedMessage());
        }

        //Log.e("UserInfo",esito.toString());

        return esito;
    }

    public static Esito updatePosition(Position position){

        //Log.e("UpdatePosition",position.toString());

        Esito esito = new Esito();

        try {
            HttpClient httpclient = new DefaultHttpClient();
            HttpPost httpPost = new HttpPost(service_add+"sendposition");

            Gson gson = new Gson();
            String json = gson.toJson(position);

            StringEntity se = new StringEntity(json);

            httpPost.setEntity(se);

            httpPost.setHeader("Accept", "application/json");
            httpPost.setHeader("Content-type", "application/json");

            HttpResponse httpResponse = httpclient.execute(httpPost);

            Reader reader = new InputStreamReader(httpResponse.getEntity().getContent());

            esito = gson.fromJson(reader,Esito.class);

            JSONObject jsonObj = new JSONObject(gson.toJson(esito.getResult()));
            esito.setResult(jsonObj.getString("$"));

        } catch (Exception e) {
            Log.e("UpdatePosition", e.toString()+" - "+e.getLocalizedMessage());
        }

        //Log.e("UpdatePosition",esito.toString());

        return esito;
    }

}
