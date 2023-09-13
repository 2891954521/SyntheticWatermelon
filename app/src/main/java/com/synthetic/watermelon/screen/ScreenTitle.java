package com.synthetic.watermelon.screen;

import com.synthetic.watermelon.App;

import com.synthetic.watermelon.graphics.Font;
import com.synthetic.watermelon.graphics.Graphics;
import com.synthetic.watermelon.graphics.Painter;
import com.synthetic.watermelon.widget.Button;

public class ScreenTitle extends Screen{
	
	public ScreenTitle(final ScreenManager manager){
		super(manager);
		
		Button startGame = new Button(Graphics.getInstance().buttonPlay,
				App.GAME_WIDTH / 2 - Graphics.dp2GameSize(32),
				App.GAME_HEIGHT / 2 + Graphics.dp2GameSize(32),
				Graphics.dp2GameSize(64),Graphics.dp2GameSize(64)){
			@Override
			public void doClick(){
				manager.addScreen(new ScreenGame(manager));
			}
		};
		
		addWidget(startGame);

	}
	
	@Override
	public void update(){
		
	}
	
	@Override
	public void onDraw(Painter painter){
		painter.drawText("合 成 大 西 瓜",Font.Size.BIG,Font.Align.CENTER,Painter.Colors.WHITE,0,0,App.GAME_WIDTH,App.GAME_HEIGHT/2);
		super.onDraw(painter);
	}
	
	@Override
	public void dispose(){
		
	}
	
	@Override
	public String getName(){
		return "title";
	}
}
