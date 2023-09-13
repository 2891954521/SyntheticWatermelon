package com.synthetic.watermelon.graphics;

import android.graphics.Canvas;
import android.graphics.Matrix;
import android.graphics.Paint;
import android.graphics.Bitmap;
import android.graphics.PorterDuff;
import android.graphics.PorterDuffColorFilter;
import android.graphics.Rect;
import android.graphics.RectF;

import com.synthetic.watermelon.App;

/*
 * 底层绘图器
 */

public class Painter{
	
	private static Painter painter;
	
	private Font font;
	
	private Canvas canvas;
	
	private Paint White,Black,Translucent;
	
	private Painter(){
		Font.init();
		font = Font.getInstance();
		
		White = new Paint();
		White.setAntiAlias(true);
		White.setStyle(Paint.Style.FILL);
		White.setColor(Colors.WHITE);
		
		Black = new Paint(White);
		Black.setColor(Colors.BLACK);
		
		Translucent = new Paint(White);
		Translucent.setAlpha(125);
	}
	
	public static void init(){
		painter = new Painter();
	}
	
	public static Painter getInstance(){
		return painter;
	}
	
	public void setCanvas(Canvas canvas){
		this.canvas = canvas;
		// 设置缩放比例
		canvas.scale(App.SCALE_SIZE,App.SCALE_SIZE);
	}
	
	// 绘制一个背景色
	public void drawColor(int color){
		canvas.drawColor(color);
	}
	
	// 画线
	public void drawLine(int x,int y,int ex,int ey,int color){
		canvas.drawLine(x,y,ex,ey,getPaint(color));
	}
	
	// 绘制贴图
	public void drawBitmap(Bitmap bmp,float x,float y){
		canvas.drawBitmap(bmp,x,y,White);
	}
	
	public void drawBitmap(Bitmap bmp,Rect src,RectF dst){
		canvas.drawBitmap(bmp,src,dst,White);
	}
	
	// xy为左上角点
	public void drawBitmap(Bitmap bmp,float x,float y,int width,int height,int rotate){
		Matrix matrix = new Matrix();
		matrix.postTranslate(-width/2, -height/2);
		matrix.postRotate(rotate);
		matrix.postTranslate(x + width/2, y + height/2);
		canvas.drawBitmap(bmp, matrix, White);
	}
	
	public void drawShaderBitmap(Bitmap bmp,RectF des,int color){
		White.setColorFilter(new PorterDuffColorFilter(color,PorterDuff.Mode.SRC_ATOP));
		canvas.drawBitmap(bmp,null,des,White);
		White.setColorFilter(null);
	}
	
	public void drawShaderBitmap(Bitmap bmp,RectF des,int alpha,int color){
		Translucent.setColorFilter(new PorterDuffColorFilter(color,PorterDuff.Mode.SRC_ATOP));
		Translucent.setAlpha(alpha);
		canvas.drawBitmap(bmp,null,des,Translucent);
		Translucent.setColorFilter(null);
	}
	
	// xy为中心点
	public void drawShaderBitmap(Bitmap bmp,float x,float y,float width,float height,int rotate,int color){
		White.setColorFilter(new PorterDuffColorFilter(color,PorterDuff.Mode.SRC_ATOP));
		Matrix matrix = new Matrix();
		matrix.postScale(width/bmp.getWidth(),height/bmp.getHeight());
		matrix.postTranslate(-width/2, -height/2);
		matrix.postRotate(rotate);
		matrix.postTranslate(x, y);
		canvas.drawBitmap(bmp, matrix, White);
		White.setColorFilter(null);
	}
	
	// 绘制文字
	public void drawText(String str,float x,float y){
		drawText(str,Font.Size.MIDDLE,Font.Align.LEFT,Colors.WHITE,x,y);
	}
	public void drawText(String str,Font.Size size,Font.Align align,int color,float x,float y){
		drawText(str,size,align,color,x,y,App.GAME_WIDTH-x,App.GAME_HEIGHT-y);
	}
	public void drawText(String str,Font.Size size,Font.Align align,int color,float x,float y,float width,float height){
		font.drawText(canvas,str,size,align,color,x,y,width,height);
	}
	public Canvas getCanvas(){
		return canvas;
	}
	
	private Paint getPaint(int color){
		switch(color){
			case Colors.WHITE:return White;
			case Colors.BLACK:return Black;
			default:return Black;
		}
	}
	
	public static class Colors{
		public static final int WHITE = -1;
		public static final int BLACK = -16777216;
		public static final int RED = -65536;
		public static final int GREEN = -16711936;
		public static final int BLUE = -16776961;
		public static final int PURPLE = -65281;
	}
	
}
