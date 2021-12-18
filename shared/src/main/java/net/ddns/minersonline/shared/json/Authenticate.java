package net.ddns.minersonline.shared.json;

public class Authenticate {
    private AuthAgent agent;
    private String username;
    private String password;
    private String clientToken;
    private boolean requestUser;

    public AuthAgent getAgent() {
        return agent;
    }

    public void setAgent(AuthAgent agent) {
        this.agent = agent;
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public String getClientToken() {
        return clientToken;
    }

    public void setClientToken(String clientToken) {
        this.clientToken = clientToken;
    }

    public boolean isRequestUser() {
        return requestUser;
    }

    public void setRequestUser(boolean requestUser) {
        this.requestUser = requestUser;
    }
}
