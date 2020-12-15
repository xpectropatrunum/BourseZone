package ir.sourcearena.filterbourse.technical.helper;

public class Utils {


       String name;
       boolean active;

        public Utils(String name, boolean active) {
            this.name = name;
            this.active = active;


        }

        public String getName() {
            return name;
        }

        public void setName(String name) {
            this.name = name;
        }
    public boolean getActive() {
        return active;
    }

    public void setActive(boolean active) {
        this.active = active;
    }




}
