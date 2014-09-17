package com.samsung.db;

/***************************************************************************
 *                                  
 *  Project : Candle (Toilet finder for tizen)
 *  
 *  The MIT License (MIT)
 *  
 *  Copyright (c) 2014, Taehyun Yoon <th82.yoon@samsung.com>, Byungyong Jeon <blue.jeon@samsung.com>, Hyunsung Kim <hs1122.kim@samsung.com>
 *  
 *  Permission is hereby granted, free of charge, to any person obtaining a copy
 *  of this software and associated documentation files (the "Software"), to deal
 *  in the Software without restriction, including without limitation the rights
 *  to use, copy, modify, merge, publish, distribute, sublicense, and/or sell
 *  copies of the Software, and to permit persons to whom the Software is
 *  furnished to do so, subject to the following conditions:
 *  
 *  The above copyright notice and this permission notice shall be included in
 *  all copies or substantial portions of the Software.
 *  
 *  THE SOFTWARE IS PROVIDED "AS IS", WITHOUT WARRANTY OF ANY KIND, EXPRESS OR
 *  IMPLIED, INCLUDING BUT NOT LIMITED TO THE WARRANTIES OF MERCHANTABILITY,
 *  FITNESS FOR A PARTICULAR PURPOSE AND NONINFRINGEMENT. IN NO EVENT SHALL THE
 *  AUTHORS OR COPYRIGHT HOLDERS BE LIABLE FOR ANY CLAIM, DAMAGES OR OTHER
 *  LIABILITY, WHETHER IN AN ACTION OF CONTRACT, TORT OR OTHERWISE, ARISING FROM,
 *  OUT OF OR IN CONNECTION WITH THE SOFTWARE OR THE USE OR OTHER DEALINGS IN
 *  THE SOFTWARE.
 *
 ***************************************************************************/

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
	
	private Statement  statementMeta = null;
	private ResultSet resultSetMeta = null;

	private ArrayList<toiletItem> toiletItems = new ArrayList<toiletItem>();
	private LinkedHashMap<Integer, toiletMetaItem> toiletMetaItems = new LinkedHashMap<Integer, toiletMetaItem>();
	
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
			
			System.out.println("HOHO." + resultSet.next());
			while(resultSet.next()){
				toiletItems.add(new toiletItem(
						        Integer.parseInt(resultSet.getString("OBJID")) ,
						        resultSet.getString("GU_NM"),
						        resultSet.getString("HNR_NAM"),
						        resultSet.getString("LAT"),
						        resultSet.getString("LNG")));
				//System.out.println(toiletItems.get(toiletItems.size()-1).toString());
			}
			
			if( null != resultSet)
				resultSet.close();
			
			//connectionMeta = DriverManager.getConnection("jdbc:sqlite:toiletDB.db");
			statementMeta = connection.createStatement(); 
			resultSetMeta = statementMeta.executeQuery("SELECT * FROM tbToiletMeta;");
			
			while(resultSetMeta.next()){
				int a = Integer.parseInt(resultSetMeta.getString("OBJID"));
				toiletMetaItems.put( Integer.parseInt(resultSetMeta.getString("OBJID"))
						            , new toiletMetaItem(
									        Integer.parseInt(resultSetMeta.getString("OBJID")) ,
									        resultSetMeta.getString("GRADE"),
									        resultSetMeta.getString("COMMENT")));
				}
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();

		}
		
		if( null != resultSetMeta)
			resultSetMeta.close();
		
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
		return false;
	}
	public Representation getToiletPos(String lat, String lng){
		LinkedHashMap<Object, String> list = new LinkedHashMap<Object, String>();
		
		String jSonstr = null;
		StringBuffer sb = new StringBuffer("[");
		int objID = 0;
		
		for (toiletItem toiletItem : toiletItems) {
			//sb.append(toiletItem.toString());
			//sb.append(",");
			
			if( false == isContainRgn(lat, lng, toiletItem.getLAT(), toiletItem.getLNG())) continue;
			
			list.put("OBJID", String.valueOf(toiletItem.getID()) );
			list.put("GUNAME", String.valueOf(toiletItem.getGUNAME()) );
			list.put("HNRAME", String.valueOf(toiletItem.getHNRNAME()) );
			list.put("LAT", String.valueOf(toiletItem.getLAT()) );
			list.put("LNG", String.valueOf(toiletItem.getLNG()) );
			
			toiletMetaItem Items = toiletMetaItems.get(toiletItem.getID());
			if( null != Items)
			{
				list.put("GRADE", Items.getGrade() );
				list.put("COMMENT", Items.getComment() );
			}
			else
			{
				list.put("GRADE", "" );
				list.put("COMMENT", "" );
			}
			jSonstr = JSONValue.toJSONString(list);
			sb.append(jSonstr);
			sb.append(",");
		}
		
		String JsonInfo = sb.substring(0, sb.length()-1);
		JsonInfo += "]";
		
		return new StringRepresentation(JsonInfo, MediaType.APPLICATION_JSON);
	}
}
