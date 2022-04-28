package com.example.flummox;

import android.content.res.Resources;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Rect;

import static com.example.flummox.GameView.screenRatioX;
import static com.example.flummox.GameView.screenRatioY;

public class Cat
{
    public int speed = 20;
    public boolean isSaved = true;
    int x, y, width, height, catCounter = 1;
    Bitmap cat1, cat2, cat3, cat4;

    Cat(Resources res)
    {
        cat1 = BitmapFactory.decodeResource(res, R.drawable.run1);
        cat2 = BitmapFactory.decodeResource(res, R.drawable.run2);
        cat3 = BitmapFactory.decodeResource(res, R.drawable.run3);
        cat4 = BitmapFactory.decodeResource(res, R.drawable.run4);;

        width = cat1.getWidth();
        height = cat1.getHeight();

        width /= 6;
        height /= 6;

        width = (int)(width * screenRatioX);
        height = (int)(height * screenRatioY);

        cat1 = Bitmap.createScaledBitmap(cat1, width, height, false);
        cat2 = Bitmap.createScaledBitmap(cat2, width, height, false);
        cat3 = Bitmap.createScaledBitmap(cat3, width, height, false);
        cat4 = Bitmap.createScaledBitmap(cat4, width, height, false);

        y = -height;
    }

    Bitmap getCat()
    {
        if(catCounter == 1)
        {
            catCounter++;

            return cat1;
        }

        if(catCounter == 2)
        {
            catCounter++;

            return cat2;
        }

        if(catCounter == 3)
        {
            catCounter++;

            return cat3;
        }

        catCounter = 1;

        return cat4;
    }

    Rect getCollisionShape()
    {
        return new Rect(x, y, x + width, y + height);
    }
}
