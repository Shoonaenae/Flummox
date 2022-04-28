package com.example.flummox;

import android.content.res.Resources;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Rect;

import static com.example.flummox.GameView.screenRatioX;
import static com.example.flummox.GameView.screenRatioY;

public class Idle {
    public boolean isGoingUp = false;
    int x, y, width, height, idleCounter = 0;
    Bitmap idle1, idle2, dead3;

    Idle(int screenY, Resources res)
    {
        idle1 = BitmapFactory.decodeResource(res, R.drawable.idle1);
        idle2 = BitmapFactory.decodeResource(res, R.drawable.idle2);

        width = idle1.getWidth();
        height = idle1.getHeight();

        width /= 4;
        height /= 4;

        width = (int)(width * screenRatioX);
        height = (int)(height * screenRatioY);

        idle1 = Bitmap.createScaledBitmap(idle1, width, height, false);
        idle2 = Bitmap.createScaledBitmap(idle2, width, height, false);

        dead3 = BitmapFactory.decodeResource(res, R.drawable.dead3);
        dead3 = Bitmap.createScaledBitmap(dead3, width, height, false);

        y = screenY / 2;
        x = (int)(64 * screenRatioX);
    }

    Bitmap getIdle()
    {
        if(idleCounter == 0)
        {
            idleCounter++;

            return idle1;
        }

        idleCounter--;

        return idle2;
    }

    Rect getCollisionShape()
    {
        return new Rect(x, y, x + width, y + height);
    }

    Bitmap getDead()
    {
        return dead3;
    }
}
