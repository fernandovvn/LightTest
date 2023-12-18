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
        private static final int PARTICLES = 0, GAS = 1, TMP = 2;
        @Override
        public void onSensorChanged(SensorEvent event) {
            // The value of the first subscript in the values array is the current light intensity
            switch((int)event.values[0]){
                case PARTICLES:{
                    int pm10 = (int) event.values[1], pm25 = (int) event.values[2];
                    Log.d("LIGHT_TEST", "PM10: " + String.valueOf(pm10));
                    Log.d("LIGHT_TEST", "PM25: " + String.valueOf(pm25));
                    break;
                }
                case GAS:{
                    int lpg = (int) event.values[1], co = (int) event.values[2], smoke = (int) event.values[3];
                    Log.d("LIGHT_TEST", "LPG: " + String.valueOf(lpg));
                    Log.d("LIGHT_TEST", "CO: " + String.valueOf(co));
                    Log.d("LIGHT_TEST", "SMOKE: " + String.valueOf(smoke));

                    break;
                }
                case TMP:{
                    int humidity = (int) event.values[1], tmp = (int) event.values[2];
                    Log.d("LIGHT_TEST", "HUMIDITY: " + String.valueOf(humidity));
                    Log.d("LIGHT_TEST", "TEMPERATURE: " + String.valueOf(tmp));
                    break;
                }
                default:
                    Log.d("LIGHT_TEST", "Invalid values[0]");
            }
            float value = event.values[0];
            lightLevel.setText("Current light level is " + value + " lx" + "\n"  + sensorListString);
        }
        @Override
        public void onAccuracyChanged(Sensor sensor, int accuracy) {
        }
    };
}