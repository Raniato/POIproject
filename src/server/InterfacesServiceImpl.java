package server;

import javax.jws.WebMethod;

import java.lang.String;
import java.lang.Math;

import javax.jws.WebParam;
import javax.jws.WebService;

import main.Memory;

import  java.util.Date;
import java.util.Properties;
import java.io.*;
import java.sql.*;
@WebService(endpointInterface="server.InterfacesService") 
public class InterfacesServiceImpl implements InterfacesService {
 
	@Override
    @WebMethod 
    
	public String RegisterUser(String s) throws Exception{
		
		String[] parts = s.split("#");
    	String username = parts[0];
		String pw1 = parts[1];
		String pw2 = parts[2];
		
		Properties prop = new Properties();
		
		try{
			//Load property file from project folder
			prop.load(new FileInputStream("settings.properties"));
		} catch (IOException ex) {
			System.err.println("IO Exception occured while loading property file");
		}
			// get the property value and print it out
			Memory.getInstance().setDatabase(prop.getProperty("database"));
			Memory.getInstance().setIp(prop.getProperty("ip"));
			Memory.getInstance().setPort(prop.getProperty("port"));
		
		
		String database = prop.getProperty("database");
		String ip = prop.getProperty("ip");
		String port = prop.getProperty("port");
		
		Class.forName("com.mysql.jdbc.Driver");
    	Connection con = DriverManager.getConnection("jdbc:mysql://"+ip+":"+port+"/"+database ,"sdi0600255","75701523");
    	Statement statement = con.createStatement(); 
    	String sql= "SELECT username FROM user WHERE username=('"+username+"')";
        ResultSet rs=statement.executeQuery(sql); 
        
        if(rs.next()){
        	String checkuser=rs.getString(1);
        	if(checkuser.equals(username)){
        		return ("User already exists. Unsuccessfull registration of new user.");
        	}       	
        }
        else{
    		
	
    		
    		if(pw1.equals(pw2)){
    			String sql2 = "INSERT INTO user(username, password)"+"VALUES('"+username+"','"+pw1+"');";
        		statement.executeUpdate(sql2);
        		System.out.println("Inserting data to base..");
    			return "Successfull registration of new user";
    		}
	
    		return ("Unsuccessfull registration of new user (passwords not the same)");	
    	}
        
        return("Something went wrong");
	}
	
	
	public String setMonitorData(String s, String newEntry) throws ClassNotFoundException, SQLException{
		Properties prop = new Properties();
		Date date = new Date();
		long dif = date.getTime();
		
		try{
			//Load property file from project folder
			prop.load(new FileInputStream("settings.properties"));
		} catch (IOException ex) {
			System.err.println("IO Exception occured while loading property file");
		}
			// get the property value and print it out
			Memory.getInstance().setDatabase(prop.getProperty("database"));
			Memory.getInstance().setIp(prop.getProperty("ip"));
			Memory.getInstance().setPort(prop.getProperty("port"));
			Memory.getInstance().setPort(prop.getProperty("T"));
			Memory.getInstance().setPort(prop.getProperty("R"));
		
		
		
		int R = Integer.parseInt(prop.getProperty("R"));
		long T = Long.parseLong(prop.getProperty("T"));
		String database = prop.getProperty("database");
		String ip = prop.getProperty("ip");
		String port = prop.getProperty("port");
		
		
		String[] parts = s.split("#");
    	String username = parts[0];
		String password = parts[1];
		
		String[] entries = newEntry.split(" ");
		String place = entries[0];
		String type = entries[1];
		String name = entries[2];
		
		Class.forName("com.mysql.jdbc.Driver");
    	Connection con = DriverManager.getConnection("jdbc:mysql://"+ip+":"+port+"/"+database ,"sdi0600255","75701523");
    	Statement statement = con.createStatement(); 
    	String sql= "SELECT username, password FROM user WHERE username=('"+username+"')";
        ResultSet rs=statement.executeQuery(sql); 
        if(rs.next()){
        	String checkuser=rs.getString(1);
        	String checkpassword = rs.getString(2);
        	if(checkuser.equals(username) && checkpassword.equals(password)){
        		Statement stat = con.createStatement(); 
            	String sq= "SELECT setcounter FROM user WHERE username=('"+username+"')";
                ResultSet r=stat.executeQuery(sq); 
                if(r.next()){
                	if(r.getInt(1)==0){
                		
                		Statement star = con.createStatement();
                		String sql3="UPDATE user SET FirstEntry='"+dif+"' WHERE username=('"+username+"')";
        				star.executeUpdate(sql3);       
        				
                	}
                	
                	else{
                		Statement stat2 = con.createStatement(); 
                		String sq2= "SELECT FirstEntry FROM user WHERE username=('"+username+"')";
                		ResultSet r2=stat2.executeQuery(sq2); 
                    
                    
                		if(r2.next()){
                    	
                			if((dif - r2.getLong(1)) > (T*1000)){
                    		
                				Statement statementc2 = con.createStatement();
                				String counter2="UPDATE user SET setcounter=0 WHERE username=('"+username+"')";
                				statementc2.executeUpdate(counter2);
            				
                				Statement star = con.createStatement();
                				String sql3="UPDATE user SET FirstEntry='"+dif+"' WHERE username=('"+username+"')";
                				star.executeUpdate(sql3); 
	
                			}
                    	}
                	}
                	
                }
                Statement statementc1 = con.createStatement();
				String counter1="UPDATE user SET setcounter=setcounter+'1'" + "WHERE username=('"+username+"')";
				statementc1.executeUpdate(counter1);
        		String[] position = place.split(",");
        		String positionX=position[0];
        		String positionY=position[1];
        		String sql2="SELECT name FROM PoiID WHERE name=('"+name+"')";
        		ResultSet rs2=statement.executeQuery(sql2); 
        		if(rs2.next()){
        			PreparedStatement statement2 = con.prepareStatement("SELECT PositionX, PositionY FROM PoiID WHERE name=('"+name+"')");
        			ResultSet result = statement2.executeQuery();
        			while(result.next()){
        	    		String PositionX = result.getString(1);
        	    		String PositionY = result.getString(2);    
            			double x1 = Double.parseDouble(PositionX);
            			double y1 = Double.parseDouble(PositionY);
            			double x2 = Double.parseDouble(positionX);
            			double y2 = Double.parseDouble(positionY);
            			x2=Math.round(x2);
            			y2=Math.round(y2);
            			if(Math.sqrt(Math.pow((x2-x1), 2) + Math.pow((y2-y1), 2)) <= R){
            				Statement statementc = con.createStatement();
            				String counter="UPDATE PoiID SET POI_counter=POI_counter+1 WHERE name=('"+name+"')";
            				statementc.executeUpdate(counter);
            				return("An entry with the same name within range R already exists. You checked in.");
            				
            			}
        	    	}   //na doume ta exception pou xei lyssaksei o gkafri        			
        		}
        		
        		else{
        			PreparedStatement statement6 = con.prepareStatement("SELECT PositionX, PositionY FROM PoiID WHERE PositionX=('"+positionX+"') && PositionY=('"+positionY+"')");
        			ResultSet result = statement6.executeQuery();   
        			
        			
        			if(result.next()){
        				
        				Statement statementc2 = con.createStatement();
        				String counter2="UPDATE PoiID SET POI_counter=POI_counter+'1' WHERE name=('"+name+"')";
        				statementc2.executeUpdate(counter2);
        				return("There is already a Point at this place. You checked in.");
        			}
        			else{
        				int positionXi = Integer.parseInt(positionX);
        				int positionYi = Integer.parseInt(positionY);
        				String sql3="INSERT INTO PoiID(username, password, type, name, PositionX, PositionY)" + "VALUES('"+username+"','"+password+"','"+type+"', '"+name+"', '"+positionXi+"', '"+positionYi+"');"; 
        				statement.executeUpdate(sql3);       
        				Statement statementc = con.createStatement();
        				String counter="UPDATE PoiID SET POI_counter=POI_counter+'1'" + "WHERE name=('"+name+"')";
        				statementc.executeUpdate(counter);
        				return("Successfull insert of data!!");
        			}
    				
        		}
        		
        	}  
        	else{
        		return("User does not exist or this is not his password. Please register specific user.");
        	}
        }
		
		return("Something went wrong while openning a connection to the database");
		
		
	}
	
	public String getMapData(String userpw, String position) throws Exception{
		Date date = new Date();
		long dif = date.getTime();
		String[] userpassword = position.split("#");
		String username = userpassword[0];
		String password = userpassword[1];
		
		Properties prop = new Properties();
		
		try{
			//Load property file from project folder
			prop.load(new FileInputStream("settings.properties"));
		} catch (IOException ex) {
			System.err.println("IO Exception occured while loading property file");
		}
			// get the property value and print it out
			Memory.getInstance().setDatabase(prop.getProperty("database"));
			Memory.getInstance().setIp(prop.getProperty("ip"));
			Memory.getInstance().setPort(prop.getProperty("port"));
			Memory.getInstance().setPort(prop.getProperty("T"));
			Memory.getInstance().setPort(prop.getProperty("R"));
		
		
		
		int R = Integer.parseInt(prop.getProperty("R"));
		long T = Long.parseLong(prop.getProperty("T"));
		
		String database = prop.getProperty("database");
		String ip = prop.getProperty("ip");
		String port = prop.getProperty("port");
		
		
		String[] part = position.split(",");
		String positionX=part[0];
		String positionY=part[1];
		Class.forName("com.mysql.jdbc.Driver");
    	Connection con = DriverManager.getConnection("jdbc:mysql://"+ip+":"+port+"/"+database ,"sdi0600255","75701523");
    	Statement statement = con.createStatement(); 
    	String sql= "SELECT username, password FROM user WHERE username=('"+username+"')";
        ResultSet rs=statement.executeQuery(sql); 
        if(rs.next()){
        	String checkuser=rs.getString(1);
        	String checkpassword = rs.getString(2);
        	if(checkuser.equals(username) && checkpassword.equals(password)){
        		
        		Statement stat = con.createStatement(); 
            	String sq= "SELECT setcounter FROM user WHERE username=('"+username+"')";
                ResultSet r=stat.executeQuery(sq); 
                if(r.next()){
                	if(r.getInt(1)==0){
                		
                		Statement star = con.createStatement();
                		String sql3="UPDATE user SET FirstEntry='"+dif+"' WHERE username=('"+username+"')";
        				star.executeUpdate(sql3);       
        				
                	}
                	
                	else{
                		Statement stat2 = con.createStatement(); 
                		String sq2= "SELECT FirstEntry FROM user WHERE username=('"+username+"')";
                		ResultSet r2=stat2.executeQuery(sq2); 
                    
                    
                		if(r2.next()){
                    	
                			if((dif - r2.getLong(1)) > (T*1000)){
                    		
                				Statement statementc2 = con.createStatement();
                				String counter2="UPDATE user SET setcounter=0 WHERE username=('"+username+"')";
                				statementc2.executeUpdate(counter2);
            				
                				Statement star = con.createStatement();
                				String sql3="UPDATE user SET FirstEntry='"+dif+"' WHERE username=('"+username+"')";
                				star.executeUpdate(sql3); 
	
                			}
                    	}
                	}
                }	
        		
    			Statement statementc = con.createStatement();
    			String counter1="UPDATE user SET getcounter=getcounter+'1'" + "WHERE username=('"+username+"')";
    			statementc.executeUpdate(counter1);
    	
    			PreparedStatement statement2 = con.prepareStatement("SELECT PositionX, PositionY FROM PoiID WHERE PositionX=('"+positionX+"') && PositionY=('"+positionY+"')");
    			ResultSet result = statement2.executeQuery();
    			int i=0;
    			while(result.next()){  //an yparxei to shmeio sth vash
			
    				Statement statementc1 = con.createStatement();
    				String counter2="UPDATE PoiID SET POI_counter=POI_counter+1 WHERE PositionX=('"+positionX+"') && PositionY=('"+positionY+"')";
    				statementc1.executeUpdate(counter2);
			
    				PreparedStatement statement3 = con.prepareStatement("SELECT COUNT(id) FROM PoiID");
    				ResultSet result2 = statement3.executeQuery();	
			
    				while(result2.next()){
    					i=result2.getInt(1);
    				}
    				if(i!=0){
    					String[] positionsX = new String[i];
    					String[] positionsY = new String[i];
	    				PreparedStatement statement4 = con.prepareStatement("SELECT PositionX, PositionY FROM PoiID");
	    				ResultSet result4 = statement4.executeQuery();
	    				int j=0;
	    				while(result4.next()){
	    					positionsX[j] = result4.getString(1);
	    					positionsY[j] = result4.getString(2);					
	    					j++;
	    				}
	    				int[] x2 = new int[i];
	    				int[] y2 = new int[i];
	    				for(int k=0;k<i;k++){
	    					x2[k] = Integer.parseInt(positionsX[k]);
	    					y2[k] = Integer.parseInt(positionsY[k]);
	    				}
	    				int x1 = Integer.parseInt(positionX);
	    				int y1 = Integer.parseInt(positionY);

	    				int counter=0;
	    				for(int k=0;k<i;k++){
	    					if(Math.sqrt(Math.pow((x2[k]-x1), 2) + Math.pow((y2[k]-y1), 2)) <= R){
	    						counter++;
	    					}
	    				}
    			    			
    			
	    				String[] newX = new String[counter];
	    				String[] newY = new String[counter]; 
	    				int l=0;
	    				for(int k=0;k<i;k++){
	    					if(Math.sqrt(Math.pow((x2[k]-x1), 2) + Math.pow((y2[k]-y1), 2)) <= R){
	    						newX[l] = Integer.toString(x2[k]);
	    						newY[l] = Integer.toString(y2[k]);
	    						l++;
	    					}
	    				}										
	    				StringWriter sw=new StringWriter();				
	    				for(int k=0;k<l;k++){			    
	    					PreparedStatement statement5 = con.prepareStatement("SELECT name, type FROM PoiID WHERE PositionX=('"+newX[k]+"') && PositionY=('"+newY[k]+"') ");
	    					ResultSet result5 = statement5.executeQuery();
	    					while(result5.next()){
	    						int length=0;
	    						String x="name=" +result5.getString(1) + " and type:"+ result5.getString(2) + " at position X=" +newX[k]+" and Y=" +newY[k] +"\n";	
	    						length=x.length();
	    						sw.write(x, 0, length);
	    					}
	    				}
	    				return (""+ sw.toString());
				//return("For the desired position, name of the place that exists is "+ result2.getString(1) + " and the type is: " + result2.getString(2));
	    		
    				}
    			}
			
        	}
        }
    	return("No places defined for the position you entered.");
	}
	
	


}
