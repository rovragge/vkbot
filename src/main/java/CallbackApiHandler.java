import com.google.gson.JsonObject;
import com.google.gson.JsonParser;
import com.vk.api.sdk.callback.CallbackApi;
import com.vk.api.sdk.client.VkApiClient;
import com.vk.api.sdk.client.actors.GroupActor;
import com.vk.api.sdk.objects.messages.Message;
import dao.*;
import model.*;

import java.util.*;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
import java.util.stream.Collectors;

public class CallbackApiHandler extends CallbackApi {

    private UserDao userDao = new UserDao();
    private TransitionsDao transitionsDao = new TransitionsDao();
    private DialogStateDao dialogStateDao = new DialogStateDao();
    private MessageVKDao messageDao = new MessageVKDao();
    private QuestionDao questionDao = new QuestionDao();
    private SummaryResultDao summaryResultDao = new SummaryResultDao();
    private Boolean d = false;


    private String dontKnowYouMessage = "Я тебя не знаю. Скажи мне \"Начать\"";
    private String gotoBeginingMessage = "Сброс бота в начальное состояние";
    private String wrongPasswordMessage = "Неверный пароль";
    private String wrongWordMessage = "Неизвестный запрос";

    VkApiClient vkclient = null;

    GroupActor actor = null;

    @Override
    public void messageNew(Integer groupId, Message message) {
//        System.out.println(message.toString());
        Integer vkID = message.getFromId();
        User user = userDao.findByVkID(vkID);
        if (user == null ){
            if (message.getBody() != null  && message.getBody().equals("Начать")){
                user = new User();
                user.setVkID(vkID);
                DialogState state = dialogStateDao.findById(1L);
                user.setDialogState(state);
                user.setSecretLength(0);
                user.setQuestions("");
                sendMessage(user.getVkID(),state.getMessage(),null);
                userDao.save(user);
                sayHello(message.getFromId(),user);
            } else {
                sendMessage(vkID,dontKnowYouMessage,null);
            }
        } else {
            DialogState currentState = user.getDialogState();
            if (currentState == null || message.getBody().equals("Начать") ) {
                sendMessage(vkID,gotoBeginingMessage,null);
                DialogState state = dialogStateDao.findById(1L);
                user.setDialogState(state);
                userDao.update(user);
                sayHello(message.getFromId(),user);
                return;
            }

            if (currentState.getId() == 666L) {

                if (user.getCurrentQuestion() != null) {
                    SummaryResult res = new SummaryResult();
                    res.setAnswer(message.getBody());
                    res.setQuestion(user.getCurrentQuestion());
                    res.setUser(user);
                    summaryResultDao.save(res);
                    user.setCurrentQuestion(null);
                    userDao.update(user);
                }

                List<String> res = Arrays.asList(user.getQuestions().split(Pattern.quote("|")));
                res = res.stream().filter(s -> !s.equals("")).collect(Collectors.toList());

                if (res.isEmpty()) {
                    DialogState state = dialogStateDao.findById(1L);
                    user.setDialogState(state);
                    userDao.update(user);
                    sendMessage(message.getFromId(),"Для Вас нет новых вопросов",null);
                    infoAboutState(message.getFromId(), user);
                } else {

                        String id = res.get(0);

                        String newQuestions = res.stream().skip(1).collect(Collectors.joining("|"));
                        user.setQuestions(newQuestions);
                        userDao.update(user);
                        Question question = questionDao.findById(Long.parseLong(id));
                        user.setCurrentQuestion(question);
                        userDao.update(user);
                        List<String> ress = Arrays.asList(question.getAnswers().split(Pattern.quote("|")));

                        String kb = KeyboardFabric.generatePublicKeyBoard(ress);
                        sendMessage(message.getFromId(),question.getQuestion(),kb);
                }
            } else {


                if (user.getSecretLength() == 0) {
                    List<Transitions> transitions = currentState.getTransitions();
                    String msg = message.getBody();
                    Optional<Transitions> transition = transitions.stream()
                            .filter(tr -> {
                                if (tr.isRegex()){
                                    Pattern pattern = Pattern.compile(tr.getMessage());
                                    Matcher matcher = pattern.matcher(msg);
                                    return matcher.matches();
                                }
                                else {
                                    return tr.getMessage().equals(msg);
                                }
                            }).findFirst();

                    if (transition.isPresent()) {
                        DialogState target = transition.get().getTargetDialogState();


                        if (transition.get().isAuth()) {
                            //enter auth mode
                            if (d) sendMessage(message.getFromId(), "secret length == 0 ", null);
                            if (d) sendMessage(message.getFromId(), "enter secret zone for length 4", null);

                            Random random = new Random();
                            JsonObject obj = new JsonObject();
                            Set<Integer> list = new HashSet<>();
                            JsonObject encode = new JsonObject();
                            JsonObject decode = new JsonObject();
                            for (int i = 0; i < 9; i++) {


//                    while (!list.add(random.nextInt())) {}
                                int rnd;
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
                            randomNum = Integer.parseInt(String.valueOf(randomNum).replace('0', (char) (random.nextInt() % 10)));
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
                            if (d) sendMessage(message.getFromId(), "Encode", keyboard);
                            if (d) sendMessage(message.getFromId(), encode.toString(), keyboard);
                            if (d) sendMessage(message.getFromId(), "Decode", keyboard);
                            if (d) sendMessage(message.getFromId(), decode.toString(), keyboard);
                        } else {
                            user.setDialogState(target);
                            userDao.update(user);

                            if (target.getId() == 666L) {
                                messageNew(message.getUserId(), message);
                                return;
                            }

                            infoAboutState(message.getFromId(), user);
                        }

                    } else {
                        errorMessageState(message.getFromId(), user);
                    }
                } else {
                    String strPayload = message.getActionText();
                    JsonParser jsonParser = new JsonParser();
                    String payload = jsonParser.parse(strPayload).getAsJsonObject().get("val").getAsString();

                    String strKeys = user.getSecretKeys();
                    String key = jsonParser.parse(strKeys).getAsJsonObject().getAsJsonObject("decode").get(payload).getAsString();
                    user.setSecret(user.getSecret() + key);
                    if (d) sendMessage(vkID, "ввел " + payload + "|" + key, user.getSecretKeyboard());
                    if (user.getSecretLength() - 1 == 0) {
                        if (user.getSecret().equals(user.getSecretExpected())) {
                            sendMessage(vkID, "Аутентификация успешна", null);
                            user.setDialogState(user.getSecretTargetState());
                            infoAboutState(message.getFromId(), user);
                        } else {
                            if (d) sendMessage(vkID, "Пароль выучи сука", null);
                            if (d) sendMessage(vkID, "Надо было" + user.getSecretExpected(), null);
                            if (d) sendMessage(vkID, "А ты ввел" + user.getSecret(), null);
                            if (d) sendMessage(vkID, "Сиди где сидел", null);
                            sendMessage(vkID, wrongPasswordMessage, null);
                            infoAboutState(message.getFromId(), user);
                        }
                        user.setSecretLength(0);
                    } else {
                        user.setSecretLength(user.getSecretLength() - 1);

                        sendMessage(vkID, "Осталось еще " + String.valueOf(user.getSecretLength()) + " символов", user.getSecretKeyboard());
                    }
                }
                saveMessage(message.getId(), message.getBody(), user, currentState);
            }
            userDao.update(user);
        }
    }

    private void sayHello(int id, User user){
        sendMessage(id, "Привет ты в истоках", KeyboardFabric.generateEmptyKeyBoard());
        try {
                vkclient.messages().send(actor)
                        .message("Вот моя карта")
                        .peerId(id)
                        .userId(id)
                        .unsafeParam("keyboard",KeyboardFabric.generateEmptyKeyBoard())
                        .attachment("photo-173460334_456239017")
                        .execute();
        } catch(Exception ex) {
            String mmm = ex.getLocalizedMessage();
            System.out.println(mmm);
        }
        infoAboutState(id,user);
    }

    private void infoAboutState(int id, User user){
        if (d) sendMessage(id, "Ты как бы тут", null);
        List<Transitions> transitions = user.getDialogState().getTransitions();
        List<String> keys = new ArrayList<>();
        for (Transitions trs:transitions) {
            if (!trs.isRegex())
                keys.add(trs.getMessage());
        }
        String kb = KeyboardFabric.generatePublicKeyBoard(keys);
        DialogState currentState = user.getDialogState();
        String message = currentState.getMessage();
        if (currentState.getId() == 17L){
            String number = messageDao.findLastByUserAndState(user,14L).getContent();
            String summ = messageDao.findLastByUserAndState(user,15L).getContent();
            message = String.format(message,summ,number);
        }
        sendMessage(id, message, kb);
    }

    private void errorMessageState(int id, User user){
        sendMessage(id, wrongWordMessage, null);
        infoAboutState(id,user);
    }

    private void saveMessage(Integer id, String text, User user, DialogState state) {
        MessageVK message = new MessageVK();
        message.setMessageId(id);
        message.setContent(text);
        message.setUser(user);
        message.setState(state);
        messageDao.save(message);
    }

    private void sendMessage(int userId, String message, String kb) {
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
