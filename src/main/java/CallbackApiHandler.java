import com.vk.api.sdk.callback.CallbackApi;
import com.vk.api.sdk.client.VkApiClient;
import com.vk.api.sdk.client.actors.GroupActor;
import com.vk.api.sdk.httpclient.HttpTransportClient;
import com.vk.api.sdk.objects.messages.Message;
import dao.DialogStateDao;
import dao.TransitionsDao;
import dao.UserDao;
import model.DialogState;
import model.Transitions;
import model.User;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

public class CallbackApiHandler extends CallbackApi {


    private UserDao userDao = new UserDao();
    private TransitionsDao transitionsDao = new TransitionsDao();
    private DialogStateDao dialogStateDao = new DialogStateDao();


    VkApiClient vkclient = null;

    GroupActor actor = null;

    @Override
    public void messageNew(Integer groupId, Message message) {
        System.out.println(message.getBody());
        Long vkID = Long.valueOf(message.getFromId());
        User user = userDao.findByVkID(vkID.intValue());
        if (user == null ){
            if (message.getBody() == "Начать"){
                user = new User();
                user.setVkID(vkID.intValue());
                DialogState state = dialogStateDao.findById(1L);
                user.setDialogState(state);
                sendMessage(user.getVkID(),state.getMessage(),null);
            } else {
                sendMessage(message.getUserId(),"Иди нахуй я тебя не знаю",null);
            }
        } else {
            DialogState state  = user.getDialogState();
            List<Transitions> transitions = state.getTransitions();
            String mes = message.getBody();
            Optional<Transitions> transition = transitions.stream()
                    .filter(tr -> tr.getRegexp().equals(mes)).findFirst();

            if (transition.isPresent()) {
                DialogState target = transition.get().getTargetDialogState();
                user.setDialogState(target);
                sendMessage(message.getUserId(),target.getMessage(),null);
            } else {
                sendMessage(message.getUserId(),"Иди нахуй я тебя не понимаю",null);
            }
        }
    }


    public void sendMessage(int userId,String message, String kb) {
            try {

                if (kb != null) {
                    vkclient.messages().send(actor)
                            .message(message)
                            .peerId(userId)
                            .userId(userId)
                            .unsafeParam("keyboard",kb)
                            .execute();
                } else {
                    vkclient.messages().send(actor)
                            .message(message)
                            .peerId(userId)
                            .userId(userId)
                            .execute();
                }

        } catch(Exception ex) {

            String mmm = ex.getLocalizedMessage();
        }
    }


}
