package classes;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlRootElement;

@XmlRootElement(name = "GmcID")

@XmlAccessorType(XmlAccessType.FIELD)

public class GcmID{

	@XmlElement(name = "id_natter")
	private String id_natter;

	@XmlElement(name = "id_gcm")
	private String id_gcm;
	
	public GcmID(){}

	public String getId_natter() {
		return id_natter;
	}

	public void setId_natter(String id_natter) {
		this.id_natter = id_natter;
	}

	public String getId_gcm() {
		return id_gcm;
	}

	public void setId_gcm(String id_gcm) {
		this.id_gcm = id_gcm;
	}

	@Override
	public String toString() {
		return "GcmID [id_natter=" + id_natter + ", id_gcm=" + id_gcm + "]";
	}

}

