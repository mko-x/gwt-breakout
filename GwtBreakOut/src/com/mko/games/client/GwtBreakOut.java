package com.mko.games.client;

import com.google.gwt.core.client.EntryPoint;
import com.google.gwt.user.client.ui.RootPanel;
import com.mko.games.client.components.BreakoutEngine;

/**
 * Entry point classes define <code>onModuleLoad()</code>.
 */
public class GwtBreakOut implements EntryPoint{

    BreakoutEngine breakoutEngine = new BreakoutEngine();

    public GwtBreakOut() {
        this.breakoutEngine.setPixelSize(500, 600);
    }

	@Override
	public void onModuleLoad() {
		RootPanel root = RootPanel.get();
		
		root.add(breakoutEngine);
		
	  this.breakoutEngine.drawPadle();
      this.breakoutEngine.drawBall();
      this.breakoutEngine.createBricks();
      this.breakoutEngine.displayScoreBoard();

      this.breakoutEngine.setPixelSize(500, 600);

      this.breakoutEngine.startGame();
	}
}
