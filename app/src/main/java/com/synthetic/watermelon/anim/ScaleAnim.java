package com.synthetic.watermelon.anim;

import android.graphics.Bitmap;
import android.graphics.RectF;

import com.synthetic.watermelon.graphics.Painter;
/*
 * 缩放动画
 */
public class ScaleAnim extends BaseAnim{
	
	// 贴图
	protected Bitmap texture;
	// 贴图中心点
	protected int x,y;
	
	public ScaleAnim(Bitmap _texture,int _x,int _y,int length){
		super(length);
		texture = _texture;
		x = _x;
		y = _y;
	}
	
	@Override
	public void draw(Painter painter){
		int hw = texture.getWidth()*current/length/2;
		int hh = texture.getHeight()*current/length/2;
		painter.drawBitmap(texture,null,new RectF(x-hw,y-hh,x+hw,y+hh));
	}
	
	public void setX(int _x){
		x = _x;
	}
}
