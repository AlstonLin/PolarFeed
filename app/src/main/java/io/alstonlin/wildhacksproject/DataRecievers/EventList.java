package io.alstonlin.wildhacksproject.DataRecievers;

import android.content.Context;
import android.content.Intent;
import android.os.AsyncTask;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ListView;
import android.widget.TextView;

import org.apache.http.HttpResponse;
import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.util.EntityUtils;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.util.ArrayList;
import java.util.concurrent.ExecutionException;

import io.alstonlin.wildhacksproject.AppActivity;
import io.alstonlin.wildhacksproject.R;

/**
 * Created by David Liu on 11/22/2015.
 */
public class EventList {
    private Context context;
    private ArrayList<Event> events;
    private ListView lv;


    public final static String EXTRA_CODE = "io.alstonlin.wildhacksproject.CODE";
    public final static String EXTRA_INTERNET = "io.alstonlin.wildhacksproject.INTERNET";
    public EventList(Context c, ListView listView){
        context = c;
        events = new ArrayList<>();
        lv = listView;
        getInformation();
    }
    public void getInformation(){
        GetInfo getInfo = new GetInfo();
        try {
            JSONArray response = getInfo.execute().get();
            if(response!=null){
                for(int i = 0; i <response.length(); i++){
                    JSONObject object = response.getJSONObject(i);
                    Event ev = new Event();
                    ev.EventID  = object.getString("EventID");
                    ev.Name  = object.getString("Name");
                    ev.details  = object.getString("details");
                    ev.location  = object.getString("location");
                    events.add(ev);
                    lv.setAdapter(new EventsListAdapter(events));
                }
            }
        } catch (InterruptedException e) {
            e.printStackTrace();
        } catch (ExecutionException e) {
            e.printStackTrace();
        } catch (JSONException e) {
            e.printStackTrace();
        }
    }

    private class GetInfo extends AsyncTask<Void,Void,JSONArray>{

        @Override
        protected JSONArray doInBackground(Void... voids) {


            JSONObject json = null;
            String str = "";
            HttpResponse response;
            HttpClient myClient = new DefaultHttpClient();
            HttpGet myConnection = new HttpGet("http://polarfeed.mybluemix.net/events");
            // HttpClient is more then less deprecated. Need to change to URLConnection
            try {
                response = myClient.execute(myConnection);
                str = EntityUtils.toString(response.getEntity(), "UTF-8");
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

    private class Event{
        public String EventID,Name,location,details;
    }

    private class EventsListAdapter extends BaseAdapter{

        public ArrayList<Event> evs;

        public EventsListAdapter(ArrayList<Event> events){
            evs = new ArrayList<>();
            evs.addAll(events);
            Event ev = new Event();
            ev.Name = "Add New";
            evs.add(ev);
        }

        @Override
        public int getCount() {
            return evs.size();
        }

        @Override
        public Object getItem(int i) {
            return evs.get(i);
        }

        @Override
        public long getItemId(int i) {
            return 0;
        }

        @Override
        public View getView(final int i, View view, ViewGroup viewGroup) {
            if(view==null) {
                LayoutInflater vi = (LayoutInflater)context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
                view = vi.inflate(R.layout.events_list_item, viewGroup, false);
            }
            ((TextView)view.findViewById(R.id.name)).setText(evs.get(i).Name);
            ((TextView)view.findViewById(R.id.location)).setText(evs.get(i).location);
            view.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    Intent intent = new Intent(context, AppActivity.class);
                    intent.putExtra(EXTRA_CODE, evs.get(i).EventID);
                    intent.putExtra(EXTRA_INTERNET, false);
                    intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                    intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK);
                    context.startActivity(intent);
                }
            });
            return view;
        }
    }
}
