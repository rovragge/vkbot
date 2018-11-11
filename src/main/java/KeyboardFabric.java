import com.google.gson.JsonArray;
import com.google.gson.JsonObject;
import jdk.nashorn.internal.runtime.JSONFunctions;

public class KeyboardFabric {

       static JsonObject generateButton(int i,String secret) {
           JsonObject button = new JsonObject();

           JsonObject payload = new JsonObject();
           payload.addProperty("val",secret);

           JsonObject action = new JsonObject();
           action.addProperty("type","text");
           action.addProperty("payload",payload.toString());
           action.addProperty("label",String.valueOf(i));
           button.add("action",action);
           button.addProperty("color","negative");
           return button;
       }

       public static String generateSecretKeyBoard(JsonObject keys) {
           JsonObject keyboard = new JsonObject();
           keyboard.addProperty("one_time",false);
           JsonArray cols = new JsonArray();
           for(int i = 0; i<3;i++){
               JsonArray row = new JsonArray();
               for(int j = 0; j<3;j++){
                   row.add(generateButton(i*3+j+1,keys.get(String.valueOf(i*3+j+1)).getAsString()));
               }
               cols.add(row);
           }
           keyboard.add("buttons",cols);
           return keyboard.toString();
       }

//    public static String kb = " { \n" +
//        "    \"one_time\": false, \n" +
//        "    \"buttons\": [ [\n" +
//        "      { \n" +
//        "        \"action\": { \n" +
//        "          \"type\": \"text\", \n" +
//        "          \"payload\": \"{\\\"val\\\": \\\""+secret+"\\\"}\", \n" +
//        "          \"label\": \"_\" \n" +
//        "        }, \n" +
//        "        \"color\": \"negative\" \n" +
//        "      }\n" +
//        "]]\n" +
//        "}";
//
//    public static String bk2 = " \n"+
//            "{ \n"+
//            "    \"one_time\": false, \n"+
//            "    \"buttons\": [ [\n"+
//            "      { \n"+
//            "        \"action\": { \n"+
//            "          \"type\": \"text\", \n"+
//            "          \"payload\": {\n"+
//            "                \"button\": \"1\",\n"+
//            "                \"description\":\"butdescr\"}, \n"+
//            "          \"label\": \"Red\" \n"+
//            "        }, \n"+
//            "        \"color\": \"negative\" \n"+
//            "      }\n"+
//            "]]\n"+
//            "}";


}
