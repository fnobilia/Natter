package classes;

import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlRootElement;

@XmlRootElement(name = "UpdatePhone")
public class UpdatePhone{
	@XmlElement private int id;
	@XmlElement private String phone;
	@XmlElement private boolean editable;
	
	public UpdatePhone(){}

	public UpdatePhone(int id, String phone, boolean editable) {
		super();
		this.id = id;
		this.phone = phone;
		this.editable = editable;
	}

	public int getId() {
		return id;
	}

	public String getPhone() {
		return phone;
	}

	public boolean isEditable() {
		return editable;
	}

	@Override
	public String toString() {
		return "UpdatePhone [id=" + id + ", phone=" + phone + ", editable="
				+ editable + "]";
	}

}
