package utils;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import org.springframework.stereotype.Component;

@Component
public class JsonUtils {
    public static String gsonPretty(Object object) {


//        Gson gson = new Gson();
//        Gson gson = new GsonBuilder().disableHtmlEscaping().create();
        Gson gson = new GsonBuilder()
                .setPrettyPrinting()
                .setDateFormat("yyyy-MM-dd HH:mm:ss")
                .disableHtmlEscaping()
                .create();
        gson.htmlSafe();
        String json = gson.toJson(object);
//        System.out.println("Powinien byc JSon: " + json);


        return json;
    }

}
