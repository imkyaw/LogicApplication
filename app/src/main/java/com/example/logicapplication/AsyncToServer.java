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
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;

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
                JSONObject postDataParams = new JSONObject();
                String staffname;
                switch (cmd.CurrentActivity){
                    case 1:
                        String username=jsobj.getString("username");
                        String password=jsobj.getString("password");
                        postDataParams.put("username",username);
                        postDataParams.put("password",password);
                        break;
                    case 2:
                        staffname=jsobj.getString("staffname");
                        postDataParams.put("staffname", staffname);
                        break;
                    case 3:
                        int JsonLocation=jsobj.getInt("locationId");
                        postDataParams.put("locationId", JsonLocation);
                        break;
                    case 4:
                        staffname=jsobj.getString("staffname");
                        String from=jsobj.getString("getStartDate");
                        String to=jsobj.getString("getEndDate");
                        from=parseDateToddMMyyyy(from);
                        to=parseDateToddMMyyyy(to);
                        postDataParams.put("staffname", staffname);
                        postDataParams.put("getStartDate",from);
                        postDataParams.put("getEndDate",to);

                }
                outstream.writeBytes(postDataParams.toString());
                outstream.flush();
                outstream.close();
            }

            // receive response
            InputStream inputStream = new BufferedInputStream(conn.getInputStream());
            BufferedReader r = new BufferedReader(new InputStreamReader(inputStream));
            for (String line; (line = r.readLine()) != null; ) {
                response.append(line).append('\n');
            }

            try {
                if(cmd.context=="set"|| cmd.CurrentActivity==1){
                    JSONObject obj=new JSONObject(response.toString());
                    jsonObj=new JSONArray();
                    jsonObj.put(obj);
                    cmd.data=jsonObj;
                }else{
                    jsonObj = new JSONArray(response.toString());
                    cmd.data=jsonObj;
                }
            } catch (Exception e) {
                e.printStackTrace();
            }
        } catch (Exception e) {
            e.printStackTrace();
        }

        return cmd;
    }
    public String parseDateToddMMyyyy(String time) {
        String inputPattern = "dd-MM-yyyy";
        String outputPattern = "MM/dd/yyyy HH:mm:ss";
        SimpleDateFormat inputFormat = new SimpleDateFormat(inputPattern);
        SimpleDateFormat outputFormat = new SimpleDateFormat(outputPattern);

        Date date = null;
        String str = null;

        try {
            date = inputFormat.parse(time);
            str = outputFormat.format(date);
        } catch (ParseException e) {
            e.printStackTrace();
        }
        return str;
    }
    protected void onPostExecute(Command jsonObj) {
        if (jsonObj != null)
            this.callback.onServerResponse(jsonObj);
    }

    public interface IServerResponse {
        void onServerResponse(Command jsonObj);
    }
}
