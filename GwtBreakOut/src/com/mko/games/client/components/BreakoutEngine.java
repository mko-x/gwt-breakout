/**
 * Copyright 2011 by pear-webdesign-agentur.de
 *
 * Florian Wagner & Markus Kosmal GbR
 * Ludwigstraße 51
 * 90763 Fürth
 *
 * @author
 */
package com.mko.games.client.components;

import java.util.ArrayList;

import com.google.gwt.canvas.client.Canvas;
import com.google.gwt.canvas.dom.client.Context2d;
import com.google.gwt.core.client.Scheduler;
import com.google.gwt.core.client.Scheduler.ScheduledCommand;
import com.google.gwt.event.dom.client.KeyCodeEvent;
import com.google.gwt.event.dom.client.KeyDownEvent;
import com.google.gwt.event.dom.client.KeyDownHandler;
import com.google.gwt.event.dom.client.KeyUpEvent;
import com.google.gwt.event.dom.client.KeyUpHandler;
import com.google.gwt.user.client.Timer;
import com.google.gwt.user.client.ui.FlowPanel;
import com.google.gwt.user.client.ui.RootPanel;

// TODO: Auto-generated Javadoc
/**
 * The Class Main.
 * 
 * @author kosmal
 */
public class BreakoutEngine extends FlowPanel {

    /** The Constant INSTANCE. */
    private static final BreakoutEngine INSTANCE = new BreakoutEngine();

    /** The canvas. */
    Canvas canvas;

    /** The context. */
    Context2d context;

    ArrayList<int[]> bricks;

    /** The width. */
    private final int width = 400;

    /** The height. */
    private final int height = 500;

    /** The bricks per row. */
    private final double bricksPerRow = 8;

    /** The brick height. */
    private final double brickHeight = 20;

    /** The brick width. */
    private double brickWidth;

    double ballRadius = 10;

    double ballX;

    double ballY;

    double paddleX;

    double paddleY;

    /** The score. */
    private int score = 0;

    private double ballDeltaX;
    private double ballDeltaY;

    private String paddleMove = "NONE";
    private double paddleDeltaX = 0;

    private double paddleSpeedX = 10;

    private int gameSpeed = 10;

    double paddleWidth;

    boolean bumpedX;
    boolean bumpedY;

    private final KeyDownHandler keyDownHandler = new KeyDownHandler() {

        @Override
        public void onKeyDown(final KeyDownEvent event) {
            if (event != null && event.getSource() != this) {
                if (event.isLeftArrow()) {
                    BreakoutEngine.this.paddleMove = "LEFT";
                }
                else if (event.isRightArrow()) {
                    BreakoutEngine.this.paddleMove = "RIGHT";
                }

            }

        }
    };

    private final KeyUpHandler keyUpHandler = new KeyUpHandler() {

        @Override
        public void onKeyUp(final KeyUpEvent event) {
            if (event != null && event.getSource() != this) {
                if (KeyCodeEvent.isArrow(39)) {
                    BreakoutEngine.this.paddleMove = "NONE";
                }
                else if (KeyCodeEvent.isArrow(37)) {
                    BreakoutEngine.this.paddleMove = "NONE";
                }
            }

        }
    };

    /**
     * Instantiates a new main.
     */
    public BreakoutEngine() {
        this.canvas = Canvas.createIfSupported();
        this.canvas.setPixelSize(this.width, this.height);
        this.canvas.setCoordinateSpaceWidth(this.width);
        this.canvas.setCoordinateSpaceHeight(this.height);

        this.add(this.canvas);

        this.ballX = 300;
        this.ballY = 300;

        this.canvas.addKeyDownHandler(this.keyDownHandler);
        this.canvas.addKeyUpHandler(this.keyUpHandler);

        RootPanel.get().addDomHandler(this.keyDownHandler, KeyDownEvent.getType());
        RootPanel.get().addDomHandler(this.keyUpHandler, KeyUpEvent.getType());

        this.paddleX = 200;
        this.paddleY = 460;

        this.bricks = new ArrayList<int[]>();

        int[] firstRow = new int[] {1, 1, 1, 1, 1, 1, 1, 2 };
        int[] secondRow = new int[] {1, 1, 3, 1, 0, 1, 1, 1 };
        int[] thirdRow = new int[] {2, 1, 2, 1, 2, 1, 0, 1 };
        int[] fourthRow = new int[] {1, 2, 1, 1, 0, 3, 1, 1 };

        this.bricks.add(firstRow);
        this.bricks.add(secondRow);
        this.bricks.add(thirdRow);
        this.bricks.add(fourthRow);
    }

    public void startGame() {
        this.ballDeltaX = -2;
        this.ballDeltaY = -4;

        this.gameLoop.scheduleRepeating(this.gameSpeed);

    }

    public void endGame() {
        this.gameLoop.cancel();

        String result = "";
        if (!this.checkScore()) {
            result = "The End!! You loose!!";

        }
        else {
            result = "You made it!!";
        }
        this.context.fillText(result, this.getCanvas().getCoordinateSpaceWidth() / 2, this.getCanvas().getCoordinateSpaceHeight() / 2);
    }

    private final Timer gameLoop = new Timer() {

        @Override
        public void run() {
            Scheduler.get().scheduleDeferred(new ScheduledCommand() {

                @Override
                public void execute() {
                    BreakoutEngine.this.animate();
                }
            });

        }
    };

    public void animate() {
        this.context.clearRect(0, 0, this.canvas.getCoordinateSpaceWidth(), this.canvas.getCoordinateSpaceHeight());

        this.movePaddle();

        this.drawPadle();
        this.createBricks();

        this.displayScoreBoard();
        this.moveBall();
        this.drawBall();

        if (this.checkScore()) {
            this.endGame();
        }
    }

    public boolean collisionXWithBricks() {
        boolean bumpedX = false;
        for (int row = 0; row < this.bricks.size() - 1; row++) {
            for (int column = 0; column < this.bricks.get(row).length - 1; column++) {
                double brickX = column * this.brickWidth;
                double brickY = row * this.brickHeight;
                if ((this.ballX + this.ballDeltaX + this.ballRadius >= brickX && this.ballX + this.ballRadius <= brickX)
                        || (this.ballX + this.ballDeltaX - this.ballRadius <= brickX + this.brickWidth) && (this.ballX - this.ballRadius >= brickX + this.brickWidth)) {
                    if (this.ballY + this.ballDeltaY - this.ballRadius <= brickY + this.brickHeight && this.ballY + this.ballDeltaY + this.ballRadius >= brickY) {
                        if (this.bricks.get(row)[column] > 0) {
                            this.explodeBrick(column, row);
                            this.bumpedX = true;
                            this.ballDeltaX = -this.ballDeltaX;
                        }
                    }
                }
            }
        }
        return bumpedX;
    }

    public boolean collisionYWithBricks() {
        boolean bumpedY = false;
        for (int row = 0; row < this.bricks.size() - 1; row++) {
            for (int column = 0; column < this.bricks.get(row).length - 1; column++) {
                double brickX = column * this.brickWidth;
                double brickY = row * this.brickHeight;
                if ((this.ballY + this.ballDeltaY - this.ballRadius <= brickY + this.brickHeight && this.ballY - this.ballRadius >= brickY + this.brickHeight)
                        || ((this.ballY + this.ballDeltaY + this.ballRadius >= brickY) && (this.ballY + this.ballRadius <= brickY))) {
                    if (this.ballX + this.ballDeltaX + this.ballRadius >= brickX && this.ballX + this.ballDeltaX - this.ballRadius <= brickX + this.brickWidth && this.bricks.get(row)[column] != 0) {
                        if (this.bricks.get(row)[column] > 0) {
                            this.explodeBrick(column, row);
                            this.bumpedY = true;
                            this.ballDeltaY = -this.ballDeltaY;
                        }
                    }
                }
            }
        }
        return bumpedY;
    }

    public void explodeBrick(final int row, final int colum) {
        if (this.bricks.get(colum)[row] > 0) {
            this.bricks.get(colum)[row]--;

            if (this.bricks.get(colum)[row] > 0) {
                this.score++;
            }
            else {
                this.score += 2;
            }
        }
    }

    public void moveBall() {

        if (this.ballY + this.ballDeltaY - this.ballRadius < 0 || this.collisionYWithBricks()) {
            this.ballDeltaY = -this.ballDeltaY;
        }

        if (this.ballY + this.ballDeltaY + this.ballRadius > this.canvas.getCoordinateSpaceHeight()) {
            this.endGame();
        }

        if (this.ballX + this.ballDeltaX + this.ballRadius >= this.canvas.getCoordinateSpaceWidth() || this.collisionXWithBricks()) {
            this.ballDeltaX = -this.ballDeltaX;
        }

        if (this.ballX + this.ballDeltaX + this.ballRadius == 0) {
            this.ballDeltaX = -this.ballDeltaX;
        }

        if (this.ballY + this.ballDeltaY + this.ballRadius >= this.paddleY) {
            if (this.ballX + this.ballDeltaX + 5 >= this.paddleX && this.ballX + this.ballDeltaX <= this.paddleX + this.paddleWidth) {
                this.ballDeltaY = -this.ballDeltaY;
            }
        }
        this.ballX += this.ballDeltaX;
        this.ballY += this.ballDeltaY;
    }

    public void movePaddle() {
        if (this.paddleMove.equals("LEFT")) {
            this.paddleDeltaX = -this.paddleSpeedX;
        }
        else if (this.paddleMove.equals("RIGHT")) {
            this.paddleDeltaX = this.paddleSpeedX;
        }
        else {
            this.paddleDeltaX = 0;
        }

        this.paddleX = this.paddleX + this.paddleDeltaX;
    }

    /**
     * Draw padle.
     */
    public void drawPadle() {

        if (this.canvas != null) {
            this.context = this.canvas.getContext2d();

            this.paddleWidth = 100;
            double height = 15;

            this.context.beginPath();
            this.context.setFillStyle("black");
            this.context.fillRect(this.paddleX, this.paddleY, this.paddleWidth, height);
        }

    }

    /**
     * Draw ball.
     */
    public void drawBall() {

        this.context.beginPath();
        this.context.setFillStyle("black");

        this.context.arc(this.ballX, this.ballY, this.ballRadius, 0, Math.PI * 2, true);

        this.context.fill();
    }

    /**
     * Creates the bricks.
     */
    public void createBricks() {
        this.brickWidth = this.canvas.getCoordinateSpaceWidth() / this.bricksPerRow;

        for (int outerCount = 0; outerCount < this.bricks.size() - 1; outerCount++) {
            for (int i = 0; i < this.bricks.get(outerCount).length - 1; i++) {
                this.drawSingleBrick(i, outerCount, this.bricks.get(outerCount)[i]);
            }
        }

    }

    /**
     * Draw single brick.
     * 
     * @param x
     *            the x
     * @param y
     *            the y
     * @param type
     *            the type
     */
    public void drawSingleBrick(final int x, final int y, final int type) {
        switch (type) {
            case 1:
                this.context.setFillStyle("orange");
                break;
            case 2:
                this.context.setFillStyle("rgb(100,200,100)");
                break;
            case 3:
                this.context.setFillStyle("rgba(50,100,50,.5)");
                break;
            default:
                this.context.clearRect(x * this.brickWidth + 1, y * this.brickHeight + 1, this.brickWidth, this.brickHeight);
                break;
        }

        if (type > 0 && type < 4) {
            this.context.fillRect(x * this.brickWidth, y * this.brickHeight, this.brickWidth, this.brickHeight);

            this.context.strokeRect(x * this.brickWidth + 1, y * this.brickHeight + 1, this.brickWidth - 2, this.brickHeight - 2);
        }

    }

    /**
     * Display score board.
     */
    public void displayScoreBoard() {
        this.context.setFillStyle("rgb(50,100,50)");
        this.context.setFont("20px Times New Roman");

        this.context.clearRect(0, this.canvas.getCoordinateSpaceHeight() - 30, this.canvas.getCoordinateSpaceWidth() + 30, 30);

        this.context.fillText("Score: " + this.score, 10, this.canvas.getCoordinateSpaceHeight() - 5);
    }

    public boolean checkScore() {

        int completedScore = 0;
        for (int row = 0; row < this.bricks.size() - 1; row++) {
            for (int column = 0; column < this.bricks.get(row).length - 1; column++) {
                completedScore += this.bricks.get(row)[column];
            }
        }

        if (completedScore == 0) {
            return true;
        }

        return false;
    }

    /**
     * Gets the canvas.
     * 
     * @return the canvas
     */
    public final Canvas getCanvas() {
        return this.canvas;
    }

    /**
     * Gets the score.
     * 
     * @return the score
     */
    public final int getScore() {
        return this.score;
    }

    /**
     * Sets the score.
     * 
     * @param score
     *            the score to set
     */
    public final void setScore(final int score) {
        this.score = score;
    }

    /**
     * @return the ballX
     */
    public final double getBallX() {
        return this.ballX;
    }

    /**
     * @param ballX
     *            the ballX to set
     */
    public final void setBallX(final double ballX) {
        this.ballX = ballX;
    }

    /**
     * @return the ballY
     */
    public final double getBallY() {
        return this.ballY;
    }

    /**
     * @param ballY
     *            the ballY to set
     */
    public final void setBallY(final double ballY) {
        this.ballY = ballY;
    }

    /**
     * @return the ballDeltaX
     */
    public final double getBallDeltaX() {
        return this.ballDeltaX;
    }

    /**
     * @param ballDeltaX
     *            the ballDeltaX to set
     */
    public final void setBallDeltaX(final double ballDeltaX) {
        this.ballDeltaX = ballDeltaX;
    }

    /**
     * @return the ballDeltaY
     */
    public final double getBallDeltaY() {
        return this.ballDeltaY;
    }

    /**
     * @param ballDeltaY
     *            the ballDeltaY to set
     */
    public final void setBallDeltaY(final double ballDeltaY) {
        this.ballDeltaY = ballDeltaY;
    }

    /**
     * @return the paddleSpeedX
     */
    public final double getPaddleSpeedX() {
        return this.paddleSpeedX;
    }

    /**
     * @param paddleSpeedX
     *            the paddleSpeedX to set
     */
    public final void setPaddleSpeedX(final double paddleSpeedX) {
        this.paddleSpeedX = paddleSpeedX;
    }

    /**
     * @return the gameSpeed
     */
    public final int getGameSpeed() {
        return this.gameSpeed;
    }

    /**
     * @param gameSpeed
     *            the gameSpeed to set
     */
    public final void setGameSpeed(final int gameSpeed) {
        this.gameSpeed = gameSpeed;
    }

    /**
     * Gets the single instance of Main.
     * 
     * @return the iNSTANCE
     */
    public static BreakoutEngine getInstance() {
        return INSTANCE;
    }

}
