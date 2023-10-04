import java.io.*;
import java.net.HttpURLConnection;
import java.net.URL;

public class WebPageReader {
    private static final String browser = "Chrome";
    private static final String fileToWrite = "webPage.html"; //should be in the same folder 
    public static void main(String[] args) throws Exception {

        URL oracleURL = new URL("https://www.digitalocean.com/community/tutorials/java-copy-file");
        HttpURLConnection connection = (HttpURLConnection) oracleURL.openConnection();
        connection.setRequestProperty("User-Agent", browser);
        try (BufferedReader reader = new BufferedReader((new InputStreamReader(connection.getInputStream())));
             BufferedWriter writer = new BufferedWriter(new FileWriter(fileToWrite))) {
            String line;
            while ((line = reader.readLine()) != null) {
                writer.write(line);
                writer.newLine();
            }
        }
    }
}

