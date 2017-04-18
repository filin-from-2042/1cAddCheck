package com.example.uzmkkonov.a1caddcheck;

// Класс описывающий остаток на одном конкретном складе
public class StoreRemainUnit
{
    String storeID;
    String storeDESCR;
    String unitCount;

    public StoreRemainUnit(String storeID, String storeDESCR, String unitCount )
    {
        super();
        this.storeID = storeID;
        this.storeDESCR = storeDESCR;
        this.unitCount = unitCount;
    }

    @Override
    public String toString() {
        return this.storeDESCR.toString().trim() + " - " + this.unitCount ;
    }
}