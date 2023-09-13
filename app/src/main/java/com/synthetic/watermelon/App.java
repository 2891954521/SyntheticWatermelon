package com.synthetic.watermelon;

import android.app.Application;
import android.os.Looper;
import android.widget.Toast;


public class App extends Application{
	
	// 游戏窗口大小
	public static int GAME_HEIGHT = 1920;
	public static int GAME_WIDTH = 1080;
	
	// 系统分辨率
	public static int SYSTEM_HEIGHT;
	public static int SYSTEM_WIDTH;
	
	private Thread.UncaughtExceptionHandler handler;
	
	// 缩放比例
	public static float SCALE_SIZE = 1.5f;
	
	@Override
	public void onCreate(){
		super.onCreate();
		// 捕获异常
		handler = Thread.getDefaultUncaughtExceptionHandler();
		Thread.setDefaultUncaughtExceptionHandler(new ExceptionHandler());
		// 初始化log工具
		LogUtil.init(this);
	}
	
	// 为Application提供一个Toast方法
	public void toast(final String message){
		new Thread(){
			@Override
			public void run(){
				Looper.prepare();
				Toast.makeText(App.this,message,Toast.LENGTH_LONG).show();
				Looper.loop();
			}
		}.start();
	}
	
	private class ExceptionHandler implements Thread.UncaughtExceptionHandler{
		@Override
		public void uncaughtException(Thread thread,final Throwable throwable){
			toast("应用发生错误，错误类型：" + throwable.getClass().toString());
			LogUtil.Log(throwable);
			if(handler!=null)handler.uncaughtException(thread,throwable);
		}
	}
	
}
