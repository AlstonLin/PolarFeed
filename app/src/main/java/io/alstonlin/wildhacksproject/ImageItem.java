package io.alstonlin.wildhacksproject;

import android.graphics.Bitmap;

/**
 * This class will contain the Image and any meta data associated with it.
 */
public class ImageItem {
    public String imageid;
    public String url;
    public Bitmap image;
    public String eventId;
    public String userid;
    public String timestamp;

    public ImageItem(Bitmap image, String url, int eventId, int deviceId) {
        super();
        this.url = url;
        this.image = image;
    }

    public ImageItem() {
    }

    public int getId(){
        return Integer.valueOf(imageid);
    }

    public String getUrl(){
        return url;
    }

    public Bitmap getImage() {
        return image;
    }

    public int getEventId(){
        return Integer.valueOf(eventId);
    }

    public int getDeviceId(){
        return Integer.valueOf(userid);
    }

}