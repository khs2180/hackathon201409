package com.samsung.by;

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
