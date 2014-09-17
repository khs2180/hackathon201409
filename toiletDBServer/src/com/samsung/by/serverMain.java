package com.samsung.by;

import java.sql.SQLException;

import org.restlet.data.Protocol;
import org.restlet.resource.ServerResource;
import org.restlet.Component;

import com.samsung.db.dbManager;

public class serverMain extends ServerResource{

	public static void main(String[] args) {
		// TODO Auto-generated method stub
		// Create a new Restlet component and add a HTTP server connector to it  
		dbManager.getInstance();
		
		Component component = new Component();  
		component.getServers().add(Protocol.HTTP, 8182); 

		// Attach the application to the component and start it  
		component.getDefaultHost().attach(new RouterApplication());  
		try {
			component.start();
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} 
	}

}
