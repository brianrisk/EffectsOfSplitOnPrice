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
import java.util.HashMap;

/**
 * Stock Data downloader class.
 * 
 * @author brianrisk
 *
 */
public class Loader
{

    /**
     * Load stock data from a downloaded CSV
     */
    public static ArrayList<Stock> loadStockData(File databaseFile) {
        HashMap<String, Stock> stocks = new HashMap<>();
        try {
            BufferedReader br = new BufferedReader(new FileReader(databaseFile));
            String line = br.readLine();
            while (line != null) {
                int indexOfFirstComma = line.indexOf(',');
                String stockName = line.substring(0, indexOfFirstComma);
                String stockData = line.substring(indexOfFirstComma + 1);
                Stock stock = stocks.get(stockName);
                if (stock == null) stock = new Stock(stockName);
                StockDay stockDay = new StockDay(stockData);
                stock.addDay(stockDay);
                stocks.put(stockName, stock);
                line = br.readLine();
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
        return new ArrayList<Stock>(stocks.values());
    }



}
