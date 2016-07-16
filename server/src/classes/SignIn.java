package classes;

import java.util.Arrays;

import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlRootElement;

@XmlRootElement(name = "SignIn")
public class SignIn{
	@XmlElement private int id;
	@XmlElement private String email;
	@XmlElement private String password;
	@XmlElement private String name;
	@XmlElement private String surname;
	@XmlElement private String phone;
	@XmlElement private boolean[] flag;
	@XmlElement private String social;
	@XmlElement private String image;
	
	public SignIn(){}
	
	public SignIn(int id, String email, String password, String name, String surname, String phone, boolean[] flag, String social,String image){
		this.id = id;
		this.email = email;
		this.password = password;
		this.name = name;
		this.surname = surname;
		this.phone = phone;
		this.flag = flag;
		this.image = image;
		this.social = social;
	}

	public int getId(){
		return id;
	}
	
	public String getEmail() {
		return email;
	}

	public String getPassword() {
		return password;
	}

	public String getName() {
		return name;
	}

	public String getSurname() {
		return surname;
	}

	public String getPhone() {
		return phone;
	}

	public boolean[] getFlag() {
		return flag;
	}
	
	public String getImage() {
		return image;
	}
	
	public String getSocial() {
		return social;
	}

	@Override
	public String toString() {
		return "SignIn [id=" + id + ", email=" + email + ", password="
				+ password + ", name=" + name + ", surname=" + surname
				+ ", phone=" + phone + ", flag=" + Arrays.toString(flag)
				+ ", social=" + social + ", image=" + image + "]";
	}
	
}
