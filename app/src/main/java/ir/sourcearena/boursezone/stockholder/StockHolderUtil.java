package ir.sourcearena.boursezone.stockholder;

import android.os.Parcel;
import android.os.Parcelable;


public class StockHolderUtil  {
    private String name, fname,market,rt;


    public StockHolderUtil(String name,String fname, String market,String rt) {
        this.name = name;
        this.fname = fname;
        this.market = market;
        this.rt = rt;
    }





    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }
    public String getRT() {
        return rt;
    }

    public void setRt(String name) {
        this.rt = name;
    }
    public String getMarket() {
        return market;
    }

    public void setMarket(String market) {
        this.market = market;
    }

    public String getFname() {
        return fname;
    }

    public void setFname(String fname) {
        this.fname = fname;
    }



}