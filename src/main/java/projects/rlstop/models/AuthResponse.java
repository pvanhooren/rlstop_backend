package projects.rlstop.models;

public class AuthResponse {
    private String token;
    private String userName;
    private int userId;
    private String adminCode;

    public AuthResponse(String token, String userName, int userId, String adminCode) {
        this.token = token;
        this.userName = userName;
        this.userId = userId;
        this.adminCode = adminCode;
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

    public String getAdminCode(){
        return adminCode;
    }
}
