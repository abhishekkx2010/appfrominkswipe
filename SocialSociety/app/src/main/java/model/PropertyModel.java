package model;

import java.io.Serializable;

/**
 * Created by Ajinkya.Deshpande on 9/17/2016.
 */
public class PropertyModel implements Serializable{

    int idd;

    String id;
    String SocietyId;
    String userId;
    String property_name;
    String property_type;
    String user_type;
    String house_no;
    String is_available_for_rent;

    String societyName;

    public String getSocietyName() {
        return societyName;
    }

    public void setSocietyName(String societyName) {
        this.societyName = societyName;
    }

    public String getCityName() {
        return cityName;
    }

    public void setCityName(String cityName) {
        this.cityName = cityName;
    }

    String cityName;

    String rent_availability_date;

    String property_image;

    String status;
    String created_on;

    private String name;


    private String socimg;
    public int getIdd() {
        return idd;
    }

    public void setIdd(int idd) {
        this.idd = idd;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getSocietyId() {
        return SocietyId;
    }

    public void setSocietyId(String societyId) {
        SocietyId = societyId;
    }

    public String getUserId() {
        return userId;
    }

    public void setUserId(String userId) {
        this.userId = userId;
    }

    public String getProperty_name() {
        return property_name;
    }

    public void setProperty_name(String property_name) {
        this.property_name = property_name;
    }

    public String getProperty_type() {
        return property_type;
    }

    public void setProperty_type(String property_type) {
        this.property_type = property_type;
    }

    public String getUser_type() {
        return user_type;
    }

    public void setUser_type(String user_type) {
        this.user_type = user_type;
    }

    public String getHouse_no() {
        return house_no;
    }

    public void setHouse_no(String house_no) {
        this.house_no = house_no;
    }

    public String getIs_available_for_rent() {
        return is_available_for_rent;
    }

    public void setIs_available_for_rent(String is_available_for_rent) {
        this.is_available_for_rent = is_available_for_rent;
    }

    public String getRent_availability_date() {
        return rent_availability_date;
    }

    public void setRent_availability_date(String rent_availability_date) {
        this.rent_availability_date = rent_availability_date;
    }

    public String getProperty_image() {
        return property_image;
    }

    public void setProperty_image(String property_image) {
        this.property_image = property_image;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public String getCreated_on() {
        return created_on;
    }

    public void setCreated_on(String created_on) {
        this.created_on = created_on;
    }






    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getSocimg() {
        return socimg;
    }

    public void setSocimg(String socimg) {
        this.socimg = socimg;
    }

    public String getId() {
        return id;
    }


}
