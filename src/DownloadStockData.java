import java.io.BufferedReader;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.FileReader;
import java.io.IOException;
import java.net.URL;
import java.nio.channels.Channels;
import java.nio.channels.ReadableByteChannel;
import java.util.ArrayList;

/**
 * Our downloader class.  
 * Execute this main class to get a fresh components file and stock data.
 * 
 * @author brianrisk
 *
 */
public class DownloadStockData {
	
	static String token = null;
	static File componentsDir;
	static File componentsFile;
	static File stockDir;
	
	static ArrayList<String> quandlCodes;
	
	public static void main(String [] args) {
		// loading the value of our user token
		loadSettings();
		U.p(token);
		
		// setting up data directory structure
		componentsDir = new File("data/components"); 
		componentsDir.mkdirs();
		componentsFile = new File(componentsDir, "components.txt");
		stockDir = new File("data/stocks");
		stockDir.mkdirs();
		
		// downloading the S&P500 list
		downloadComponentsFile();
		quandlCodes = loadQuandlCodes(componentsFile);
		
		// download the stocks for each Quandl code
		for (String code: quandlCodes) {
			U.p("downloading: " + code);
			downloadStock(code);
		}
		
		// "I'm finished!"
		U.p("done");
	}
	
	/**
	 * Downloads a stock csv file given the Quandl code
	 */
	public static void downloadStock(String code) {
		String url = "http://www.quandl.com/api/v1/datasets/EOD/" + code + ".csv?auth_token=" + token;
		File stock = new File(stockDir, code + ".csv");
		downloadFileFromURL(url, stock);
	}
	
	/**
	 * Loads the Quandl codes for the components into an ArrayList
	 * @param componentsFile
	 * @return
	 */
	public static ArrayList<String> loadQuandlCodes(File componentsFile) {
		ArrayList<String> out = new ArrayList<String>();
		
		try {
			BufferedReader br = new BufferedReader(new FileReader(componentsFile));
			String line = br.readLine();
			//skipping the first line, which is the column names
			line = br.readLine();
			while (line != null) {
				
				// Doing some reformatting to work with EOD codes
				String [] chunks = line.split(",");
				int start = chunks[1].indexOf('_') + 1;
				String code = chunks[1].substring(start);
				code = code.replace('.', '_');
				
				out.add(code);
				line = br.readLine();
			}
			br.close();
		} catch (IOException e) {
			e.printStackTrace();
		}
		
		return out;
	}
	
	
	/**
	 * Gets the components file for the S&P500
	 * Other indices and full exchange listings can be found here:
	 * https://www.quandl.com/resources/useful-lists
	 */
	public static void downloadComponentsFile() {
		String url = "https://s3.amazonaws.com/quandl-static-content/Ticker+CSV%27s/Indicies/SP500.csv";
		downloadFileFromURL(url, componentsFile);
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
	 * Loads settings from your settings file.
	 * A valid settings line is a key/value pair spaced with tabs
	 * This method looks for lines with specific keys
	 */
	public static void loadSettings() {
		try {
			BufferedReader br = new BufferedReader(new FileReader("settings.txt"));
			String line = br.readLine();
			while (line != null) {
				if (line.startsWith("token")) {
					String [] chunks = line.split("\t");
					if (chunks.length != 2) {
						U.fail("Improperly formatted settings file.  Must be tab-separated.");
					} else {
						token = chunks[1];
					}
				}
				line = br.readLine();
			}
			br.close();
		} catch (FileNotFoundException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		}
	}

}
