package com.synthetic.watermelon.body;

import android.graphics.RectF;

import com.synthetic.watermelon.App;
import com.synthetic.watermelon.graphics.Painter;

import org.dyn4j.geometry.Geometry;
import org.dyn4j.geometry.MassType;

public class BaseFruit extends BaseBody{
	
	// 出现动画时长
	private static final int ANIM_LENGTH = 5;
	
	// 半径
	private int r;
	// 种类
	private Fruit fruit;
	
	// 被标记的水果不会再触发碰撞
	public boolean isConsume;
	
	// 水果是否与地面发生过碰撞
	public boolean isTouchGround;
	
	// 水果是否是被合成出的
	private boolean isSynthetic;
	
	private int current;
	
	public BaseFruit(Fruit _fruit,boolean _isSynthetic){
		super(_fruit.getTexture());
		fruit = _fruit;
		isSynthetic = _isSynthetic;
		
		r = texture.getHeight()/2;
		
		// 设置物体为圆形
		addFixture(Geometry.createCircle(r));
		// 设置弹性
		getFixture(0).setRestitution(0.05);
		// 设置位置
		translate(App.GAME_WIDTH/2d,50+r);
		//自动计算质量
		setMass(MassType.NORMAL);
	}
	
	@Override
	public void drawBody(Painter painter){
		if(isSynthetic){
			// 被合成出的水果会播放一个短暂的缩放动画
			if(++current==ANIM_LENGTH)isSynthetic = false;
			int s = (int)(r*(0.8f+0.2f*current/ANIM_LENGTH));
			painter.drawBitmap(texture, null,new RectF((float)(getTransform().getTranslationX() - s),(float)(getTransform().getTranslationY() - s), (float)(getTransform().getTranslationX() + s), (float)(getTransform().getTranslationY() + s)));
		}else{
			int degree = (int)(transform.getRotationAngle()*180/Math.PI);
			painter.drawBitmap(texture, (float)getTransform().getTranslationX() - r, (float)getTransform().getTranslationY() - r, r*2, r*2, degree);
		}
	}
	
	public int getR(){
		return r;
	}
	
	public Fruit getFruit(){
		return fruit;
	}
	
}
