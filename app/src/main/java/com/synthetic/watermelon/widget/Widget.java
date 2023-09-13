package com.synthetic.watermelon.widget;

import android.graphics.RectF;

import com.synthetic.watermelon.graphics.Painter;

/*
 * 游戏基础Widget
 */
public abstract class Widget{

	protected RectF rectF;

	protected int width,height;

	// 是否正在取得焦点
	public boolean isFounding = false;

	public boolean isVisible = true;

	public float getX(){
		return rectF.left;
	}
	
	public float getY(){
		return rectF.top;
	}

	public void setX(float x){
		rectF.left = x;
		rectF.right = x + width;
	}
	
	public void setY(float y){
		rectF.top = y;
		rectF.bottom = y + height;
	}

	public int getWidth(){
		return width;
	}
	
	public int getHeight(){
		return height;
	}

	public void setHeight(int height){
		this.height = height;
		rectF.bottom = rectF.top + height;
	}
	
	public void setWidth(int width){
		this.width = width;
		rectF.right = rectF.left + width;
	}
	
	/*
	 * 是否被点击
	 */
	public boolean isClick(int action,float x,float y){
		// 是否点击到控件
		if(rectF.contains(x,y)){
			return true;
		}else{
			// 如果正在取得焦点，那么失去焦点
			if(isFounding)lostFound();
			return false;
		}
	}
	
	public void onTouch(int action){ }

	public void lostFound(){
		isFounding = false;
	}

	public abstract void createUI(Painter painter);

	public abstract void Dispose();
}
