import com.vk.api.sdk.client.TransportClient;
import com.vk.api.sdk.client.VkApiClient;
import com.vk.api.sdk.client.actors.GroupActor;
import com.vk.api.sdk.httpclient.HttpTransportClient;
import com.vk.api.sdk.objects.GroupAuthResponse;
import com.vk.api.sdk.objects.groups.responses.GetLongPollServerResponse;
import com.vk.api.sdk.objects.messages.LongpollMessages;
import com.vk.api.sdk.objects.messages.Message;
import com.vk.api.sdk.objects.messages.responses.GetLongPollHistoryResponse;

import java.io.IOException;
import java.util.List;
import java.util.Properties;

public class RunApp {
    public static void main(String[] args) {

        RestClient restClient = new RestClient();

        Properties properties = new Properties();
        try {
            properties.load(RunApp.class.getClassLoader().getResourceAsStream("keys.properties"));
        } catch (Exception e) {
            e.printStackTrace();
        }

        VkApiClient vkclient = new VkApiClient(HttpTransportClient.getInstance());

        int groupId = 173460334;

        GroupActor actor = new GroupActor(groupId, properties.getProperty("group_access_token"));
        GetLongPollServerResponse cont = null;
        try {
            cont = vkclient.groups().getLongPollServer(actor).execute();
        } catch (Exception ex) {
            String msg = ex.getLocalizedMessage();
        }
        String key = cont.getKey();
        String server = cont.getServer();
        int ts = cont.getTs();

        String zap = server+"?act=a_check&key="+key+"&ts="+ts+"&wait=25&mode=8&version=3";

        restClient.req(zap);

        try {
            GetLongPollHistoryResponse resp = vkclient.messages().getLongPollHistory(actor).ts(10).execute();
            LongpollMessages mes = resp.getMessages();
            List<Message> meslist = mes.getMessages();
            Message m = meslist.get(0);
            String body = m.getBody();
        } catch (Exception ex) {
            String s = ex.getLocalizedMessage();
        }

        String kb = "{ \n" +
                "    \"one_time\": false, \n" +
                "    \"buttons\": [ \n" +
                "      [{ \n" +
                "        \"action\": { \n" +
                "          \"type\": \"text\", \n" +
                "          \"payload\": \"{\\\"button\\\": \\\"1\\\"}\", \n" +
                "          \"label\": \"Red\" \n" +
                "        }, \n" +
                "        \"color\": \"negative\" \n" +
                "      }, \n" +
                "     { \n" +
                "        \"action\": { \n" +
                "          \"type\": \"text\", \n" +
                "          \"payload\": \"{\\\"button\\\": \\\"2\\\"}\", \n" +
                "          \"label\": \"Green\" \n" +
                "        }, \n" +
                "        \"color\": \"positive\" \n" +
                "      }], \n" +
                "      [{ \n" +
                "        \"action\": { \n" +
                "          \"type\": \"text\", \n" +
                "          \"payload\": \"{\\\"button\\\": \\\"3\\\"}\", \n" +
                "          \"label\": \"White\" \n" +
                "        }, \n" +
                "        \"color\": \"default\" \n" +
                "      }, \n" +
                "     { \n" +
                "        \"action\": { \n" +
                "          \"type\": \"text\", \n" +
                "          \"payload\": \"{\\\"button\\\": \\\"4\\\"}\", \n" +
                "          \"label\": \"Blue\" \n" +
                "        }, \n" +
                "        \"color\": \"primary\" \n" +
                "      }] \n" +
                "    ] \n" +
                "  } ";


        try {
            vkclient.messages().send(actor)
                    .message("cfcfcfcfcffccfcf")
                    .peerId(25788799)
                    .userId(25788799)
                    .unsafeParam("keyboard",kb)
                    .execute();
        } catch(Exception ex) {

            String mmm = ex.getLocalizedMessage();
        }

        zap=zap;

        try {
            vkclient.messages().send(actor).message("cfcfcfcfcffccfcf").peerId(25788799).userId(25788799).execute();
        }catch(Exception ex) {

        }


    }
    public static void main2() {

        Properties properties = new Properties();
        try {
            properties.load(RunApp.class.getClassLoader().getResourceAsStream("keys.properties"));
        } catch (IOException e) {
            e.printStackTrace();
        }

        int APP_ID = 6740398;
        String CLIENT_SECRET = properties.getProperty("client_secret");


        TransportClient transportClient = HttpTransportClient.getInstance();
        VkApiClient vkClient = new VkApiClient(transportClient);
        GroupAuthResponse authResponse = null;

        GroupActor groupActor = null;
        try {
            authResponse = vkClient.oauth()
                    .groupAuthorizationCodeFlow(APP_ID, CLIENT_SECRET, "google.com", "code")
                    .execute();
        } catch (Exception ex) {
            String msg = ex.getLocalizedMessage();
        }
        int groupId = 173373756;
        GroupActor actor = new GroupActor(groupId, authResponse.getAccessTokens().get(groupId));
        GetLongPollServerResponse cont = null;
        try {
            cont = vkClient.groups().getLongPollServer(groupActor).execute();
        } catch (Exception ex) {
            String msg = ex.getLocalizedMessage();
        }
        String aa = cont.getKey();
    }
}






