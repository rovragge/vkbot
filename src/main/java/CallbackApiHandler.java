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


    private String dontKnowYouMessage = "Давайте сначала поздороваемся :) Скажите мне \"Привет!\"";
    private String gotoBeginingMessage = "Сброс бота в начальное состояние";
    private String wrongPasswordMessage = "Неверный пароль";
    private String wrongWordMessage = "Неизвестный запрос";
    private String helloMessage = "Если что то пойдет не так, меня можно сбросить в начальное состояние, сказав \"Сброс\"";


    VkApiClient vkclient = null;

    GroupActor actor = null;

    @Override
    public void messageNew(Integer groupId, Message message) {
//        System.out.println(message.toString());
        Integer vkID = message.getFromId();
        User user = userDao.findByVkID(vkID);
        if (user == null) {
            if (message.getBody() != null && message.getBody().toLowerCase().equals("привет")) {
                user = new User();
                user.setVkID(vkID);
                DialogState state = dialogStateDao.findById(1L);
                user.setDialogState(state);
                user.setSecretLength(0);
                List<Question> questions = questionDao.findAll();
                String questionString = questions.stream()
                        .map(Question::getId)
                        .map(String::valueOf)
                        .collect(Collectors.joining("|"));
                user.setQuestions(questionString);
                sendMessage(vkID, helloMessage, null);
                infoAboutState(user.getVkID(), user);
                userDao.save(user);
            } else {
                sendMessage(vkID, dontKnowYouMessage, null);
            }
            return;
        }

        DialogState currentState = user.getDialogState();

        if (currentState.getId() == 19L) {
            questionsMode(user, message);
            return;
        }

        if (message.getBody().toLowerCase().equals("cброс")) {
            sendMessage(vkID, gotoBeginingMessage, null);
            DialogState state = dialogStateDao.findById(1L);
            user.setDialogState(state);
            user.setSecretLength(0);
            userDao.update(user);
            infoAboutState(message.getFromId(), user);
            return;
        }

        if (user.getSecretLength() == 0) {
            List<Transitions> transitions = currentState.getTransitions();
            String msg = message.getBody();
            Optional<Transitions> transition = transitions.stream()
                    .filter(tr -> {
                        if (tr.isRegex()) {
                            Pattern pattern = Pattern.compile(tr.getMessage());
                            Matcher matcher = pattern.matcher(msg);
                            return matcher.matches();
                        } else {
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
                    sendMessage(message.getFromId(), "Введите PIN", keyboard);
                    sendMessage(message.getFromId(), "Данный код пришел Вам в смс " + user.getSecretExpected(), keyboard);
                    sendFixedPhoto(user.getVkID(), "173373756_456239020", keyboard);
                    if (d) sendMessage(message.getFromId(), "Encode", keyboard);
                    if (d) sendMessage(message.getFromId(), encode.toString(), keyboard);
                    if (d) sendMessage(message.getFromId(), "Decode", keyboard);
                    if (d) sendMessage(message.getFromId(), decode.toString(), keyboard);
                } else {
                    user.setDialogState(target);
                    userDao.update(user);
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
            if (user.getSecretLength() - 1 == 0) {
                if (user.getSecret().equals(user.getSecretExpected())) {
                    sendMessage(vkID, "Аутентификация успешна", null);
                    user.setDialogState(user.getSecretTargetState());
                    infoAboutState(message.getFromId(), user);
                } else {
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

        userDao.update(user);

    }

    private void questionsMode(User user, Message message) {
        if (message.getBody().equals("Продолжить позже")) {
            DialogState state = dialogStateDao.findById(1L);
            user.setDialogState(state);
            userDao.update(user);
            infoAboutState(message.getFromId(), user);
            return;
        }

        List<String> questionIds = Arrays.asList(user.getQuestions().split(Pattern.quote("|")));
        questionIds = questionIds.stream().filter(s -> !s.equals("")).collect(Collectors.toList());

        if (user.getCurrentQuestion() != null && !message.getBody().equals("Пройти опрос")) {
            SummaryResult summaryResult = new SummaryResult();
            summaryResult.setAnswer(message.getBody());
            summaryResult.setQuestion(user.getCurrentQuestion());
            summaryResult.setUser(user);
            summaryResultDao.save(summaryResult);
            questionIds = questionIds.stream().skip(1).collect(Collectors.toList());
            String newQuestions = questionIds.stream().collect(Collectors.joining("|"));
            user.setQuestions(newQuestions);
            userDao.update(user);
        }

        if (questionIds.isEmpty()) {
            DialogState state = dialogStateDao.findById(1L);
            user.setDialogState(state);
            userDao.update(user);
            sendMessage(message.getFromId(), "Для Вас нет новых вопросов", null);
            infoAboutState(message.getFromId(), user);
        } else {
            String id = questionIds.get(0);
            Question question = questionDao.findById(Long.parseLong(id));
            user.setCurrentQuestion(question);
            userDao.update(user);
            List<String> ress = Arrays.asList(question.getAnswers().split(Pattern.quote("|")));
            ress = new ArrayList<>(ress);
            ress.add("Продолжить позже");
            String kb = KeyboardFabric.generatePublicKeyBoard(ress);
            sendMessage(message.getFromId(), question.getQuestion(), kb);
        }
    }


    private void infoAboutState(int id, User user) {
        if (d) sendMessage(id, "Ты как бы тут", null);
        List<Transitions> transitions = user.getDialogState().getTransitions();
        List<String> keys = new ArrayList<>();
        for (Transitions trs : transitions) {
            if (!trs.isRegex())
                keys.add(trs.getMessage());
        }
        String kb = KeyboardFabric.generatePublicKeyBoard(keys);
        DialogState currentState = user.getDialogState();
        String message = currentState.getMessage();
        if (currentState.getId() == 17L) {
            String number = messageDao.findLastByUserAndState(user, 14L).getContent();
            String summ = messageDao.findLastByUserAndState(user, 15L).getContent();
            message = String.format(message, summ, number);
        }

        if (currentState.getId() == 12L) {
            try {
                Double creditSum = Double.parseDouble(messageDao.findLastByUserAndState(user, 9L).getContent());
                Double startupSum = Double.parseDouble(messageDao.findLastByUserAndState(user, 10L).getContent());
                Integer term = Integer.parseInt(messageDao.findLastByUserAndState(user, 11L).getContent());
                Double percent = 10.8;
                Double monthlyPay = (creditSum - startupSum) * (1 - percent / 100) / term;
                Double salary = monthlyPay * 1.5;
                message = String.format(message, monthlyPay, percent, salary, creditSum);
            }
            catch (Exception ex){
                sendMessage(id, "Не удалось почитать, возможно вы что то ввели неправильно",kb);
                return;
            }
        }

        sendMessage(id, message, kb);
        if (currentState.getId() == 2L) {
            sendFixedPhoto(id, "173373756_456239019", kb);
        }
        if (currentState.getId() == 3L) {
            sendFixedPhoto(id, "173373756_456239018", kb);
        }
        if (currentState.getId() == 18L) {
            sendFixedDoc(id, "25788799_481506692", kb);
        }
    }

    private void errorMessageState(int id, User user) {
        sendMessage(id, wrongWordMessage, null);
        infoAboutState(id, user);
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

            if (kb == null) {
                kb = KeyboardFabric.generateEmptyKeyBoard();
            }

            vkclient.messages().send(actor)
                    .message(message)
                    .peerId(userId)
                    .userId(userId)
                    .unsafeParam("keyboard", kb)
                    .execute();

        } catch (Exception ex) {
            String mmm = ex.getLocalizedMessage();
            System.out.println(mmm);
        }
    }

    private void sendFixedPhoto(int userId, String photoId, String kb) {
        try {
            if (kb == null) {
                kb = KeyboardFabric.generateEmptyKeyBoard();
            }
            vkclient.messages().send(actor)
                    .message(" ")
                    .peerId(userId)
                    .userId(userId)
                    .unsafeParam("keyboard", kb)
                    .attachment("photo-" + photoId)
                    .execute();
        } catch (Exception ex) {
            String mmm = ex.getLocalizedMessage();
            System.out.println(mmm);
        }
    }

    private void sendFixedDoc(int userId, String docId, String kb) {
        try {
            if (kb == null) {
                kb = KeyboardFabric.generateEmptyKeyBoard();
            }
            vkclient.messages().send(actor)
                    .message(" ")
                    .peerId(userId)
                    .userId(userId)
                    .unsafeParam("keyboard", kb)
                    .attachment("doc" + docId)
                    .execute();
        } catch (Exception ex) {
            String mmm = ex.getLocalizedMessage();
            System.out.println(mmm);
        }
    }
}
