package demo;

import java.awt.AWTException;
import java.awt.GraphicsConfiguration;
import java.awt.GraphicsDevice;
import java.awt.GraphicsEnvironment;
import java.awt.Rectangle;
import java.awt.Robot;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import java.util.Map;

import javax.imageio.ImageIO;

import org.json.JSONException;
import org.json.JSONObject;

import commandproxy.core.Command;
import commandproxy.core.CommandException;

/**
 * A slightly more complex example, 
 * this allows you to take screenshots. 
 * Crazy! 
 * 
 * @author hansi
 */
public class Screenshot implements Command{

	public JSONObject execute( Map<String, String> params ) throws CommandException, JSONException {
		int screenNr; 
		File dest = null;
		
		try{
			screenNr = Integer.parseInt( params.get( "screenNr" ) );
		}
		catch( Exception e ){
			screenNr = 0; 
		}
		
		try{
			dest = new File( params.get( "file" ) ); 
		}
		catch( Exception e ){
			try {
				dest = File.createTempFile( "commandproxy", "screenshot.png" );
			}
			catch (IOException ioe ){
				throw new CommandException( "Failed to create temp file, try using the dest parameter!", this, ioe );
			}
		}
		
		try {
			
			GraphicsDevice devices[] = GraphicsEnvironment.getLocalGraphicsEnvironment().getScreenDevices();
			screenNr %= devices.length; 
			GraphicsDevice device = devices[screenNr];
			GraphicsConfiguration config = device.getDefaultConfiguration();
			Rectangle region = new Rectangle( config.getBounds() );
			Robot robot = new Robot();
			BufferedImage screenshot = robot.createScreenCapture( region );
			
			ImageIO.write( screenshot, "png", dest );
		}
		catch ( AWTException e ){
			throw new CommandException( "Grabbing screenshot failed: " + e.getMessage(), this, e );
		}
		catch (IOException e) {
			throw new CommandException( "Screenshot couldn't be written to disk: " + e.getMessage(), this, e );
		}
		
		
		JSONObject result = new JSONObject();
		result.put( "file", dest.getAbsolutePath() );
		result.put( "screenNr", screenNr );
		
		return result; 
	}

	public String getName() {
		return "screenshot"; 
	}
}