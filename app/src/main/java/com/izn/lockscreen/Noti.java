package com.izn.lockscreen;
import android.graphics.drawable.*;

public class Noti
{
	String key;
	Drawable icon;
	String Package;
	String Title;
	String Message;
	long postedTime;
	boolean isOngoing;
	public Noti(String key,Drawable icon,String Package,String Title,String Message,boolean isOngoing,long postedTime){
		this.key=key;
		this.icon= icon;
		this.Package=Package;
		this.Title=Title;
		this.Message=Message;
		this.isOngoing=isOngoing;
		this.postedTime=postedTime;
	}
}
