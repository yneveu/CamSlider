package fr.gabuzomeu.camslider;

import android.os.Bundle;
import android.provider.SyncStateContract;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.GestureDetector;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;

/**
 * Created by yann on 11/10/13.
 */
public class GestureFragment extends Fragment {

    public static String TAG = "Gesture";



    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        if (container == null) {
            return null;
        }
        View v = inflater.inflate( R.layout.gesturefragment, null, false);

        final GestureDetector gesture = new GestureDetector(getActivity(),
                new GestureDetector.SimpleOnGestureListener() {

                    @Override
                    public boolean onDown(MotionEvent e) {
                        return true;
                    }


                    @Override
                    public boolean onFling(MotionEvent e1, MotionEvent e2, float velocityX,
                                           float velocityY) {
                        Log.d(TAG, "onFling");
                        final int SWIPE_MIN_DISTANCE = 120;
                        final int SWIPE_MAX_OFF_PATH = 250;
                        final int SWIPE_THRESHOLD_VELOCITY = 200;
                        try {
                            if (Math.abs(e1.getX() - e2.getX()) > SWIPE_MAX_OFF_PATH)
                                return false;
                            if (e1.getY() - e2.getY() > SWIPE_MIN_DISTANCE
                                    && Math.abs(velocityY) > SWIPE_THRESHOLD_VELOCITY) {
                                Log.d(TAG, "Right to Left");
                                String tmpString = "fling:" + Math.round( velocityY/100);
                                byte[] mess = (tmpString + "\n").getBytes();
                                MainActivity.mSerialIoManager.writeAsync( mess);
                                Log.d( TAG, Float.toString( velocityY));
                            } else if (e2.getY() - e1.getY() > SWIPE_MIN_DISTANCE
                                    && Math.abs(velocityY) > SWIPE_THRESHOLD_VELOCITY) {
                                Log.d(TAG, "Left to Right");
                                String tmpString = "fling:" + Math.round( velocityY/100);
                                byte[] mess = (tmpString + "\n").getBytes();
                                MainActivity.mSerialIoManager.writeAsync( mess);
                                Log.d( TAG,  Float.toString( velocityY));
                               // titles.showDetails(getPosition() - 1);
                            }
                        } catch (Exception e) {
                            // nothing
                        }
                        return super.onFling(e1, e2, velocityX, velocityY);
                    }
                });

        v.setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View v, MotionEvent event) {
                return gesture.onTouchEvent(event);
            }
        });

        return v;
    }






}
