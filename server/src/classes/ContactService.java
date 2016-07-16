package classes;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlRootElement;

@XmlRootElement(name = "ContactService")

@XmlAccessorType(XmlAccessType.FIELD)

public class ContactService{

	@XmlElement(name = "id_natter")
	private String id_natter;

	@XmlElement(name = "email")
	private String email;

	@XmlElement(name = "name")
	private String name;
	
	@XmlElement(name = "surname")
	private String surname;

    @XmlElement(name = "phone")
	private String phone;
    
    @XmlElement(name = "photo")
	private String photo;

    @XmlElement(name = "hasPhoto")
    private boolean hasPhoto;
	
	public ContactService(){}

	public String getId_natter() {
		return id_natter;
	}

	public void setId_natter(String id_natter) {
		this.id_natter = id_natter;
	}

	public String getEmail() {
		return email;
	}

	public void setEmail(String email) {
		this.email = email;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public String getSurname() {
		return surname;
	}

	public void setSurname(String surname) {
		this.surname = surname;
	}

	public String getPhone() {
		return phone;
	}

	public void setPhone(String phone) {
		this.phone = phone;
	}

	public String getPhoto() {
		return photo;
	}

	public void setPhoto(String photo) {
		this.photo = photo;
	}

	public boolean isHasPhoto() {
		return hasPhoto;
	}

	public void setHasPhoto(boolean hasPhoto) {
		this.hasPhoto = hasPhoto;
	}

	@Override
	public String toString() {
		return "ContactService [id_natter=" + id_natter + ", email=" + email
				+ ", name=" + name + ", surname=" + surname + ", phone="
				+ phone + ", photo=" + photo + ", hasPhoto=" + hasPhoto + "]";
	}
	
}

