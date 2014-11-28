import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Collections;


public class Stock {
	String code;
	ArrayList<StockDay> days;
	
	public Stock(String code) {
		this.code = code;
		days = loadStockDays(code);
	}
	
	
	/**
	 * Given an Quandl code, this finds the appropriately named file
	 * in the data/stocks directory and loads it into an ArrayList of
	 * StockDay objects.  This list is sorted from least recent to
	 * most recent.
	 * @param code
	 * @return
	 */
	public  ArrayList<StockDay> loadStockDays(String code) {
		ArrayList<StockDay> out = new ArrayList<StockDay>();
		File stockFile = new File("data/stocks/" + code + ".csv");
		try {
			BufferedReader br = new BufferedReader(new FileReader(stockFile));
			String line = br.readLine();
			//skipping the first line, which is the column names
			line = br.readLine();
			while (line != null) {
				out.add(new StockDay(line));
				line = br.readLine();
			}
			br.close();
		} catch (IOException e) {
			e.printStackTrace();
		}
		Collections.sort(out);
		return out;
	}

}
