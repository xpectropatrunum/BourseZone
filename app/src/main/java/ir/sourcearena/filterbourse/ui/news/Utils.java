package ir.sourcearena.filterbourse.ui.news;

import android.graphics.Bitmap;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;

public class Utils extends ViewModel {

    String name , text, pic,pic_a, date;
    Bitmap bmap;
    byte[] b;
    String link;
    public Utils(String name, String text, String pic, String date, Bitmap bmap, String link, byte[] b,String pic_a) {
       this.name = name;
       this.text = text;
       this.pic = pic;
       this.date = date;
       this.bmap = bmap;
        this.link = link;
        this.b = b;
        this.pic_a = pic_a;
    }
    public String getItem(int i){
        switch (i){
            case 0:
                return name;

            case 1:
                return text;

            case 2:
                return pic;

            case 3:
                return date;
            case 4:
                return link;
        }
        return "";

    }
    public String getPic(){
        return  pic_a;
    }
    public byte[] getByte(){
        return  b;
    }
    public Bitmap getBmap(){
        return  bmap;
    }
    public void setBmap(Bitmap bmp){
        this.bmap = bmp;
    }


    public void setByte(byte[] byteArray) {
        this.b = byteArray;
    }
}