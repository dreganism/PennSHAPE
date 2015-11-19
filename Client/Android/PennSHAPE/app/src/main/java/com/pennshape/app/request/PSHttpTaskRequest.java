/**
 * Created by hasun on 11/3/15.
 */
package com.pennshape.app.request;

import android.annotation.TargetApi;
import android.os.AsyncTask;
import android.os.Build;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedInputStream;
import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;

public abstract class PSHttpTaskRequest extends AsyncTask<Void, Void, Object> {
    public PSHttpTaskRequestHandler handler;
    protected String error = null;

    public interface PSHttpTaskRequestHandler{
        void onSuccess(PSHttpTaskRequest request, Object result);
        void onFailure(PSHttpTaskRequest request, String error);
    }

    @Override
    protected Object doInBackground(Void... params) {
        Object result = null;
        URL url = null;
        try {
            url = new URL(getUrl());
        } catch (MalformedURLException e) {
            error = "Incorrect URL";
            return result;
        }
        try {
            InputStream inputStream = null;
            HttpURLConnection urlConnection = null;
            urlConnection = (HttpURLConnection) url.openConnection();
            urlConnection.setRequestMethod(getRequestMethod());
            urlConnection.setRequestProperty("Content-Type", "application/json");
            urlConnection.setRequestProperty("Accept", "application/json");
            urlConnection.setDoInput(true);
            setupConnection(urlConnection);
            urlConnection.setReadTimeout(10000);
            urlConnection.setConnectTimeout(15000);
            int statusCode = urlConnection.getResponseCode();
            if (statusCode == 200) {
                try {
                    inputStream = new BufferedInputStream(urlConnection.getInputStream());
                    String response = convertInputStreamToString(inputStream);
                    String json = response.substring(response.indexOf("{"));
                    JSONObject jsonObj = new JSONObject(json);
                    if(validResult(jsonObj)){
                        result = parseResult(jsonObj);
                    }else{
                        error = "Request failing ("+json+")";
                    }
                } catch (JSONException e) {
                    error = "Wrong request result format";
                } catch (IOException e) {
                    error = "Wrong request result";
                }
            } else {
                error = "Network error (" + statusCode + ")";
                result = null;
            }
        }catch (IOException e) {
            error = "Failed to send request";
        }
        return result;
    }

    @Override
    protected void onPostExecute(Object result) {
        if (handler!=null) {
            if (result != null) {
                handler.onSuccess(this, result);
            } else {
                handler.onFailure(this, error);
            }
            handler = null;
        }
    }

    @TargetApi(Build.VERSION_CODES.HONEYCOMB) // API 11
    public void run() {
        if(Build.VERSION.SDK_INT >= Build.VERSION_CODES.HONEYCOMB) {
            executeOnExecutor(AsyncTask.THREAD_POOL_EXECUTOR);
        }else {
            execute();
        }

    }

    private String convertInputStreamToString(InputStream inputStream) throws IOException {
        BufferedReader bufferedReader = new BufferedReader( new InputStreamReader(inputStream));
        String line = "";
        String result = "";
        while((line = bufferedReader.readLine()) != null) result += line;
        inputStream.close();
        return result;
    }

    protected boolean validResult(JSONObject jsonObject) {
        return jsonObject.has("202");
    }

    protected Object parseResult(JSONObject jsonObject) {
        return jsonObject;
    }

    protected String getRequestMethod() {
        return "GET";
    }

    protected String getBaseURL() {
        return "http://54.85.95.11:9000/";
    }

    protected void setupConnection(HttpURLConnection urlConnection) throws IOException{ }

    protected abstract String getUrl();
}
