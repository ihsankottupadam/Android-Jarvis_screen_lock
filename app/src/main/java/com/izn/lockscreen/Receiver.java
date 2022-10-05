package com.izn.lockscreen;
import android.content.*;
import android.widget.*;
import android.app.*;
import android.preference.*;

public class Receiver extends BroadcastReceiver
{

	private Context mContext;
	private SharedPreferences S_pref;

	private  static final String KEY_ISENABLED="String";

	
	@Override
	public void onReceive(Context context, Intent intent)
	{
		mContext=context;
		S_pref=PreferenceManager.getDefaultSharedPreferences(mContext);
		String action=intent.getAction();
	    if(S_pref.getBoolean("IS_ENABLED", true)){
            if (action.equals(Intent.ACTION_BOOT_COMPLETED)||action.equals(Intent.ACTION_SCREEN_OFF)) {
				if(true){
					Intent s = new  Intent(context,LockService.class);
					context.startService(s);
					context.startService(new Intent(mContext,MyService.class));
				}
				
            }
			if(action.equals("IZN_LOCK_RESTART_SERVICE")){
			  context.startService(new Intent(mContext,MyService.class));
			}
			}
			
		
		// TODO: Implement this method
	}

	private void showMessage(String action)
	{
	  Toast.makeText(mContext,action,Toast.LENGTH_SHORT).show();
		// TODO: Implement this method
	}
	private boolean isLockEn(){
		if(S_pref.getBoolean(KEY_ISENABLED,false)){
			return true;
		}
		return true;
	}
	
}
