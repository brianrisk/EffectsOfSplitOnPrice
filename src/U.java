import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.net.URL;
import java.nio.channels.Channels;
import java.nio.channels.ReadableByteChannel;


public class U {
	
	public static void fail(String s) {
		p(s);
		System.exit(1);
	}
	
	public static void p(Object o) {
		System.out.println(o);
	}
	
	/**
	 * Utility file to download a file from a URL
	 * 
	 * Based on:
	 * http://stackoverflow.com/questions/921262/how-to-download-and-save-a-file-from-internet-using-java
	 * @param urlString
	 * @param destination
	 */
	public static void downloadFileFromURL(String urlString, File destination) {	
		try {
			URL website = new URL(urlString);
			ReadableByteChannel rbc;
			rbc = Channels.newChannel(website.openStream());
			FileOutputStream fos = new FileOutputStream(destination);
			fos.getChannel().transferFrom(rbc, 0, Long.MAX_VALUE);
			fos.close();
		} catch (IOException e) {
			e.printStackTrace();
		}
	}
	
	/**
	 * Sleeeeep
	 * @param millis
	 */
	public static void sleep(long millis) {
		try {
		    Thread.sleep(millis);
		} catch(InterruptedException ex) {
		    Thread.currentThread().interrupt();
		}
	}

}
