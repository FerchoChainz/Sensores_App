package com.example.sensores;

import androidx.appcompat.app.AppCompatActivity;
import androidx.core.view.ViewCompat;

import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.hardware.Sensor;
import android.hardware.SensorEvent;
import android.hardware.SensorEventListener;
import android.hardware.SensorManager;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import java.util.List;

public class MainActivity extends AppCompatActivity implements SensorEventListener {

    private TextView titulo;
    private Button listado, magnetico, acelerometer, lightSensor;
    private SensorManager sensorManager;
    private List<Sensor> deviceSensor;
    private Sensor sensor;
    private float valorCambio, valorAcele;
    private Sensor proximoSensor, acelerometroSensor;
    private boolean existeSensorProximidad, existeAcelerometro;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        titulo = findViewById(R.id.txtTitulo);
        listado = findViewById(R.id.btnListado);
        magnetico = findViewById(R.id.btnMagnetico);
        acelerometer = findViewById(R.id.btnAcelerometro);
        lightSensor = findViewById(R.id.btnSensorLight);

        //gestion de sensores
        sensorManager = (SensorManager) getSystemService(Context.SENSOR_SERVICE);
        deviceSensor = sensorManager.getSensorList(Sensor.TYPE_ALL);



        //validar la existencia del sensor de proximidad
        if (sensorManager.getDefaultSensor(Sensor.TYPE_PROXIMITY)  != null){
            proximoSensor = sensorManager.getDefaultSensor(Sensor.TYPE_PROXIMITY);
            acelerometroSensor = sensorManager.getDefaultSensor(Sensor.TYPE_ACCELEROMETER);
            sensorManager.registerListener(this, acelerometroSensor, SensorManager.SENSOR_DELAY_NORMAL);
            existeSensorProximidad = true;
            existeAcelerometro = true;
        }else{
            titulo.setText("No se cuenta con sensor de proximidad");
            existeSensorProximidad = false;
            existeAcelerometro = false;
        }

        listado.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                //Mostrar la lista de sensosres del dispositivo
                titulo.setText("");
                titulo.setBackgroundColor(Color.GRAY);
                titulo.setTextColor(Color.WHITE);
                for (Sensor sensor: deviceSensor){
                    titulo.setText(titulo.getText() + "\n " + sensor.getName());
                }
            }
        });

        magnetico.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                //validar sensor magnetico
                if (sensorManager.getDefaultSensor(Sensor.TYPE_MAGNETIC_FIELD) != null){
                    Toast.makeText(MainActivity.this, "El dispositivo tiene sensor magnetico", Toast.LENGTH_SHORT).show();
                    //Mostrar los atributos del sensor
                    sensor = sensorManager.getDefaultSensor(Sensor.TYPE_MAGNETIC_FIELD);
                    titulo.setBackgroundColor(Color.GRAY);
                    titulo.setTextColor(Color.WHITE);
                    titulo.setText(String.valueOf("Propiedades del sensor magnetico: \n" +
                            "Potencia en mA: " + String.valueOf(sensor.getPower()) + "\n" +
                            "Nombre: " + sensor.getName() + "\n" +
                            "Version: " + String.valueOf(sensor.getVersion()) + "\n" +
                            "Provedor: " + String.valueOf(sensor.getVendor())));
                }else{
                    Toast.makeText(MainActivity.this, "El dispositivo sin contar con sensor magnetico", Toast.LENGTH_SHORT).show();
                }
            }
        });

        acelerometer.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(getApplicationContext(),Acelerometro.class);
                startActivity(intent);
            }
        });

        lightSensor.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(getApplicationContext(), SensorLuzActivity.class);
                startActivity(intent);
            }
        });
    }




    @Override
    public void onSensorChanged(SensorEvent sensorEvent) {
        valorCambio = sensorEvent.values[0];
        if (valorCambio < 1.0){
            titulo.setTextSize(30);
            titulo.setBackgroundColor(Color.BLUE);
            titulo.setTextColor(Color.WHITE);
            titulo.setText("\nCerca" + valorCambio);
        }else{
            titulo.setTextSize(14);
            titulo.setBackgroundColor(Color.GREEN);
            titulo.setTextColor(Color.BLACK);
            titulo.setText("\nLenjos" + valorCambio);
        }
    }

    @Override
    public void onAccuracyChanged(Sensor sensor, int i) {

    }

    @Override
    protected void onResume() {
        super.onResume();
        //validar el tipo de sensor y registrar cuando la aplicacion esta en primer plano
        if (existeSensorProximidad)
            sensorManager.registerListener(this,proximoSensor,
                    SensorManager.SENSOR_DELAY_NORMAL);
    }

    @Override
    protected void onPause() {
        super.onPause();
        //validar el tipo de sensor y eliminar registro del sensor
        if(existeSensorProximidad){
            sensorManager.unregisterListener(this);
        }
    }
}