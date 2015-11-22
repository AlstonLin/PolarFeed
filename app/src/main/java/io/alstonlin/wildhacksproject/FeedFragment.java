package io.alstonlin.wildhacksproject;

import android.content.Context;
import android.net.Uri;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.GridView;

import java.util.ArrayList;

/**
 * The Fragment for the image feed in AppActivity
 */
public class FeedFragment extends Fragment {

    private static final String ARG_ACTIVITY = "activity";

    private GridAdapter adapter;
    private OnFragmentInteractionListener mListener;
    private AppActivity activity;

    private FeedFragment() { //Just in case someone tries to create a new instance with the constructor
    }


    /**
     * Use this Factory method to create the Fragment instead of the constructor.
     * @param activity The Activity this Fragment will be attached to
     * @return The new Fragment instance
     */
    public static FeedFragment newInstance(AppActivity activity) {
        FeedFragment fragment = new FeedFragment();
        Bundle args = new Bundle();
        args.putSerializable(ARG_ACTIVITY, activity);
        fragment.setArguments(args);
        fragment.activity = activity;
        return fragment;
    }

    private void setupAdapter(View v){
        ArrayList<ImageItem> items = DAO.getInstance().getImages();
        GridView grid = (GridView) v.findViewById(R.id.gridView);
        adapter = new GridAdapter(activity, R.layout.grid_item, items);
        grid.setAdapter(adapter);
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