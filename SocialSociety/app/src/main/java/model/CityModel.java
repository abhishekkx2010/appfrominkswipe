package model;

import java.io.Serializable;

/**
 * Created by ajinkya.d on 9/30/2016.
 */
public class CityModel implements Serializable {

    String CityId;
    String CityName;
    String StateId;
    String CountryId;

    public String getCityId() {
        return CityId;
    }

    public void setCityId(String cityId) {
        CityId = cityId;
    }

    public String getCityName() {
        return CityName;
    }

    public void setCityName(String cityName) {
        CityName = cityName;
    }

    public String getStateId() {
        return StateId;
    }

    public void setStateId(String stateId) {
        StateId = stateId;
    }

    public String getCountryId() {
        return CountryId;
    }

    public void setCountryId(String countryId) {
        CountryId = countryId;
    }


}
