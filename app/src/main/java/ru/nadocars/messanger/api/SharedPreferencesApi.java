package ru.nadocars.messanger.api;

public interface SharedPreferencesApi {

    /*
    Names to use as key in shared preferences
     */

    /**
     * Константи, которые используюся как ключи, для служби хранение настроек.
     */

    String TOKEN = "token";
    String LOGIN = "login";
    String PASSWORD = "password";
    String AVATAR = "avatar";
    String AUTO_LOGIN = "is auto login enable";
    String CHECK_NEW_MESSAGES_INTERVAL = "che for new messages interval";

}
