package it.natter.utility;

import android.util.Log;

import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;

/**
 * Created by francesco on 31/03/14.
 */
public class Cryptography{

    public static String md5(String input){

        String output = "";

        try{
            MessageDigest md = MessageDigest.getInstance("MD5");
            md.update(input.getBytes());
            byte[] digest = md.digest();

            StringBuffer sb = new StringBuffer();

            for (byte b : digest) {
                sb.append(String.format("%02x", b & 0xff));
            }

            output = sb.toString();
        }
        catch(NoSuchAlgorithmException e){
            Log.e("MD5",e.getMessage());
        }

        return output;
    }
}
