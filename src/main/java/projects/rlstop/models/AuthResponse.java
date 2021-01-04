package projects.rlstop.models;

public class AuthResponse {
    private String token;
    private String userName;
    private int userId;
    private boolean isAdmin;

    public AuthResponse(String token, String userName, int userId, boolean isAdmin) {
        this.token = token;
        this.userName = userName;
        this.userId = userId;
        this.isAdmin = isAdmin;
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

    public boolean getIsAdmin(){
        return isAdmin;
    }
}
