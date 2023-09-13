package com.synthetic.watermelon;

import android.graphics.Canvas;
import android.view.MotionEvent;
import android.view.SurfaceHolder;
import android.view.SurfaceView;

import com.synthetic.watermelon.graphics.Graphics;
import com.synthetic.watermelon.graphics.Painter;
import com.synthetic.watermelon.screen.Screen;
import com.synthetic.watermelon.screen.ScreenManager;
import com.synthetic.watermelon.screen.ScreenTitle;

public class GameView extends SurfaceView implements SurfaceHolder.Callback{
	
	public MainActivity activity;
	// 窗口管理类
	public ScreenManager manager;
	// 负责更新界面的线程
	private Runnable run;
	//
	private Painter painter;
	// 帧率
	private float fps = 60f;
	
	public GameView(MainActivity activity){
		super(activity);
		this.activity = activity;
		setLongClickable(true);
		init();
	}
	
	private void init(){
		// 画面刷新
		run = new Runnable(){
			@Override
			public void run(){
				// 更新screen
				manager.updateScreen();
				getHolder().unlockCanvasAndPost(preDraw(getHolder().lockCanvas()));
				// 延迟16ms（60帧）
				postDelayed(this,(int)(1000/fps));
			}
		};
		// 初始化绘图器
		Painter.init();
		painter = Painter.getInstance();
		
		manager = new ScreenManager(getContext());
		manager.addScreen(new ScreenTitle(manager));
		
		getHolder().setType(SurfaceHolder.SURFACE_TYPE_GPU);
		getHolder().addCallback(this);
	}
	
	@Override
	public void surfaceCreated(SurfaceHolder holder){
		// removeCallbacks(run);
		// post(run);
	}
	
	@Override
	public void surfaceChanged(SurfaceHolder holder,int p2,int p3,int p4){ }
	
	@Override
	public void surfaceDestroyed(SurfaceHolder holder){
		removeCallbacks(run);
	}
	
	public void onResume(){
		removeCallbacks(run);
		postDelayed(run,(int)(1000/fps));
	}
	
	public void onPause(){
		removeCallbacks(run);
	}
	
	private Canvas preDraw(Canvas canvas){
		painter.setCanvas(canvas);
		// 绘制游戏背景
		painter.drawColor(Graphics.getInstance().background);
		// 绘制Screen
		manager.drawScreen(painter);
		return painter.getCanvas();
	}
	
	@Override
	public boolean onTouchEvent(MotionEvent event){
		manager.touchScreen(event.getAction(),event.getX()/App.SCALE_SIZE,event.getY()/App.SCALE_SIZE);
		// 消耗掉点击事件
		return true;
	}
	
	public void addScreen(Screen screen){
		manager.addScreen(screen);
	}
	
	public void removeScreen(){
		manager.removeScreen();
	}
	
}
