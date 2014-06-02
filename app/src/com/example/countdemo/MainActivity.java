package com.example.countdemo;

import java.text.DecimalFormat;

import android.app.Activity;
import android.content.Context;
import android.hardware.Sensor;
import android.hardware.SensorEvent;
import android.hardware.SensorEventListener;
import android.hardware.SensorManager;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

public class MainActivity extends Activity {

	private Button start;  
	private TextView count;
	private boolean isCounting = false;
	private float countnumber=0;
	
    private long initTime = 0;   
    private long lastTime = 0;   
    private long curTime = 0;   
    private long duration = 0;   

    private float last_x = 0.0f;   
    private float last_y = 0.0f;   
    private float last_z = 0.0f;   

    private float shake = 0.0f;   
    
    
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_main);
		
		start = (Button) this.findViewById(R.id.start);
		count = (TextView) this.findViewById(R.id.count);
		count.setText("0.00");
		sensor();
		
		start.setOnClickListener(new View.OnClickListener() {
			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
                if(!isCounting){   
                    start.setText("Stop");
                    Toast.makeText(getApplicationContext(), "Started Counting", Toast.LENGTH_SHORT).show();
                    isCounting=true;
                    count.setText("0.00");
                } 
                else{
                	initShake();
                	start.setText("Start");
                	Toast.makeText(getApplicationContext(), "Stopped Counting", Toast.LENGTH_SHORT).show();
                	isCounting=false;
                }
			}
		});
	}

	
	public void sensor(){
		SensorManager sm = (SensorManager)getSystemService(Context.SENSOR_SERVICE);
		Sensor accelerometerSensor = sm.getDefaultSensor(Sensor.TYPE_ACCELEROMETER);
		SensorEventListener accelerometerListener = new SensorEventListener(){

			@Override
			public void onAccuracyChanged(Sensor arg0, int arg1) {
				// TODO Auto-generated method stub
				
			}

			@Override
			public void onSensorChanged(SensorEvent event) {
				// TODO Auto-generated method stub
				if(isCounting){
					float x = event.values[SensorManager.DATA_X];
					float y = event.values[SensorManager.DATA_Y];
					float z = event.values[SensorManager.DATA_Z];
					
//					Log.i("check", x+"");
					
					curTime = System.currentTimeMillis();
					if((curTime-lastTime)>100){
						duration = (curTime - lastTime);
						shake =(Math.abs(x - last_x)+Math.abs(y-last_y)+Math.abs(z-last_z))/duration*100;

						if(shake>1){
							count(shake);
							shake = 0;
						}

						last_x = x;
						last_y = y;
						last_z = z;
						
						lastTime = curTime;
						
					}
					
				}
			}
			
		};
		
		sm.registerListener(accelerometerListener, accelerometerSensor, SensorManager.SENSOR_DELAY_NORMAL);   
	}

	
	
	public void count(float n){
		countnumber+=(float)Math.abs((int)n);
		double show = (countnumber)/100;
		DecimalFormat df = new DecimalFormat("0.00");  
		String s = df.format(show);  
		count.setText(s);
	}
	
	
	
    public void initShake() {   
        lastTime = 0;   
        duration = 0;   
        curTime = 0;   
        initTime = 0;   
        last_x = 0.0f;   
        last_y = 0.0f;   
        last_z = 0.0f;   
        shake = 0.0f;   
        
        countnumber=0;
    } 
}
