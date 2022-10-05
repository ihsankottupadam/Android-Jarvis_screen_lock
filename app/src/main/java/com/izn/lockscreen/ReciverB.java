package com.izn.lockscreen;
import android.content.*;
import android.telephony.*;

public class ReciverB extends BroadcastReceiver
{

	@Override
	public void onReceive(Context mContext, Intent intent)
	{
		if (intent.getAction().equals("android.intent.action.SCREEN_ON")) {
			mContext.startService(new Intent(mContext, LockService.class));
		} else {
			/*if (intent.getAction().equals("android.intent.action.USER_PRESENT")) {
				mContext.stopService(new Intent(mContext, LockService.class));
				return;
			}*/
			
			if (intent.getAction().equals("android.intent.action.SCREEN_OFF")) {
				if (((TelephonyManager)mContext.getSystemService("phone")).getCallState() == 2) {
					mContext.stopService(new Intent(mContext, LockService.class));
					//listen call end and restart
					
				}
				else{
					mContext.startService(new Intent(mContext, LockService.class));
					return;
				}
			}
		}

	
		
	}
}
