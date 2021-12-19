package net.ddns.minersonline.shared;

import com.google.gson.Gson;
import net.ddns.minersonline.shared.json.*;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.net.*;
import java.nio.charset.StandardCharsets;
import java.util.UUID;
import java.util.stream.Collectors;

public class AuthClient {
    public static String authServer = "http://minersonline.ddns.net/gameauth/";

    public static boolean signOut(String username, String password) throws IOException, AuthException {
        Gson gson = new Gson();

        URL url = new URL(authServer+"signout");
        URLConnection con = url.openConnection();
        HttpURLConnection http = (HttpURLConnection)con;
        http.setRequestMethod("POST"); // PUT is another valid option
        http.setDoOutput(true);

        AuthUser user = new AuthUser();
        user.password = password;
        user.username = username;
        String data = gson.toJson(user);
        byte[] out = data.getBytes(StandardCharsets.UTF_8);
        int length = out.length;

        http.setFixedLengthStreamingMode(length);
        http.setRequestProperty("Content-Type", "application/json; charset=UTF-8");
        http.connect();
        try(OutputStream os = http.getOutputStream()) {
            os.write(out);
        }
        BufferedReader br;
        if (100 <= http.getResponseCode() && http.getResponseCode() <= 399) {
            br = new BufferedReader(new InputStreamReader(http.getInputStream()));
        } else {
            br = new BufferedReader(new InputStreamReader(http.getErrorStream()));
        }
        String response = br.lines().collect(Collectors.joining());
        AuthError error = gson.fromJson(response, AuthError.class);
        if (error.getError() == null) {
            return true;
        } else {
            throw new AuthException(error.getErrorMessage());
        }
    }

    public static boolean validate(TokenPair pair) throws IOException, AuthException {
        Gson gson = new Gson();

        URL url = new URL(authServer+"validate");
        URLConnection con = url.openConnection();
        HttpURLConnection http = (HttpURLConnection)con;
        http.setRequestMethod("POST"); // PUT is another valid option
        http.setDoOutput(true);

        String data = gson.toJson(pair);
        byte[] out = data.getBytes(StandardCharsets.UTF_8);
        int length = out.length;

        http.setFixedLengthStreamingMode(length);
        http.setRequestProperty("Content-Type", "application/json; charset=UTF-8");
        http.connect();
        try(OutputStream os = http.getOutputStream()) {
            os.write(out);
        }
        BufferedReader br;
        if (100 <= http.getResponseCode() && http.getResponseCode() <= 399) {
            br = new BufferedReader(new InputStreamReader(http.getInputStream()));
        } else {
            br = new BufferedReader(new InputStreamReader(http.getErrorStream()));
        }
        String response = br.lines().collect(Collectors.joining());
        AuthError error = gson.fromJson(response, AuthError.class);
        if (error.getError() == null) {
            return true;
        } else {
            throw new AuthException(error.getErrorMessage());
        }
    }

    public static AuthenticateResponse authenticate(String username, String password, UUID clientToken) throws IOException, AuthException {
        Gson gson = new Gson();

        URL url = new URL(authServer+"authenticate");
        URLConnection con = url.openConnection();
        HttpURLConnection http = (HttpURLConnection)con;
        http.setRequestMethod("POST"); // PUT is another valid option
        http.setDoOutput(true);

        AuthAgent agent = new AuthAgent();
        agent.setGame("HistorySurvival");
        agent.setVersion(1);
        Authenticate authenticate = new Authenticate();
        authenticate.agent = agent;
        authenticate.username = username;
        authenticate.password = password;
        authenticate.clientToken = clientToken.toString();
        authenticate.requestUser = true;

        String data = gson.toJson(authenticate);
        byte[] out = data.getBytes(StandardCharsets.UTF_8);
        int length = out.length;

        http.setFixedLengthStreamingMode(length);
        http.setRequestProperty("Content-Type", "application/json; charset=UTF-8");
        http.connect();
        try(OutputStream os = http.getOutputStream()) {
            os.write(out);
        }
        BufferedReader br;
        if (100 <= http.getResponseCode() && http.getResponseCode() <= 399) {
            br = new BufferedReader(new InputStreamReader(http.getInputStream()));
        } else {
            br = new BufferedReader(new InputStreamReader(http.getErrorStream()));
        }
        String response = br.lines().collect(Collectors.joining());
        AuthError error = gson.fromJson(response, AuthError.class);
        if (error.getError() == null) {
            return gson.fromJson(response, AuthenticateResponse.class);
        } else {
            throw new AuthException(error.getErrorMessage());
        }
    }
}
