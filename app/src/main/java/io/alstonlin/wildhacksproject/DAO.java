package io.alstonlin.wildhacksproject;

import android.graphics.Bitmap;
import android.os.AsyncTask;
import android.util.Log;
import android.widget.Toast;

import org.apache.http.HttpResponse;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.impl.client.DefaultHttpClient;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.net.HttpURLConnection;
import java.util.ArrayList;


/**
 * This class handles the communication between the Server and the App.
 */
public class DAO {
    private static String URL = "ENTER URL HERE";
    private static DAO instance;

    private HttpURLConnection conn;
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
            instance.setupInternet();
        }else{
            instance.setupTwilio();
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
            return getImagesInternet();
        } else{
            return getImagesTwilio();
        }
    }

    public void sendItem (ImageItem item){
        if (internet){
            sendItemInternet(item);
        }else{
            sendItemTwilio(item);
        }
    }


    private void setupInternet(){
        //DAVID DO YOUR STUFF HERE
    }

    private void sendItemInternet(ImageItem item) {
        //DAVID DO YOUR STUFF HERE
    }


    public ArrayList<ImageItem> getImagesInternet() {
        //DAVID DO YOUR STUFF HERE
        return null;
    }


    private void setupTwilio(){

    }

    public ArrayList<ImageItem> getImagesTwilio() {
        return null;
    }


    private void sendItemTwilio(ImageItem item) {

    }
}
