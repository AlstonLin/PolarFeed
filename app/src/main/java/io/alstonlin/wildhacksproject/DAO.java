package io.alstonlin.wildhacksproject;

import android.app.Activity;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Environment;
import android.provider.MediaStore;
import android.telephony.SmsManager;
import android.util.Log;

import org.apache.http.HttpEntity;
import org.apache.http.HttpResponse;
import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.entity.ContentType;
import org.apache.http.entity.mime.HttpMultipartMode;
import org.apache.http.entity.mime.MultipartEntity;
import org.apache.http.entity.mime.MultipartEntityBuilder;
import org.apache.http.entity.mime.content.ContentBody;
import org.apache.http.entity.mime.content.FileBody;
import org.apache.http.entity.mime.content.StringBody;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.util.EntityUtils;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.ArrayList;
import java.util.Random;
import java.util.concurrent.ExecutionException;


/**
 * This class handles the communication between the Server and the App.
 */
public class DAO {
    private static String SITE_URL = "http://polarfeed.mybluemix.net";
    private static String GET_PRINT_PATH = "/print";
    private static String POST_IMAGE_PATH = "/fileupload";
    private static String POST_EVENT_PATH = "/addevent";
    private static String GET_IMAGES_PATH = "/files";
    private static String GET_EVENTS_PATH = "/events";
    private static String NUMBER = "8473837143";
    private static String SEND_IMAGE_KEY = "SEND";
    private static String REQUEST_KEY = "REQUEST";
    private static DAO instance;

    private SmsManager manager;
    private HttpURLConnection conn;
    private boolean internet = true;
    private Activity activity;
    private ArrayList<ImageItem> items = new ArrayList<>();
    private int eventId;
    private String deviceId;

    private DAO(){}

    public static void instantiate(boolean internet, Activity activity, int eventId, String deviceId){
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
     */
    public ArrayList<ImageItem> getImages(){
        if (internet){
            try {
                return requestImagesInternet(eventId);
            } catch (Exception e) {
                e.printStackTrace();
                return null;
            }
        } else{
            return requestImagesTwilio(eventId);
        }
    }

    /**
     * Gets a List of all the available events to join from the server.
     * @return A list of all the available events to join
     */
    public ArrayList<Event> requestEvents(){
        if (internet){
            try {
                return requestEventsInternet();
            } catch (Exception e) {
                e.printStackTrace();
                return null;
            }
        }else {
            return requestEventsTwilio();
        }
    }

    /**
     * Posts an Item onto the server.
     * @param item The item to post
     */
    public void requestItem(ImageItem item){
        if (internet){
            try {
                sendItemInternet(item);
            } catch (JSONException | UnsupportedEncodingException e) {
                e.printStackTrace();
            }
        }else{
            sendItemTwilio(item);
        }
    }


    /**
     * Requests a image to be printed
     * @param item The item to print
     * @return The SITE_URL to the webpage
     */
    public String printImage(ImageItem item) throws Exception {
        if (internet){
            return printImageInternet(item);
        }else{
            return printImageTwilio(item);
        }
    }

    public String postEvent(Event e) throws IOException, JSONException, InterruptedException, ExecutionException {
        return postEventInternet(e);
    }

    public void postImage(Bitmap image) throws UnsupportedEncodingException, JSONException {
        ImageItem item = new ImageItem();
        item.image = image;
        item.userid = deviceId;
        item.eventId = Integer.toString(eventId);
        sendItemInternet(item);
    }

    /*
       ------------------- INTERNET METHODS -------------------------------------
     */
    private void setupInternet(){
    }

    private void sendItemInternet(ImageItem item) throws JSONException, UnsupportedEncodingException {
        MultipartEntity entity = new MultipartEntity(HttpMultipartMode.BROWSER_COMPATIBLE);

        Random rand = new Random();
        //String path = MediaStore.Images.Media.insertImage(activity.getContentResolver(), item.getImage(), "Image" + rand.nextInt(999), null);
        FileOutputStream out = null;
        String file_path = Environment.getExternalStorageDirectory().getAbsolutePath() +
                "/WildHacks";
        File dir = new File(file_path);

        if(!dir.exists())
            dir.mkdirs();
        try {
            File file = new File(dir, "sketchpad" + rand.nextInt(999) + ".jpg");
            FileOutputStream fOut = new FileOutputStream(file);

            item.getImage().compress(Bitmap.CompressFormat.JPEG, 100, fOut);
            fOut.flush();
            fOut.close();
            file_path = file.getAbsolutePath();
            uploadImage u = new uploadImage();
            u.execute(file_path);
        }
        catch (Exception e) {
            e.printStackTrace();
        }


        File file = new File(file_path);
        ContentBody cb = new FileBody(file, "image/jpeg");

        entity.addPart("photo", cb);
        entity.addPart("eventID", new StringBody(Integer.toString(eventId)));
        entity.addPart("userID",  new StringBody(deviceId));

        HTTPPost post = new HTTPPost(POST_IMAGE_PATH);
        //post.execute(entity);
    }

    private class uploadImage extends AsyncTask<String, Void, Void>{

        @Override
        protected Void doInBackground(String... strings) {


            MultipartEntityBuilder multipartEntity = MultipartEntityBuilder
                    .create();
            multipartEntity.setMode(HttpMultipartMode.BROWSER_COMPATIBLE);
            ContentType contentType = ContentType.create("image/jpg");
            multipartEntity.addPart("photo", new FileBody(new File(strings[0]), contentType, "image.jpg"));
            HttpPost post = new HttpPost("http://polarfeed.mybluemix.net/fileupload");
            try {
                post.addHeader("eventID", String.valueOf(new StringBody(Integer.toString(eventId))));//id is anything as you may need
                multipartEntity.addPart("eventID", new StringBody(Integer.toString(eventId)));
                multipartEntity.addPart("userID", new StringBody(deviceId));
            } catch (UnsupportedEncodingException e) {
                e.printStackTrace();
            }
            post.setEntity(multipartEntity.build());

            HttpClient client = new DefaultHttpClient();

            try {
                HttpResponse response = client.execute(post);
                Log.d("asdfasd", EntityUtils.toString(response.getEntity()));
            } catch (IOException e) {
                e.printStackTrace();
            }

            return null;
        }
    }


    private ArrayList<Event> requestEventsInternet() throws ExecutionException, InterruptedException, JSONException {
        HTTPGet get = new HTTPGet(GET_EVENTS_PATH);
        JSONArray jArray = get.execute().get();
        ArrayList<Event> events = new ArrayList<>();
        for (int i = 0; i < jArray.length(); i++){
            JSONObject json = jArray.getJSONObject(i);
            Event event = new Event(json.getString("EventID"), json.getString("Name"), json.getString("location"), json.getString("details"));
            events.add(event);
        }
        return events;
    }

    private ArrayList<ImageItem> requestImagesInternet(int eventId) throws ExecutionException, InterruptedException, JSONException, IOException {
        HTTPGet get = new HTTPGet(GET_IMAGES_PATH + "/" + Integer.toString(eventId));
        JSONArray jArray = get.execute().get();
        ArrayList<ImageItem> images = new ArrayList<>();
        for (int i = 0; i < jArray.length(); i++){
            JSONObject json = jArray.getJSONObject(i);
            URL url = new URL(json.getString("ImageID"));
            Bitmap image = BitmapFactory.decodeStream(url.openConnection().getInputStream());
            ImageItem item = new ImageItem(image, json.getString("ImageID"), Integer.parseInt(json.getString("EventID")), Integer.parseInt(json.getString("UserID")));
            images.add(item);
        }
        return images;
    }

    private String postEventInternet(Event e) throws JSONException, IOException, ExecutionException, InterruptedException {
        HTTPPost post = new HTTPPost(POST_EVENT_PATH);
        JSONObject json = new JSONObject();
        json.put("name", e.getName());
        json.put("location", e.getLocation());
        json.put("description", e.getDetails());
        //HttpResponse response = post.execute(new StringEntity(json.toString()));
//        String s =  (new AsyncTask<HttpResponse, Void, String>() {
//            @Override
//            protected String doInBackground(HttpResponse... params) {
//                try {
//                    return EntityUtils.toString(params[0].getEntity());
//                } catch (IOException e1) {
//                    e1.printStackTrace();
//                    return null;
//                }
//            }
//        }).execute(response).get();
        return "'";
    }

    private String printImageInternet(ImageItem item) throws Exception {
        HTTPGetString get = new HTTPGetString(GET_PRINT_PATH + "/" + item.getId());
        String url = get.execute().get();
        return url;
    }

     /*
       ------------------- TWILIO METHODS -------------------------------------
     */

    private void setupTwilio(){
        manager = SmsManager.getDefault();
    }

    public ArrayList<ImageItem> requestImagesTwilio(int eventId) {
        manager.sendTextMessage(NUMBER, null, REQUEST_KEY, null, null);
        return null;
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

    public ArrayList<Event> requestEventsTwilio(){
        return null;
    }


    private String printImageTwilio(ImageItem item){
        return null;
    }


    /*
        HELPER CLASSES
     */

    private class HTTPGet extends AsyncTask<Void,Void,JSONArray> {
        private String path;

        public HTTPGet(String path){
            this.path = path;
        }

        @Override
        protected JSONArray doInBackground(Void... voids) {
            HttpResponse response;
            HttpClient myClient = new DefaultHttpClient();
            HttpGet myConnection = new HttpGet(SITE_URL + path);
            try {
                response = myClient.execute(myConnection);
                String str = EntityUtils.toString(response.getEntity(), "UTF-8");
                JSONArray jArray = new JSONArray(str);
                return jArray;
            } catch (IOException e) {
                e.printStackTrace();
            } catch (JSONException e) {
                e.printStackTrace();
            }
            return null;
        }
    }


    private class HTTPGetString extends AsyncTask<Void,Void,String> {
        private String path;

        public HTTPGetString(String path){
            this.path = path;
        }

        @Override
        protected String doInBackground(Void... voids) {
            HttpResponse response;
            HttpClient myClient = new DefaultHttpClient();
            HttpGet myConnection = new HttpGet(SITE_URL + path);
            try {
                response = myClient.execute(myConnection);
                String str = EntityUtils.toString(response.getEntity(), "UTF-8");
                return str;
            } catch (IOException e) {
                e.printStackTrace();
            }
            return null;
        }
    }

    private class HTTPPost extends AsyncTask<HttpEntity, Void, HttpResponse > {
        private String path;

        public HTTPPost(String path){
            this.path = path;
        }

        @Override
        protected HttpResponse doInBackground(HttpEntity... entity) {
            HttpClient myClient = new DefaultHttpClient();
            HttpPost post = new HttpPost(SITE_URL + path);
            try {
                post.setEntity(entity[0]);
                HttpResponse response = myClient.execute(post);
                Log.d("asdfasd", EntityUtils.toString(response.getEntity()));
            } catch (Exception e) {
                e.printStackTrace();
            }
            return null;
        }
    }
}


