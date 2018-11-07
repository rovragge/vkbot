
import org.apache.http.HttpEntity;

import org.apache.http.client.methods.CloseableHttpResponse;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.impl.client.*;
import org.apache.http.util.EntityUtils;
import org.json.simple.JSONArray;
import org.json.simple.JSONObject;
import org.json.simple.parser.JSONParser;


public class RestClient {
//        private static String url = "http://www.apache.org/";
        public int req(String url) {
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

                    JSONParser parser = new JSONParser();
                    JSONObject json = (JSONObject) parser.parse(retSrc);
                    json=json;
                    CallbackApiHandler callbackApiHandler = new CallbackApiHandler();


                    JSONArray objects = (JSONArray) json.get("updates");
                    String ts = (String) json.get("ts");
                    int tts = Integer.parseInt(ts);
                    return tts;
//                    for(int i=0; i<objects.size(); i++)
//                    {
//                        JSONObject o = (JSONObject) objects.get(i);
//                        String ss = o.toString();
//                        callbackApiHandler.parse(ss);
//                    }

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
            return 0;
        }

}
