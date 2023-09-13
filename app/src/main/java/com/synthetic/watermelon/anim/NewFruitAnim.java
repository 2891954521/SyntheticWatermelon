package com.synthetic.watermelon.anim;

import android.graphics.Bitmap;

/*
 * 新的水果出现的动画
 */
public class NewFruitAnim extends ScaleAnim{
	
	public NewFruitAnim(int length){
		super(null,0,0,length);
	}
	
	public void startAnim(Bitmap _texture,int _x,int _y){
		texture = _texture;
		x = _x;
		y = _y;
		current = 0;
		isFinish = false;
	}
}
