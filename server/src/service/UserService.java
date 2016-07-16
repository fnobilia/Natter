package service;

import java.sql.SQLException;

import javax.ws.rs.GET;
import javax.ws.rs.POST;
import javax.ws.rs.Path;
import javax.ws.rs.Produces;
import javax.ws.rs.core.Context;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Request;
import javax.ws.rs.core.UriInfo;

import classes.Esito;
import classes.ImageService;
import classes.Login;
import classes.Position;
import classes.SignIn;
import classes.UpdateEmail;
import classes.UpdatePhone;
import dao.UserDao;

@Path("/user")
public class UserService{
	
	@Context
    UriInfo uriInfo;
    @Context
    Request request;
        
    @GET
    @Path("/verifica_user")
    @Produces({MediaType.APPLICATION_XML, MediaType.APPLICATION_JSON})
    public String verifica(){
    	return "<Test>USER "+System.currentTimeMillis()+"</Test>";
    }
    
    @POST
    @Path("/login")
    @Produces({MediaType.APPLICATION_XML, MediaType.APPLICATION_JSON})
    public Esito login(final Login login){
    	UserDao dao = new UserDao();
    	
    	Esito esito;
    	
    	try{
    		int id = dao.login(login);
    	
    		if(id==-1){
    			esito = new Esito("Email or password are wrong");
    		}
    		else{
    			esito = new Esito(true,"OK",new Integer(id));
    		}
    	}catch(SQLException e){
    		System.out.println(e.getMessage());
    		esito = new Esito(e.getMessage());
    	}
    	finally{
    		dao.closeConnection();
    	}
    	
    	return esito;
    }
    
    @POST
    @Path("/signin")
    @Produces({MediaType.APPLICATION_XML, MediaType.APPLICATION_JSON})
    public Esito signIn(final SignIn signIn){
    	UserDao dao = new UserDao();
    	
    	Esito esito;
    	
    	try{
    		int id = dao.signIn(signIn);
    	
    		if(id==-1){
    			esito = new Esito("Email already exists");
    		}
    		else{
    			esito = new Esito(true,"OK",new Integer(id));
    		}
    	}catch(SQLException e){
    		System.out.println(e.getMessage());
    		esito = new Esito(e.getMessage());
    	}
    	finally{
    		dao.closeConnection();
    	}
    	
    	return esito;
    }
    
    @POST
    @Path("/userInfo")
    @Produces({MediaType.APPLICATION_XML, MediaType.APPLICATION_JSON})
    public Esito userInfo(final SignIn signIn){
    	UserDao dao = new UserDao();
    	
    	Esito esito;
    	
    	try{   	
    		SignIn result = dao.userInfo(signIn);
    		
    		esito = new Esito(true,"OK",result);
    	}catch(SQLException e){
    		System.out.println(e.getMessage());
    		esito = new Esito(e.getMessage());
    	}
    	finally{
    		dao.closeConnection();
    	}
    	
    	return esito;
    }
    
    @POST
    @Path("/updateEmail")
    @Produces({MediaType.APPLICATION_XML, MediaType.APPLICATION_JSON})
    public Esito updateEmail(UpdateEmail update){
    	UserDao dao = new UserDao();
    	
    	Esito esito;
    	
		try{
	    	int outcome = dao.updateEmail(update);
			if(outcome==-1){
	    		esito = new Esito("ID does not exist!");
	    	}
	    	else{
	    		esito = new Esito(true,"OK","Email updated!");
	    	}
		}catch(SQLException e){
			System.out.println(e.getMessage());
			esito = new Esito(e.getMessage());
		}
		finally{
			dao.closeConnection();
		}
    	
    	return esito;
    }
    
    @POST
    @Path("/updatePhone")
    @Produces({MediaType.APPLICATION_XML, MediaType.APPLICATION_JSON})
    public Esito updatePhone(UpdatePhone update){
    	UserDao dao = new UserDao();
    	
    	Esito esito;
    	
    	try{
    		int outcome = dao.updatePhone(update);
    	
    		if(outcome==-1){
    			esito = new Esito("ID does not exist!");
    		}
    		else{
    			esito = new Esito(true,"OK","Phone updated!");
    		}
    	}catch(SQLException e){
    		System.out.println(e.getMessage());
    		esito = new Esito(e.getMessage());
    	}
    	finally{
    		dao.closeConnection();
    	}
    	
    	return esito;
    }
    
    @POST
    @Path("/updateImageProfile")
    @Produces({MediaType.APPLICATION_XML, MediaType.APPLICATION_JSON})
    public Esito sendImageProfile(ImageService image){
    	UserDao dao = new UserDao();
    	
    	Esito esito;
    	
    	try{
    		int outcome = dao.updateProfileImage(image);
    	
    		if(outcome==-1){
    			esito = new Esito("ID does not exist!");
    		}
    		else{
    			esito = new Esito(true,"OK","Image updated!");
    		}
    	}catch(SQLException e){
    		System.out.println(e.getMessage());
    		esito = new Esito(e.getMessage());
    	}
    	finally{
    		dao.closeConnection();
    	}
    	
    	return esito;
    }
    
    @POST
    @Path("/sendposition")
    @Produces({MediaType.APPLICATION_XML, MediaType.APPLICATION_JSON})
    public Esito sendPosition(Position position){
    	UserDao dao = new UserDao();
    	
    	Esito esito;
    	
    	try{
    		boolean outcome = dao.updatePosition(position);
    	
    		if(outcome){
    			esito = new Esito(true,"OK","Position updated!");
    		}
    		else{
    			esito = new Esito(false,"KO","Position NOT updated!");
    		}
    	}catch(SQLException e){
    		System.out.println(e.getMessage());
    		esito = new Esito(e.getMessage());
    	}
    	finally{
    		dao.closeConnection();
    	}
    	
    	return esito;
    }
}