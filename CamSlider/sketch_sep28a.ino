#include <AFMotor.h>


AF_Stepper motor( 200, 1);

String readString ="";

int mstep = 0;
int paused = 0;

int delayTime = 1000;
int mspeed = 100;
int activityOk = 0;


void setup() {
  Serial.begin(115200);
  motor.setSpeed( mspeed);
}

void performMove( String code){
	if( code == "forward"){
		mstep = mstep +1 ;
	}
	if( code == "backward"){
		mstep = mstep -1;
	}
        if( code == "pause"){
                if( paused == 0){
                  paused = 1;
                }else if( paused == 1){
                  paused = 0;
                }
	}
        if( code.startsWith("speed:")){
          String readSpeed = code.substring( code.indexOf( ":")+1);
          Serial.print( "SPPPEEEDDDD:" + readSpeed );
          mspeed =readSpeed.toInt();
        }
        if( code.startsWith("steps:")){
          String readSteps = code.substring( code.indexOf( ":")+1);
          Serial.print( "STEEPPPSSS:" + readSteps );
          mstep =readSteps.toInt();
        }
        if( code.startsWith("delay:")){
          String readDelay = code.substring( code.indexOf( ":")+1);
          Serial.print( "DELLLLAAYYY:" + readDelay );
          delayTime =readDelay.toInt();
        }
        if( code.startsWith("fling:")){
          String readFling = code.substring( code.indexOf( ":")+1);
          Serial.print( "FFLLLIINNGGGG:" + readFling );
          int flingValue = readFling.toInt();
          
           if( flingValue > 0){
		motor.step( abs( flingValue), FORWARD, DOUBLE);
	  }
	  if( flingValue < 0){
		motor.step( abs( flingValue), BACKWARD, DOUBLE);
	  }
          
        }
}

void loop() {
	
        while ( Serial.available() > 0) {
		char readChar = Serial.read();
		readString = readString + readChar;
        if( readChar == '\n'){
                        readString.replace( "\n", "");
			//Serial.println( "In loop, read: " + readString + " Step: " + mstep);
                        performMove( readString);
			readString="";
		}			
    }
	if( paused != 1){
    	  if( mstep > 0){
		motor.step( abs( mstep), FORWARD, DOUBLE);
	  }
	  if( mstep < 0){
		motor.step( abs( mstep), BACKWARD, DOUBLE);
	  }
        }
        delay( delayTime);
        activityOk++;
        if( activityOk == 5 ){
          Serial.print( "Speed:");
          Serial.println( mspeed, DEC);
          Serial.print( "Step:" );
          Serial.println( mstep, DEC);
          Serial.print( "Delay:" );
          Serial.println( delayTime, DEC);
          activityOk = 0;
        }
}




