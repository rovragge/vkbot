
import org.apache.http.HttpEntity;
import org.apache.http.client.*;

import org.apache.http.client.methods.CloseableHttpResponse;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.impl.client.*;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.util.EntityUtils;
import org.json.simple.JSONArray;
import org.json.simple.JSONObject;
import org.json.simple.parser.JSONParser;


public class RestClient {






//        private static String url = "http://www.apache.org/";

        public  void req(String url) {
            // Create an instance of HttpClient.
            CloseableHttpClient httpclient = HttpClients.createDefault();
            HttpGet httpGet = new HttpGet(url);
            CloseableHttpResponse response1 = null;
            try{
                response1 = httpclient.execute(httpGet);
            }catch (Exception ex) {}
            try {
                System.out.println(response1.getStatusLine());
                HttpEntity entity1 = response1.getEntity();
                // do something useful with the response body

                // and ensure it is fully consumed
                if (entity1 != null) {
                    String retSrc = EntityUtils.toString(entity1);
                    // parsing JSON
                    JSONObject jsonobj = new JSONObject();
                    JSONParser parser = new JSONParser();
                    JSONObject json = (JSONObject) parser.parse(retSrc);
                    json=json;
                    CallbackApiHandler callbackApiHandler = new CallbackApiHandler();

                    String body = retSrc;
                    JSONArray objects = jsonobj.getString();
                    callbackApiHandler.parse(body);

                }
                EntityUtils.consume(entity1);
            } catch(Exception ex){}
            finally {
                try {
                    response1.close();
                }
                catch (Exception ex){

                }
            }
            req(url);
        }

}
