package com.dragonstory;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Matrix;

import java.util.List;

/**
 * Created by user on 16/10/12.
 */
public class Character {
    public int Xspeed;
    public int Yspeed;
    public int direction;
    public int x;
    public int y;
    public int width;
    public int height;
    public List<Floor> floorList;
    public Context context;

    final static private int MAX_Y_SPEED = 2;
    final static private int MAX_X_SPEED = 10;
    final static private int MIN_X_SPEED = -4;
    final static private int X_DECAY_STEP = 2; // speed -= step for every update

    // character bitmap
    public Bitmap characterBitmap;
    public int bitmapStatus;

    public boolean isDead;
    //public Matrix bitmapMatrix;

    Character(Context context) {
        this.context = context;
        init();
    }

    public void init() {
        x = 10;
        y = 2;
        width = 12;
        height = 7;
        Xspeed = 0;
        Yspeed = 0;
        direction = 1;

        bitmapStatus = 0;
        isDead = false;
        setCharacterBitmap();
    }

    public void setFloorList(List<Floor> floorList) {
        this.floorList = floorList;
    }

    public void jump() {
        Xspeed = MAX_X_SPEED;
    }

    /* update the positon of character each frame */
    public void update() {
        //dead update
        if (isDead) {
            bitmapStatus = 3; // dead character bitmap
            setCharacterBitmap();
            x += 3;
            return;
        }

        // character is no dead,  update its position
        int xStand = 0;
        for (Floor floor:floorList) {
            if (floor.xMin <= x && floor.xMin > xStand) {
                if (y + width / 2 >= floor.yMin && y + width / 2 <= floor.yMax)  xStand = (int)floor.xMin;
            }
        }

        x += Xspeed;
        y += direction * Yspeed;
        if (x < xStand) x = xStand;
        if (x > 110) x = 110;
        if (y < 0) y = 0;
        if (y > 94) y = 94;

        // shrink the Yspeed && Xspeed
        //if (Yspeed > 0) Yspeed--;
        if (Xspeed > MIN_X_SPEED) Xspeed -= X_DECAY_STEP;

        // update bitmap status
        if (Yspeed != 0)
            bitmapStatus = (bitmapStatus+1) % 3;

        setCharacterBitmap();
    }

    /* handle move event */
    public void move() {
        Yspeed = MAX_Y_SPEED;
        //Log.d("debug", "move");
    }

    /* handle right event */
    public void right() {
        if (direction != 1)
            Yspeed = 0;

        direction = 1;
        //Log.d("debug", "right");
    }

    /* handle left event */
    public void left() {
        if (direction != -1)
            Yspeed = 0;

        direction = -1;
        //Log.d("debug", "left");
    }

    /* character dead */
    public void dead() {
        isDead = true;
    }

    public void setCharacterBitmap() {
        switch (bitmapStatus) {
            case 0:
                this.characterBitmap = BitmapFactory.decodeResource(context.getResources(), R.drawable.character_0);
                break;
            case 1:
                this.characterBitmap = BitmapFactory.decodeResource(context.getResources(), R.drawable.character_1);
                break;
            case 2:
                this.characterBitmap = BitmapFactory.decodeResource(context.getResources(), R.drawable.character_2);
                break;
            case 3:
                this.characterBitmap = BitmapFactory.decodeResource(context.getResources(), R.drawable.death_2);
                break;
            default:
                this.characterBitmap = BitmapFactory.decodeResource(context.getResources(), R.drawable.character_0);
                break;
        }

        // flip the bitmap
        if (direction == 1) {
            Matrix matrix = new Matrix();
            matrix.postScale(1,-1);
            characterBitmap = Bitmap.createBitmap(characterBitmap, 0, 0,
                    characterBitmap.getWidth(), characterBitmap.getHeight(), matrix, true);
        }
    }

}
