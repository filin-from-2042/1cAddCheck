package com.example.uzmkkonov.a1caddcheck;

import java.text.DecimalFormat;

// Класс описывающий остаток на одном конкретном складе
public class StoreRemainUnit
{
    String storeID;
    String storeDESCR;
    double unitCount;

    public StoreRemainUnit(String storeID, String storeDESCR, double unitCount )
    {
        super();
        this.storeID = storeID;
        this.storeDESCR = storeDESCR;
        this.unitCount = unitCount;
    }

    @Override
    public String toString() {

        DecimalFormat dfCnt = new DecimalFormat("###.##");
        return this.storeDESCR.toString().trim() + " - " + dfCnt.format(this.unitCount) ;
    }
}