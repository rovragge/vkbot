import com.google.gson.JsonObject;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.impl.nio.client.CloseableHttpAsyncClient;
import org.apache.http.impl.nio.client.HttpAsyncClients;
import org.junit.Test;

import java.util.Arrays;
import java.util.List;
import java.util.regex.Pattern;
import java.util.stream.Collectors;

public class EntryPoint {

    @Test
    public void testJson(){
        String url = "https://lp.vk.com/wh173460334?act=a_check&key=f458f27119d98e6d3daa15a83dd68a4a59f2a841&ts=58&wait=25&mode=8&version=3";
        CloseableHttpAsyncClient httpclient = HttpAsyncClients.createDefault();
        httpclient.start();
        HttpGet httpGet = new HttpGet(url);

        AsyncUpdateHandler asyncUpdateHandler = new AsyncUpdateHandler();
        httpclient.execute(httpGet, asyncUpdateHandler);
        while (true) {
            // do nothing and waiting for response
        }
    }

    @Test
    public void hibernateTest(){

    }

    @Test
    public void keyboardTest(){

        JsonObject obj = new JsonObject();
        for(int i=0;i<9;i++){
            obj.addProperty(String.valueOf(i+1),"k"+String.valueOf(i+1));
        }

        String keyboard = KeyboardFabric.generateSecretKeyBoard(obj);
        System.out.println(keyboard);
    }

    @Test
    public void splitTest() {
        String source = "1|2";
        String pattern = Pattern.quote("|");
        List<String> res = Arrays.asList(source.split(pattern));
        res = res.stream().filter(s -> !s.equals("")).collect(Collectors.toList());
        String nq = res.stream().skip(1).collect(Collectors.joining("|"));
    }
}
