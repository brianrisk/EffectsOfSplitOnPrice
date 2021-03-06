import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;

/**
 * Singleton settings loader
 *
 * @author Brian Risk
 */
public class Settings
{
    private static Settings settings;
    public String token;

    protected Settings() {
        loadSettings();
    }

    public static Settings getInstance() {
        if (settings == null) settings = new Settings();
        return settings;
    }

    /**
     * Loads settings from your settings file.
     * A valid settings line is a key/value pair spaced with tabs
     * This method looks for lines with specific keys
     */
    private void loadSettings() {
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
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

}
