package com.example.megas.todolist2;

import android.os.AsyncTask;

import java.io.BufferedInputStream;
import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.net.URLConnection;
import java.util.Scanner;

public class HttpRequest2 extends AsyncTask<String, Integer, String> {
    Action action;

    public HttpRequest2(Action action) {
        this.action = action;
    }

    @Override
    protected String doInBackground(String... strings) {
        String result = "";
        // Build and set timeout values for the request.
        URLConnection connection = null;

        try {
            connection = (new URL(strings[0])).openConnection();
            connection.setConnectTimeout(5000);
            connection.setReadTimeout(5000);
            connection.connect();

            InputStream in = connection.getInputStream();
            BufferedReader reader = new BufferedReader(new InputStreamReader(in));
            StringBuilder html = new StringBuilder();
            for (String line; (line = reader.readLine()) != null; ) {
                html.append(line);
            }
            in.close();
            result = html.toString();
        } catch (IOException e) {
            e.printStackTrace();
        }

        return result;
    }

    @Override
    protected void onPostExecute(String s) {
        action.action(s);
    }

    public interface Action {
        public void action(String s);
    }
}
