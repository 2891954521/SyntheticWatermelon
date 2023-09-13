package com.synthetic.watermelon.anim;

import com.synthetic.watermelon.graphics.Painter;
/*
 * 动画类
 */
public abstract class BaseAnim{
	// 当前帧
	protected int current;
	// 总时长
	protected int length;
	// 是否播放完毕
	protected boolean isFinish;
	
	public BaseAnim(int _length){
		length = _length;
	}
	
	public abstract void draw(Painter painter);
	
	public boolean update(){
		if(isFinish)return true;
		if(++current==length)isFinish = true;
		return isFinish;
	}
	
	public boolean isFinish(){
		return isFinish;
	}
	
}
