package com.example.monopoly.game;

import android.content.Context;
import android.hardware.Sensor;
import android.hardware.SensorEvent;
import android.hardware.SensorEventListener;
import android.hardware.SensorManager;
import android.media.MediaPlayer;
import android.util.Log;
import android.widget.ImageView;

import androidx.annotation.NonNull;
import androidx.lifecycle.DefaultLifecycleObserver;
import androidx.lifecycle.LifecycleOwner;

import com.example.monopoly.GameFragment;
import com.example.monopoly.MainActivity;
import com.example.monopoly.R;
import com.example.monopoly.databinding.ActivityMainBinding;
import com.example.monopoly.databinding.FragmentGameBinding;

public class RollDiceSensor implements DefaultLifecycleObserver {

    private SensorManager sensorManager = null;

    private float y1;
    private float y2 = -1;

    private boolean shakeStarted = false;

    private FragmentGameBinding binding;

    private Context context;

    private GameFragment gameFragment;

    private boolean stop = false;


    private SensorEventListener listener = new SensorEventListener() {
        @Override
        public void onSensorChanged(SensorEvent event) {
            y1 = event.values[1];
            if(binding.endTurnRollDiceButton.getText().equals("Roll dices")){
                stop = false;
            }
            if(!stop) {
                if (binding.endTurnRollDiceButton.getText().equals("Roll dices") || binding.endTurnRollDiceButton.getText().equals("Roll double")) {

                    if (y2 != -1) {

                        if (Math.abs(y2 - y1) > MainActivity.SHAKE_LEVEL && !shakeStarted) {
                            shakeStarted = true;
                            gameFragment.startSound();
                        } else if (Math.abs(y2 - y1) < 2) {
                            if (shakeStarted && !stop) {
                                stop = true;
                                shakeStarted = false;

                                gameFragment.stopSound();

                                int dice1 = (int) Math.floor(Math.random() * 6 + 1);
                                int dice2 = (int) Math.floor(Math.random() * 6 + 1);
                            dice1 = 1;
                            dice2 = 2;
                                binding.messageTextView.setText("Rolled " + dice1 + " and " + dice2 + " = " + (dice1 + dice2));

                                ImageView dice1Image = GameFragment.dice1ImageView;
                                int image1 = context.getResources().getIdentifier("dice" + dice1, "drawable", context.getPackageName());
                                dice1Image.setImageResource(image1);

                                ImageView dice2Image = GameFragment.dice2ImageView;
                                int image2 = context.getResources().getIdentifier("dice" + dice2, "drawable", context.getPackageName());
                                dice2Image.setImageResource(image2);

                                Game.playingPlayer.setDice(dice1, dice2);
                                Game.playingPlayer.rollDice();
                            }
                        }
                    }

                }
            }
            y2 = y1;
        }

        @Override
        public void onAccuracyChanged(Sensor sensor, int accuracy) {

        }
    };

    public RollDiceSensor(FragmentGameBinding binding, GameFragment gameFragment){
        this.binding = binding;
        this.gameFragment = gameFragment;
    }

    public void start(Context context){
        this.context = context;
        sensorManager = (SensorManager) context.getSystemService(Context.SENSOR_SERVICE);
        Sensor sensor = sensorManager.getDefaultSensor(Sensor.TYPE_LINEAR_ACCELERATION);
        sensorManager.registerListener(listener, sensor, SensorManager.SENSOR_DELAY_NORMAL);
    }

    @Override
    public void onDestroy(@NonNull LifecycleOwner owner) {
        if(sensorManager != null){
            sensorManager.unregisterListener(listener);
        }
    }

    @Override
    public void onStop(@NonNull LifecycleOwner owner) {
        gameFragment.stopSound();
    }

    public void setStop(boolean stop){
        this.stop = stop;
    }
}
