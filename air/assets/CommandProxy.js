CommandProxy = {
	url : "", 
	port : "37148", 
	
	/**
	 * Called by AIR when the application is started.
	 * This is used to determine the port number of the 
	 * command proxy. 
	 */
	onInvoke : function( event ){
		for( arg in event.arguments ){
			if( event.arguments[arg].indexOf( "--port=" ) == 0 ){
				CommandProxy.port = parseInt( event.arguments[arg].substring( 7 ) ); 
			}
		}
		
		CommandProxy.url = "http://localhost:" + CommandProxy.port + "/"; 
	}, 
	
	/**
	 * Use this to call a command on the command proxy server. 
	 *
	 * @param command The name of the command
	 * @param parameters A map of parameters, e.g. { "file": "/myfile.pdf" }
	 * @param (optional) callback, a function with one parameter, that will be given a json object when the result is ready.  
	 */
	call : function( command, parameters, callback ){
		if( !parameters )
			parameters = {}; 

		
		parameters.applicationDirectory = air.File.applicationDirectory.nativePath; 
		
		if( CommandProxy.url == "" ){
			if( callback ){
				callback( { "error" : "no connection to the command proxy" } );
			}
			else{
				return { "error" : "no connection to the command proxy" }; 
			}
		}
		else{
			// Let's set everything up.. 
			var paramString = ""; 
			for( var param in parameters ){
				paramString += encodeURI( param ) + "=" + encodeURI( parameters[param] ) + "&"; 
			}
			
			var async = (typeof (callback) == "function");

			var req = new XMLHttpRequest();
			req.open( "POST", CommandProxy.url + command, async ); 
			
			req.setRequestHeader("Content-type", "application/x-www-form-urlencoded");
			req.setRequestHeader("Content-length", paramString.length);
			req.setRequestHeader("Connection", "close");
			
			if( async ){
				req.onreadystatechange = function(){
					if( req.readyState == 4 && callback ) {
						if( req.status == 200 ){
							callback( JSON.parse( req.responseText ) ); 
						}
						else{
							callback( { "error" : req.responseText } ); 
						}
					}
				};
				req.send( paramString ); 
			}
			else{
				req.send( paramString ); 
				
				var ret = JSON.parse( req.responseText );
				
				return ret; 
			}
		}
	},
	
	available : function(){
		return port > 0; 
	}
};

air.NativeApplication.nativeApplication.addEventListener( air.InvokeEvent.INVOKE, CommandProxy.onInvoke );