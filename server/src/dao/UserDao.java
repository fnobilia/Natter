package dao;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;

import classes.ImageService;
import classes.Login;
import classes.Position;
import classes.SignIn;
import classes.UpdateEmail;
import classes.UpdatePhone;

public class UserDao{
	
	private Connection connection;
	
	public UserDao(){
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
	
	public int login(Login login) throws SQLException{
		Statement statement = this.connection.createStatement();
		ResultSet resultSet = statement.executeQuery("SELECT natter.user.id FROM natter.user "
					+ "WHERE natter.user.email = '"+login.getEmail()+"' AND natter.user.password = '"+login.getPassword()+"'");
			
		if(resultSet.next()){
			int id = resultSet.getInt(1); 
			resultSet.close();
			return id;		
		}
	
		return -1;
	}
	
	public int signIn(SignIn signIn) throws SQLException{
		Statement statement = this.connection.createStatement();
		ResultSet resultSet = statement.executeQuery("SELECT COUNT(natter.user.id) FROM natter.user WHERE natter.user.email = '"+signIn.getEmail()+"'");
		if(resultSet.next()){
			if(resultSet.getInt(1)==0){
					
				resultSet.close();
					
				int result = statement.executeUpdate("INSERT INTO natter.user("
							+ "natter.user.email,natter.user.email_edit,"
							+ "natter.user.password,"
							+ "natter.user.name,natter.user.name_edit,"
							+ "natter.user.surname,natter.user.surname_edit,"
							+ "natter.user.phone,natter.user.phone_edit,"
							+ "natter.user.image,"
							+ "natter.user.social"
							+ ") VALUES ("
							+"'"+signIn.getEmail()+"','"+fromBooleanToInt(signIn.getFlag()[0])+"',"
							+"'"+signIn.getPassword()+"',"
							+"'"+signIn.getName()+"','"+fromBooleanToInt(signIn.getFlag()[1])+"',"
							+"'"+signIn.getSurname()+"','"+fromBooleanToInt(signIn.getFlag()[2])+"',"
							+"'"+signIn.getPhone()+"','"+fromBooleanToInt(signIn.getFlag()[3])+"',"
							+"'"+signIn.getImage()+"',"
							+"'"+signIn.getSocial()+"'"
							+")");
					
				if(result==1){
					resultSet = statement.executeQuery("SELECT natter.user.id FROM natter.user WHERE natter.user.email = '"+signIn.getEmail()+"'");
						
					if(resultSet.next()){
						int id = resultSet.getInt(1); 
						resultSet.close();
						return id;		
					}
				}
			}
			else{
				resultSet.close();
					
				resultSet = statement.executeQuery("SELECT natter.user.id FROM natter.user WHERE "
							+ "natter.user.email = '"+signIn.getEmail()+"' AND natter.user.social = '"+signIn.getSocial()+"'");
					
				if(resultSet.next()){
					int id = resultSet.getInt(1); 
					resultSet.close();		
					return id;
				}
			}
		}
		
		return -1;
	}
	
	public int updateEmail(UpdateEmail update) throws SQLException{
		Statement statement = this.connection.createStatement();
		ResultSet resultSet = statement.executeQuery("SELECT COUNT(natter.user.id) FROM natter.user WHERE natter.user.id = '"+update.getId()+"'");
		if(resultSet.next()){
			if(resultSet.getInt(1)==1){
					
				resultSet.close();
					
				int result = statement.executeUpdate("UPDATE natter.user SET "
							+ "natter.user.email = '"+update.getEmail()+"' , "
							+ "natter.user.email_edit = '"+fromBooleanToInt(update.isEditable())
							+"' WHERE natter.user.id = '" + update.getId() + "';");
					
				return result;
			}
		}
		
		return -1;
	}
	
	public int updatePhone(UpdatePhone update) throws SQLException{
		Statement statement = this.connection.createStatement();
		ResultSet resultSet = statement.executeQuery("SELECT COUNT(natter.user.id) FROM natter.user WHERE natter.user.id = '"+update.getId()+"'");
		if(resultSet.next()){
			if(resultSet.getInt(1)==1){
					
				resultSet.close();
					
				int result = statement.executeUpdate("UPDATE natter.user SET "
							+ "natter.user.phone = '"+update.getPhone()+"' , "
							+ "natter.user.phone_edit = '"+fromBooleanToInt(update.isEditable())
							+"' WHERE natter.user.id = '" + update.getId() + "';");
					
				return result;
			}
		}
		
		return -1;
	}
	
	public int updateProfileImage(ImageService image) throws SQLException{
		Statement statement = this.connection.createStatement();
		ResultSet resultSet = statement.executeQuery("SELECT COUNT(natter.user.id) FROM natter.user WHERE natter.user.id = '"+image.getId()+"'");
		if(resultSet.next()){
			if(resultSet.getInt(1)==1){
					
				resultSet.close();
					
				int result = statement.executeUpdate("UPDATE natter.user SET "
							+"natter.user.image = '"+image.getImage()
							+"' WHERE natter.user.id = '" + image.getId() + "';");
				
				return result;
			}
		}
		
		return -1;
	}
	
	public SignIn userInfo(SignIn signIn) throws SQLException{
		Statement statement = this.connection.createStatement();
		ResultSet resultSet = statement.executeQuery(
				"SELECT * FROM natter.user WHERE natter.user.id = '"+signIn.getId()+"'");
		if(resultSet.next()){
			boolean[] flag = new boolean[4];
			flag[0] = fromIntToBoolean(resultSet.getInt(3));
			flag[1] = fromIntToBoolean(resultSet.getInt(6));
			flag[2] = fromIntToBoolean(resultSet.getInt(8));
			flag[3] = fromIntToBoolean(resultSet.getInt(10));
			
			signIn = new SignIn(resultSet.getInt(1),resultSet.getString(2),resultSet.getString(4),resultSet.getString(5),resultSet.getString(7),resultSet.getString(9),flag,resultSet.getString(11),resultSet.getString(12));
			//id,email,password,name,surname,phone,flag,social,image
			//1 id - 2 email - 3 email_edit - 4 password - 5 name - 6 name_edit - 7 surname - 
			//8 surname_edit - 9 phone - 10 phone_edit - 11 social - 12 image
		}
		
		return signIn;
	}
	
	public boolean updatePosition(Position position) throws SQLException{
		
		String tabella = "natter.position";
		
		Statement statement = this.connection.createStatement();
		ResultSet resultSet = statement.executeQuery("SELECT COUNT("+tabella+".id_user) FROM "+tabella+" WHERE "+tabella+".id_user = '"+position.getId_natter()+"'");
		if(resultSet.next()){
			
			if(resultSet.getInt(1)==0){	
				int result = statement.executeUpdate("INSERT INTO "+tabella+"("+ 
						tabella+".id_user,"+
						tabella+".latitude,"+
						tabella+".longitude"+ ") "
						+ "VALUES ("+"'"+position.getId_natter()+"',"+"'"+position.getLatitude()+"',"+"'"+position.getLongitude()+"'"+")");
				
				if(result==1){
					
					System.out.println("Insert ok");
					return true;
				}
			}
			else{
				int result = statement.executeUpdate("UPDATE "+tabella+" SET "
						+tabella+".latitude = '"+position.getLatitude()+"' , "+tabella+".longitude = '"+position.getLongitude()+"' WHERE "+tabella+".id_user = '" + position.getId_natter() + "';");
				
				if(result==1){
					return true;
				}
			}
		}
		
		return false;
	}
	
	private static int fromBooleanToInt(boolean flag){
		return (flag==true) ? 1 : 0;
	}
	
	private static boolean fromIntToBoolean(int flag){
		return (flag==1) ? true : false;
	}
	
	
}
