package com.izn.lockscreen;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.service.notification.NotificationListenerService;
import android.service.notification.StatusBarNotification;
import android.util.Log;
import java.text.*;
import android.widget.*;
import android.app.*;
import java.util.*;
import android.graphics.drawable.*;

public class NLService extends NotificationListenerService {
	private String TAG = this.getClass().getSimpleName();
	private NLServiceReceiver nlservicereciver;
	public static final String NOTI_RECIVED="com.ihsan.lock.NOTI_LISTENER";
	public static final String ACTION_SEND_COMMAND="com.ihsan.lock.NOTI_RECIVER";
	private String RemoveKey="";
	@Override
	public void onCreate() {
		super.onCreate();
		nlservicereciver = new NLServiceReceiver();
		IntentFilter filter = new IntentFilter();
		filter.addAction(ACTION_SEND_COMMAND);
		registerReceiver(nlservicereciver,filter);
		
	}

	@Override
	public void onDestroy() {
		super.onDestroy();
		unregisterReceiver(nlservicereciver);
	}

	@Override
	public void onNotificationPosted(StatusBarNotification sbn) {

		try{
		Notification notification=sbn.getNotification();
		String Title=notification.extras.getCharSequence(Notification.EXTRA_TITLE).toString();
		String Message="";
		if(!notification.extras.getCharSequence(Notification.EXTRA_TITLE).equals(null)) {
			Message= notification.extras.getCharSequence(Notification.EXTRA_TEXT).toString();
		}
		
		Intent i = new  Intent(NOTI_RECIVED);
		 i.putExtra("Event","Posted");
		 i.putExtra("Key",sbn.getKey());
		 i.putExtra("Package",sbn.getPackageName());
		 i.putExtra("Title",(Title.equals("null")?"":Title));
		 i.putExtra("Message",Message.equals("null")?" ":Message);
		 i.putExtra("isOngoing",sbn.isOngoing());
		 i.putExtra("Time",sbn.getPostTime());
		 sendBroadcast(i);
		/*Intent i2 = new  Intent(NOTI_RECIVED);
		i2.putExtra("Event","Info");
		i2.putExtra("Info","Removed"+sbn.getKey());
		sendBroadcast(i2);*/
	   }catch(Exception e){
		   Log.e("Errrrrrrrrrrrrrr",e.toString());
	   }
	}
	@Override
	public void onNotificationRemoved(StatusBarNotification sbn) {
		try{
		if(!RemoveKey.equals(sbn.getKey())){
		  Intent i = new  Intent(NOTI_RECIVED);
		  i.putExtra("Event","Removed");
		  i.putExtra("Key",sbn.getKey());
		  sendBroadcast(i);
		}
		else{
		  
		}
		}catch(Exception e){
			
		}
		
		
	}

	class NLServiceReceiver extends BroadcastReceiver{

		@Override
		public void onReceive(Context context, Intent intent) {
			/*Intent il = new  Intent(NOTI_RECIVED);
			il.putExtra("Event","Info");
			il.putExtra("Info","Recived ;"+intent.getStringExtra("Command"));
			sendBroadcast(il);*/
			if(intent.hasExtra("Command")){
			String Command=intent.getStringExtra("Command");
			if(Command.equals("ClearAll")){
				NLService.this.cancelAllNotifications();
			}
			else if(Command.equals("List")){
				int i=1;
				try{
				Intent i4 = new  Intent(NOTI_RECIVED);
				i4.putExtra("Event","ListClear");
				sendBroadcast(i4);
				for (StatusBarNotification sbn : NLService.this.getActiveNotifications()) {
					Notification notification=sbn.getNotification();
					Intent i5 = new  Intent(NOTI_RECIVED);
					String Title=notification.extras.getCharSequence(Notification.EXTRA_TITLE).toString();
					String Message= notification.extras.getCharSequence(Notification.EXTRA_TEXT).toString();
					i5.putExtra("Event","List");
					
					i5.putExtra("Key",sbn.getKey());
					String Package=sbn.getPackageName();
					   
					i5.putExtra("Package",Package);
					i5.putExtra("Title",(Title.equals("null")?"":Title));
					i5.putExtra("Message",(Message.equals("null")?"":Message));
					i5.putExtra("isOngoing",sbn.isOngoing());
					i5.putExtra("Time",sbn.getPostTime());
					sendBroadcast(i5);
					
					i++;
				}
				Intent i2 = new  Intent(NOTI_RECIVED);
				i2.putExtra("Event","ListSet");
				sendBroadcast(i2);
				}catch(Exception e){
					Log.e("Errrrrrrrrrrr on listing",e.toString());
				}
				
			}
			else if(Command.equals("Unlock")){
				context.stopService(new Intent(getApplicationContext(),LockService.class));
				return;
			}
			else if(Command.equals("Remove")){
				RemoveKey=intent.getStringExtra("Key");
				NLService.this.cancelNotification(RemoveKey);
			}

		}}
	}
	private void showMessage(String isOngoing)
	{

		Toast.makeText(getApplicationContext(),isOngoing,Toast.LENGTH_LONG).show();
	}
	private Drawable getNotiIcon(String Package,int id){

		Context remotePackageContext = null; 
		Drawable icon=null;
		try {  
			remotePackageContext = getApplicationContext().createPackageContext(Package, 0);  
			icon = remotePackageContext.getResources().getDrawable(id);  
			return icon;
		} catch (Exception e) {  
			e.printStackTrace();  
			return icon;
		}
	}
}
