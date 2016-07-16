package classes;

import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlRootElement;
import javax.xml.bind.annotation.XmlSeeAlso;

@XmlRootElement(name = "Esito")

@XmlSeeAlso({SignIn.class,ContactService.class,Position.class,MessageNatter.class,MessageList.class,Access.class})

public class Esito{
	@XmlElement private boolean flag;
	@XmlElement private String message;
	@XmlElement private Object result;
	
	public Esito(){}

	public Esito(boolean flag, String message, Object result){
		this.flag = flag;
		this.message = message;
		this.result = result;
	}
	
	public Esito(String message){
		this.flag = false;
		this.message = message;
		this.result = new String("Error");
	}

	public void setFlag(boolean flag) {
		this.flag = flag;
	}

	public void setMessage(String message) {
		this.message = message;
	}

	public void setResult(Object result) {
		this.result = result;
	}

	@Override
	public String toString() {
		return "Esito [flag=" + flag + ", message=" + message + ", result="
				+ result + "]";
	}
	
}

