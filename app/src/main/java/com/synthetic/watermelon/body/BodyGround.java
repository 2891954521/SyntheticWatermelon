package com.synthetic.watermelon.body;

import android.graphics.Bitmap;

import com.synthetic.watermelon.App;
import com.synthetic.watermelon.graphics.Painter;

import org.dyn4j.geometry.Geometry;
import org.dyn4j.geometry.MassType;

/*
 * 地面
 */
public class BodyGround extends BaseBody{
	
	public BodyGround(Bitmap texture){
		super(texture);
		
		addFixture(Geometry.createRectangle(App.GAME_WIDTH,texture.getHeight()));
		
		translate(App.GAME_WIDTH/2f,App.GAME_HEIGHT - texture.getHeight()/2f);
		// 质量无穷大
		setMass(MassType.INFINITE);
	}
	
	@Override
	public void drawBody(Painter painter){
		// 地面的位置是固定的
		painter.drawBitmap(texture,0,App.GAME_HEIGHT - texture.getHeight());
	}
}
