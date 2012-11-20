{\rtf1\ansi\ansicpg1252\cocoartf1187\cocoasubrtf340
{\fonttbl\f0\fswiss\fcharset0 Helvetica;\f1\fnil\fcharset0 Monaco;}
{\colortbl;\red255\green255\blue255;}
\paperw11900\paperh16840\margl1440\margr1440\vieww17980\viewh8400\viewkind0
\pard\tx566\tx1133\tx1700\tx2267\tx2834\tx3401\tx3968\tx4535\tx5102\tx5669\tx6236\tx6803\pardirnatural

\f0\fs24 \cf0 GWT Breakout\
\
This is a simple Breakout Engine made for GWT.\
\
To use it just add the BreakoutEngine.java to your source.\
\
You can simply start using following code:\
\
	
\f1\fs22 BreakoutEngine breakoutEngine = new BreakoutEngine();
\f0\fs24 \
	
\f1\fs22 RootPanel root = RootPanel.get();\
\pard\pardeftab720
\cf0 		\
	root.add(breakoutEngine);\
		\
     this.breakoutEngine.drawPadle();\
     this.breakoutEngine.drawBall();\
     this.breakoutEngine.createBricks();\
     this.breakoutEngine.displayScoreBoard();\
\
     this.breakoutEngine.setPixelSize(500, 600);\
\
     this.breakoutEngine.startGame();}