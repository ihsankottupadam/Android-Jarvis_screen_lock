package com.izn.lockscreen;
import android.widget.*;
import android.util.*;
import java.security.interfaces.*;
import java.util.*;
import android.content.*;
import android.app.*;
import android.view.*;
import android.graphics.*;
import android.content.res.*;
import java.io.*;

public class wallpaper_adapter extends BaseAdapter
{
	DisplayMetrics DisM;
	Context mContex;
	int DisWidth;
	int DisHeight;
	Activity activity;
	private ArrayList<ItemWal> Items;

	
	public wallpaper_adapter(Activity activity,ArrayList<ItemWal> Items){
		//this.mContex=context;
		this.activity=activity;
		this.Items=Items;
		this.DisM =activity.getApplicationContext().getResources().getDisplayMetrics();
        activity.getWindowManager().getDefaultDisplay().getMetrics(this.DisM);
        this.DisHeight = this.DisM.heightPixels;
        this.DisWidth = this.DisM.widthPixels;
	    
		Toast.makeText(activity,String.valueOf( DisWidth),Toast.LENGTH_SHORT).show();
	}
	public int getCount() {
        return this.Items.size();
    }

    public Object getItem(int id) {
        return this.Items.get(id);
    }

    public long getItemId(int id) {
        return id;
    }
	public View getView(int position, View convertView, ViewGroup parent) {
		View view;
		if (convertView == null) {
			LayoutInflater inflater = (LayoutInflater) activity.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
			view = inflater.inflate(R.layout.item_theme, null);
		}
		else {
			view = convertView;
		}
		try{
		ImageView thumb=(ImageView)view.findViewById(R.id.image);
		thumb.setLayoutParams(new LinearLayout.LayoutParams(LinearLayout.LayoutParams.MATCH_PARENT,320 * DisHeight / 800));
		Bitmap bi=decodeSampleBitmapfromAsset(Items.get(position).path,320 * DisWidth / 800,320 * DisHeight / 800);
		//Bitmap bi=decodeSampledBitmapFromFile(Items.get(position).path,320 * DisWidth / 800,320 * DisHeight / 800);
		thumb.setImageBitmap(bi);
		}
		catch(Exception e){
			Log.i("Wallpapers loading failed",e.toString());
		}
		return view;
	}
	static Bitmap decodeSampledBitmapFromFile(String fileName, int reqWidth,int reqHeight) {
        final BitmapFactory.Options options = new BitmapFactory.Options();
        options.inJustDecodeBounds = true;
		//BitmapFactory.decodeFile(fileName);
        options.inSampleSize = calculateInSampleSize(options, reqWidth, reqHeight);
        options.inJustDecodeBounds = false;
        return BitmapFactory.decodeFile(fileName, options);
    }
    private Bitmap decodeSampleBitmapfromAsset(String fileName,int reqWidth,int reqHeight){
		final BitmapFactory.Options options = new BitmapFactory.Options();
        options.inJustDecodeBounds = true;
		//BitmapFactory.decodeFile(fileName);
        options.inSampleSize = calculateInSampleSize(options, reqWidth,reqHeight);
        options.inJustDecodeBounds = false;
		AssetManager assetManager = activity.getAssets();
		try{
		InputStream inputStream = assetManager.open(fileName);
		Rect a =new Rect(0,0,0,0);
		
		return BitmapFactory.decodeStream(inputStream,a,options);
			
		}catch(Exception e){
			Toast.makeText(mContex,e.toString(),1).show();
		}
		return null;
	}
    static int calculateInSampleSize( BitmapFactory.Options options, int reqWidth, int reqHeight) {
        int inSampleSize = 1;   //Default subsampling size
        
        if (options.outHeight > reqHeight || options.outWidth > reqWidth) {
            final int halfHeight = options.outHeight / 2;
            final int halfWidth = options.outWidth / 2;
            // Calculate the largest inSampleSize value that is a power of 2 and keeps both
            // height and width larger than the requested height and width.
            while ((halfHeight / inSampleSize) > reqHeight
				   && (halfWidth / inSampleSize) > reqWidth) {
                inSampleSize *= 2;
            }
        }
        return inSampleSize;
    }
}
