package com.samsung.by;

import org.restlet.representation.Representation;
import org.restlet.resource.Get;
import org.restlet.resource.ServerResource;

import com.google.gson.JsonArray;
import com.samsung.db.dbManager;


public class GeoPosInfo extends ServerResource {
	@Get  
	public Representation printString() {
		String lat = (String)getRequestAttributes().get("latPos");
		String lng = (String)getRequestAttributes().get("lngPos");
		
		return dbManager.getInstance().getToiletPos(lat, lng);
	}  

}
