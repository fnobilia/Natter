package service;

import java.io.IOException;
import java.sql.SQLException;
import java.util.ArrayList;

import javax.ws.rs.GET;
import javax.ws.rs.POST;
import javax.ws.rs.Path;
import javax.ws.rs.Produces;
import javax.ws.rs.core.Context;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Request;
import javax.ws.rs.core.UriInfo;

import classes.Access;
import classes.ContactService;
import classes.Esito;
import classes.GcmID;
import classes.MessageList;
import classes.MessageNatter;
import classes.Position;
import dao.ComunicationDao;

@Path("/comu")
public class ComunicationService{
	
	@Context
    UriInfo uriInfo;
    @Context
    Request request;
        
    @GET
    @Path("/verifica_comu")
    @Produces({MediaType.APPLICATION_XML, MediaType.APPLICATION_JSON})
    public String verifica(){
    	return "<Test>COMUNICATION "+System.currentTimeMillis()+"</Test>";
    }
    
    @POST
    @Path("/isuser")
    @Produces({MediaType.APPLICATION_XML, MediaType.APPLICATION_JSON})
    public Esito isUser(ContactService contact){
    	
    	ComunicationDao dao = new ComunicationDao();
    	
    	Esito esito;
    	
    	try{
    		if(dao.checkContact(contact)){
    			esito = new Esito(true,"Contact updated",contact);
    		}
    		else{
    			esito = new Esito(false,"Contact NOT updated",contact);
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
    @Path("/getposition")
    @Produces({MediaType.APPLICATION_XML, MediaType.APPLICATION_JSON})
    public Esito getPosition(Position position){
    	
    	ComunicationDao dao = new ComunicationDao();
    	
    	Esito esito;
    	
    	try{
    		if(dao.getPosition(position)){
    			esito = new Esito(true,"Contact updated",position);
    		}
    		else{
    			esito = new Esito(false,"This contact does NOT share his position",position);
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
    @Path("/registergmc")
    @Produces({MediaType.APPLICATION_XML, MediaType.APPLICATION_JSON})
    public Esito registerGCM(GcmID gmc){
    	
    	ComunicationDao dao = new ComunicationDao();
    	
    	Esito esito;
    	
    	try{
    		if(dao.registerGCM(gmc)){
    			esito = new Esito(true,"OK","Gcm id registered!");
    		}
    		else{
    			esito = new Esito(false,"KO","Error");
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
    @Path("/removegmc")
    @Produces({MediaType.APPLICATION_XML, MediaType.APPLICATION_JSON})
    public Esito removeGCM(GcmID gmc){
    	
    	ComunicationDao dao = new ComunicationDao();
    	
    	Esito esito;
    	
    	try{
    		if(dao.removeGCM(gmc)){
    			esito = new Esito(true,"OK","Gcm id removed!");
    		}
    		else{
    			esito = new Esito(false,"KO","Error");
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
    @Path("/sendMessage")
    @Produces({MediaType.APPLICATION_XML, MediaType.APPLICATION_JSON})
    public Esito sendMessage(MessageNatter message){
    	
    	/**********DA RIMUOVERE*********/
    	/**message.setReceiver("2");/**/
    	/***message.setSender("3");/***/
    	/**********DA RIMUOVERE*********/
    	
    	ComunicationDao dao = new ComunicationDao();
    	
    	Esito esito;
    	
    	try{
    		String result = dao.saveMessage(message);
    		
    		/**************************************DA RIMUOVERE*************************************/
    		/** MessageNatter temp = new MessageNatter("6","2","Ciao Francesco! Sono Ivi!",""); /**/
    		/****************************** dao.saveMessage(temp); /*******************************/
    		/**************************************DA RIMUOVERE*************************************/
    		
    		if(result.equals("Message saved!")){	
    			GcmUtility.sendNotific(message.getSender(),"T",dao.getGcm_Id(message.getReceiver()));
    			esito = new Esito(true,"OK","Notification sent!");
    		}
    		else{
    			esito = new Esito(result);
    		}
    		
    	}catch(IOException e){
    		System.out.println(e.getMessage());
    		esito = new Esito(e.getMessage());
    	} catch (SQLException e) {
    		System.out.println(e.getMessage());
    		esito = new Esito(e.getMessage());
		}
    	finally{
    		dao.closeConnection();
    	}
    	
    	return esito;
    }
    
    @POST
    @Path("/getMessage")
    @Produces({MediaType.APPLICATION_XML, MediaType.APPLICATION_JSON})
    public Esito getMessage(MessageNatter message){
    	ComunicationDao dao = new ComunicationDao();
    	
    	Esito esito;
    	
    	try{
    		ArrayList<MessageNatter> list = dao.getMessage(message);
    		
    		if(list==null){
    			esito = new Esito("Error");
    		}
    		else if(list.isEmpty()){
    			esito = new Esito(false,"OK","No message");
    		}
    		else{
    			
    			list.add(new MessageNatter("-1","-1","-1","-1"));
    			
    			esito = new Esito(true,Integer.toString(list.size()),new MessageList<>(list));
    		}
    		
    	} catch (SQLException e) {
    		System.out.println(e.getMessage());
    		esito = new Esito(e.getMessage());
		}
    	finally{
    		dao.closeConnection();
    	}
    	
    	return esito;
    }
    
    @POST
    @Path("/saveLastAccess")
    @Produces({MediaType.APPLICATION_XML, MediaType.APPLICATION_JSON})
    public Esito saveLastAccess(Access access){
    	ComunicationDao dao = new ComunicationDao();
    	
    	Esito esito;
    	
    	try{
    		if(dao.saveLastAccess(access)){
    			esito = new Esito(true,"OK","Access saved!");
    		}
    		else{
    			esito = new Esito("Id natter does not exist!");
    		}
    		
    	} catch (SQLException e) {
    		System.out.println(e.getMessage());
    		esito = new Esito(e.getMessage());
		}
    	finally{
    		dao.closeConnection();
    	}
    	
    	return esito;
    }
    
    @POST
    @Path("/getLastAccess")
    @Produces({MediaType.APPLICATION_XML, MediaType.APPLICATION_JSON})
    public Esito getLastAccess(Access access){
    	ComunicationDao dao = new ComunicationDao();
    	
    	Esito esito;
    	
    	try{
    		if(dao.getLastAccess(access)){
    			esito = new Esito(true,"OK",access);
    		}
    		else{
    			esito = new Esito("Id natter does not exist!");
    		}
    		
    	} catch (SQLException e) {
    		System.out.println(e.getMessage());
    		esito = new Esito(e.getMessage());
		}
    	finally{
    		dao.closeConnection();
    	}
    	
    	return esito;
    }
    
    @POST
    @Path("/deleteLastAccess")
    @Produces({MediaType.APPLICATION_XML, MediaType.APPLICATION_JSON})
    public Esito deleteLastAccess(Access access){
    	ComunicationDao dao = new ComunicationDao();
    	
    	Esito esito;
    	
    	try{
    		if(dao.deleteLastAccess(access)){
    			esito = new Esito(true,"OK","Access successfully deleted!");
    		}
    		else{
    			esito = new Esito("Id natter does not exist!");
    		}
    		
    	} catch (SQLException e) {
    		System.out.println(e.getMessage());
    		esito = new Esito(e.getMessage());
		}
    	finally{
    		dao.closeConnection();
    	}
    	
    	return esito;
    }
    
    @POST
    @Path("/sendVoice")
    @Produces({MediaType.APPLICATION_XML, MediaType.APPLICATION_JSON})
    public Esito sendVoice(MessageNatter message){
    	
    	/**********DA RIMUOVERE*********/
    	/**message.setReceiver("2");/**/
    	/***message.setSender("3");/***/
    	/**********DA RIMUOVERE*********/
    	
    	ComunicationDao dao = new ComunicationDao();
    	
    	Esito esito;
    	
    	try{
    		String result = dao.saveVoice(message);
    		
    		if(result.equals("Message saved!")){	
    			GcmUtility.sendNotific(message.getSender(),"V",dao.getGcm_Id(message.getReceiver()));
    			esito = new Esito(true,"OK","Notification sent!");
    		}
    		else{
    			esito = new Esito(result);
    		}
    		
    	}catch(IOException e){
    		System.out.println(e.getMessage());
    		esito = new Esito(e.getMessage());
    	} catch (SQLException e) {
    		System.out.println(e.getMessage());
    		esito = new Esito(e.getMessage());
		}
    	finally{
    		dao.closeConnection();
    	}
    	
    	return esito;
    }
    
    @POST
    @Path("/getVoice")
    @Produces({MediaType.APPLICATION_XML, MediaType.APPLICATION_JSON})
    public Esito getVoice(MessageNatter message){
    	ComunicationDao dao = new ComunicationDao();
    	
    	Esito esito = new Esito("INIT");
    	
    	try{
    		int result = dao.getVoice(message);
    		
    		if(result==-1){
    			esito = new Esito("Error");
    		}
    		else if(result==0){
    			esito = new Esito(false,"OK","No message");
    		}
    		else if(result==1){	
    			esito = new Esito(true,"OK",message);
    		}
    		
    	} catch (SQLException e) {
    		System.out.println(e.getMessage());
    		esito = new Esito(e.getMessage());
		}
    	finally{
    		dao.closeConnection();
    	}
    	
    	return esito;
    }
    
}
