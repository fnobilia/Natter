package classes;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlRootElement;

@XmlRootElement(name = "Position")

@XmlAccessorType(XmlAccessType.FIELD)

public class Position{
	
	@XmlElement(name = "id_natter")
	private String id_natter;
	
	@XmlElement(name = "latitude")
	private String latitude;

	@XmlElement(name = "longitude")
	private String longitude;
	
	public Position(){}

	public String getId_natter() {
		return id_natter;
	}

	public String getLatitude() {
		return latitude;
	}

	public void setLatitude(String latitude) {
		this.latitude = latitude;
	}
	
	public void setId_natter(String id_natter) {
		this.id_natter = id_natter;
	}

	public String getLongitude() {
		return longitude;
	}

	public void setLongitude(String longitude) {
		this.longitude = longitude;
	}

	@Override
	public String toString() {
		return "Position [id_natter=" + id_natter + ", latitude=" + latitude
				+ ", longitude=" + longitude + "]";
	}

	
}

