import com.vk.api.sdk.callback.CallbackApi;
import com.vk.api.sdk.client.VkApiClient;
import com.vk.api.sdk.client.actors.GroupActor;
import com.vk.api.sdk.objects.messages.Message;
import dao.DialogStateDao;
import dao.MessageVKDao;
import dao.TransitionsDao;
import dao.UserDao;
import model.DialogState;
import model.MessageVK;
import model.Transitions;
import model.User;

import javax.transaction.Transactional;
import java.util.List;
import java.util.Optional;

public class CallbackApiHandler extends CallbackApi {


    private UserDao userDao = new UserDao();
    private TransitionsDao transitionsDao = new TransitionsDao();
    private DialogStateDao dialogStateDao = new DialogStateDao();
    private MessageVKDao messageDao = new MessageVKDao();


    VkApiClient vkclient = null;

    GroupActor actor = null;

    @Override
    @Transactional
    public void messageNew(Integer groupId, Message message) {
//        System.out.println(message.toString());
        Integer vkID = message.getFromId();
        User user = userDao.findById(3L);
        if (user == null ){
            if (message.getBody() != null  && message.getBody().equals("Начать")){
                user = new User();
                user.setVkID(vkID);
                DialogState state = dialogStateDao.findById(1L);
                user.setDialogState(state);
                sendMessage(user.getVkID(),state.getMessage(),null);
                userDao.save(user);
                saveMessage(message.getId(),message.getBody(),user);
            } else {
                sendMessage(vkID,"Иди нахуй я тебя не знаю",null);
            }
        } else {

            if (user.getSecretLength() == 0) {
                sendMessage(message.getFromId(),"secret length == 0 ",null);
                sendMessage(message.getFromId(),"enter secret zone for length 4",null);
                sendMessage(message.getFromId(),"enter secret zone for length 4",KeyboardFabric.kb);

            }

            DialogState state  = user.getDialogState();
            List<Transitions> transitions = state.getTransitions();
            String mes = message.getBody();
            Optional<Transitions> transition = transitions.stream()
                    .filter(tr -> tr.getRegexp().equals(mes)).findFirst();

            if (transition.isPresent()) {
                DialogState target = transition.get().getTargetDialogState();
                user.setDialogState(target);
                sendMessage(message.getFromId(),target.getMessage(),null);
            } else {
                sendMessage(message.getFromId(),"Иди нахуй я тебя не понимаю",null);
            }
            saveMessage(message.getId(),message.getBody(),user);
        }
    }


    public void saveMessage(Integer id, String text, User user) {
        MessageVK message = new MessageVK();
        message.setMessageId(id);
        message.setContent(text);
        message.setUser(user);
        messageDao.save(message);
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
