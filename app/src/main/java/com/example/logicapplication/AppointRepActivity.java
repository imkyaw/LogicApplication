package com.example.logicapplication;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.Spinner;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import org.json.JSONArray;
import org.json.JSONObject;

import java.util.ArrayList;

public class AppointRepActivity extends AppCompatActivity implements View.OnClickListener,AsyncToServer.IServerResponse {
    Button btnSet;
    Spinner spinner;
    ArrayList<DepartmentStaff> list;
    Command cmd;
    int code;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_appointrep);
        Intent data = getIntent();
        code = data.getIntExtra("code", 0);
        btnSet=(Button) findViewById(R.id.btnSet);
        btnSet.setOnClickListener(this);
    }

    @Override
    protected void onStart() {
        super.onStart();
        cmd = new Command(this, "get", "http://10.0.2.2:64451/DeptHead/GetStaffList", null,code);
        new AsyncToServer().execute(cmd);
    }

    @Override
    public void onClick(View v) {
//        Command cmd;
//
        int id = v.getId();
        JSONArray array=new JSONArray();
        JSONObject jsonObj = new JSONObject();
        try {
            DepartmentStaff staff=(DepartmentStaff) spinner.getSelectedItem();
            String staffname=staff.name;
            jsonObj.put("staffname", staffname);
            array.put(jsonObj);
        }
        catch (Exception e) {
            e.printStackTrace();
        }
        cmd = new Command(this, "set", "http://10.0.2.2:64451/DeptHead/AppointRep",array,code);
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
        }
        catch (Exception e) {
            e.printStackTrace();
        }
        spinner=this.findViewById(R.id.EmployeeName);
        list=new ArrayList<DepartmentStaff>();
        try {
            String context = (String) jsonObj.context;
            JSONArray j=jsonObj.data;
            if (context.compareTo("get") == 0)
            {
                for(int i=0;i<j.length();i++){
                    JSONObject obj=j.getJSONObject(i);
                    String name=obj.getString("text");
                    int id=obj.getInt("id");
                    list.add(new DepartmentStaff(id,name));
                }
                spinner.setAdapter(new ArrayAdapter<DepartmentStaff>(this, android.R.layout.simple_spinner_dropdown_item, list));
//                System.out.println("id: " + name + ", text: " + age);
                spinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
                    @Override
                    public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l) {
                        String employee=   spinner.getItemAtPosition(spinner.getSelectedItemPosition()).toString();
                        Toast.makeText(getApplicationContext(),employee,Toast.LENGTH_LONG).show();
                    }
                    @Override
                    public void onNothingSelected(AdapterView<?> adapterView) {
                        // DO Nothing here
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
}
