package com.synthetic.watermelon.graphics;

import android.annotation.SuppressLint;
import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Color;

import com.synthetic.watermelon.App;
import com.synthetic.watermelon.anim.BombAnim;
import com.synthetic.watermelon.body.BaseFruit;

import java.io.IOException;
import java.io.InputStream;

/*
 * 存放全部的贴图的类
 */
public class Graphics{
	
	private static Graphics graphics;
	
	// 屏幕密度
	private static float density;
	
	// 水果贴图
	public static String[] FruitTexture = new String[]{
			"fruit_grape.png","fruit_cherry.png","fruit_orange.png",
			"fruit_lemon.png","fruit_kiwifruit.png","fruit_tomato.png",
			"fruit_peach.png","fruit_pineapple.png","fruit_coconut.png",
			"fruit_half_watermelon.png","fruit_watermelon.png"
	};
	
	// 背景贴图
	public int background;
	// 地面贴图
	public Bitmap ground;
	// 警示线
	public Bitmap deadline;
	
	// 合成时的粒子特效
	public Bitmap juice,juice2,juiceS,flesh;
	
	// 粒子颜色
	public int[] color = new int[]{
			Color.parseColor("#fe36f9"),
			Color.parseColor("#fe4f7b"),
			Color.parseColor("#fee264"),
			Color.parseColor("#fedd92"),
			Color.parseColor("#78de4a"),
			Color.parseColor("#fe4f58"),
			Color.parseColor("#fe934c"),
			Color.parseColor("#fee264"),
			Color.parseColor("#ffffff"),
			Color.parseColor("#fe4c56"),
			Color.parseColor("#fe4c56"),
	};
	
	// 开始按钮
	public Bitmap buttonPlay;
	
	public Bitmap buttonHome;
	
	public Bitmap buttonSetting;
	
	public Bitmap number;
	
	public Bitmap best;
	
	private Context context;
	
	private Graphics(Context _context){
		context = _context;
		
		density = context.getResources().getDisplayMetrics().density;
		
		background = Color.parseColor("#ffe89d");
		
		ground = getBitmap("ground.png",App.GAME_WIDTH,200);
		
		deadline = getBitmap("deadline.png",App.GAME_WIDTH,10);
		
		juice = getBitmap("juice.png");
		
		juice2 = getBitmap("juice2.png");
		
		juiceS = getBitmap("juiceS.png");
		
		flesh = getBitmap("flesh.png");
		
		buttonPlay = getBitmap("button_play.png");
		
		buttonHome = getBitmap("button_home.png");
		
		buttonSetting = getBitmap("button_setting.png");
		
		number = getBitmap("number.png");
		
		best = getBitmap("best.png");
	}
	
	public static void init(Context context){
		graphics = new Graphics(context);
	}
	
	public static Graphics getInstance(){
		return graphics;
	}
	
	// 创建一个粒子效果对象
	public BombAnim getFruitAnim(BaseFruit fruit,int x,int y){
		return new BombAnim(
				// 随机选择一张贴图
				Math.random()>0.5?Graphics.getInstance().juice:Graphics.getInstance().juice2,
				x,y,fruit.getTexture().getWidth(),
				// 选择对应的颜色
				Graphics.getInstance().color[fruit.getFruit().getMark()],15);
	}
	
	// 从assets里读取贴图
	public Bitmap getBitmap(String resource){
		BitmapFactory.Options options = new BitmapFactory.Options();
		options.inPreferredConfig = Bitmap.Config.ARGB_8888;
		options.inScaled = false;
		InputStream stream = null;
		
		try{
			 stream = context.getAssets().open(resource);
		}catch(IOException ignored){ }
		
		Bitmap bit = BitmapFactory.decodeStream(stream,null,options);
		
		if(stream!=null)try{
			stream.close();
		}catch(IOException ignored){ }
		
		return bit;
	}
	
	// 获取一张指定大小的贴图
	public Bitmap getBitmap(String s,int width,int height){
		return Bitmap.createScaledBitmap(getBitmap(s),width,height,false);
	}
	
	// 将dp转换为游戏里的大小
	public static int dp2GameSize(float dp){
		return (int) (dp/App.SCALE_SIZE*density+0.5f);
	}
	
	// 释放贴图资源
	public void dispose(){
		context = null;

		deadline.recycle();
		deadline = null;
		
		buttonPlay.recycle();
		buttonPlay = null;
		
		buttonHome.recycle();;
		buttonHome = null;
		
		buttonSetting.recycle();
		buttonSetting = null;
		
		juice.recycle();
		juice = null;
		
		juice2.recycle();
		juice2 = null;
		
		juiceS.recycle();
		juiceS = null;
		
		flesh.recycle();
		flesh = null;
		
		number.recycle();
		number = null;
		
		best.recycle();
		best = null;
	}
	
}
