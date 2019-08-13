package com.example.logicapplication;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ListView;
import android.widget.SimpleAdapter;
import android.widget.Toast;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

public class LocationActivity extends AppCompatActivity implements AsyncToServer.IServerResponse, View.OnClickListener {

    Integer selectedId;
    ArrayList<Location> list;
    Command cmd;
    Button btnSet;
    ListView listView;
    int code;
    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_location);
        Intent data = getIntent();
        code = data.getIntExtra("code", 0);

        btnSet=(Button) findViewById(R.id.btnsubmit);
        btnSet.setOnClickListener(this);

    }

    @Override
    protected void onStart() {
        super.onStart();
        cmd = new Command(this, "get", "http://10.0.2.2:64451/Request/GetLocationApi", null,code);
        new AsyncToServer().execute(cmd);
    }

    @Override
    public void onServerResponse(Command jsonObj) {
        if (jsonObj == null)
            return;
        try {
            if(jsonObj.data.getJSONObject(0).getString("isok")!=null){
                Toast.makeText(getApplicationContext(),jsonObj.data.getJSONObject(0).getString("isok"),Toast.LENGTH_LONG).show();
                Intent intent=new Intent(this,MainActivity.class);
                startActivity(intent);
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        list=new ArrayList<Location>();

        listView=findViewById(R.id.LocationList);
        try {
            String context = (String) jsonObj.context;
            JSONArray j=jsonObj.data;
            if (context.compareTo("get") == 0)
            {
                for(int i=0;i<j.length();i++){
                    JSONObject obj=j.getJSONObject(i);
                    String name=obj.getString("name");
                    int id=obj.getInt("id");
                    String description=obj.getString("text");
                    list.add(new Location(id,name,description));
                }
                listView.setChoiceMode(listView.CHOICE_MODE_SINGLE);
                listView.setAdapter(new SimpleAdapter(this,list,R.layout.location_row,
                        new String[] { "description","name"},
                        new int[] { R.id.LocDes, R.id.LocName}));

                listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {

                    @Override
                    public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
                        Location item = (Location) adapterView.getItemAtPosition(i);
                        Toast.makeText(getApplicationContext(),item.get("name").toString(),Toast.LENGTH_LONG).show();
                        selectedId=(Integer) item.get("id");

                    }
                });

            }else if(context.compareTo("set")==0){
                String status = (String)jsonObj.data.getJSONObject(0).get("text");
                System.out.println("status:" + status);
            }
        }
        catch (Exception e) {
            e.printStackTrace();
        }
    }

    @Override
    public void onClick(View view) {
        JSONArray array=new JSONArray();
        JSONObject jsonObj = new JSONObject();
        try {
            Integer id=selectedId;
            jsonObj.put("locationId", id);
            array.put(jsonObj);
        }
        catch (Exception e) {
            e.printStackTrace();
        }
        cmd = new Command(this, "set", "http://10.0.2.2:64451/Request/GetLocationAsync",array,code);
        new AsyncToServer().execute(cmd);
    }
}
