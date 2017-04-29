package info.androidhive.loginandregistration.model;

/**
 * Created by Siya on 3/30/2017.
 */

public class Appointment {
    private String date, thumbnailUrl;
    private String ctime;
   private String prefe;
    private String status;
    private String reqid;
    public Appointment() {
    }

    public Appointment(String reqid, String date,String ctime, String prefe,
                       String status,String thumbnailUrl) {
        this.date = date;
        this.thumbnailUrl = thumbnailUrl;
        this.ctime = ctime;
        this.prefe = prefe;
        this.status = status;
        this.reqid=reqid;
    }

    public String getDate() {
        return date;
    }

    public void setDate(String name) {
        this.date = date;
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

    public void setCtime(String year) {
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
