package gcu.mpd.mpd_coursework.models;

import android.os.Parcel;
import android.os.Parcelable;

import java.time.LocalDateTime;
import java.util.Date;
//Name: Abel Toth
//Student No:S1828152
public class CurrentRoadwork implements Parcelable {
    private String title;
    private Date startDate;
    private Date endDate;
    private String delayInfo;
    private String link;
    private String georrs;
    private String pubDate;

    public CurrentRoadwork() {
    }

    //constructor
    public CurrentRoadwork(String title, Date startDate, Date endDate, String delayInfo, String link, String georrs, String pubDate) {
        this.title = title;
        this.startDate = startDate;
        this.endDate = endDate;
        this.delayInfo = delayInfo;
        this.link = link;
        this.georrs = georrs;
        this.pubDate = pubDate;
    }


    public CurrentRoadwork(Parcel in) {
        String[] string = new String[5];
        in.readStringArray(string);

        title = string[0];
        delayInfo = string[1];
        link = string[2];
        georrs = string[3];
        pubDate = string[4];
        startDate = (java.util.Date) in.readSerializable();
        endDate = (java.util.Date) in.readSerializable();
    }

    /*public static final Creator<CurrentRoadwork> CREATOR = new Creator<CurrentRoadwork>() {
        @Override
        public CurrentRoadwork createFromParcel(Parcel in) {
            return new CurrentRoadwork(in);
        }

        @Override
        public CurrentRoadwork[] newArray(int size) {
            return new CurrentRoadwork[size];
        }
    };*/
    public static final Parcelable.Creator CREATOR = new Parcelable.Creator() {
        public CurrentRoadwork createFromParcel(Parcel in) {
            return new CurrentRoadwork(in);
        }

        public CurrentRoadwork[] newArray(int size) {
            return new CurrentRoadwork[size];
        }
    };

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

    public String getDelayInfo() {
        return delayInfo;
    }

    public void setDelayInfo(String delayInfo) {
        this.delayInfo = delayInfo;
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

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeStringArray(new String[]{title,delayInfo,link,georrs,pubDate});
        dest.writeSerializable(startDate);
        dest.writeSerializable(endDate);
    }
}
