package com.tdh.Sup;

import android.content.Context;
import android.os.Vibrator;

public class VibrateManager
{
	Context context;
	Vibrator v;
	
	@SuppressWarnings("static-access")
	public VibrateManager(android.content.Context Context)
	{
		this.context=Context;		
		v = (Vibrator) this.context.getSystemService(Context.VIBRATOR_SERVICE);
	}	
	public void Vibtime(long miliS)
	{
		v.vibrate(miliS);
	}
}
