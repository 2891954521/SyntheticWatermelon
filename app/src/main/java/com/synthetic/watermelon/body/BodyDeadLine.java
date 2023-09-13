package com.synthetic.watermelon.body;

import android.graphics.Bitmap;

import com.synthetic.watermelon.App;
import com.synthetic.watermelon.graphics.Painter;

import org.dyn4j.geometry.MassType;
import org.dyn4j.geometry.Rectangle;

// 死亡线
public class BodyDeadLine extends BaseBody{
	
	private int y;
	
	public BodyDeadLine(Bitmap texture,int _y){
		super(texture);
		
		y = _y;
		
		addFixture(new Rectangle(App.GAME_WIDTH, texture.getHeight()));
		
		translate(App.GAME_WIDTH/2f, y);
		
		setMass(MassType.INFINITE);
	}
	
	@Override
	public void drawBody(Painter painter){
		painter.drawBitmap(texture,0,y);
	}
}
