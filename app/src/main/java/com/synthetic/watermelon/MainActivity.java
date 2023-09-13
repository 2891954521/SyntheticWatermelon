package com.synthetic.watermelon;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.Display;
import android.view.WindowManager;

import com.synthetic.watermelon.graphics.Graphics;

public class MainActivity extends Activity{
	
	private GameView view;
	
	@Override
	protected void onCreate(Bundle savedInstanceState){
		super.onCreate(savedInstanceState);
		// 读取设置的缩放比例
		SharedPreferences sp = getSharedPreferences("Data",Context.MODE_PRIVATE);
		App.SCALE_SIZE = sp.getFloat("scale",1.5f);
		// 适配分辨率
		WindowManager mWindow = (WindowManager)this.getSystemService(Context.WINDOW_SERVICE);
		Display display = mWindow.getDefaultDisplay();
		App.SYSTEM_HEIGHT = display.getHeight();
		App.SYSTEM_WIDTH = display.getWidth();
		// 计算缩放后的尺寸
		App.GAME_HEIGHT = (int)(App.SYSTEM_HEIGHT/App.SCALE_SIZE);
		App.GAME_WIDTH = (int)(App.SYSTEM_WIDTH/App.SCALE_SIZE);
		// 初始化贴图数据
		Graphics.init(this);
		// 创建GameView
		view = new GameView(this);
		setContentView(view);
	}
	
	@Override
	protected void onPause(){
		super.onPause();
		view.onPause();
	}
	
	@Override
	protected void onResume(){
		super.onResume();
		view.onResume();
	}
	
	@Override
	public void onBackPressed(){
		// 按下返回键弹出对话框
		AlertDialog.Builder dialog = new AlertDialog.Builder(this);
		dialog.setTitle("退出游戏");
		dialog.setMessage("是否退出游戏");
		dialog.setPositiveButton("确定",new DialogInterface.OnClickListener(){
			@Override
			public void onClick(DialogInterface dialog,int which){
				dialog.dismiss();
				MainActivity.super.onBackPressed();
			}
		});
		dialog.setNegativeButton("取消",new DialogInterface.OnClickListener(){
			@Override
			public void onClick(DialogInterface dialog,int which){
				dialog.dismiss();
			}
		});
		dialog.show();
	}
	
	@Override
	protected void onDestroy(){
		super.onDestroy();
		// 释放游戏资源
		Graphics.getInstance().dispose();
	}
}