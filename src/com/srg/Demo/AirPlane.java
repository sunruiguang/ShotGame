package com.srg.Demo;

import com.srg.Interface.Enemy;
import com.srg.Main;

import java.util.Random;

public class AirPlane extends FlyObject implements Enemy {
    private int speed;

    public AirPlane() {
        speed = 2;
        image = Main.airPlane;
        width = image.getWidth();
        height = image.getHeight();
        y = -height;

        Random rd = new Random();
        x = rd.nextInt(Main.WIDTH - width);
    }

    @Override
    public int getScore() {
        return 1;
    }

    @Override
    public void move() {
        y += speed;
    }

    @Override
    public boolean isOutBound(){
        return y > Main.HEIGHT;
    }
}
