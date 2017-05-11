package model;

/**
 * Created by rohit.gaikwad on 9/21/2016.
 */
public class ShareeventModel {
    String image;
    String name;

    public boolean isImageChanged;
    public String getImage() {
        return image;
    }

    public void setImage(String image) {
        this.image = image;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public boolean isImageChanged() {
        return isImageChanged;
    }

    public void setIsImageChanged(boolean isImageChanged) {
        this.isImageChanged = isImageChanged;
    }
}
