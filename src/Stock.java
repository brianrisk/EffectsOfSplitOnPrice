import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Collections;


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
