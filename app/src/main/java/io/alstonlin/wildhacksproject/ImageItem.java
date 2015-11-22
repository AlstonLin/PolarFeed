package io.alstonlin.wildhacksproject;

import android.graphics.Bitmap;

/**
 * This class will contain the Image and any meta data associated with it.
 */
public class ImageItem {
    private Bitmap image;
    private String url;
    private int eventId;
    private int deviceId;
    private int id;

    public ImageItem(Bitmap image, String url, int eventId, int deviceId) {
        super();
        this.url = url;
        this.image = image;
    }

    public int getId(){
        return id;
    }

    public String getUrl(){
        return url;
    }

    public Bitmap getImage() {
        return image;
    }

    public int getEventId(){
        return eventId;
    }

    public int getDeviceId(){
        return deviceId;
    }

}