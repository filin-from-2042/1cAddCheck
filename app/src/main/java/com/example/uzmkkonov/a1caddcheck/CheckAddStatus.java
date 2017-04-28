package com.example.uzmkkonov.a1caddcheck;

import android.util.ArrayMap;

import java.util.HashMap;
import java.util.Map;

public class CheckAddStatus {
    public static String SUCCESS = "Чек успешно добавлен";
    public static String ERROR_1SJOURN = "Не удалось добавить чек в журнал документов";
    public static String ERROR_1SUPDTS = "Не удалось добавить данные в список изменений для УРБД";
    public static String ERROR_1SUIDCTL = "Не удалось установить новый максимальный id для чеков";
    public static String ERROR_DH1473 = "Не удалось добавить реквизиты чека";
    public static String ERROR_1SJOURN_EXIST = "Чек с таким номером документа уже существует";

    public static Map<Integer,String> AddStatus = new HashMap<Integer,String>(){{
        put(0,CheckAddStatus.SUCCESS);
        put(1,CheckAddStatus.ERROR_1SJOURN);
        put(2,CheckAddStatus.ERROR_1SUPDTS);
        put(3,CheckAddStatus.ERROR_1SUIDCTL);
        put(4,CheckAddStatus.ERROR_DH1473);
        put(5,CheckAddStatus.ERROR_1SJOURN_EXIST);
    }};
}
