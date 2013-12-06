package fr.gabuzomeu.camslider;

import android.app.ActionBar;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ScrollView;
import android.widget.TextView;

/**
 * Created by yann on 04/10/13.
 */
public class MainFragment extends Fragment {


    TextView textViewSpeed;


    String speedToSend;
    String stepsToSend;
    String delayToSend;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {





        View v = inflater.inflate( R.layout.mainfragment, null);



        final EditText speedEditText = ( EditText) v.findViewById( R.id.speedEditText);
        final EditText stepsEditText = ( EditText) v.findViewById( R.id.stepEditText);
        final EditText delayEditText = ( EditText) v.findViewById( R.id.delayEditText);

        //Steps -
        final Button buttonStepsMinus = (Button) v.findViewById(R.id.buttonStepsMinus );
        buttonStepsMinus.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                byte[] mess = "forward\n".getBytes();
                stepsToSend =   stepsEditText.getText().toString();
                int iStepstoSend = Integer.parseInt( stepsToSend) -1;
                stepsToSend = Integer.toString( iStepstoSend);
                stepsEditText.setText( stepsToSend);
            }
        });

        //Steps +
        final Button buttonStepsPlus = (Button) v.findViewById(R.id.buttonStepsPlus );
        buttonStepsPlus.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                byte[] mess = "backward\n".getBytes();
                stepsToSend =   stepsEditText.getText().toString();
                int iStepstoSend = Integer.parseInt( stepsToSend) +1;
                stepsToSend = Integer.toString( iStepstoSend);
                stepsEditText.setText( stepsToSend);


            }
        });

        //Speed -
        final Button buttonSpeedMinus = (Button) v.findViewById(R.id.buttonSpeedMinus );
        buttonSpeedMinus.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                speedToSend =   speedEditText.getText().toString();
                int iSpeedtoSend = Integer.parseInt( speedToSend) -1;
                speedToSend = Integer.toString( iSpeedtoSend);
                speedEditText.setText( speedToSend);
            }
        });

        //Speed +
        final Button buttonSpeedPlus = (Button) v.findViewById(R.id.buttonSpeedPlus );
        buttonSpeedPlus.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                speedToSend =   speedEditText.getText().toString();
                int iSpeedtoSend = Integer.parseInt( speedToSend) +1;
                speedToSend = Integer.toString( iSpeedtoSend);
                speedEditText.setText( speedToSend);
            }
        });


        //Delay -
        final Button buttonDelayMinus = (Button) v.findViewById(R.id.buttonDelayMinus );
        buttonDelayMinus.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                delayToSend =   delayEditText.getText().toString();
                int iDelaytoSend = Integer.parseInt( delayToSend) -1;
                delayToSend = Integer.toString( iDelaytoSend);
                delayEditText.setText( delayToSend);
            }
        });

        //Delay +
        final Button buttonDelayPlus = (Button) v.findViewById(R.id.buttonDelayPlus );
        buttonDelayPlus.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                delayToSend =   delayEditText.getText().toString();
                int iDelaytoSend = Integer.parseInt( delayToSend) +1;
                delayToSend = Integer.toString( iDelaytoSend);
                delayEditText.setText( delayToSend);
            }
        });

        //Right button
        final Button rightButton = (Button) v.findViewById(R.id.rightButton );
        rightButton.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                String tmpString = "fling:-5";
                byte[] mess = (tmpString + "\n").getBytes();
                MainActivity.mSerialIoManager.writeAsync( mess);

            }
        });

        //Left button
        final Button leftButton = (Button) v.findViewById(R.id.leftButton );
        leftButton.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                String tmpString = "fling:5";
                byte[] mess = (tmpString + "\n").getBytes();
                MainActivity.mSerialIoManager.writeAsync( mess);

            }
        });

        //Send config
        final Button sendConfigButton = (Button) v.findViewById( R.id.sendConfigButton );
        sendConfigButton.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                stepsToSend =   stepsEditText.getText().toString();
                speedToSend =   speedEditText.getText().toString();
                delayToSend =   delayEditText.getText().toString();

                String tmpString = "speed:" + speedToSend;
                byte[] mess = (tmpString + "\n").getBytes();
                MainActivity.mSerialIoManager.writeAsync( mess);

                tmpString = "steps:" + stepsToSend;
                mess = (tmpString + "\n").getBytes();
                MainActivity.mSerialIoManager.writeAsync( mess);

                tmpString = "delay:" + delayToSend;
                mess = (tmpString + "\n").getBytes();
                MainActivity.mSerialIoManager.writeAsync( mess);

            }
        });

        //Pause/Resume
        final Button buttonPauseResume = (Button) v.findViewById(R.id.buttonPauseResume );
        buttonPauseResume.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                byte[] mess = "pause\n".getBytes();
                MainActivity.mSerialIoManager.writeAsync( mess);
            }
        });


        textViewSpeed = (TextView) v.findViewById(R.id.readSpeed );
   //     textViewSteps = (TextView) v.findViewById(R.id.readSteps );
   //     textViewDelay = (TextView) v.findViewById(R.id.readDelay );

        return( v);

    }

    public void updateDisplayedSpeed( String str){
        textViewSpeed.setText( str);
    }


}

