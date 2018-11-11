public class KeyboardFabric {

//    class Button {
//        class Action {
//
//        }
//    }
    public static String kb = " { \n" +
        "    \"one_time\": false, \n" +
        "    \"buttons\": [ [\n" +
        "      { \n" +
        "        \"action\": { \n" +
        "          \"type\": \"text\", \n" +
        "          \"payload\": \"{\\\"button\\\": \\\"1\\\"}\", \n" +
        "          \"label\": \"_\" \n" +
        "        }, \n" +
        "        \"color\": \"negative\" \n" +
        "      }\n" +
        "]]\n" +
        "}";

    public static String bk2 = " \n"+
            "{ \n"+
            "    \"one_time\": false, \n"+
            "    \"buttons\": [ [\n"+
            "      { \n"+
            "        \"action\": { \n"+
            "          \"type\": \"text\", \n"+
            "          \"payload\": {\n"+
            "                \"button\": \"1\",\n"+
            "                \"description\":\"butdescr\"}, \n"+
            "          \"label\": \"Red\" \n"+
            "        }, \n"+
            "        \"color\": \"negative\" \n"+
            "      }\n"+
            "]]\n"+
            "}";


}
