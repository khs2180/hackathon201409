package com.samsung.db;

public class toiletItem {
	private int OBJID;
	private String GUNAME_;
	private String HNRAME_;
	private String LAT_;
	private String LNG;
	
	public int getID(){ return OBJID;}
	public String getGUNAME(){ return GUNAME_;}
	public String getHNRNAME(){ return HNRAME_;}
	public String getLAT(){ return LAT_;}
	public String getLNG(){ return LNG;}
	
	public toiletItem(int id, String guname, String hnrame, String lat, String lgn){
		this.OBJID = id;
		this.GUNAME_ = guname;
		this.HNRAME_ = hnrame;
		this.LAT_  = lat;
		this.LNG   = lgn;
	}
	
	public String toString(){
		return "{"+"OBJID"+":"+String.valueOf(this.OBJID) + ","+"GU_NAME"+":"+this.GUNAME_+ ","+"HNR_NAME"+":"+this.HNRAME_+ "," +"LAT:"+ this.LAT_ + "," +"LNG:" +this.LNG + "}";
	}
}
