package com.synthetic.watermelon.widget;

import android.graphics.Bitmap;
import android.graphics.Rect;
import android.graphics.RectF;

import com.synthetic.watermelon.graphics.Painter;
/*
 * 分数绘制器
 */
public class ScoreDrawer{
	
	private Bitmap texture;
	
	private int srcWidth,srcHeight;
	
	private int x,y;
	
	private int width,height;
	
	public ScoreDrawer(Bitmap _texture,int _src_width,int _src_height,int _x,int _y,int _width,int _height){
		texture = _texture;
		srcWidth = _src_width;
		srcHeight = _src_height;
		x = _x;
		y = _y;
		width = _width;
		height = _height;
	}
	
	public void drawScore(Painter painter,int mark){
		// 按位绘制每一个数字的贴图
		char[] c = Integer.toString(mark).toCharArray();
		for(int i = 0;i<c.length;i++)
			painter.drawBitmap(texture,new Rect(srcWidth * (c[i] - 48),0,srcWidth * (c[i] - 47),srcHeight),new RectF(x + width * i,y,x + width + width * i,y + height));
		
	}

}
