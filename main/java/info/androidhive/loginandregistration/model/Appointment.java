package info.androidhive.loginandregistration.model;

import android.util.Log;

/**
 * Created by Siya on 3/30/2017.
 */

public class Appointment {
    private String date, thumbnailUrl;
    private String ctime;
   private String prefe;
    private String status;
    private String reqid;
    private String hpaddress;
    private String hospitalname;
    public Appointment() {
    }

    public Appointment(String reqid, String date,String ctime, String prefe,
                       String status,String thumbnailUrl,String hpaddress,String hospitalname) {
        this.date = date;
        this.thumbnailUrl = thumbnailUrl;
        this.ctime = ctime;
        this.prefe = prefe;
        this.status = status;
        this.reqid=reqid;
        this.hpaddress=hpaddress;
        this.hospitalname=hospitalname;
    }

    public String getDate() {
        return date;
    }



    public void setDate(String date) {
        this.date = date;
    }

    public String getHpaddress() {

        Log.e("hp", "inside gethpaddress");
        return hpaddress;
    }

    public void setHpaddress(String hpaddress) {

        Log.e("hp", "inside sethpaddress");

        this.hpaddress = hpaddress;
    }

    public String getHospitalname() {
        return hospitalname;
    }

    public void setHospitalname(String hospitalname) {
        this.hospitalname = hospitalname;
    }

    public String getThumbnailUrl() {
        return thumbnailUrl;
    }

    public void setThumbnailUrl(String thumbnailUrl) {
        this.thumbnailUrl = thumbnailUrl;
    }

    public String getCtime() {
        return ctime;
    }

    public void setCtime(String ctime) {
        this.ctime = ctime;
    }

    public String getPrefe() {
        return prefe;
    }

    public void setPrefe(String prefe) {
        this.prefe = prefe;
    }

    public String getReqid() {
        return reqid;
    }

    public void setReqid(String reqid) {
        this.reqid = reqid;
    }


    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

}
