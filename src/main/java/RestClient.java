import com.google.gson.JsonArray;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.google.gson.JsonParser;
import org.apache.http.client.ResponseHandler;
import org.apache.http.client.config.RequestConfig;
import org.apache.http.client.methods.CloseableHttpResponse;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.impl.client.BasicResponseHandler;
import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.http.impl.client.HttpClients;


public class RestClient {
    CallbackApiHandler callHandler = new CallbackApiHandler();

        public int req(String url, int oldTs) {
            // Create an instance of HttpClient.
            CloseableHttpClient httpclient = HttpClients.createDefault();
            HttpGet httpGet = new HttpGet(url);
            RequestConfig requestConfig = RequestConfig.custom()
                    .setConnectTimeout(30 * 1000)
                    .setConnectionRequestTimeout(30 * 1000)
                    .setSocketTimeout(30 * 1000)
                    .build();
            httpGet.setConfig(requestConfig);

            CloseableHttpResponse response = null;
            try {
                response = httpclient.execute(httpGet);
                System.out.println(response.getStatusLine());
                ResponseHandler<String> handler = new BasicResponseHandler();
                String body = handler.handleResponse(response);

                if (body != null) {
                    // normal json parsing
                    JsonObject parsedJson = new JsonParser().parse(body).getAsJsonObject();
                    Integer ts = parsedJson.get("ts").getAsInt();
                    JsonArray updates = parsedJson.get("updates").getAsJsonArray();
                    for (JsonElement jsonElement : updates){
                        String update = jsonElement.toString();
                        update = update.replace("\"important\":false","\"important\":\"false\"");
                        update = update.replace("\"important\":true","\"important\":\"true\"");
                        Boolean  a = callHandler.parse(update);
                    }
                    return ts;
                }
            } catch(Exception ex){
                ex.printStackTrace();
            }
            return oldTs;
        }

}
