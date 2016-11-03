package Extra;

import android.graphics.Bitmap;

public class ListItem {

    private String name;
    private Bitmap imageId;
    private String date;

    public ListItem(String name, String date, Bitmap imageId) {
        this.name = name;
        this.imageId = imageId;
        this.date = date;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public Bitmap getImageId() {
        return imageId;
    }

    public void setImageId(Bitmap imageId) {
        this.imageId = imageId;
    }

    public String getDate() {
        return date;
    }

    public void setDate(String date) {
        this.date = date;
    }

    @Override
    public String toString() {
        return this.name + " : " + this.date;
    }
}

