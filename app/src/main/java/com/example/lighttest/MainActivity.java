package com.example.lighttest;
import androidx.appcompat.app.AppCompatActivity;
import android.content.Context;
import android.hardware.Sensor;
import android.hardware.SensorEvent;
import android.hardware.SensorEventListener;
import android.hardware.SensorManager;
import android.os.Bundle;
import android.util.Log;
import android.widget.TextView;
import java.util.List;

public class MainActivity extends AppCompatActivity {
    private SensorManager sensorManager;
    private TextView lightLevel;
    public String sensorListString = "";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        lightLevel = (TextView) findViewById(R.id.light_level);

        sensorManager = (SensorManager) getSystemService(Context.SENSOR_SERVICE);
        List<Sensor> sensorList = sensorManager.getSensorList(Sensor.TYPE_DEVICE_PRIVATE_BASE);

        String sensorListString = "";
        for (Sensor el : sensorList)
            sensorListString += el.getName() + " " + el.getStringType() + "\n";
        Log.println(Log.INFO, "LIGHT_TEST", sensorListString);

        // O sensor de luminosidade padrão vai ser o primeiro da lista, por isso o SmartLamp é o segundo
        Sensor sensor = sensorList.get(1);

        sensorManager.registerListener(listener, sensor, SensorManager.SENSOR_DELAY_NORMAL);
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        if (sensorManager != null) {
            sensorManager.unregisterListener(listener);
        }
    }

    private SensorEventListener listener = new SensorEventListener() {
        @Override
        public void onSensorChanged(SensorEvent event) {
            // The value of the first subscript in the values array is the current light intensity
            float value = event.values[0];
            lightLevel.setText("Current light level is " + value + " lx" + "\n"  + sensorListString);
        }
        @Override
        public void onAccuracyChanged(Sensor sensor, int accuracy) {
        }
    };
}