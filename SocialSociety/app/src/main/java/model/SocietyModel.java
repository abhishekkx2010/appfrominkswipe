package model;

import java.io.Serializable;

/**
 * Created by Ajinkya on 9/16/2016.
 */
public class SocietyModel implements Serializable {

    int idd;


    private String name;
    String id;
    private String address;
    private String landmark;
    private String state;
    private String city;
    private String pincode;
    private String post;
    private String post_name;
    private String society_image;
    private String user_id;
    private String status;
    private String date_created;
    private String date_modified;
    private String society_image_url;
    private String created_by;

    private String socimg;


    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getAddress() {
        return address;
    }

    public void setAddress(String address) {
        this.address = address;
    }

    public String getLandmark() {
        return landmark;
    }

    public void setLandmark(String landmark) {
        this.landmark = landmark;
    }

    public String getState() {
        return state;
    }

    public void setState(String state) {
        this.state = state;
    }

    public String getCity() {
        return city;
    }

    public void setCity(String city) {
        this.city = city;
    }

    public String getPincode() {
        return pincode;
    }

    public void setPincode(String pincode) {
        this.pincode = pincode;
    }

    public String getPost() {
        return post;
    }

    public void setPost(String post) {
        this.post = post;
    }

    public String getPost_name() {
        return post_name;
    }

    public void setPost_name(String post_name) {
        this.post_name = post_name;
    }

    public String getSociety_image() {
        return society_image;
    }

    public void setSociety_image(String society_image) {
        this.society_image = society_image;
    }

    public String getUser_id() {
        return user_id;
    }

    public void setUser_id(String user_id) {
        this.user_id = user_id;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public String getDate_created() {
        return date_created;
    }

    public void setDate_created(String date_created) {
        this.date_created = date_created;
    }

    public String getDate_modified() {
        return date_modified;
    }

    public void setDate_modified(String date_modified) {
        this.date_modified = date_modified;
    }

    public String getSociety_image_url() {
        return society_image_url;
    }

    public void setSociety_image_url(String society_image_url) {
        this.society_image_url = society_image_url;
    }

    public String getCreated_by() {
        return created_by;
    }

    public void setCreated_by(String created_by) {
        this.created_by = created_by;
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

    public int getIdd() {
        return idd;
    }

    public void setIdd(int id) {
        this.idd = id;
    }
}
