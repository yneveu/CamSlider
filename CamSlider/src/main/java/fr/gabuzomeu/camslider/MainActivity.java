package fr.gabuzomeu.camslider;

import android.app.ActionBar;

import android.app.FragmentTransaction;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentActivity;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;
import android.support.v4.view.ViewPager;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.View;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.Button;
import android.widget.ImageView;

import com.hoho.android.usbserial.driver.UsbSerialDriver;
import com.hoho.android.usbserial.util.SerialInputOutputManager;

import java.io.IOException;
import java.util.StringTokenizer;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;




/**
 * Monitors a single {@link UsbSerialDriver} instance, showing all data
 * received.
 *
 * @author mike wakerly (opensource@hoho.com)
 */
public class MainActivity extends FragmentActivity implements ActionBar.TabListener{

    static final int ITEMS = 3;
    MyAdapter mAdapter;
    ViewPager mPager;

    Menu mMenu = null;
    public static MainFragment mainFragment = null;
    public static LoggingFragment loggingFragment = null;
    public static GestureFragment gestureFragment = null;


    public static class MyAdapter extends FragmentPagerAdapter {
        public MyAdapter(FragmentManager fragmentManager) {
            super(fragmentManager);
        }

        @Override
        public int getCount() {
            return ITEMS;
        }

        @Override
        public Fragment getItem(int position) {
            switch (position) {
                case 0: // Fragment # 0 - This will show image
                    if ( mainFragment == null)
                        return new MainFragment();
                    else return mainFragment;
                case 1: // Fragment # 1 - This will show image
                    if( loggingFragment == null )
                        return new LoggingFragment();
                    else
                        return loggingFragment;
                case 2: // Fragment # 1 - This will show image
                    if( gestureFragment == null )
                        return new GestureFragment();
                    else
                        return gestureFragment;
                default:
                    if( loggingFragment == null )
                        return new MainFragment();
                    else
                        return mainFragment;

            }
        }
    }


    private final String TAG = MainActivity.class.getSimpleName();

    /**
     * Driver instance, passed in statically via
     * {@link #show(Context, UsbSerialDriver)}.
     *
     * <p/>
     * This is a devious hack; it'd be cleaner to re-create the driver using
     * arguments passed in with the {@link #startActivity(Intent)} intent. We
     * can get away with it because both activities will run in the same
     * process, and this is a simple demo.
     */
    private static UsbSerialDriver sDriver = null;



    private final ExecutorService mExecutor = Executors.newSingleThreadExecutor();

    public static  SerialInputOutputManager mSerialIoManager;


    @Override
    public void onTabSelected(ActionBar.Tab tab, FragmentTransaction ft) {

    }

    @Override
    public void onTabUnselected(ActionBar.Tab tab, FragmentTransaction ft) {
       /* if (fragList.size() > tab.getPosition()) {
            ft.remove(fragList.get(tab.getPosition()));
        }*/

    }

    @Override
    public void onTabReselected(ActionBar.Tab tab, FragmentTransaction ft) {

    }


    private final SerialInputOutputManager.Listener mListener =
            new SerialInputOutputManager.Listener() {

                @Override
                public void onRunError(Exception e) {
                    Log.d(TAG, "Runner stopped.");
                }

                @Override
                public void onNewData(final byte[] data) {
                    MainActivity.this.runOnUiThread(new Runnable() {
                        @Override
                        public void run() {
                            MainActivity.this.updateReceivedData(data);
                        }
                    });
                }
            };

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView( R.layout.activity_main);
        mAdapter = new MyAdapter(getSupportFragmentManager());
        mPager = (ViewPager) findViewById(R.id.pager);
        mPager.setOffscreenPageLimit(2);
        mPager.setAdapter(mAdapter);
        mAdapter.getItem(1);

        loggingFragment = new LoggingFragment();
        mainFragment = new MainFragment();

        Button button = (Button) findViewById(R.id.first);
        button.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                mPager.setCurrentItem(0);
            }
        });
        button = (Button) findViewById(R.id.last);
        button.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                mPager.setCurrentItem(ITEMS - 1);
            }
        });


    }

    @Override
    protected void onPause() {
        super.onPause();
        stopIoManager();
        if (sDriver != null) {
            try {
                sDriver.close();
            } catch (IOException e) {
                // Ignore.
            }
            sDriver = null;
        }
        finish();
    }

    @Override
    protected void onResume() {
        super.onResume();
        Log.d(TAG, "Resumed, sDriver=" + sDriver);
        if (sDriver == null) {
           // mTitleTextView.setText("No serial device.");
        } else {
            try {
                sDriver.open();
                sDriver.setParameters(115200, 8, UsbSerialDriver.STOPBITS_1, UsbSerialDriver.PARITY_NONE);
            } catch (IOException e) {
                Log.e(TAG, "Error setting up device: " + e.getMessage(), e);
             //   mTitleTextView.setText("Error opening device: " + e.getMessage());
                try {
                    sDriver.close();
                } catch (IOException e2) {
                    // Ignore.
                }
                sDriver = null;
                return;
            }
           // mTitleTextView.setText("Serial device: " + sDriver.getClass().getSimpleName());
        }
        onDeviceStateChange();
    }

    private void stopIoManager() {
        if (mSerialIoManager != null) {
            Log.i(TAG, "Stopping io manager ..");
            mSerialIoManager.stop();
            mSerialIoManager = null;
        }
    }

    private void startIoManager() {
        if (sDriver != null) {
            Log.i(TAG, "Starting io manager ..");
            mSerialIoManager = new SerialInputOutputManager(sDriver, mListener);
            mExecutor.submit(mSerialIoManager);
        }
    }

    private void onDeviceStateChange() {
        stopIoManager();
        startIoManager();
    }

    private void updateReceivedData(byte[] data) {
        //final String message = "Read " + data.length + " bytes: \n"
        //        + HexDump.dumpHexString(data) + "\n\n";
        String message = new String( data);

        StringTokenizer str = new StringTokenizer( message);



       // if( message.contains( "Speed"))
            mainFragment.updateDisplayedSpeed( message);


        loggingFragment.displayMessage( message);

        LayoutInflater inflater = (LayoutInflater) getApplication()
                .getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        ImageView iv = (ImageView) inflater.inflate(R.layout.action_refresh,
                null);



        Animation rotation = AnimationUtils.loadAnimation(getApplication(),
                R.anim.refresh_rotate);
        rotation.setRepeatCount( 2);
        iv.startAnimation(rotation);
        if( mMenu != null)
            mMenu.getItem( 0).setActionView( iv);
        //item.setActionView(iv);

    }

    /**
     * Starts the activity, using the supplied driver instance.
     *
     * @param context
     * @param driver
     */
    static void show(Context context, UsbSerialDriver driver) {
        sDriver = driver;
        final Intent intent = new Intent(context, MainActivity.class);
        intent.addFlags(Intent.FLAG_ACTIVITY_SINGLE_TOP | Intent.FLAG_ACTIVITY_NO_HISTORY);
        context.startActivity(intent);
    }

    @Override
    public boolean onPrepareOptionsMenu(Menu menu) {
        new MenuInflater(this).inflate(R.menu.main, menu);
       // if( mMenu == null)
            mMenu = menu;
        //return (super.onCreateOptionsMenu(menu));
        return true;
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        new MenuInflater(this).inflate(R.menu.main, menu);
        //if( mMenu == null)
        mMenu = menu;
        return (super.onCreateOptionsMenu(menu));
    }

}
