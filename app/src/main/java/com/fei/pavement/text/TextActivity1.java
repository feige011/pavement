package com.fei.pavement.text;

import androidx.appcompat.app.AppCompatActivity;

import android.hardware.Sensor;
import android.hardware.SensorEvent;
import android.hardware.SensorEventListener;
import android.hardware.SensorManager;
import android.os.Bundle;
import android.widget.TextView;

import com.fei.pavement.R;

public class TextActivity1 extends AppCompatActivity implements SensorEventListener {
    Sensor orientationSensor;
    SensorManager manager;
    TextView textView;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_text1);
        textView=findViewById(R.id.text1);
        //获取传感器管理对象
         manager= (SensorManager) getSystemService(SENSOR_SERVICE);
        //获取方向传感器
        orientationSensor=manager.getDefaultSensor(Sensor.TYPE_ORIENTATION);
        
    }
    protected void onResume() {
        super.onResume();
        if (orientationSensor!=null){

//三个参数分别是SensorEventListener，sensor，速率
            manager.registerListener( this, orientationSensor, SensorManager.SENSOR_DELAY_NORMAL);
        }
    }


    @Override
    protected void onPause() {
        super.onPause();
        if (manager!=null){
            manager.unregisterListener(this);
        }
    }
    @Override
    public void onSensorChanged(SensorEvent event) {
        float[] values = event.values;
        StringBuilder sb=new StringBuilder();
        sb.append("z轴:"+values[0]+"\n");
        sb.append("x轴:"+values[1]+"\n");
        sb.append("y轴:"+values[2]+"\n");
        textView.setText("旧API：\n"+sb.toString());

    }
    //当已注册传感器的精度发生变化时调用
    public void onAccuracyChanged(Sensor sensor, int accuracy) {

    }
}