package model;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import org.apache.commons.io.FileUtils;

import java.io.*;

public class SysSetings {
    public int TS;
    private static String path = "set.txt";
    public static SysSetings getSysSetings() {
        try {
            File file = new File(path);
            FileInputStream fis = new FileInputStream(file);
            byte[] data = new byte[(int) file.length()];
            fis.read(data);
            fis.close();
            String str = new String(data, "UTF-8");
            Gson gson = new Gson();
            return  gson.fromJson(str,SysSetings.class);
        } catch (Exception ex) {
            return null;
        }

    }

    public void saveSettings(){
        try {
        Gson gson = new GsonBuilder()
                .create();
        String json = gson.toJson(this);
        FileUtils.write(new File(path), json);
        } catch (Exception ex) {

        }
    }

}
