package org.elsys.wireless;

import android.content.Intent;
import android.content.pm.ActivityInfo;
import android.content.res.Configuration;
import android.graphics.Color;
import android.graphics.PorterDuff;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.KeyEvent;
import android.view.MotionEvent;
import android.view.View;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.SeekBar;

public class ControlActivity extends AppCompatActivity {

    SeekBar speedSlider;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_control);
        setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_PORTRAIT);

        MovementService.setSpeed(25);
        MovementService.stop();
        MovementService.straight();

        final ImageButton forwardButton = (ImageButton) findViewById(R.id.forwardButton);
        forwardButton.setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View v, MotionEvent event) {
                if (event.getAction() == MotionEvent.ACTION_DOWN) {
                    MovementService.forward();
                    forwardButton.setColorFilter(Color.RED, PorterDuff.Mode.LIGHTEN);
                }
                if (event.getAction() == MotionEvent.ACTION_UP) {
                    MovementService.stop();
                    forwardButton.setColorFilter(Color.TRANSPARENT, PorterDuff.Mode.LIGHTEN);
                }
                return false;
            }
        });

        final ImageButton leftButton = (ImageButton) findViewById(R.id.leftButton);
        leftButton.setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View v, MotionEvent event) {
                if (event.getAction() == MotionEvent.ACTION_DOWN) {
                    MovementService.left();
                    leftButton.setColorFilter(Color.RED, PorterDuff.Mode.LIGHTEN);
                }
                if (event.getAction() == MotionEvent.ACTION_UP) {
                    MovementService.straight();
                    leftButton.setColorFilter(Color.TRANSPARENT, PorterDuff.Mode.LIGHTEN);
                }
                return false;
            }
        });

        final ImageButton rightButton = (ImageButton) findViewById(R.id.rightButton);
        rightButton.setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View v, MotionEvent event) {
                if (event.getAction() == MotionEvent.ACTION_DOWN) {
                    MovementService.right();
                    rightButton.setColorFilter(Color.RED, PorterDuff.Mode.LIGHTEN);
                }
                if (event.getAction() == MotionEvent.ACTION_UP) {
                    MovementService.straight();
                    rightButton.setColorFilter(Color.TRANSPARENT, PorterDuff.Mode.LIGHTEN);
                }
                return false;
            }
        });

        final ImageButton backButton = (ImageButton) findViewById(R.id.backButton);
        backButton.setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View v, MotionEvent event) {
                if (event.getAction() == MotionEvent.ACTION_DOWN) {
                    MovementService.back();
                    backButton.setColorFilter(Color.RED, PorterDuff.Mode.LIGHTEN);
                } else if (event.getAction() == MotionEvent.ACTION_UP) {
                    MovementService.stop();
                    backButton.setColorFilter(Color.TRANSPARENT, PorterDuff.Mode.LIGHTEN);
                }
                return false;
            }
        });

        speedSlider = (SeekBar) findViewById(R.id.speedSlider);
        speedSlider.setProgress(25);
        speedSlider.setOnSeekBarChangeListener(new SeekBar.OnSeekBarChangeListener() {
            @Override
            public void onProgressChanged(SeekBar seekBar, int progress, boolean fromUser) {
                if (progress < 25)
                    speedSlider.setProgress(25);
            }

            @Override
            public void onStartTrackingTouch(SeekBar seekBar) {

            }

            @Override
            public void onStopTrackingTouch(SeekBar seekBar) {
                int progress = speedSlider.getProgress();
                if (progress < 25) {
                    progress = 25;
                }
                MovementService.setSpeed(progress);
            }
        });

        final Button intModeButton = (Button) findViewById(R.id.intModeButton);
        intModeButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent myIntent = new Intent(ControlActivity.this, InteractiveControlActivity.class);
                ControlActivity.this.startActivity(myIntent);
            }
        });
    }

    public void onResume() {
        super.onResume();
        MovementService.setSpeed(25);
        MovementService.stop();
        MovementService.straight();
        speedSlider.setProgress(25);
    }

    @Override
    public void onConfigurationChanged(Configuration newConfig) {
        super.onConfigurationChanged(newConfig);
        setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_PORTRAIT);
    }

    @Override
    public void onBackPressed() {
        MovementService.netService.disconnect();
        super.onBackPressed();
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
