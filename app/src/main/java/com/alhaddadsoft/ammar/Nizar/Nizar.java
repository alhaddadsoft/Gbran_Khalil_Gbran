package com.alhaddadsoft.ammar.Nizar;

/**
 * Created by veinhorn on 7.3.15.
 */
public class Nizar {
    private String title;
    private String enTitle;
    private int poster;

    public Nizar(String title, String enTitle, int poster) {
        this.title = title;
        this.enTitle = enTitle;
        this.poster = poster;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getTitle() {
        return title;
    }

    public void setEnTitle(String enTitle) {
        this.enTitle = enTitle;
    }

    public String getEnTitle() {
        return enTitle;
    }

    public void setPoster(int poster) {
        this.poster = poster;
    }

    public int getPoster() {
        return poster;
    }
}