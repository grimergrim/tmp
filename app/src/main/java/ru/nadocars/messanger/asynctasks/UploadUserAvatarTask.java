package ru.nadocars.messanger.asynctasks;

import android.content.Context;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.AsyncTask;
import android.support.v7.app.AppCompatActivity;

import java.io.File;
import java.io.IOException;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.List;

import ru.nadocars.messanger.api.HttpApi;
import ru.nadocars.messanger.ui.navigation.Navigator;
import ru.nadocars.messanger.ui.navigation.NavigatorImpl;
import ru.nadocars.messanger.utils.MultipartUtility;

public class UploadUserAvatarTask extends AsyncTask<Void, Void, Boolean> {

    private final String token;
    private final String url;
    private final Context context;
    private final Navigator navigator;
    private final ConnectivityManager connectivityManager;
    private boolean isInternetConnected;
    private String requestResultMessage;
    private AppCompatActivity appCompatActivity;

    public UploadUserAvatarTask(String token, String url, Context context, AppCompatActivity appCompatActivity) {
        this.token = token;
        this.url = url;
        this.context = context;
        this.appCompatActivity = appCompatActivity;
        navigator = new NavigatorImpl();
        isInternetConnected = false;
        connectivityManager = (ConnectivityManager) this.context.getSystemService(Context.CONNECTIVITY_SERVICE);
    }

    @Override
    protected Boolean doInBackground(Void... params) {
        boolean isSuccessful = false;
        NetworkInfo[] networkInfo = connectivityManager.getAllNetworkInfo();
        for (NetworkInfo aNetworkInfo : networkInfo) {
            if (aNetworkInfo.getState() == NetworkInfo.State.CONNECTED) {
                isInternetConnected = true;
                URL uploadAvatarUrl = null;
                try {
                    uploadAvatarUrl = new URL(HttpApi.BASE_URL + HttpApi.UPLOAD_USER_AVATAR);
                } catch (MalformedURLException e) {
                    e.printStackTrace();
                }
                if (null != uploadAvatarUrl) {
                    HttpURLConnection httpURLConnection = null;
                    try {

                        MultipartUtility multipartUtility = new MultipartUtility(HttpApi.BASE_URL + HttpApi.UPLOAD_USER_AVATAR, "utf-8");
                        multipartUtility.addFormField("access_token", token);
                        multipartUtility.addFilePart("avatar1991191182237", new File(url));
                        List<String> stringList = multipartUtility.finish();


//                        String urlParameters = "access_token=" + token;
//                        byte[] postData = urlParameters.getBytes("UTF-8");
//                        int postDataLength = postData.length;
//
//                        httpURLConnection = (HttpURLConnection) uploadAvatarUrl.openConnection();
//                        httpURLConnection.setRequestMethod("POST");
//                        httpURLConnection.setDoInput(true);
//                        httpURLConnection.setDoOutput(true);
//                        httpURLConnection.setInstanceFollowRedirects(false);
//                        httpURLConnection.setRequestProperty("Content-Type", "multipart/form-data");
//                        httpURLConnection.setRequestProperty("charset", "utf-8");
//                        httpURLConnection.setRequestProperty("Content-Length", Integer.toString(postDataLength));
//                        httpURLConnection.setUseCaches(false);
//
//                        DataOutputStream dataOutputStream = new DataOutputStream(httpURLConnection.getOutputStream());
//                        dataOutputStream.write(postData);
//                        dataOutputStream.flush();
//                        dataOutputStream.close();
//
//                        InputStream inputStream = httpURLConnection.getInputStream();
//                        BufferedReader reader = new BufferedReader(new InputStreamReader(inputStream, "UTF-8"));
//                        String requestResult;
//                        StringBuilder stringBuffer = new StringBuilder();
//                        String data;
//                        while ((data = reader.readLine()) != null) {
//                            stringBuffer.append(data);
//                        }
//                        requestResult = stringBuffer.toString();
//                        reader.close();
//
//                        httpURLConnection.connect();
//                        int status = httpURLConnection.getResponseCode();
//                        if (status == 200) {
//                            if (requestResult.contains("response")) {
//                                JSONObject jsonObject = new JSONObject(requestResult);
//                                JSONObject jsonObjectResponse = jsonObject.getJSONObject("response");
//                                String accessToken = jsonObjectResponse.getString("access_token");
//
//                                isSuccessful = true;
//                            } else if (requestResult.contains("error")) {
//                                requestResultMessage = context.getResources().getString(R.string.error_wrong_login_or_password);
//                            }
//                        } else {
//                            requestResultMessage = context.getResources().getString(R.string.error_server_error);
//                        }
                    } catch (IOException  e) {
                        e.printStackTrace();
                    } finally {
                        if (httpURLConnection != null) {
                            httpURLConnection.disconnect();
                        }
                    }
                }
            }
        }
        return isSuccessful;
    }

    //если успешно - переход к контактам, иначе показываем ошибки
    @Override
    protected void onPostExecute(final Boolean success) {
        System.out.println();
//        LoginPresenter loginPresenter = LoginPresenterImpl.getLoginPresenter();
//        loginPresenter.setAuthTask(null);
//        loginPresenter.showProgress(false);
//        if (!isInternetConnected) {
//            Toast.makeText(context, context.getResources().getString(R.string.error_no_internet), Toast.LENGTH_SHORT).show();
//        } else if (success) {
//            navigator.navigateToContacts(context);
//            appCompatActivity.finish();
//        } else {
//            loginPresenter.showError(requestResultMessage);
//        }

    }

}