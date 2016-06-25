/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package server;
  
import javax.jws.WebMethod;

import java.lang.String;

import javax.jws.WebParam;
import javax.jws.WebService;
import javax.jws.soap.SOAPBinding;
import javax.jws.soap.SOAPBinding.Style;

@WebService
@SOAPBinding(style=Style.DOCUMENT)
public interface InterfacesService 
{      
    @WebMethod 
    
    public String RegisterUser(String s) throws Exception; 
    public String setMonitorData(String s, String newEntry) throws Exception; 
    public String getMapData(String userpw, String position) throws Exception;
    
    
}