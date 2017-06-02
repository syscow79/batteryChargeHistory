package hu.syscow.charge.model;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Date;

/**
 * Created by syscow on 2017. 04. 22..
 */

public class PowerLogItem {

    private long id;
    private String plugAction;
    private int level;
    private String plugType;
    private Date date;

    public PowerLogItem() {
    }

    public String getPlugAction() {
        return plugAction;
    }

    public long getId() {
        return id;
    }

    public void setId(long id) {
        this.id = id;
    }

    public void setPlugAction(String plugAction) {
        this.plugAction = plugAction;
    }

    public int getLevel() {
        return level;
    }

    public void setLevel(int level) {
        this.level = level;
    }

    public String getPlugType() {
        return plugType;
    }

    public void setPlugType(String plugType) {
        this.plugType = plugType;
    }

    public Date getDate() {
        return date;
    }

    public void setDate(Date date) {
        this.date = date;
    }

    @Override
    public String toString() {
        DateFormat dateFormat = new SimpleDateFormat("yyyy-mm-dd HH:mm:ss");
        return "(" + id + ")" + dateFormat.format(date) + " : " + plugAction + " : " + level + "% : " + plugType;
    }
}
