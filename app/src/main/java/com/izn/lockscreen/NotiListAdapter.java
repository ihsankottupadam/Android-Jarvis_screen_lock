package com.izn.lockscreen;
import android.widget.*;
import android.content.*;
import java.util.*;
import android.view.*;
import android.content.pm.*;
import android.graphics.drawable.*;
import android.icu.text.*;

public class NotiListAdapter extends BaseAdapter {
	Context mContext;
	ArrayList<Noti> mNotiItems;
	public NotiListAdapter(Context context, ArrayList<Noti> NotiItems) {
		mContext = context;
		mNotiItems = NotiItems;
	}
	@Override
	public int getCount() {
		return mNotiItems.size();
	}
	@Override
	public Object getItem(int position) {
		return mNotiItems.get(position);
	}
	@Override
	public long getItemId(int position) {
		return position ;
	}
	@Override
	public View getView( int position, View convertView, ViewGroup parent) {
		View view;
		if (convertView == null) {
			LayoutInflater inflater = (LayoutInflater) mContext.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
			view = inflater.inflate(R.layout.notification_item, null);
		}
		else {
			view = convertView;
		}
		TextView titleView = (TextView) view.findViewById(R.id.Title);
		ImageView iconView = (ImageView) view.findViewById(R.id.icon);
		TextView AppName=(TextView)view.findViewById(R.id.AppName);
		TextView Message=(TextView)view.findViewById(R.id.Message);
		TextView Time=(TextView)view.findViewById(R.id.Time);
		RelativeLayout li=(RelativeLayout)view.findViewById(R.id.not_mainlay);
		
		titleView.setText( mNotiItems.get(position).Title);
		Message.setText(mNotiItems.get(position).Message);
		PackageManager PM=mContext.getPackageManager();
		String PackageName=mNotiItems.get(position).Package;
		try{
			ApplicationInfo info=PM.getApplicationInfo(PackageName,PackageManager.GET_META_DATA);
			String AppNam=(String) PM.getApplicationLabel(info);
			Drawable icon=PM.getApplicationIcon(info);
			AppName.setText(AppNam);
			iconView.setImageDrawable(icon);
		}catch(PackageManager.NameNotFoundException e){
			
		}
		Time.setText("• "+getTimeAgo(mNotiItems.get(position).postedTime));
		return view;
	}
	private static final int SECOND_MILLIS = 1000 ;
	private static final int MINUTE_MILLIS = 60 * SECOND_MILLIS;
	private static final int HOUR_MILLIS = 60 * MINUTE_MILLIS;
	public static String getTimeAgo(long time)
	{
		if (time < 1000000000000L ) {time *= 1000; }
		long now = System.currentTimeMillis();
		final long diff = now - time;
		if(diff<0){
			SimpleDateFormat sdf = new SimpleDateFormat("dd/MM/yyyy");
			return sdf.format(time);
		}
		else if (diff < MINUTE_MILLIS) 
		{ 
			return "now" ; 
		}
		else if (diff < 2 * MINUTE_MILLIS)
		{
			return "1m" ;
		}
		else if (diff < 50 * MINUTE_MILLIS)
		{ 
			return diff / MINUTE_MILLIS + " m" ; 
		}
		else if (diff < 90 * MINUTE_MILLIS) 
		{ return "1h" ; 
		}
		else if (diff < 24 * HOUR_MILLIS)
		{
			return diff / HOUR_MILLIS + " h" ; 
		}
		else if (diff < 48 * HOUR_MILLIS)
		{
			return "Yesterday" ;
		}
		else {
			SimpleDateFormat sdf = new SimpleDateFormat("dd/MM/yyyy");
			return sdf.format(time);
		} 

	}
}
