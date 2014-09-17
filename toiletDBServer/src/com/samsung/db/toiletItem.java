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
