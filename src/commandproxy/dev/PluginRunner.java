package commandproxy.dev;

import commandproxy.cli.DebugProxy;

public class PluginRunner {

	public static void main( String args[] ){
		System.out.println( "Launching debug proxy..." ); 
		DebugProxy proxy = new DebugProxy();
	}
}
