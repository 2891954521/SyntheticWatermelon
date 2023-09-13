package com.synthetic.watermelon.screen;

import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.SharedPreferences;
import android.graphics.Color;
import android.graphics.Rect;
import android.graphics.RectF;
import android.hardware.Sensor;
import android.hardware.SensorEvent;
import android.hardware.SensorEventListener;
import android.hardware.SensorManager;
import android.media.AudioManager;
import android.media.SoundPool;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.EditText;

import com.synthetic.watermelon.App;
import com.synthetic.watermelon.R;
import com.synthetic.watermelon.anim.BombAnim;
import com.synthetic.watermelon.anim.NewFruitAnim;
import com.synthetic.watermelon.body.BaseBody;
import com.synthetic.watermelon.body.BaseFruit;
import com.synthetic.watermelon.body.BodyDeadLine;
import com.synthetic.watermelon.body.BodyGround;
import com.synthetic.watermelon.body.BodyWall;
import com.synthetic.watermelon.body.Fruit;
import com.synthetic.watermelon.graphics.Graphics;
import com.synthetic.watermelon.graphics.Painter;
import com.synthetic.watermelon.widget.Button;
import com.synthetic.watermelon.widget.ScoreDrawer;

import org.dyn4j.dynamics.BodyFixture;
import org.dyn4j.geometry.MassType;
import org.dyn4j.geometry.Rectangle;
import org.dyn4j.geometry.Vector2;
import org.dyn4j.world.NarrowphaseCollisionData;
import org.dyn4j.world.World;
import org.dyn4j.world.listener.CollisionListenerAdapter;

import java.util.ArrayList;

public class ScreenGame extends Screen{
	
	// 触碰到死亡线后的死亡时间
	private static final int DEATH_TIME = 60;
	
	// 调试工具
	private boolean neverGameOver;
	private boolean onlyRandomWatermelon;
	private boolean autoDrop;
	private boolean allowAI;
	private boolean isClearingScreen;
	
	// 音效相关
	private SoundPool soundPool;
	
	private int[] sounds;
	private float volume;
	
	// 物理引擎的World
	private World<BaseBody> world;
	
	// 分数
	private int mark;
	// 最高分
	private int best;
	
	// 延迟
	private int delay;
	// AI的延迟
	private int delayAI;
	
	// 死亡时间计时器
	private int deathTime;
	
	private boolean isTouchDeadline;
	private boolean isGameOver;
	private boolean isRemovingFruits;
	
	// 发生碰撞的水果的集合
	private ArrayList<CollisionFruit> collisionFruits;
	// 储存粒子特效的集合
	private ArrayList<BombAnim> bombAnim;
	
	// 上一个水果
	private BaseFruit lastFruit;
	// 当前的水果
	private BaseFruit fruit;
	
	// 水果种类
	private Fruit[] fruits;
	
	// 新水果进入的动画
	private NewFruitAnim newFruitAnim;
	
	private Button setting;
	
	private ScoreDrawer scoreDrawer;
	
	private AlertDialog dialog;
	private View dialogView;
	
	private SensorManager sensorManager;
	// 重力传感器
	private GravitySensorEventListener sensor;
	
	public ScreenGame(final ScreenManager manager){
		super(manager);
		
		collisionFruits = new ArrayList<>();
		
		bombAnim = new ArrayList<>();
		
		AudioManager audioManager = (AudioManager)manager.getContext().getSystemService(Context.AUDIO_SERVICE);
		
		soundPool = new SoundPool(3, AudioManager.STREAM_MUSIC, 0);
		
		sounds = new int[3];
		sounds[0] = soundPool.load(manager.getContext(), R.raw.fruit_drop, 1);
		sounds[1] = soundPool.load(manager.getContext(), R.raw.fruit_synthetic, 1);
		sounds[2] = soundPool.load(manager.getContext(), R.raw.fruit_bomb, 1);
		
		volume = (float)audioManager.getStreamVolume(AudioManager.STREAM_MUSIC) / audioManager.getStreamMaxVolume(AudioManager.STREAM_MUSIC);
		
		sensorManager = (SensorManager)manager.getContext().getSystemService(Context.SENSOR_SERVICE);
		
		init();
		initFruit();
		newFruitAnim = new NewFruitAnim(10);
		createFruit();
	}
	
	private void init(){
		world = new World<>();
		// 重力加速度设为10m·s^(-2)，坐标系的原因，让重力使物体向上移动
		world.setGravity(new Vector2(0,10));
		
		// 地面
		BodyGround ground = new BodyGround(Graphics.getInstance().ground);
		
		// 左右两堵空气墙
		BodyWall leftWall = new BodyWall(-1);
		BodyWall rightWall = new BodyWall(App.GAME_WIDTH + 1);
		
		// 顶部空气墙
		BaseBody topWall = new BaseBody(null){
			@Override public void drawBody(Painter painter){ }
		};
		topWall.addFixture(new Rectangle(App.GAME_WIDTH,2));
		topWall.translate(App.GAME_WIDTH/2f,-1);
		topWall.setMass(MassType.INFINITE);
		
		// 死亡线
		BodyDeadLine deadLine = new BodyDeadLine(Graphics.getInstance().deadline,App.GAME_HEIGHT/10);
		
		// 添加到物理引擎里
		world.addBody(ground);
		world.addBody(leftWall);
		world.addBody(rightWall);
		world.addBody(topWall);
		world.addBody(deadLine);
		// 监听物体碰撞
		world.addCollisionListener(new GameCollisionListener());
		
		Button home = new Button(Graphics.getInstance().buttonHome,Graphics.dp2GameSize(16),Graphics.dp2GameSize(16),Graphics.dp2GameSize(48),Graphics.dp2GameSize(48)){
			@Override
			public void doClick(){
				manager.removeScreen();
			}
		};
		
		Button play = new Button(Graphics.getInstance().buttonPlay,App.GAME_WIDTH / 2 - Graphics.dp2GameSize(32),App.GAME_HEIGHT / 2 + Graphics.dp2GameSize(32),Graphics.dp2GameSize(64),Graphics.dp2GameSize(64)){
			@Override
			public void doClick(){
				isGameOver = false;
				isRemovingFruits = false;
				isTouchDeadline = false;
				deathTime = 0;
				mark = 0;
				createFruit();
			}
		};
		
		addWidget(home);
		addWidget(play);
		
		setting = new Button(Graphics.getInstance().buttonSetting,App.GAME_WIDTH - Graphics.dp2GameSize(64),Graphics.dp2GameSize(16),Graphics.dp2GameSize(48),Graphics.dp2GameSize(48)){
			@Override
			public void doClick(){
				showSetting();
			}
		};
		
		scoreDrawer = new ScoreDrawer(Graphics.getInstance().number,66,60,Graphics.dp2GameSize(16),Graphics.dp2GameSize(16),Graphics.dp2GameSize(32),Graphics.dp2GameSize(29));
		
	}
	
	private void initFruit(){
		// 初始化水果数据
		Fruit[] temp = new Fruit[11];

		Fruit.FruitType[] fruitType = Fruit.FruitType.values();
		for(int i=0;i<temp.length;i++)temp[i] = new Fruit(fruitType[temp.length-1-i],Graphics.getInstance().getBitmap(Graphics.FruitTexture[i]),i);
		fruits = temp;
	}
	
	@Override
	public void update(){
		
		isTouchDeadline = false;
		// 游戏结束，且正在移除所有水果
		if(isRemovingFruits){
			if(world.getBodyCount()>5){
				// 短暂延迟后移除下一个水果
				if(--delay<=0){
					delay = 3;
					// 从最后一个开始移除
					BaseFruit f = (BaseFruit)world.getBody(world.getBodyCount()-1);
					bombAnim.add(Graphics.getInstance().getFruitAnim(f,(int)f.getTransform().getTranslationX(),(int)f.getTransform().getTranslationY()));
					world.removeBody(world.getBodyCount()-1);
					// 播放音效
					soundPool.play(sounds[2], volume, volume, 1, 0, 1f);
					// 加分
					mark += f.getFruit().getMark();
				}
			}else if(bombAnim.size()==0){
				// 所有水果和动画已全部移除，宣告游戏结束
				isRemovingFruits = false;
				// 不是玩家手动清屏
				if(!isClearingScreen)gameOver();
			}
		}else{
			if(!isGameOver){
				// 测试用，自动放置水果
				if(autoDrop&&newFruitAnim.isFinish()){
					if(allowAI){
						// AI自动放置
						if(delayAI--==0){
							delayAI = 20;
							BaseFruit closest = null;
							for(int i = 5;i<world.getBodyCount();i++){
								BaseFruit f = (BaseFruit)world.getBody(i);
								if(f.getFruit()==fruit.getFruit()){
									if(closest==null||f.getTransform().getTranslationY()<closest.getTransform().getTranslationY())
										closest = f;
								}
							}
							if(closest!=null)throwFruit(closest.getTransform().getTranslationX());
							else throwFruit(App.GAME_WIDTH/2);
						}
					}else throwFruit(App.GAME_WIDTH/10 + App.GAME_WIDTH*0.8*Math.random());
				}
				// 让物理引擎步进20步
				world.step(20);
				if(!collisionFruits.isEmpty()){
					// 处理发生碰撞的水果
					int i = 0;
					while(i<collisionFruits.size()){
						if(collisionFruits.get(i).update())collisionFruits.remove(i);
						else i++;
					}
				}
				
				// 如果触碰到了死亡线则开始计时，否则计时清零
				if(isTouchDeadline){
					deathTime++;
					// 计时结束
					if(deathTime==DEATH_TIME){
						isGameOver = true;
						isRemovingFruits = true;
						delay = 30;
						return;
					}
				}else deathTime = 0;
			}
		}
		
		// 更新所有爆炸粒子动画
		int i = 0;
		while(i<bombAnim.size()){
			bombAnim.get(i).update();
			if(bombAnim.get(i).isFinish()) bombAnim.remove(i);
			else i++;
		}
		// 更新新水果动画
		newFruitAnim.update();
	}
	
	@Override
	public void onDraw(Painter painter){
		// 游戏结束时绘制游戏结束界面
		if(isGameOver && !isRemovingFruits){
			painter.drawColor(Color.parseColor("#7f282828"));
			char[] c = Integer.toString(mark).toCharArray();
			int l = c.length * 100;
			int left = (App.GAME_WIDTH - l) / 2;
			for(int i = 0;i<c.length;i++)
				painter.drawBitmap(Graphics.getInstance().number,new Rect(66 * (c[i] - 48),0,66 * (c[i] - 47),60),new RectF(left + 100 * i,App.GAME_HEIGHT / 2f - 250,left + 100 + 100 * i,App.GAME_HEIGHT / 2f - 159));
			c = Integer.toString(best).toCharArray();
			l = c.length * 66;
			left = (App.GAME_WIDTH - l) / 2 + 50;
			for(int i = 0;i<c.length;i++)
				painter.drawBitmap(Graphics.getInstance().number,new Rect(66 * (c[i] - 48),0,66 * (c[i] - 47),60),new RectF(left + 66 * i,App.GAME_HEIGHT / 2f - 70,left + 66 + 66 * i,App.GAME_HEIGHT / 2f - 4));
			painter.drawBitmap(Graphics.getInstance().best,left - 100,App.GAME_HEIGHT / 2f - 70);
			// 让Button显示出来
			super.onDraw(painter);
			return;
		}
		// 绘制所有实体
		// 绘制地板
		world.getBody(0).drawBody(painter);
		// 绘制死亡线
		if(isTouchDeadline) world.getBody(4).drawBody(painter);
		
		// 绘制水果
		for(int i = 5;i<world.getBodyCount();i++) world.getBody(i).drawBody(painter);
		// 绘制动画
		for(BombAnim a : bombAnim) a.draw(painter);
		
		newFruitAnim.draw(painter);
		
		// 绘制分数
		scoreDrawer.drawScore(painter,mark);
		// 绘制设置按钮
		setting.createUI(painter);
		
		// 不绘制widget
		// super.onDraw(painter);
	}
	
	// 随机生成一个水果
	private void createFruit(){
		if(onlyRandomWatermelon){
			fruit = new BaseFruit(fruits[fruits.length-1],false);
		}else{
			int i = (int)(Math.random()*4);
			Fruit f = fruits[i];
			fruit = new BaseFruit(f,false);
		}
		newFruitAnim.startAnim(fruit.getTexture(),App.GAME_WIDTH / 2,50 + fruit.getR());
	}
	
	// 放置水果
	private void throwFruit(double x){
		fruit.getTransform().setTranslationX(x);
		// 添加一个初速度
		fruit.setLinearVelocity(0,100);
		world.addBody(fruit);
		lastFruit = fruit;
		createFruit();
	}
	
	@Override
	public void touch(int action,float x,float y){
		if(isRemovingFruits) return;
		
		if(isGameOver){
			super.touch(action,x,y);
			return;
		}
		
		if(setting.isClick(action,x,y)){
			setting.onTouch(action);
			return;
		}
		
		switch(action){
			case MotionEvent.ACTION_DOWN: case MotionEvent.ACTION_MOVE:
				// 新的水果动画已播放完成，移动到指定地点
				if(newFruitAnim.isFinish())newFruitAnim.setX((int)x);
				break;
			case MotionEvent.ACTION_UP:
				// 手指抬起，放下水果
				if(newFruitAnim.isFinish()) throwFruit(x);
				break;
		}
	}
	
	// 游戏结束，记录最高分
	private void gameOver(){
		SharedPreferences sp = manager.getContext().getSharedPreferences("Data",Context.MODE_PRIVATE);
		best = sp.getInt("best",0);
		if(mark>best){
			best = mark;
			sp.edit().putInt("best",best).apply();
		}
	}
	
	@Override
	public void dispose(){
		super.dispose();
		if(dialog!=null)dialog.dismiss();
		if(sensor!=null)sensorManager.unregisterListener(sensor);
	}
	
	private class GameCollisionListener extends CollisionListenerAdapter<BaseBody,BodyFixture>{
		@Override
		public boolean collision(NarrowphaseCollisionData<BaseBody,BodyFixture> collision){
			
			// 物体与死亡线碰撞
			if(collision.getBody1() instanceof BodyDeadLine || collision.getBody2() instanceof BodyDeadLine){
				// 刚刚放下去的水果不算
				if(neverGameOver || collision.getBody1().equals(lastFruit) || collision.getBody2().equals(lastFruit))
					return false;
				
				isTouchDeadline = true;
				// 返回 false 使与死亡线的碰撞不生效
				return false;
			}
			
			// 水果与地板碰撞
			if(collision.getBody1() instanceof BodyGround || collision.getBody2() instanceof BodyGround){
				BaseFruit f = null;
				if(collision.getBody1() instanceof BaseFruit) f = (BaseFruit)collision.getBody1();
				else if(collision.getBody2() instanceof BaseFruit) f = (BaseFruit)collision.getBody2();
				if(f != null && !f.isTouchGround){
					// 水果第一次与地板碰撞，播放一个碰撞音效
					f.isTouchGround = true;
					soundPool.play(sounds[0], volume, volume, 1, 0, 1f);
				}
				return true;
			}
			
			// 发生碰撞的两者必须都是水果
			if(!(collision.getBody1() instanceof BaseFruit) || !(collision.getBody2() instanceof BaseFruit))
				return true;
			
			BaseFruit fruit1 = (BaseFruit)collision.getBody1();
			BaseFruit fruit2 = (BaseFruit)collision.getBody2();
			
			// 被标记的水果不算
			if(fruit1.isConsume || fruit2.isConsume) return !(fruit1.isConsume && fruit2.isConsume);
			
			// 同类发生碰撞，且不是大西瓜
			if(fruit1.getFruit().equals(fruit2.getFruit()) && !fruit1.getFruit().equals(fruits[fruits.length-1])){
				
				// 计算两点间距离
				int dx = (int)(fruit2.getTransform().getTranslationX() - fruit1.getTransform().getTranslationX());
				int dy = (int)(fruit2.getTransform().getTranslationY() - fruit1.getTransform().getTranslationY());
				int distance = (int)Math.sqrt(dx*dx + dy*dy);
				// 计算分速度
				float vx = 100f*dx/distance;
				float vy = 100f*dy/distance;
				
				fruit1.isConsume = true;
				fruit2.isConsume = true;
				// 添加到碰撞列表中等待处理，同时给予一个初速度使其相向移动
				if(fruit1.isAtRest()||fruit1.getTransform().getTranslationY()<fruit2.getTransform().getTranslationY()){
					fruit1.setLinearVelocity(vx,vy);
					fruit2.setLinearVelocity(0,0);
					collisionFruits.add(new CollisionFruit(fruit1,fruit2));
				}else{
					fruit1.setLinearVelocity(0,0);
					fruit2.setLinearVelocity(-vx,-vy);
					collisionFruits.add(new CollisionFruit(fruit2,fruit1));
				}
				return false;
			}
			
			return true;
		}
	}
	
	// 处理水果碰撞的类
	private class CollisionFruit{
		
		private int delay;
		
		private BaseFruit fruit1, fruit2;
		
		public CollisionFruit(BaseFruit f1,BaseFruit f2){
			delay = 5;
			fruit1 = f1;
			fruit2 = f2;
		}
		
		public boolean update(){
			if(--delay==0){
				// 获取新的水果种类
				Fruit fruit = fruits[fruit1.getFruit().getMark()+1];
				BaseFruit n = new BaseFruit(fruit,true);
				n.getTransform().setTranslation(fruit2.getTransform().getTranslationX(),fruit2.getTransform().getTranslationY());
				
				bombAnim.add(Graphics.getInstance().getFruitAnim(fruit1,(int)fruit2.getTransform().getTranslationX(),(int)fruit2.getTransform().getTranslationY()));
				soundPool.play(sounds[1], volume, volume, 1, 0, 1f);
				
				world.removeBody(fruit1);
				world.removeBody(fruit2);
				
				world.addBody(n);
				
				// 加分
				mark += fruit.getMark();
				// 合成大西瓜额外加100分
				if(fruit.getType()==Fruit.FruitType.Watermelon)mark += 100;
				return true;
			}else return false;
		}
	}
	
	private class GravitySensorEventListener implements SensorEventListener{
		@Override
		//可以得到重力传感器实时测量出来的变化值
		public void onSensorChanged(SensorEvent event){
			if(event.sensor.getType()==Sensor.TYPE_ACCELEROMETER){
				world.setGravity(-10*event.values[SensorManager.DATA_X],10*event.values[SensorManager.DATA_Y]);
			}
		}
		
		@Override
		public void onAccuracyChanged(Sensor sensor,int accuracy){ }
	}
	
	// 设置界面
	private void showSetting(){
		if(dialog!=null){
			((CheckBox)dialogView.findViewById(R.id.setting_never_game_over)).setChecked(neverGameOver);
			((CheckBox)dialogView.findViewById(R.id.setting_only_random_watermelon)).setChecked(onlyRandomWatermelon);
			((CheckBox)dialogView.findViewById(R.id.setting_auto_drop)).setChecked(autoDrop);
			((CheckBox)dialogView.findViewById(R.id.setting_allow_ai)).setChecked(allowAI);
			dialog.show();
			return;
		}
		AlertDialog.Builder builder = new AlertDialog.Builder(manager.getContext());
		builder.setTitle("调试工具");
		dialogView = LayoutInflater.from(manager.getContext()).inflate(R.layout.dialog_setting,null);
		
		((CheckBox)dialogView.findViewById(R.id.setting_never_game_over)).setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener(){
			@Override
			public void onCheckedChanged(CompoundButton buttonView,boolean isChecked){
				neverGameOver = isChecked;
			}
		});
		
		((CheckBox)dialogView.findViewById(R.id.setting_only_random_watermelon)).setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener(){
			@Override
			public void onCheckedChanged(CompoundButton buttonView,boolean isChecked){
				onlyRandomWatermelon = isChecked;
			}
		});
		
		((CheckBox)dialogView.findViewById(R.id.setting_auto_drop)).setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener(){
			@Override
			public void onCheckedChanged(CompoundButton buttonView,boolean isChecked){
				autoDrop = isChecked;
			}
		});
		
		((CheckBox)dialogView.findViewById(R.id.setting_allow_ai)).setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener(){
			@Override
			public void onCheckedChanged(CompoundButton buttonView,boolean isChecked){
				allowAI = isChecked;
			}
		});
		
		((CheckBox)dialogView.findViewById(R.id.setting_gravity)).setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener(){
			@Override
			public void onCheckedChanged(CompoundButton buttonView,boolean isChecked){
				if(isChecked){
					sensor = new GravitySensorEventListener();
					sensorManager.registerListener(sensor,sensorManager.getDefaultSensor(Sensor.TYPE_ACCELEROMETER),SensorManager.SENSOR_DELAY_GAME);
				}else{
					sensorManager.unregisterListener(sensor);
					sensor = null;
				}
			}
		});
		
		dialogView.findViewById(R.id.setting_mark_apply).setOnClickListener(new View.OnClickListener(){
			@Override
			public void onClick(View v){
				mark = Integer.parseInt(((EditText)dialogView.findViewById(R.id.setting_mark)).getText().toString());
			}
		});
		
		dialogView.findViewById(R.id.setting_scale_apply).setOnClickListener(new View.OnClickListener(){
			@Override
			public void onClick(View v){
				float f = Float.parseFloat(((EditText)dialogView.findViewById(R.id.setting_scale)).getText().toString());
				SharedPreferences sp = manager.getContext().getSharedPreferences("Data",Context.MODE_PRIVATE);
				sp.edit().putFloat("scale",f).apply();

				App.SCALE_SIZE = f;
				App.GAME_HEIGHT = (int)(App.SYSTEM_HEIGHT/f);
				App.GAME_WIDTH = (int)(App.SYSTEM_WIDTH/f);
				
				init();
			}
		});
		
		dialogView.findViewById(R.id.setting_clear).setOnClickListener(new View.OnClickListener(){
			@Override
			public void onClick(View v){
				isClearingScreen = true;
				isRemovingFruits = true;
			}
		});
		
		dialogView.findViewById(R.id.setting_game_over).setOnClickListener(new View.OnClickListener(){
			@Override
			public void onClick(View v){
				isGameOver = true;
				isRemovingFruits = true;
				delay = 0;
			}
		});
		
		builder.setView(dialogView);
		builder.setNegativeButton("取消",new DialogInterface.OnClickListener(){
			@Override
			public void onClick(DialogInterface _dialog,int which){
				dialog.hide();
			}
		});
		dialog = builder.create();
		dialog.setCanceledOnTouchOutside(false);
		dialog.show();
	}
	
	@Override
	public String getName(){
		return "game";
	}
	
}
