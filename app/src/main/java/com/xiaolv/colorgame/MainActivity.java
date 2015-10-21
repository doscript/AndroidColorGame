package com.xiaolv.colorgame;

import android.app.Activity;
import android.content.Intent;
import android.hardware.Sensor;
import android.hardware.SensorEvent;
import android.hardware.SensorEventListener;
import android.hardware.SensorManager;
import android.os.Vibrator;
import android.support.v7.app.ActionBarActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.KeyEvent;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.TextView;


public class MainActivity extends Activity implements View.OnClickListener,SensorEventListener{

    private static final String TAG = "MainActivity";
    int mCurColor = 0;
    private SensorManager mSensorManager;
    private Vibrator vibrator;
    private Sensor mAccelerometer;
    private TextView mTv;


    private float mAccel; // acceleration apart from gravity
    private float mAccelCurrent; // current acceleration including gravity
    private float mAccelLast; // last acceleration including gravity

    public MainActivity(){

    }


    Intent i = new Intent();


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        mTv = (TextView) findViewById(R.id.tv_main);
        mTv.setOnClickListener(this);

        mSensorManager = (SensorManager)getSystemService(SENSOR_SERVICE);
        mAccelerometer = mSensorManager.getDefaultSensor(Sensor.TYPE_ACCELEROMETER);

        mAccel = 0.00f;
        mAccelCurrent = SensorManager.GRAVITY_EARTH;
        mAccelLast = SensorManager.GRAVITY_EARTH;
        vibrator = (Vibrator) getSystemService(VIBRATOR_SERVICE);
    }

    @Override
    protected void onResume() {
        super.onResume();
        mSensorManager.registerListener(this, mAccelerometer, SensorManager.SENSOR_DELAY_NORMAL);

    }

    @Override
    protected void onPause() {
        super.onPause();
        mSensorManager.unregisterListener(this);

    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_main, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_settings) {
            return true;
        }

        return super.onOptionsItemSelected(item);
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()){
            case R.id.tv_main :
                changeColorSequently(v, mCurColor);
                break;
            default:
                break;

        }
    }

    /**
     * 禁用key
     * @param keyCode
     * @param event
     * @return
     */
    @Override
    public boolean onKeyDown(int keyCode, KeyEvent event) {
        return true;
    }
//    @Override
//    public boolean onKeyUp(int keyCode, KeyEvent event) {
//        return true;
//    }
//
//    @Override
//    public boolean onKeyMultiple(int keyCode, int repeatCount, KeyEvent event) {
////        return super.onKeyMultiple(keyCode, repeatCount, event);
//        return true;
//    }
//
//    @Override
//    public boolean onKeyLongPress(int keyCode, KeyEvent event) {
////        return super.onKeyLongPress(keyCode, event);
//        return true;
//    }
//
//
//    @Override
//    public boolean onKeyShortcut(int keyCode, KeyEvent event) {
////        return super.onKeyShortcut(keyCode, event);
//        return true;
//    }




    private int changeColorSequently(View v,int colorNo){
        switch(mCurColor) {
            case 0:
                v.setBackgroundColor(getResources().getColor(R.color.red));
                mCurColor++;
                break;
            case 1:
                v.setBackgroundColor(getResources().getColor(R.color.green));
                mCurColor++;
                break;
            case 2:
                v.setBackgroundColor(getResources().getColor(R.color.blue));
                mCurColor++;
                break;
            case 3:
                v.setBackgroundColor(getResources().getColor(R.color.white));
                mCurColor=0;
                break;
            default:
                break;
        }
        return 0;
    }

    @Override
    public void onSensorChanged(SensorEvent se) {

        float x = se.values[0];
        float y = se.values[1];
        float z = se.values[2];
        mAccelLast = mAccelCurrent;
        mAccelCurrent = (float) Math.sqrt((double) (x*x + y*y + z*z));
        float delta = mAccelCurrent - mAccelLast;
        mAccel = mAccel * 0.9f + delta; // perform low-cut filter

        Log.i(TAG,"mAccel = " + mAccel);

        if (Math.abs(mAccel) > 2) //shake
        {
            Log.i(TAG,"shaked mAccel = " + mAccel);
//            vibrator.vibrate(200);
            changeColorSequently(mTv,mCurColor);
        }
    }

    @Override
    public void onAccuracyChanged(Sensor sensor, int accuracy) {

    }
}
