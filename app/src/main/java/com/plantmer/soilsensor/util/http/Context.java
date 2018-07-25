package com.plantmer.soilsensor.util.http;

import java.lang.reflect.Type;
import java.net.HttpURLConnection;

/*
* thanks:
* https://github.com/boldijar/JSON-Rest-API-Client-Java-Android
* */
public class Context {
    /* this class will handle all requests and (de)serializing of the objects */

    public Context(String root) {
        this.root = root;
    }

    /* the root adress of the api */
    private String root;
    private String token;

    public String getRoot() {
        return root;
    }

    public String getToken() {
        return token;
    }

    public void setToken(String token) {
        this.token = token;
    }


    public void setRoot(String root) {
        this.root = root;
    }

    /* for the get requets you can't set a body, for the others you can */
    public <T> T doGetRequest(String path, Class<T> type) {
        try {
            HttpURLConnection urlConnection = HttpHelper
                    .createHttpUrlConnection(root + path, "GET", null, token);
            String response = HttpHelper
                    .readHttpUrlConnectionResponse(urlConnection);
            return HttpHelper.stringToObject(response, type);
        } catch (Exception e) {
            e.printStackTrace();
            return null;
        }
    }
    public <T> T doGetRequest(String path, Type type) {
        try {
            HttpURLConnection urlConnection = HttpHelper
                    .createHttpUrlConnection(root + path, "GET", null, token);
            String response = HttpHelper
                    .readHttpUrlConnectionResponse(urlConnection);
            return HttpHelper.stringToObject(response, type);
        } catch (Exception e) {
            e.printStackTrace();
            return null;
        }
    }

    public <T> T doPostRequest(String path, Object body, Class<T> type) {
        try {
            HttpURLConnection urlConnection = HttpHelper
                    .createHttpUrlConnection(root + path, "POST", body, token);
            String response = HttpHelper
                    .readHttpUrlConnectionResponse(urlConnection);
            return HttpHelper.stringToObject(response, type);
        } catch (Exception e) {
            e.printStackTrace();
            return null;
        }
    }

    public <T> T doDeleteRequest(String path, Object body, Class<T> type) {
        try {
            HttpURLConnection urlConnection = HttpHelper
                    .createHttpUrlConnection(root + path, "DELETE", body, token);
            String response = HttpHelper
                    .readHttpUrlConnectionResponse(urlConnection);
            return HttpHelper.stringToObject(response, type);
        } catch (Exception e) {
            e.printStackTrace();
            return null;
        }
    }

    public <T> T doPutRequest(String path, Object body, Class<T> type) {
        try {
            HttpURLConnection urlConnection = HttpHelper
                    .createHttpUrlConnection(root + path, "PUT", body, token);
            String response = HttpHelper
                    .readHttpUrlConnectionResponse(urlConnection);
            return HttpHelper.stringToObject(response, type);
        } catch (Exception e) {
            e.printStackTrace();
            return null;
        }
    }

}