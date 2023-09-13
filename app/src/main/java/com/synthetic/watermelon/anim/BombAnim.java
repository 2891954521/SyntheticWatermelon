package com.synthetic.watermelon.anim;

import android.graphics.Bitmap;
import android.graphics.RectF;

import com.synthetic.watermelon.graphics.Graphics;
import com.synthetic.watermelon.graphics.Painter;

/*
 * 爆炸粒子效果动画
 */
public class BombAnim extends BaseAnim{
	
	// 贴图
	protected Bitmap texture;
	// 颜色
	protected int color;
	
	protected int x,y;
	
	protected int size;
	
	protected ParticleAnim[] juice;
	
	protected ParticleAnim[] flesh;
	
	public BombAnim(Bitmap _texture,int _x,int _y,int _size,int _color,int length){
		super(length);
		texture = _texture;
		x = _x;
		y = _y;
		size = _size;
		color = _color;
		
		juice = new ParticleAnim[4 + (int)(4 * Math.random())];
		flesh = new ParticleAnim[4 + (int)(4 * Math.random())];
		
		for(int i=0;i<juice.length;i++){
			juice[i] = new ParticleAnim(x,y,size / (int)(4 + 2 * Math.random()),7 + (int)(8 * Math.random()));
			juice[i].dx = getRandomSpeed();
			juice[i].dy = getRandomSpeed();
		}
		
		for(int i=0;i<flesh.length;i++){
			flesh[i] = new ParticleAnim(x,y,size / (int)(5 + 2 * Math.random()),7 + (int)(8 * Math.random()));
			flesh[i].dx = getRandomSpeed();
			flesh[i].dy = getRandomSpeed();
			flesh[i].degree = (int)(360 * Math.random());
		}
		
	}
	
	@Override
	public void draw(Painter painter){
		
		int alpha = (int)(255 * (1 - (float)current/length));
		int scale = (int)(size * (0.7f + 0.3f*current/length));
		painter.drawShaderBitmap(texture,new RectF(x-scale,y-scale,x+scale,y+scale),alpha,color);
		
		for(ParticleAnim anim:juice){
			if(anim.isFinish){
				scale = (int)(anim.size * (1 - (float)current/length));
				painter.drawShaderBitmap(Graphics.getInstance().juiceS,new RectF(anim.x-scale,anim.y-scale,anim.x+scale,anim.y+scale),color);
				continue;
			}
			if(current==anim.length){
				anim.isFinish = true;
			}else{
				anim.x += anim.dx;
				anim.y += anim.dy;
			}
			painter.drawShaderBitmap(Graphics.getInstance().juiceS,new RectF(anim.x-anim.size,anim.y-anim.size,anim.x+anim.size,anim.y+anim.size),color);
		}
		
		for(ParticleAnim anim:flesh){
			if(anim.isFinish){
				painter.drawShaderBitmap(Graphics.getInstance().flesh,new RectF(anim.x-anim.size,anim.y-anim.size,anim.x+anim.size,anim.y+anim.size),alpha,color);
				continue;
			}else{
				if(current==anim.length){
					anim.isFinish = true;
				}else{
					anim.x += anim.dx;
					anim.y += anim.dy;
					anim.degree += 15;
				}
			}
			painter.drawShaderBitmap(Graphics.getInstance().flesh,anim.x,anim.y,anim.size,anim.size,anim.degree,color);
		}
		
	}
	
	// 随机一个速度
	private int getRandomSpeed(){
		return (int)(size/30*(0.7+Math.random())) * (Math.random()>0.5 ? 1 : -1);
	}
	
	private static class ParticleAnim{
		
		protected int x,y;
		
		protected int dx,dy;
		
		protected int size;
		
		protected int length;
		
		protected int degree;
		
		protected boolean isFinish;
		
		public ParticleAnim(int _x,int _y,int _size,int _length){
			x = _x;
			y = _y;
			size = _size;
			length = _length;
		}
	}
	
}

