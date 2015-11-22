package io.alstonlin.wildhacksproject;

import java.util.ArrayList;


/**
 * This class handles the communication between the Server and the App.
 */
public class DAO {
    private static String URL = "ENTER URL HERE";
    private static DAO instance;
    private boolean internet;
    private AppActivity activity;
    private ArrayList<ImageItem> items = new ArrayList<>();
    private int eventId;
    private String deviceId;

    private DAO(){}

    public static void instantiate(boolean internet, AppActivity activity, int eventId, String deviceId){
        instance = new DAO();
        instance.internet = internet;
        instance.activity = activity;
        instance.eventId = eventId;
        instance.deviceId = deviceId;
        if (internet){
            //INSTANTIATE INTERNET
        }else{
            //INSTANTIATE TWILIO
        }
    }

    /**
     * Use this to get the Singleton of DAO; instantiate() must be called first before calling this!
     *
     * @return The Singleton DAO Object
     */
    public static DAO getInstance(){
        if (instance == null){
            throw new IllegalStateException("DAO was not instantiated!");
        }
        return instance;
    }

    /**
     * Queries the server for images. If it already has been called, refreshes the
     * list of items and returns the updated list.
     * @return The updated list of items
     */
    public ArrayList<ImageItem> getImages(){
        if (internet){

        }else{

        }
        return new ArrayList<>();
    }

    public void sendItem (ImageItem item){
        if (internet){

        }else{

        }
    }

}
