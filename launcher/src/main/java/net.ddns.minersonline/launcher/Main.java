package net.ddns.minersonline.launcher;

import com.google.gson.Gson;
import com.google.gson.internal.Primitives;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Stage;
import net.ddns.minersonline.shared.AuthClient;
import net.ddns.minersonline.shared.AuthException;
import net.ddns.minersonline.shared.TokenPair;
import net.ddns.minersonline.shared.json.AuthUser;
import net.ddns.minersonline.shared.json.AuthenticateResponse;
import net.harawata.appdirs.AppDirs;
import net.harawata.appdirs.AppDirsFactory;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import java.io.*;
import java.lang.reflect.Type;
import java.net.URL;
import java.util.ArrayList;
import java.util.Objects;
import java.util.UUID;
import java.util.stream.Collectors;

public class Main {
    public static UUID clientToken;
    public static AuthenticateResponse user;
    public static final Logger LOGGER = LogManager.getLogger(Main.class);
    public static Stage primaryStage;
    public static final String dataDir = AppDirsFactory.getInstance().getUserDataDir("minersonline/launcher", "", "", true);
    public static void main(String[] args){
        LOGGER.info("User data dir: " + dataDir);
        Main.clientToken = UUID.randomUUID();
        GUI.main(args);
        try {
            if(Main.user != null) {
                AuthClient.signOut(Main.user.user.username, Main.user.user.password);
            }
        } catch (Exception e) {
            Main.LOGGER.trace(e);
        }
    }

    public static void writeFile(String name, String value){
        File directory = new File(dataDir);
        if (! directory.exists()){
            directory.mkdirs();
        }

        File file = new File(dataDir + "/" + name);
        try{
            FileWriter fw = new FileWriter(file.getAbsoluteFile());
            BufferedWriter bw = new BufferedWriter(fw);
            bw.write(value);
            bw.close();
        }
        catch (IOException e){
            e.printStackTrace();
        }
    }

    public static <T> T readJson(String name, Class<T> classOfT) throws IOException{
        File file = new File(dataDir + "/" + name);
        Gson gson = new Gson();
        FileReader fr = new FileReader(file.getAbsoluteFile());
        BufferedReader br = new BufferedReader(fr);
        String data =  br.lines().collect(Collectors.joining());
        Object object = gson.fromJson(data, (Type) classOfT);
        br.close();
        return Primitives.wrap(classOfT).cast(object);
    }

    public static <T> T readJsonFromUrl(String url, Class<T> classOfT) throws IOException {
        Gson gson = new Gson();
        InputStream is = new URL(url).openStream();
        BufferedReader br = new BufferedReader(new InputStreamReader(is));
        String data =  br.lines().collect(Collectors.joining());
        Object object = gson.fromJson(data, (Type) classOfT);
        br.close();
        return Primitives.wrap(classOfT).cast(object);
    }
}
