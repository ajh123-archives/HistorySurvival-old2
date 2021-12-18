package net.ddns.minersonline.launcher;

import net.ddns.minersonline.shared.json.AuthenticateResponse;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import java.util.UUID;

public class Main {
    public static UUID clientToken;
    public static AuthenticateResponse user;
    public static final Logger LOGGER = LogManager.getLogger(Main.class);
    public static void main(String[] args){
        Main.clientToken = UUID.randomUUID();
        LOGGER.info("Client access token is "+clientToken);
        GUI.main(args);
    }
}
