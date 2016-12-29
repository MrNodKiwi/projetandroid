package com.esiea.arnaud.beerdex;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.Color;
import android.hardware.Sensor;
import android.hardware.SensorEvent;
import android.hardware.SensorEventListener;
import android.hardware.SensorManager;
import android.util.AttributeSet;
import android.util.Log;
import android.view.SurfaceHolder;
import android.view.SurfaceView;

import com.esiea.arnaud.objects.Bubble;

import java.util.LinkedList;
import java.util.List;
import java.util.Random;

/**
 * Created by alabyre on 27/12/2016.
 */

public class BubbleBackground extends SurfaceView implements SurfaceHolder.Callback {

    private DrawThread dThread;
    private Bitmap[] bmps;
    private List<Bubble> bubbles;
    private List<Bubble> rBubbles;
    private Random random;

    public BubbleBackground(Context context){
        super(context);
        getHolder().addCallback(this);
        bubbles = new LinkedList<Bubble>();
        rBubbles = new LinkedList<Bubble>();
        init();
    }

    public BubbleBackground(Context context, AttributeSet attributeSet){
        super(context, attributeSet);
        getHolder().addCallback(this);
        bubbles = new LinkedList<Bubble>();
        rBubbles = new LinkedList<Bubble>();
        init();
    }

    //Init: get the drawable, init random, create all the bubbles
    private void init(){
        random = new Random();
        bmps = new Bitmap[3];
        bmps[0] = BitmapFactory.decodeResource(getResources(), R.drawable.yellow_bubble);
        bmps[1] = BitmapFactory.decodeResource(getResources(), R.drawable.dark_yellow_bubble);
        bmps[2] = BitmapFactory.decodeResource(getResources(), R.drawable.orange_bubble);
        while(bubbles.size() < 20){
            bubbles.add(new Bubble(bmps[random.nextInt(3)]));
        }
    }

    //Thread which draw the surfaceview
    class DrawThread extends Thread{

        private SurfaceHolder surfaceHolder;
        private boolean runFlag = false;
        private boolean firstTime = true;

        public DrawThread(SurfaceHolder surfaceHolder){
            this.surfaceHolder = surfaceHolder;
        }

        public void setRunning(boolean run){
            this.runFlag = run;
        }

        @Override
        public void run(){

            while(this.runFlag){
                Canvas canvas = getHolder().lockCanvas();

                if(canvas != null){
                    synchronized (this.surfaceHolder){
                        if(firstTime) {
                            for(Bubble bubble: bubbles) {
                                bubble.setDimension(canvas.getWidth(), canvas.getHeight());
                            }
                            firstTime = false;
                        }
                        doDraw(canvas);
                    }
                    getHolder().unlockCanvasAndPost(canvas);
                }

                try {
                    sleep(30);
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
            }

        }
    }

    // Creation of the surfaceview
    @Override
    public void surfaceCreated(SurfaceHolder holder) {
        dThread = new DrawThread(getHolder());
        dThread.setRunning(true);
        dThread.start();
    }

    @Override
    public void surfaceChanged(SurfaceHolder holder, int format, int width, int height) {}

    // When the surfaceview is destroy
    @Override
    public void surfaceDestroyed(SurfaceHolder holder) {
        boolean retry = true;
        dThread.setRunning(false);
        while(retry){
            try{
                dThread.join();
                retry = false;
            } catch (InterruptedException e){}
        }
    }

    // Draw the surfaceview
    public void doDraw(Canvas canvas){
        // Draw the background Color
        canvas.drawColor(Color.rgb(244,229,66));
        // Make all the bubbles move, and when they are off the screen add them to the rBubbles list
        for(Bubble bubble: bubbles) {
            bubble.move();
            if (bubble.getY() < -bubble.getBmpHeight() ){
                rBubbles.add(bubble);
            }
            canvas.drawBitmap(bubble.getBmp(), bubble.getX(), bubble.getY(), null);
        }
        //Destroy the bubbles in the rBubbles List
        for(Bubble bubble: rBubbles){
            bubbles.remove(bubble);
            Bubble newBubble = new Bubble(bmps[random.nextInt(3)]);
            newBubble.setDimension(canvas.getWidth(), canvas.getHeight());
            bubbles.add(newBubble);
        }
        //Clear the rBubbles list
        rBubbles.clear();
    }

    public List<Bubble> getBubbles(){
        return bubbles;
    }
}
