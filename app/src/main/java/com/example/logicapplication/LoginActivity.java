package com.example.logicapplication;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;


public class LoginActivity extends AppCompatActivity implements View.OnClickListener,AsyncToServer.IServerResponse {
    EditText userName, passWord;
    boolean isUserFound;
    int code=1;
    Command cmd;
    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);
        Button btnSubmit=(Button) findViewById(R.id.loginbutton);
        btnSubmit.setOnClickListener(this);
        userName = (EditText) findViewById(R.id.logIn);
        passWord = (EditText) findViewById(R.id.Password);
    }

    @Override
    public void onClick(View view) {
        JSONObject jsonObj = new JSONObject();
        if (userName.getText().toString().isEmpty() || passWord.getText().toString().isEmpty()) {
            return;
        }
        else
        {
            try {
                jsonObj.put("username",userName.getText().toString());
                jsonObj.put("password",passWord.getText().toString());
            } catch (JSONException e) {
                e.printStackTrace();
            }
            JSONArray jsonArr=new JSONArray();
            jsonArr.put(jsonObj);
            cmd = new Command(this, "get", "http://10.0.2.2:64451/Login/DepartmentLoginApi", jsonArr,code);
            new AsyncToServer().execute(cmd);
        }
    }

    @Override
    public void onServerResponse(Command jsonObj) {
        Intent intent;
        if (jsonObj == null)
            return;
        try {
            JSONArray j=jsonObj.data;
            for(int i=0;i<j.length();i++){
                JSONObject obj=j.getJSONObject(i);
                isUserFound=obj.getBoolean("isok");
            }
            if(isUserFound){
                intent=new Intent(this,MainActivity.class);
                startActivity(intent);
            }else {
                return;
            }
        }
        catch (Exception e) {
            e.printStackTrace();
        }
    }
}
