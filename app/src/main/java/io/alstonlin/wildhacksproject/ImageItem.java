package io.alstonlin.wildhacksproject;

import android.graphics.Bitmap;

/**
 * This class will contain the Image and any meta data associated with it.
 */
public class ImageItem {
    private Bitmap image;
    private int eventId;
    private int deviceId;
    private int id;

    public ImageItem(Bitmap image, int eventId, int deviceId) {
        super();
        this.image = image;
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