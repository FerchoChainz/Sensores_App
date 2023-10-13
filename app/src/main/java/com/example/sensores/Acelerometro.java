package com.example.sensores;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Context;
import android.hardware.Sensor;
import android.hardware.SensorEvent;
import android.hardware.SensorEventListener;
import android.hardware.SensorManager;
import android.os.Bundle;
import android.widget.TextView;

import java.util.List;

public class Acelerometro extends AppCompatActivity implements SensorEventListener {

    private TextView acelerometro;
    private SensorManager sensorManager;
    private List<Sensor> deviceSensor;
    private Sensor acelerometroSensor;
    private boolean existeAcelerometro;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_acelerometro);

        acelerometro = findViewById(R.id.txtAcelerometer);

        // Inicializar el SensorManager
        sensorManager = (SensorManager) getSystemService(Context.SENSOR_SERVICE);

        if (sensorManager.getDefaultSensor(Sensor.TYPE_ACCELEROMETER) != null) {
            acelerometroSensor = sensorManager.getDefaultSensor(Sensor.TYPE_ACCELEROMETER);
            sensorManager.registerListener(this, acelerometroSensor, SensorManager.SENSOR_DELAY_NORMAL);
            existeAcelerometro = true;
        } else {
            acelerometro.setText("No se cuenta con sensor de acelerómetro");
            existeAcelerometro = false;
        }
    }

    @Override
    public void onSensorChanged(SensorEvent sensorEvent) {
        if (sensorEvent.sensor.getType() == Sensor.TYPE_ACCELEROMETER) {
            acelerometro.setText("X: " + sensorEvent.values[0] + "\n" +
                    "Y: " + sensorEvent.values[1] + "\n" +
                    "Z: " + sensorEvent.values[2]);
        } else {
            acelerometro.setText("");
        }
    }

    @Override
    public void onAccuracyChanged(Sensor sensor, int i) {
        // Implementa este método si deseas realizar alguna acción específica cuando cambie la precisión del sensor.
    }

    @Override
    protected void onResume() {
        super.onResume();
        // Validar el tipo de sensor y registrar cuando la aplicación está en primer plano
        if (existeAcelerometro) {
            sensorManager.registerListener(this, acelerometroSensor, SensorManager.SENSOR_DELAY_NORMAL);
        }
    }

    @Override
    protected void onPause() {
        super.onPause();
        // Validar el tipo de sensor y eliminar el registro del sensor
        if (existeAcelerometro) {
            sensorManager.unregisterListener(this);
        }
    }
}
