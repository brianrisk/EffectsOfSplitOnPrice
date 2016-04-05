import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;

/**
 * @author Brian Risk
 */
public class StockDay implements Comparable<StockDay>{
	
	Date date;
	float open;
    float high;
    float low;
    float close;
    float volume;
    float dividend;
    float split;
    float adj_open;
    float adj_high;
    float adj_low;
    float adj_close;
    float adj_volume;
	
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
				
		open = Float.parseFloat(chunks[1]);
		high = Float.parseFloat(chunks[2]);
		low = Float.parseFloat(chunks[3]);
		close = Float.parseFloat(chunks[4]);
		volume = Float.parseFloat(chunks[5]);
        if (chunks[6].isEmpty()) {
            dividend = 0;
        } else {
            dividend = Float.parseFloat(chunks[6]);
        }
		split = Float.parseFloat(chunks[7]);
		adj_open = Float.parseFloat(chunks[8]);
		adj_high = Float.parseFloat(chunks[9]);
		adj_low = Float.parseFloat(chunks[10]);
		adj_close = Float.parseFloat(chunks[11]);
		adj_volume = Float.parseFloat(chunks[12]);	

	}
	
	public String toString() {
		return date + "\t" + adj_close;
	}

	@Override
	public int compareTo(StockDay other) {
		return date.compareTo(other.date);
	}

}
