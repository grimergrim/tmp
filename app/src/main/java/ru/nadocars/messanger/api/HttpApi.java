package ru.nadocars.messanger.api;

public interface HttpApi {

    /**
     * API overview http://nadocars.ru/api/v1.0/docs/#LoginRequestExample
     */

    /**
     * Это константы с адресами апи
     */

    String BASE_URL = "http://nadocars.ru/api/v1.0/";
    String LOGIN = "user.login/";
    String GET_USER = "user.get/";
    String LOGOUT = "user.logout/";
    String GET_DIALOGS_LIST = "dialog.list/";
    String GET_DIALOG = "dialog.get/";
    String DIALOG_POST = "dialog.post/";
    String DIALOG_READED = "dialog.readed/";


}
