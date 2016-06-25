/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package main;

import javax.xml.ws.Endpoint; 

import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.Properties;
import java.sql.*;

import javax.swing.JFrame;

import server.InterfacesServiceImpl;
import server.AdminIndex;

public class MyMain {

    /**
     * @param args the command line arguments
     * @throws ClassNotFoundException 
     * @throws SQLException 
     */
	
	
    public static void main(String[] args) throws Exception{     
    
    	graphs();
    	Properties prop = new Properties();

		try {
			// Store properties to property file
			prop.setProperty("database", "sdi0600255");
			prop.setProperty("ip", "195.134.65.254");
			prop.setProperty("port", "3306");
			prop.setProperty("T", "10800");
			prop.setProperty("R", "2");

			// Save property file to project folder
			prop.store(new FileOutputStream("settings.properties"), null);
		} catch (IOException ex) {
			System.err.println("IO Exception occured while saving property file");
		}try{
			//Load property file from project folder
			prop.load(new FileInputStream("settings.properties"));
		} catch (IOException ex) {
			System.err.println("IO Exception occured while loading property file");
		}
			

        Endpoint.publish("http://localhost:8085/Askisi1/InterfacesService?WSDL", new InterfacesServiceImpl()); 
        
        
    	
		
    }
    
    public static void graphs() throws Exception {
    	
    	AdminIndex newframe = new AdminIndex();
        newframe.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        newframe.setSize(600, 300);
        newframe.setVisible(true);
    	
    }
    
    
    
    
    
    
    
}
