package model;

import java.io.Serializable;

/**
 * Created by ajinkya.d on 10/6/2016.
 */
public class PropertyType implements Serializable
{

    String id;
    String property_type;
    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getProperty_type() {
        return property_type;
    }

    public void setProperty_type(String property_type) {
        this.property_type = property_type;
    }


}
