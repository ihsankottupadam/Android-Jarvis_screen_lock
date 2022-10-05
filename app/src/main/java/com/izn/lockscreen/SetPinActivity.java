package com.izn.lockscreen;

import android.app.*;
import android.content.*;
import android.view.*;
import android.util.*;
import android.os.*;
import android.widget.*;
import java.util.*;
import android.widget.RelativeLayout.*;
import android.view.animation.*;
import android.icu.text.*;
import android.graphics.*;
import android.preference.*;

public class SetPinActivity extends Activity
{
	private RelativeLayout mainRelativeLayout;
	private LinearLayout layout_keypad;

	private ImageView dot1;
	private ImageView dot2;
	private ImageView dot3;
	private ImageView dot4;
	private ImageView img_lo;
	private TextView textResult;
	private TextView textGranted;
    
	private boolean InTimer;
	private Vibrator vibrate;
	private Timer _ltimer=new Timer();
	private Timer _timer = new Timer();
	private TimerTask timer;

	private String password="";
	private String NewPass="";
	private String Act="DISABLE";
	private String ActOg;
	private String CurrentPass="";
	private boolean isPin;
	private SharedPreferences S_pref;
	
	private int wrongTrys;
	private TextView textTimer;

	private TimerTask EnTimer;

	private int sec;

	private TextView txtAm;

	private TextView txtTime;

	private TextView txtDate;

	private Handler handler;

	private Runnable runnable;

	private  boolean use24 = false;
	@Override
	protected void onCreate(Bundle savedInstanceState)
	{
		// TODO: Implement this method
		super.onCreate(savedInstanceState);
		setContentView(R.layout.main);
		
		init();
	}

	private void init()
	{
		mainRelativeLayout=(RelativeLayout)findViewById(R.id.mainRelativeLayout);
		layout_keypad=(LinearLayout)findViewById(R.id.linear_keypad);
		dot1=(ImageView)findViewById(R.id.dot_one);
		dot2=(ImageView)findViewById(R.id.dot_two);
		dot3=(ImageView)findViewById(R.id.dot_three);
		dot4=(ImageView)findViewById(R.id.dot_four);
		img_lo=(ImageView)findViewById(R.id.img_lo);
		textResult=(TextView)findViewById(R.id.txt_result);
		textGranted=(TextView)findViewById(R.id.txt_granted);
		txtTime=(TextView)findViewById(R.id.txtTime);
		txtAm=(TextView)findViewById(R.id.txtAm);
		txtDate=(TextView)findViewById(R.id.txtDate);
		textTimer=(TextView)findViewById(R.id.txt_timer);
		vibrate = (Vibrator) getSystemService(Context.VIBRATOR_SERVICE);
		S_pref= PreferenceManager.getDefaultSharedPreferences(this);
		Handler handlera=new Handler(getMainLooper());
		Runnable runnablea=new Runnable(){
			@Override
			public void run(){
				runOnUiThread(new Runnable() {
						@Override
						public void run() {
							AsyInit.execute();
						}});
			}
		};
		handlera.postDelayed(runnablea,1);
		
	}
	
	private AsyncTask<Void,Void,Void > AsyInit= new AsyncTask<Void, Void, Void>() {

	private RelativeLayout.LayoutParams param;
    @Override
    protected void onPreExecute() {
        super.onPreExecute();
		txtTime.setLineSpacing(0,0);
		txtAm.setLineSpacing(0,0);
		txtTime.setSingleLine();
        Intent callerIntent = getIntent();
		if(callerIntent != null) {
			Bundle extras = callerIntent.getExtras();
			if(callerIntent.hasExtra("ACTION")) {
				Act = extras.getString("ACTION");
				ActOg=Act;
			}
		}
		
    }
		@Override
		protected Void doInBackground(Void... params) {
			DisplayMetrics displaymetrics = new DisplayMetrics();
			getWindowManager().getDefaultDisplay().getMetrics(displaymetrics);
			int loWidth = displaymetrics.widthPixels;
			int height = displaymetrics.heightPixels;
			int loHeight=(int)(loWidth/1.822916);
			txtAm.setY(loHeight/6.2619047f);
			txtAm.setX(loWidth/1.388621f);
			txtTime.setY(loHeight/12.4649047f);
			int p=(int)(loWidth-(loWidth/1.388621f)+2);
			txtTime.setPadding(0,0,p,0);
			txtDate.setY(loHeight/1.2101227f);
			txtDate.setX(loWidth/12);
			float ds=px2sp(loHeight/15);
			txtTime.setTextSize(px2sp(loHeight/6.575f));
			txtAm.setTextSize(px2sp(loHeight/13.15f));
			txtDate.setTextSize(ds);
			
		    param=new RelativeLayout. LayoutParams(loWidth,loHeight);
			
			return null;
		}
    @Override
    protected void onPostExecute(Void aVoid) {
        super.onPostExecute(aVoid);
		img_lo.setLayoutParams(param);
		layout_keypad.setVisibility(View.VISIBLE);
		img_lo.setVisibility(View.VISIBLE);
		Animation keypadAnim=AnimationUtils.loadAnimation(getApplicationContext(),R.anim.load_key);
        layout_keypad.startAnimation(keypadAnim);
		
		
		Typeface face= Typeface.createFromAsset(getAssets(), "fonts/osp_din.ttf");
		textResult.setTypeface(face);
		textGranted.setTypeface(face);
		textTimer.setTypeface(face);
		Typeface face1=Typeface.createFromAsset(getAssets(),"fonts/font.ttf");
		txtAm.setTypeface(face1);
		txtTime.setTypeface(face1);
		txtDate.setTypeface(face1);
	    handler=new Handler(getMainLooper());
		runnable=new Runnable(){
			@Override
			public void run(){
				runOnUiThread(new Runnable() {
						@Override
						public void run() {
				Date date=new Date();
				Calendar cal=Calendar.getInstance();
				String format=use24?"H:mm":"h:mm";
				txtTime.setText(new SimpleDateFormat(format).format(date));
				txtAm.setText(cal.get(Calendar.AM_PM)==0?"AM":"PM");
				txtDate.setText(new SimpleDateFormat("d / M  EEE").format(date).toUpperCase());
				//txtDate.setText(cal.get(Calendar.DAY_OF_MONTH)+" / "+cal.get(Calendar.MONTH)+" "+new SimpleDateFormat("EEE").format(new Date()));
				handler.postDelayed(this,1000);
				}});
			}
		};
		handler.postDelayed(runnable,1);
		if(Act.equals("CHANGE")||Act.equals("DISABLE")){

			startGAnimation("CURRENT PASSWORD");
		}
		else if(Act.equals("SET")) {
			startGAnimation("NEW PASSWORD");
		}
    }
		
  
	
};
	private void setdot()
	{
		int l=password.length();
		if(l==0){
			dot1.setVisibility(View.INVISIBLE);
			dot2.setVisibility(View.INVISIBLE);
			dot3.setVisibility(View.INVISIBLE);
			dot4.setVisibility(View.INVISIBLE);
		}
		if(l==1){
			if(textResult.getVisibility()==View.VISIBLE){textResult.setVisibility(View.INVISIBLE);}
			Animation Anim=AnimationUtils.loadAnimation(getApplicationContext(),R.anim.zoom_out_dot);
			dot1.setVisibility(View.VISIBLE);
			dot1.startAnimation(Anim);
		}
		if(l==2){
			Animation Anim=AnimationUtils.loadAnimation(getApplicationContext(),R.anim.zoom_out_dot);
			dot2.setVisibility(View.VISIBLE);
			dot2.startAnimation(Anim);
		}
		if(l==3){
			Animation Anim=AnimationUtils.loadAnimation(getApplicationContext(),R.anim.zoom_out_dot);
			dot3.setVisibility(View.VISIBLE);
			dot3.startAnimation(Anim);
		}
		if(l==4){
			Animation Anim=AnimationUtils.loadAnimation(getApplicationContext(),R.anim.zoom_out_dot);
			dot4.setVisibility(View.VISIBLE);
			dot4.startAnimation(Anim);
			Act();
		}
	}
	
	private float px2sp(float pxValue){
		final float fontScale = getResources().getDisplayMetrics().scaledDensity;
		return (pxValue-0.5f)/fontScale;
	}
	public void btnClick(View v){
		if(InTimer){return;}
		switch (v.getId()){

			case R.id.bn_1:
				password+="1";
			
				break;
			case R.id.bn_2:
				password+="2";
				break;
				
			case R.id.bn_3:
				password+="3";
				break;
			case R.id.bn_4:
				password+="4";
				break;
			case R.id.bn_5:
				password+="5";
				break;
			case R.id.bn_6:
				password+="6";
				break;
			case R.id.bn_7:
				password+="7";
				break;
			case R.id.bn_8:
				password+="8";
				break;
			case R.id.bn_9:
				password+="9";
				break;
			default:break;
		}
		setdot();
	}
	private void Act(){
		if(Act.equals("Unlock")){
			CurrentPass=S_pref.getString("CurrentPass","");
			if(password.equals(CurrentPass)){
				Animation animation1 =AnimationUtils.loadAnimation(getApplicationContext(), R.anim.zoom_in_short);
				textGranted.setText("ACCESS GRANTED");
				textGranted.setVisibility(View.VISIBLE);
				textGranted.startAnimation(animation1);
				timer = new TimerTask() {
					@Override
					public void run() {
						runOnUiThread(new Runnable() {
								@Override
								public void run() {
									textGranted.setTextColor(0xFF1DD18F);
									timer = new TimerTask() {
										@Override
										public void run() {
											runOnUiThread(new Runnable() {
													@Override
													public void run() {
														finish();
														overridePendingTransition(R.anim.zoom_in_enter, R.anim.zoom_exit);

													}
												});
										}
									};
									_timer.schedule(timer, (int)(100));

								}
							});
					}
				};

				_timer.schedule(timer, (int)(1000));


			}
			else{
				wrongTrys++;
				error();

			}
		}
		else if(Act.equals("SET")){
			NewPass=password;
			startGAnimation("CONFORM PASSWORD");
			Act="CONFORM";
		}
		else if (Act.equals("CONFORM")){
			if(NewPass.equals(password)){
				S_pref.edit().putString("CurrentPass",password).commit();
				S_pref.edit().putString("inited","done").commit();
				S_pref.edit().putBoolean("isPin",true).commit();
				if(ActOg.equals("SET")){ShowMesssage("Password seted");}
				else if(ActOg.equals("CHANGE")){ShowMesssage("Password changed");}
				finish();
			}	
			else{
				textResult.setText("PASSWORD NOT MATCHED");
				error();
			}
		}
		else if(Act.equals("CHANGE")){
			CurrentPass=S_pref.getString("CurrentPass","4286");
			if(CurrentPass.equals(password)){
				Act="SET";
				startGAnimation("NEW PASSWORD");
				wrongTrys=0;
			}
			else{
				textResult.setText("PASSWORD ERROR");
				wrongTrys++;
				error();

			}
		}
		else if(Act.equals("SET_DISABLE")){
			CurrentPass=S_pref.getString("CurrentPass","");
			if(CurrentPass.equals(password)){
				wrongTrys=0;
				this.setResult(RESULT_OK);
				finish();
				}
				else{
					textResult.setText("PASSWORD ERROR");
					wrongTrys++;
					error();
				}
		}
		else if(Act.equals("DISABLE")){
			CurrentPass=S_pref.getString("CurrentPass","");
			if(CurrentPass.equals(password)){
				wrongTrys=0;
				S_pref.edit().putBoolean("isPin",false).commit();
				S_pref.edit().putString("CurrentPass","").commit();
				ShowMesssage("Password disabled");
				finish();
				
			}
			else{
				textResult.setText("PASSWORD ERROR");
				wrongTrys++;
				error();

			}
		}
	}
	private void error(){

		if(textGranted.getVisibility()==View.VISIBLE){
			textGranted.setVisibility(View.INVISIBLE);
		}
		timer = new TimerTask() {
			@Override
			public void run() {
				runOnUiThread(new Runnable() {
						@Override
						public void run() {
							if(!Act.equals("CONFORM")&& wrongTrys>=30){
								startTimer();

							}
							else if(!Act.equals("CONFORM")&&wrongTrys%5==0){
								startTimer();
							}
							else{
								Animation animation1 = 
									AnimationUtils.loadAnimation(getApplicationContext(), R.anim.fed_rep);
								textResult.setVisibility(View.VISIBLE);
								textResult.startAnimation(animation1);

							}
							vibrate.vibrate((long)(600));
							password="";
							setdot();
						}
					});
			}
		};
		_timer.schedule(timer, (int)(800));
	}
	private void startTimer()
	{
		//showeTDialog();
		sec = 14;
		InTimer = true;
		textTimer.setVisibility(View.VISIBLE);
		repeatTimer();
	}

	private void repeatTimer()
	{
		EnTimer = new TimerTask() {
			@Override
			public void run()
			{
				runOnUiThread(new Runnable() {
						@Override
						public void run()
						{
							textTimer.setText("TRY AGAIN IN " + sec + " SECONDS");
							sec--;
							if (sec < 0)
							{
								sec=15;
								InTimer = false;
								textTimer.setVisibility(View.GONE);
								textGranted.setVisibility(View.VISIBLE);
							}
							else
							{
								repeatTimer();
							}
						}
					});
			}
		};
		_ltimer.schedule(EnTimer, 1000);
	}
	private void startGAnimation(String s){
		Animation animation1 =AnimationUtils.loadAnimation(getApplicationContext(), R.anim.zoom_in_short);
		textGranted.setText(s);
		textGranted.setTextColor(0x001DD18F);
		textGranted.setVisibility(View.VISIBLE);
		textGranted.startAnimation(animation1);
		timer = new TimerTask() {
			@Override
			public void run() {
				runOnUiThread(new Runnable() {
						@Override
						public void run() {
							password="";
							setdot();
							textGranted.setTextColor(0xFF1DD18F);
						}
					});
			}
		};
		_timer.schedule(timer, (int)(1000));
	}
	private void ShowMesssage(Object _s){
		Toast.makeText(getApplicationContext(),String.valueOf(_s), Toast.LENGTH_SHORT).show();
	}
	@Override
	protected void onDestroy()
	{
		handler.removeCallbacks(runnable);
		super.onDestroy();
	}
}
