package com.grability.test.juancadi.model;

//class-name:     	ITunesFeed
//class-overview: 	Objeto modelo que representara la información asociada al feed obtenido desde iTunes RSS
//class-autor:    	Juancadi
//class-date:     	2015-11-12

public class ITunesFeed {


    private String author;
    private String lastUpdate;
    private String rights;

    public ITunesFeed() {
    }

    public ITunesFeed(String author, String lastUpdate, String rights) {
        this.author = author;
        this.lastUpdate = lastUpdate;
        this.rights = rights;
    }

    public String getAuthor() {
        return author;
    }

    public String getLastUpdate() {
        return lastUpdate;
    }

    public String getRights() {
        return rights;
    }

    public void setAuthor(String author) {
        this.author = author;
    }

    public void setLastUpdate(String lastUpdate) {
        this.lastUpdate = lastUpdate;
    }

    public void setRights(String rights) {
        this.rights = rights;
    }

    @Override
    public String toString() {
        return "ITunesFeed{" +
                "author='" + author + '\'' +
                ", lastUpdate='" + lastUpdate + '\'' +
                ", rights='" + rights + '\'' +
                '}';
    }
}
