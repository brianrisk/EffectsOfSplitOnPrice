import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;


public class StockDay implements Comparable<StockDay>{
	
	Date date;
	double open;
	double high;
	double low;
	double close;
	double volume;
	double dividend;
	double split;
	double adj_open;
	double adj_high;
	double adj_low;
	double adj_close;
	double adj_volume;
	
	/**
	 * values based on content of line taken from EOD csv file
	 * 
	 * Date,Open,High,Low,Close,Volume,Dividend,Split,Adj_Open,Adj_High,Adj_Low,Adj_Close,Adj_Volume
	 * 
	 * @param line
	 */
	public StockDay(String line) {
		String [] chunks = line.split(",");
		
		//get the date from the string in form yyyy-MM-dd
		DateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd");
		try {
			date = dateFormat.parse(chunks[0]);
		} catch (ParseException e) {
			e.printStackTrace();
		}
				
		open = Double.parseDouble(chunks[1]);
		high = Double.parseDouble(chunks[2]);
		low = Double.parseDouble(chunks[3]);
		close = Double.parseDouble(chunks[4]);
		volume = Double.parseDouble(chunks[5]);
		dividend = Double.parseDouble(chunks[6]);
		split = Double.parseDouble(chunks[7]);
		adj_open = Double.parseDouble(chunks[8]);
		adj_high = Double.parseDouble(chunks[9]);
		adj_low = Double.parseDouble(chunks[10]);
		adj_close = Double.parseDouble(chunks[11]);
		adj_volume = Double.parseDouble(chunks[12]);	

	}
	
	public String toString() {
		return date + "\t" + adj_close;
	}

	@Override
	public int compareTo(StockDay other) {
		return date.compareTo(other.date);
	}

}
