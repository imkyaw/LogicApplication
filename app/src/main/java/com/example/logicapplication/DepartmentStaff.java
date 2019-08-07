package com.example.logicapplication;

import java.util.HashMap;

public class DepartmentStaff {
    public int id;
    public String name;


    public DepartmentStaff(int _id,String _name){
        this.id=_id;
        this.name=_name;
    }

    @Override
    public String toString() {
        return name ;
    }
}
