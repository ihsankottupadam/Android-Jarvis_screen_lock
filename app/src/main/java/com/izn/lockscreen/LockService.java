package com.izn.lockscreen;

import android.animation.*;
import android.app.*;
import android.content.*;
import android.content.pm.*;
import android.graphics.*;
import android.graphics.drawable.*;
import android.media.*;
import android.os.*;
import android.preference.*;
import android.provider.*;
import android.telephony.*;
import android.util.*;
import android.view.*;
import android.view.View.*;
import android.view.animation.*;
import android.widget.*;
import java.io.*;
import java.text.*;
import java.util.*;;

public class LockService extends Service
{

	
	private LayoutInflater inflate;

	private RelativeLayout li;

	private WindowManager wm;

	private View overlayedButton;

	private ImageView btn_unlock;
	
	private SharedPreferences S_pref;


	
	private ImageView btn_call;

	private ImageView btn_camra;

	private TextView txtInfo;

	private LinearLayout LBtn_Unlock;

	private ImageView ImgATop;

	private TextView txtTime;

	private TextView txtAm;
	private TextView txtDate;
	private TextView textResult;
	private TextView textGranted;
	private TextView textTimer;
	private Handler handler;
	private Vibrator vibrate;
    private Context mContext;
	private Runnable runnable;
    float DisplayWidth,loHeight;

	private boolean use24;
	public static final String NOTI_RECIVED="com.ihsan.lock.NOTI_LISTENER";
	public static final String ACTION_SEND_COMMAND="com.ihsan.lock.NOTI_RECIVER";
	
	private ListView ListNoti;
	private ArrayList<Noti> notItems=new ArrayList<Noti>();
	public static ArrayList<Drawable> notiIcons=new ArrayList<Drawable>();
	private NotiListAdapter Notiadapter;

	private LockService.LockNotiReceiver nlservicereciver;

	private RelativeLayout liHomeBase;

	private LinearLayout liBottombtns;
	private int CState=0;

	private ImageView btnClose;

	private RelativeLayout liPinBase;

	private TextView txtCharg;

	private RelativeLayout liMusic;
	private ImageButton btnPlay;
	public static final String SERVICECMD = "com.android.music.musicservicecommand";
	public static final String CMDNAME = "command";
	public static final String CMDTOGGLEPAUSE = "togglepause";
	public static final String CMDSTOP = "stop";
	public static final String CMDPAUSE = "pause";
	public static final String CMDPLAY = "play";
	public static final String CMDPREVIOUS = "previous";
	public static final String CMDNEXT = "next";

	private boolean isPin;

	private LinearLayout ListBNot;

	private ArrayList<Noti> mainNoties;
	private boolean isStatusBar;

	private ImageView batIndicator;

	private ImageView sigIndicator;
	@Override
	public void onCreate()
	{
		
		super.onCreate();
		init();
		initialise();
		mContext=this;
		btnClose.setOnClickListener(new View.OnClickListener(){
				@Override
				public void onClick(View _v) { 
					if(CState==1){
						backHome();
					}else{
				   stops();
				}
				}
			});
	
		btn_call.setOnTouchListener(apptouchLustener);
	    btn_camra.setOnTouchListener(apptouchLustener);
		liHomeBase.setOnTouchListener(OnSwipeListener);
		WindowManager.LayoutParams par=new WindowManager.LayoutParams();
		par.width=WindowManager.LayoutParams.MATCH_PARENT;
		par.height=WindowManager.LayoutParams.MATCH_PARENT;
		par.type=WindowManager.LayoutParams.TYPE_SYSTEM_ALERT;
		par.format=PixelFormat .TRANSLUCENT ;
		par.gravity=80;
		par.screenOrientation = ActivityInfo.SCREEN_ORIENTATION_PORTRAIT;
		par.flags=1280;
		
		/*WindowManager .LayoutParams params
			= new WindowManager.LayoutParams
		(WindowManager .LayoutParams .MATCH_PARENT,
		 WindowManager .LayoutParams .MATCH_PARENT,
		 WindowManager .LayoutParams . TYPE_SYSTEM_ALERT ,
		 WindowManager .LayoutParams . FLAG_SHOW_WHEN_LOCKED|
		 WindowManager.LayoutParams.FLAG_FULLSCREEN,
		 PixelFormat .TRANSLUCENT );
		params .gravity =
			Gravity.LEFT | Gravity .TOP;
		params .x = 0 ;
		params .y = 0 ;*/
		wm .addView(overlayedButton, par);
		
	}

	
	private void init(){
		wm = (WindowManager) getSystemService(Context .WINDOW_SERVICE);
		inflate = ( LayoutInflater ) getBaseContext().getSystemService(Context.LAYOUT_INFLATER_SERVICE);
		overlayedButton = inflate.inflate(R.layout.lock_screen, null);
		li =  ( RelativeLayout) overlayedButton.findViewById(R.id.lock_screenRelativeLayout);
		btn_unlock=(ImageView)overlayedButton.findViewById(R.id.btn_unlock);
		btn_camra=(ImageView)overlayedButton.findViewById(R.id.app_camera);
		btn_call=(ImageView)overlayedButton.findViewById(R.id.app_call);
		LBtn_Unlock=(LinearLayout)overlayedButton.findViewById(R.id.linear_Unlock);
		ImgATop=(ImageView)overlayedButton.findViewById(R.id.imgAngleTop);
		txtTime=(TextView)overlayedButton.findViewById(R.id.txtTime);
		txtAm=(TextView)overlayedButton. findViewById(R.id.txtAm);
		txtDate=(TextView)overlayedButton. findViewById(R.id.txtDate);
		vibrate = (Vibrator) getSystemService(Context.VIBRATOR_SERVICE);
		S_pref= PreferenceManager.getDefaultSharedPreferences(this);
		liHomeBase=(RelativeLayout)overlayedButton.findViewById(R.id.liHome);
		liBottombtns=(LinearLayout)overlayedButton.findViewById(R.id.liBottombtns);
		liPinBase=(RelativeLayout)overlayedButton.findViewById(R.id.liPinBase);
		ListNoti=(ListView)overlayedButton.findViewById(R.id.notiList);
		ListBNot=(LinearLayout)overlayedButton.findViewById(R.id.exNoti);
		textGranted=(TextView)overlayedButton.findViewById(R.id.txt_granted);
		textResult=(TextView)overlayedButton.findViewById(R.id.txt_result);
		textTimer=(TextView)overlayedButton. findViewById(R.id.txt_timer);
		txtCharg=(TextView)overlayedButton.findViewById(R.id.txtCharging);
		layout_keypad=(LinearLayout)overlayedButton. findViewById(R.id.linear_keypad);
		dot1=(ImageView)overlayedButton. findViewById(R.id.dot_one);
		dot2=(ImageView)overlayedButton.findViewById(R.id.dot_two);
		dot3=(ImageView)overlayedButton.findViewById(R.id.dot_three);
		dot4=(ImageView)overlayedButton.findViewById(R.id.dot_four);
		img_lo=(ImageView)overlayedButton.findViewById(R.id.img_lo);
		liMusic=(RelativeLayout)overlayedButton.findViewById(R.id.musicWidget);
		btnClose=(ImageView)overlayedButton.findViewById(R.id.closeBtn);
		txtInfo=(TextView)overlayedButton.findViewById(R.id.info);
		batIndicator=(ImageView)overlayedButton.findViewById(R.id.intBat);
		sigIndicator=(ImageView)overlayedButton.findViewById(R.id.intSig);
		btnPlay=(ImageButton)overlayedButton. findViewById(R.id.btnPlay);
		
		
	}
	private void initialise(){
		Typeface timeFace= Typeface.createFromAsset(getAssets(), "fonts/font.ttf");
		txtTime.setTypeface(timeFace);
		txtAm.setTypeface(timeFace);
		txtDate.setTypeface(timeFace);
		//registerRecivers
		nlservicereciver = new LockNotiReceiver();
		IntentFilter filter = new IntentFilter();
		filter.addAction(NOTI_RECIVED);
		registerReceiver(nlservicereciver,filter);
		this.registerReceiver(this.BatInfoReceiver, new IntentFilter(Intent.ACTION_BATTERY_CHANGED));
		IntentFilter iF = new IntentFilter();	
		iF.addAction("com.android.music.metachanged");
		iF.addAction("com.android.music.playstatechanged");
		iF.addAction("com.android.music.playbackcomplete");
		iF.addAction("com.android.music.queuechanged");
		registerReceiver(musicReceiver, iF);
		Intent i=new Intent(ACTION_SEND_COMMAND);
		i.putExtra("Command","List");
		sendBroadcast(i);
		ListNoti.setVisibility(View.VISIBLE);
        SwipeDismissListViewTouchListener touchListener =
			new SwipeDismissListViewTouchListener(
			ListNoti,
			new SwipeDismissListViewTouchListener.DismissCallbacks() {
				@Override
				public boolean canDismiss(int position) {
					
					return !notItems.get(position).isOngoing;
				}

				@Override
				public void onDismiss(ListView listView, int[] reverseSortedPositions) {
					for (int position : reverseSortedPositions) {
						Intent i=new Intent(NLService.ACTION_SEND_COMMAND);
						i.putExtra("Command","Remove");
						i.putExtra("Key",notItems.get(position).key);
						sendBroadcast(i);
						notItems.remove(position);
						setNotilis();
					}

				}
			});
       ListNoti.setOnTouchListener(touchListener);
		setSizes();
		li.setOnTouchListener(new OnTouchListener(){

				private long startClickTime;
				public boolean onTouch(View v , MotionEvent event ) {
					if (event . getAction() ==
						MotionEvent .ACTION_DOWN ) {
						startClickTime =System .currentTimeMillis();

					} else if (event . getAction() ==
							   MotionEvent .ACTION_MOVE) {
						return true;
					} else if (event . getAction() ==
							   MotionEvent .ACTION_UP ) {
						if(System .currentTimeMillis() - startClickTime <
						   ViewConfiguration.getTapTimeout()) {
							startUpAngleAnim();

						}

					}
					return false ;
				}

			});
	}

	@Override
	public int onStartCommand(Intent intent, int flags, int startId)
	{
	    updateValues();
		return super.onStartCommand(intent, flags, startId);
	}
	
	private void updateValues(){
		
		String Wall=S_pref.getString("WallpaperPath","backgrounds/wallpaper1.jpg");
		LinearLayout StatusBar=(LinearLayout)overlayedButton.findViewById(R.id.statusBar);
		isStatusBar=S_pref.getBoolean("isStatuBar",false);
		if(isStatusBar){
			
			StatusBar.setLayoutParams(new RelativeLayout.LayoutParams(RelativeLayout.LayoutParams.MATCH_PARENT,getStatusBarHeight()));
			batIndicator.setVisibility(View.GONE);
			sigIndicator.setVisibility(View.GONE);
		}
		else{
			
			StatusBar.setLayoutParams(new RelativeLayout.LayoutParams(RelativeLayout.LayoutParams.MATCH_PARENT,0));
			batIndicator.setVisibility(View.VISIBLE);
			sigIndicator.setVisibility(View.VISIBLE);
			phoneListener b= new phoneListener();
			((TelephonyManager)this.getApplicationContext().getSystemService("phone")).listen((PhoneStateListener)b, 256);

		}
		
		if(Wall.equals("backgrounds/wallpaper1.jpg")&&isStatusBar){
			StatusBar.setBackgroundColor(0xFF000000);
			RelativeLayout bg=(RelativeLayout)overlayedButton.findViewById(R.id.li_base);
			bg.setBackgroundResource(R.drawable.bg);
		}
		else if(S_pref.getBoolean("isWallfromAsset",true)){
			li.setBackgroundDrawable(loadDrawableFromAssets(this,Wall));
		}
		else{
			li.setBackground(Drawable.createFromPath(Wall));
		}
		isPin=S_pref.getBoolean("isPin",false);
		use24=S_pref.getBoolean("key_use_24",false);
	    liMusic.setVisibility(isMusicActive()?View.VISIBLE:View.GONE);
		updatePlayButton();
		if(CState==1){
			backHome();
		}
	}
	public Drawable loadDrawableFromAssets(Context context, String path)
	{
		InputStream stream = null;
		try
		{
			stream = context.getAssets().open(path);
			return Drawable.createFromStream(stream, null);
		}
		catch (Exception ignored) {} finally
		{
			try
			{
				if(stream != null)
				{
					stream.close();
				}
			} catch (Exception ignored) {}
		}
		return null;
	}
	@Override
	public IBinder onBind(Intent p1)
	{
		// TODO: Implement this method
		return null;
	}
	@Override
	public void onDestroy()
	{
		// TODO: Implement this method
		super.onDestroy();
		if (overlayedButton != null) {
			wm .removeView(overlayedButton);
			overlayedButton = null;
		}
		unregisterReceiver(BatInfoReceiver);
		unregisterReceiver(nlservicereciver);
		unregisterReceiver(musicReceiver);
	}
	private OnTouchListener OnSwipeListener=new OnTouchListener(){
		float pY=0;
		float cY=0;
		private long startClickTime;
		public boolean onTouch(View view, MotionEvent event) {

			switch (event.getAction()) {

				case MotionEvent.ACTION_DOWN:
					startClickTime =System .currentTimeMillis();
					pY = event.getRawY();
					ImgATop.setVisibility(View.VISIBLE);
					liBottombtns.setAlpha(0f);
					btn_unlock.setImageResource(R.drawable.ic_unlock_blue);
					txtCharg.setAlpha(0f);
					//dY = view.getY() - event.getRawY();
					break;

				case MotionEvent.ACTION_MOVE:
					if(pY==0){
						pY=event.getRawY();
					}
				   cY=event.getRawY();
					if(cY<pY){
						liHomeBase.animate()
						.y(cY-pY)
						.setDuration(0)
						.start();
						
					}
					
					break;
				case MotionEvent.ACTION_UP:
					if(System .currentTimeMillis() - startClickTime <
					   ViewConfiguration.getTapTimeout()) {
						startUpAngleAnim();
					}
					if((pY- cY)<(liHomeBase.getHeight()/6)){
						animateBack();
					}
					else{
						if(isPin){
							openPiScreen();}
						else{
							CState=1;
							liHomeBase.animate()
								.y(0-liHomeBase.getHeight())
								.setDuration(100)
								.start();
							stops();}
					}
					
					
					break;
				default:
					return false;
			}
			return true;
		}
	};
	
	private void animateBack(){
		liHomeBase.animate().
		y(0)
		.setDuration(200)
		.start();
		liBottombtns.animate().alpha(1f).setStartDelay(200).start();
		btn_unlock.setImageResource(R.drawable.ic_lock_blue);
		txtCharg.setAlpha(1f);
	}
	private void openPiScreen(){
		CState=1;
		liHomeBase.animate()
		.y(0-liHomeBase.getHeight())
		.setDuration(100)
		.start();
		OpenPinScreen();
	}
	private void backHome(){
		liHomeBase.setY(0f);
		liHomeBase.setVisibility(View.VISIBLE);
		liPinBase.setVisibility(View.INVISIBLE);
		btn_unlock.setImageResource(R.drawable.ic_lock_blue);
		textGranted.setVisibility(View.GONE);
		textResult.setVisibility(View.GONE);
		liBottombtns.setAlpha(1f);
		img_lo.setAlpha(0f);
		SetHomeTimes();
		CState=0;
		
	}
	
	/*public boolean dispatchKeyEvent(KeyEvent event)
	{
		if (event.getKeyCode() == KeyEvent.KEYCODE_BACK)
		{
		info("back");
			// if (event.getAction() == KeyEvent.ACTION_DOWN)
			return true;
		}
		return false;
	}*-
	/*
	public boolean dispatchKeyEvent(KeyEvent event) {
		txtInfo.setText(txtInfo.getText().toString()+event.toString());
		if ((event.getKeyCode() == KeyEvent.KEYCODE_BACK)) {
			if(CState==1){
				txtInfo.setText(txtInfo.getText().toString()+"back");
				backHome();
			}
			return true;
		}
		return false;
	}*/
	float dX, dY;
	private OnTouchListener apptouchLustener=new OnTouchListener(){
	float cY,pY;
		private long startClickTime;

		private float pX;
	public boolean onTouch(View view, MotionEvent event) {
		
		switch (event.getAction()) {

			case MotionEvent.ACTION_DOWN:
				startClickTime =System .currentTimeMillis();
				dX = view.getX() - event.getRawX();
				dY = view.getY() - event.getRawY();
				pY=view.getY();
				pX=view.getX();
				break;

			case MotionEvent.ACTION_MOVE:

				view.animate()
                   // .x(event.getRawX() + dX)
                    .y(event.getRawY() + dY)
                    .setDuration(0)
                    .start();
				break;
			case MotionEvent.ACTION_UP:
				cY=view.getY();
				if(System .currentTimeMillis() - startClickTime <
				   ViewConfiguration.getTapTimeout()) {
				  if (view.getAnimation()==null){
					   Animation upApp=AnimationUtils.loadAnimation(mContext,R.anim.app_up);
					   view.startAnimation(upApp);
				   }
				   
				}
				if(pY-cY>view.getHeight()){
					view.animate().x(pX).y(pY).start();
					switch(view.getId()){
						case R.id.app_camera:
							try {
								Intent intent = new Intent(MediaStore.INTENT_ACTION_STILL_IMAGE_CAMERA);
								intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
								startActivity(intent);
							} catch (Exception e){ 
								Log.i("Err.......", "Unable to launch camera: " + e); 
							}
							break;
						case R.id.app_call:
							Intent intent = new Intent(Intent.ACTION_DIAL); 
							intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
							startActivity(intent);
							break;
						default:break;
						
					}
					
					if(isPin){
						
						openPiScreen();
						
					}
					else{
						liHomeBase.animate()
							.y(0-liHomeBase.getHeight())
							.setDuration(300)
							.setListener(
							new Animator.AnimatorListener(){
								public void onAnimationStart(Animator a){

								}
								public void onAnimationEnd(Animator a){
									wm.removeView(overlayedButton);
									overlayedButton=null;
									Intent i=new Intent(ACTION_SEND_COMMAND);
									i.putExtra("Command","Unlock");
									sendBroadcast(i);
								}
								public void onAnimationCancel(Animator a){

								}
								public void onAnimationRepeat(Animator a){

								}}
							).start();
					}
					
				}
				else{
					view.animate().x(pX).y(pY).start();
					
				}
				break;
			default:
				return false;
		}
		return true;
	}
	};
	private void setNotilis(){
		LinearLayout bnoti=(LinearLayout)overlayedButton.findViewById(R.id.liBnoti);
		mainNoties=new ArrayList<Noti>();
		int max=notItems.size()>2?2:notItems.size();
		for(int i=0;i<max;i++){
			mainNoties.add(notItems.get(i));
		}
		Notiadapter=new NotiListAdapter(LockService.this,mainNoties);
		ListNoti.setAdapter(Notiadapter);
		if(notItems.size()>2){
		bnoti.setVisibility(View.VISIBLE);
		ListBNot.removeAllViews();
		int bs=0;
		PackageManager PM=mContext.getPackageManager();
		for(int j=2;j<notItems.size();j++){
		
			try{
			ApplicationInfo info=PM.getApplicationInfo(notItems.get(j).Package,PackageManager.GET_META_DATA);
			Drawable icon=PM.getApplicationIcon(info);
			ImageView nicon = new ImageView(this);
			nicon.setLayoutParams(new LinearLayout.LayoutParams((int)fdp2px(22),(int)fdp2px(22)));
			nicon.setPadding(4,4,4,4);
		    nicon.setImageDrawable(icon);
			nicon.setColorFilter(0xBB5BF8F9);
			bs++;
			ListBNot.addView(nicon);
			}catch(PackageManager.NameNotFoundException E){
				
			}
				TextView TxtNoti=(TextView)overlayedButton.findViewById(R.id.txtBnoti);
				TxtNoti.setText("+"+bs);
		}
		
		}
		else{
			bnoti.setVisibility(View.GONE);
		}
		
		
		
			
	}

	void notiPosted(String key, Noti n)
	{
		if (!containsKey(key))
		{
			notItems.add(0, n);
			setNotilis();
		}
		else
		{
			notItems.set(getIndexOfKey(key), n);
			setNotilis();
		}
	}
	private boolean containsKey(String key){
		if(!notItems.isEmpty()){
			for(Noti s:notItems){
				if(s.key.equals(key)){
					return true;
				}
			}

		}
		return false;
	}
	private int getIndexOfKey(String key){

		for(int i=0;i<=notItems.size();i++){
			if(notItems.get(i).key.equals(key)){
				return i;
			}
		}
		return -1;
	}
	public int getStatusBarHeight() {
		boolean var10001;
		int var2;
		try {
			var2 = this.getResources().getIdentifier("status_bar_height", "dimen", "android");
		} catch (Exception vle) {
			var10001 = false;
			return (int)Math.ceil((double)(25.0F * this.getResources().getDisplayMetrics().density));
		}

		if (var2 > 0) {
			try {
				return this.getResources().getDimensionPixelSize(var2);
			} catch (Exception e) {
				var10001 = false;
				return (int)Math.ceil((double)(25.0F * this.getResources().getDisplayMetrics().density));
			}
		} else {
			double var3;
			try {
				var3 = Math.ceil((double)(25.0F * this.getResources().getDisplayMetrics().density));
			} catch (Exception var6) {
				var10001 = false;
				return (int)Math.ceil((double)(25.0F * this.getResources().getDisplayMetrics().density));
			}

			return (int)var3;
		}
	}
	
	private void startUpAngleAnim(){
		if(ImgATop.getAnimation()==null){
		Animation anim=AnimationUtils.loadAnimation(getApplicationContext(),R.anim.up_rep);
		anim.setAnimationListener(new Animation.AnimationListener(){
			public void onAnimationStart(Animation a){
				liBottombtns.animate().alpha(0f).start();
			}
					public void onAnimationRepeat(Animation a){
						}
					public void onAnimationEnd(Animation a){
						liBottombtns.animate().alpha(1f).start();
					}

					
					
		});
			ImgATop.setVisibility(View.VISIBLE);
			ImgATop.startAnimation(anim);
		}
		
	}
	private void ShowMesssage(Object _s){
		Toast.makeText(getApplicationContext(),String.valueOf(_s), Toast.LENGTH_SHORT).show();
	}
	private void UpdateTimeText(){
		handler=new Handler(getMainLooper());
		runnable=new Runnable(){
			@Override
			public void run(){
				Date date=new Date();
				Calendar cal=Calendar.getInstance();
				String format=use24?"H:mm":"h:mm";
				txtTime.setText(new SimpleDateFormat(format).format(date));
				txtAm.setText(cal.get(Calendar.AM_PM)==0?"AM":"PM");
				txtDate.setText(new SimpleDateFormat("d / M  EEE").format(date).toUpperCase());
				handler.postDelayed(this,1000);
			}
		};
		handler.postDelayed(runnable,10);
	}
	public void stops(){
		overlayedButton.animate().alpha(0f).setDuration(0).setListener(
		new Animator.AnimatorListener(){
			public void onAnimationStart(Animator a){
				
			}
			public void onAnimationEnd(Animator a){
				wm.removeView(overlayedButton);
				overlayedButton=null;
				Intent i=new Intent(ACTION_SEND_COMMAND);
				i.putExtra("Command","Unlock");
				sendBroadcast(i);
				LockService.this.stopSelf();
			}
			public void onAnimationCancel(Animator a){
				
			}
			public void onAnimationRepeat(Animator a){
				
			}
		}).start();
	}
	//Pin Screen();
	private LinearLayout layout_keypad;
	private ImageView dot1;
	private ImageView dot2;
	private ImageView dot3;
	private ImageView dot4;
	private ImageView img_lo;
	
	private String password="";
	private String CurrentPass="";
	private int sec;
	private boolean InTimer;
	private int wrongTrys;
	
	private void OpenPinScreen(){
		liPinBase.setVisibility(View.VISIBLE);
		liHomeBase.setVisibility(View.GONE);
		initPin();
		pinInitialise();
		float loHeight=img_lo.getHeight();
		float loWidth=img_lo.getWidth();
		int at=600;
		ValueAnimator animator0= ObjectAnimator.ofFloat(txtTime, "textSize",px2sp( txtTime.getTextSize()),px2sp(loHeight/6.575f)).setDuration(at);
		ValueAnimator animator1 = ObjectAnimator.ofFloat(txtAm, "textSize",px2sp(txtAm.getTextSize()), px2sp(loHeight/13.15f)).setDuration(at);
		ValueAnimator animator2 = ObjectAnimator.ofFloat(txtDate, "textSize",px2sp(txtDate.getTextSize()) ,px2sp(loHeight/15)).setDuration(at);
		animator0.start(); animator1.start();animator2.start();
		
		txtAm.animate().y(img_lo.getY()+ loHeight/6.2619047f).x(loWidth/1.388621f).setDuration(at).start();
		txtTime.animate().y(img_lo.getY()+ (loHeight/12.4649047f)).setDuration(at).start();
		txtTime.setPadding(0,0,(int)(loWidth-txtAm.getX()+2),0);
		txtDate.animate().y(img_lo.getY()+ (loHeight/1.2101227f)).x(loWidth/12).setDuration(at).start();
		Animation keypadAnim=AnimationUtils.loadAnimation(getApplicationContext(),R.anim.load_key);
		layout_keypad.startAnimation(keypadAnim);
		img_lo.animate().alpha(1f).setDuration(600).start();
		
	}
	private void SetHomeTimes(){
    	
		txtAm.setY(loHeight/2.3f);
		txtAm.setX(DisplayWidth/1.3886f);
		txtTime.setY(loHeight/3.25f);
		txtTime.setPadding(0,0,(int)(DisplayWidth-txtAm.getX()),0);
		txtDate.setY((loHeight/1.6437f));
		txtDate.setX(DisplayWidth/2.2018f);
		txtTime.setTextSize(px2sp(loHeight/3.945f));
		txtAm.setTextSize(px2sp(loHeight/7.89f));
		txtDate.setTextSize(px2sp(loHeight/13.15f));
		UpdateTimeText();
	}
	private void initPin(){
		}
	private void pinInitialise(){
		Handler handler1=new Handler(getMainLooper());
		Runnable runnable1=new Runnable(){
			@Override
			public void run(){
				startGAnimation("ENTER PASSWORD");
			}
		};
		handler1.postDelayed(runnable1,100);
		Typeface face= Typeface.createFromAsset(getAssets(), "fonts/osp_din.ttf");
		textResult.setTypeface(face);
		textGranted.setTypeface(face);
		textTimer.setTypeface(face);
	}
	private float px2sp(float pxValue){
		final float fontScale = getResources().getDisplayMetrics().scaledDensity;
		return (pxValue-0.5f)/fontScale;
	}
	private int sp2px(float spValue) {
        final float fontScale = getResources().getDisplayMetrics().scaledDensity;
        return (int) (spValue * fontScale + 0.5f);
    }
	/*public void onKeyDown(int keyCode, KeyEvent event)
	{
		info(String.valueOf( keyCode));
		// TODO: Implement this method
		if(keyCode==KeyEvent.KEYCODE_BACK){
			backHome();
			
		}
	}*/
	private void startGAnimation(String s){
		Animation animation1 =AnimationUtils.loadAnimation(getApplicationContext(), R.anim.zoom_in_short);
		textGranted.setText(s);
		textGranted.setTextColor(0x001DD18F);
		textGranted.setVisibility(View.VISIBLE);
		textGranted.startAnimation(animation1);
		Handler handler1=new Handler(getMainLooper());
		Runnable runnable1=new Runnable(){
			@Override
			public void run(){
				password="";
				setdot();
				textGranted.setTextColor(0xFF1DD18F);
			}
		};
		handler1.postDelayed(runnable1,1000);
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
	private void setdot()
	{
		Handler handler1=new Handler(getMainLooper());
		Runnable runnable1=new Runnable(){
			@Override
			public void run(){
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
		};
		handler1.postDelayed(runnable1,5);
		
		
	}

	private void Act()
	{
		CurrentPass=S_pref.getString("CurrentPass","");
		if(password.equals(CurrentPass)){
			Animation animation1 =AnimationUtils.loadAnimation(getApplicationContext(), R.anim.zoom_in_short);
			textGranted.setText("ACCESS GRANTED");
			textGranted.setVisibility(View.VISIBLE);
			textGranted.startAnimation(animation1);
			Handler handler=new Handler(getMainLooper());
			Runnable runnable=new Runnable(){
				@Override
				public void run(){
					textGranted.setTextColor(0xFF1DD18F);
					Handler handler1=new Handler(getMainLooper());
					Runnable runnable1=new Runnable(){
						@Override
						public void run(){
							stops();
							
						}
					};
					handler1.postDelayed(runnable1,200);
					
				}
			};
			handler.postDelayed(runnable,1000);
		}
		else{
			wrongTrys++;
			error();

		}
	}
	public int getDisplayHeight(){
		DisplayMetrics dm=LockService.this.getApplicationContext().getResources().getDisplayMetrics();
		return dm.heightPixels;
	}
	private void error(){

		if(textGranted.getVisibility()==View.VISIBLE){
			textGranted.setVisibility(View.INVISIBLE);
		}
		Handler handler1=new Handler(getMainLooper());
		Runnable runnable1=new Runnable(){
			@Override
			public void run(){
				if( wrongTrys>=30){
					startTimer();

				}
				else if(wrongTrys%5==0){
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
		};
		handler1.postDelayed(runnable1,800);
		}
	private void startTimer()
	{
	//showeTDialog();
		sec = 14;
		InTimer = true;
		textTimer.setVisibility(View.VISIBLE);
		textTimer.setText("TRY AGAIN IN 15 SECONDS");
		repeatTimer();
	}
	private void repeatTimer()
	{Handler handler1=new Handler(getMainLooper());
		Runnable runnable1=new Runnable(){
			@Override
			public void run(){
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
		};
		handler1.postDelayed(runnable1,1000);
	}
	private void setSizes(){
		Handler handler1=new Handler(getMainLooper());
		Runnable runnable1=new Runnable(){
			@Override
			public void run(){
			    DisplayMetrics dm=LockService.this.getApplicationContext().getResources().getDisplayMetrics();
			    DisplayWidth = dm.widthPixels;
				loHeight=(int)(DisplayWidth/1.822916);
				RelativeLayout. LayoutParams params=new RelativeLayout. LayoutParams(((int)DisplayWidth),((int)loHeight));
				img_lo.setLayoutParams(params);
				img_lo.setY(fdp2px(30));
				SetHomeTimes();
			}
		};
		handler1.postDelayed(runnable1,100);
		
		
	}
	private float fdp2px(float dp) {
        final float scale = getResources().getDisplayMetrics().density;
        return( dp * scale + 0.5f);
    }
	
	
	
	
	// Reciver class -  -  -    -   -  -   -  - - -   - -  -   -  - - - -  - - - - -
	class LockNotiReceiver extends BroadcastReceiver{

		@Override
		public void onReceive(Context context, Intent intent) {
			Bundle Extras=intent.getExtras();
			if(intent.hasExtra("Event")){
			String Event=Extras.getString("Event");
			
			if(Event.equals("ListClear")){
				if(!notItems.isEmpty()){
				   notItems.clear();
				   }
			}
			else if(Event.equals("List")){
		
				notItems.add(new Noti(Extras.getString("Key",""),
				null,
				Extras.getString("Package",""),
				Extras.getString("Title",""),
				Extras.getString("Message",""),
			    Extras.getBoolean("isOngoing",false),
				Extras.getLong("Time",(long)0)));

			}
			else if(Event.equals("ListSet")){
				setNotilis();
			}
			else if(Event.equals("Posted")){
			
				String key=Extras.getString("Key","");
				Noti n=new Noti(key,
								null,
								Extras.getString("Package",""),
								Extras.getString("Title",""),
								Extras.getString("Message",""),
								Extras.getBoolean("isOngoing",false),
								Extras.getLong("Time",(long)0));
				                notiPosted(key, n);
			}
			else if(Event.equals("Removed")){
				try{
				String key =Extras.getString("Key");
				int index=getIndexOfKey(key);
				if(index>=0){
					notItems.remove(index);
					setNotilis();
				}
				}catch(Exception e){}
			}
			else if(Event.equals("Info")){
				txtInfo.setText(txtInfo.getText().toString()+System.getProperty("line.separator") +Extras.getString("Info",""));
			}
			
	}}

		
	private boolean containsKey(String key){
		if(!notItems.isEmpty()){
			for(Noti s:notItems){
				if(s.key.equals(key)){
					return true;
				}
			}
			
		}
		return false;
	}
	private int getIndexOfKey(String key){
	
		for(int i=0;i<=notItems.size();i++){
			if(notItems.get(i).key.equals(key)){
				return i;
			}
		}
		return -1;
	}
	
	}
	
	private BroadcastReceiver musicReceiver = new BroadcastReceiver() {

		@Override
		public void onReceive(Context context, Intent intent)

		{
			UpdatePlayState();
		}
	};
	private void UpdatePlayState(){
		Handler handler= new Handler ();
		Runnable r=new Runnable() {
			public void run() {
				updatePlayButton();
			}
		};
		handler.postDelayed(r,250);
		}
	public void onBtnClick(View v){
		Intent i = new Intent(SERVICECMD);
		switch(v.getId()){
			case R.id.btnPlay:
				if(btnPlay.getTag().equals("pause")){
					i.putExtra(CMDNAME, CMDPAUSE);
					btnPlay.setImageResource(R.drawable.ic_play_circle);
					btnPlay.setTag("play");
				}
				else{
					i.putExtra(CMDNAME, CMDPLAY);
					btnPlay.setImageResource(R.drawable.ic_pause_circle);
					btnPlay.setTag("pause");
				}
				break;
			case R.id.btnNext:
				i.putExtra(CMDNAME, CMDNEXT);

				break;
			case R.id.btnPrev:
				i.putExtra(CMDNAME, CMDPREVIOUS);
				break;

			default:
				break;
		}

		mContext.sendBroadcast(i);
	}
	private void updatePlayButton(){
		if(isMusicActive()){
			btnPlay.setImageResource(R.drawable.ic_pause_circle);
			btnPlay.setTag("pause");
		}
		else{
			btnPlay.setImageResource(R.drawable.ic_play_circle);
			btnPlay.setTag("play");
		}
	}
	private boolean isMusicActive(){
		AudioManager manager = (AudioManager)this.getSystemService(Context.AUDIO_SERVICE);
		if(!manager.isMusicActive())
		{
			return false;
		}
		return true;
	}
	private BroadcastReceiver BatInfoReceiver = new BroadcastReceiver(){
		
		@Override
		public void onReceive(Context ctxt, Intent intent) {
			//int level = intent.getIntExtra(BatteryManager.EXTRA_LEVEL, 0);
			//int status = intent.getIntExtra("status", 0);
			int level = -1;
			int Slevel = intent.getIntExtra(BatteryManager.EXTRA_LEVEL, level);
			int status = intent.getIntExtra(BatteryManager.EXTRA_STATUS, level);
			boolean isCharging = status == 2 || status == 5;
			int scale = intent.getIntExtra(BatteryManager.EXTRA_SCALE, level);
			if (Slevel >= 0 && scale > 0) {
				level = Slevel * 100 / scale;
			}
			else{
				level=Slevel;
			}
			if(!isStatusBar){
			if (isCharging) {
				
				
			} else {
				
			}
			if (level < 5) {
				batIndicator.setImageResource(R.drawable.battery1);
				return;
			}
			if (level < 16) {
				batIndicator.setImageResource(R.drawable.battery2);
				return;
			}
			if (level < 31) {
				batIndicator.setImageResource(R.drawable.battery3);
				return;
			}
			if (level < 43) {
				batIndicator.setImageResource(R.drawable.battery4);
				return;
			}
			if (level < 55) {
				batIndicator.setImageResource(R.drawable.battery5);
				return;
			}
			if (level < 65) {
				batIndicator.setImageResource(R.drawable.battery6);
				return;
			}
			if (level < 75) {
				batIndicator.setImageResource(R.drawable.battery7);
				return;
			}
			if (level < 85) {
				batIndicator.setImageResource(R.drawable.battery8);
				return;
			}
			if (level < 95) {
				batIndicator.setImageResource(R.drawable.battery9);
				return;
			}
				batIndicator.setImageResource(R.drawable.battery10);
		}
			if(status==BatteryManager.BATTERY_STATUS_CHARGING){
				txtCharg.setVisibility(View.VISIBLE);
				txtCharg.setText("Charging, "+level+"%");

				}
				else if(status==BatteryManager.BATTERY_STATUS_FULL){
					txtCharg.setVisibility(View.VISIBLE);
					txtCharg.setText("Battery fully charged");
					
				}
			else{
				txtCharg.setVisibility(View.GONE);
			}
		}
		
	};
	public class phoneListener extends PhoneStateListener {
        public int signal;

        public void onSignalStrengthsChanged(SignalStrength signalStrength) {
            super.onSignalStrengthsChanged(signalStrength);
			
            if (signalStrength.isGsm()) {
                if (signalStrength.getGsmSignalStrength() != 99) {
                    this.signal = -113 + 2 * signalStrength.getGsmSignalStrength();
					
                    return;
                }
				
                this.signal = signalStrength.getGsmSignalStrength();
				
                return;
            }
            this.signal = signalStrength.getCdmaDbm();
			
        }
    }
	private void info(Object o){
		String s=String.valueOf(o);
		txtInfo.setText(txtInfo.getText().toString()+System.getProperty("line.separator")+s);
	}
}
