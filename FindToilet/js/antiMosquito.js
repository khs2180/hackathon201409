var antiMosquito = {
		soundObject : '#antiMosSoundObject',
		play : function(){
			$(antiMosquito.soundObject).trigger("play");
		},
		pause : function() {
			$(antiMosquito.soundObject).trigger("pause");
		}
};