package com.synthetic.watermelon.screen;

import android.content.Context;

import com.synthetic.watermelon.graphics.Painter;

import java.util.ArrayList;

public class ScreenManager{
	
	private ArrayList<Screen> screens;
	
	private Screen current;
	
	private Context context;
	
	public ScreenManager(Context _context){
		context = _context;
		screens = new ArrayList<>();
	}
	
	public void drawScreen(Painter painter){
		current.onDraw(painter);
	}
	
	public void touchScreen(int action,float x,float y){
		current.touch(action,x,y);
	}
	
	public void updateScreen(){
		current.update();
	}
	
	public void addScreen(Screen screen){
		current = screen;
		screens.add(screen);
	}
	
	public void removeScreen(){
		current.dispose();
		current = null;
		screens.remove(screens.size()-1);
		current = screens.get(screens.size()-1);
	}
	
	public Context getContext(){
		return context;
	}
	
	public void disposeGame(){
		current = null;
		screens = null;
	}
	
}
