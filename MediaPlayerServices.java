package com.tdh.Sup;

import com.tdh.tetris.R;

import android.app.Service;
import android.content.Intent;
import android.media.MediaPlayer;
import android.os.IBinder;

public class MediaPlayerServices extends Service {
	MediaPlayer player;
	public static Boolean keeplaying=false;
	
	@Override
	public IBinder onBind(Intent intent) {
		return null;
	}
	
	@Override
	public void onCreate() {
		player = MediaPlayer.create(this, R.raw.theme);
		player.setLooping(true); // Set looping
	}
	
	@Override
	public void onDestroy() {
		if(!keeplaying)
			player.stop();
	}
	
	@Override
	public void onStart(Intent intent, int startid) {
		player.start();
	}
	
}
