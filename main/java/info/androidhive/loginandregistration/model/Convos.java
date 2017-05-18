package info.androidhive.loginandregistration.model;

import android.util.Log;

/**
 * Created by Siya on 3/30/2017.
 */

public class Convos {
    private String cnsndname;
    private String cnmssg;

    public Convos() {
    }

    public Convos(String cnsndname, String cnmssg) {
        this.cnsndname = cnsndname;
        this.cnmssg = cnmssg;


    }

    public String getCnsndname() {
        return cnsndname;
    }



    public void setCnsndname(String cnsndname) {
        this.cnsndname = cnsndname;
    }

    public String getCnmssg() {

        Log.e("hp", "inside duid");
        return cnmssg;
    }

    public void setCnmssg(String cnmssg) {

        Log.e("hp", "inside sethpaddress");

        this.cnmssg = cnmssg;
    }




}
