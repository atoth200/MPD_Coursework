package gcu.mpd.mpd_coursework.models;

import java.time.LocalDateTime;
//Name: Abel Toth
//Student No:S1828152
public class CurrentIncident {
    private String title;
    private String description;
    private String link;
    private String georrs;
    private String pubDate;

    public CurrentIncident() {
    }

    //constructor
    public CurrentIncident(String title, String description, String link, String georrs, String pubDate) {
        this.title = title;
        this.description = description;
        this.link = link;
        this.georrs = georrs;
        this.pubDate = pubDate;
    }


    //getters and setters
    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public String getLink() {
        return link;
    }

    public void setLink(String link) {
        this.link = link;
    }

    public String getGeorrs() {
        return georrs;
    }

    public void setGeorrs(String georrs) {
        this.georrs = georrs;
    }

    public String getPubDate() {
        return pubDate;
    }

    public void setPubDate(String pubDate) {
        this.pubDate = pubDate;
    }
}
