package io.alstonlin.wildhacksproject;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.hardware.Camera;
import android.net.Uri;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.FrameLayout;

import java.util.logging.Level;
import java.util.logging.Logger;

/**
 * The Fragment for taking a picture. Note: Uses deprecated classes.
 */
public class CameraFragment extends Fragment {

    private static final String ARG_ACTIVITY = "activity";

    private Camera camera;
    private CameraPreview preview;
    private AppActivity activity;
    private OnFragmentInteractionListener mListener;
    private Camera.PictureCallback picture = new Camera.PictureCallback() {
        @Override
        public void onPictureTaken(byte[] data, Camera camera) {
            Bitmap image = BitmapFactory.decodeByteArray(data, 0, data.length);
        }
    };


    /**
     * Gets the Camera Object for the phone.
     * @return The Camera of the phone, if available
     */
    public static Camera getCameraInstance(){
        Camera c = null;
        try {
            c = Camera.open(); // attempt to get a Camera instance
            c.setDisplayOrientation(90); //Portait Mode
        }
        catch (Exception e){
            Logger.getLogger(AppActivity.class.getName()).log(Level.SEVERE, e.getMessage());
        }
        return c;
    }

    /**
     * Factory method; use this instead of constructor to instantiate the Fragment.
     * @return A new instance of the Fragment
     */
    public static CameraFragment newInstance(AppActivity activity) {
        CameraFragment fragment = new CameraFragment();
        Bundle args = new Bundle();
        args.putSerializable(ARG_ACTIVITY, activity);
        fragment.setArguments(args);
        fragment.activity = activity;
        return fragment;
    }

    /*
     * BELOW ARE THE AUTO-GENERATED ANDROID FRAGMENT METHODS.
     */

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
        previewFrame.addView(preview, 0);

        FloatingActionButton captureButton = (FloatingActionButton) v.findViewById(R.id.capture);
        captureButton.setOnClickListener(
                new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        camera.takePicture(null, null, picture);
                    }
                });
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


}
