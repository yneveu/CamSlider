package fr.gabuzomeu.camslider;

import android.support.v4.app.Fragment;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ScrollView;
import android.widget.TextView;

/**
 * Created by yann on 04/10/13.
 */
public class LoggingFragment extends Fragment {

    private TextView mTitleTextView;
    private TextView mDumpTextView;
    private ScrollView mScrollView;




    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        View v = inflater.inflate( R.layout.loggingfragment, null);
        mTitleTextView = (TextView) v.findViewById(R.id.demoTitle);
        mDumpTextView = (TextView) v.findViewById(R.id.consoleText);
        mScrollView = (ScrollView) v.findViewById(R.id.demoScroller);
        return (v);
    }

    public void displayMessage( String message){

           if( mDumpTextView != null && mScrollView != null  ){
                mDumpTextView.append(message);
                mScrollView.smoothScrollTo(0, mDumpTextView.getBottom());
           }

    }

}
