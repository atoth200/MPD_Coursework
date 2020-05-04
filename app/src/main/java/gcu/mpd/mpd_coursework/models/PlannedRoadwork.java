package gcu.mpd.mpd_coursework.models;

import java.time.LocalDateTime;
import java.util.Date;
//Name: Abel Toth
//Student No:S1828152
public class PlannedRoadwork {
    private String title;
    private Date startDate;
    private Date endDate;
    private String type;
    private String link;
    private String georrs;
    private String pubDate;

    public PlannedRoadwork() {
    }

    public PlannedRoadwork(String title, Date startDate, Date endDate, String type, String link, String georrs, String pubDate) {
        this.title = title;
        this.startDate = startDate;
        this.endDate = endDate;
        this.type = type;
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

    public Date getStartDate() {
        return startDate;
    }

    public void setStartDate(Date startDate) {
        this.startDate = startDate;
    }

    public Date getEndDate() {
        return endDate;
    }

    public void setEndDate(Date endDate) {
        this.endDate = endDate;
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
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
