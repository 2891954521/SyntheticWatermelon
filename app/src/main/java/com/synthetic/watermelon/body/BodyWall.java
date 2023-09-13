package com.synthetic.watermelon.body;

import com.synthetic.watermelon.App;
import com.synthetic.watermelon.graphics.Painter;

import org.dyn4j.geometry.MassType;
import org.dyn4j.geometry.Rectangle;
/*
 * 游戏两侧的空气墙
 */
public class BodyWall extends BaseBody{
	
	public BodyWall(float x){
		super(null);
		// 物体为矩形
		addFixture(new Rectangle(2,App.GAME_HEIGHT));

		translate(x,App.GAME_HEIGHT/2f);
		// 质量无穷大
		setMass(MassType.INFINITE);
	}
	
	@Override
	public void drawBody(Painter painter){ }
}
