package com.izn.lockscreen;

import android.os.*;
import android.preference.*;
import android.view.*;
import android.content.*;
import android.provider.*;
import android.widget.*;
import android.app.*;
import java.util.*;

public class SettingsActivity extends PreferenceActivity
{
	Preference change_pass;
	Preference set_pass;
	private SharedPreferences S_pref;
	private boolean isPinSeted=false;
	private Preference dis_sys_lock;

	private PreferenceScreen PrefScreen;

	private Preference noti;

	private Preference Dis_pass;

	private PreferenceScreen passwordPrSc;

	private Preference walp;

	private Preference ENABLED;
	@Override
	protected void onCreate(Bundle savedInstanceState)
	{
		super.onCreate(savedInstanceState);
		addPreferencesFromResource(R.layout.preference);
		Context mContext=this;
		final Intent serviceIntent=new Intent(getApplicationContext(),MyServiceTmp.class);
		S_pref=PreferenceManager.getDefaultSharedPreferences(mContext);
		boolean a=S_pref.getBoolean("IS_ENABLED",true);
		if(a&&(!isMyServiceRunning(mContext,MyServiceTmp.class))){
			startService(serviceIntent);
		}
		
		ENABLED=findPreference("IS_ENABLED");
		change_pass=findPreference("key_change_pswd");
		dis_sys_lock=findPreference("key_disable_sys_lock");
		set_pass=findPreference("key_set_pswd");
		Dis_pass=findPreference("key_disable_pswd");
		noti=findPreference("isNoti");
		PrefScreen=(PreferenceScreen)findPreference("Settings");
		passwordPrSc=(PreferenceScreen)findPreference("Password");
		walp=findPreference("chnge_wlp");
		ENABLED.setOnPreferenceChangeListener(new Preference.OnPreferenceChangeListener(){
			@Override
			public boolean onPreferenceChange(Preference p,Object bool){
			    boolean isEn=Boolean.parseBoolean(bool.toString());
				
				if(isEn){
					startService(serviceIntent.putExtra("Start",""));
				    }
					else{
				    if(isPinSeted){
					Intent i=new Intent(getApplicationContext(),SetPinActivity.class);
					i.putExtra("ACTION","SET_DISABLE");
					startActivityForResult(i,25);
					return false;
					}
					else{
						stopService(serviceIntent);
						return true;
					}
					}
				return true;
			}
		});
		
		Intent i=new Intent(getApplicationContext(),SetPinActivity.class);
		i.putExtra("ACTION","CHANGE");
		change_pass.setIntent(i);
		
		Intent is=new Intent(getApplicationContext(),SetPinActivity.class);
		is.putExtra("ACTION","SET");
		set_pass.setIntent(is);
		
		Intent idisable=new Intent(getApplicationContext(),SetPinActivity.class);
		idisable.putExtra("ACTION","DISABLE");
		Dis_pass.setIntent(idisable);
		
		
		//to remove
		
		Intent wal=new Intent(getApplicationContext(),WallpaperActivity.class);
		walp.setIntent(wal);
		
		
		noti.setOnPreferenceClickListener(new Preference.OnPreferenceClickListener(){
			@Override
			public boolean onPreferenceClick(Preference p){
				startActivityForResult(new Intent("android.settings.ACTION_NOTIFICATION_LISTENER_SETTINGS"),10);
			return true;
			}
		});
		
		Intent var23 = new Intent();
		var23.setAction("android.app.action.SET_NEW_PASSWORD");
		dis_sys_lock.setIntent(var23);
		String isNoti = Settings.Secure.getString((ContentResolver)this.getContentResolver(), (String)"enabled_notification_listeners").contains((CharSequence)this.getApplicationContext().getPackageName()) ? "On" : "Off";
		noti.setSummary(isNoti);
		refreshlock();
	}
	private void refreshlock(){
		isPinSeted=S_pref.getBoolean("isPin",false);
		if(!isPinSeted){
			passwordPrSc.removePreference(change_pass);
			passwordPrSc.addPreference(set_pass);
			Dis_pass.setEnabled(false);
		    
		}
		else{
			passwordPrSc.removePreference(set_pass);
			passwordPrSc.addPreference(change_pass);
			Dis_pass.setEnabled(true);
		}
	}

	@Override
	protected void onResume()
	{
		
		super.onResume();
		refreshlock();
	}
	
	@Override
	public boolean onCreateOptionsMenu(Menu menu)
	{
		MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.pref, menu);
		// TODO: Implement this method
		return super.onCreateOptionsMenu(menu);
	}

	@Override
	public boolean onOptionsItemSelected(MenuItem item)
	{
		switch (item.getItemId()) {
			case R.id.ACTION_PREVIEW:
				startService(new Intent(getApplicationContext(),LockService.class));
				return true;
			default:
				return super.onOptionsItemSelected(item);
				}
			
	}

	@Override
	protected void onActivityResult(int requestCode, int resultCode, Intent data)
	{
		super.onActivityResult(requestCode, resultCode, data);
		if (requestCode == 10) {
			try {
				if (Settings.Secure.getString(this.getContentResolver(), "enabled_notification_listeners").contains(this.getApplicationContext().getPackageName())) {
					findPreference("isNoti").setSummary("On");
					
				} else {
					Toast.makeText(this.getApplicationContext(), "Access Permission needed to show notifications in lock screen when phone is locked", 1).show();
					findPreference("isNoti").setSummary("Off");
				}
			} catch (Exception e) {
				
			}
			}
			if(requestCode==25&&resultCode==RESULT_OK){
				S_pref.edit().putBoolean("IS_ENABLED",false).commit();
				//super.onResume();
			}
			
			}
	public  boolean isMyServiceRunning(Context context, Class<?> serviceClass) {
        ActivityManager activityManager = (ActivityManager) context.getSystemService(Context.ACTIVITY_SERVICE);
        List<ActivityManager.RunningServiceInfo> services = activityManager.getRunningServices(Integer.MAX_VALUE);
        if (services != null) {
            for (int i = 0; i < services.size(); i++) {
                if ((serviceClass.getName()).equals(services.get(i).service.getClassName()) && services.get(i).pid != 0) {
                    return true;
                }
            }
        }
        return false;
    }
	
}
