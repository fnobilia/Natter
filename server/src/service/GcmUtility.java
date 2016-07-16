package service;

import java.io.IOException;
import java.util.List;

import com.google.android.gcm.server.Message;
import com.google.android.gcm.server.MulticastResult;
import com.google.android.gcm.server.Result;
import com.google.android.gcm.server.Sender;

public class GcmUtility {
	
	public static void sendNotific(String writer,String type,List<String> devicesList) throws IOException{
		Sender sender = new	Sender("AIzaSyDqo8LpXR0RfaCfDcJWhyihr9UxufZI1GE");
        
        Message message = new Message.Builder()
        .collapseKey("message")
        .timeToLive(3) 
        .delayWhileIdle(true)
        .addData("message", writer+"#"+type)
        .build();  
       
        if(devicesList.size()==1){	
        	Result result = sender.send(message,devicesList.get(0),1); 	
        }
        else{    
        	MulticastResult multicastResult = sender.send(message, devicesList, 0);
        	sender.send(message, devicesList, 0);
        }
        
	}
	
}
