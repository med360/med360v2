package info.androidhive.loginandregistration.model;

/**
 * Created by priya on 5/2/2017.
 */

public class Filtervalues {

    public static String specvalue="";
    public static String natvalue="";
    public static String genvalue="";

    public String getSpec() {
        return specvalue;
    }
    public String getNat() {
        return natvalue;
    }
    public String getGen() {
        return genvalue;
    }
    public Filtervalues(String fv){
        this.specvalue=fv;

    }
    public void setSpec(String spec) {
        this.specvalue = spec;
    }


    public void setNat(String nat) {
        this.natvalue = nat;
    }

    public void setGen(String gen) {
        this.genvalue = gen;
    }

}
