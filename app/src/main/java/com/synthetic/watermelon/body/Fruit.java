package com.synthetic.watermelon.body;

import android.graphics.Bitmap;

/*
 * 水果种类
 */
public class Fruit{
	
	public enum FruitType {
		//
		Grape,
		Cherry,
		Orange,
		Lemon,
		Kiwifruit,
		Tomato,
		Peach,
		Pineapple,
		Coconut,
		HalfWatermelon,
		Watermelon;
	}
	
	private int mark;
	
	private Bitmap texture;
	
	private FruitType type;
	
	public Fruit(FruitType _type,Bitmap _texture,int _mark){
		type = _type;
		texture = _texture;
		mark = _mark;
	}
	
	public int getMark(){
		return mark;
	}
	
	public void setMark(int _mark){
		mark = _mark;
	}
	
	public Bitmap getTexture(){
		return texture;
	}
	
	public void setTexture(Bitmap _texture){
		texture = _texture;
	}
	
	public FruitType getType(){
		return type;
	}
	
	public void setType(FruitType _type){
		type = _type;
	}
	
	public void dispose(){
		texture.recycle();
	}
}
