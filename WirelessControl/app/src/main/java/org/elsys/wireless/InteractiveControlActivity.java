package org.elsys.wireless;

import android.content.Context;
import android.content.pm.ActivityInfo;
import android.content.res.Configuration;
import android.graphics.Color;
import android.graphics.PorterDuff;
import android.hardware.Sensor;
import android.hardware.SensorEvent;
import android.hardware.SensorEventListener;
import android.hardware.SensorManager;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.KeyEvent;
import android.widget.ImageView;

public class InteractiveControlActivity extends AppCompatActivity implements SensorEventListener {

    private SensorManager mSensorManager;
    private Sensor mAccelerometer;
    private ImageView forward;
    private ImageView left;
    private ImageView right;
    private ImageView back;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_interactive_control);
        setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_LANDSCAPE);

        forward = (ImageView) findViewById(R.id.forward);
        left = (ImageView) findViewById(R.id.left);
        right = (ImageView) findViewById(R.id.right);
        back = (ImageView) findViewById(R.id.back);

        mSensorManager = (SensorManager) getSystemService(Context.SENSOR_SERVICE);
        mAccelerometer = mSensorManager.getDefaultSensor(Sensor.TYPE_ACCELEROMETER);
        mSensorManager.registerListener(this, mAccelerometer, SensorManager.SENSOR_DELAY_NORMAL);
    }

    @Override
    public void onConfigurationChanged(Configuration newConfig) {
        super.onConfigurationChanged(newConfig);
        setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_LANDSCAPE);
    }

    protected void onResume() {
        super.onResume();
        mSensorManager.registerListener(this, mAccelerometer, SensorManager.SENSOR_DELAY_NORMAL);
    }

    protected void onPause() {
        super.onPause();
        mSensorManager.unregisterListener(this);
    }

    @Override
    public void onAccuracyChanged(Sensor sensor, int accuracy) {
        // can be safely ignored for this demo
    }

    @Override
    public void onSensorChanged(SensorEvent event) {
        float x = event.values[0];
        float y = event.values[1];
        float z = event.values[2];

        // Acceleration
        if (Math.abs(x) > 2) {
            MovementService.setSpeed((int) (Math.abs(x) * 20));
            if (x < 0) {
                MovementService.forward();
                forward.setColorFilter(Color.RED, PorterDuff.Mode.LIGHTEN);
            }
            if (x > 0) {
                MovementService.back();
                back.setColorFilter(Color.RED, PorterDuff.Mode.LIGHTEN);
            }
        } else {
            MovementService.stop();
            forward.setColorFilter(Color.TRANSPARENT, PorterDuff.Mode.LIGHTEN);
            back.setColorFilter(Color.TRANSPARENT, PorterDuff.Mode.LIGHTEN);
        }

        // Steering
        if (Math.abs(y) > 0.5) {
            MovementService.steerByAngle(y*2);
            if (y < 0) {
                left.setColorFilter(Color.RED, PorterDuff.Mode.LIGHTEN);
                right.setColorFilter(Color.TRANSPARENT, PorterDuff.Mode.LIGHTEN);
            }
            if (y > 0) {
                left.setColorFilter(Color.TRANSPARENT, PorterDuff.Mode.LIGHTEN);
                right.setColorFilter(Color.RED, PorterDuff.Mode.LIGHTEN);
            }
        } else {
            MovementService.steerByAngle(0);
            left.setColorFilter(Color.TRANSPARENT, PorterDuff.Mode.LIGHTEN);
            right.setColorFilter(Color.TRANSPARENT, PorterDuff.Mode.LIGHTEN);
        }
    }

    @Override
    public boolean onKeyDown(int keyCode, KeyEvent event) {
        if (keyCode == KeyEvent.KEYCODE_VOLUME_UP) {
            MovementService.headlight(true);
            return true;
        } else if (keyCode == KeyEvent.KEYCODE_VOLUME_DOWN) {
            MovementService.headlight(false);
            return true;
        } else {
            return super.onKeyDown(keyCode, event);
        }
    }
}