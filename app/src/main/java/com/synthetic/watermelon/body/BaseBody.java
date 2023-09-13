package com.synthetic.watermelon.body;

import android.graphics.Bitmap;

import com.synthetic.watermelon.graphics.Painter;

import org.dyn4j.dynamics.Body;

/*
 * 游戏里的一个物体类
 */
public abstract class BaseBody extends Body{
	
	// 贴图
	protected Bitmap texture;
	
	public BaseBody(Bitmap _texture){
		texture = _texture;
	}
	
	// 绘制贴图
	public abstract void drawBody(Painter painter);
	
	public Bitmap getTexture(){
		return texture;
	}
	
}
