package io.alstonlin.wildhacksproject;

import android.content.Intent;
import android.net.Uri;
import android.provider.MediaStore;
import android.telephony.SmsManager;

import org.apache.http.HttpResponse;
import org.apache.http.client.ResponseHandler;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.entity.StringEntity;
import org.apache.http.impl.client.BasicResponseHandler;
import org.apache.http.impl.client.DefaultHttpClient;
import org.json.JSONException;
import org.json.JSONObject;

import java.net.HttpURLConnection;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;
import java.util.Random;


/**
 * This class handles the communication between the Server and the App.
 */
public class DAO {
    private static String URL = "ENTER URL HERE";
    private static String WALGREENS_URL = "https://services-qa.walgreens.com/api/util/mweb5url";
    private static String WALGREENS_KEY = "uEgdGWwKeWc6WPekIyotrgntvTHhYtaz";
    private static String NUMBER = "8473837143";
    private static String SEND_IMAGE_KEY = "SEND";
    private static String REQUEST_KEY = "REQUEST";
    private static DAO instance;

    private Command callback;
    private SmsManager manager;
    private HttpURLConnection conn;
    private boolean internet;
    private AppActivity activity;
    private ArrayList<ImageItem> items = new ArrayList<>();
    private int eventId;
    private String deviceId;

    private DAO(){}

    public static void instantiate(boolean internet, AppActivity activity, int eventId, String deviceId, Command callback){
        instance = new DAO();
        instance.internet = internet;
        instance.activity = activity;
        instance.eventId = eventId;
        instance.deviceId = deviceId;
        instance.callback = callback;
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
     */
    public void getImages(){
        if (internet){
            requestImagesInternet();
        } else{
            requestImagesTwilio();
        }
    }

    public void sendItem(ImageItem item){
        if (internet){
            sendItemInternet(item);
        }else{
            sendItemTwilio(item);
        }
    }

    /**
     * This should be called when the server sends back the images after the query.
     *
     * @param images The images sent
     */
    private void receiveImages(ArrayList<ImageItem> images){
        callback.exec(images);
    }

    /**
     * Requests a image to be printed
     * @param item
     */
    private void printImage(ImageItem item){

    }


    private void setupInternet(){
        //DAVID DO YOUR STUFF HERE
    }

    private void sendItemInternet(ImageItem item) {
        Map<String, String> map = new HashMap();

    }


    public void requestImagesInternet() {
        //DAVID DO YOUR STUFF HERE
    }


    private void setupTwilio(){
        manager = SmsManager.getDefault();
    }

    public void requestImagesTwilio() {
        manager.sendTextMessage(NUMBER, null, REQUEST_KEY, null, null);
    }


    private void sendItemTwilio(ImageItem item) {
        Random rand = new Random();
        String path = MediaStore.Images.Media.insertImage(activity.getContentResolver(), item.getImage(), "Image" + rand.nextInt(99999), null);
        Uri uri = Uri.parse(path);
        Intent intent = new Intent(Intent.ACTION_SEND);
        intent.putExtra("address", NUMBER);
        intent.putExtra("sms_body", SEND_IMAGE_KEY + " " + item.getEventId() + " " + item.getDeviceId());
        intent.putExtra(Intent.EXTRA_STREAM, "file:/" + uri);
        intent.setType("image/png");
        intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
        activity.startActivity(intent);
    }

    public static HttpResponse makeRequest(String path, Map params) throws Exception {
        DefaultHttpClient httpclient = new DefaultHttpClient();
        HttpPost httpost = new HttpPost(path);
        JSONObject holder = getJsonObjectFromMap(params);
        StringEntity se = new StringEntity(holder.toString());

        httpost.setEntity(se);
        httpost.setHeader("Accept", "application/json");
        httpost.setHeader("Content-type", "application/json");
        ResponseHandler responseHandler = new BasicResponseHandler();

        return (HttpResponse) httpclient.execute(httpost, responseHandler);
    }


    private static JSONObject getJsonObjectFromMap(Map params) throws JSONException {
        Iterator iter = params.entrySet().iterator();
        JSONObject holder = new JSONObject();

        while (iter.hasNext()) {
            Map.Entry pairs = (Map.Entry) iter.next();
            String key = (String) pairs.getKey();
            Map m = (Map) pairs.getValue();
            JSONObject data = new JSONObject();
            Iterator iter2 = m.entrySet().iterator();
            while (iter2.hasNext()) {
                Map.Entry pairs2 = (Map.Entry) iter2.next();
                data.put((String) pairs2.getKey(), (String) pairs2.getValue());
            }
            holder.put(key, data);
        }
        return holder;
    }
}


