package com.grability.test.juancadi.model;

//class-name:     	ITunesEntry
//class-overview: 	Objeto modelo que representara la información asociada a las aplicaciones iTunes
//class-autor:    	Juancadi
//class-date:     	2015-11-13

public class ITunesEntry {

    private int idEntry;
    private String idApp;
    private String name;
    private String imageLink;
    private String imageB64;
    private String summary;
    private String price;
    private String rights;
    private String artist;
    private String appDownloadLink;
    private String category;
    private String releaseDate;


    public ITunesEntry() {


    }

    public ITunesEntry(int idEntry, String idApp, String name, String imageLink, String imageB64, String summary, String price, String rights, String artist, String appDownloadLink, String category, String releaseDate) {
        this.idEntry = idEntry;
        this.idApp = idApp;
        this.name = name;
        this.imageLink = imageLink;
        this.imageB64 = imageB64;
        this.summary = summary;
        this.price = price;
        this.rights = rights;
        this.artist = artist;
        this.appDownloadLink = appDownloadLink;
        this.category = category;
        this.releaseDate = releaseDate;
    }

    public int getIdEntry() {
        return idEntry;
    }

    public String getIdApp() {
        return idApp;
    }

    public String getName() {
        return name;
    }

    public String getImageLink() {
        return imageLink;
    }

    public String getImageB64() {
        return imageB64;
    }

    public String getSummary() {
        return summary;
    }

    public String getPrice() {
        return price;
    }

    public String getRights() {
        return rights;
    }

    public String getArtist() {
        return artist;
    }

    public String getAppDownloadLink() {
        return appDownloadLink;
    }

    public String getCategory() {
        return category;
    }

    public String getReleaseDate() {
        return releaseDate;
    }

    public void setIdEntry(int idEntry) {
        this.idEntry = idEntry;
    }

    public void setIdApp(String idApp) {
        this.idApp = idApp;
    }

    public void setName(String name) {
        this.name = name;
    }

    public void setImageLink(String imageLink) {
        this.imageLink = imageLink;
    }

    public void setImageB64(String imageB64) {
        this.imageB64 = imageB64;
    }

    public void setSummary(String summary) {
        this.summary = summary;
    }

    public void setPrice(String price) {
        this.price = price;
    }

    public void setRights(String rights) {
        this.rights = rights;
    }

    public void setArtist(String artist) {
        this.artist = artist;
    }

    public void setAppDownloadLink(String appDownloadLink) {
        this.appDownloadLink = appDownloadLink;
    }

    public void setCategory(String category) {
        this.category = category;
    }

    public void setReleaseDate(String releaseDate) {
        this.releaseDate = releaseDate;
    }

    @Override
    public String toString() {
        return "ITunesEntry{" +
                "idEntry='" + idEntry + '\'' +
                ", idApp='" + idApp + '\'' +
                ", name='" + name + '\'' +
                ", imageLink='" + imageLink + '\'' +
                ", imageB64='" + imageB64 + '\'' +
                ", summary='" + summary + '\'' +
                ", price='" + price + '\'' +
                ", rights='" + rights + '\'' +
                ", artist='" + artist + '\'' +
                ", appDownloadLink='" + appDownloadLink + '\'' +
                ", category='" + category + '\'' +
                ", releaseDate='" + releaseDate + '\'' +
                '}';
    }
}
