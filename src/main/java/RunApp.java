import com.vk.api.sdk.client.VkApiClient;
import com.vk.api.sdk.client.actors.GroupActor;
import com.vk.api.sdk.httpclient.HttpTransportClient;
import com.vk.api.sdk.objects.groups.responses.GetLongPollServerResponse;
import model.SysSetings;
import org.apache.http.client.utils.URIBuilder;

import java.net.URISyntaxException;
import java.util.Properties;

public class RunApp {
    public static void main(String[] args) {
        SysSetings set = SysSetings.getSysSetings();
        int curTS = set.TS;
        CallbackApiHandler handler = new CallbackApiHandler();



        Properties properties = new Properties();
        try {
            properties.load(RunApp.class.getClassLoader().getResourceAsStream("keys.properties"));
        } catch (Exception e) {
            e.printStackTrace();
        }

        final int GROUP_ID = 173460334;
        final String GROUP_TOKEN = properties.getProperty("group_access_token");

        RestClient restClient = new RestClient();

        VkApiClient vkclient = new VkApiClient(HttpTransportClient.getInstance());

        GroupActor actor = new GroupActor(GROUP_ID, GROUP_TOKEN);

        handler.vkclient = vkclient;
        handler.actor = actor;

        while(true){
            try {
                Thread.sleep(500);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
            GetLongPollServerResponse cont = null;
            try {
                cont = vkclient.groups().getLongPollServer(actor).execute();
            } catch (Exception ex) {
                String msg = ex.getLocalizedMessage();
                System.out.println(msg);
            }
            String key = cont.getKey();
            String server = cont.getServer();


            int ts = cont.getTs();


            if (ts > curTS) {
                String uri;
                try {
                    uri = new URIBuilder()
                            .setPath(server)
                            .addParameter("act","a_check")
                            .addParameter("key",key)
                            .addParameter("ts", String.valueOf(curTS))
                            .addParameter("wait", "25")
                            .addParameter("mode","8")
                            .addParameter("version", "3")
                            .build().toString();
                } catch (URISyntaxException e) {
                    uri = null;
                    e.printStackTrace();
                }

                curTS = restClient.req(handler,uri, ts);
                set.TS = ts;
                set.saveSettings();
            }
        }
    }
}


//
//            if (true) {
//                try {
//                    GetLongPollHistoryResponse resp = vkclient.messages().getLongPollHistory(actor).ts(newTs).execute();
//                    LongpollMessages mes = resp.getMessages();
//                    List<Message> meslist = mes.getMessages();
//                    Message m = meslist.get(0);
//                    String body = m.getBody();
//                    System.out.println(body);
//                } catch (Exception ex) {
//                    String s = ex.getLocalizedMessage();
//                }
//            }








//        String kb = "{ \n" +
//                "    \"one_time\": false, \n" +
//                "    \"buttons\": [ \n" +
//                "      [{ \n" +
//                "        \"action\": { \n" +
//                "          \"type\": \"text\", \n" +
//                "          \"payload\": \"{\\\"button\\\": \\\"1\\\"}\", \n" +
//                "          \"label\": \"Red\" \n" +
//                "        }, \n" +
//                "        \"color\": \"negative\" \n" +
//                "      }, \n" +
//                "     { \n" +
//                "        \"action\": { \n" +
//                "          \"type\": \"text\", \n" +
//                "          \"payload\": \"{\\\"button\\\": \\\"2\\\"}\", \n" +
//                "          \"label\": \"Green\" \n" +
//                "        }, \n" +
//                "        \"color\": \"positive\" \n" +
//                "      }], \n" +
//                "      [{ \n" +
//                "        \"action\": { \n" +
//                "          \"type\": \"text\", \n" +
//                "          \"payload\": \"{\\\"button\\\": \\\"3\\\"}\", \n" +
//                "          \"label\": \"White\" \n" +
//                "        }, \n" +
//                "        \"color\": \"default\" \n" +
//                "      }, \n" +
//                "     { \n" +
//                "        \"action\": { \n" +
//                "          \"type\": \"text\", \n" +
//                "          \"payload\": \"{\\\"button\\\": \\\"4\\\"}\", \n" +
//                "          \"label\": \"Blue\" \n" +
//                "        }, \n" +
//                "        \"color\": \"primary\" \n" +
//                "      }] \n" +
//                "    ] \n" +
//                "  } ";


//        try {
//            vkclient.messages().send(actor)
//                    .message("cfcfcfcfcffccfcf")
//                    .peerId(25788799)
//                    .userId(25788799)
//                    .unsafeParam("keyboard",kb)
//                    .execute();
//        } catch(Exception ex) {
//
//            String mmm = ex.getLocalizedMessage();
//        }
//
//
//        try {
//            vkclient.messages().send(actor).message("cfcfcfcfcffccfcf").peerId(25788799).userId(25788799).execute();
//        }catch(Exception ex) {
//
//        }



//    public static void main2() {
//
//        Properties properties = new Properties();
//        try {
//            properties.load(RunApp.class.getClassLoader().getResourceAsStream("keys.properties"));
//        } catch (IOException e) {
//            e.printStackTrace();
//        }
//
//        int APP_ID = 6740398;
//        String CLIENT_SECRET = properties.getProperty("client_secret");
//
//
//        TransportClient transportClient = HttpTransportClient.getInstance();
//        VkApiClient vkClient = new VkApiClient(transportClient);
//        GroupAuthResponse authResponse = null;
//
//        GroupActor groupActor = null;
//        try {
//            authResponse = vkClient.oauth()
//                    .groupAuthorizationCodeFlow(APP_ID, CLIENT_SECRET, "google.com", "code")
//                    .execute();
//        } catch (Exception ex) {
//            String msg = ex.getLocalizedMessage();
//        }
//        int groupId = 173373756;
//        GroupActor actor = new GroupActor(groupId, authResponse.getAccessTokens().get(groupId));
//        GetLongPollServerResponse cont = null;
//        try {
//            cont = vkClient.groups().getLongPollServer(groupActor).execute();
//        } catch (Exception ex) {
//            String msg = ex.getLocalizedMessage();
//        }
//        String aa = cont.getKey();
//    }







