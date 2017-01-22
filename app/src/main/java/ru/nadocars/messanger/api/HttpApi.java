package ru.nadocars.messanger.api;

public interface HttpApi {

    /**
     * API overview http://nadocars.ru/api/v1.0/docs/#LoginRequestExample
     */

    /**
     * Это константы с адресами апи
     */

    String BASE_URL = "https://nadocars.ru/api/v2.0/";
    String USER_GET = "user.get/";
    String LOGIN = "user.login/";
    String GET_USER = "user.get/";
    String LOGOUT = "user.logout/";
    String GET_DIALOGS_LIST = "dialog.list/";
    String GET_DIALOG = "dialog.get/";
    String DIALOG_POST = "dialog.post/";
    String DIALOG_READED = "dialog.readed/";
    String USER_UPDATE = "user.update/";
    String CARS_GET = "cars.get/";
    String UPLOAD_USER_AVATAR = "user.avatar/";
    String UPLOAD_CAR_PHOTO = "cars.uploadphoto/";
    String DELETE_CAR_PHOTO = "cars.deletephoto/";
    String GET_CARS_CALENDAR = "cars.calendar/";
    String ADD_BUSY_DAYS = "cars.addbusy/";
    String UPDATE_CAR_PRICE = "cars.price/";

}
