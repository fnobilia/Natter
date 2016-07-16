package classes;

import java.util.ArrayList;
import java.util.Iterator;

import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlRootElement;

@XmlRootElement(name="List")
public class MessageList<T>{
	
    protected ArrayList<T> list;

    public MessageList(){}

    public MessageList(ArrayList<T> list){
    	this.list=list;
    }

    @XmlElement(name="Messages")
    public ArrayList<T> getList(){
    	return list;
    }
    
    public String toString(){
    	String temp = "";
    	
    	Iterator<T> i = this.list.iterator();
    	while(i.hasNext())	temp = temp + i.next();
    	
    	return temp;
    }
}