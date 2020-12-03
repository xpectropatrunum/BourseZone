package ir.sourcearena.filterbourse.filteryab.simple.adapter;

public class AddSegments {


    int sp1, sp2;
    float val;

    public AddSegments(int sp1, int sp2, float val) {
        this.sp1 = sp1;
        this.sp2 = sp2;
        this.val = val;
    }

    public int getSp1() {
        return sp1;
    }

    public int getSp2() {
        return sp2;
    }

    public float getVal() {
        return val;
    }
    public void setVal(float val) {
        this.val = val;
    }
    public void setSp1(int sp1) {
        this.sp1 = sp1;
    }
    public void setSp2(int sp2) {
        this.sp2 = sp2;
    }


}
