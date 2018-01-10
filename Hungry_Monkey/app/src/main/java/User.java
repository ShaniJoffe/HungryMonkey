/**
 * Created by Shani Joffe on 1/10/2018.
 */

public class User {
    private int    _id;
    private String _username;
    private String _name;
    private String _last_name;
    private String _password;
    private String _email;
    public User()
    {

    }

    public User(String _name, String _last_name, String _username, String _password, String _email) {
        this._username=_username;
        this._name=_name;
        this._last_name=_last_name;
        this._password=_password;
        this._email=_email;
    }


}
