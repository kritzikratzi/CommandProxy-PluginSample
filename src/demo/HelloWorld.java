package demo;

import java.util.Map;

import org.json.JSONException;
import org.json.JSONObject;

import commandproxy.core.Command;
import commandproxy.core.CommandException;

public class HelloWorld implements Command{
	
	public String getName() {
		return "helloWorld"; 
	}
	
	public JSONObject execute( Map<String, String> params ) throws CommandException, JSONException {
		JSONObject response = new JSONObject(); 
		response.put( "Hello", "World!" );
		
		return response; 
	}
}
