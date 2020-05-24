import javax.ws.rs.GET;
import javax.ws.rs.Produces;
import javax.ws.rs.core.MediaType;
import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.concurrent.Callable;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class PriceGetter implements Callable<Float> {
    private int id;
    private URL url;

    public PriceGetter(int id, String url) {
        this.id = id;
        try {
            this.url = new URL(url);
        } catch (Exception ex) {
            ex.printStackTrace();
        }

    }

    public Float call() {
        return getPrice();
    }

    @GET
    @Produces(MediaType.APPLICATION_JSON)
    public float getPrice() {
        try {

            HttpURLConnection conn = (HttpURLConnection) url.openConnection();
            conn.setRequestMethod("GET");
            conn.setRequestProperty("Accept", "");

            if (conn.getResponseCode() != 200) {
                throw new RuntimeException("Failed : HTTP error code : "
                        + conn.getResponseCode());
            }

            BufferedReader br = new BufferedReader(new InputStreamReader((conn.getInputStream())));

            Pattern compiledPattern = Pattern.compile("\"price\":");
            String output;
            while ((output = br.readLine()) != null) {
                Matcher matcher = compiledPattern.matcher(output);
                if (matcher.find()) {
                    int i = 14;
                    for (; i < output.length(); i++) {
                        if (output.charAt(i) == '"') {
                            break;
                        }
                    }
                    return Float.valueOf(output.substring(14, i));
                }
            }

            conn.disconnect();

        } catch (MalformedURLException e) {

            e.printStackTrace();

        } catch (IOException e) {

            e.printStackTrace();

        }
        return -1;
    }
}
