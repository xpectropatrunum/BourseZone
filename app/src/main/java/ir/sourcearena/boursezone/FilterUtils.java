package ir.sourcearena.boursezone;

public class FilterUtils {


       String name, market, industry, f1,  f2,  f3, fn1,  fn2,  fn3;

        public FilterUtils(String name,String market,String industry,String f1, String f2, String f3,String fn1, String fn2, String fn3) {
            this.name = name;
            this.market = market;
            this.industry = industry;
            this.f1 = f1;
            this.f2 = f2;
            this.f3 = f3;
            this.fn1 = fn1;
            this.fn2 = fn2;
            this.fn3 = fn3;

        }

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
    public String getIndustry() {
        return industry;
    }

    public void setIndustry(String industry) {
        this.industry = industry;
    }
    public String getF1() {
        return f1;
    }

    public void setF1(String f1) {
        this.f1 = f1;
    }
    public String getF2() {
        return f2;
    }

    public void setF2(String f2) {
        this.f2 = f2;
    }
    public String getF3() {
        return f3;
    }

    public void setF3(String f3) {
        this.f3 = f3;
    }

    public String getFn1() {
        return fn1;
    }

    public void setFn1(String fn1) {
        this.fn1 = fn1;
    }
    public String getFn2() {
        return fn2;
    }

    public void setFn2(String fn2) {
        this.fn2 = fn2;
    }
    public String getFn3() {
        return fn3;
    }

    public void setFn3(String fn3) {
        this.fn3 = fn3;
    }



}
