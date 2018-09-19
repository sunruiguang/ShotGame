package com.srg.Demo;

import com.srg.Main;

public class Bullet extends FlyObject {

    private int speed;

    Bullet(int x, int y) {
        speed = 10;
        this.x = x;
        this.y = y;

        image = Main.bullet;
        width = image.getWidth();
        height = image.getHeight();
    }

    @Override
    public void move() {
        y -= speed;
    }

    @Override
    public boolean isOutBound(){
        return y < -height || x < -width;
    }
}
