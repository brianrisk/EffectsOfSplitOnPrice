import java.util.ArrayList;

/**
 * @author Brian Risk
 */
public class Stock {
	String code;
	ArrayList<StockDay> days = new ArrayList<>();
	
	public Stock(String code) {
		this.code = code;
	}

    public void addDay(StockDay day) {
        days.add(day);
    }


}
