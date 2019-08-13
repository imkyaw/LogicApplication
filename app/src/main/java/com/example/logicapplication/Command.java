package com.example.logicapplication;

import org.json.JSONArray;
import org.json.JSONObject;

public class Command {
    protected AsyncToServer.IServerResponse callback;
    protected String context;
    protected String endPt;
    protected JSONArray data;
    protected int CurrentActivity;

    Command(AsyncToServer.IServerResponse callback,
            String context, String endPt, JSONArray data,int currentActivity)
    {
        this.callback = callback;
        this.context = context;
        this.endPt = endPt;
        this.data = data;
        this.CurrentActivity=currentActivity;
    }
}
