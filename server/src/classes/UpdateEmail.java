package classes;

import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlRootElement;

@XmlRootElement(name = "UpdateEmail")
public class UpdateEmail{
	@XmlElement private int id;
	@XmlElement private String email;
	@XmlElement private boolean editable;
	
	public UpdateEmail(){}

	public UpdateEmail(int id, String email, boolean editable) {
		super();
		this.id = id;
		this.email = email;
		this.editable = editable;
	}

	public int getId() {
		return id;
	}

	public String getEmail() {
		return email;
	}

	public boolean isEditable() {
		return editable;
	}

	@Override
	public String toString() {
		return "UpdateEmail [id=" + id + ", email=" + email + ", editable="
				+ editable + "]";
	}

}
