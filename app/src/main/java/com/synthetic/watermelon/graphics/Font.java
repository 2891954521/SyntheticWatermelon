package com.synthetic.watermelon.graphics;

import android.graphics.Paint;
import android.graphics.Typeface;
import android.graphics.Canvas;
import java.util.*;

public class Font{

	private static Font font;
	
	private Paint small,middle,big;

	private Font(){
		small = new Paint();
		small.setAntiAlias(true);
		small.setDither(true);
		small.setSubpixelText(true);
		small.setColor(-1);
		small.setTextSize(30);
		small.setTypeface(Typeface.defaultFromStyle(Typeface.BOLD));
		middle = new Paint(small);
		middle.setTextSize(50);
		big = new Paint(small);
		big.setTextSize(70);
	}
	
	public static void init(){
		font = new Font();
	}
	
	public static Font getInstance(){
		return font;
	}
	
	// 在指定区域绘制文本
	public void drawText(Canvas c,String str,Font.Size size,Align align,int color,float x,float y,float width,float height){
		// 获取指定大小的画笔
		Paint p = getPaint(size);
		p.setColor(color);
		// 储存每行文字
        ArrayList<String> line = new ArrayList<>();
		int begin = 0;
		for(int i=0;i<str.length();i++){
			if(p.measureText(str.substring(begin,i))>width){
				line.add(str.substring(begin,i-1));
				begin = i;
			}
		}
		if(begin<str.length()){
			if(begin==0)line.add(str);
			else line.add(str.substring(begin-1));
		}
		float lineY = y + p.getTextSize()/2 + (p.getFontMetrics().descent-p.getFontMetrics().ascent)/2-p.getFontMetrics().descent;
		// 根据设置的对齐方式计算坐标
		for(int i=0;i<line.size();i++){
			float lineX;
			switch(align){
				case LEFT:
					lineX = x;break;
				case RIGHT_CENTER:
					if(line.size()==1) lineY += (height - p.getTextSize())/2;
				case RIGHT:
					lineX = x + width - p.measureText(line.get(i));break;
				case CENTER:
					if(line.size()==1) lineY += height/2 - p.getTextSize()/2;
				case TOP_CENTER:
					lineX = x + (width - p.measureText(line.get(i)))/2;break;
				default:
					lineX = x;
					lineY = y;
			}
			c.drawText(line.get(i),lineX,lineY,p);
			lineY += p.getTextSize();
		}
	}

	public Paint getPaint(Size i){
		switch(i){
			case SMALL:return small;
			case MIDDLE:return middle;
			case BIG:return big;
			default:return middle;
		}
	}

	public enum Align{
		// 左对齐
		LEFT,
		// 右对齐
		RIGHT,
		// 顶部居中
		TOP_CENTER,
		// 底部居中
		BOTTOM_CENTER,
		// 靠右居中
		RIGHT_CENTER,
		// 绝对居中
		CENTER,
		// 左下
		BOTTOM_LEFT,
		// 右下
		BOTTOM_RIGHT,
	}

	public enum Size {
		// 小，中，大
		SMALL, MIDDLE, BIG
	}
	
}
