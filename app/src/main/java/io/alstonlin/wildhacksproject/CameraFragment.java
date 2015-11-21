package io.alstonlin.wildhacksproject;

import android.content.Context;
import android.hardware.Camera;

import android.net.Uri;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.FrameLayout;

import java.util.logging.Level;
import java.util.logging.Logger;


public class CameraFragment extends Fragment {

    private static final String ARG_ACTIVITY = "activity";

    private Camera camera;
    private CameraPreview preview;
    private AppActivity activity;
    private OnFragmentInteractionListener mListener;
    private Camera.PictureCallback picture = new Camera.PictureCallback() {
        @Override
        public void onPictureTaken(byte[] data, Camera camera) {
            //TODO: Send once picture is taken
        }
    };



    public static Camera getCameraInstance(){
        Camera c = null;
        try {
            c = Camera.open(); // attempt to get a Camera instance
            c.setDisplayOrientation(90);
        }
        catch (Exception e){
            Logger.getLogger(AppActivity.class.getName()).log(Level.SEVERE, e.getMessage());
            //GG
        }
        return c; // returns null if camera is unavailable
    }

    public static CameraFragment newInstance(AppActivity activity) {
        CameraFragment fragment = new CameraFragment();
        Bundle args = new Bundle();
        args.putSerializable(ARG_ACTIVITY, activity);
        fragment.setArguments(args);
        fragment.activity = activity;
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {
            activity = (AppActivity) getArguments().getSerializable(ARG_ACTIVITY);
        }
        camera = getCameraInstance();
        preview = new CameraPreview(activity, camera);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View v = inflater.inflate(R.layout.fragment_camera, container, false);
        FrameLayout previewFrame = (FrameLayout) v.findViewById(R.id.camera_preview);
        previewFrame.addView(preview);
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

    public interface OnFragmentInteractionListener {
        void onFragmentInteraction(Uri uri);
    }

    public void setupButtons(){
        Button captureButton = (Button) activity.findViewById(R.id.capture);
        captureButton.setOnClickListener(
                new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        camera.takePicture(null, null, picture);
                    }
                }
        );
    }


}
