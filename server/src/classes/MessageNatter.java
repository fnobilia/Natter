package classes;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlRootElement;

@XmlRootElement(name = "MessageNatter")

@XmlAccessorType(XmlAccessType.FIELD)

public class MessageNatter{

	@XmlElement(name = "sender")
	private String sender;
	
	@XmlElement(name = "receiver")
	private String receiver;

	@XmlElement(name = "message")
	private String message;
	
	@XmlElement(name = "timestamp")
	private String timestamp;
	
	public MessageNatter(){}

	public MessageNatter(String sender, String receiver, String message,String timestamp) {
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
		return "MessageNatter [sender=" + sender + ", receiver=" + receiver
				+ ", message=" + message + ", timestamp=" + timestamp + "]";
	}
	
}

