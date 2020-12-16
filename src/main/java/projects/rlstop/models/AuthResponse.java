package projects.rlstop.models;

public class AuthResponse {
    private String token;
    private String userName;
    private int userId;

    public AuthResponse(String token, String userName, int userId) {
        this.token = token;
        this.userName = userName;
        this.userId = userId;
    }

    public String getToken() {
        return token;
    }

    public String getUserName() {
        return userName;
    }

    public int getUserId() {
        return userId;
    }
}
