import java.io.*;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.Date;
import java.util.zip.ZipEntry;
import java.util.zip.ZipInputStream;

/**
 * File functions
 *
 * @author Brian Risk
 */
public class F {

    static final int BUFFER_SIZE = 4096;

    /**
     * unzips the contents of a URL to a directory
     * @return The last file unzipped
     */
    public static File unzipUrl(URL zipFile, File parentDirectory) {
        byte[] buffer = new byte[BUFFER_SIZE];
        File out = null;
        try {
            ZipInputStream stream = new ZipInputStream(new BufferedInputStream(zipFile.openStream()));
            ZipEntry entry = stream.getNextEntry();
            int readLength;
            while (entry != null) {
                File newFile = new File(parentDirectory, entry.getName());
                out = newFile;
                if (!entry.isDirectory()) {
                    FileOutputStream fos = new FileOutputStream(newFile);
                    while ((readLength = stream.read(buffer)) > 0) {
                        fos.write(buffer, 0, readLength);
                    }
                    fos.close();
                } else {
                    newFile.mkdirs();
                }
                stream.closeEntry();
                entry = stream.getNextEntry();
            }
            stream.closeEntry();
            stream.close();
            System.out.println("Done");
        } catch (IOException e) {
            e.printStackTrace();
        }
        return out;
    }


    public static Date getModifiedDateOfUrl(URL url) {
        Date out = null;
        try {
            HttpURLConnection conn = (HttpURLConnection) url.openConnection();
            out = new Date(conn.getLastModified());
            conn.disconnect();
        } catch (Exception e) {
            e.printStackTrace();
        }
        return out;
    }

}
