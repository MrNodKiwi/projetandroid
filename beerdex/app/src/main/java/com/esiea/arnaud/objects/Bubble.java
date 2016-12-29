package com.esiea.arnaud.objects;

import android.content.Context;
import android.graphics.Bitmap;
import android.util.Log;
import android.view.SurfaceView;
import android.widget.Toast;

import java.util.Random;

/**
 * Created by alabyre on 21/12/2016.
 */

public class Bubble {

    private Bitmap bmp;
    private int xPos;
    private int yPos;
    private int bmpWidth;
    private int bmpHeight;
    private int maxWidth;
    private int maxHeight;
    private int speed;
    private int size;

    private Random random;

    public Bubble(Bitmap bmp){
        random = new Random();
        size = random.nextInt(20) + 10;
        this.bmp = Bitmap.createScaledBitmap(bmp, bmp.getWidth()/size, bmp.getHeight()/size, false);
        bmpWidth = this.bmp.getWidth();
        bmpHeight = this.bmp.getHeight();
        yPos = 0;
        xPos = 0;
        speed = random.nextInt(10) + 3;
    }

    // Set dimension of the screen as an attribute off the bubble, could use Dimension in futur
    public void setDimension(int width, int height){
        this.maxWidth = width;
        this.maxHeight = height;
        yPos = height;
        xPos = random.nextInt(width - bmpWidth) + bmpWidth;
    }

    // Move the bubble
    public void move(){
        yPos-=speed;
        xPos+= random.nextInt(5) * (random.nextInt(1)-1);
        if(xPos < -bmpWidth-1){
            xPos+=maxWidth+bmpWidth;
        }
        if(xPos > maxWidth + bmpWidth+1){
            xPos= -bmpWidth;
        }
    }

    // Getter
    public Bitmap getBmp(){
        return bmp;
    }

    public int getX(){
        return xPos;
    }

    public int getY(){
        return yPos;
    }

    public int getMaxHeight() {
        return maxHeight;
    }

    public int getMaxWidth() {
        return maxWidth;
    }

    public int getBmpHeight() {
        return bmpHeight;
    }

    public int getBmpWidth() {
        return bmpWidth;
    }
}
