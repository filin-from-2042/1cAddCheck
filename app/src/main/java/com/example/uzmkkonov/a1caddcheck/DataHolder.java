package com.example.uzmkkonov.a1caddcheck;


import java.util.HashMap;
import java.util.Map;

public class DataHolder {
    private static Map<String, Object> data = new HashMap<String, Object>()  ;
    public static Object getData(String key) {return data.get(key);}
    public static void setData(String key, Object newData) {data.put(key,newData);}
}