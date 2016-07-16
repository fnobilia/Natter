package classes;

import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlRootElement;

@XmlRootElement(name = "ImageService")
public class ImageService{
	@XmlElement private String id;
	@XmlElement private String image;
	
	public ImageService(){}
	
	public ImageService(String id,String image){
		this.id = id;
		this.image = image;
	}

	public String getImage() {
		return image;
	}
	
	public String getId() {
		return id;
	}

	@Override
	public String toString() {
		return "ImageService [id=" + id + ", image=" + image + "]";
	}
}
