package dao;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.Iterator;
import java.util.LinkedList;
import java.util.List;

import classes.Access;
import classes.ContactService;
import classes.GcmID;
import classes.MessageNatter;
import classes.Position;

public class ComunicationDao{
	
	private Connection connection;
	
	public ComunicationDao(){
		try{
			Class.forName("com.mysql.jdbc.Driver");
			this.connection = DriverManager.getConnection("jdbc:mysql://localhost/natter?"+"user=root&password=151");
		}catch(ClassNotFoundException e){
			// TODO Auto-generated catch block
			e.printStackTrace();
		}catch(SQLException e){
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
	
	public void closeConnection(){
		try{
			this.connection.close();
		}catch(SQLException e){
			e.printStackTrace();
		}
	}
	
	public boolean checkContact(ContactService contact) throws SQLException{
		
		Statement statement = this.connection.createStatement();
		
		String query = "SELECT "
				+ "natter.user.id , "
				+ "natter.user.email , "
				+ "natter.user.name , "
				+ "natter.user.surname , "
				+ "natter.user.phone , "
				+ "natter.user.image "
				+ "FROM natter.user "
				+ "WHERE ";
		
		boolean prev = false;
		
		if(!contact.getEmail().equals("")){
				query = query + "natter.user.email LIKE '"+contact.getEmail()+"' ";
				prev = true;
		}
		
		if(!contact.getName().equals("")){
			if(prev){
				query = query +" OR ";
			}
			
			String name = contact.getName();
			if(name.contains("'")){
				name = this.manageApice(name);
			}
			
			query = query + "natter.user.name LIKE '"+name+"' ";
			prev = true;
		}
		
		if(!contact.getSurname().equals("")){
			if(prev){
				query = query +" OR ";
			}
			
			String surname = contact.getSurname();
			if(surname.contains("'")){
				surname = this.manageApice(surname);
			}
			
			query = query + "natter.user.surname LIKE '"+surname+"' ";
			prev = true;
		}
		
		if(!contact.getPhone().equals("")){
			if(prev){
				query = query +" OR ";
			}
			query = query + "natter.user.phone LIKE '"+contact.getPhone()+"' ";
			prev = true;
		}
		
		ResultSet resultSet = statement.executeQuery(query);
				
		if(resultSet.next()){
			contact.setId_natter(resultSet.getString(1));
			contact.setEmail(resultSet.getString(2));
			contact.setName(resultSet.getString(3));
			contact.setSurname(resultSet.getString(4));
			contact.setPhone(resultSet.getString(5));
			contact.setPhoto(resultSet.getString(6));
			
			if(contact.getPhoto().equals("Not stated")){
				contact.setHasPhoto(false);
			}
			else{
				contact.setHasPhoto(true);
			}
			
			resultSet.close();
			
			return true;
		}
				
		resultSet.close();
	
		return false;
	}
	
	public boolean getPosition(Position position) throws SQLException{
		Statement statement = this.connection.createStatement();
		ResultSet resultSet = statement.executeQuery("SELECT COUNT(natter.position.id_user) FROM natter.position WHERE natter.position.id_user = '"+position.getId_natter()+"'");
		if(resultSet.next()){
			if(resultSet.getInt(1)!=0){
				resultSet = statement.executeQuery("SELECT natter.position.latitude,natter.position.longitude FROM natter.position WHERE natter.position.id_user = '"+position.getId_natter()+"'");
				
				if(resultSet.next()){
					position.setLatitude(resultSet.getString(1));
					position.setLongitude(resultSet.getString(2));
					
					resultSet.close();
					
					return true;
				}
				
			}
		}
		
		resultSet.close();
		
		return false;
	}
	
	public boolean registerGCM(GcmID gcm) throws SQLException{
		Statement statement = this.connection.createStatement();
		ResultSet resultSet = statement.executeQuery("SELECT natter.device.id FROM natter.device WHERE natter.device.id = '"+gcm.getId_natter()+"' AND natter.device.id_gcm = '"+gcm.getId_gcm()+"'");
		if(resultSet.next()){
			
			resultSet.close();
			
			return true;
		}
		else{
			resultSet = statement.executeQuery("SELECT natter.user.id FROM natter.user WHERE natter.user.id = '"+gcm.getId_natter()+"'");
			
			resultSet.close();
			
			int result = statement.executeUpdate("INSERT INTO natter.device (natter.device.id_gcm,natter.device.id) VALUES ('"+gcm.getId_gcm()+"','"+gcm.getId_natter()+"')");
			
			if(result==1){
				
				resultSet.close();
				
				return true;
			}
		}
		
		resultSet.close();
		
		return false;
	}
	
	public boolean removeGCM(GcmID gcm) throws SQLException{
		Statement statement = this.connection.createStatement();
		ResultSet resultSet = statement.executeQuery("SELECT natter.user.id FROM natter.user WHERE natter.user.id = '"+gcm.getId_natter()+"'");
		if(resultSet.next()){
			
			resultSet.close();
			
			int result = statement.executeUpdate("DELETE FROM natter.device WHERE natter.device.id = '"+gcm.getId_natter()+"' AND natter.device.id_gcm = '"+gcm.getId_gcm()+"' LIMIT 1");
			
			if(result==1) return true;
		}
		
		return false;
	}
	
	public List<String> getGcm_Id(String id_natter) throws SQLException{
		List<String> devices = new LinkedList<String>();
		
		Statement statement = this.connection.createStatement();
		ResultSet resultSet = statement.executeQuery("SELECT natter.device.id_gcm FROM natter.device WHERE natter.device.id = '"+id_natter+"'");
		
		while(resultSet.next()){
			devices.add(resultSet.getString(1));
		}
		
		resultSet.close();
		
		return devices;
	}
	
	public String saveMessage(MessageNatter message) throws SQLException{
		Statement statement = this.connection.createStatement();
		ResultSet resultSet = statement.executeQuery("SELECT natter.user.id FROM natter.user WHERE natter.user.id = '"+message.getSender()+"'");
		if(resultSet.next()){
			resultSet = statement.executeQuery("SELECT natter.user.id FROM natter.user WHERE natter.user.id = '"+message.getReceiver()+"'");
			if(resultSet.next()){
				resultSet.close();
				
				int result = statement.executeUpdate("INSERT INTO natter.message (natter.message.sender,natter.message.receiver,natter.message.message) VALUES "
						+ "('"+message.getSender()+"','"+message.getReceiver()+"','"+message.getMessage()+"')");
				
				if(result==1){
					return "Message saved!";
				}
			}
			else{
				return "Receiver not exist!";
			}
		}
		else{
			return "Sender not exist!";
		}
		
		return "Error!";
	}
	
	public ArrayList<MessageNatter> getMessage(MessageNatter message) throws SQLException{
		
		ArrayList<MessageNatter> list = null;
		
		Statement statement = this.connection.createStatement();
		ResultSet resultSet = statement.executeQuery("SELECT natter.user.id FROM natter.user WHERE natter.user.id = '"+message.getReceiver()+"'");
		if(resultSet.next()){
			
			resultSet = statement.executeQuery("SELECT * FROM natter.message WHERE natter.message.receiver = '"+message.getReceiver()+"' ORDER BY natter.message.time ASC");
			
			list = new ArrayList<MessageNatter>();
			
			while(resultSet.next()){
				list.add(new MessageNatter(resultSet.getString(1),resultSet.getString(2),resultSet.getString(3),resultSet.getString(4)));
			}

		}
		
		resultSet.close();
		
		statement = this.connection.createStatement();
		
		if(list!=null){
			Iterator<MessageNatter> i = list.iterator();
			MessageNatter mex;
			while(i.hasNext()){
			
				mex = i.next();
			
				int result = statement.executeUpdate("DELETE FROM natter.message WHERE "
						+ "natter.message.sender = '"+mex.getSender()+"' AND "
						+ "natter.message.receiver = '"+mex.getReceiver()+"' AND "
						+ "natter.message.time = '"+mex.getTimestamp()+"' LIMIT 1");
				
			}
		}
		
		return list;
	}
	
	public boolean saveLastAccess(Access access) throws SQLException{
		SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
		Date date = new Date();
		
		String value = dateFormat.format(date);
		
		if(!access.getTimestamp().equals("")){
			value = access.getTimestamp(); 
		}
		
		Statement statement = this.connection.createStatement();
		ResultSet resultSet = statement.executeQuery("SELECT natter.user.id FROM natter.user WHERE natter.user.id = '"+access.getId_natter()+"'");
		if(resultSet.next()){
			
			resultSet.close();
			
			resultSet = statement.executeQuery("SELECT natter.access.timestamp FROM natter.access WHERE natter.access.id_natter = '"+access.getId_natter()+"'");
			
			if(resultSet.next()){
				
				int result = statement.executeUpdate("UPDATE natter.access SET natter.access.timestamp = '"+value+"' WHERE natter.access.id_natter = '"+access.getId_natter()+"'");
				
				if(result==1) return true;
				
			}
			else{
			
				int result = statement.executeUpdate("INSERT INTO natter.access (natter.access.id_natter,natter.access.timestamp) VALUES ('"+access.getId_natter()+"','"+value+"')");
			
				if(result==1) return true;
			
			}
			
			resultSet.close();
		}
		
		return false;
		
	}
	
	public boolean getLastAccess(Access access) throws SQLException{
		Statement statement = this.connection.createStatement();
		ResultSet resultSet = statement.executeQuery("SELECT natter.access.timestamp FROM natter.access WHERE natter.access.id_natter = '"+access.getId_natter()+"'");
		
		if(resultSet.next()){
			
			access.setTimestamp(resultSet.getString(1));
			
			resultSet.close();
			
			return true;
		}
		
		resultSet.close();
		
		return false;
		
	}
	
	public boolean deleteLastAccess(Access access) throws SQLException{
		Statement statement = this.connection.createStatement();
		ResultSet resultSet = statement.executeQuery("SELECT natter.access.timestamp FROM natter.access WHERE natter.access.id_natter = '"+access.getId_natter()+"'");
		
		if(resultSet.next()){
			
			resultSet.close();
			
			int result = statement.executeUpdate("DELETE FROM natter.access WHERE natter.access.id_natter = '"+access.getId_natter()+"' LIMIT 1");
			
			if(result==1) return true;

		}
		
		resultSet.close();
		
		return false;
		
	}
	
	public String saveVoice(MessageNatter message) throws SQLException{
		Statement statement = this.connection.createStatement();
		ResultSet resultSet = statement.executeQuery("SELECT natter.user.id FROM natter.user WHERE natter.user.id = '"+message.getSender()+"'");
		if(resultSet.next()){
			resultSet = statement.executeQuery("SELECT natter.user.id FROM natter.user WHERE natter.user.id = '"+message.getReceiver()+"'");
			if(resultSet.next()){
				resultSet.close();
				
				SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss.SSS");
				Date date = new Date();
				
				
				int result = statement.executeUpdate("INSERT INTO natter.voice (natter.voice.sender,natter.voice.receiver,natter.voice.message,natter.voice.time) VALUES "
						+ "('"+message.getSender()+"','"+message.getReceiver()+"','"+message.getMessage()+"','"+dateFormat.format(date)+"')");
				
				if(result==1){
					return "Message saved!";
				}
			}
			else{
				return "Receiver not exist!";
			}
		}
		else{
			return "Sender not exist!";
		}
		
		return "Error!";
	}
	
	
	public int getVoice(MessageNatter message) throws SQLException{
		
		int result = -1;
		
		Statement statement = this.connection.createStatement();
		ResultSet resultSet = statement.executeQuery("SELECT natter.user.id FROM natter.user WHERE natter.user.id = '"+message.getReceiver()+"'");
		if(resultSet.next()){
			
			resultSet = statement.executeQuery("SELECT natter.voice.message,natter.voice.time FROM natter.voice WHERE natter.voice.receiver = '"+message.getReceiver()+"' AND natter.voice.sender = '"+message.getSender()+"' ORDER BY natter.voice.time ASC LIMIT 1");
			
			if(resultSet.next()){
				
				String data = resultSet.getString(1);
				message.setMessage(data);
				message.setTimestamp(resultSet.getString(2));
				
				result = 1;
			}
			else{
				result = 0;
			}

		}
		
		resultSet.close();
		
		statement = this.connection.createStatement();
		
		if(result != -1){
		
			int outcome = statement.executeUpdate("DELETE FROM natter.voice WHERE "
						+ "natter.voice.sender = '"+message.getSender()+"' AND "
						+ "natter.voice.receiver = '"+message.getReceiver()+"' AND "
						+ "natter.voice.time = '"+message.getTimestamp()+"' LIMIT 1");
		}
		
		return result;
	}
	
	
	private String manageApice(String value){
		String[] parts = value.split("'");
		
		value = "";
		
		for(int i=0;i<parts.length;i++){
			value = value + parts[i] + "''''";
		}
		
		value = value.substring(0,(value.length()-4));
		
		return value;
	}
	
}
