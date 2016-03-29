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
 * Our downloader class.  
 * Execute this main class to get a fresh components file and stock data.
 * 
 * @author brianrisk
 *
 */
public class StockDataLoader
{

    /**
     * Sample EOD CSV line:
     * A,1999-11-18,45.5,50.0,40.0,44.0,44739900.0,0.0,1.0,29.75972820951089,32.70299803242955,26.16239842594364,28.778638268538003,44739900.0
     *
     * @param databaseFile
     * @return
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
        return null;
    }



}
