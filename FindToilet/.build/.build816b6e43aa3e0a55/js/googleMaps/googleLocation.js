
var googleLocation = (function ($, logger, view, network, ajax) {
    var appKey, internetConnectionCheck, setMarker, setMarkerList, setInfo;
    
    appKey = "AIzaSyDdKjhStoKF6t0xxA_hFxYBmKrEb77b-nQ";

    /**
     * Asynch method to check the network connection
     * @private
     */
    internetConnectionCheck = function () {
        network.isInternetConnection(function (isConnection) {
            if (!isConnection) {
                view.hideLoader();
                view.showPopup("No Internet connection. Application may not work properly.");
            }
        });
    };
    
    setInfo = function(map, marker, name, info, imgUrl) {
    	// info window
    	var contentString = 
  		  '<div id="toiletInfoWnd">'+
  		  	'<h1 id="toiletName">' + name + '</h1>'+ 
  		  	'<div id="toiletInfo">'+ info + '</div>'+
  		  '</div>';

  	  	var infowindow = new google.maps.InfoWindow({
  	  		content: contentString
  	  	});
    
    google.maps.event.addListener(marker, 'click', function() {
  	    infowindow.open(map,marker);
  	  });
    }

    // set single marker on center of map
    setMarker = function(lat, lon) {
    	var myLatlng = new google.maps.LatLng(lat,lon);
    	var mapOptions = {
    	  zoom: 15,
    	  center: myLatlng
    	}
    	var map = new google.maps.Map(document.getElementById("map_canvas"), mapOptions);

    	var marker = new google.maps.Marker({
    	    position: myLatlng,
    	    title:"화장실1"
    	});

    	// To add the marker to the map, call setMap();
    	marker.setMap(map);
    	setInfo(map, marker, "참좋은 화장실", "아주 좋은 화장살이지요", "");    	
    };
    
    // set center position of map and set multiple markers on the map 
    setMarkerList = function(centerLat, centerLon, locations) {
    	
    	var map = new google.maps.Map(document.getElementById('map_canvas'), {
    	      zoom: 15,
    	      center: new google.maps.LatLng(centerLat, centerLon),
    	      mapTypeId: google.maps.MapTypeId.ROADMAP
    	    });
    	
    	var infowindow = new google.maps.InfoWindow();

        var marker, i;
        
        // center is user. 
        locations.unshift(["Me", centerLat, centerLon, "Me"]);
        
        // set marker icon image & size
        var markerIconMe = new google.maps.MarkerImage(
        		"img/human_icon_2.png",
        	    null, /* size is determined at runtime */
        	    null, /* origin is 0,0 */
        	    null, /* anchor is bottom center of the scaled image */
        	    new google.maps.Size(42, 42)
        	);
        var markerIconToilet = new google.maps.MarkerImage(
        		"img/marker_pink.png",
        	    null, /* size is determined at runtime */
        	    null, /* origin is 0,0 */
        	    null, /* anchor is bottom center of the scaled image */
        	    new google.maps.Size(42, 42)
        	);

        
        for (i = 0; i < locations.length; i++) {  
            marker = new google.maps.Marker({
              position: new google.maps.LatLng(locations[i][1], locations[i][2]),
              map: map,
              icon : (i==0 ? markerIconMe : markerIconToilet)
            });

            google.maps.event.addListener(marker, 'click', (function(marker, i) {
              return function() {
                infowindow.setContent(locations[i][0]);
                infowindow.open(map, marker);
              }
            })(marker, i));
          }
    };
    
    return {
        /**
         * Provides initialization for the app
         */
        initialize: function () {
            var that = this;
            ajax();
            $.extend($.mobile, {
                defaultPageTransition: "flip",
                loadingMessageTextVisible: true,
                pageLoadErrorMessage: "Unable to load page",
                pageLoadErrorMessageTheme: "d",
                touchOverflowEnabled: true,
                loadingMessage: "Please wait...",
                allowCrossDomainPages: true,
                ajaxEnabled: false
            });
            logger.info("googleLocation.initialize()");
            internetConnectionCheck();
            $('#main').live('pageshow', function () {
                internetConnectionCheck();
                $(this).find("li#myLocation").bind({
                    click: function (event) {
                        event.preventDefault();
                        //view.showLoader();
                        that.getCurrentLocation();
                    }
                });
            });
            
            logger.info("Samsung-Dong");
            var centerLat = 37.508755;
            var centerLon = 127.060961;
            
            $('#toiletMap').live('pageshow', function () {
                internetConnectionCheck();
                that.createMapForGivenContainer("map_canvas", {
                    zoom: 15,
                    lat:centerLat,
                    lon:centerLon,
                    streetViewControl: false,
                    mapTypeId: google.maps.MapTypeId.ROADMAP	// ROADMAP, HYBRID
                });
                
                $.ajax({
                    type:"GET",
                    url: 'http://175.209.0.128:8182/lat/' + centerLat + '/lng/' + centerLon,
                    dataType : "json", //전송받을 데이터의 타입 "xml", "html", "script", "json" 등 지정 가능
                    timeout : 60000, //제한시간 지정
                    success: function (data) {
                        logger.info(data);
                        logger.info("Status", data.status);
                        var locations = [];	// "[[위치, latitude, longitude, metadata],...]"
                        for(var i=0; i<data.length; ++i) {
                        	var d = data[i];
                        	locations.push([d.GUNAME+" "+d.HNRAME, d.LAT, d.LNG, ""]);
                        }
                        setMarkerList(centerLat, centerLon, locations);
                    },
                    complete : function(data) {
                        // 통신이 실패했어도 완료가 되었을 때 이 함수를 타게 된다.
                    	//hide.hideLoader();
                    	view.hideLoader();                    	
                    },
                    error : function(xhr, status, error) {
                    	view.hideLoader();
                        alert("당황하지 말고... 다시  시도 하면~ 끝!\n" + status + xhr);
                    }
                });
            });
           
            view.getScreenHeight();
            view.getScreenWidth();
        },


        /**
         * Method that can be used for basic google.maps.Map creation for given container
         * @param container
         * @param options
         * @returns {Object} google.maps.Map
         */
        createMapForGivenContainer: function (container, options) {
            var mapOptions, map;

            mapOptions = {
                center: new google.maps.LatLng(options.lat, options.lon),
                zoom: options.zoom,
                mapTypeId: options.mapTypeId,
                streetViewControl: options.streetViewControl
            };
            map = new google.maps.Map(document.getElementById(container), mapOptions);
            return map;
        },

        /**
         * Method that can be used to get current device geolocation according to W3C Geolocation API
         * @returns
         */
        getCurrentLocation: function () {
            logger.info('getCurrentLocation');
            if (navigator && navigator.geolocation && navigator.geolocation.getCurrentPosition) {
                navigator.geolocation.getCurrentPosition(function (position) {
                    view.hideLoader();
                    // Currently Tizen returns coords as 0 0 and we should treat this as an error
                    if (position.coords.latitude === 0 && position.coords.longitude === 0) {
                        view.showPopup('Unable to acquire your location');
                    } else {
                        view.showPopup('Latitude: ' + position.coords.latitude + "<br />" + 'Longitude: ' + position.coords.longitude);
                    }
                }, function (error) {
                    view.hideLoader();
                    view.showPopup('Unable to acquire your location');
                    logger.err('GPS error occurred. Error code: ', JSON.stringify(error));
                });
            } else {
                view.hideLoader();
                view.showPopup('Unable to acquire your location');
                logger.err('No W3C Geolocation API available');
            }
        }
    };
}($, tlib.logger, tlib.view, tlib.network, tlib.ajax));

googleLocation.initialize();