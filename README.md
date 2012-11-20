GWT Breakout

This is a simple Breakout Engine made for GWT.

To use it just add the BreakoutEngine.java to your source.

You can simply start using following code:

	
	BreakoutEngine breakoutEngine = new BreakoutEngine();

	RootPanel root = RootPanel.get();

	root.add(breakoutEngine);
	
	this.breakoutEngine.drawPadle();
	this.breakoutEngine.drawBall();
	this.breakoutEngine.createBricks();
	this.breakoutEngine.displayScoreBoard();
	this.breakoutEngine.setPixelSize(500, 600);
	
	this.breakoutEngine.startGame();