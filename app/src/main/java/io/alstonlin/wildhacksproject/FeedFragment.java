package io.alstonlin.wildhacksproject;

import android.app.AlertDialog;
import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.GridView;

import org.apache.http.HttpEntity;
import org.apache.http.HttpResponse;
import org.apache.http.client.ClientProtocolException;
import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.util.EntityUtils;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.ArrayList;

/**
 * The Fragment for the image feed in AppActivity
 */
public class FeedFragment extends Fragment {

    private static final String ARG_ACTIVITY = "activity";

    private ArrayList<ImageItem> files;
    private GridView grid;
    private GridAdapter adapter;
    private OnFragmentInteractionListener mListener;
    private AppActivity activity;


    /**
     * Use this Factory method to create the Fragment instead of the constructor.
     * @param activity The Activity this Fragment will be attached to
     * @return The new Fragment instance
     */
    public static FeedFragment newInstance(AppActivity activity) {
        final FeedFragment fragment = new FeedFragment();
        Bundle args = new Bundle();
        args.putSerializable(ARG_ACTIVITY, activity);
        fragment.setArguments(args);
        fragment.activity = activity;
        return fragment;
    }

    private void setupAdapter(View v){
        grid= (GridView) v.findViewById(R.id.gridView);
        GetInfo getInfo = new GetInfo();
        files = new ArrayList<>();
        getInfo.execute(getActivity().getIntent().getStringExtra(MainActivity.EXTRA_CODE));
    }

    private class GetInfo extends AsyncTask<String,Void,Void> {
        @Override
        protected Void doInBackground(String... strings) {
            //get message from message box

            //check whether the msg empty or not
            HttpClient httpclient = new DefaultHttpClient();
            HttpGet httppost = new HttpGet("http://polarfeed.mybluemix.net/files/"+strings[0]);

            try {
                HttpResponse response = httpclient.execute(httppost);
                HttpEntity entity = response.getEntity();
                JSONArray a = new JSONArray(EntityUtils.toString(entity, "UTF-8"));
                for (int i = 0; i < a.length(); i++){
                    JSONObject b =  a.getJSONObject(i);
                    ImageItem file =  new ImageItem();
                    file.eventId = b.getString("EventID");
                    file.imageid = b.getString("ImageID");
                    file.url = b.getString("ImageURL");
                    file.timestamp = b.getString("Timestamp");
                    file.userid = b.getString("UserID");

                    try {
                        URL url = new URL("http://polarfeed.mybluemix.net/uploads/"+file.url);
                        Bitmap image = BitmapFactory.decodeStream(url.openConnection().getInputStream());
                        file.image = image;
                    } catch (MalformedURLException e) {
                        e.printStackTrace();
                    } catch (IOException e) {
                        e.printStackTrace();
                    }
                    files.add(file);
                }
            } catch (ClientProtocolException e) {
                e.printStackTrace();
            } catch (IOException e) {
                e.printStackTrace();
            } catch (JSONException e) {
                e.printStackTrace();
            }
            return null;
        }

        @Override
        protected void onPostExecute(Void aVoid) {
            super.onPostExecute(aVoid);

            adapter = new GridAdapter(activity, R.layout.grid_item, files);
            grid.setAdapter(adapter);
            grid.setOnItemClickListener(new AdapterView.OnItemClickListener() {
                @Override
                public void onItemClick(AdapterView<?> parent, View view, final int position, long id) {
                    LayoutInflater inflater = activity.getLayoutInflater();
                    View v = inflater.inflate(R.layout.image_dialog, null);
                    final AlertDialog dialog = new AlertDialog.Builder(activity).create();
                    dialog.setView(v);
                    v.findViewById(R.id.close).setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View v) {
                            dialog.dismiss();
                        }
                    });
                    v.findViewById(R.id.print).setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View v) {
                            ImageItem item = files.get(position);
                            try {
                                String url = DAO.getInstance().printImage(item);
                                //TODO: Open Web View
                            } catch (Exception e) {
                                e.printStackTrace();
                            }
                        }
                    });
                }
            });
        }
    }


    /*
     * EVERYTHING BELOW ARE THE AUTO-GENERATED ANDROID FRAGMENT METHODS
     */

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {
            activity = (AppActivity) getArguments().getSerializable(ARG_ACTIVITY);
        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View v = inflater.inflate(R.layout.fragment_feed, container, false);
        setupAdapter(v);
        return v;
    }

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        if (context instanceof OnFragmentInteractionListener) {
            mListener = (OnFragmentInteractionListener) context;
        } else {
            throw new RuntimeException(context.toString()
                    + " must implement OnFragmentInteractionListener");
        }
    }

    @Override
    public void onDetach() {
        super.onDetach();
        mListener = null;
    }

    /**
     * This interface must be implemented by activities that contain this
     * fragment to allow an interaction in this fragment to be communicated
     * to the activity and potentially other fragments contained in that
     * activity.
     * <p/>
     * See the Android Training lesson <a href=
     * "http://developer.android.com/training/basics/fragments/communicating.html"
     * >Communicating with Other Fragments</a> for more information.
     */
    public interface OnFragmentInteractionListener {
        // TODO: Update argument type and name
        void onFragmentInteraction(Uri uri);
    }
}
