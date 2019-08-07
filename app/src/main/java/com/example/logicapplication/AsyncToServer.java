package com.example.logicapplication;

import android.os.AsyncTask;

import org.json.JSONArray;
import org.json.JSONObject;

import java.io.BufferedInputStream;
import java.io.BufferedReader;
import java.io.DataOutputStream;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;

public class AsyncToServer extends AsyncTask<Command, Void, Command> {
    IServerResponse callback;
    @Override
    protected Command doInBackground(Command... commands) {
        Command cmd = commands[0];
        this.callback = cmd.callback;

        JSONArray jsonObj = null;
        StringBuilder response = new StringBuilder();
        try {
            URL url = new URL(cmd.endPt);
            HttpURLConnection conn = (HttpURLConnection) url.openConnection();

            // send data
            if (cmd.data != null) {
                conn.setDoOutput(true);
                conn.setRequestMethod("POST");
                conn.setRequestProperty("Content-Type", "application/json; charset=utf-8");
                DataOutputStream outstream = new DataOutputStream(conn.getOutputStream());

                JSONObject jsobj=cmd.data.getJSONObject(0);
                String staffname=jsobj.getString("staffname");

                JSONObject postDataParams = new JSONObject();
                postDataParams.put("staffname", staffname);
                outstream.writeBytes(postDataParams.toString());
                outstream.flush();
                outstream.close();
                //conn.connect();
            }

            // receive response
            InputStream inputStream = new BufferedInputStream(conn.getInputStream());
            BufferedReader r = new BufferedReader(new InputStreamReader(inputStream));
            for (String line; (line = r.readLine()) != null; ) {
                response.append(line).append('\n');
            }

            try {
                jsonObj = new JSONArray(response.toString());
                cmd.data=jsonObj;

            } catch (Exception e) {
                e.printStackTrace();
            }
        } catch (Exception e) {
            e.printStackTrace();
        }

        return cmd;
    }

    protected void onPostExecute(Command jsonObj) {
        if (jsonObj != null)
            this.callback.onServerResponse(jsonObj);
    }

    public interface IServerResponse {
        void onServerResponse(Command jsonObj);
    }
}
