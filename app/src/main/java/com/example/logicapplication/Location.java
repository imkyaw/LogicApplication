package com.example.logicapplication;

import java.util.HashMap;

public class Location extends HashMap<String,Object> {
    public int id;
    public String name;
    public String description;

    public Location(int _id,String _description,String _name){
        put("id",_id);
        put("description",_description);
        put("name",_name);
    }
}
