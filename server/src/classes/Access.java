package classes;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlRootElement;

@XmlRootElement(name = "Access")

@XmlAccessorType(XmlAccessType.FIELD)

public class Access{

	@XmlElement(name = "id_natter")
	private String id_natter;
	
	@XmlElement(name = "timestamp")
	private String timestamp;
	
	public Access(){}

	public Access(String id_natter, String timestamp) {
		super();
		this.id_natter = id_natter;
		this.timestamp = timestamp;
	}

	public String getId_natter() {
		return id_natter;
	}

	public void setId_natter(String id_natter) {
		this.id_natter = id_natter;
	}

	public String getTimestamp() {
		return timestamp;
	}

	public void setTimestamp(String timestamp) {
		this.timestamp = timestamp;
	}

	@Override
	public String toString() {
		return "Access [id_natter=" + id_natter + ", timestamp=" + timestamp + "]";
	}
	
}
