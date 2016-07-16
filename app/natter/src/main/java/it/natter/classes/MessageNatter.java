package it.natter.classes;

import com.google.gson.annotations.SerializedName;

/**
 * Created by francesco on 22/04/14.
 */
public class MessageNatter {

    @SerializedName("sender")
    private String sender;

    @SerializedName("receiver")
    private String receiver;

    @SerializedName("message")
    private String message;

    @SerializedName("timestamp")
    private String timestamp;

    public MessageNatter(String sender, String receiver, String message, String timestamp) {
        this.sender = sender;
        this.receiver = receiver;
        this.message = message;
        this.timestamp = timestamp;
    }

    public String getSender() {
        return sender;
    }

    public void setSender(String sender) {
        this.sender = sender;
    }

    public String getReceiver() {
        return receiver;
    }

    public void setReceiver(String receiver) {
        this.receiver = receiver;
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }

    public String getTimestamp() {
        return timestamp;
    }

    public void setTimestamp(String timestamp) {
        this.timestamp = timestamp;
    }

    @Override
    public String toString() {

        String mex = message;
        if(message.length()>1000) mex = "AUDIO";

        return "MessageNatter{" +
                "sender='" + sender + '\'' +
                ", receiver='" + receiver + '\'' +
                ", message='" + mex + '\'' +
                ", timestamp=" + timestamp +
                '}';
    }
}
