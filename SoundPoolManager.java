package com.tdh.Sup;

import android.content.Context;
import android.media.AudioManager;
import android.media.SoundPool;

public class SoundPoolManager {
	private Context pContext;
	private SoundPool sndPool;
	private float rate = 1.0f;
	private float leftVolume = 1.0f;
	private float rightVolume = 1.0f;
    
    // Constructor, setup the audio manager and 
    // store the app context
	public SoundPoolManager(Context appContext)
	{
	  sndPool = new SoundPool(16, AudioManager.STREAM_MUSIC, 100);
 	  pContext = appContext;
	}
	
	// Load up a sound and return the id
	public int load(int sound_id)
	{
		return sndPool.load(pContext, sound_id, 1);
	}
	
	// Play a sound
	public int play(int sound_id)
	{
		return sndPool.play(sound_id, leftVolume, rightVolume, 1, 0, rate); 	
	}
	
	// Free ALL the things
	public void unloadAll()
	{
		sndPool.release();		
	}

}
