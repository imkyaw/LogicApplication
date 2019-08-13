package com.example.logicapplication;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import org.json.JSONArray;
import org.json.JSONObject;

import java.util.ArrayList;

public class MainActivity extends AppCompatActivity implements View.OnClickListener {
    Button appoint,location,head;
    static final int REQUEST_CODE_LOGIN = 1;
    static final int REQUEST_CODE_APPOINTREP = 2;
    static final int REQUEST_CODE_LOCATION = 3;
    static final int REQUEST_CODE_APPOINTHEAD = 4;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        appoint=(Button) findViewById(R.id.RepBtn);
        appoint.setOnClickListener(this);
        location=(Button) findViewById(R.id.LocationBtn);
        location.setOnClickListener(this);
        head=(Button)findViewById(R.id.HeadBtn);
        head.setOnClickListener(this);
    }

    @Override
    public void onClick(View v) {
//        Command cmd;
//
        int id = v.getId();
        Intent intent;

        switch (v.getId()){
            case R.id.RepBtn:
                intent=new Intent(this,AppointRepActivity.class);
                intent.putExtra("code",REQUEST_CODE_APPOINTREP);
                startActivity(intent);
                break;
            case R.id.LocationBtn:
                intent=new Intent(this,LocationActivity.class);
                intent.putExtra("code",REQUEST_CODE_LOCATION);
                startActivity(intent);
                break;
            case R.id.HeadBtn:
                intent=new Intent(this,AppointHeadActivity.class);
                intent.putExtra("code",REQUEST_CODE_APPOINTHEAD);
                startActivity(intent);
                break;
        }
    }

}
