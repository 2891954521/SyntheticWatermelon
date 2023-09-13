package com.synthetic.watermelon.widget;

import android.graphics.Bitmap;
import android.graphics.Color;
import android.graphics.RectF;
import android.view.MotionEvent;

import com.synthetic.watermelon.graphics.Font;
import com.synthetic.watermelon.graphics.Painter;

public class Button extends Widget{
	
	private boolean canTouch = true;
	
	private String text;
	
	private Bitmap texture;
	
	public Button(Bitmap _texture,String text,int x,int y,int buttonWidth,int buttonHeight){
		texture = _texture;
		setText(text);
		rectF = new RectF(x,y,x+buttonWidth,y+buttonHeight);
	}
	
	public Button(Bitmap texture,int x,int y,int buttonWidth,int buttonHeight){
		this(texture,"",x,y,buttonWidth,buttonHeight);
	}
	
	// 被点击后的事件
	public void doClick(){ }
	
	public void found(){
		isFounding = true;
	}
	
	@Override
	public void createUI(Painter p){
		if(canTouch){
			// 取得焦点后添加一层灰色遮罩
			if(isFounding)p.drawShaderBitmap(texture,rectF,Color.parseColor("#332c2c2c"));
			else p.drawBitmap(texture,null,rectF);
		}else p.drawShaderBitmap(texture,rectF,Color.parseColor("#332c2c2c"));
		p.drawText(text,Font.Size.MIDDLE,Font.Align.CENTER,Painter.Colors.WHITE,getX(),getY(),getWidth(),getHeight());
	}
	
	@Override
	public void onTouch(int action){
		if(action==MotionEvent.ACTION_DOWN)found();
		else if(action==MotionEvent.ACTION_UP&&isFounding){
			doClick();
			lostFound();
		}
	}
	
	public boolean canTouch(){
		return canTouch;
	}
	
	public void setTouchAble(boolean b){
		canTouch = b;
	}
	
	public void setText(String text){
		this.text = text;
	}
	
	@Override
	public void Dispose(){
		texture = null;
	}
}
