package id.co.telkomsigma.githubsearch;

public class UserModel {
    String login;
    String avatar;

    public UserModel(String login, String avatar) {
        this.login = login;
        this.avatar = avatar;
    }

    public String getLogin() {
        return login;
    }

    public void setLogin(String login) {
        this.login = login;
    }

    public String getAvatar() {
        return avatar;
    }

    public void setAvatar(String avatar) {
        this.avatar = avatar;
    }
}
