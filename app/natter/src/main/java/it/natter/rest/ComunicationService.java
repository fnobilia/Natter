package it.natter.rest;

import android.util.Log;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;

import org.apache.http.HttpResponse;
import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.entity.StringEntity;
import org.apache.http.impl.client.DefaultHttpClient;
import org.json.JSONObject;

import java.io.InputStreamReader;
import java.io.Reader;
import java.lang.reflect.Type;
import java.util.ArrayList;
import java.util.List;

import it.natter.classes.Access;
import it.natter.classes.Contact;
import it.natter.classes.ContactService;
import it.natter.classes.Esito;
import it.natter.classes.GcmID;
import it.natter.classes.MessageNatter;
import it.natter.classes.Position;
import it.natter.utility.Code;
import it.natter.utility.Hardware;

/**
 * Created by francesco on 25/03/14.
 */
public class ComunicationService {

    private static String service_add = Code.SERVER+"comu/";

    public static Esito isUser(Contact c){

        ContactService contact = new ContactService();
        contact.setId_natter(c.getId_natter());
        contact.setEmail(c.getEmail());
        contact.setName(c.getName());
        contact.setSurname(c.getSurname());
        contact.setPhone(c.getNumber());

        Esito esito = new Esito();

        try {
            HttpClient httpclient = new DefaultHttpClient();
            HttpPost httpPost = new HttpPost(service_add+"isuser");

            Gson gson = new Gson();
            String json = gson.toJson(contact);

            StringEntity se = new StringEntity(json);

            httpPost.setEntity(se);

            httpPost.setHeader("Accept", "application/json");
            httpPost.setHeader("Content-type", "application/json");

            HttpResponse httpResponse = httpclient.execute(httpPost);

            Reader reader = new InputStreamReader(httpResponse.getEntity().getContent());

            esito = gson.fromJson(reader,Esito.class);

            JSONObject jsonObj = new JSONObject(gson.toJson(esito.getResult()));
            esito.setResult(gson.fromJson(jsonObj.toString(),ContactService.class));

        } catch(Exception e){
            Log.e("IsUser", e.toString()+" - "+e.getLocalizedMessage());
        }

        if(esito.isFlag()) {
            //Log.e("IsUser", esito.toString());
        }

        return esito;
    }

    public static Esito getPosition(Position position){
        Esito esito = new Esito();

        try {
            HttpClient httpclient = new DefaultHttpClient();
            HttpPost httpPost = new HttpPost(service_add+"getposition");

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
            esito.setResult(gson.fromJson(jsonObj.toString(), Position.class));

        } catch (Exception e) {
            Log.e("GetPosition", e.toString()+" - "+e.getLocalizedMessage());
        }

        //Log.e("GetPosition",esito.toString());

        return esito;
    }

    public static Esito registerGCM(GcmID gcm){
        Esito esito = new Esito();

        try {
            HttpClient httpclient = new DefaultHttpClient();
            HttpPost httpPost = new HttpPost(service_add+"registergmc");

            Gson gson = new Gson();
            String json = gson.toJson(gcm);

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
            Log.e("RegisterGCM", e.toString()+" - "+e.getLocalizedMessage());
        }

        //Log.e("RegisterGCM",esito.toString());

        return esito;
    }

    public static Esito removeGCM(GcmID gcm){
        Esito esito = new Esito();

        try {
            HttpClient httpclient = new DefaultHttpClient();
            HttpPost httpPost = new HttpPost(service_add+"removegmc");

            Gson gson = new Gson();
            String json = gson.toJson(gcm);

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
            Log.e("RemoveGCM", e.toString()+" - "+e.getLocalizedMessage());
        }

        //Log.e("RemoveGCM",esito.toString());

        return esito;
    }

    public static Esito sendMessage(MessageNatter message){

        message.setMessage(Hardware.replaceSpecialChar(message.getMessage(),true));

        Esito esito = new Esito();

        try {
            HttpClient httpclient = new DefaultHttpClient();
            HttpPost httpPost = new HttpPost(service_add+"sendMessage");

            Gson gson = new Gson();
            String json = gson.toJson(message);

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
            Log.e("SendMessage", e.toString()+" - "+e.getLocalizedMessage());
        }

        //Log.e("SendMessage",esito.toString());

        return esito;
    }

    public static Esito getMessage(MessageNatter message){
        Esito esito = new Esito();

        boolean flag = true;

        try {
            HttpClient httpclient = new DefaultHttpClient();
            HttpPost httpPost = new HttpPost(service_add+"getMessage");

            Gson gson = new Gson();
            String json = gson.toJson(message);

            StringEntity se = new StringEntity(json);

            httpPost.setEntity(se);

            httpPost.setHeader("Accept", "application/json");
            httpPost.setHeader("Content-type", "application/json");

            HttpResponse httpResponse = httpclient.execute(httpPost);

            Reader reader = new InputStreamReader(httpResponse.getEntity().getContent());

            esito = gson.fromJson(reader,Esito.class);

            if(esito.isFlag()){
                JSONObject jsonObj = new JSONObject(gson.toJson(esito.getResult()));

                Type listType = new TypeToken<List<MessageNatter>>() {}.getType();
                ArrayList<MessageNatter> list = gson.fromJson(jsonObj.getString("Messages"), listType);
                list.remove(list.size()-1);

                esito.setResult(list);
            }
            else {
                JSONObject jsonObj = new JSONObject(gson.toJson(esito.getResult()));
                esito.setResult(jsonObj.getString("$"));

                flag = false;
            }

        } catch (Exception e) {
            Log.e("GetMessage", e.toString()+" - "+e.getLocalizedMessage());
        }

        if(flag){

            ArrayList<MessageNatter> list = ((ArrayList<MessageNatter>) esito.getResult());
            for(MessageNatter mex : list) {
                mex.setMessage(Hardware.replaceSpecialChar(mex.getMessage(), false));
            }

            //Log.e("GetMessage",esito.toString());
        }

        return esito;
    }

    public static Esito saveLastAccess(Access access){
        Esito esito = new Esito();

        try {
            HttpClient httpclient = new DefaultHttpClient();
            HttpPost httpPost = new HttpPost(service_add+"saveLastAccess");

            Gson gson = new Gson();
            String json = gson.toJson(access);

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
            Log.e("SaveLastAccess", e.toString()+" - "+e.getLocalizedMessage());
        }

        //Log.e("SaveLastAccess",esito.toString());

        return esito;
    }

    public static Esito getLastAccess(Access access){
        Esito esito = new Esito();

        try {
            HttpClient httpclient = new DefaultHttpClient();
            HttpPost httpPost = new HttpPost(service_add+"getLastAccess");

            Gson gson = new Gson();
            String json = gson.toJson(access);

            StringEntity se = new StringEntity(json);

            httpPost.setEntity(se);

            httpPost.setHeader("Accept", "application/json");
            httpPost.setHeader("Content-type", "application/json");

            HttpResponse httpResponse = httpclient.execute(httpPost);

            Reader reader = new InputStreamReader(httpResponse.getEntity().getContent());

            esito = gson.fromJson(reader,Esito.class);

            if(esito.isFlag()){
                JSONObject jsonObj = new JSONObject(gson.toJson(esito.getResult()));
                esito.setResult(gson.fromJson(jsonObj.toString(), Access.class));
            }
            else {
                JSONObject jsonObj = new JSONObject(gson.toJson(esito.getResult()));
                esito.setResult(jsonObj.getString("$"));
            }

        } catch (Exception e) {
            Log.e("GetLastAccess", e.toString()+" - "+e.getLocalizedMessage());
        }

        //Log.e("GetLastAccess",esito.toString());

        return esito;
    }

    public static Esito deleteLastAccess(Access access){
        Esito esito = new Esito();

        try {
            HttpClient httpclient = new DefaultHttpClient();
            HttpPost httpPost = new HttpPost(service_add+"deleteLastAccess");

            Gson gson = new Gson();
            String json = gson.toJson(access);

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
            Log.e("DeleteLastAccess", e.toString()+" - "+e.getLocalizedMessage());
        }

        //Log.e("DeleteLastAccess",esito.toString());

        return esito;
    }

    public static Esito sendVoice(MessageNatter message){
        Esito esito = new Esito();

        try {
            HttpClient httpclient = new DefaultHttpClient();
            HttpPost httpPost = new HttpPost(service_add+"sendVoice");

            Gson gson = new Gson();
            String json = gson.toJson(message);

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
            Log.e("SendMessage", e.toString()+" - "+e.getLocalizedMessage());
        }

        //Log.e("SendMessage",esito.toString());

        return esito;
    }

    public static Esito getVoice(MessageNatter message){
        Esito esito = new Esito();

        boolean flag = true;

        try {
            HttpClient httpclient = new DefaultHttpClient();
            HttpPost httpPost = new HttpPost(service_add+"getVoice");

            Gson gson = new Gson();
            String json = gson.toJson(message);

            StringEntity se = new StringEntity(json);

            httpPost.setEntity(se);

            httpPost.setHeader("Accept", "application/json");
            httpPost.setHeader("Content-type", "application/json");

            HttpResponse httpResponse = httpclient.execute(httpPost);

            Reader reader = new InputStreamReader(httpResponse.getEntity().getContent());

            esito = gson.fromJson(reader,Esito.class);

            if(esito.isFlag()){

                JSONObject jsonObj = new JSONObject(gson.toJson(esito.getResult()));
                esito.setResult(gson.fromJson(jsonObj.toString(), MessageNatter.class));

            }
            else {
                JSONObject jsonObj = new JSONObject(gson.toJson(esito.getResult()));
                esito.setResult(jsonObj.getString("$"));

                flag = false;
            }

        } catch (Exception e) {
            Log.e("GetVoice", e.toString()+" - "+e.getLocalizedMessage());
        }

        //if(flag)    Log.e("GetVoice",esito.toString());

        return esito;
    }

}
