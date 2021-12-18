package net.ddns.minersonline.shared.json;

public class AuthenticateResponse {
    private String accessToken;
    private String clientToken;
    private AuthProfile selectedProfile;
    private AuthProfile[] availableProfiles;

    public String getAccessToken() {
        return accessToken;
    }

    public String getClientToken() {
        return clientToken;
    }

    public AuthProfile getSelectedProfile() {
        return selectedProfile;
    }

    public AuthProfile[] getAvailableProfiles() {
        return availableProfiles;
    }
}
