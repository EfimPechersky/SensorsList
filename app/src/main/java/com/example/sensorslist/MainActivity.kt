package com.example.sensorslist

import android.hardware.Sensor
import android.hardware.SensorEvent
import android.hardware.SensorEventListener
import android.hardware.SensorManager
import android.os.Bundle
import android.view.View
import android.widget.Adapter
import android.widget.AdapterView
import android.widget.ArrayAdapter
import android.widget.ListView
import android.widget.RadioGroup
import android.widget.Spinner
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import androidx.databinding.DataBindingUtil
import androidx.databinding.ViewDataBinding
import com.example.sensorslist.databinding.ActivityMainBinding
import com.google.android.material.button.MaterialButton.OnCheckedChangeListener

class MainActivity :SensorEventListener, AppCompatActivity() {
    lateinit var sm: SensorManager
    lateinit var list: ListView
    val noSensor = "датчик отсутствует"
    var tSensor:Sensor? = null             //температура
    var lSensor:Sensor? = null             //свет
    var hSensor:Sensor? = null           //влажность
    lateinit var db: ActivityMainBinding
    var sensorName = ArrayList<String>()
    fun containsAny(target: String, strings: Array<String>): Boolean {
        return strings.any { target.contains(it) }
    }
    var choice = 1
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContentView(R.layout.activity_main)
        sm = getSystemService(SENSOR_SERVICE) as SensorManager
        db = DataBindingUtil.setContentView(this,R.layout.activity_main)
        val array: Array<String> = resources.getStringArray(R.array.type_sensors)
        var s:Spinner =  findViewById(R.id.spinner);
        var adapter = ArrayAdapter<String>(this, android.R.layout.simple_spinner_item, array);
        var rad:RadioGroup = findViewById(R.id.sensors)
        rad.setOnCheckedChangeListener{ _, checkedId ->
            when(checkedId){
                R.id.light->{choice=1;db.sensorsInfo.text = "ОСВЕЩЕННОСТЬ: " + noSensor}
                R.id.humidity->{choice=2;db.sensorsInfo.text = "ОТНОСИТЕЛЬНАЯ ВЛАЖНОСТЬ: " + noSensor}
                R.id.temperature->{choice=3;db.sensorsInfo.text = "ТЕМПЕРАТУРА: " + noSensor}
            }
        }
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
    override fun onResume() {
        super.onResume()
        tSensor = sm.getDefaultSensor(Sensor.TYPE_AMBIENT_TEMPERATURE)
        if(tSensor !=null)
            sm.registerListener(this,tSensor,SensorManager.SENSOR_DELAY_GAME)
        lSensor = sm.getDefaultSensor(Sensor.TYPE_LIGHT)
        if(lSensor !=null)
            sm.registerListener(this,lSensor,SensorManager.SENSOR_DELAY_GAME)
        hSensor = sm.getDefaultSensor(Sensor.TYPE_RELATIVE_HUMIDITY)
        if(hSensor !=null)
            sm.registerListener(this,hSensor,SensorManager.SENSOR_DELAY_GAME)
    }
    override fun onPause() {
        super.onPause()
        sm.unregisterListener(this) }
    override fun onSensorChanged(event: SensorEvent?) {
        var h = 0f
        var t = 0f
        if (tSensor == null && choice==3) db.sensorsInfo.text = "ТЕМПЕРАТУРА: " + noSensor
        else if (event!!.sensor.type == tSensor!!.type && choice==3) {
            t = event.values[0]; db.sensorsInfo.text = "ТЕМПЕРАТУРА: " + t   }
        if (lSensor == null && choice==1) db.sensorsInfo.text = "ОСВЕЩЁННОСТЬ: " + noSensor
        else if (event!!.sensor.type == lSensor!!.type && choice==1) db.sensorsInfo.text =
            "ОСВЕЩЁННОСТЬ: " + event.values[0]
        if (hSensor == null && choice==2) db.sensorsInfo.text = "ОТНОСИТЕЛЬНАЯ ВЛАЖНОСТЬ: " + noSensor
        else if (event!!.sensor.type == hSensor!!.type && choice==2) {
            h = event.values[0]; db.sensorsInfo.text = "ОТНОСИТЕЛЬНАЯ ВЛАЖНОСТЬ: " + h
        }
    }
        override fun onAccuracyChanged(sensor: Sensor?, accuracy: Int) { }
}