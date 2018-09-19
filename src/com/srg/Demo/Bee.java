package com.srg.Demo;

import com.srg.Interface.Award;
import com.srg.Main;

import java.util.Random;

public class Bee extends FlyObject implements Award {

    private int xspeed;
    private int yspeed;
    private int awardType;

    public Bee() {
        yspeed = 1;
        yspeed = 2;
        image = Main.bee;

        width = image.getWidth();
        height = image.getHeight();
        y = -height;
        Random rd = new Random();
        x = rd.nextInt(Main.WIDTH - width);
        awardType = rd.nextInt(2);
    }

    @Override
    public void move() {
        x += xspeed;
        y += yspeed;

        if (x > Main.WIDTH - width) {
            xspeed = -1;
        }
        if (x < 0) {
            xspeed = 1;
        }
    }

    @Override
    public int getType() {
        return awardType;
    }

    @Override
    public boolean isOutBound(){
        return y > Main.HEIGHT;
    }
}
