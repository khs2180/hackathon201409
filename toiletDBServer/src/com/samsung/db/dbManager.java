package com.samsung.db;

import java.io.PrintWriter;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.LinkedHashMap;

import org.json.simple.JSONValue;
import org.restlet.data.MediaType;
import org.restlet.representation.Representation;
import org.restlet.representation.StringRepresentation;


public class dbManager {
	private static dbManager singleton = null;
	private Connection connection = null;
	private Statement  statement = null;
	private ResultSet resultSet = null;
	private ArrayList<toiletItem> toiletItems = new ArrayList<toiletItem>();
	
	public static dbManager getInstance(){
		if( null == singleton ){
			singleton = new dbManager();
			try {
				singleton.setDBInfo();
			} catch (SQLException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		}
		
		return singleton;
	}
	public void setDBInfo() throws SQLException{
		try{
			Class.forName("org.sqlite.JDBC");
		}
		catch(ClassNotFoundException e){
			System.out.println("org.sqlite,JDBC를 찾지 못했습니다.");
		}
		
		try {
			connection = DriverManager.getConnection("jdbc:sqlite:toiletDB.db");
			statement  = connection.createStatement();
			resultSet = statement.executeQuery("SELECT * FROM tbToilet;");
			toiletItems.clear();
			//resultSet = statement.executeQuery("SELECT * FROM sqlite_master WHERE type = 'table'");
			while(resultSet.next()){
				//PrintWriter pw = new PrintWriter()
				//pw.printf("\"%d\",\"%s\"", new Object[]{new Integer(resultSet.getString("OBJID")), resultSet.getString("LAT")});
				toiletItems.add(new toiletItem(
						        Integer.parseInt(resultSet.getString("OBJID")) ,
						        resultSet.getString("GU_NM"),
						        resultSet.getString("HNR_NAM"),
						        resultSet.getString("LAT"),
						        resultSet.getString("LNG")));

				//System.out.println(toiletItems.get(toiletItems.size()-1).toString());
			}
			
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();

		}
		
		if( null != resultSet)
			resultSet.close();
		if( null != connection)
			connection.close();
	}
	private dbManager() {
		
	}
	
	private Boolean isContainRgn(String lat, String lgn, String latTgt, String lngTgt){
		Boolean result = false;
		double orgLat = Double.valueOf(lat);
		double orgLgn = Double.valueOf(lgn);
		
		double srcLat = Double.valueOf(latTgt);
		double srcLgn = Double.valueOf(lngTgt);
		
		//System.out.println(Math.abs(orgLat - srcLat) + "//" + Math.abs(orgLgn - srcLgn));
		if( Math.abs(orgLat - srcLat) <= 0.01 && Math.abs(orgLgn - srcLgn) <= 0.01) 
			return true;
		return result;
	}
	public Representation getToiletPos(String lat, String lng){
		LinkedHashMap<Object, String> list = new LinkedHashMap<Object, String>();
		
		String jSonstr = null;
		StringBuffer sb = new StringBuffer("[");

		for (toiletItem toiletItem : toiletItems) {
			//sb.append(toiletItem.toString());
			//sb.append(",");
			
			if( false == isContainRgn(lat, lng, toiletItem.getLAT(), toiletItem.getLNG())) continue;
			list.put("OBJID", String.valueOf(toiletItem.getID()) );
			list.put("GUNAME", String.valueOf(toiletItem.getGUNAME()) );
			list.put("HNRAME", String.valueOf(toiletItem.getHNRNAME()) );
			list.put("LAT", String.valueOf(toiletItem.getLAT()) );
			list.put("LNG", String.valueOf(toiletItem.getLNG()) );
			
			jSonstr = JSONValue.toJSONString(list);
			sb.append(jSonstr);
			sb.append(",");
		}
		
		String JsonInfo = sb.substring(0, sb.length()-1);
		JsonInfo += "]";
		
		return new StringRepresentation(JsonInfo, MediaType.APPLICATION_JSON);
	}
}
