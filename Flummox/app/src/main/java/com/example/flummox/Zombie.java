package com.example.flummox;

import android.content.res.Resources;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Rect;

import static com.example.flummox.GameView.screenRatioX;
import static com.example.flummox.GameView.screenRatioY;

public class Zombie
{
    public int speed = 50;
    int x = 0, y, width, height, zombieCounter = 1;
    Bitmap zombie1, zombie2, zombie3, zombie4;
    public boolean isPassed = true;

    Zombie(Resources res)
    {
        zombie1 = BitmapFactory.decodeResource(res, R.drawable.walk1);
        zombie2 = BitmapFactory.decodeResource(res, R.drawable.walk2);
        zombie3 = BitmapFactory.decodeResource(res, R.drawable.walk3);
        zombie4 = BitmapFactory.decodeResource(res, R.drawable.walk4);



        width = zombie1.getWidth();
        height = zombie1.getHeight();

        width /= 6;
        height /= 6;

        width = (int)(width * screenRatioX);
        height = (int)(height * screenRatioY);

        zombie1 = Bitmap.createScaledBitmap(zombie1, width, height, false);
        zombie2 = Bitmap.createScaledBitmap(zombie2, width, height, false);
        zombie3 = Bitmap.createScaledBitmap(zombie3, width, height, false);
        zombie4 = Bitmap.createScaledBitmap(zombie4, width, height, false);


        y = -height;
    }

    Bitmap getZombie(){
     if (zombieCounter == 1) {
    zombieCounter++;

    return zombie1;
}

        if (zombieCounter == 2) {
    zombieCounter++;

    return zombie2;
}

        if (zombieCounter == 3) {
    zombieCounter++;

    return zombie3;
}


    zombieCounter=1;

            return zombie4;
}

    Rect getCollisionShape()
    {
        return new Rect(x, y, x + width, y + height);
    }
}
