import com.google.gson.JsonObject;
import com.google.gson.JsonParser;
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
import java.util.*;

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
            if (user.getDialogState() == null) {
                sendMessage(vkID,"Ты ломаешь меня, но я верну тебя к истокам",null);
                DialogState state = dialogStateDao.findById(1L);
                user.setDialogState(state);
                sendMessage(vkID,state.getMessage(),null);
                userDao.save(user);
                return;
            }


            if (user.getSecretLength()==0) {


                DialogState state = user.getDialogState();
                List<Transitions> transitions = state.getTransitions();
                String mes = message.getBody();
                Optional<Transitions> transition = transitions.stream()
                        .filter(tr -> tr.getRegexp().equals(mes)).findFirst();

                if (transition.isPresent()) {
                    DialogState target = transition.get().getTargetDialogState();


                    if (transition.get().getAuth()) {
                        //enter auth mode
                        sendMessage(message.getFromId(), "secret length == 0 ", null);
                        sendMessage(message.getFromId(), "enter secret zone for length 4", null);

                        Random random = new Random();
                        JsonObject obj = new JsonObject();
                        Set<Integer> list = new HashSet<>();
                        JsonObject encode = new JsonObject();
                        JsonObject decode = new JsonObject();
                        for (int i = 0; i < 9; i++) {


//                    while (!list.add(random.nextInt())) {}
                            int rnd = 0;
                            do {
                                rnd = random.nextInt();
                            } while (!list.add(rnd));

                            encode.addProperty(String.valueOf(i + 1), String.valueOf(rnd));
                            decode.addProperty(String.valueOf(rnd), String.valueOf(i + 1));
                        }

                        obj.add("encode", encode);
                        obj.add("decode", decode);
                        int n = 9999 - 1111 + 1;
                        int i = random.nextInt() % n;
                        int randomNum = 1111 + Math.abs(i);
                        String keyboard = KeyboardFabric.generateSecretKeyBoard(obj.get("encode").getAsJsonObject());
                        user.setSecret("");
                        user.setSecretExpected(String.valueOf(randomNum));
                        user.setSecretLength(4);
                        user.setSecretTargetState(target);
                        user.setSecretKeys(obj.toString());
                        user.setSecretKeyboard(keyboard);
//                userDao.update(user);
                        sendMessage(message.getFromId(), "Enter PIN", keyboard);
                        sendMessage(message.getFromId(), "Expecting " + user.getSecretExpected(), keyboard);
                        sendMessage(message.getFromId(), "Encode", keyboard);
                        sendMessage(message.getFromId(), encode.toString(), keyboard);
                        sendMessage(message.getFromId(), "Decode", keyboard);
                        sendMessage(message.getFromId(), decode.toString(), keyboard);
                    } else {
                        user.setDialogState(target);
                        sendMessage(message.getFromId(), target.getMessage(), null);
                    }

                } else {
                    sendMessage(message.getFromId(), "Иди нахуй я тебя не понимаю", null);
                    sendMessage(message.getFromId(), "Ты как бы тут", null);
                    sendMessage(message.getFromId(), user.getDialogState().getMessage(), null);
                }
            } else {
                String strPayload = message.getActionText();
                JsonParser jsonParser = new JsonParser();
                String payload = jsonParser.parse(strPayload).getAsJsonObject().get("val").getAsString();

                String strKeys = user.getSecretKeys();
                String key = jsonParser.parse(strKeys).getAsJsonObject().getAsJsonObject("decode").get(payload).getAsString();
                user.setSecret(user.getSecret()+key);
                sendMessage(vkID,"ввел "+payload+"|"+key,user.getSecretKeyboard());
                if (user.getSecretLength()-1 == 0) {
                    if (user.getSecret().equals(user.getSecretExpected())){
                        sendMessage(vkID,"Велком брат",null);
                        user.setDialogState(user.getSecretTargetState());
                        sendMessage(message.getFromId(), user.getDialogState().getMessage(), null);
                    } else {
                        sendMessage(vkID,"Пароль выучи сука",null);
                        sendMessage(vkID,"Надо было" + user.getSecretExpected(),null);
                        sendMessage(vkID,"А ты ввел" + user.getSecret(),null);
                        sendMessage(vkID,"Сиди где сидел" ,null);
                        sendMessage(message.getFromId(), "Ты как бы тут", null);
                        sendMessage(message.getFromId(), user.getDialogState().getMessage(), null);
                    }
                    user.setSecretLength(0);
                } else {
                    user.setSecretLength(user.getSecretLength()-1);

                    sendMessage(vkID,"Осталось ещзе "+String.valueOf(user.getSecretLength())+" символов",user.getSecretKeyboard());
                }
            }
            saveMessage(message.getId(),message.getBody(),user);
            userDao.update(user);
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
                            .unsafeParam("keyboard",KeyboardFabric.generateEmptyKeyBoard())
                            .execute();
                }

        } catch(Exception ex) {
            String mmm = ex.getLocalizedMessage();
            System.out.println(mmm);
        }
    }


}
