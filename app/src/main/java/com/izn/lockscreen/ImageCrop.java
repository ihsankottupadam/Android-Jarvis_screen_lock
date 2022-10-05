package com.izn.lockscreen;


import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.Paint;
import android.graphics.Color;
import android.graphics.ColorFilter;
import android.graphics.LightingColorFilter;
import android.graphics.Matrix;
import android.net.*;
import android.os.*;
import android.renderscript.*;
import android.support.v7.app.*;
import android.util.*;
import android.view.*;
import android.widget.*;
import com.theartofdev.edmodo.cropper.*;
import java.io.*;

public class ImageCrop extends AppCompatActivity
{

	private int DisHeight;
	
	private int DisWidth;
	CropImageView cropView;
	private String path;
	private boolean isBlur=true;
	@Override
	protected void onCreate(Bundle savedInstanceState)
	{
		
		super.onCreate(savedInstanceState);
		setContentView(R.layout.image_crop_activity);
		DisplayMetrics displayMetrics = new DisplayMetrics();
        this.getWindowManager().getDefaultDisplay().getMetrics(displayMetrics);
        DisWidth = displayMetrics.widthPixels;
        DisHeight = displayMetrics.heightPixels;
	    path=this.getIntent().getStringExtra("path");
		float Ratio=DisHeight/DisWidth;
		
	    Bitmap bitmapOriginal=BitmapFactory.decodeFile(path);
		if(bitmapOriginal.getHeight()/bitmapOriginal.getWidth()==Ratio){
			SaveImage(bitmapOriginal);
		}
		else{
			Uri uri=Uri.fromFile(new File(path));
			cropView=new CropImageView(this);
			cropView.setBackgroundColor(0xFF000000);
			cropView.setImageUriAsync(uri);
			cropView.setAspectRatio(5,8);
			cropView.setFixedAspectRatio(true);
			addContentView(cropView, (ViewGroup.LayoutParams)new RelativeLayout.LayoutParams(-1, -1));
			
		}
		
		
	}
	@Override
	public boolean onCreateOptionsMenu(Menu menu)
	{
		MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.crop, menu);
		return super.onCreateOptionsMenu(menu);
	}

	@Override
	public boolean onOptionsItemSelected(MenuItem item)
	{
		int id=item.getItemId();
		if(id==R.id.ACTION_DONE){
			Toast.makeText(ImageCrop.this,cropView.getCropRect().toString()+"",Toast.LENGTH_SHORT).show();
			SaveImage(cropView.getCroppedImage());
			
			
		}
		else if(id==android.R.id.home){
			super.onBackPressed();
		}
		return super.onOptionsItemSelected(item);
	}
	public void SaveImage(final Bitmap bitmapOg) {
        new AsyncTask<Void, Void, Void>(){

			
			@Override
			protected void onPreExecute() {
                Toast.makeText(ImageCrop.this,"Saving..", Toast.LENGTH_SHORT).show();
            }
			@Override
			protected Void doInBackground(Void... params){
			Bitmap bitmap=bitmapOg;
				try{
					if(isBlur){
						bitmap=createBitmap_ScriptIntrinsicBlur(bitmap, 1.0f);
					}
		            if(!isBitmapDark(bitmap)){
					    bitmap=darkenBitMap(bitmap,0xFF7F7F7F);
					}
					else{bitmap=darkenBitMap(bitmap,0xFFBBBBBB);}
					File file = new File(getFilesDir() + "/Wallpaper.jpg");
					bitmap.compress(Bitmap.CompressFormat.JPEG, 100, (OutputStream)new FileOutputStream(file));
				}catch(IOException e){

				}
				return null;
			}
			@Override
			protected void onPostExecute(Void vois){
			   ImageCrop. this.setResult(RESULT_OK);
               ImageCrop. this.finish();
				
			}
			
		}.execute();
		
	}
	private Bitmap createBitmap_ScriptIntrinsicBlur(Bitmap src, float r) {   //Radius range (0 < r <= 25)   
		if(r <= 0){    r = 0.1f;   }else if(r > 25){    r = 25.0f;   }
		Bitmap bitmap = Bitmap.createBitmap(     src.getWidth(), src.getHeight(),     Bitmap.Config.ARGB_8888);
		RenderScript renderScript = RenderScript.create(this);
		Allocation blurInput = Allocation.createFromBitmap(renderScript, src);   Allocation blurOutput = Allocation.createFromBitmap(renderScript, bitmap);
		ScriptIntrinsicBlur blur = ScriptIntrinsicBlur.create(renderScript,     Element.U8_4(renderScript));   blur.setInput(blurInput);   blur.setRadius(r);   blur.forEach(blurOutput);
		blurOutput.copyTo(bitmap);   renderScript.destroy();   return bitmap;  }
		private Bitmap darkenBitMap(Bitmap bm,int mul) {

		Canvas canvas = new Canvas(bm);
		Paint p = new Paint(Color.RED);
		//ColorFilter filter = new LightingColorFilter(0xFFFFFFFF , 0x00222222); // lighten
		ColorFilter filter = new LightingColorFilter(mul, 0x00000000);    // darken
		p.setColorFilter(filter);
		canvas.drawBitmap(bm, new Matrix(), p);

		return bm;
	}
	private Boolean isBitmapDark(Bitmap bitmap){
		Bitmap newBitmap = Bitmap.createScaledBitmap(bitmap, 1, 1, true);
		final int color = newBitmap.getPixel(0, 0);
		newBitmap.recycle();
		double darkness = 1-(0.299*Color.red(color) + 0.587*Color.green(color) + 0.114*Color.blue(color))/255;
		if(darkness<0.6){
			return false; 
		}else{
			return true; 
		}
	}
	
	
}
