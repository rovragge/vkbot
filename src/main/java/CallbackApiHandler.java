import com.vk.api.sdk.callback.CallbackApi;
import com.vk.api.sdk.objects.messages.Message;
import dao.DialogStateDao;
import dao.TransitionsDao;
import dao.UserDao;

public class CallbackApiHandler extends CallbackApi {

    private UserDao userDao = new UserDao();
    private TransitionsDao transitionsDao = new TransitionsDao();
    DialogStateDao dialogStateDao = new DialogStateDao();

    @Override
    public void messageNew(Integer groupId, Message message) {
        System.out.println(message.getBody());
//        Long vkID = Long.valueOf(message.getUserId());
//        User user = userDao.findById(vkID);
//        if (user == null){
//            user = new User();
//            user.setVkID();
//
//        }
    }
}
