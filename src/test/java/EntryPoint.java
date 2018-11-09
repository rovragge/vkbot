import dao.UserDao;
import model.User;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.impl.nio.client.CloseableHttpAsyncClient;
import org.apache.http.impl.nio.client.HttpAsyncClients;
import org.junit.Test;

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

        UserDao userDao = new UserDao();
        //GET USER
        User user1 = userDao.findByVkID(11);
        int bp = 42;
    }

}
