package info.androidhive.loginandregistration.model;

import android.util.Log;

/**
 * Created by Siya on 3/30/2017.
 */

public class Chats {
    private String name, thumbnailUrl;
    private String puid;
   private String duid;
    private String msgid;
    private String mssg;
    private String stime;

    public Chats() {
    }

    public Chats(String thumbnailUrl, String name, String puid, String duid, String mssg, String msgid, String stime) {
        this.puid = puid;
        this.thumbnailUrl = thumbnailUrl;
        this.duid = duid;
        this.name = name;
        this.msgid = msgid;
        this.mssg = mssg;
        this.stime = stime;

    }

    public String getPuid() {
        return puid;
    }



    public void setPuid(String puid) {
        this.puid = puid;
    }

    public String getDuid() {

        Log.e("hp", "inside duid");
        return duid;
    }

    public void setDuid(String duid) {

        Log.e("hp", "inside sethpaddress");

        this.duid = duid;
    }

    public String getMsgid() {
        return msgid;
    }

    public void setMsgid(String msgid) {
        this.msgid = msgid;
    }

    public String getThumbnailUrl() {
        return thumbnailUrl;
    }

    public void setThumbnailUrl(String thumbnailUrl) {
        this.thumbnailUrl = thumbnailUrl;
    }

    public String getMssg() {
        return mssg;
    }

    public void setMssg(String mssg) {
        this.mssg = mssg;
    }


    public String getDname() {
        return name;
    }

    public void setDname(String name) {
        this.name = name;
    }

    public String getStime() {
        return stime;
    }

    public void setStime(String stime) {
        this.stime = stime;
    }



}
