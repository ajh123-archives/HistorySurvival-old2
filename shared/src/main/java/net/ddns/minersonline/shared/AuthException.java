package net.ddns.minersonline.shared;

public class AuthException extends Exception{
    public AuthException(String errorMessage) {
        super(errorMessage);
    }
}
