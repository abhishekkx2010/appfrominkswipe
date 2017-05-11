package model;

import java.io.Serializable;

/**
 * Created by ajinkya.d on 9/30/2016.
 */
public class StateModel implements Serializable {

    String stateid;
    String statename;
    String countryid;
    public String getCountryid() {
        return countryid;
    }

    public void setCountryid(String countryid) {
        this.countryid = countryid;
    }



    public String getStateid() {
        return stateid;
    }

    public void setStateid(String stateid) {
        this.stateid = stateid;
    }

    public String getStatename() {
        return statename;
    }

    public void setStatename(String statename) {
        this.statename = statename;
    }
}
