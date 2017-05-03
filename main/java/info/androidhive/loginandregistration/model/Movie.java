package info.androidhive.loginandregistration.model;

/**
 * Created by Siya on 3/30/2017.
 */

import java.util.ArrayList;

public class Movie {
    private String title, thumbnailUrl;
    private String year;
    private double rating;
    private String genre;
    private String hpadd;
    private String hpname;
    private String did;
    public Movie() {
    }

    public Movie(String did,String name, String thumbnailUrl, String year, double rating,
                 String genre, String hpadd, String hpname) {
        this.title = name;
        this.thumbnailUrl = thumbnailUrl;
        this.year = year;
        this.rating = rating;
        this.genre = genre;
        this.did=did;
        this.hpadd=hpadd;
        this.hpname=hpname;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String name) {
        this.title = name;
    }


    public String getHpadd() {
        return hpadd;
    }

    public void setHpadd(String hpadd) {
        this.hpadd = hpadd;
    }

    public String getHpname() {
        return hpname;
    }

    public void setHpname(String hpname) {
        this.hpname = hpname;
    }




    public String getThumbnailUrl() {
        return thumbnailUrl;
    }

    public void setThumbnailUrl(String thumbnailUrl) {
        this.thumbnailUrl = thumbnailUrl;
    }

    public String getYear() {
        return year;
    }

    public void setYear(String year) {
        this.year = year;
    }

    public String getDid() {
        return did;
    }

    public void setDid(String did) {
        this.did = did;
    }


    public double getRating() {
        return rating;
    }

    public void setRating(double rating) {
        this.rating = rating;
    }

    public String getGenre() {
        return genre;
    }

    public void setGenre(String genre) {
        this.genre = genre;
    }

}
