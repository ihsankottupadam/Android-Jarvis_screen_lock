package com.izn.lockscreen;
import android.app.*;
import android.os.*;
import android.content.*;
import android.view.View.*;
import android.widget.*;

public class MyService extends Service
{
	@Override
	public void onCreate()
	{
		
		super.onCreate();
		//Toast.makeText(getApplicationContext(),"created",1).show();
	}
	@Override
	public IBinder onBind(Intent p1)
	{
		
		return null;
	}


}
