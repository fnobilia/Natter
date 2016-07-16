package it.natter.classes;

import com.google.gson.annotations.SerializedName;

/**
 * Created by francesco on 25/03/14.
 */

public class Esito {

    @SerializedName("flag")
    private boolean flag;

    @SerializedName("message")
    private String message;

    @SerializedName("result")
    private Object result;

    public Esito(){}

    public boolean isFlag() {
        return flag;
    }

    public void setFlag(boolean flag){
        this.flag = flag;
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }

    public Object getResult() {
        return result;
    }

    public void setResult(Object result) {
        this.result = result;
    }

    @Override
    public String toString() {
        return "Esito{" +
                "flag=" + flag +
                ", message='" + message + '\'' +
                ", result=" + result +
                '}';
    }
}