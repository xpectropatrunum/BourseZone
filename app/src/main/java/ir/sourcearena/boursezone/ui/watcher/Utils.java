package ir.sourcearena.boursezone.ui.watcher;

public class Utils {


       String name, fullname,state,price,change,percent;

        public Utils(String name, String fullname, String state, String price, String change, String percent) {
            this.name = name;
            this.fullname = fullname;
            this.state = state;
            this.price = price;
            this.change = change;
            this.percent = percent;


        }

        public String getName() {
            return name;
        }

        public void setName(String name) {
            this.name = name;
        }
    public String getFName() {
        return fullname;
    }
    public String getState() {
        return state;
    }
    public String getPrice() {
        return price;
    }
    public String getChange() {
        return change;
    }
    public String getPercent() {
        return percent;
    }

}
