package com.synthetic.watermelon.screen;

import java.util.ArrayList;

import com.synthetic.watermelon.graphics.Painter;
import com.synthetic.watermelon.widget.Widget;

/*
 * 游戏Screen
 */
public abstract class Screen{

	protected ScreenManager manager;
	// 界面里的所有组件
	private ArrayList<Widget> widget = new ArrayList<>();

	public Screen(ScreenManager _manager){
		manager = _manager;
	}
	
	public abstract void update();
	
	// 绘制所有组件
	public void onDraw(Painter pinter){
		for(Widget b:widget)if(b.isVisible)b.createUI(pinter);
	}
	
	// 处理点击事件
	public void touch(int action,float x,float y){
		boolean hasUsed = false;
		// 检查是否点击到了某个组件
		for(Widget b:widget){
			if(hasUsed){
				b.lostFound();
				continue;
			}
			if(b.isClick(action,x,y)){
				b.onTouch(action);
				hasUsed = true;
			}
		}
		// 这里直接返还，阻止子类继续处理点击事件
		if(hasUsed)return;
	}

	public void addWidget(Widget widget){
		this.widget.add(widget);
	}
	
	public void removeWidget(Widget widget){
		this.widget.remove(widget);
	}
	
	public void dispose(){
		for(Widget w:widget)w.Dispose();
	}
	
	public abstract String getName();
	
}
