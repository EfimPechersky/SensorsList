package com.example.sensorslist

import android.hardware.Sensor
import android.hardware.SensorManager
import android.os.Bundle
import android.view.View
import android.widget.Adapter
import android.widget.AdapterView
import android.widget.ArrayAdapter
import android.widget.ListView
import android.widget.Spinner
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat

class MainActivity : AppCompatActivity() {
    lateinit var sm: SensorManager
    lateinit var list: ListView
    var sensorName = ArrayList<String>()
    fun containsAny(target: String, strings: Array<String>): Boolean {
        return strings.any { target.contains(it) }
    }
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContentView(R.layout.activity_main)
        sm = getSystemService(SENSOR_SERVICE) as SensorManager

        val array: Array<String> = resources.getStringArray(R.array.type_sensors)
        var s:Spinner =  findViewById(R.id.spinner);
        var adapter = ArrayAdapter<String>(this, android.R.layout.simple_spinner_item, array);

        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        s.setAdapter(adapter);
        var environmentalSensors = ArrayList<String>()
        var positionSensors = ArrayList<String>()
        var bodySensors = ArrayList<String>()
        var sensorList = sm.getSensorList(-1)
        for (sensor in sensorList) {
            when (sensor.type) {

                Sensor.TYPE_AMBIENT_TEMPERATURE,
                Sensor.TYPE_LIGHT,
                Sensor.TYPE_PRESSURE,
                Sensor.TYPE_RELATIVE_HUMIDITY,
                Sensor.TYPE_PROXIMITY -> environmentalSensors.add(sensor.name)


                Sensor.TYPE_ACCELEROMETER,
                Sensor.TYPE_GYROSCOPE,
                Sensor.TYPE_GRAVITY,
                Sensor.TYPE_LINEAR_ACCELERATION,
                Sensor.TYPE_ROTATION_VECTOR,
                Sensor.TYPE_MAGNETIC_FIELD,
                Sensor.TYPE_ORIENTATION -> positionSensors.add(sensor.name)


                Sensor.TYPE_HEART_RATE,
                Sensor.TYPE_STEP_COUNTER,
                Sensor.TYPE_STEP_DETECTOR,
                Sensor.TYPE_SIGNIFICANT_MOTION -> bodySensors.add(sensor.name)


            }
        }
        s.onItemSelectedListener = object : AdapterView.OnItemSelectedListener {
            override fun onItemSelected(parent: AdapterView<*>, view: View?, position: Int, id: Long) {
                list = findViewById(R.id.sensorlist)
                when (position){
                    0->sensorName=environmentalSensors
                    1->sensorName=positionSensors
                    2->sensorName=bodySensors
                }
                var adapterName = ArrayAdapter(
                applicationContext,
                android.R.layout.simple_list_item_1,
                sensorName)
                list.adapter = adapterName
            }
            override fun onNothingSelected(parent: AdapterView<*>) {
            }
        }
    }
}