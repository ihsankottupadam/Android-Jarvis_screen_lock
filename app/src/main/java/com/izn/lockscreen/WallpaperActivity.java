package com.izn.lockscreen;
import android.app.*;
import android.os.*;
import android.widget.*;
import android.content.res.*;
import java.io.*;
import java.util.*;
import android.view.*;
import android.content.*;
import android.preference.*;
import android.provider.*;
import android.net.*;
import android.database.*;
import android.hardware.display.*;
import android.util.*;
import android.graphics.*;

public class WallpaperActivity extends Activity
{

	private GridView grideView;

	private ArrayList<ItemWal> items;

	@Override
	protected void onCreate(Bundle savedInstanceState)
	{
		
		super.onCreate(savedInstanceState);
		setContentView(R.layout.theme_activity);
		init();
	}

	private void init()
	{
		grideView=(GridView)findViewById(R.id.gride_wallpapers);
		grideView.setOverScrollMode(2);
	    items=new ArrayList<ItemWal>();
		try{
		AssetManager assetManager = this.getAssets();
        String[] arrstring = assetManager.list("backgrounds");
		for(int i=0;i<arrstring.length;i++){
			String name=arrstring[i];
			items.add(new ItemWal("backgrounds/"+name));
		}
		
		}catch(IOException e){
			
		}
		//String filesPath= getFilesDir().getAbsolutePath()+"/Wallpapers";
		//Toast.makeText(WallpaperActivity.this,String.valueOf(filesPath),Toast.LENGTH_SHORT).show();
		/*File file = new File("/storage/emulated/0/backgrounds");
        if (!file.exists()) {
            file.mkdirs();
        }
		for (File file2 : file.listFiles()) {
            String string = file2.getAbsolutePath();
            items.add(new ItemWal( string));
        }*/
		
		
		wallpaper_adapter walAdapter=new wallpaper_adapter(this,items);
		grideView.setAdapter(walAdapter);
		grideView.setOnItemClickListener(new AdapterView.OnItemClickListener(){
				public void onItemClick(AdapterView<?> adapterView, View view, int position, long l2) {
					
						SharedPreferences.Editor editor = PreferenceManager.getDefaultSharedPreferences(getApplicationContext()).edit();
						editor.putString("WallpaperPath", items.get(position).path);
						editor.putBoolean("isWallfromAsset",true);
						editor.commit();
						Toast.makeText(getApplicationContext(), (CharSequence)"LockScreen wallpaper changed", 0).show();
						finish();
						return;
				}
		});
	}

	@Override
	protected void onActivityResult(int requestCode, int resultCode, Intent data)
	{
		if(resultCode==RESULT_OK &&requestCode==25&&data!=null){
		Uri uri = data.getData();
		try{
		String path = path(uri);
		Intent intent2 = new Intent(this.getApplicationContext(),ImageCrop.class);
		intent2.putExtra("path",path);
		this.startActivityForResult(intent2, 30);
		
		}catch(Exception e){
			Toast.makeText(WallpaperActivity.this,"Error setting gallry background\n try again",Toast.LENGTH_SHORT).show();
		}
		}
		else if (requestCode == 30 && resultCode == RESULT_OK) {
            SharedPreferences.Editor editor = PreferenceManager.getDefaultSharedPreferences(this.getApplicationContext()).edit();
			editor.putString("WallpaperPath", this.getFilesDir()+ "/Wallpaper.jpg");
			editor.putBoolean("isWallfromAsset", false);
			editor.commit();
			Toast.makeText(this, "Wallpaper has been set", Toast.LENGTH_SHORT).show();
			this.finish();
        }
		super.onActivityResult(requestCode, resultCode, data);
		
	}
	@Override
	public boolean onCreateOptionsMenu(Menu menu)
	{
		MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.wallpapers, menu);
		return super.onCreateOptionsMenu(menu);
	}

	@Override
	public boolean onOptionsItemSelected(MenuItem item)
	{
		if(item.getItemId()==R.id.ACTION_PICK_FROM_GALLERY){
			this.startActivityForResult(new Intent("android.intent.action.PICK", MediaStore.Images.Media.EXTERNAL_CONTENT_URI), 25);
			return true;
		}
		if(item.getItemId()==android.R.id.home){
			finish();
		}
		return super.onOptionsItemSelected(item);
	}
	public String path(Uri uri) {
        if (uri == null) {
            return null;
        }
        Cursor cursor = this.managedQuery(uri, new String[]{"_data"}, null, null, null);
        if (cursor != null) {
            int n = cursor.getColumnIndexOrThrow("_data");
            cursor.moveToFirst();
            return cursor.getString(n);
        }
        return uri.getPath();
    }
	}

	
