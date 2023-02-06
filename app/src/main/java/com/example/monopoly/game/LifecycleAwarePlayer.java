package com.example.monopoly.game;

import android.content.Context;
import android.media.MediaPlayer;

import androidx.annotation.NonNull;
import androidx.lifecycle.DefaultLifecycleObserver;
import androidx.lifecycle.LifecycleOwner;

import java.io.File;
import java.io.IOException;



public class LifecycleAwarePlayer implements DefaultLifecycleObserver {

    private MediaPlayer mediaPlayer;


    public LifecycleAwarePlayer(){

    }

    public void start(Context context){
        //if(mediaPlayer == null) {     TODO: trenutno je vezana za main activity lifecycle tako da se nikada ne unistava
            try {
                String sound = "dice_sound.wav";
                String path = context.getFilesDir().getAbsoluteFile() + File.separator + sound;
                mediaPlayer = new MediaPlayer();
                mediaPlayer.setDataSource(path);
                mediaPlayer.setOnPreparedListener(new MediaPlayer.OnPreparedListener() {
                    @Override
                    public void onPrepared(MediaPlayer mp) {
                        mediaPlayer.start();
                    }
                });

                mediaPlayer.prepareAsync();

            } catch (IOException e) {
                e.printStackTrace();
            }
       // }
    }

    public void stopSound(){
        if(mediaPlayer != null){
            mediaPlayer.stop();
            mediaPlayer.release();
            mediaPlayer = null;
        }
    }

    @Override
    public void onDestroy(@NonNull LifecycleOwner owner) {
        stopSound();
    }
}
