package com.inkswipe.SocialSociety;

import java.io.Serializable;
import java.util.ArrayList;


import model.SocietyModel;

/**
 * Created by rohit.gaikwad on 9/3/2016.
 */
public class DataWrapper implements Serializable {

    private ArrayList<SocietyModel> parliaments;

    public DataWrapper(ArrayList<SocietyModel> data) {
        this.parliaments = data;
    }

    public ArrayList<SocietyModel> getSocieties() {
        return this.parliaments;
    }
}