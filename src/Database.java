import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.IOException;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;

/**
 * @author Brian Risk
 */
public class Database
{

    private String code;

    public Database(String code) {
        this.code = code;
    }

    public static ArrayList<Stock> loadData(File databaseFile) {
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

    /**
     * Retrieves latest data from Quandl, if necessary.
     *
     * @return True if there is new data and the database successfully downloaded.
     * False otherwise.
     */
    public boolean refresh() {
        boolean isUpdated = false;

        File dataDir = new File("databases");
        dataDir.mkdir();

        File oldDatabase = new File(dataDir, code + ".csv");
        Date lastModified;
        if (oldDatabase.exists()) {
            lastModified = new Date(oldDatabase.lastModified());
        } else {
            lastModified = new Date(0);
        }
        try {
            String batchString = "https://www.quandl.com/api/v3/databases/" + code + "/data?auth_token=" + Settings.getInstance().token;

            URL url = new URL(batchString);

            // determining if we should perform an update
            Date updateDate = F.getModifiedDateOfUrl(url);

            U.p(updateDate);

            boolean performUpdate = false;
            if (updateDate.after(lastModified)) performUpdate = true;

            if (performUpdate) {

                U.p("downloading: " + code);

                // download and unzip
                File updatedDatabase = F.unzipUrl(url, dataDir);

                // delete old file if exists
                if (updatedDatabase != null) {
                    if (oldDatabase.exists()) oldDatabase.delete();
                    updatedDatabase.renameTo(oldDatabase);
                    isUpdated = true;
                }
            } else {
                U.p(code + " current; no download necessary");
            }

        } catch (MalformedURLException e) {
            e.printStackTrace();
        }
        return isUpdated;
    }

}
