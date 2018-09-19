package com.srg.Demo;

import java.awt.image.BufferedImage;

public abstract class FlyObject {
    int width;
    int height;
    public int x;
    public int y;
    public BufferedImage image;

    public abstract void move();

    public boolean shootBy(Bullet bullet) {
        int x = bullet.x;
        int y = bullet.y;
        return x > this.x - bullet.width && x < this.x + width &&
                y > this.y - bullet.height && y < this.y + height;
    }

    public abstract boolean isOutBound();
}
