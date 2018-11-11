import com.google.gson.JsonObject;
import dao.DialogStateDao;
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

        DialogStateDao dialogStateDao = new DialogStateDao();
        UserDao userDao = new UserDao();
        User user = new User();
        user.setVkID(123123);
        user.setDialogState(dialogStateDao.findById(1L));
        userDao.save(user);
        //GET USER
        User user1 = userDao.findByVkID(11);
        int bp = 42;
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

}
