package com.example.sensores;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Context;
import android.hardware.Sensor;
import android.hardware.SensorEvent;
import android.hardware.SensorEventListener;
import android.hardware.SensorManager;
import android.os.Bundle;
import android.widget.TextView;

public class SensorLuzActivity extends AppCompatActivity implements SensorEventListener {

    private TextView intensidadLuz;
    private SensorManager sensorManager;
    private Sensor luzSensor;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_sensor_luz);

        intensidadLuz = findViewById(R.id.txtIntensidadLuz);

        // Inicializar el SensorManager
        sensorManager = (SensorManager) getSystemService(Context.SENSOR_SERVICE);

        // Verificar si el sensor de luz está disponible en el dispositivo
        if (sensorManager.getDefaultSensor(Sensor.TYPE_LIGHT) != null) {
            luzSensor = sensorManager.getDefaultSensor(Sensor.TYPE_LIGHT);
        } else {
            intensidadLuz.setText("El dispositivo no tiene sensor de luz");
        }
    }

    @Override
    public void onSensorChanged(SensorEvent sensorEvent) {
        if (sensorEvent.sensor.getType() == Sensor.TYPE_LIGHT) {
            float lux = sensorEvent.values[0];
            intensidadLuz.setText("Intensidad de luz: " + lux + " lux");
        }
    }

    @Override
    public void onAccuracyChanged(Sensor sensor, int accuracy) {
        // Implementa este método si deseas realizar alguna acción específica cuando cambie la precisión del sensor.
    }

    @Override
    protected void onResume() {
        super.onResume();
        // Registrar el sensor de luz cuando la aplicación está en primer plano
        if (luzSensor != null) {
            sensorManager.registerListener(this, luzSensor, SensorManager.SENSOR_DELAY_NORMAL);
        }
    }

    @Override
    protected void onPause() {
        super.onPause();
        // Eliminar el registro del sensor de luz cuando la aplicación pasa a segundo plano
        if (luzSensor != null) {
            sensorManager.unregisterListener(this);
        }
    }
}
