package ir.sourcearena.filterbourse.searchHelper;


import android.os.Parcel;
import android.os.Parcelable;

public class Namad implements Parcelable {
    private String name, fname,market;


    public Namad(String name,String fname, String market) {
        this.name = name;
        this.fname = fname;
        this.market = market;
    }


    protected Namad(Parcel in) {
        name = in.readString();
        fname = in.readString();
        market = in.readString();
    }

    public static final Creator<Namad> CREATOR = new Creator<Namad>() {
        @Override
        public Namad createFromParcel(Parcel in) {
            return new Namad(in);
        }

        @Override
        public Namad[] newArray(int size) {
            return new Namad[size];
        }
    };

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
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

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel parcel, int i) {
        parcel.writeInt(i);
    }
}