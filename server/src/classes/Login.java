package classes;

import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlRootElement;

@XmlRootElement(name = "Login")
public class Login{
	@XmlElement private String email;
	@XmlElement private String password;
	
	public Login(){}
	
	public Login(String email,String password){
		this.email = email;
		this.password = password;
	}

	public String getEmail() {
		return email;
	}

	public String getPassword() {
		return password;
	}

	@Override
	public String toString() {
		return "Login [email=" + email + ", password=" + password + "]";
	}
}
