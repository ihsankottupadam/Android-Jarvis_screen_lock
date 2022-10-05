package com.izn.lockscreen;
import android.app.*;
import android.content.*;
import android.os.*;
import android.preference.*;
import android.widget.*;

public class MyServiceTmp extends Service
{
	BroadcastReceiver mReciverB;
    IntentFilter intentFilter;
    boolean d;
    BroadcastReceiver reciver;//ey l
    boolean isEnabled;
	private SharedPreferences SPref;

	public void Stop()
	{
		isEnabled = false;
        unregisterReceiver(this.reciver);
		unregisterReceiver(mReciverB);
        stopForeground(true);
        stopSelf();
	}

	@Override
	public void onCreate()
	{
		super.onCreate();
		Toast.makeText(getApplicationContext(),"crated",1).show();
	    SPref= PreferenceManager.getDefaultSharedPreferences(this.getApplicationContext());
		intentFilter = new IntentFilter("android.intent.action.SCREEN_ON");
        intentFilter.addAction("android.intent.action.SCREEN_OFF");
		mReciverB=new ReciverB();
		this.reciver = new BroadcastReceiver(){

            public void onReceive(Context context, Intent intent) {
                if (intent.getAction().equals("SURE_SERVICE_STOP")) {
                   isEnabled=false;
					Stop();
                }
            }
        };
	}
	@Override
	public void onDestroy() {
       
        
		if (isEnabled) {
			unregisterReceiver(mReciverB);
            this.sendBroadcast(new Intent("IZN_LOCK_RESTART_SERVICE"));
        }
		super.onDestroy();
    }
	@Override
	public IBinder onBind(Intent p1)
	{
		
		return null;
	}

	
	@Override
	public int onStartCommand(Intent intent, int n2, int n3) {
		isEnabled= SPref.getBoolean("IS_ENABLED", true);
		/*if(intent.hasExtra("Start")){
			isEnabled=true;
		}*/
		
		
		
        if (isEnabled) {
         /*  if (this.mReciverB.isOrderedBroadcast()) 
            {*/
                this.registerReceiver(mReciverB, intentFilter);
         /*  }
        }
        if (!this.mReciverB.isOrderedBroadcast())
        try {
            this.unregisterReceiver(this.mReciverB);
            return 1;
        }
        catch (Exception exception) {
           
        }*/}
		return super.onStartCommand(intent,n2,n3);
    }
	
	
}
