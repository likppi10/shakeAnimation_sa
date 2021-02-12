package com.example.shakeanimation;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.fragment.app.FragmentTransaction;

import android.hardware.Sensor;
import android.hardware.SensorEvent;
import android.hardware.SensorEventListener;
import android.hardware.SensorManager;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.TextView;

import butterknife.ButterKnife;

public class MainActivity extends AppCompatActivity implements SensorEventListener{

    private long mShakeTime;
    private int mShakeCount = 0;
    private static final int SHAKE_SKIP_TIME = 500;
    private static final float SKAKE_THRESHOLD_GRAVITY = 2.7F;

    private SensorManager mSensorManager;
    private Sensor mAccelerometer;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        ButterKnife.bind(this);

        FragmentTransaction ft = getSupportFragmentManager().beginTransaction();
        ft.replace(R.id.layout_main, ExampleFragment.newInstance(ExampleFragment.NODIR));
        ft.commit();


        mSensorManager = (SensorManager) getSystemService(SENSOR_SERVICE);
        mAccelerometer = mSensorManager.getDefaultSensor(Sensor.TYPE_ACCELEROMETER);
    }
        @Override
        public boolean onCreateOptionsMenu(Menu menu) {
            getMenuInflater().inflate(R.menu.menu_main, menu);
            return true;
        }
        @Override
        public boolean onOptionsItemSelected(MenuItem item) {
            int id = item.getItemId();
            ExampleFragment f = (ExampleFragment)getSupportFragmentManager().findFragmentById(R.id.layout_main);
            switch (id) {
                case R.id.style_cube:
                    f.setAnimationStyle(ExampleFragment.CUBE);
                    return true;
                case R.id.style_flip:
                    f.setAnimationStyle(ExampleFragment.FLIP);
                    return true;
            }
            return super.onOptionsItemSelected(item);
        }

    //listener 등록과 해제 - 배터리 소모를 생각함
    @Override
    protected void onResume(){
        super.onResume();
        mSensorManager.registerListener(this,mAccelerometer,SensorManager.SENSOR_DELAY_GAME);
    }
    @Override
    protected void onPause(){
        super.onPause();
        mSensorManager.unregisterListener(this);
    }

    public void onAccuracyChanged(Sensor sensor, int accuracy){
        //정확도 설정
    }

    //흔들면 회전
    @Override
    public void onSensorChanged(SensorEvent event) {
        if(event.sensor.getType() == Sensor.TYPE_ACCELEROMETER){
            float axisX = event.values[0];
            float axisY = event.values[1];
            float axisZ = event.values[2];

            float gravityX = axisX / SensorManager.GRAVITY_EARTH;
            float gravityY = axisY / SensorManager.GRAVITY_EARTH;
            float gravityZ = axisZ / SensorManager.GRAVITY_EARTH;

            Float f = gravityX * gravityX + gravityY * gravityY + gravityZ * gravityZ;
            double squaredD = Math.sqrt(f.doubleValue());
            float gForce = (float) squaredD;
            if(gForce > SKAKE_THRESHOLD_GRAVITY){
                long currentTime = System.currentTimeMillis();
                if(mShakeTime + SHAKE_SKIP_TIME > currentTime){
                    return;
                }
                mShakeTime = currentTime;
                ExampleFragment mf = (ExampleFragment) getSupportFragmentManager().findFragmentById(R.id.layout_main);
                mf.onButtonLeft();
            }
        }
    }
}