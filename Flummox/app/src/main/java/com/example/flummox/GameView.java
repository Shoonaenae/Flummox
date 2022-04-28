package com.example.flummox;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Rect;
import android.graphics.Typeface;
import android.view.MotionEvent;
import android.view.SurfaceView;
import android.os.Build;
import java.text.BreakIterator;
import java.util.Random;

public class GameView extends SurfaceView implements Runnable{
    private Thread thread;
    private boolean isPlaying, isGameOver = false;
    private Background background1, background2;
    private int screenX, screenY, score = 0;
    private Paint paint;
    public static float screenRatioX, screenRatioY;
    private SharedPreferences prefs;
    private Idle idle;
    private Bitmap life[] = new Bitmap[2];
    private Zombie[] zombies;
    private Random random;
    private GameActivity activity;

    public GameView(GameActivity activity, int screenX, int screenY) {
        super(activity);
        this.activity = activity;
        prefs = activity.getSharedPreferences("game", Context.MODE_PRIVATE);


        this.screenX = screenX;
        this.screenY = screenY;
        screenRatioX = 1920f/screenX;
        screenRatioY = 1080f/screenY;

        background1 = new Background(screenX, screenY, getResources());
        background2 = new Background(screenX, screenY, getResources());

        idle = new Idle(screenY, getResources());

        background2.x = screenX;

        life[0] = BitmapFactory.decodeResource(getResources(), R.drawable.hearts);
        life[1] = BitmapFactory.decodeResource(getResources(), R.drawable.heart_grey);

        paint = new Paint();
        paint.setTextSize(128);
        paint.setColor(Color.WHITE);

        zombies = new Zombie[1];

        for(int i = 0; i < 1; i++)
        {
            Zombie zombie = new Zombie(getResources());
            zombies[i] = zombie;
        }

        /*cats = new Cat[4];

        for(int i = 0; i < 4; i++)
        {
            Cat cat = new Cat(getResources());
            cats[i] = cat;
        }*/

        random = new Random();
    }

    @Override
    public void run() {
        while (isPlaying)
        {
            update();
            draw();
            sleep();
        }
    }

    public void update() {
        //Moves by 10px towards left
        background1.x -= 10 * screenRatioX;
        background2.x -= 10 * screenRatioX;

        //When background is completely off the screen
        if(background1.x + background1.background.getWidth() < 0)
        {
            background1.x = screenX;
        }

        if(background2.x + background2.background.getWidth() < 0)
        {
            background2.x = screenX;
        }

        if(idle.isGoingUp)
            idle.y -= 30 * screenRatioY;    //up
        else
            idle.y += 30 * screenRatioY;    //down

        //idle does dot go beyond the lane from top
        if(idle.y < 0)
            idle.y = 0;

        //idle does dot go beyond the lane from bottom
        if(idle.y > screenY - idle.height)
            idle.y = screenY - idle.height;

        for(Zombie zombie : zombies)
        {
            zombie.x -= zombie.speed;

            if(zombie.x + zombie.width < 0)
            {
                if(!zombie.isPassed)
                {
                    score++;
                    isGameOver = false;
                }

                int bound = (int) (30 * screenRatioX);
                zombie.speed = random.nextInt(bound);

                //If speed is 0
                if(zombie.speed < 10 * screenRatioX)
                    zombie.speed = (int) (10 * screenRatioX);   //Set minimum speed

                zombie.x = screenX; //Bird towards the end of the screen in the right side
                zombie.y = random.nextInt(screenY - zombie.height); //Y position

                zombie.isPassed = false;
            }

            //If zombie collides flummox
            if (Rect.intersects(zombie.getCollisionShape(), idle.getCollisionShape()))
            {
                isGameOver = true;

                return;
            }
        }

        /*for(Cat cat : cats)
        {
            cat.x = cat.speed;

            if(cat.x + cat.width < 0)
            {
                if(!cat.isSaved)
                {
                    isGameOver = true;

                    return;
                }

                int bound = (int) (30 * screenRatioX);
                cat.speed = random.nextInt(bound);

                //If speed is 0
                if(cat.speed < 10 * screenRatioX)
                    cat.speed = (int) (10 * screenRatioX);   //Set minimum speed

                cat.x = screenX; //Bird towards the end of the screen in the right side
                cat.y = random.nextInt(screenY - cat.height); //Y position

                cat.isSaved = false;
            }

            //If zombie collides flummox
            if (Rect.intersects(cat.getCollisionShape(), idle.getCollisionShape()))
            {
                cat.x = -500;
                cat.isSaved = true;
                return;
            }
        }*/


    }

    public void draw() {
        if(getHolder().getSurface().isValid())  //Successfully initiated
        {
            Canvas canvas = getHolder().lockCanvas();

            //Background
            canvas.drawBitmap(background1.background, background1.x, background1.y, paint);
            canvas.drawBitmap(background2.background, background2.x, background2.y, paint);

            canvas.drawText(score + "", screenX / 2f, 164, paint);

            if(isGameOver)
            {
                isPlaying = false;  //Break thread
                canvas.drawBitmap(idle.getDead(), idle.x, idle.y, paint);
                getHolder().unlockCanvasAndPost(canvas);
                saveIfHighScore();
                return;
            }

            for(Zombie zombie : zombies)
                canvas.drawBitmap(zombie.getZombie(), zombie.x, zombie.y, paint);

            /*for(Cat cat : cats)
                canvas.drawBitmap(cat.getCat(), cat.x, cat.y, paint);*/

            //Life
            canvas.drawBitmap(life[0], 1580, 35, null);
            canvas.drawBitmap(life[0], 1640, 35, null);
            canvas.drawBitmap(life[0], 1700, 35, null);

            canvas.drawBitmap(idle.getIdle(), idle.x, idle.y, paint);

            getHolder().unlockCanvasAndPost(canvas);    //Show canvas on screen
        }

    }

    private void saveIfHighScore() {

        if (prefs.getInt("highscore", 0) < score) {
            SharedPreferences.Editor editor = prefs.edit();
            editor.putInt("highscore", score);
            editor.apply();
        }

    }

    public void sleep() {
        try {
            Thread.sleep(17);   //60fps
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
    }

    public void resume() {
        isPlaying = true;

        //Initialize
        thread = new Thread(this);
        thread.start();
    }

    //Stop thread
    public void pause() {
        try {
            isPlaying = false;
            thread.join();
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
    }

    @Override
    public boolean onTouchEvent(MotionEvent event) {
        switch(event.getAction())
        {
            case MotionEvent.ACTION_DOWN:
                if(event.getY() < screenY / 2)
                {
                    idle.isGoingUp = true;
                }
                break;
            case MotionEvent.ACTION_UP:
                idle.isGoingUp = false;
                break;
        }

        return true;
    }
}