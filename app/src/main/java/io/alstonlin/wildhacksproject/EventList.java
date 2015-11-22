package io.alstonlin.wildhacksproject;

import android.content.Context;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;

import java.util.ArrayList;


public class EventList {
    private Context context;
    private ArrayList<Event> events;
    private ListView lv;

    public EventList(Context c, ListView listView){
        context = c;
        events = new ArrayList<>();
        lv = listView;
        getInformation();
        lv.setAdapter(new EventsListAdapter(events));
    }

    public void getInformation(){
       events = DAO.getInstance().requestEvents();
    }


    private class EventsListAdapter extends BaseAdapter{

        public ArrayList<Event> evs;

        public EventsListAdapter(ArrayList<Event> events){
            evs = new ArrayList<>();
            if(events!=null) {
                evs.addAll(events);
            }
            Event ev = new Event("", "Add New", "", "");
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
            if (view == null) {
                LayoutInflater vi = (LayoutInflater)context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
                view = vi.inflate(R.layout.events_list_item, viewGroup, false);
            }
            ((TextView)view.findViewById(R.id.name)).setText(evs.get(i).getName());
            ((TextView)view.findViewById(R.id.location)).setText(evs.get(i).location);
            if (evs.get(i).getName().equals("Add New")){
                ((ImageView)view.findViewById(R.id.imageView3)).setImageDrawable(context.getDrawable(R.drawable.ic_my_library_add_indigo_a200_24dp));
            }else{
                ((ImageView)view.findViewById(R.id.imageView3)).setImageDrawable(context.getDrawable(R.drawable.groupon));
            }
            view.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    Intent intent;
                    if(evs.get(i).getName().equals("Add New")){
                        intent = new Intent(context, AddEventsActivity.class);
                    }else {
                        intent = new Intent(context, AppActivity.class);
                    }
                    intent.putExtra(MainActivity.EXTRA_CODE, evs.get(i).getEventID());
                    intent.putExtra(MainActivity.EXTRA_INTERNET, false);
                    intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                    intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK);
                    context.startActivity(intent);
                }
            });
            return view;
        }
    }
}
